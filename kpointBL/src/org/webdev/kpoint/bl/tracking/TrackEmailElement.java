package org.webdev.kpoint.bl.tracking;

public class TrackEmailElement {
	private String trackingNumber;
	private String emailStatus;
	
	public TrackEmailElement(String foundTrackingNumber) {
		trackingNumber = foundTrackingNumber;
	}
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public String getEmailStatus() {
		return emailStatus;
	}
	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}
}
