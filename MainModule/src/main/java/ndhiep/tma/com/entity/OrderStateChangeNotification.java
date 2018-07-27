package ndhiep.tma.com.entity;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ndhiep.tma.com.common.CustomDateDeserializer;
import ndhiep.tma.com.common.CustomDateSerializer2;
import ndhiep.tma.com.dto.OrderStateChangeNotificationDTO;


public class OrderStateChangeNotification {
	
	private String eventId;
	
	@JsonDeserialize(using = CustomDateDeserializer.class)
	@JsonSerialize(using = CustomDateSerializer2.class)
	private Date eventTime;
	private String eventType;
	private ProductOrder event;
	public OrderStateChangeNotification(String eventId, Date eventTime, String eventType, ProductOrder event) {
		this.eventId = eventId;
		this.eventTime = eventTime;
		this.eventType = eventType;
		this.event = event;
	}
	public OrderStateChangeNotification() {
		super();
	}
	public String getEventId() {
		return eventId;
	}
	public Date getEventTime() {
		return eventTime;
	}
	public String getEventType() {
		return eventType;
	}
	public ProductOrder getEvent() {
		return event;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public void setEvent(ProductOrder event) {
		this.event = event;
	}
	 public OrderStateChangeNotificationDTO convertDTO() {
	        OrderStateChangeNotificationDTO dto = new OrderStateChangeNotificationDTO();
	        dto.setEventId(this.getEventId());
	        dto.setEventTime(this.getEventTime());
	        dto.setEventType(this.getEventType());
	        dto.setEvent(this.getEvent());
	        return dto;
	    }
	
	

}
