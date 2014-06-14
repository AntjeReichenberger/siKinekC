package org.webdev.kpoint.bl.pojo;

import java.util.Date;
import java.util.Set;

public class Pickup {

	private Integer pickupId = -1;
	private String transactionId;
	private Date pickupDate;
	private KinekPoint kinekPoint;
	private User consumer;
	private Set<Package> packages;
	private int userId;
	private String app;
	
	public Pickup(){
	}
	
	public Pickup(Integer pickupId){
		this.pickupId = pickupId;
	}
	
	public Integer getPickupId() {
		return pickupId;
	}
	public void setPickupId(Integer pickupId) {
		this.pickupId = pickupId;
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	public Date getPickupDate() {
		return pickupDate;
	}
	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}
	
	public KinekPoint getKinekPoint() {
		return kinekPoint;
	}
	public void setKinekPoint(KinekPoint kinekPoint) {
		this.kinekPoint = kinekPoint;
	}
	
	public User getConsumer() {
		return consumer;
	}
	public void setConsumer(User consumer) {
		this.consumer = consumer;
	}
	
	public Set<Package> getPackages() {
		return packages;
	}
	public void setPackages(Set<Package> packages) {
		this.packages = packages;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getUserId() {
		return userId;
	}

	public void setApp(String app) {
		this.app = app;
	}
	public String getApp() {
		return app;
	}
}