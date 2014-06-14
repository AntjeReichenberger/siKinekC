package org.webdev.kpoint.bl.pojo;

import java.math.BigDecimal;

public class StorageWeightGroup{
	private transient int id;
	private BigDecimal minWeight;
	private BigDecimal maxWeight;
	private String friendlyLabel;
	
	public BigDecimal getMinWeight() {
		return minWeight;
	}
	public void setMinWeight(BigDecimal minWeight) {
		this.minWeight = minWeight;
	}
	public BigDecimal getMaxWeight() {
		return maxWeight;
	}
	public void setMaxWeight(BigDecimal maxWeight) {
		this.maxWeight = maxWeight;
	}
	public int getId(){
		return id;
	}	
	public void setId(int id){
		this.id = id;
	}	
	public String getFriendlyLabel(){
		String result = "";
		if(maxWeight != null){
			result = minWeight + " lbs - " + maxWeight + " lbs";
		}
		else{
			result = minWeight + " lbs or more";
		}
		return result;
 	}
	public void setFriendlyLabel(String friendlyLabel) {
		this.friendlyLabel = friendlyLabel;
	}
	
}