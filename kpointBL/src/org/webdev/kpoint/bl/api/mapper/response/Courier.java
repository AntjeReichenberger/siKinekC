package org.webdev.kpoint.bl.api.mapper.response;

public class Courier {
	private String name;
	private String courierCode;
	private boolean isTrackable;

	public Courier(org.webdev.kpoint.bl.pojo.Courier courier) {
		name = courier.getName();
		courierCode = courier.getCourierCode();
		isTrackable = courier.getIsTrackable();
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
