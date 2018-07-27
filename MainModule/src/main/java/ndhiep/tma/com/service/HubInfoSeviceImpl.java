package ndhiep.tma.com.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ndhiep.tma.com.common.CustomJsonValidation;
import ndhiep.tma.com.common.Utils;
import ndhiep.tma.com.dao.HubInfoRepository;
import ndhiep.tma.com.dto.HubInfoDTO;
import ndhiep.tma.com.entity.HubInfo;

@Service
public class HubInfoSeviceImpl implements HubInfoService{
	private static final Logger logger = LoggerFactory.getLogger(HubInfoSeviceImpl.class);

	
	@Autowired
	private HubInfoRepository hubInfoRepositoryDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<ArrayList<HubInfoDTO>> findAll() {
		ArrayList<HubInfo> listhub = (ArrayList<HubInfo>) hubInfoRepositoryDao.findAll();
		ArrayList<HubInfoDTO> listhubdto = new ArrayList<>();
		for (int i = 0; i < listhub.size(); i++) {
			listhubdto.add(listhub.get(i).convertDTO());
			
		}
		if(listhubdto.isEmpty()){
			logger.error("Fail to get object from json");
			return (ResponseEntity<ArrayList<HubInfoDTO>>) Utils.createResponse(HttpStatus.NOT_FOUND, null, null);
		}
		return (ResponseEntity<ArrayList<HubInfoDTO>>) Utils.createResponse(HttpStatus.OK,null, listhubdto);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<HubInfoDTO> findbyId(Long id) {
		HubInfo hub = hubInfoRepositoryDao.findOne(id);
		if(hub==null){
			logger.error("Fail to get object from json");
			return (ResponseEntity<HubInfoDTO>) Utils.createResponse(HttpStatus.NOT_FOUND, null, null);
		}
		return (ResponseEntity<HubInfoDTO>) Utils.createResponse(HttpStatus.OK, null, hub);
	}
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<HubInfoDTO> post(String json) {
		 try {
			 logger.debug("json data = {}", Utils.trimJsonData(json));
	     
	            String jsonSchemaPath = getClass().getClassLoader().getResource("jsonSchema/hubinfo.json").getPath();
	            boolean checkSchema = CustomJsonValidation.validate(jsonSchemaPath, json);
	            if (!checkSchema) {
	            	 logger.warn("Schema validate fail");
	                
	                return (ResponseEntity<HubInfoDTO>) Utils.createResponse(HttpStatus.BAD_REQUEST, null, null);
	            }

	            HubInfo hub = (HubInfo) Utils.getObjectFromJson(json, HubInfo.class);
	            if (hub == null) {
	            	 logger.error("Fail to get object from json");
	                
	                return (ResponseEntity<HubInfoDTO>) Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null);
	            }

	            // Add resource
	            HubInfo result = hubInfoRepositoryDao.save(hub);
	            if (result == null) {
	                // Add fail: Return 500
	            	logger.error("Cannot create HubInfo");
	                return (ResponseEntity<HubInfoDTO>) Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null);
	            }
	            return (ResponseEntity<HubInfoDTO>) Utils.createResponse(HttpStatus.CREATED, null, result.convertDTO());
	        } catch (Exception e) {
	        	   logger.error("Exception: ", e);
	            return (ResponseEntity<HubInfoDTO>) Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null);
	        }
	}
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<HubInfoDTO> delete(Long id) {
		try{
		if(!hubInfoRepositoryDao.exists(id)){
			logger.warn("id {} is not exist", id);
			return (ResponseEntity<HubInfoDTO>) Utils.createResponse(HttpStatus.NOT_FOUND, null, null);
		}
		hubInfoRepositoryDao.delete(id);
		return (ResponseEntity<HubInfoDTO>) Utils.createResponse(HttpStatus.OK, null, null);
		}catch(Exception e){
			logger.error("Exception: ", e);
			return (ResponseEntity<HubInfoDTO>) Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null);
		}
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<HubInfoDTO> put(Long id, String json) {
		try{
			logger.debug("id = {}, json data = {}", id, Utils.trimJsonData(json));
			  
			if(!hubInfoRepositoryDao.exists(id)){
				  logger.warn("id {} is not exist", id);
				return (ResponseEntity<HubInfoDTO>) Utils.createResponse(HttpStatus.NOT_FOUND, null, null);
			}
			String jsonSchema = getClass().getClassLoader().getResource("jsonSchema/custom.json").getPath();
			boolean checkSchema = CustomJsonValidation.validate(jsonSchema, json);
			 if (!checkSchema) {
				    logger.warn("Schema validate fail");
	                
	                return (ResponseEntity<HubInfoDTO>) Utils.createResponse(HttpStatus.BAD_REQUEST, null, null);
	            }
			 HubInfoDTO hubInfoDTO = (HubInfoDTO) Utils.getObjectFromJson(json, HubInfoDTO.class);
			 if(hubInfoDTO==null){
				  logger.error("Fail to get object from json");
				 return (ResponseEntity<HubInfoDTO>) Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null);
			 }
			 if (!id.equals(hubInfoDTO.getId())) {
				    logger.warn("id {} from param not match with id {} in json", id, hubInfoDTO.getId());
	                return (ResponseEntity<HubInfoDTO>) Utils.createResponse(HttpStatus.BAD_REQUEST, null, null);
	            }
			 //update
			 HubInfo hubInfo = hubInfoRepositoryDao.findOne(id);
			 HubInfo mergedObject = (HubInfo) Utils.mergeObject(hubInfo, hubInfoDTO, false);
			 if(mergedObject==null){
				    logger.error("Cannot merged object");
				 return (ResponseEntity<HubInfoDTO>) Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null);
				 
			 }
			 HubInfo result = hubInfoRepositoryDao.save(mergedObject);
			 if(result==null){
				  logger.error("Cannot update(PUT) HubInfo");
				 return (ResponseEntity<HubInfoDTO>) Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null);
				 
			 }
			 return (ResponseEntity<HubInfoDTO>) Utils.createResponse(HttpStatus.CREATED, null, result.convertDTO());
		}catch(Exception e){
			logger.error("Exception: ", e);
			return (ResponseEntity<HubInfoDTO>) Utils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR, null, null);
			
		}
		
	}

	

	

	
	

	

}
