package org.webdev.kpoint.bl.pojo;

public class DeviceToken {
	private int id;
	private int userId;
	private String token;
	private String targetDeviceType;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	public void setTargetDeviceType(String targetDeviceType) {
		this.targetDeviceType = targetDeviceType;
	}

	public String getTargetDeviceType() {
		return targetDeviceType;
	}
	
}
