package org.webdev.kpoint.bl.api.mapper.response;

public class NotificationSummary {

	private boolean deliveryTextSupported;
	private boolean deliveryPushSupported;

	private boolean trackingEmailSupported;
	private boolean trackingTextSupported;
	private boolean trackingPushSupported;

	public NotificationSummary(
			org.webdev.kpoint.bl.pojo.NotificationSummary blNotificationSummary) {
		deliveryTextSupported = blNotificationSummary.isDeliveryTextSupported();
		deliveryPushSupported = blNotificationSummary.isDeliveryPushSupported();
		trackingEmailSupported = blNotificationSummary
				.isTrackingEmailSupported();
		trackingTextSupported = blNotificationSummary.isTrackingTextSupported();
		trackingPushSupported = blNotificationSummary.isTrackingPushSupported();
	}

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
