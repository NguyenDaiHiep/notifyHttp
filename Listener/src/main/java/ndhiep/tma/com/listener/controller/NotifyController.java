package ndhiep.tma.com.listener.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import ndhiep.tma.com.listener.common.ListenerJsonValidation;
import ndhiep.tma.com.listener.common.Utils;


@RestController
@RequestMapping(value="/api/v1/listener")
public class NotifyController {
	private static final Logger logger = LoggerFactory.getLogger(NotifyController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<String> notification(@ RequestBody String json) {
			try {
				logger.debug("json data = {}", Utils.trimJsonData(json));
				String jsonProductPost = getClass().getClassLoader().getResource("JsonSchema/notify.json").getPath();
				boolean checkSchema = ListenerJsonValidation.validate(jsonProductPost, json);
				if (!checkSchema) {
					logger.warn("Schema validate fail");

				}
				logger.info("sent post from listener to successssssssssssssssssssssss..........");
		        return new ResponseEntity<>(HttpStatus.CREATED);
			}catch (Exception e) {
				logger.error("Exception: ", e);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			
    }

}
