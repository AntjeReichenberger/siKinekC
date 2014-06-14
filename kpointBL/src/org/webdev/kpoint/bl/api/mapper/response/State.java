package org.webdev.kpoint.bl.api.mapper.response;

public class State {

	private String name;
	private Country country;
	private boolean isBorderState;
	private Double latitude;
	private Double longitude;
	private String stateProvCode;

	public State(org.webdev.kpoint.bl.pojo.State blState) {
		name = blState.getName();
		if (blState.getCountry() != null) {
			country = new Country(blState.getCountry());
		}
		isBorderState = blState.getIsBorderState();
		stateProvCode = blState.getStateProvCode();
		latitude = blState.getLatitude();
		longitude = blState.getLongitude();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public boolean getIsBorderState() {
		return isBorderState;
	}

	public void setIsBorderState(boolean isBorderState) {
		this.isBorderState = isBorderState;
	}

	public String getStateProvCode() {
		return stateProvCode;
	}

	public void setStateProvCode(String stateProvCode) {
		this.stateProvCode = stateProvCode;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

}
