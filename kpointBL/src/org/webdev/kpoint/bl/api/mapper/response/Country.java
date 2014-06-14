package org.webdev.kpoint.bl.api.mapper.response;

public class Country {

	private String countryCode;
	private String name;
	private String currencyCode;

	public Country(org.webdev.kpoint.bl.pojo.Country blCountry) {
		countryCode = blCountry.getCountryCode();
		currencyCode = blCountry.getCurrencyCode();
		name = blCountry.getName();
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
