package org.webdev.kpoint.bl.pojo;

import java.util.Calendar;

public class ConsumerCredit extends Credit {
	private User user;
	private Pickup pickup;
	private Calendar expiryNotificationDate;

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Pickup getPickup() {
		return pickup;
	}
	
	public void setPickup(Pickup pickup) {
		this.pickup = pickup;
	}
	
	public Calendar getExpiryNotificationDate() {
		return expiryNotificationDate;
	}
	
	public void setExpiryNotificationDate(Calendar expiryNotificationDate) {
		this.expiryNotificationDate = expiryNotificationDate;
	}

}