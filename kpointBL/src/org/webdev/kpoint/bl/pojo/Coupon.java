package org.webdev.kpoint.bl.pojo;

import java.util.Calendar;
import java.util.Set;

public class Coupon implements Comparable{

	private int couponId;
	private String title;
	private String description;
	private String imageUrl;
	private Calendar expiryDate;
	private Calendar distributionStartDate;
	private Calendar distributionEndDate;
	private boolean alwaysShowCoupon;
	private Region region;
	private Organization organization;
	private KinekPoint kinekPoint;
	
	private Set<MessageTrigger> messageTriggers;
	
	public int getCouponId() {
		return couponId;
	}
	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Calendar getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Calendar expiryDate) {
		this.expiryDate = expiryDate;
	}
	public Calendar getDistributionStartDate() {
		return distributionStartDate;
	}
	public void setDistributionStartDate(Calendar distributionStartDate) {
		this.distributionStartDate = distributionStartDate;
	}
	public Calendar getDistributionEndDate() {
		return distributionEndDate;
	}
	public void setDistributionEndDate(Calendar distributionEndDate) {
		this.distributionEndDate = distributionEndDate;
	}
	public boolean isAlwaysShowCoupon() {
		return alwaysShowCoupon;
	}
	public void setAlwaysShowCoupon(boolean alwaysShowCoupon) {
		this.alwaysShowCoupon = alwaysShowCoupon;
	}
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	public KinekPoint getKinekPoint() {
		return kinekPoint;
	}
	public void setKinekPoint(KinekPoint kinekPoint) {
		this.kinekPoint = kinekPoint;
	}
	
	public Set<MessageTrigger> getMessageTriggers(){
		return messageTriggers;
	}
	public void setMessageTriggers(Set<MessageTrigger> messageTriggers){
		this.messageTriggers = messageTriggers;
	}
	@Override
	public int compareTo(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof Coupon){			
			Coupon coupon = (Coupon) obj;
			if(this.getExpiryDate().after(coupon.getExpiryDate()))
				return 1;
			else if(this.getExpiryDate().before(coupon.getExpiryDate()))
				return -1;
		}
		
		return 0;
	}
	public void setImageUrl(String imgUrl) {
		this.imageUrl = imgUrl;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	
}
