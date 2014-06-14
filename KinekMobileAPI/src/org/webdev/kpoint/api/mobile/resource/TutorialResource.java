package org.webdev.kpoint.api.mobile.resource;

import static org.webdev.kpoint.bl.api.mapper.response.util.CollectionConstructor.construct;

import java.util.Hashtable;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.webdev.kpoint.api.mobile.domain.TutorialDomain;
import org.webdev.kpoint.api.mobile.util.WSApplicationError;
import org.webdev.kpoint.api.mobile.util.WSApplicationException;
import org.webdev.kpoint.api.mobile.util.WSLogger;
import org.webdev.kpoint.bl.pojo.TrainingTutorial;

@Path("/tutorials")
public class TutorialResource extends BaseResource{
	

	WSLogger logger = new WSLogger(TutorialResource.class);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTrainingVideos() {
		Hashtable<String, String> logData = new Hashtable<String, String>();
		logger.debug("Request to retrieve all training videos", logData);
		
		String json = "";
		try{
			List<TrainingTutorial> trainingTutorials = TutorialDomain.getTrainingTutorials();
			logger.info("Tutorial list successfully retrieved. Count: "	+ trainingTutorials.size(), logData);
			
			json = gson.toJson(construct(trainingTutorials,	org.webdev.kpoint.bl.api.mapper.response.TrainingTutorial.class));
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
}