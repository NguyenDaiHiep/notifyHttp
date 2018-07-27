package ndhiep.tma.com.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ndhiep.tma.com.dto.HubInfoDTO;
import ndhiep.tma.com.service.HubInfoService;

	@RestController
	@RequestMapping("/api/v1/hub")
	public class HubInfoController {
		
		@Autowired
		private HubInfoService hubInfoService;
		
		@RequestMapping(value = "/", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
		public ResponseEntity<ArrayList<HubInfoDTO>> getallHubinfo() {

			return hubInfoService.findAll();

		}
		@RequestMapping(value="/{id}", method = RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
		public ResponseEntity<HubInfoDTO> finHubinfoByid(@PathVariable("id") long id){
			return hubInfoService.findbyId(id);
			
		}
		@RequestMapping(value="/", method = RequestMethod.POST, produces={MediaType.APPLICATION_JSON_VALUE})
		public ResponseEntity<HubInfoDTO> post(@RequestBody String json){
			return hubInfoService.post(json);
			
		}
		@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
		public ResponseEntity<HubInfoDTO> delete(@PathVariable("id") Long id){
			return hubInfoService.delete(id);
			
			
		}
		@RequestMapping(value="/{id}", method =  RequestMethod.PUT)
		public ResponseEntity<HubInfoDTO> put(@RequestBody String json, @PathVariable("id") Long id){
			return hubInfoService.put(id, json);
			
		}
		
		

	}



