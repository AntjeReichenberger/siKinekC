package org.webdev.kpoint.api.partner.resource;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.webdev.kpoint.api.partner.domain.UsersDomain;
import org.webdev.kpoint.api.partner.util.WSApplicationError;
import org.webdev.kpoint.api.partner.util.WSApplicationException;
import org.webdev.kpoint.api.partner.util.WSLogger;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.pojo.KinekPartner;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.api.partner.resource.BaseResource;

import com.google.gson.Gson;

/**
 * This class provides access to all User related operations and functionality.  It follows the REST
 * standards by using the proper HTTP Method based on the operation that is invoked.  For example,
 * GET (Fetch), POST (Create), PUT (Update), and DELETE (Remove).
 * 
 * All user related API operations fall under the <code>/users</code> context.
 */
@Path("/users")
public class UsersResource extends BaseResource {
	
	WSLogger logger = new WSLogger(UsersResource.class);
	@Context HttpServletRequest request;
	
	/**
	 * Creates a new KinekPoint User. This method accepts valid JSON which can be a subset of the 
	 * complete JSON defining a user.
	 * @param userData JSON describing the user to be created
	 * @return A Response object containing a JSON representation of the new user.
	 * 
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String userData) {
		Hashtable<String,String> logData = new Hashtable<String,String>();
	  	logData.put("UserData", userData);
	  	logger.debug("Request to create User", logData);
	  	
		String json = "";
		try{
			Gson gson = new Gson();
			User userToCreate = gson.fromJson(userData, User.class);
			JSONObject obj= (JSONObject)JSONValue.parse(userData);
       		String stateCode = (String)obj.get("stateCode");
       		String kinekPointId = (String)obj.get("kinekPointId");
       		
       		KinekPartner activeUser = (KinekPartner)request.getAttribute(ACTIVE_USER);
       		User user = UsersDomain.createUser(userToCreate, stateCode, kinekPointId, activeUser.getId());
			logger.info("Users successfully created.", logData);
			json = gson.toJson(new org.webdev.kpoint.bl.api.mapper.response.User(user));
		}
		catch(WSApplicationException wsaex){
			logger.error(wsaex.getApplicationError());
			throw wsaex;
		}
		catch(ApplicationException aex){
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR, aex.getCause());
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}
		catch(Exception ex){
			WSApplicationError err = new WSApplicationError(UNEXPECTED_ERROR, ex);
			logger.error(err);
			throw new WSApplicationException(err, err.getResponse());
		}
		
		return Response.ok(json).build();
	}
}
