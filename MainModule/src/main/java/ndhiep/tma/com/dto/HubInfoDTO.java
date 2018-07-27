package ndhiep.tma.com.dto;
import ndhiep.tma.com.entity.HubInfo;

public class HubInfoDTO {

	private long id;
	private String userId;
	private String callback;
	private String query;
	
	public HubInfoDTO() {
	}
	public HubInfoDTO(long id, String userId, String callback, String query) {
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
	public HubInfo convertEntity(){
		HubInfo entity = new HubInfo();
		entity.setId(this.getId());
		entity.setUserId(this.getUserId());
		entity.setCallback(this.getCallback());
		entity.setQuery(this.getQuery());
		return entity;
	}
	

}
