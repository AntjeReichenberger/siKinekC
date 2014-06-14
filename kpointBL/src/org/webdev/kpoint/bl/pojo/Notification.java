package org.webdev.kpoint.bl.pojo;

public class Notification {

	private int id;
	private MessageTrigger trigger;
	private MessageMedia medium;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public MessageTrigger getTrigger() {
		return trigger;
	}

	public void setTrigger(MessageTrigger trigger) {
		this.trigger = trigger;
	}
	
	public MessageMedia getMedium() {
		return medium;
	}

	public void setMedium(MessageMedia medium) {
		this.medium = medium;
	}
}
