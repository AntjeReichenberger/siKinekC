package org.webdev.kpoint.bl.pojo;

public class CountryAreaCode {

	private int countryAreaCodeId = -1;
	private String countryCode;
	private String areaCode;
	
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getAreaCode() {
		return areaCode;
	}

	public void setCountryAreaCodeId(int countryAreaCodeId) {
		this.countryAreaCodeId = countryAreaCodeId;
	}
	public int getCountryAreaCodeId() {
		return countryAreaCodeId;
	}
	
	
}
