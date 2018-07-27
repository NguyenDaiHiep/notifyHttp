package ndhiep.tma.com.thread;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import ndhiep.tma.com.dao.HubInfoRepository;
import ndhiep.tma.com.entity.HubInfo;
import ndhiep.tma.com.entity.OrderStateChangeNotification;
import ndhiep.tma.com.entity.ProductOrder;

public class ThreadNotify implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(ThreadNotify.class);
	private ProductOrder productOrder;
	
	@Autowired
	private HubInfoRepository hubInfoRepository;

	public ThreadNotify() {
	}
	

	public ThreadNotify(HubInfoRepository hubInfoRepository, ProductOrder productOrder) {
		this.hubInfoRepository = hubInfoRepository;
		this.productOrder = productOrder;
	}


	public boolean checkSateInQuery(HubInfo hubInfo) {
		if (hubInfo.getQuery() == null) {
			logger.debug("query hubinfo is null");
			return false;
		} else {
			String[] arrayStatus = hubInfo.getQuery().split("=");
			if(arrayStatus[1].equals(productOrder.getState())) return true;
			
		}
		return false;

	}

	public void sendNotify(OrderStateChangeNotification orderState, String callback) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-ServiceProviderAuthorizationID", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<OrderStateChangeNotification> request = new HttpEntity<>(orderState, headers);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		ResponseEntity<OrderStateChangeNotification> order = restTemplate.exchange(callback, HttpMethod.POST, request,
				OrderStateChangeNotification.class);
		logger.info("sending post to listener................");

	}

	public String generateString() {
		return String.valueOf(LocalDate.now());
	}

	@Override
	public void run() {
		try {
			OrderStateChangeNotification orderState = new OrderStateChangeNotification();
			orderState.setEventId(generateString());
			orderState.setEventTime(new Date());
			orderState.setEventType("orderStateChangeNotification");
			orderState.setEvent(productOrder);
			ArrayList<HubInfo> arr = hubInfoRepository.findHubInfo();
			for (HubInfo hubInfos : arr) {
				if (checkSateInQuery(hubInfos) == true) {
					sendNotify(orderState, hubInfos.getCallback());
					logger.info("sending http post to listener................");
				}

			}

		} catch (

		Exception e) {
			logger.error("exception...");
			e.printStackTrace();
		}

	}
}
