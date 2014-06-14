package org.webdev.kpoint.bl.api.mapper.response;

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
		
	public Coupon(org.webdev.kpoint.bl.pojo.Coupon coupon) {
		couponId = coupon.getCouponId();
		title = coupon.getTitle();
		description = coupon.getDescription();
		imageUrl = coupon.getImageUrl();
		expiryDate = coupon.getExpiryDate();
		distributionStartDate = coupon.getDistributionStartDate();
		distributionEndDate = coupon.getDistributionEndDate();
	}
	
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
