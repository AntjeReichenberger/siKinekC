package org.webdev.kpoint.api.mobile.util;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class WSApplicationException extends WebApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3411905236942029071L;
	public WSApplicationError appError;

	public WSApplicationException() {
		super();
	}

	public WSApplicationException(WSApplicationError err, Response res) {
		super(res);
		appError = err;
	}

	public WSApplicationError getApplicationError() {
		return appError;
	}
}
