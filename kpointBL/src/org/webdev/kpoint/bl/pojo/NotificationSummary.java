package org.webdev.kpoint.bl.pojo;

public class NotificationSummary {

	private boolean deliveryTextSupported = false;
	private boolean deliveryPushSupported = false;
	
	private boolean trackingEmailSupported = false;
	private boolean trackingTextSupported = false;
	private boolean trackingPushSupported = false;
	
	public void setDeliveryTextSupported(boolean deliveryText) {
		this.deliveryTextSupported = deliveryText;
	}
	public boolean isDeliveryTextSupported() {
		return deliveryTextSupported;
	}
	
	public void setDeliveryPushSupported(boolean deliveryPush) {
		this.deliveryPushSupported = deliveryPush;
	}
	public boolean isDeliveryPushSupported() {
		return deliveryPushSupported;
	}
	
	public void setTrackingEmailSupported(boolean trackingEmail) {
		this.trackingEmailSupported = trackingEmail;
	}
	public boolean isTrackingEmailSupported() {
		return trackingEmailSupported;
	}
	
	public void setTrackingTextSupported(boolean trackingText) {
		this.trackingTextSupported = trackingText;
	}
	public boolean isTrackingTextSupported() {
		return trackingTextSupported;
	}
	
	public void setTrackingPushSupported(boolean trackingPush) {
		this.trackingPushSupported = trackingPush;
	}
	public boolean isTrackingPushSupported() {
		return trackingPushSupported;
	}
	
	
}
