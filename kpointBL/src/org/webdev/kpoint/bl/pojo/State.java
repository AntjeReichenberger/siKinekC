package org.webdev.kpoint.bl.pojo;

public class State {

	private int stateId;
	private String name;
	private Country country;
	private boolean isBorderState;
	private Double latitude;
	private Double longitude;
	private String stateProvCode;
	


	public State() {
	}
	
	public State(int id) {
		stateId = id;
	}
	
	/**
	 * This constructor is only available to avoid default type converter exceptions within the Stripes
	 * Framework.  It does not contain any implementation and should not be used by any code except the
	 * internal Stripes classes.
	 * @param depecatedParam
	 */
	public State(String depecatedParam) {
	}
	
	public int getStateId() {
		return stateId;
	}
	public void setStateId(int stateId) {
		this.stateId = stateId;
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
