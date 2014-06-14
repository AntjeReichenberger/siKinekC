package org.webdev.kpoint.api.mobile.resource;

import static org.webdev.kpoint.bl.api.mapper.response.util.CollectionConstructor.construct;

import java.util.Hashtable;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.webdev.kpoint.api.mobile.domain.CourierDomain;
import org.webdev.kpoint.api.mobile.domain.TrackingDomain;
import org.webdev.kpoint.api.mobile.util.WSApplicationError;
import org.webdev.kpoint.api.mobile.util.WSApplicationException;
import org.webdev.kpoint.api.mobile.util.WSLogger;
import org.webdev.kpoint.bl.pojo.Courier;

/**
 * This class provides access to all Courier related operations and
 * functionality. It follows the REST standards by using the proper HTTP Method
 * based on the operation that is invoked. For example, GET (Fetch), POST
 * (Create), PUT (Update), and DELETE (Remove).
 * 
 * All Courier related API operations fall under the
 * <code>/couriers</code> context.
 */
@Path("/couriers")
public class CourierResource extends BaseResource {
	
	WSLogger logger = new WSLogger(CourierResource.class);
	
	/**
	 * Retrieves a list of all couriers
	 * 
	 * @return A list of all couriers
	 * 
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCouriers() {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logger.debug("Request to retrieve all couriers", logData);
		
		String json = "";
		try{
			List<Courier> couriers = CourierDomain.getCouriers();
			logger.info("Courier list successfully retrieved. Count: "
					+ couriers.size(), logData);
			
			json = gson.toJson(construct(couriers,	org.webdev.kpoint.bl.api.mapper.response.Courier.class));
		}	catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError(), logData);
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR,
					ex);
			logger.error(err, logData);
			throw new WSApplicationException(err, err.getResponse());
		}
		
		return Response.ok(json).build();
	}
	
	/**
	 * Retrieves a list of couriers that are available for package tracking
	 * 
	 * @return A list of couriers available for package tracking
	 * 
	 */
	@GET
	@Path("trackable")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCouriersForTracking() {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logger.debug("Request to retrieve tracking couriers", logData);

		String json = "";
		try {
			List<Courier> couriers = CourierDomain.getTrackingCouriers();
			logger.info("Couriers available for tracking successfully retrieved.", logData);

			json = gson.toJson(couriers);
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
}
