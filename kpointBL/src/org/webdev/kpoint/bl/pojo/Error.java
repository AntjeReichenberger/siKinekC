package org.webdev.kpoint.bl.pojo;

public class Error {

	private int id;
	private String errorCode;
	private String friendlyMessage;
	
	public Error() {
	}
	
	public Error(String errorCode, String friendlyMessage) {
		this.errorCode = errorCode;
		this.friendlyMessage = friendlyMessage;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getFriendlyMessage() {
		return friendlyMessage;
	}
	public void setFriendlyMessage(String friendlyMessage) {
		this.friendlyMessage = friendlyMessage;
	}
}
