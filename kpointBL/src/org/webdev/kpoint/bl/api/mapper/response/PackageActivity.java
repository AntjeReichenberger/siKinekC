package org.webdev.kpoint.bl.api.mapper.response;

import java.util.Calendar;

public class PackageActivity {
	private String country;
	private String city;
	private String stateProv;
	private String activity;
	private Calendar dateTime;

	public PackageActivity(
			org.webdev.kpoint.bl.tracking.PackageActivity blPackageActivity) {
		country = blPackageActivity.getCountry();
		city = blPackageActivity.getCity();
		stateProv = blPackageActivity.getStateProv();
		activity = blPackageActivity.getActivity();
		dateTime = blPackageActivity.getDateTime();
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStateProv() {
		return stateProv;
	}

	public void setStateProv(String stateProv) {
		this.stateProv = stateProv;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public Calendar getDateTime() {
		return dateTime;
	}

	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;
	}
}
