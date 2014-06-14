package org.webdev.kpoint.bl.pojo;

import java.io.Serializable;
import java.util.Calendar;

public class KinekPointProspectLocation implements Serializable {
	private static final long serialVersionUID = -1770997745746540514L;
	private int id = -1;
	private String city;
	private State state;
	private String zip;
	private User recommendedByUser; 
	//if the KP is recommended by a user that is not a KP consumer, they can enter an
	//email address to be notified when the KP is available
	private String notifyEmailAddress;
	private Calendar createdDate;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public String getZip() {
		return zip;
	}
	
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	public Calendar getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}

	public User getRecommendedByUser() {
		return recommendedByUser;
	}

	public void setRecommendedByUser(User recommendedByUser) {
		this.recommendedByUser = recommendedByUser;
	}
	
	public String getNotifyEmailAddress() {
		return notifyEmailAddress;
	}
	
	public void setNotifyEmailAddress(String notifyEmailAddress) {
		this.notifyEmailAddress = notifyEmailAddress;
	}
}
