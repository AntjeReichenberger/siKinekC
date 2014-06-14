package org.webdev.kpoint.bl.api.mapper.response;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Role;

public class User implements Serializable {

	private static final long serialVersionUID = -2268091928249244707L;

	private int userId;
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
	private String username;
	private String kinekNumber;
	private boolean enabled;
	private KinekPoint depot;
	private int roleId;
	private Calendar createdDate;
	private Calendar enabledDate;
	private boolean agreedToTOS;
	private String sessionId;
	private Date lastLoginDate;
	private Calendar regReminderEmailDate;
	private String registrationPromoCode;
	private ReferralSource referralSource;
	private String openID;
	private String authenticationToken;
	private NotificationSummary notificationSummary;

	public User(org.webdev.kpoint.bl.pojo.User blUser) {
		createdDate = blUser.getCreatedDate();
		enabled = blUser.getEnabled();
		userId = blUser.getUserId();
		firstName = blUser.getFirstName();
		lastName = blUser.getLastName();
		address1 = blUser.getAddress1();
		address2 = blUser.getAddress2();
		city = blUser.getCity();
		if (blUser.getState() != null) {
			state = new State(blUser.getState());
		}
		zip = blUser.getZip();
		cellPhone = blUser.getCellPhone();
		phone = blUser.getPhone();
		email = blUser.getEmail();
		username = blUser.getUsername();
		kinekNumber = blUser.getKinekNumber();
		roleId = blUser.getRoleId();
		agreedToTOS = blUser.getAgreedToTOS();
		enabledDate = blUser.getEnabledDate();
		sessionId = blUser.getSessionId();
		regReminderEmailDate = blUser.getRegReminderEmailDate();
		registrationPromoCode = blUser.getRegistrationPromoCode();
		if (blUser.getReferralSource() != null) {
			referralSource = new ReferralSource(blUser.getReferralSource());
		}
		openID = blUser.getOpenID();
		authenticationToken = blUser.getAuthenticationToken();
		if (blUser.getNotificationSummary() != null) {
			notificationSummary = new NotificationSummary(
					blUser.getNotificationSummary());
		}
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
	 * Gets a boolean indicating whether the Terms of service have been agreed
	 * to or not
	 * 
	 * @return a boolean indicating whether the Terms of service have been
	 *         agreed to or not
	 */
	public boolean getAgreedToTOS() {
		return agreedToTOS;
	}

	/**
	 * Sets a boolean indicating whether the Terms of service have been agreed
	 * to or not
	 * 
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
	 * 
	 * @return The current session id of the user
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Sets the current session id of the user
	 * 
	 * @param sessionId
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
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
	 * Authentication tokens are used during some forms of authentication,
	 * including Janrain OpenID Auth.
	 * 
	 * @param authenticationToken
	 */
	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

	public String getAuthenticationToken() {
		return authenticationToken;
	}

	/**
	 * Provides a wrapper and simple mechanism for determining if a customer has
	 * subscribed for a particular notification
	 * 
	 * @param notificationSummary
	 */
	public NotificationSummary getNotificationSummary() {
		return notificationSummary;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof User))
			return false;

		User packageObj = (User) o;
		return this.getUserId() == packageObj.getUserId();
	}
}
