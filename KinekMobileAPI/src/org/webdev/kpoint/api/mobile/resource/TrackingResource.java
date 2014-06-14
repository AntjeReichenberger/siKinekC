package org.webdev.kpoint.api.mobile.resource;

import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.webdev.kpoint.api.mobile.domain.CourierDomain;
import org.webdev.kpoint.api.mobile.domain.TrackingDomain;
import org.webdev.kpoint.api.mobile.util.WSApplicationError;
import org.webdev.kpoint.api.mobile.util.WSApplicationException;
import org.webdev.kpoint.api.mobile.util.WSLogger;
import org.webdev.kpoint.bl.pojo.Courier;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.pojo.UserTracking;
import org.webdev.kpoint.bl.tracking.TrackedPackage;
import org.webdev.kpoint.bl.api.mapper.request.Tracking;

import com.google.gson.Gson;

/**
 * This class provides access to all Tracking related operations and
 * functionality. It follows the REST standards by using the proper HTTP Method
 * based on the operation that is invoked. For example, GET (Fetch), POST
 * (Create), PUT (Update), and DELETE (Remove).
 * 
 * All user related API operations fall under the <code>/tracking</code>
 * context.
 */
@Path("/tracking")
public class TrackingResource extends BaseResource {

	WSLogger logger = new WSLogger(TrackingResource.class);
	@Context HttpServletRequest request;
	
	/**
	 * Retrieves detailed information for the specified Tracking record.
	 * 
	 * @param trackingNumber
	 *            The unique identifier of the Tracking record
	 * @return A Response object containing the tracking details
	 * 
	 */
	@GET
	@Path("{trackingId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("trackingId") int trackingId) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("trackingId", String.valueOf(trackingId));
		logger.debug("Request to retrieve Tracking Details", logData);

		String json = "";
		try {
			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			TrackedPackage tracking = TrackingDomain.getTrackingDetails(trackingId, authenticatedUser);
			logger.info("Tracking details successfully retrieved.", logData);

			json = gson.toJson(new org.webdev.kpoint.bl.api.mapper.response.TrackedPackage(tracking));
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
	 * Creates a new Tracking record. This method accepts valid JSON which can
	 * be a subset of the complete JSON defining a tracking entity.
	 * 
	 * @param trackingData
	 *            JSON describing the tracking record to be created
	 * @return A Response object containing a JSON representation of the new
	 *         tracking data.
	 * 
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String trackingData) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("TrackingData", trackingData);
		logger.debug("Request to create Tracking record", logData);

		String json = "";
		try {
			Gson gson = new Gson();
			Tracking trackingToCreate = gson.fromJson(trackingData, Tracking.class);
			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			UserTracking tracking = TrackingDomain.createTrackingRecord(trackingToCreate, authenticatedUser);
			logger.info("Tracking record successfully created.", logData);
			json = gson.toJson(tracking);
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
	 * @deprecated As of October 12, 2011 this method has been deprecated and replaced with the method named
	 * getCouriersForTracking() in the CourierResource.  This method should no longer be used.
	 * 
	 * @return A list of couriers available for tracking
	 * 
	 */
	@GET
	@Path("couriers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCouriers() {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logger.debug("Request to retrieve tracking couriers", logData);

		String json = "";
		try {
			List<Courier> couriers = CourierDomain.getTrackingCouriers();
			logger.info("Couriers successfully retrieved.", logData);

			json = gson.toJson(couriers);
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

}
