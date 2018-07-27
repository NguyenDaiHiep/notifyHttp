package ndhiep.tma.com.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ndhiep.tma.com.dto.ProductOrderDTO;

@Entity
@Table(name= "product_order")
public class ProductOrder {
	@Id
	@Column(name = "id", nullable= false)
	private String id;
	
	@Column(name= "description")
	private String description;
	
	@Column(name="state")
	private String state;
	
	@Column(name="orderdate")
	private Date order_date;
	
	@Column(name= "modifydate")
	private Date modify_date;
	
	public ProductOrder() {
	}
	public ProductOrder(String id, String description, String state, Date order_date, Date modify_date) {
		super();
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
	public ProductOrderDTO convertProDTO(){
		ProductOrderDTO proDTO = new ProductOrderDTO();
		proDTO.setId(this.getId());
		proDTO.setDescription(this.getDescription());
		proDTO.setState(this.getState());
		proDTO.setOrder_date(this.getOrder_date());
		proDTO.setModify_date(this.getModify_date());
		return proDTO;
	}
	
	

}
