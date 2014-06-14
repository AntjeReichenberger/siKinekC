package org.webdev.kpoint.bl.api.mapper.response;

import static org.webdev.kpoint.bl.api.mapper.response.util.CollectionConstructor.construct;

import java.util.Calendar;
import java.util.List;

public class TrackedPackage {
	private Calendar arrivalDate;
	private boolean arrivalDateHasTime;
	private Calendar dateShipped;
	private boolean dateShippedHasTime;
	private String trackingNumber;
	private String weight;
	private String weightType;
	private String shipmentType;
	private String status;
	private String shipToAddress;
	private String shipFromAddress;
	private boolean isValid;
	private Courier courier;
	private String nickname;
	private List<PackageActivity> packageHistory;
	private boolean hasChanged;
	private boolean isValidTrackingNumber;

	public TrackedPackage(
			org.webdev.kpoint.bl.tracking.TrackedPackage blTrackedPackage) {
		arrivalDate = blTrackedPackage.getArrivalDate();
		arrivalDateHasTime = blTrackedPackage.isArrivalDateHasTime();
		dateShipped = blTrackedPackage.getDateShipped();
		dateShippedHasTime = blTrackedPackage.isDateShippedHasTime();
		trackingNumber = blTrackedPackage.getTrackingNumber();
		weight = blTrackedPackage.getWeight();
		weightType = blTrackedPackage.getWeightType();
		shipmentType = blTrackedPackage.getShipmentType();
		status = blTrackedPackage.getStatus();
		shipToAddress = blTrackedPackage.getShipToAddress();
		shipFromAddress = blTrackedPackage.getShipFromAddress();
		isValid = blTrackedPackage.getIsAvailableInCourierSystem();
		if (blTrackedPackage.getCourier() != null) {
			courier = new Courier(blTrackedPackage.getCourier());
		}
		nickname = blTrackedPackage.getNickname();
		packageHistory = construct(blTrackedPackage.getPackageHistory(),
				PackageActivity.class);
		hasChanged = blTrackedPackage.getHasChanged();
		isValidTrackingNumber = blTrackedPackage.getIsValidTrackingNumber();
	}

	public void setIsValid(boolean isValid) {
		this.isValid = isValid;
	}

	public boolean getIsValid() {
		return isValid;
	}

	public boolean isArrivalDateHasTime() {
		return arrivalDateHasTime;
	}

	public void setArrivalDateHasTime(boolean arrivalDateHasTime) {
		this.arrivalDateHasTime = arrivalDateHasTime;
	}

	public boolean isDateShippedHasTime() {
		return dateShippedHasTime;
	}

	public void setDateShippedHasTime(boolean dateShippedHasTime) {
		this.dateShippedHasTime = dateShippedHasTime;
	}

	public String getShipToAddress() {
		return shipToAddress;
	}

	public void setShipToAddress(String shipToAddress) {
		this.shipToAddress = shipToAddress;
	}

	public String getShipFromAddress() {
		return shipFromAddress;
	}

	public void setShipFromAddress(String shipFromAddress) {
		this.shipFromAddress = shipFromAddress;
	}

	public String getWeightType() {
		return weightType;
	}

	public void setWeightType(String weightType) {
		this.weightType = weightType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<PackageActivity> getPackageHistory() {
		return packageHistory;
	}

	public void setPackageHistory(List<PackageActivity> packageHistory) {
		this.packageHistory = packageHistory;
	}

	public Calendar getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Calendar arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public Calendar getDateShipped() {
		return dateShipped;
	}

	public void setDateShipped(Calendar dateShipped) {
		this.dateShipped = dateShipped;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getShipmentType() {
		return shipmentType;
	}

	public void setShipmentType(String shipmentType) {
		this.shipmentType = shipmentType;
	}

	public void setCourier(Courier courier) {
		this.courier = courier;
	}

	public Courier getCourier() {
		return courier;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setHasChanged(boolean hasChanged) {
		this.hasChanged = hasChanged;
	}

	public boolean getHasChanged() {
		return hasChanged;
	}

	public void setIsValidTrackingNumber(boolean isValidTrackingNumber) {
		this.isValidTrackingNumber = isValidTrackingNumber;
	}

	public boolean getIsValidTrackingNumber() {
		return isValidTrackingNumber;
	}

}
