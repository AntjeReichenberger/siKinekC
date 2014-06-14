package org.webdev.kpoint.bl.api.mapper.response;

import java.io.Serializable;

public class TrainingTutorial implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3981994769824500963L;
	
	private String name;
	private String videoUrl;
	
	public TrainingTutorial(org.webdev.kpoint.bl.pojo.TrainingTutorial trainingTutorial){
		name = trainingTutorial.getName();
		videoUrl = trainingTutorial.getVideoUrl();
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
