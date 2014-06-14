package org.webdev.kpoint.api.mobile.auth;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.webdev.kpoint.api.mobile.util.WSApplicationError;
import org.webdev.kpoint.api.mobile.util.WSApplicationException;
import org.webdev.kpoint.api.mobile.util.WSLogger;
import org.webdev.kpoint.bl.pojo.Role;
import org.webdev.kpoint.bl.pojo.User;

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
  
   private boolean authenticatedResource(HttpServletRequest httpRequest){
	  String requestUri = httpRequest.getRequestURI();
 	  String meth = httpRequest.getMethod();
 	  String contextPath = httpRequest.getContextPath();
 	  
 	  if((requestUri.equals(contextPath+"/rest/users") && meth.equals("PUT"))
 			  || requestUri.equals(contextPath+"/rest/users/janrainauth"))
 	  {
 		  return false;
 	  }
 	  
 	  return true;
   }
   
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, WSApplicationException {
	  HttpServletResponse httpResponse = (HttpServletResponse) response;    	 
 	  httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
 	  
 	  if (filterConfig == null)
         return;
      
      try{
    	  HttpServletRequest httpRequest = (HttpServletRequest) request;
    	  MDC.put("RequestURI", httpRequest.getRequestURI());
          MDC.put("RequestQueryString", httpRequest.getQueryString());
          String appHeader = httpRequest.getHeader("Application");
          MDC.put("App", appHeader != null ? appHeader : "Iphone");
          
          if(!authenticatedResource(httpRequest))
    	  {
			  //unauthenticated resource ... proceed immediately
        	  chain.doFilter(request, response);
              MDC.clear();
              return;
          }  
    	  
    	  String credentials = httpRequest.getHeader("Authorization");
    	  if(credentials == null || credentials.equals(""))
    	  {
    		  WSApplicationError err = new WSApplicationError(INVALID_AUTHENTICATION_TYPE, "No authentication credentials were provided. Credentials string: " + credentials);
        	  logger.error(err);
        	  httpResponse.getWriter().println(err.generateFormattedErrorMsg());
        	  return;
    	  }
    	  
          //determine authentication type
    	  Authenticator authenticator = AuthenticationFactory.getAuthenticator(credentials);
    	  if(authenticator == null){
    		  WSApplicationError err = new WSApplicationError(INVALID_AUTHENTICATION_TYPE, "An unsupported authentication type was provided. Credentials string: " + credentials);
        	  logger.error(err);
        	  httpResponse.getWriter().println(err.generateFormattedErrorMsg());
        	  return;
    	  }
    		  
    	  //call appropriate authenticator
    	  User user = authenticator.authenticate();
    	  if(user == null){
        	  //create 401-unauthorized error
        	  WSApplicationError err = new WSApplicationError(AUTHENTICATION_FAILED, "Invalid authentication attempt.");
        	  
        	  //log unauthorized access
        	  Hashtable<String,String> logData = new Hashtable<String,String>();
        	  logData.put("authCredentials", authenticator.getAuthenticationCredentials());
        	  logData.put("host", httpRequest.getRemoteHost());
        	  logger.info(err.getInternalError().getFriendlyMessage(), logData);
        	  
        	  //throw 401-unauthorized error
        	  httpResponse.getWriter().println(err.generateFormattedErrorMsg());
          }
          else{
        	  //valid client found, setup contextual (MDC) info for remaining log entries and add user to current request
        	  MDC.put("username", user.getUsername());
        	  int roleId = user.getRoleId();
        	  if(roleId == Role.DepotAdmin || roleId == Role.DepotStaff)
        		  user.setApp(User.App.IPHONE_ADMIN_APP);
        	  else if(roleId == Role.Consumer)
        	  {
        		  String app = httpRequest.getHeader("Application");
        		  if(app != null && app.equals("ANDROID_CONSUMER_APP"))
        		  {
        			  user.setApp(User.App.ANDROID_CONSUMER_APP);
        		  }
        		  else
        		  {
        			  //if request variable not specified, assume iphone consumer app.  This assumption should be changed to log "UNSPECIFIED" if request variable not provided, but will require a new iphone release.  Until then, we assume Iphone for default
        			  user.setApp(User.App.IPHONE_CONSUMER_APP);
        		  }
        			  
        	  }  
        	  request.setAttribute("activeUser", user);
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

