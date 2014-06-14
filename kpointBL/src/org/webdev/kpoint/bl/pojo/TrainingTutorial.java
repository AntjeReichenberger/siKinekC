package org.webdev.kpoint.bl.pojo;

import java.io.Serializable;

public class TrainingTutorial implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3981994769824500963L;
	
	private int id;
	private String name;
	private String videoUrl;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
