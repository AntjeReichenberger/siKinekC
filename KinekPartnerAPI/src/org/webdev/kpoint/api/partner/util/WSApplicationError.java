package org.webdev.kpoint.api.partner.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.webdev.kpoint.bl.persistence.ErrorDAO;
import org.webdev.kpoint.bl.pojo.Error;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import javax.ws.rs.core.Response.Status;


public class WSApplicationError{
		
	private Error internalError = new Error();
	private Throwable internalException;
	private String detailedMessage;
	
	private transient Response exceptionResponse;
	
	public WSApplicationError(){
	}
	
	public WSApplicationError(String errorCode, String detailedMessage){
		internalError = getError(errorCode);
		this.detailedMessage = detailedMessage;
		
		exceptionResponse = Response.status(Status.INTERNAL_SERVER_ERROR).entity(generateFormattedErrorMsg()).type(MediaType.APPLICATION_JSON).build();
	}
	
	public WSApplicationError(String errorCode,Status status){
		internalError = getError(errorCode);
				
		exceptionResponse = Response.status(status).entity(generateFormattedErrorMsg()).type(MediaType.APPLICATION_JSON).build();
	}		
	
	public WSApplicationError(String errorCode, String detailedMessage, Status status){
		internalError = getError(errorCode);
		this.detailedMessage = detailedMessage;
		
		exceptionResponse = Response.status(status).entity(generateFormattedErrorMsg()).type(MediaType.APPLICATION_JSON).build();
	}	
	
	public WSApplicationError(String errorCode, Throwable ex, Status status) {
		internalError = getError(errorCode);
		internalException = ex;

		exceptionResponse = Response.status(status)
				.entity(generateFormattedErrorMsg())
				.type(MediaType.APPLICATION_JSON).build();
	}

	public WSApplicationError(String errorCode, Throwable ex) {
		internalError = getError(errorCode);
		internalException = ex;

		exceptionResponse = Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity(generateFormattedErrorMsg())
				.type(MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * Retrieves error information from the database.  If an error is not found in the database, a generic error will be created.
	 * @param errorCode The error code to retrieve from the database
	 */
	private Error getError(String errorCode)
	{
		Error error = null;
		try{
			ErrorDAO errorDao=new ErrorDAO();
			error=errorDao.read(errorCode);
			if(error == null){
				error = new Error("UNSPECIFIED", "An unspecified error has occurred.");	
			}
		}
		catch(Exception e){
			error = new Error("UNSPECIFIED", "An unspecified error has occurred.");	
		}
		
		return error;
	}

	public String convertPrintStackToString(){
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		internalException.printStackTrace(printWriter);
		printWriter.flush();
		String stackTraceString = stringWriter.toString();
		stackTraceString = stackTraceString.replace('\n', ' ');
		stackTraceString = stackTraceString.replace('\t', ' ');
		stackTraceString = stackTraceString.replace('\r', ' ');
		
		return stackTraceString;
	}
	
	public String generateFormattedErrorMsg(){

		Gson gson = new Gson();
		
		String json = "{ \n"; 
		if(internalError != null)
		{
			json += "\"errorcode\": \"" + internalError.getErrorCode() + "\", \n";
			json += "\"friendlyMessage\": \"" + internalError.getFriendlyMessage() + "\", \n";
		}

		if(internalException != null)
		{
			json += "\"exception\": \"" + convertPrintStackToString() + "\" \n";
		}
		else{
			json += "\"exception\": \"" + detailedMessage + "\" \n";
		}
	
		json += "}\n";
		
		return json;
		
	}
	
	public String getDetailedMessage(){
		return detailedMessage;
	}
		
	public Error getInternalError(){
		return internalError;
	}

	public Throwable getException(){
		return internalException;
	}
	
	public Response getResponse(){
		return exceptionResponse;
	}

}
