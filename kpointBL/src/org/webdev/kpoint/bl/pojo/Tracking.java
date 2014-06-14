package org.webdev.kpoint.bl.pojo;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.webdev.kpoint.bl.persistence.CourierDao;

public class Tracking {

	private String trackingNumber;
	private Integer id = -1;
	private String currentLocation;
	private String currentStatus;
	
	private Float weight;
	private String weightType;
	
	private Courier courier;
	private Calendar estimatedArrival;
	
	private Boolean isDelivered = false; 
	private transient Calendar createdDate;
	private transient Calendar lastModifiedDate;
	
	public Tracking(){
		GregorianCalendar now = new GregorianCalendar();
		createdDate = now;
		lastModifiedDate = now;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}	
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public String getCurrentLocation() {
		return currentLocation;
	}
	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public Calendar getEstimatedArrival() {
		return estimatedArrival;
	}
	public void setEstimatedArrival(Calendar estimatedArrival) {
		this.estimatedArrival = estimatedArrival;
	}
	public void setWeight(Float weight) {
		this.weight = weight;
	}
	public Float getWeight() {
		return weight;
	}
	public void setWeightType(String weightType) {
		this.weightType = weightType;
	}
	public String getWeightType() {
		return weightType;
	}
	public void setCourier(Courier courier) {
		this.courier = courier;
	}
	public Courier getCourier() {
		return courier;
	}
	public void setCourierById(int id) throws Exception{
		CourierDao courierDao = new CourierDao();	
		courier = courierDao.read(id);	
	}
	public void setIsDelivered(Boolean isDelivered) {
		this.isDelivered = isDelivered;
	}
	public Boolean getIsDelivered() {
		return isDelivered;
	}
	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}
	public Calendar getCreatedDate() {
		return createdDate;
	}
	public void setLastModifiedDate(Calendar lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public Calendar getLastModifiedDate() {
		return lastModifiedDate;
	}
}
