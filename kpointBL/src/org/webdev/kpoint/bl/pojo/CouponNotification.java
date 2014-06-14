package org.webdev.kpoint.bl.pojo;

import java.util.Calendar;

public class CouponNotification {

	private int id;
	private Coupon coupon;
	private User user;
	private Calendar receivedDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Coupon getCoupon() {
		return coupon;
	}
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Calendar getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Calendar receivedDate) {
		this.receivedDate = receivedDate;
	}
	
	
	
}
