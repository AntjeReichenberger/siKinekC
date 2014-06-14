package org.webdev.kpoint.bl.pojo;

public class CouponEmails {

	private int Id;
	private Coupon coupon;
	private MessageTrigger messageTrigger;
	
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public Coupon getCoupon() {
		return coupon;
	}
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}
	public MessageTrigger getMessageTrigger() {
		return messageTrigger;
	}
	public void setMessageTrigger(MessageTrigger messageTrigger) {
		this.messageTrigger = messageTrigger;
	}

	
}
