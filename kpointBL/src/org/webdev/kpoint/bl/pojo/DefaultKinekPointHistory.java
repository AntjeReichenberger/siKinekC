package org.webdev.kpoint.bl.pojo;

import java.util.Date;

public class DefaultKinekPointHistory {
	private int id;
	private int userId;
	private int kinekPointId;
	private Date createdDate;
	
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
	
	public int getKinekPointId() {
		return kinekPointId;
	}
	
	public void setKinekPointId(int kinekPointId) {
		this.kinekPointId = kinekPointId;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
