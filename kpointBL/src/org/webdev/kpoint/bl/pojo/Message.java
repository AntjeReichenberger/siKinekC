package org.webdev.kpoint.bl.pojo;

import java.util.Calendar;

public class Message {

	private int id;
	private MessageTrigger trigger;
	private MessageMedia medium;
	private String recipientEmail;
	private String recipientCell;
	private String contents;
	private Calendar sentDate;
	
	
	
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
	
	
	public String getRecipientEmail() {
		return recipientEmail;
	}

	public void setRecipientEmail(String recipientEmail) {
		this.recipientEmail = recipientEmail;
	}

	public String getRecipientCell() {
		return recipientCell;
	}

	public void setRecipientCell(String recipientCell) {
		this.recipientCell = recipientCell;
	}
	
	
	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
	
	public Calendar getSentDate() {
		return sentDate;
	}

	public void setSentDate(Calendar sentDate) {
		this.sentDate = sentDate;
	}
}
