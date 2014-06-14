package org.webdev.kpoint.bl.pojo;

import java.io.Serializable;

public class SizeAllowance implements Serializable {

	private static final long serialVersionUID = -7880358685809655170L;
	private int sizeAllowanceId;
	private String name;
	private String friendlyName;

	public int getSizeAllowanceId() {
		return sizeAllowanceId;
	}
	public void setSizeAllowanceId(int sizeAllowanceId) {
		this.sizeAllowanceId = sizeAllowanceId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFriendlyName() {
		return friendlyName;
	}
	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}
	
	
	
	
	
}
