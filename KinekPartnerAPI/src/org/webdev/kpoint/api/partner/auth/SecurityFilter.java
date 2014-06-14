package org.webdev.kpoint.api.partner.auth;

import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.slf4j.MDC;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.webdev.kpoint.api.partner.util.PropertyReader;
import org.webdev.kpoint.api.partner.util.WSApplicationError;
import org.webdev.kpoint.api.partner.util.WSApplicationException;
import org.webdev.kpoint.api.partner.util.WSLogger;
import org.webdev.kpoint.bl.pojo.KinekPartner;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.api.partner.util.*;

public final class SecurityFilter implements Filter {
	
   WSLogger logger = new WSLogger(SecurityFilter.class);
	   
   private final static String AUTHENTICATION_FAILED = "AUTHENTICATION_FAILED";	
   private final static String UNEXPECTED_AUTHENTICATION_ERROR = "AUTHENTICATION_ERROR";	
   private final static String INVALID_AUTHENTICATION_TYPE = "INVALID_AUTHENTICATION_TYPE";	
  	
   private FilterConfig filterConfig = null;
   
   public void init(FilterConfig filterConfig) 
      throws ServletException {
      this.filterConfig = filterConfig;
   }
   
   public void destroy() {
      this.filterConfig = null;
   }

   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, WSApplicationException {
	  HttpServletResponse httpResponse = (HttpServletResponse) response;    	 
 	  httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
 	  
 	  if (filterConfig == null)
         return;
      
      try{
    	  HttpServletRequest httpRequest = (HttpServletRequest) request;
    	  
    	  String credentials = httpRequest.getHeader("AUTHORIZATION");
    	  if(credentials == null || credentials.equals(""))
    	  {
    		  WSApplicationError err = new WSApplicationError(INVALID_AUTHENTICATION_TYPE, "No authentication credentials were provided. Credentials string: " + credentials);
        	  logger.error(err);
        	  httpResponse.getWriter().println(err.generateFormattedErrorMsg());
    	  }
    	  
    	  StringTokenizer authTokens = new StringTokenizer(credentials," ");
    		
	  	  if (authTokens.hasMoreTokens()){
	  		String type = authTokens.nextToken();
	  		credentials = authTokens.nextToken();
	  	  }

    	  BasicAuthenticator authenticator = new BasicAuthenticator(credentials);
    		  
    	  KinekPartner kinekPartner = authenticator.authenticate();
    	  if(kinekPartner == null){
        	  //create 401-unauthorized error
        	  WSApplicationError err = new WSApplicationError(AUTHENTICATION_FAILED, "Invalid authentication attempt.");
        	  
        	  //log unauthorized access
        	  Hashtable<String,String> logData = new Hashtable<String,String>();
        	  logData.put("authCredentials", authenticator.getAuthenticationCredentials());
        	  logData.put("host", httpRequest.getRemoteHost());
        	  logger.error(err, logData);
        	  
        	  //throw 401-unauthorized error
        	  httpResponse.getWriter().println(err.generateFormattedErrorMsg());
          }
          else{
        	  //valid client found, setup contextual (MDC) info for remaining log entries and add user to request context
        	  MDC.put("username", kinekPartner.getToken());
        	  request.setAttribute("activeUser", kinekPartner);
        	  
        	  chain.doFilter(request, response);
              MDC.clear();
          }
      }
      catch(Exception ex){
    	  WSApplicationError err = new WSApplicationError(UNEXPECTED_AUTHENTICATION_ERROR, ex);
    	  logger.error(err);
    	  
    	  //throw 401-unauthorized error
    	  httpResponse.getWriter().println(err.generateFormattedErrorMsg());
      } 
   }

}

