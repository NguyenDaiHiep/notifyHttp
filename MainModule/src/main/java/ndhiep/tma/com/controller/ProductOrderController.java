package ndhiep.tma.com.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ndhiep.tma.com.dto.ProductOrderDTO;
import ndhiep.tma.com.service.ProductOrderService;

@RestController
@RequestMapping("/api/v1/order")
public class ProductOrderController {
	@Autowired
	private ProductOrderService productOderService;

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ArrayList<ProductOrderDTO>> getallProduct() {
		return productOderService.findAll();

	}

	@RequestMapping(value = ("/{id}"), method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ProductOrderDTO> finProductOderById(@PathVariable("id") String id) {
		return productOderService.findbyId(id);

	}

	@RequestMapping(value = "/", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ProductOrderDTO> post(@RequestBody String json, HttpServletRequest request){
		String userId = request.getHeader("X-ServiceProviderAuthorizationID:");
		return productOderService.post(json,userId);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ProductOrderDTO> delete(@PathVariable("id") String id) {
		return productOderService.delete(id);

	}
	@RequestMapping(value="/{id}", method =  RequestMethod.PUT)
	public ResponseEntity<ProductOrderDTO> put(@RequestBody String json, @PathVariable("id") String id, HttpServletRequest request){
		 String header = request.getHeader("X-ServiceProviderAuthorizationID:");
		return productOderService.put(id, json,header);
		
	}
}
