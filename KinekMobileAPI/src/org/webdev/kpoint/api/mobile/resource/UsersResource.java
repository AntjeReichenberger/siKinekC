package org.webdev.kpoint.api.mobile.resource;

import static org.webdev.kpoint.bl.api.mapper.response.util.CollectionConstructor.construct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.MDC;
import org.webdev.kpoint.api.mobile.domain.DeviceTokenDomain;
import org.webdev.kpoint.api.mobile.domain.KinekPointsDomain;
import org.webdev.kpoint.api.mobile.domain.PackagesDomain;
import org.webdev.kpoint.api.mobile.domain.TrackingDomain;
import org.webdev.kpoint.api.mobile.domain.UsersDomain;
import org.webdev.kpoint.api.mobile.util.WSApplicationError;
import org.webdev.kpoint.api.mobile.util.WSApplicationException;
import org.webdev.kpoint.api.mobile.util.WSLogger;
import org.webdev.kpoint.bl.api.mapper.request.UserNotifications;
import org.webdev.kpoint.bl.api.mapper.response.UserKinekPointsResponse;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.pojo.DeviceToken;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.pojo.UserTracking;

/**
 * This class provides access to all User related operations and functionality.
 * It follows the REST standards by using the proper HTTP Method based on the
 * operation that is invoked. For example, GET (Fetch), POST (Create), PUT
 * (Update), and DELETE (Remove).
 * 
 * All user related API operations fall under the <code>/users</code> context.
 */
@Path("/users")
public class UsersResource extends BaseResource {

	WSLogger logger = new WSLogger(UsersResource.class);
	@Context HttpServletRequest request;
	
	/**
	 * Authenticates a user during login. ALL requests to resources are
	 * authenticated via a SecurityFilter using Basic Auth. The SecurityFilter
	 * is responsible for user authentication and if the user is invalid, it
	 * throws an unauthorized exception before reaching the target resource.
	 * 
	 * If the target resource has been reached, this means the user has already
	 * been authenticated and no further action is required.
	 * 
	 * @return A Response object containing a representation of the
	 *         authenticated user
	 * 
	 */
	@GET
	@Path("authenticate")
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticate() {
		String username = MDC.get("username");
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("userName", username);
		logger.debug("User authenticated.", logData);

		String json = "";
		try {
			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			int loginRecordId = UsersDomain.createLoginRecord(authenticatedUser.getUserId(), authenticatedUser.getApp());
			logData.put("App", authenticatedUser.getApp().toString());
			logData.put("Record Id", String.valueOf(loginRecordId));
			logger.info("Login history record created.", logData);
			
			User user = UsersDomain.getUser(username, authenticatedUser);
			logger.info("Authenticated User successfully retrieved.", logData);

			json = gson.toJson(new org.webdev.kpoint.bl.api.mapper.response.User(user));
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR,
					ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}
		return Response.ok(json).build();
	}

	/**
	 * Handles all of the required user population and mapping when an
	 * authentication request is received from Janrain OpenID. The mobile applications do
	 * not provide an opportunity for manual account linking.  This method will attempt
	 * to link accounts automatically, but if no automatic link is found (based on ID or email)
	 * a new account will be created. The method also updates the user profile
	 * with a valid authentication token.
	 * 
	 * This method is public (no security) and must be used when a user attempts
	 * to sign in or sign up using the third-party Janrain component.
	 * 
	 * @return A Response object containing a representation of the
	 *         authenticated user
	 * @throws Exception
	 * 
	 */
	@POST
	@Path("janrainauth")
	@Produces(MediaType.APPLICATION_JSON)
	public Response janrainAuthenticate(String tokenBody) throws Exception {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("tokenBody", tokenBody);
		logger.debug("Request to authenticate user via Janrain", logData);

		String json = "";
		try {
			User user = UsersDomain.authenticateJanrainUser(tokenBody);
			logger.info("Janrain user authenticated.", logData);

			int loginRecordId = UsersDomain.createLoginRecord(user.getUserId(), user.getApp());
			logData.put("App", user.getApp().toString());
			logData.put("Record Id", String.valueOf(loginRecordId));
			logger.info("Login history record created.", logData);
			
			json = gson.toJson(new org.webdev.kpoint.bl.api.mapper.response.User(user));
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR,
					ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}

		return Response.ok(json).build();
	}
	

	/**
	 * Retrieves detailed information for the specified user. Throws an
	 * unauthorized exception if an attempt is made to retrieve user data that
	 * is not associated with the authenticated user.
	 * 
	 * @param userName
	 *            The username of the user to retrieve
	 * @return A Response object containing a representation of the user
	 * 
	 */
	@GET
	@Path("{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getById(@PathParam("userId") int userId) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("userId", String.valueOf(userId));
		logger.debug("Request to retrieve User by ID", logData);

		String json = "";
		try {
			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			User user = UsersDomain.getUser(userId, authenticatedUser);
			logger.info("User successfully retrieved.", logData);

			json = gson.toJson(new org.webdev.kpoint.bl.api.mapper.response.User(user));
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR, ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}
		return Response.ok(json).build();
	}

	/**
	 * Retrieves detailed information for the specified user. Throws an
	 * unauthorized exception if an attempt is made to retrieve user data that
	 * is not associated with the authenticated user.
	 * 
	 * @param userName
	 *            The username of the user to retrieve
	 * @return A Response object containing a representation of the user
	 * 
	 */
	@GET
	@Path("username/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("username") String userName) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("userName", userName);
		logger.debug("Request to retrieve User", logData);

		String json = "";
		try {
			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			User user = UsersDomain.getUser(userName, authenticatedUser);
			logger.info("User successfully retrieved.", logData);

			json = gson
					.toJson(new org.webdev.kpoint.bl.api.mapper.response.User(
							user));
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR,
					ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}
		return Response.ok(json).build();
	}

	/**
	 * Retrieves a user based on KinekNumber.  The kinek number must be an exact match.
	 * 
	 * @param userKinekNumber
	 *            The kinek number of the user to retrieve
	 * @return A Response object containing a representation of the user
	 * 
	 */
	@GET
	@Path("kineknumber/{kinekNumber}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserByKinekNumber(@PathParam("kinekNumber") String kinekNumber) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("userKinekNumber", kinekNumber);
		logger.debug("Request to retrieve User by KinekNumber", logData);

		String json = "";
		try {
			User user = UsersDomain.getUserByKinekNumber(kinekNumber);
			logger.info("User successfully retrieved.", logData);

			json = gson.toJson(new org.webdev.kpoint.bl.api.mapper.response.User(user));
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR, ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}
		return Response.ok(json).build();
	}

	/**
	 * Performs a user search based on the provided query string parameters.  If KinekNumber is provided, the search is based on that
	 * attribute and first name and last name are ignored.  If kinekPointWithPendingPackages is provided, the search only returns users that
	 * have packages pending pickup at the specified KinekPoint.  Only KP admin users have permission to call this method.
	 * 
	 * @param kinekNumber
	 *            The kinekNumber of the user to retrieve
	 * @param firstName
	 *            The first name of the user to retrieve
	 * @param lastName
	 *            The last name of the user to retrieve
     * @param kinekPointWithPendingPackages
     *   		  The KP ID of the kinekPoint that has pending packages for the user
     * @return A Response object containing a representation of the user
	 * 
	 */
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@QueryParam("kinekNumber") String kinekNumber, @QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName, @QueryParam("kinekPointWithPendingPackages") int kinekPointId) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("kinekNumber", kinekNumber == null ? "" : kinekNumber);
		logData.put("firstName", firstName == null ? "" : firstName);
		logData.put("lastName", lastName == null ? "" : lastName);
		logData.put("kinekPointWithPendingPackages", String.valueOf(kinekPointId));
		logger.debug("Request to perform a user search", logData);

		String json = "";
		try {
			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			List<User> users = UsersDomain.getUsers(kinekNumber, firstName, lastName, kinekPointId, authenticatedUser);
			logger.info("User(s) successfully retrieved.", logData);

			json = gson.toJson(construct(
					users,
					org.webdev.kpoint.bl.api.mapper.response.User.class));
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR,
					ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}
		return Response.ok(json).build();
	}

	/**
	 * Retrieves the list of packages with the specified status for the user.
	 * 
	 * @param userName
	 *            The userid of the user
	 * @return A Response object containing a JSON representation of the
	 *         KinekPoint
	 * 
	 */
	@GET
	@Path("{userId}/packages/{packageStatus}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserPackages(@PathParam("userId") int userId,
			@PathParam("packageStatus") String packageStatus) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("userId", String.valueOf(userId));
		logData.put("packageStatus", packageStatus);
		logger.debug("Request to retrieve users packages", logData);

		String json = "";
		try {
			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			List<PackageReceipt> packageReceipts = PackagesDomain.getPackages(userId, packageStatus, authenticatedUser);
			logger.info("Users packages successfully retrieved.", logData);

			json = gson
					.toJson(construct(
							packageReceipts,
							org.webdev.kpoint.bl.api.mapper.response.PackageReceipt.class));
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR,
					ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}
		return Response.ok(json).build();
	}
	
	/**
	 * Retrieves the list of packages with the specified status for the user that are located at the specified KinekPoint.
	 * 
	 * @param userId  The userid of the user
	 * @param packageStatus The status for packages that should be retrieved
	 * @param kinekPointId The id of the Kinekpoint
	 * @return A Response object containing a JSON representation of the packages
	 * 
	 */
	@GET
	@Path("{userId}/packages/{packageStatus}/kinekpoint/{kinekPointId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserPackages(@PathParam("userId") int userId,
									@PathParam("packageStatus") String packageStatus,
									@PathParam("kinekPointId") int kinekPointId) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("userId", String.valueOf(userId));
		logData.put("packageStatus", packageStatus);
		logData.put("kinekPointId", String.valueOf(kinekPointId));
		logger.debug("Request to retrieve users packages at a specified KinekPoint", logData);

		String json = "";
		try {
			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			List<PackageReceipt> packageReceipts = PackagesDomain.getPackages(userId, packageStatus, kinekPointId, authenticatedUser);
			logger.info("Users packages successfully retrieved.", logData);

			json = gson
					.toJson(construct(
							packageReceipts,
							org.webdev.kpoint.bl.api.mapper.response.PackageReceipt.class));
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR, ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}
		return Response.ok(json).build();
	}
	
	/**
	 * Retrieves the list of shipments that are being tracked for the user.
	 * 
	 * @param userId  The userid of the user to retrieve tracking records for
	 * @return A Response object containing a JSON representation of the
	 *         Tracking records
	 * 
	 */
	@GET
	@Path("{userId}/tracking")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserTrackingRecords(@PathParam("userId") int userId) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("userId", String.valueOf(userId));
		logger.debug("Request to retrieve users tracking records", logData);

		String json = "";
		try {
			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			List<UserTracking> trackings = TrackingDomain.getTrackingRecords(userId, authenticatedUser);
			Collections.sort(trackings);
			logger.info("Users trackings successfully retrieved.", logData);

			json = gson.toJson(trackings);
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR,
					ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}
		return Response.ok(json).build();
	}

	/**
	 * Deletes the Tracking record for the specified user.
	 * 
	 * @param trackingId
	 *            The tracking id of the record to delete.
	 * @return A Response object containing a JSON representation of the new
	 *         tracking data.
	 * 
	 */
	@DELETE
	@Path("{userId}/tracking/{trackingId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUserTrackingRecord(@PathParam("userId") int userId, @PathParam("trackingId") int trackingId) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("UserID", String.valueOf(userId));
		logData.put("TrackingID", String.valueOf(trackingId));
		logger.debug("Request to delete Tracking record", logData);

		try {
			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			TrackingDomain.deleteTrackingRecord(userId, trackingId, authenticatedUser);
			logger.info("Tracking record successfully deleted.", logData);
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR,
					ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}

		return Response.ok().build();
	}
	
	
	/**
	 * Creates a new KinekPoint User. This method accepts valid JSON which can
	 * be a subset of the complete JSON defining a user.
	 * 
	 * @param userData
	 *            JSON describing the user to be created
	 * @return A Response object containing a JSON representation of the new
	 *         user.
	 * 
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String userData) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("UserData", userData);
		logger.debug("Request to create User", logData);

		String json = "";
		try {
			org.webdev.kpoint.bl.api.mapper.request.User userToCreate = gson.fromJson(
					userData,
					org.webdev.kpoint.bl.api.mapper.request.User.class);
			
			User user = UsersDomain.createUser(userToCreate);
			logger.info("Users successfully created.", logData);
			json = gson.toJson(new org.webdev.kpoint.bl.api.mapper.response.User(user));
		} catch (WSApplicationException wsaex) {
			logger.error(wsaex.getApplicationError());
			throw wsaex;
		} catch (ApplicationException aex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR, aex.getCause());
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR, ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}

		return Response.ok(json).build();
	}

	/**
	 * Updates or adds new information to an existing KinekPoint user.
	 * 
	 * @param userData
	 *            JSON describing the user to be created
	 * @return A Response object containing a JSON representation of the new
	 *         user.
	 */
	@POST
	@Path("{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("userId") String userId, String userData) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("UserData", userData);
		logger.debug("Request to update User", logData);

		String json = "";
		try {
			org.webdev.kpoint.bl.api.mapper.request.User user = gson.fromJson(
					userData,
					org.webdev.kpoint.bl.api.mapper.request.User.class);
			user.setUserId(Integer.valueOf(userId));

			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			User updatedUser = UsersDomain.updateUser(user, authenticatedUser);
			logger.info("User successfully updated.", logData);
			json = gson
					.toJson(new org.webdev.kpoint.bl.api.mapper.response.User(
							updatedUser));
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR,
					ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}

		return Response.ok(json).build();
	}

	/**
	 * Updates the notifications that a user has subscribed to receive.
	 * 
	 * @param notificationData
	 *            JSON describing the notification settings for the user
	 * @return A Response object containing a JSON representation of the new
	 *         user.
	 */
	@POST
	@Path("{userId}/notifications")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateNotifications(@PathParam("userId") int userId,
			String notificationData) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("NotificationData", notificationData);
		logger.debug("Request to update User notifications", logData);

		String json = "";
		try {
			UserNotifications notifications = gson.fromJson(notificationData,
					UserNotifications.class);

			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			User updatedUser = UsersDomain.updateUserNotifications(userId, notifications, authenticatedUser);
			logger.info("User notifications successfully updated.", logData);
			json = gson
					.toJson(new org.webdev.kpoint.bl.api.mapper.response.User(
							updatedUser));
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR,
					ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}

		return Response.ok(json).build();
	}

	/**
	 * Adds a new device token for the specified user
	 * 
	 * @param token
	 *            the token that will be added to the user
	 * @param userId
	 *            the user associated with the token
	 * @return A Response object containing a JSON representation of the new
	 *         user.
	 */
	@PUT
	@Path("{userId}/devicetokens")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addDeviceToken(@PathParam("userId") int userId, String token) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("Token", token);
		logger.debug("Request to add a new device token", logData);

		String json = "";
		try {
			DeviceToken deviceToken = gson.fromJson(token, DeviceToken.class);
			deviceToken.setUserId(userId);

			deviceToken = DeviceTokenDomain.createToken(deviceToken);
			logger.info("Device Token added.", logData);
			json = gson.toJson(deviceToken);
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR,
					ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}

		return Response.ok(json).build();
	}
	
	/**
	 * Removes a device token for the specified user
	 * 
	 * @param token
	 *            the device token that will be removed
	 * @return A 200 OK Response
	 */
	@DELETE
	@Path("{userId}/devicetokens/{token}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeDeviceToken(@PathParam("userId") int userId,
			@PathParam("token") String token) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("Token", token);
		logger.debug("Request to remove a new device token", logData);

		try {
			DeviceToken deviceToken = new DeviceToken();
			deviceToken.setToken(token);
			deviceToken.setUserId(userId);

			DeviceTokenDomain.removeToken(deviceToken);
			logger.info("Device Token removed.", logData);
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR,
					ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}

		return Response.ok().build();
	}

	/**
	 * Retrieves the list of KinekPoints that are mapped to the specified user.
	 * 
	 * @param userName
	 *            The userid of the user
	 * @return A Response object containing a JSON representation of the
	 *         KinekPoints
	 * 
	 */
	@GET
	@Path("{userId}/kinekpoints")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserKinekPoints(@PathParam("userId") int userId) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("userId", String.valueOf(userId));
		logger.debug("Request to retrieve users KinekPoints", logData);

		String json = "";
		try {
			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			Set<KinekPoint> kp = UsersDomain.getUserKinekPoints(userId, authenticatedUser);
			List<KinekPoint> kpList = new ArrayList<KinekPoint>(kp);
			Collections.sort(kpList);
			logger.info("Users KinekPoints successfully retrieved.", logData);

			UserKinekPointsResponse response = new UserKinekPointsResponse();
			response.setFavoriteKinekPoints(construct(kpList, org.webdev.kpoint.bl.api.mapper.response.kinekpoint.KinekPoint.class));
			json = gson.toJson(response);
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR, ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}
		return Response.ok(json).build();
	}

	/**
	 * Adds a KinekPoint to the specified user's list of saved KinekPoints
	 * 
	 * @param kpMapping
	 *            JSON describing the KP which should be added
	 * @return A 200 OK Response
	 * 
	 */
	@PUT
	@Path("{userId}/kinekpoints")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addKinekPoint(@PathParam("userId") int userId,
			String kpMapping) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("userId", String.valueOf(userId));
		logData.put("KinekPoint", kpMapping);
		logger.debug("Request to add a KP favorite", logData);

		try {
			KinekPoint kpID = gson.fromJson(kpMapping, KinekPoint.class);
			KinekPoint kpToMap = KinekPointsDomain.getKinekPoint(kpID.getDepotId()); //this gives us a fully populated KinekPoint object
			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			KinekPointsDomain.mapKPFavorite(userId, kpToMap, authenticatedUser);
			logger.info("KinekPoint favorite successfully mapped.", logData);
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR,
					ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}

		return Response.ok().build();
	}

	/**
	 * Removes the specified KinekPoint from the user's list of kinekpoints.
	 * 
	 * @param kinekPointId
	 *            The ID of the KinekPoint to remove
	 * @return A 200 OK Response
	 * 
	 */
	@DELETE
	@Path("{userId}/kinekpoints/{kinekPointId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteFavorite(@PathParam("userId") int userId,
			@PathParam("kinekPointId") int kinekPointId) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("userId", String.valueOf(userId));
		logData.put("KinekPoint Id", String.valueOf(kinekPointId));
		logger.debug("Request to delete a KP Favorite", logData);

		try {
			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			KinekPointsDomain.removeKPFavorite(userId, kinekPointId, authenticatedUser);
			logger.info("KP favorite successfully deleted.", logData);
		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError());
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR,
					ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}

		return Response.ok().build();
	}
}
