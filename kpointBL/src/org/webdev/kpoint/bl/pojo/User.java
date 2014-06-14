package org.webdev.kpoint.bl.pojo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.webdev.kpoint.bl.manager.ExternalSettingsManager;

public class User implements Serializable {

	private static final long serialVersionUID = -2268091928249244707L;

    public static enum App { ADMIN_PORTAL, CONSUMER_PORTAL, IPHONE_ADMIN_APP, IPHONE_CONSUMER_APP, ANDROID_CONSUMER_APP, MOBILE_WEB }
	 
	private int userId = -1;
	private String firstName;
	private String lastName;
	private String address1;
	private String address2;
	private String city;
	private State state;
	private String zip;
	private String phone;
	private String cellPhone;
	private String email;
	private transient String password;
	private String username;
	private String kinekNumber;
	private boolean enabled = true;
	private KinekPoint depot;
	private int roleId = 1;
	private Calendar createdDate;
	private Calendar enabledDate;
	private boolean agreedToTOS;
	private String sessionId;
	private Calendar regReminderEmailDate;
	private String registrationPromoCode;
	private ReferralSource referralSource;
	private String openID;
	private String authenticationToken;
	private transient Set<Notification> notifications = new HashSet<Notification>();
	private NotificationSummary notificationSummary = new NotificationSummary();
	private Set<KinekPoint> kinekPoints = new HashSet<KinekPoint>();
	private int createdByPartnerId;
	private App app;
	
	public String getConfirmPassword() {
		return password;
	}

	public boolean getReportAccessCheck() {

		if (getRoleId() == Role.ReportAdmin)
			return true;

		return false;
	}

	public boolean getAdminAccessCheck() {

		if (getRoleId() == Role.KinekAdmin)
			return true;

		return false;
	}

	public boolean getDepotStaffAccessCheck() {
		if (getRoleId() == Role.DepotStaff)
			return true;

		return false;
	}

	public boolean getDepotAdminAccessCheck() {

		if (getRoleId() == Role.DepotAdmin)
			return true;

		return false;
	}

	public boolean isConsumer() {

		if (getRoleId() == Role.Consumer)
			return true;

		return false;
	}

	public Calendar getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public KinekPoint getDepot() {
		return depot;
	}

	public void setDepot(KinekPoint depot) {
		this.depot = depot;
	}
	
	public KinekPoint getKinekPoint() {
		return depot;
	}

	public void setKinekPoint(KinekPoint depot) {
		this.depot = depot;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {

		if (zip == null)
			zip = "";

		zip = zip.trim();
		zip = zip.replace(" ", "");
		zip = zip.replace("-", "");

		if (zip.length() == 7) {
			zip = zip.substring(0, 3) + zip.substring(4, 7);
		}

		if (zip.length() > 6)
			zip = zip.substring(0, 6);

		this.zip = zip;
	}
	
	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getKinekNumber() {
		// if not a consumer no Kinek Number for you !
		if (!isConsumer())
			return "";
		return kinekNumber;
	}

	public void setKinekNumber(String kinekNumber) {
		this.kinekNumber = kinekNumber;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getFullName() {
		String name = "";
		if (firstName != null)
			name = name + firstName;
		if (lastName != null)
			name = name + " " + lastName;

		return name;
	}

	/**
	 * Gets a boolean indicating whether the Terms of service have been agreed to or not
	 * @return a boolean indicating whether the Terms of service have been agreed to or not
	 */
	public boolean getAgreedToTOS() {
		return agreedToTOS;
	}
	
	/**
	 * Sets a boolean indicating whether the Terms of service have been agreed to or not
	 * @param agreedToTOS
	 */
	public void setAgreedToTOS(boolean agreedToTOS) {
		this.agreedToTOS = agreedToTOS;
	}
	
	public Calendar getEnabledDate() {
		return enabledDate;
	}

	public void setEnabledDate(Calendar enabledDate) {
		this.enabledDate = enabledDate;
	}

	/**
	 * Retrieves the current session id of the user
	 * @return The current session id of the user
	 */
	public String getSessionId() {
		return sessionId;
	}
	
	/**
	 * Sets the current session id of the user
	 * @param sessionId
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public Calendar getRegReminderEmailDate() {
		return regReminderEmailDate;
	}

	public void setRegReminderEmailDate(Calendar regReminderEmailDate) {
		this.regReminderEmailDate = regReminderEmailDate;
	}

	public String getRegistrationPromoCode() {
		return registrationPromoCode;
	}

	public void setRegistrationPromoCode(String registrationPromoCode) {
		this.registrationPromoCode = registrationPromoCode;
	}
	
	public ReferralSource getReferralSource() {
		return referralSource;
	}
	
	public void setReferralSource(ReferralSource referralSource) {
		this.referralSource = referralSource;
	}
	
	public String getOpenID() {
		return openID;
	}

	public void setOpenID(String openID) {
		this.openID = openID;
	}

	/**
	 * Authentication tokens are used during some forms of authentication, including Janrain OpenID Auth.
	 * @param authenticationToken
	 */
	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

	public String getAuthenticationToken() {
		return authenticationToken;
	}

	public Set<KinekPoint> getKinekPoints() {
		return kinekPoints;
	}

	public void setKinekPoints(Set<KinekPoint> kinekPoints) {
		this.kinekPoints = kinekPoints;
	}

	public Set<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(Set<Notification> notifications) {
		this.notifications = notifications;
	}
	
	public void setNotificationSummary() {
		notificationSummary = new NotificationSummary();
		java.util.Iterator<Notification> data = getNotifications().iterator();
		while(data.hasNext())
		{
			Notification notification = (Notification)data.next();
			if(notification.getId() == ExternalSettingsManager.getDeliveryTextNotification())
				notificationSummary.setDeliveryTextSupported(true);
			else if(notification.getId() == ExternalSettingsManager.getDeliveryPushNotification())
				notificationSummary.setDeliveryPushSupported(true);
			else if(notification.getId() == ExternalSettingsManager.getTrackingEmailNotification())
				notificationSummary.setTrackingEmailSupported(true);
			else if(notification.getId() == ExternalSettingsManager.getTrackingTextNotification())
				notificationSummary.setTrackingTextSupported(true);
			else if(notification.getId() == ExternalSettingsManager.getTrackingPushNotification())
				notificationSummary.setTrackingPushSupported(true);
		}
	}
	
	/**
	 * Used before sending a GSON to a partner, notifications not needed
	 * @param notificationSummary
	 */
	public void removeNotificationSummary() {
		notificationSummary = null;
	}

	/**
	 * Provides a wrapper and simple mechanism for determining if a customer has subscribed for a particular notification
	 * @param notificationSummary
	 */
	public NotificationSummary getNotificationSummary() {
		return notificationSummary;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) return true;
		
		if ( !(o instanceof User) ) return false;
		
		User packageObj = (User)o;
		return this.getUserId() == packageObj.getUserId();
	}

	public void setCreatedByPartnerId(int partnerId) {
		this.createdByPartnerId = partnerId;
	}

	public int getCreatedByPartnerId() {
		return createdByPartnerId;
	}

	/**
	 * This property represents the application that the user is currently using. Ideally, the value would
	 * be set at runtime when the user authenticates.  This would ensure it is available for use with all 
	 * subsequent operations.
	 */
	public void setApp(App app) {
		this.app = app;
	}

	/**
	 * This property represents the application that the user is currently using.  The value is only
	 * available at runtime and there is no guarantee that it's value will always be populated.
	 * @return
	 */
	public App getApp() {
		return app;
	}
}
