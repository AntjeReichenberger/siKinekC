package org.webdev.kpoint.bl.pojo;

public class Courier {
	private transient int courierId = -1;
	private String name;
	private String courierCode;
	private boolean isTrackable;
	
	public Courier() {
	}
	
	public Courier(int i, String string) {
		courierId = i;
		name = string;
	}
	
	public int getCourierId() {
		return courierId;
	}
	public void setCourierId(int courierId) {
		this.courierId = courierId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setCourierCode(String courierCode) {
		this.courierCode = courierCode;
	}

	public String getCourierCode() {
		return courierCode;
	}

	public void setIsTrackable(boolean isTrackable) {
		this.isTrackable = isTrackable;
	}

	public boolean getIsTrackable() {
		return isTrackable;
	}
	
	
}
