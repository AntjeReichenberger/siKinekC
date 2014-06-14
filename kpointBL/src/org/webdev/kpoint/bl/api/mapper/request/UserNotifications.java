package org.webdev.kpoint.bl.api.mapper.request;

import java.io.Serializable;

public class UserNotifications implements Serializable {

private static final long serialVersionUID = -8752853137885250242L;
	
	private String deliveryEmailSupported;
	private String deliveryTextSupported;
	private String deliveryPushSupported;
	
	private String trackingEmailSupported;
	private String trackingTextSupported;
	private String trackingPushSupported;
	
	public String getDeliveryEmailSupported() {
		return deliveryEmailSupported;
	}
	public void setDeliveryEmailSupported(String deliveryEmailSupported) {
		this.deliveryEmailSupported = deliveryEmailSupported;
	}
	public String getDeliveryTextSupported() {
		return deliveryTextSupported;
	}
	public void setDeliveryTextSupported(String deliveryTextSupported) {
		this.deliveryTextSupported = deliveryTextSupported;
	}
	public String getDeliveryPushSupported() {
		return deliveryPushSupported;
	}
	public void setDeliveryPushSupported(String deliveryPushSupported) {
		this.deliveryPushSupported = deliveryPushSupported;
	}
	public String getTrackingEmailSupported() {
		return trackingEmailSupported;
	}
	public void setTrackingEmailSupported(String trackingEmailSupported) {
		this.trackingEmailSupported = trackingEmailSupported;
	}
	public String getTrackingTextSupported() {
		return trackingTextSupported;
	}
	public void setTrackingTextSupported(String trackingTextSupported) {
		this.trackingTextSupported = trackingTextSupported;
	}
	public String getTrackingPushSupported() {
		return trackingPushSupported;
	}
	public void setTrackingPushSupported(String trackingPushSupported) {
		this.trackingPushSupported = trackingPushSupported;
	}
		
}
