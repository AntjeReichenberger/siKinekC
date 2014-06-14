package org.webdev.kpoint.api.mobile.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This is a Base Resource class that all Resources inherit. It does not provide
 * any public facing API operations and should not be used directly. It's
 * purpose is to provide access to common functionality that can be shared
 * amongst all of the API Resources.
 * 
 * There is no context setup for this resource and it is not publicly
 * accessible.
 */
public class BaseResource {
	final static String UNEXPECTED_ERROR = "UNEXPECTED_ERROR";
	
	// Gson is thread safe
	protected final static Gson gson = new GsonBuilder().create();
	
    //key used for fetching active user from request context
	final static String ACTIVE_USER = "activeUser";
}
