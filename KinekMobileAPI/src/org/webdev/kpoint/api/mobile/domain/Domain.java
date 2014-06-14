package org.webdev.kpoint.api.mobile.domain;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.webdev.kpoint.api.mobile.util.WSApplicationError;
import org.webdev.kpoint.api.mobile.util.WSApplicationException;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.User;

public class Domain {

	public final static String UNAUTHORIZED = "UNAUTHORIZED";

	/**
	 * Determines if the authenticated user is allowed to access the target
	 * resource. An authenticated user can only access their own user data.
	 * 
	 * @param targetUserId
	 */
	public static void authorizationCheck(int targetUserId, User authenticatedUser,	String unAuthorizedMessage) {
		if (targetUserId != authenticatedUser.getUserId()) {
			WSApplicationError err = new WSApplicationError(UNAUTHORIZED, unAuthorizedMessage, Response.Status.UNAUTHORIZED);
			throw new WSApplicationException(err, err.getResponse());
		}
	}

	/**
	 * Determines if the authenticated user is allowed to access the target
	 * resource. An authenticated user can only access their own user data.
	 * 
	 * @param targetUsername
	 */
	public static void authorizationCheck(String targetUsername, User authenticatedUser, String unAuthorizedMessage) {
		if (!targetUsername.equalsIgnoreCase(authenticatedUser.getUsername())) {
			WSApplicationError err = new WSApplicationError(UNAUTHORIZED,
					unAuthorizedMessage, Response.Status.UNAUTHORIZED);
			throw new WSApplicationException(err, err.getResponse());
		}
	}

	/**
	 * Determines if the authenticated user is a KP admin.  Some functionality, such as getting a list of user packages
	 * at a KinekPoint, should only be available to users that are Kinekpoint admins.  This prevents a consumer from
	 * viewing another consumers packages.
	 * 
	 * @param targetUsername
	 */
	public static void isKPAdminAuthorizationCheck(User authenticatedUser, String unAuthorizedMessage) {
		if (!(authenticatedUser.getDepotStaffAccessCheck() || authenticatedUser.getDepotAdminAccessCheck()))
		{
			WSApplicationError err = new WSApplicationError(UNAUTHORIZED, unAuthorizedMessage, Response.Status.UNAUTHORIZED);
			throw new WSApplicationException(err, err.getResponse());
		}
	} 

	/**
	 * Determines if the authenticated user is allowed to access the target
	 * resource. An authenticated user can only access their own user data. If
	 * the authenticated user is not in the list of targetUsers, access is
	 * denied.
	 * 
	 * @param targetUsers
	 *            A list of target users to authorize against.
	 */
	public static void authorizationCheck(Set<User> targetUsers, User authenticatedUser, String unAuthorizedMessage) {
		boolean authorized = false;
		Iterator<User> entries = targetUsers.iterator();
		while (entries.hasNext()) {
			User user = (User) entries.next();
			if (user.getUserId() == authenticatedUser.getUserId()) {
				authorized = true;
				break;
			}
		}

		if (!authorized) {
			WSApplicationError err = new WSApplicationError(UNAUTHORIZED,
					unAuthorizedMessage, Response.Status.UNAUTHORIZED);
			throw new WSApplicationException(err, err.getResponse());
		}
	}
	
	/**
	 * Determines if the authenticated user is allowed to access the target
	 * KinekPoint. An authenticated user can only access data for KinekPoints that
	 * they are mapped to. If the authenticated user is not mapped to the KinekPoint
	 * access is denied.
	 * 
	 * @param targetKinekPointId  The target KP to authorize against.
	 */
	public static void kinekPointAuthorizationCheck(int targetKinekPointId, User authenticatedUser, String unAuthorizedMessage) {
		boolean authorized = false;
		Set<KinekPoint> mappedKinekPoints = authenticatedUser.getKinekPoints();
		Iterator<KinekPoint> entries = mappedKinekPoints.iterator();
		while (entries.hasNext()) {
			KinekPoint kp = (KinekPoint) entries.next();
			if (kp.getDepotId() == targetKinekPointId) {
				authorized = true;
				break;
			}
		}

		if (!authorized) {
			WSApplicationError err = new WSApplicationError(UNAUTHORIZED,
					unAuthorizedMessage, Response.Status.UNAUTHORIZED);
			throw new WSApplicationException(err, err.getResponse());
		}
	}

	
	public static String generateRandomString(int length)
	{
	    Random rand = new Random();
	    String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
	    char[] text = new char[length];
	    for (int i = 0; i < length; i++)
	    {
	        text[i] = characters.charAt(rand.nextInt(characters.length()));
	    }
	    return new String(text);
	}
}
