package ndhiep.tma.com.service;

import java.util.ArrayList;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ndhiep.tma.com.common.CustomJsonValidation;
import ndhiep.tma.com.common.Utils;
import ndhiep.tma.com.dao.HubInfoRepository;
import ndhiep.tma.com.dao.ProductOrderRepository;
import ndhiep.tma.com.dto.ProductOrderDTO;
import ndhiep.tma.com.entity.ProductOrder;
import ndhiep.tma.com.thread.ThreadNotify;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {
	private static final Logger logger = LoggerFactory.getLogger(ProductOrderServiceImpl.class);

	@Autowired
	private HubInfoRepository hubInfoRepository;

	@Autowired
	private ProductOrderRepository productOrderRepository;

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<ArrayList<ProductOrderDTO>> findAll() {
		ArrayList<ProductOrder> listpro = (ArrayList<ProductOrder>) productOrderRepository.findAll();
		ArrayList<ProductOrderDTO> listProDTO = new ArrayList<>();
		for (int i = 0; i < listpro.size(); i++) {
			listProDTO.add(listpro.get(i).convertProDTO());

		}
		if (listProDTO.isEmpty()) {
			logger.error("Fail to get object from json");
			return (ResponseEntity<ArrayList<ProductOrderDTO>>) Utils.createResponse(HttpStatus.NOT_FOUND, null, null);
		}
		return (ResponseEntity<ArrayList<ProductOrderDTO>>) Utils.createResponse(HttpStatus.OK, null, listProDTO);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<ProductOrderDTO> findbyId(String id) {
		ProductOrder pro = productOrderRepository.findOne(id);
		if (pro == null) {
			logger.error("Fail to get object from json");
			return (ResponseEntity<ProductOrderDTO>) Utils.createResponse(HttpStatus.NOT_FOUND, null, null);
		}
		return (ResponseEntity<ProductOrderDTO>) Utils.createResponse(HttpStatus.OK, null, pro);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<ProductOrderDTO> post(String json, String userId) {
		try {
			logger.debug("json data = {}", Utils.trimJsonData(json));
			String jsonProductPost = getClass().getClassLoader().getResource("jsonSchema/ProductOrderPost.json").getPath();
			boolean checkSchema = CustomJsonValidation.validate(jsonProductPost, json);
			if (!checkSchema) {
				logger.warn("Schema validate fail");

				return (ResponseEntity<ProductOrderDTO>) Utils.createResponse(HttpStatus.BAD_REQUEST, null, null);
			}

			ProductOrder pro = (ProductOrder) Utils.getObjectFromJson(json, ProductOrder.class);
			if (pro == null) {
				logger.error("Fail to get object from json");
				return (ResponseEntity<ProductOrderDTO>) Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, null,
						null);
			}
			// id
			Random rd = new Random();
			int a = rd.nextInt(100);
			pro.setId(String.valueOf(a));

			// Add resource
			ProductOrder result = productOrderRepository.save(pro);
			if (result == null) {
				logger.error("Cannot create ISO8601");
				// Add fail: Return 500
				return (ResponseEntity<ProductOrderDTO>) Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, null,
						null);
			}

			return (ResponseEntity<ProductOrderDTO>) Utils.createResponse(HttpStatus.CREATED, null,
					result.convertProDTO());
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return (ResponseEntity<ProductOrderDTO>) Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<ProductOrderDTO> delete(String id) {
		try {
			if (!productOrderRepository.exists(id)) {
				logger.error("id is not exits");
				return (ResponseEntity<ProductOrderDTO>) Utils.createResponse(HttpStatus.NOT_FOUND, null, null);
			}
			productOrderRepository.delete(id);
			return (ResponseEntity<ProductOrderDTO>) Utils.createResponse(HttpStatus.OK, null, null);
		} catch (Exception e) {
			logger.error("Exception: ", e);
			return (ResponseEntity<ProductOrderDTO>) Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<ProductOrderDTO> put(String id, String json, String userId) {
		try {
			logger.debug("id = {}, json data = {}", id, Utils.trimJsonData(json));
			if (!productOrderRepository.exists(id)) {
				logger.warn("id {} is not exist", id);
				return (ResponseEntity<ProductOrderDTO>) Utils.createResponse(HttpStatus.NOT_FOUND, null, null);
			}
			String jsonProduct = getClass().getClassLoader().getResource("jsonSchema/ProductOrderPut.json").getPath();
			boolean checkSchema = CustomJsonValidation.validate(jsonProduct, json);
			if (!checkSchema) {
				logger.warn("Schema validate fail");

				return (ResponseEntity<ProductOrderDTO>) Utils.createResponse(HttpStatus.BAD_REQUEST, null, null);
			}
			ProductOrderDTO productDTO = (ProductOrderDTO) Utils.getObjectFromJson(json, ProductOrderDTO.class);
			if (productDTO == null) {
				logger.error("Fail to get object from json");
				return (ResponseEntity<ProductOrderDTO>) Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, null,
						null);
			}
			if (!productDTO.getId().equals(id)) {
				logger.warn("id {} from param not match with id {} in json", id, productDTO.getId());
				return (ResponseEntity<ProductOrderDTO>) Utils.createResponse(HttpStatus.BAD_REQUEST, null, null);
			}
			// update
			ProductOrder product = productOrderRepository.findOne(id);
			String oldState = product.getState();
			ProductOrder mergedObject = (ProductOrder) Utils.mergeObject(product, productDTO, false);
			if (mergedObject == null) {
				logger.error("Cannot merged object");
				return (ResponseEntity<ProductOrderDTO>) Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, null,
						null);

			}
			ProductOrder result = productOrderRepository.save(mergedObject);
			if (result == null) {
				logger.error("Cannot update(PUT) ISO8601");
				return (ResponseEntity<ProductOrderDTO>) Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, null,
						null);

			}
			// notify
			if (!result.getState().equals(oldState)) {
				ThreadNotify thread = new ThreadNotify(hubInfoRepository,result);		
				thread.run();
				logger.info("request http post success..........................");

			}

			return (ResponseEntity<ProductOrderDTO>) Utils.createResponse(HttpStatus.CREATED, null,
					result.convertProDTO());

		} catch (Exception e) {
			logger.error("Exception: ", e);
			return (ResponseEntity<ProductOrderDTO>) Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null);

		}

	}
}