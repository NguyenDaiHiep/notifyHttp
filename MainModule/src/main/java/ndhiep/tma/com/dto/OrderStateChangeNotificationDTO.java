package ndhiep.tma.com.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ndhiep.tma.com.common.CustomDateDeserializer;
import ndhiep.tma.com.common.CustomDateSerializer2;
import ndhiep.tma.com.entity.OrderStateChangeNotification;
import ndhiep.tma.com.entity.ProductOrder;


public class OrderStateChangeNotificationDTO {
	private String eventId;
	
	@JsonDeserialize(using = CustomDateDeserializer.class)
	@JsonSerialize(using = CustomDateSerializer2.class)
	private Date eventTime;
	private String eventType;
	private ProductOrder event;
	
	public OrderStateChangeNotificationDTO() {
	}

	public OrderStateChangeNotificationDTO(String eventId, Date eventTime, String eventType, ProductOrder event) {
		this.eventId = eventId;
		this.eventTime = eventTime;
		this.eventType = eventType;
		this.event = event;
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
	  public OrderStateChangeNotification convertEntity() {
	        OrderStateChangeNotification entity = new OrderStateChangeNotification();
	        entity.setEventId(this.getEventId());
	        entity.setEventTime(this.getEventTime());
	        entity.setEventType(this.getEventType());
	        entity.setEvent(this.getEvent());
	        return entity;
	    }
	
	
	

}
