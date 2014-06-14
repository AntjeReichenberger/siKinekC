package org.webdev.kpoint.bl.api.mapper.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ReceivePackage implements Serializable {

	private static final long serialVersionUID = -2268091928249244707L;
	
	private int receivingKinekPointId = -1;
	private List<String> userList;
	private List<org.webdev.kpoint.bl.api.mapper.request.Package> packages;
	
	public int getReceivingKinekPointId() {
		return receivingKinekPointId;
	}
	public void setReceivingKinekPointId(int receivingKinekPointId) {
		this.receivingKinekPointId = receivingKinekPointId;
	}
	
	public List<String> getUserList() {
		return userList;
	}
	public void setUserList(List<String> userList) {
		this.userList = userList;
	}
	
	public List<org.webdev.kpoint.bl.api.mapper.request.Package> getPackages() {
		return packages;
	}
	public void setPackages(
			List<org.webdev.kpoint.bl.api.mapper.request.Package> packages) {
		this.packages = packages;
	}
	
}
