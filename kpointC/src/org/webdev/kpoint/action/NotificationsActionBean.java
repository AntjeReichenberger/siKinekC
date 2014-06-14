package org.webdev.kpoint.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.manager.NotificationManager;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.Notification;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.managers.MessageManager;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/Notifications.action")
public class NotificationsActionBean extends AccountDashboardActionBean {
	private User user;
	private boolean receiveEmailTrackingStatus;
	private boolean receiveTextMsgTrackingStatus;
	private boolean receiveTextMsgDeliveryStatus;
	
	@DefaultHandler @DontValidate
    public Resolution view() {
		user = getActiveUser();
		
		receiveEmailTrackingStatus = user.getNotificationSummary().isTrackingEmailSupported();
		receiveTextMsgTrackingStatus = user.getNotificationSummary().isTrackingTextSupported();
		receiveTextMsgDeliveryStatus = user.getNotificationSummary().isDeliveryTextSupported();
		
		return UrlManager.getNotifications();
    }
	
	@ValidationMethod(on="storeUser")
    public void formValidate(ValidationErrors errors) throws Exception {
		if((user == null || user.getCellPhone() == null) && (receiveTextMsgTrackingStatus || receiveTextMsgDeliveryStatus)) {
			errors.add("receiveTextMsgTrackingStatus", new SimpleError("Cannot send text msg notifications with no cell phone number provided."));
		}
	}

	public Resolution storeUser() throws Exception {
		User activeUser = getActiveUser();
		if (user != null) {
			activeUser.setCellPhone(user.getCellPhone());
		}
		else {
			activeUser.setCellPhone(null);
		}
		
		List<Notification> currentNotifications = new ArrayList<Notification>(activeUser.getNotifications()); 
		currentNotifications = NotificationManager.mergeNotification(currentNotifications, receiveEmailTrackingStatus, ExternalSettingsManager.getTrackingEmailNotification());
		currentNotifications = NotificationManager.mergeNotification(currentNotifications, receiveTextMsgTrackingStatus, ExternalSettingsManager.getTrackingTextNotification());
		currentNotifications = NotificationManager.mergeNotification(currentNotifications, receiveTextMsgDeliveryStatus, ExternalSettingsManager.getDeliveryTextNotification());

		activeUser.getNotificationSummary().setTrackingEmailSupported(receiveEmailTrackingStatus);
		activeUser.getNotificationSummary().setTrackingTextSupported(receiveTextMsgTrackingStatus);
		activeUser.getNotificationSummary().setDeliveryTextSupported(receiveTextMsgDeliveryStatus);
	
		activeUser.setNotifications(new HashSet<Notification>(currentNotifications));
		
        new UserDao().update(activeUser);
        
        // Update the user in session
        setActiveUser(activeUser);
        
        //return SUCCESS_PAGE;
        this.setSuccessMessage(MessageManager.getMyAccount());
        return new RedirectResolution(NotificationsActionBean.class);
    }
	
	public Resolution returnToMyParcels() {
		return new RedirectResolution(MyParcelsActionBean.class);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setReceiveEmailTrackingStatus(boolean receiveEmailTrackingStatus) {
		this.receiveEmailTrackingStatus = receiveEmailTrackingStatus;
	}

	public boolean getReceiveEmailTrackingStatus() {
		return receiveEmailTrackingStatus;
	}

	public void setReceiveTextMsgTrackingStatus(boolean receiveTextMsgTrackingStatus) {
		this.receiveTextMsgTrackingStatus = receiveTextMsgTrackingStatus;
	}

	public boolean getReceiveTextMsgTrackingStatus() {
		return receiveTextMsgTrackingStatus;
	}

	/**
	 * Used to determine where to return to if an action is taken that leads you from this page
	 * @return This class name
	 */
	public String getReferrer() {
		return this.getClass().getName();
	}

	public void setReceiveTextMsgDeliveryStatus(boolean receiveTextMsgDeliveryStatus) {
		this.receiveTextMsgDeliveryStatus = receiveTextMsgDeliveryStatus;
	}

	public boolean getReceiveTextMsgDeliveryStatus() {
		return receiveTextMsgDeliveryStatus;
	}
}
