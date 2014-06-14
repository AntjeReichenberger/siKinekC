package org.webdev.kpoint.bl.api.mapper.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class PickupPackage implements Serializable {

	private static final long serialVersionUID = -2268091928249244707L;
	
	private int receivingKinekPointId = -1;
	private int userId = -1;
	private List<String> packageIdList;
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getUserId() {
		return userId;
	}
	
	public void setPackageIdList(List<String> packageIdList) {
		this.packageIdList = packageIdList;
	}
	public List<String> getPackageIdList() {
		return packageIdList;
	}
	
	public void setReceivingKinekPointId(int receivingKinekPointId) {
		this.receivingKinekPointId = receivingKinekPointId;
	}
	public int getReceivingKinekPointId() {
		return receivingKinekPointId;
	}
}
