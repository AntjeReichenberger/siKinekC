package org.webdev.kpoint.bl.api.mapper.response;

import java.math.BigDecimal;
import java.util.Calendar;

import org.webdev.kpoint.bl.api.mapper.response.kinekpoint.PackageWeightGroup;

public class Package {
	private int packageId;
	private Courier courier;
	private String customInfo;
	private BigDecimal dutyAndTax;
	// private BigDecimal kinekFee;
	private String shippedFrom;
	private Calendar pickupDate;
	private boolean onSkid;
	private BigDecimal pickupFee;
	private BigDecimal extendedDurationFee;
	private BigDecimal skidFee;
	private PackageWeightGroup packageWeightGroup;
	

	public Package(org.webdev.kpoint.bl.pojo.Package blPackage) {
		packageId = blPackage.getPackageId();
		if (blPackage.getCourier() != null) {
			courier = new Courier(blPackage.getCourier());
		}
		customInfo = blPackage.getCustomInfo();
		dutyAndTax = blPackage.getDutyAndTax();
		packageWeightGroup = new PackageWeightGroup(blPackage.getPackageWeightGroup());
		shippedFrom = blPackage.getShippedFrom();
		pickupDate = blPackage.getPickupDate();
		onSkid = blPackage.getOnSkid();
		pickupFee = blPackage.getPickupFee();
		extendedDurationFee = blPackage.getExtendedDurationFee();
		skidFee = blPackage.getSkidFee();
	}

	public int getPackageId() {
		return packageId;
	}

	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}

	public Courier getCourier() {
		return courier;
	}

	public void setCourier(Courier courier) {
		this.courier = courier;
	}

	public String getCustomInfo() {
		return customInfo;
	}

	public void setCustomInfo(String customInfo) {
		this.customInfo = customInfo;
	}

	public BigDecimal getDutyAndTax() {
		return dutyAndTax;
	}

	public void setDutyAndTax(BigDecimal dutyAndTax) {
		this.dutyAndTax = dutyAndTax;
	}

	/*
	 * public BigDecimal getKinekFee() { return kinekFee; }
	 * 
	 * public void setKinekFee(BigDecimal kinekFee) { this.kinekFee = kinekFee;
	 * }
	 */

	public String getShippedFrom() {
		return shippedFrom;
	}

	public void setShippedFrom(String shippedFrom) {
		this.shippedFrom = shippedFrom;
	}

	public Calendar getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(Calendar pickupDate) {
		this.pickupDate = pickupDate;
	}

	public boolean isOnSkid() {
		return onSkid;
	}

	public void setOnSkid(boolean onSkid) {
		this.onSkid = onSkid;
	}

	public BigDecimal getPickupFee() {
		return pickupFee;
	}

	public void setPickupFee(BigDecimal pickupFee) {
		this.pickupFee = pickupFee;
	}

	public BigDecimal getExtendedDurationFee() {
		return extendedDurationFee;
	}

	public void setExtendedDurationFee(BigDecimal extendedDurationFee) {
		this.extendedDurationFee = extendedDurationFee;
	}

	public BigDecimal getSkidFee() {
		return skidFee;
	}

	public void setSkidFee(BigDecimal skidFee) {
		this.skidFee = skidFee;
	}

	public void setPackageWeightGroup(PackageWeightGroup packageWeightGroup) {
		this.packageWeightGroup = packageWeightGroup;
	}

	public PackageWeightGroup getPackageWeightGroup() {
		return packageWeightGroup;
	}

}
