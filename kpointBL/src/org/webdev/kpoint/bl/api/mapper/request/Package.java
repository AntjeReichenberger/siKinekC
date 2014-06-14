package org.webdev.kpoint.bl.api.mapper.request;

import java.io.Serializable;
import java.math.BigDecimal;

public class Package implements Serializable {

	private static final long serialVersionUID = -2268091928249244707L;
	
	private int packageId = -1;
	private String status;
	private String courierCode;
	private BigDecimal dutyAndTax = new BigDecimal(0.0);
	private String customInfo;
	private String shippedFrom;
	private int weightId = -1;
	private boolean onSkid = false;
	
	public int getPackageId() {
		return packageId;
	}
	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public BigDecimal getDutyAndTax() {
		return dutyAndTax;
	}
	public void setDutyAndTax(BigDecimal dutyAndTax) {
		this.dutyAndTax = dutyAndTax;
	}
	
	public String getCustomInfo() {
		return customInfo;
	}
	public void setCustomInfo(String customInfo) {
		this.customInfo = customInfo;
	}
	
	public String getShippedFrom() {
		return shippedFrom;
	}
	public void setShippedFrom(String shippedFrom) {
		this.shippedFrom = shippedFrom;
	}
	
	public int getWeightId() {
		return weightId;
	}
	public void setWeightId(int weightId) {
		this.weightId = weightId;
	}
	
	public boolean isOnSkid() {
		return onSkid;
	}
	public void setOnSkid(boolean onSkid) {
		this.onSkid = onSkid;
	}
	
	public void setCourierCode(String courierCode) {
		this.courierCode = courierCode;
	}
	public String getCourierCode() {
		return courierCode;
	}
	
}
