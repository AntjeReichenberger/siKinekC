package org.webdev.kpoint.api.mobile.resource;

import static org.webdev.kpoint.bl.api.mapper.response.util.CollectionConstructor.construct;

import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.webdev.kpoint.api.mobile.domain.CouponDomain;
import org.webdev.kpoint.api.mobile.domain.KinekPointsDomain;
import org.webdev.kpoint.api.mobile.domain.UsersDomain;
import org.webdev.kpoint.api.mobile.util.WSApplicationError;
import org.webdev.kpoint.api.mobile.util.WSApplicationException;
import org.webdev.kpoint.api.mobile.util.WSLogger;
import org.webdev.kpoint.bl.api.mapper.response.kinekpoint.KinekPointStatistics;
import org.webdev.kpoint.bl.manager.KinekPointManager;
import org.webdev.kpoint.bl.pojo.Coupon;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.PackageWeightGroup;
import org.webdev.kpoint.bl.pojo.UnifiedPackageRate;
import org.webdev.kpoint.bl.pojo.User;

import com.google.gson.Gson;

/**
 * This class provides access to all KinekPoint related operations and
 * functionality. It follows the REST standards by using the proper HTTP Method
 * based on the operation that is invoked. For example, GET (Fetch), POST
 * (Create), PUT (Update), and DELETE (Remove).
 * 
 * All KinekPoint related API operations fall under the
 * <code>/kinekpoints</code> context.
 */
@Path("/kinekpoints")
public class KinekPointsResource extends BaseResource {

	WSLogger logger = new WSLogger(KinekPointsResource.class);
	@Context HttpServletRequest request;
	
	/**
	 * Retrieves detailed information for the specified KinekPoint.
	 * 
	 * @param kinekPointId
	 *            The unique identifier of the KinekPoint
	 * @return A Response object containing the details of the KP
	 * 
	 */
	@GET
	@Path("{kinekPointId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("kinekPointId") int kinekPointId) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("kinekPointId", String.valueOf(kinekPointId));
		logger.debug("Request to retrieve KinekPoint", logData);

		String json = "";
		try {
			KinekPoint kp = KinekPointsDomain.getKinekPoint(kinekPointId);
			logger.info("KinekPoint successfully retrieved.", logData);

			json = gson.toJson(new org.webdev.kpoint.bl.api.mapper.response.kinekpoint.KinekPoint(kp));
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
	 * Retrieves a list of KinekPoints that are contained within the specified
	 * coordinates. The Search Point latitude and longitude are used to
	 * calculate the distance between each KinekPoint and the Consumers search
	 * location. This allows search results to be prioritized and ordered based
	 * on proximity to the Consumer. The isBorderLocation is a boolean
	 * indicating whether or not the Search should be limited to Border
	 * KinekPoints.
	 * 
	 * The data returned from this method can be used as required to meet the
	 * clients needs. It is typically plotted onto some mapping software (google
	 * or bing) for display the end user.
	 * 
	 * @param minLat
	 *            The minimum latitude value
	 * @param maxLat
	 *            The maximum latitude value
	 * @param minLong
	 *            The minimum longitude value
	 * @param maxLong
	 *            The maximum longitude value
	 * @param searchPointLat
	 *            The latitude of search point
	 * @param searchPointLong
	 *            The longitude of search point
	 * @param onlyBorderLocations
	 *            If true, only Border KinekPoints will be searched
	 * @return JSON response that includes all KinekPoints that matched the
	 *         search criteria
	 * 
	 * @see <a target="new" href="KinekPoints.txt">KinekPoints Response
	 *      Schema</a>
	 */
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@QueryParam("minLat") Double minLat,
			@QueryParam("maxLat") Double maxLat,
			@QueryParam("minLong") Double minLong,
			@QueryParam("maxLong") Double maxLong,
			@QueryParam("searchPointLat") Double searchPointLat,
			@QueryParam("searchPointLong") Double searchPointLong,
			@QueryParam("onlyBorderLocations") boolean onlyBorderLocations) {

		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("minLat", minLat.toString());
		logData.put("maxLat", maxLat.toString());
		logData.put("minLong", minLong.toString());
		logData.put("maxLong", maxLong.toString());
		logData.put("searchPointLat", searchPointLat.toString());
		logData.put("searchPointLong", searchPointLong.toString());
		logData.put("onlyBorderLocations", String.valueOf(onlyBorderLocations));

		logger.debug("Request to retrieve KinekPoints", logData);

		String json = "";
		try {
			List<KinekPoint> kpoints = KinekPointsDomain
					.getKinekPoints(minLat, maxLat, minLong, maxLong,
							searchPointLat, searchPointLong,
							onlyBorderLocations);
			logger.info("KinekPoint list successfully retrieved. Count: " + kpoints.size(), logData);

			json = gson.toJson(construct(kpoints, org.webdev.kpoint.bl.api.mapper.response.kinekpoint.KinekPoint.class));

		} catch (WSApplicationException aex) {
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
	 * Retrieves a list of coupons for the specified KinekPoint.
	 * 
	 * @param kinekPointId
	 *            The unique identifier of the KinekPoint
	 * @return A Response object containing an XML representation of the KP
	 * 
	 */
	@GET
	@Path("{kinekPointId}/coupons")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCoupons(@PathParam("kinekPointId") int kinekPointId) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("kinekPointId", String.valueOf(kinekPointId));
		logger.debug("Request to retrieve KinekPoint coupons", logData);

		String json = "";
		try {
			List<Coupon> coupons = CouponDomain.getCoupons(kinekPointId);
			logger.info("KinekPoint Coupons successfully retrieved.", logData);

			json = gson.toJson(construct(coupons, org.webdev.kpoint.bl.api.mapper.response.Coupon.class));
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
	 * Retrieves statistics for the specified KinekPoint.
	 * 
	 * @param kinekPointId
	 *            The unique identifier of the KinekPoint
	 * @return A Response object containing an JSON representation of the KP stats
	 * 
	 */
	@GET
	@Path("{kinekPointId}/statistics")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStatistics(@PathParam("kinekPointId") int kinekPointId) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("kinekPointId", String.valueOf(kinekPointId));
		logger.debug("Request to retrieve KinekPoint statistics", logData);

		String json = "";
		try {
			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			KinekPointStatistics stats = KinekPointsDomain.getStatistics(kinekPointId, authenticatedUser);
			logger.info("KinekPoint statistics successfully retrieved.", logData);

			Gson gson = new Gson();
			json = gson.toJson(stats);
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
	 * Retrieves a list of package weight groups for the specified KinekPoint.
	 * 
	 * @param kinekPointId
	 *            The unique identifier of the KinekPoint
	 * @return A Response object containing an XML representation of the KP weight groups
	 * 
	 */
	
	@GET
	@Path("{kinekPointId}/weights")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWeights(@PathParam("kinekPointId") int kinekPointId) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("kinekPointId", String.valueOf(kinekPointId));
		logger.debug("Request to retrieve KinekPoint weights", logData);

		String json = "";
		try {
			List<PackageWeightGroup> weightGroups = KinekPointsDomain.getWeightGroups(kinekPointId);
			logger.info("KinekPoint weight groups successfully retrieved.", logData);
			
			Gson gson = new Gson();
			json = gson.toJson(construct(weightGroups, org.webdev.kpoint.bl.api.mapper.response.kinekpoint.PackageWeightGroup.class));
			
		} catch (WSApplicationException aex) {
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
	 * Updates a KinekPoint.
	 * 
	 * @param userData
	 *            JSON describing the user to be created
	 * @return A Response object containing a JSON representation of the new
	 *         KinekPoint.
	 */
	@POST
	@Path("{kinekPointId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("kinekPointId") String kinekPointId, String kinekPointData) {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("KinekPoint ID", kinekPointId);
		logger.debug("Request to update KinekPoint", logData);

		String json = "";
		try {
			org.webdev.kpoint.bl.api.mapper.request.kinekpoint.KinekPoint kinekPoint = gson.fromJson(
					kinekPointData,
					org.webdev.kpoint.bl.api.mapper.request.kinekpoint.KinekPoint.class);
			kinekPoint.setDepotId(Integer.valueOf(kinekPointId));

			User authenticatedUser = (User)request.getAttribute(ACTIVE_USER);
			KinekPoint updatedKinekPoint = KinekPointsDomain.updateKP(kinekPoint, authenticatedUser);
			logger.info("KinekPoint successfully updated.", logData);
			json = gson.toJson(new org.webdev.kpoint.bl.api.mapper.response.kinekpoint.KinekPoint(updatedKinekPoint));
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
