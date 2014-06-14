package org.webdev.kpoint.api.mobile.resource;

import static org.webdev.kpoint.bl.api.mapper.response.util.CollectionConstructor.construct;

import java.util.Hashtable;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.webdev.kpoint.api.mobile.domain.CountryDomain;
import org.webdev.kpoint.api.mobile.util.WSApplicationError;
import org.webdev.kpoint.api.mobile.util.WSApplicationException;
import org.webdev.kpoint.api.mobile.util.WSLogger;
import org.webdev.kpoint.bl.pojo.Country;

/**
 * This class provides access Countries. It follows the REST standards by using
 * the proper HTTP Method based on the operation that is invoked. For example,
 * GET (Fetch), POST (Create), PUT (Update), and DELETE (Remove).
 * 
 * All Referral Source related API operations fall under the
 * <code>/countries</code> context.
 */
@Path("/countries")
public class CountryResource extends BaseResource {

	WSLogger logger = new WSLogger(CountryResource.class);

	/**
	 * Retrieves a list of all Countries.
	 * 
	 * @return JSON representing all the Countries
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCountries() {

		Hashtable<String, String> logData = new Hashtable<String, String>();
		logger.debug("Request to retrieve Countries", logData);

		String json = "";
		try {
			List<Country> countries = CountryDomain.getCountries();
			logger.info("Country list successfully retrieved. Count: "
					+ countries.size(), logData);

			json = gson.toJson(construct(countries,	org.webdev.kpoint.bl.api.mapper.response.Country.class));

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
}
