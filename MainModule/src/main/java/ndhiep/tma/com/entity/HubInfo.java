package ndhiep.tma.com.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ndhiep.tma.com.dto.HubInfoDTO;

@Entity
@Table(name = "hubinfo")
public class HubInfo {
	
	

	@Id
	@Column(name="id", nullable =  true)
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private long id;
	
	@Column(name = "userid")
	private String userId;
	
	@Column(name = "callback")
	private String callback;
	
	@Column(name = "query")
	private String query;

	public HubInfo() {
	}

	public HubInfo(long id, String userId, String callback, String query) {
		this.id = id;
		this.userId = userId;
		this.callback = callback;
		this.query = query;

	}

	public long getId() {
		return id;
	}

	public String getUserId() {
		return userId;
	}

	public String getCallback() {
		return callback;
	}

	public String getQuery() {
		return query;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	public HubInfoDTO convertDTO(){
		HubInfoDTO hubDTO = new HubInfoDTO();
		hubDTO.setId(this.getId());
		hubDTO.setUserId(this.getUserId());
		hubDTO.setCallback(this.getCallback());
		hubDTO.setQuery(this.getQuery());
		return hubDTO;
	}

}
