package org.webdev.kpoint.bl.tracking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PackageActivity {
	private String country;
	private String city;
	private String stateProv;
	private String activity;
	private Calendar dateTime;
	
	public String getLocation(){
		List<String> location = new ArrayList<String>();
		if(city != null) location.add(city);
		if(stateProv != null) location.add(stateProv);
		if(country != null) location.add(country);
		
		String result = "";
		for(int i=0; i < location.size(); i++){
			if(i != 0) result +=", ";
			result += location.get(i);
		}
		
		if(result.equals("")){
			result = "N/A";
		}
		return result;
		
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
