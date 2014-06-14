package org.webdev.kpoint.bl.tracking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.webdev.kpoint.bl.persistence.CourierDao;
import org.webdev.kpoint.bl.pojo.Courier;

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
	private transient boolean hasMap = true;
	private boolean isAvailableInCourierSystem = true;
	private Courier courier;
	private String nickname = "";
	private List<PackageActivity> packageHistory = new ArrayList<PackageActivity>();
	private boolean hasChanged = false;
	private boolean isValidTrackingNumber = true;
	private boolean isDelivered = false;
	
	

	public void setIsAvailableInCourierSystem(boolean availableInCourierSystem) {
		this.isAvailableInCourierSystem = availableInCourierSystem;
	}

	public boolean getIsAvailableInCourierSystem() {
		return isAvailableInCourierSystem;
	}

	public boolean getHasMap() {
		return hasMap;
	}

	public void setHasMap(boolean hasMap) {
		this.hasMap = hasMap;
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

	public TrackedPackage(){		
		hasMap = false;
		isAvailableInCourierSystem = false;
	}
	
	public TrackedPackage(String trackingNumber, int courierId) throws Exception {		
		this.trackingNumber = trackingNumber;	
		this.setCourier(courierId);
	}
	
	public TrackedPackage(String trackingNumber, int courierId, String nickname) throws Exception {		
		this.trackingNumber = trackingNumber;	
		this.setCourier(courierId);
		this.setNickname(nickname);
	}
	
	public void setCourier(Integer courierId) throws Exception {
		if(courierId != null){
			CourierDao courierDao = new CourierDao();
			courier = courierDao.read(courierId);
		}
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

	public String getCurrentLocation() {
		if(packageHistory.size() != 0) return packageHistory.get(0).getLocation();
		else return "N/A";
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

	public void setIsDelivered(boolean isDelivered) {
		this.isDelivered = isDelivered;
	}

	public boolean getIsDelivered() {
		return isDelivered;
	}

}
