package org.webdev.kpoint.api.partner.util;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;


public class WSApplicationException extends WebApplicationException{
		
	public WSApplicationError appError;
	
	public WSApplicationException(){
		super();
	}
	
	public WSApplicationException(WSApplicationError err, Response res){		
		super(res);
		appError = err;
	}	
	
	public WSApplicationError getApplicationError(){
		return appError;
	}
}
