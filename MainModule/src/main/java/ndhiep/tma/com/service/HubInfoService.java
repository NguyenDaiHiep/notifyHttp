package ndhiep.tma.com.service;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;

import ndhiep.tma.com.dto.HubInfoDTO;

public interface HubInfoService {
	public ResponseEntity<ArrayList<HubInfoDTO>> findAll();
	public ResponseEntity<HubInfoDTO> findbyId(Long id);
	public ResponseEntity<HubInfoDTO> post(String json);
	public ResponseEntity<HubInfoDTO> delete(Long id);
	public ResponseEntity<HubInfoDTO> put(Long id,String json);

}
