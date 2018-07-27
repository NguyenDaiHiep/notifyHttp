package ndhiep.tma.com.service;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;

import ndhiep.tma.com.dto.ProductOrderDTO;

public interface ProductOrderService {
	public ResponseEntity<ArrayList<ProductOrderDTO>> findAll();
	public ResponseEntity<ProductOrderDTO> findbyId(String id);
	public ResponseEntity<ProductOrderDTO> post(String json,String header);
	public ResponseEntity<ProductOrderDTO> delete(String id);
	public ResponseEntity<ProductOrderDTO> put(String id, String json,String header);
	

}
