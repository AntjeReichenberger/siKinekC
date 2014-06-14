package org.webdev.kpoint.api.partner.resource;

import static org.webdev.kpoint.bl.api.mapper.response.util.CollectionConstructor.construct;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.ws.rs.GET;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.webdev.kpoint.api.partner.resource.BaseResource;
import org.webdev.kpoint.api.partner.domain.KinekPointsDomain;

import org.webdev.kpoint.api.partner.util.WSApplicationError;
import org.webdev.kpoint.api.partner.util.WSApplicationException;
import org.webdev.kpoint.api.partner.util.WSLogger;
import org.webdev.kpoint.bl.pojo.KinekPoint;


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
	 * @return JSON response that includes all KinekPoints that matched the search criteria
	 * 
	 * @see <a target="new" href="KinekPoints.txt">KinekPoints Response
	 *      Schema</a>
	 */
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@QueryParam("address1") String address1,
			@QueryParam("address2") String address2,
			@QueryParam("city") String city,
			@QueryParam("stateprov") String stateprov,
			@QueryParam("country") String country,
			@QueryParam("onlyBorderLocations") Boolean onlyBorderLocations) {

		Hashtable<String, String> logData = new Hashtable<String, String>();
		logData.put("address1", address1);
		logData.put("address2", address2);
		logData.put("city", city);
		logData.put("stateprov", stateprov);
		logData.put("country", country);
		logData.put("onlyBorderLocations", onlyBorderLocations.toString());
		logger.debug("Request to retrieve KinekPoints", logData);

		String json = "";
		try {
			//in km
			double searchDistance = 100;
			
			List<KinekPoint> kpoints = new ArrayList<KinekPoint>();
			while(kpoints.size() == 0 && searchDistance < 500){
				kpoints = KinekPointsDomain.getKinekPoints(address1,address2,city,stateprov,country, onlyBorderLocations, searchDistance);
				searchDistance = searchDistance + 100;
			}
			Gson gson = new Gson();
			json = gson.toJson(construct(kpoints, org.webdev.kpoint.bl.api.mapper.response.kinekpoint.KinekPoint.class));

		} catch (WSApplicationException aex) {
			logger.error(aex.getApplicationError(), logData);
			throw aex;
		} catch (Exception ex) {
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR, ex);
			logger.error(err, logData);
			throw new WSApplicationException(err, err.getResponse());
		}

		return Response.ok(json).build();
	}
	
	/**
	 * Retrieves detailed information for the specified KinekPoint.
	 * @param kinekPointId The unique identifier of the KinekPoint
	 * @return A Response object containing the details of the KP
	 * 
	 */
	@GET
	@Path("{kinekPointId}")
	@Produces(MediaType.APPLICATION_JSON)	
	public Response get(@PathParam ("kinekPointId") int kinekPointId){	
		Hashtable<String,String> logData = new Hashtable<String,String>();
	  	logData.put("kinekPointId", String.valueOf(kinekPointId));
		logger.debug("Request to retrieve KinekPoint", logData);
		
		String json = "";
		try{
			KinekPoint kp = KinekPointsDomain.getKinekPoint(kinekPointId);
			logger.info("KinekPoint successfully retrieved.", logData);
			
			Gson gson = new Gson();
			json = gson.toJson(new org.webdev.kpoint.bl.api.mapper.response.kinekpoint.KinekPoint(kp));
		}
		catch(WSApplicationException aex){
			logger.error(aex.getApplicationError());
			throw aex;
		}
		catch(Exception ex){
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR, ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}	
		return Response.ok(json).build();
	}
}
