package org.webdev.kpoint.api.mobile.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.webdev.kpoint.api.mobile.util.WSApplicationError;
import org.webdev.kpoint.api.mobile.util.WSApplicationException;
import org.webdev.kpoint.bl.api.mapper.request.UserNotifications;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.manager.JanrainAuthenticationManager;
import org.webdev.kpoint.bl.manager.NotificationManager;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.LoginHistoryDao;
import org.webdev.kpoint.bl.persistence.NotificationDao;
import org.webdev.kpoint.bl.persistence.ReferralSourceDao;
import org.webdev.kpoint.bl.persistence.StateDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.LoginHistory;
import org.webdev.kpoint.bl.pojo.Notification;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.Role;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.pojo.User.App;

import com.googlecode.janrain4j.api.engage.response.profile.Profile;

public class UsersDomain extends Domain {
	private final static String USER_NOT_FOUND = "INVALID_USER_IDENTIFIER";
	private final static String USER_ALREADY_EXISTS = "USER_ALREADY_EXISTS";

	public static User authenticateJanrainUser(String tokenBody) throws Exception {
		//Retrieve the Janrain token from the post body
		String[] tokenParts = tokenBody.split("=");
		String token = tokenParts[1];
		   		
		JanrainAuthenticationManager manager = new JanrainAuthenticationManager();
		Profile profile = manager.getUserProfileFromJanrain(token);
		User openIdUser = manager.populateUser(profile);
		User mappedUser = manager.mapAccounts(openIdUser);
		if(mappedUser.getUserId() < 0)
		{
			//no automatic map found, need to create user account in Kinek DB
			manager.createNewUserAccount(mappedUser);
		}
		
		//set new authentication token in the user table
		mappedUser.setAuthenticationToken(token);
		UserDao dao = new UserDao();
		dao.update(mappedUser);

		if(mappedUser.getRoleId() == Role.DepotAdmin || mappedUser.getRoleId() == Role.DepotStaff)
			mappedUser.setApp(User.App.IPHONE_ADMIN_APP);
		else if(mappedUser.getRoleId() == Role.Consumer)
			mappedUser.setApp(User.App.IPHONE_CONSUMER_APP);
	  
		return mappedUser;
	}
	
	public static User getUser(String userName, User authenticatedUser) throws Exception {
		authorizationCheck(userName, authenticatedUser, "User does not have access to the target user data. Target Username: " + userName);
		
		UserDao userDao = new UserDao();				
		User user = userDao.read(userName);				
		
		if(user == null){							
			WSApplicationError err = new WSApplicationError(USER_NOT_FOUND, "No user was found. Requested Username: " + userName, Response.Status.BAD_REQUEST);				
			throw new WSApplicationException(err, err.getResponse());
		}
		
		return user;
	}
	
	/**
	 * Retrieves detailed user account info for the specified user.  This method is only available to KP Admin users.
	 * @param userId
	 * @param authenticatedUser
	 * @return
	 * @throws Exception
	 */
	public static User getUser(int userId, User authenticatedUser) throws Exception {
		isKPAdminAuthorizationCheck(authenticatedUser, "User does not have access to the target user data.");
		
		UserDao userDao = new UserDao();				
		User user = userDao.read(userId);				
		
		if(user == null){							
			WSApplicationError err = new WSApplicationError(USER_NOT_FOUND, "No user was found.  Requested User ID: " + userId, Response.Status.BAD_REQUEST);				
			throw new WSApplicationException(err, err.getResponse());
		}
		
		return  user;
	}
	
	public static int createLoginRecord(int userId, App app) throws Exception {
		LoginHistory loginRecord = new LoginHistory();
		loginRecord.setUserId(userId);
		loginRecord.setApplication(app.toString());
		LoginHistoryDao loginDao = new LoginHistoryDao();				
		return loginDao.create(loginRecord);				
	}
	
	public static User getUserByKinekNumber(String kinekNumber) throws Exception {
		UserDao userDao = new UserDao();
		User user = userDao.readConsumer(kinekNumber);
		return user;
	}
	
	public static List<User> getUsersByKinekNumber(String kinekNumber) throws Exception {
		UserDao userDao = new UserDao();
		List<User> users = userDao.fetchConsumers(kinekNumber);
		return users;
	}
	
	public static List<User> getUsers(String kinekNumber, String firstName, String lastName, int kinekPointId, User authenticatedUser) throws Exception {
		UserDao userDao = new UserDao();
		List<User> users = null;
		if(kinekNumber != null && kinekNumber != ""){
			users = getUsersByKinekNumber(kinekNumber);
		}
		else{
			users = userDao.fetchConsumersByNamesOrPhone(firstName, lastName, null);
		}
		
		if(users == null) {
			return new ArrayList<User>();
		}
		
		//filter the list of users that were found to only those that have packages pending pickup at the provided kinekpoint
		List<User> searchResults = new ArrayList<User>();
		if(kinekPointId > 0){
			for (User user : users){
				List<PackageReceipt> packageReceipts = PackagesDomain.getPackages(user.getUserId(), PackagesDomain.RECEIVED_STATUS, kinekPointId, authenticatedUser);
				if(packageReceipts.size() > 0){
					searchResults.add(user);
				}
			}
		}
		else{
			searchResults = users;
		}
		
		return searchResults;
	}
	
	public static User createUser(org.webdev.kpoint.bl.api.mapper.request.User user) throws Exception {
		User createUser = new User();
		mergeUserData(createUser, user);
		createUser.setUsername(user.getEmail());
		
		if(!isUsernameAvailable(createUser)){
			WSApplicationError err = new WSApplicationError(USER_ALREADY_EXISTS, "The email address (username) that was provided is already in use. Requested Username: " + createUser.getUsername(), Response.Status.BAD_REQUEST);				
			throw new WSApplicationException(err, err.getResponse());
		}
		
		cleanPhoneNumbers(createUser);
				
		createUser.setCreatedDate(Calendar.getInstance());
		createUser.setRoleId(Role.Consumer); //only supports creating consumer role
		createUser.setEnabled(true);
		createUser.setAgreedToTOS(false);
		createUser.setPassword(user.getPassword());
		if(createUser.getPassword() == null || createUser.getPassword().equals("")){
			//assign a random password
			createUser.setPassword(generateRandomString(8));
		}
		
		if(createUser.getKinekPoint() == null)
		{
			KinekPoint kp = new KinekPointDao().read(1);
			createUser.setKinekPoint(kp);
		}
				
		//user also receive tracking emails by default
		NotificationDao notificationDAO = new NotificationDao();
		Notification trackingEmail = notificationDAO.read(ExternalSettingsManager.getTrackingEmailNotification());
		HashSet<Notification> notifications = new HashSet<Notification>();
		notifications.add(trackingEmail);
		if(createUser.getCellPhone() != null && !createUser.getCellPhone().equals("")){
			//if a user enters their cell phone, they are automatically subscribed to receive delivery and tracking text msg
			notifications.add(notificationDAO.read(ExternalSettingsManager.getDeliveryTextNotification()));
			notifications.add(notificationDAO.read(ExternalSettingsManager.getTrackingTextNotification()));
		}
		createUser.setNotifications(notifications);
			
		UserDao userDAO = new UserDao();
		int id = userDAO.create(createUser);
		User updatedUser = userDAO.read(id);
		
		return updatedUser;
	}
	
	private static void cleanPhoneNumbers(User user) {
		user.setPhone(cleanPhoneNumber(user.getPhone()));
		user.setCellPhone(cleanPhoneNumber(user.getCellPhone()));
	}

	private static String cleanPhoneNumber(String phoneNumber) {
		if (phoneNumber == null || !phoneNumber.startsWith("1(")) {
			return phoneNumber;
		}
		
		return phoneNumber.substring(1);
	}

	public static User updateUser(org.webdev.kpoint.bl.api.mapper.request.User user, User authenticatedUser) throws Exception {
		authorizationCheck(user.getUserId(), authenticatedUser, "User does not have access to update the target user data. Target User ID: " + user.getUserId());
		
		//Do a read of the user to fetch full object data
		UserDao userDao = new UserDao();				
		User existingUserRecord = userDao.read(user.getUserId());
		if(existingUserRecord == null){							
			WSApplicationError err = new WSApplicationError(USER_NOT_FOUND, "User could not be updated, no user record was found.  Requested User ID: " + user.getUserId(), Response.Status.BAD_REQUEST);				
			throw new WSApplicationException(err, err.getResponse());
		}
		
		//If user had a cell number and removed it, we need to remove any subscriptions to receive text notifications
		List<Notification> currentNotifications = new ArrayList<Notification>(existingUserRecord.getNotifications());		
		if(existingUserRecord.getCellPhone() != null && !existingUserRecord.getCellPhone().equals("") && user.getCellPhone() != null && user.getCellPhone().equals(""))
		{
			currentNotifications = NotificationManager.mergeNotification(currentNotifications, false, ExternalSettingsManager.getDeliveryTextNotification());
			currentNotifications = NotificationManager.mergeNotification(currentNotifications, false, ExternalSettingsManager.getTrackingTextNotification());
		
			existingUserRecord.setNotifications(new HashSet<Notification>(currentNotifications));
			existingUserRecord.setNotificationSummary();
		}
		else if((existingUserRecord.getCellPhone() == null || existingUserRecord.getCellPhone().equals("")) && user.getCellPhone() != null && !user.getCellPhone().equals("")){
			//if a user enters their cell phone, they are automatically subscribed to receive delivery and tracking text msg
			currentNotifications = NotificationManager.mergeNotification(currentNotifications, true, ExternalSettingsManager.getDeliveryTextNotification());
			currentNotifications = NotificationManager.mergeNotification(currentNotifications, true, ExternalSettingsManager.getTrackingTextNotification());
			
			existingUserRecord.setNotifications(new HashSet<Notification>(currentNotifications));
			existingUserRecord.setNotificationSummary();
		}
				
		//Update existingUser record with the new data that was submitted
		User updatedUserObject = mergeUserData(existingUserRecord, user);
		cleanPhoneNumbers(updatedUserObject);
		
		UserDao userDAO = new UserDao();
		userDAO.update(updatedUserObject);
		
		return updatedUserObject;	
	}
	
	public static User updateUserNotifications(int userId, UserNotifications notificationSummary, User authenticatedUser) throws Exception {
		authorizationCheck(userId, authenticatedUser, "User does not have access to update the target user data. Target User ID: " + userId);
		
		//Do a read of the user to fetch full object data
		UserDao userDao = new UserDao();				
		User existingUserRecord = userDao.read(userId);
		
		if(existingUserRecord == null){							
			WSApplicationError err = new WSApplicationError(USER_NOT_FOUND, "User could not be updated, no user record was found.  Requested User ID: " + userId, Response.Status.BAD_REQUEST);				
			throw new WSApplicationException(err, err.getResponse());
		}
		
		//Update existingUser record with the new data that was submitted
		User updatedUserObject = mergeUserNotificationData(existingUserRecord, notificationSummary);
		UserDao userDAO = new UserDao();
		userDAO.update(updatedUserObject);
			
		//read user from db again to ensure accurate data
		User user = userDAO.read(updatedUserObject.getUserId());
		return user;	
	}

	public static Set<KinekPoint> getUserKinekPoints(int userId, User authenticatedUser) throws Exception {
		authorizationCheck(userId, authenticatedUser, "User does not have access to the target user's KinekPoint data. Target User ID: " + userId);
		
		UserDao userDao = new UserDao();				
		User user = userDao.read(userId);				
		
		if(user == null){							
			WSApplicationError err = new WSApplicationError(USER_NOT_FOUND, "Failed to retrieve user's default KinekPoint, no user record was found. Requested User Id: " + userId, Response.Status.BAD_REQUEST);				
			throw new WSApplicationException(err, err.getResponse());
		}

		return user.getKinekPoints();
	}

	//Returns null if the input string is empty.  Otherwise, returns the input string itself.
	private static String nullIfEmpty(String val) {
		if(val.equals("")) return null;
		
		return val;
	}
	
	//Merges the new user data into the current User and then returns the current User
	private static User mergeUserData(User currentUser, org.webdev.kpoint.bl.api.mapper.request.User newUser) throws Exception {
		if(newUser.getFirstName() != null)
			currentUser.setFirstName(nullIfEmpty(newUser.getFirstName()));
		
		if(newUser.getLastName() != null)
			currentUser.setLastName(nullIfEmpty(newUser.getLastName()));
		
		if(newUser.getAddress1() != null)
			currentUser.setAddress1(nullIfEmpty(newUser.getAddress1()));

		if(newUser.getAddress2() != null)
			currentUser.setAddress2(nullIfEmpty(newUser.getAddress2()));
		
		if(newUser.getCity() != null)
			currentUser.setCity(nullIfEmpty(newUser.getCity()));

		if(newUser.getStateCode() != null){
			if(newUser.getStateCode().equals(""))
			{
				currentUser.setState(null);
			}
			else if(currentUser.getState() == null || (currentUser.getState().getStateProvCode() != newUser.getStateCode()))
			{
				currentUser.setState(new StateDao().readFromStateProvCode(newUser.getStateCode()));
			}
		}
		
		if(newUser.getZip() != null)
			currentUser.setZip(nullIfEmpty(newUser.getZip()));
		
		if(newUser.getPhone() != null)
			currentUser.setPhone(nullIfEmpty(newUser.getPhone()));
		
		if(newUser.getCellPhone() != null)
			currentUser.setCellPhone(nullIfEmpty(newUser.getCellPhone()));
		
		if(newUser.getReferralSourceId() > 0)
			currentUser.setReferralSource(new ReferralSourceDao().read(newUser.getReferralSourceId()));

		
		if(newUser.getKinekPointId() > 0)
		{
			KinekPoint kp = new KinekPointDao().read(newUser.getKinekPointId());
			currentUser.setKinekPoint(kp);
			currentUser.getKinekPoints().add(kp);
		}
		
		if(newUser.getAgreedToTOS() != null)
		{
			currentUser.setAgreedToTOS(Boolean.valueOf(newUser.getAgreedToTOS()));
		}
		
		if(newUser.getEmail() != null)
		{
			currentUser.setEmail(newUser.getEmail());
			currentUser.setUsername(newUser.getEmail()); //username and email should be kept in sync
		}
		return currentUser;
	}
		
	//Merges the new user notification data into the current User and then returns the current User
	private static User mergeUserNotificationData(User currentUser, UserNotifications newNotificationList) throws Exception {
		List<Notification> currentNotifications = new ArrayList<Notification>(new NotificationDao().fetchNotifications(currentUser.getUserId()));		
		
		if(newNotificationList.getDeliveryTextSupported()!= null) currentNotifications = NotificationManager.mergeNotification(currentNotifications, Boolean.valueOf(newNotificationList.getDeliveryTextSupported()), ExternalSettingsManager.getDeliveryTextNotification());
		if(newNotificationList.getDeliveryPushSupported()!= null) currentNotifications = NotificationManager.mergeNotification(currentNotifications, Boolean.valueOf(newNotificationList.getDeliveryPushSupported()), ExternalSettingsManager.getDeliveryPushNotification());
		if(newNotificationList.getTrackingEmailSupported()!= null) currentNotifications = NotificationManager.mergeNotification(currentNotifications, Boolean.valueOf(newNotificationList.getTrackingEmailSupported()), ExternalSettingsManager.getTrackingEmailNotification());
		if(newNotificationList.getTrackingTextSupported()!= null) currentNotifications = NotificationManager.mergeNotification(currentNotifications, Boolean.valueOf(newNotificationList.getTrackingTextSupported()), ExternalSettingsManager.getTrackingTextNotification());
		if(newNotificationList.getTrackingPushSupported()!= null) currentNotifications = NotificationManager.mergeNotification(currentNotifications, Boolean.valueOf(newNotificationList.getTrackingPushSupported()), ExternalSettingsManager.getTrackingPushNotification());
		
		currentUser.setNotifications(new HashSet<Notification>(currentNotifications));
		return currentUser;
	}
	
	private static boolean isUsernameAvailable(User user) throws Exception {
		UserDao userDao = new UserDao();
		if(userDao.read(user.getUsername()) == null && userDao.readByEmail(user.getEmail()) == null)
			return true;
		
		return false;
	}
}
