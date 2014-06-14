package org.webdev.kpoint.bl.api.mapper.request;

import java.io.Serializable;

public class Tracking implements Serializable {

	private static final long serialVersionUID = -2268091928249244707L;
	
	private String trackingNumber;
	private String packageNickname;
	private String courierCode;

	public String getTrackingNumber() {
		return trackingNumber;
	}
	
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	
	public String getPackageNickname() {
		return packageNickname;
	}
	
	public void setPackageNickname(String packageNickname) {
		this.packageNickname = packageNickname;
	}

	public void setCourierCode(String courierCode) {
		this.courierCode = courierCode;
	}

	public String getCourierCode() {
		return courierCode;
	}
	
}
