package org.webdev.kpoint.api.partner.domain;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.webdev.kpoint.api.partner.util.WSApplicationError;
import org.webdev.kpoint.api.partner.util.WSApplicationException;
import org.webdev.kpoint.bl.manager.EmailManager;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.NotificationDao;
import org.webdev.kpoint.bl.persistence.StateDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Notification;
import org.webdev.kpoint.bl.pojo.Role;
import org.webdev.kpoint.bl.pojo.User;

public class UsersDomain extends Domain{
	private final static String USER_NOT_FOUND = "INVALID_USER_IDENTIFIER";

	public static User getUser(String userName) throws Exception {
		UserDao userDao = new UserDao();				
		User user = userDao.read(userName);				
		
		if(user == null){							
			WSApplicationError err = new WSApplicationError(USER_NOT_FOUND, "No user was found. Requested Username: " + userName, Response.Status.BAD_REQUEST);				
			throw new WSApplicationException(err, err.getResponse());
		}
		
		return user;
	}
	
	public static User getUser(int userId) throws Exception {
		UserDao userDao = new UserDao();				
		User user = userDao.read(userId);				
		
		if(user == null){							
			WSApplicationError err = new WSApplicationError(USER_NOT_FOUND, "No user was found.  Requested User ID: " + userId, Response.Status.BAD_REQUEST);				
			throw new WSApplicationException(err, err.getResponse());
		}
		
		return user;
	}
	
	public static User createUser(User user, String stateCode, String kinekPointId, int partnerId) throws Exception{
		user.setUsername(user.getEmail());
		
		if(!isUsernameAvailable(user)){
			UserDao userDao = new UserDao();
			User existingUser = userDao.read(user.getEmail());
			return existingUser;
		}
				
		UserDao userDAO = new UserDao();
		user.setCreatedDate(Calendar.getInstance());
		user.setRoleId(Role.Consumer); //only supports creating consumer role
		user.setEnabled(true);
		user.setAgreedToTOS(false);
		user.setCreatedByPartnerId(partnerId);
		//assign a random password
		user.setPassword(generateRandomString(8));
			
		if(stateCode != null && !stateCode.equals(""))
			user.setState(new StateDao().readFromStateProvCode(stateCode));
		
		if(kinekPointId != null && !kinekPointId.equals(""))
		{
			KinekPoint kp = new KinekPointDao().read(Integer.valueOf(kinekPointId));
			user.setKinekPoint(kp);
			Set<KinekPoint> kpList = new HashSet<KinekPoint>();
			kpList.add(kp);
			user.setKinekPoints(kpList);
		}
		else
		{
			int defaultDepotId = 1;
			user.setKinekPoint(new KinekPointDao().read(defaultDepotId));
		}
		
		//user also receive tracking emails by default
		NotificationDao notificationDAO = new NotificationDao();
		Notification trackingEmail = notificationDAO.read(ExternalSettingsManager.getTrackingEmailNotification());
		HashSet<Notification> notifications = new HashSet<Notification>();
		notifications.add(trackingEmail);
		if(user.getCellPhone() != null && !user.getCellPhone().equals("")){
			//if a user enters their cell phone, they are automatically subscribed to receive delivery and tracking text msg
			notifications.add(notificationDAO.read(ExternalSettingsManager.getDeliveryTextNotification()));
			notifications.add(notificationDAO.read(ExternalSettingsManager.getTrackingTextNotification()));
		}
		user.setNotifications(notifications);
			
		int id = userDAO.create(user);
		User updatedUser = userDAO.read(id);
		
		EmailManager emailManager = new EmailManager();
		emailManager.sendPartnerSignupEmail(updatedUser, partnerId);
		
		return user;
	}

	private static boolean isUsernameAvailable(User user) throws Exception {
		UserDao userDao = new UserDao();
		if(userDao.read(user.getUsername()) == null && userDao.readByEmail(user.getEmail()) == null)
			return true;
		
		return false;
	}
}
