package org.webdev.kpoint.bl.api.mapper.response;

public class Notification {

	private int id;
	private String trigger;
	private String medium;

	public Notification(org.webdev.kpoint.bl.pojo.Notification blNotification) {
		this.id = blNotification.getId();
		this.trigger = blNotification.getTrigger().getName();
		this.medium = blNotification.getMedium().getName();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

}
