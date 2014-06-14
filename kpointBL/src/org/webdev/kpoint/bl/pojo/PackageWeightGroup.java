package org.webdev.kpoint.bl.pojo;

import java.math.BigDecimal;
import java.util.Set;

public class PackageWeightGroup {
	private int id;
	private BigDecimal minWeight;
	private BigDecimal maxWeight;
	private String friendlyLabel;
	
	private Set<StorageWeightGroup> storageWeightGroup;
	
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
	
	public void setStorageWeightGroup(Set<StorageWeightGroup> storageWeightGroup) {
		this.storageWeightGroup = storageWeightGroup;
	}
	
	public Set<StorageWeightGroup> getStorageWeightGroup() {
		return storageWeightGroup;
	}
	
	public StorageWeightGroup getSingleStorageWeightGroup(){
		if(storageWeightGroup.size() > 0)
			return storageWeightGroup.iterator().next();
		else
			return null;
	}
}