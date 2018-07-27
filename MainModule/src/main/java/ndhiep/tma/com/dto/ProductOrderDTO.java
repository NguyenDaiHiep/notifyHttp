package ndhiep.tma.com.dto;

import java.util.Date;

import ndhiep.tma.com.entity.ProductOrder;

public class ProductOrderDTO {
	private String id;
	private String description;
	private String state;
	private Date order_date;
	private Date modify_date;

	public ProductOrderDTO() {
	}

	public ProductOrderDTO(String id, String description, String state, Date order_date, Date modify_date) {
		this.id = id;
		this.description = description;
		this.state = state;
		this.order_date = order_date;
		this.modify_date = modify_date;
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public String getState() {
		return state;
	}

	public Date getOrder_date() {
		return order_date;
	}

	public Date getModify_date() {
		return modify_date;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setOrder_date(Date order_date) {
		this.order_date = order_date;
	}

	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
	}
	public ProductOrder convertPro(){
		ProductOrder pro = new ProductOrder();
		pro.setId(this.getId());
		pro.setDescription(this.getDescription());
		pro.setOrder_date(this.getOrder_date());
		pro.setModify_date(this.getModify_date());
		return pro;
	}
	
	
}

