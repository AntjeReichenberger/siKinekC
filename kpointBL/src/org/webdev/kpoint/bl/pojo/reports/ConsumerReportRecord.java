package org.webdev.kpoint.bl.pojo.reports;

import java.util.Calendar;


public class ConsumerReportRecord {
	private String firstName;
	private String lastName;
	private String kinekNumber;
	private Calendar firstPickupDate;
	private String firstPickupLocation;
	private Calendar lastPickupDate;
	private String lastPickupLocation;
	private Integer totalPickupCount;
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getKinekNumber() {
		return kinekNumber;
	}
	
	public void setKinekNumber(String kinekNumber) {
		this.kinekNumber = kinekNumber;
	}
	
	public Calendar getFirstPickupDate() {
		return firstPickupDate;
	}
	
	public void setFirstPickupDate(Calendar firstPickupDate) {
		this.firstPickupDate = firstPickupDate;
	}
	
	public String getFirstPickupLocation() {
		return firstPickupLocation;
	}
	
	public void setFirstPickupLocation(String firstPickupLocation) {
		this.firstPickupLocation = firstPickupLocation;
	}
	
	public Calendar getLastPickupDate() {
		return lastPickupDate;
	}
	
	public void setLastPickupDate(Calendar lastPickupDate) {
		this.lastPickupDate = lastPickupDate;
	}
	
	public String getLastPickupLocation() {
		return lastPickupLocation;
	}
	
	public void setLastPickupLocation(String lastPickupLocation) {
		this.lastPickupLocation = lastPickupLocation;
	}
	
	public Integer getTotalPickupCount() {
		return totalPickupCount;
	}
	
	public void setTotalPickupCount(Integer totalPickupCount) {
		this.totalPickupCount = totalPickupCount;
	}
	
}
