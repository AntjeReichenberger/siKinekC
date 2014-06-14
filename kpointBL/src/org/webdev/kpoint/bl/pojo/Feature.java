package org.webdev.kpoint.bl.pojo;

import java.io.Serializable;

public class Feature implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3981994769824500963L;
	
	private int featureId;
	private String name;
	
	
	public int getFeatureId() {
		return featureId;
	}
	public void setFeatureId(int featureId) {
		this.featureId = featureId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
	
	
	
}
