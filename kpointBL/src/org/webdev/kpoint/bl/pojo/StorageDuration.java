package org.webdev.kpoint.bl.pojo;

public class StorageDuration{
	private transient int id;
	private Integer minDays;
	private Integer maxDays;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Integer getMinDays() {
		return minDays;
	}
	public void setMinDays(Integer minDays) {
		this.minDays = minDays;
	}
	public Integer getMaxDays() {
		return maxDays;
	}
	public void setMaxDays(Integer maxDays) {
		this.maxDays = maxDays;
	}
	public String getFriendlyLabel(){
		String result = "";
		if(maxDays != null){
			result = minDays + " days - " + maxDays + " days";
		}
		else{
			result = minDays + " days or more";
		}
		return result;
 	}
}