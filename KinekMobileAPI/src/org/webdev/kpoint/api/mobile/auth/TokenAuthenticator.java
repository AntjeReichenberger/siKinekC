package org.webdev.kpoint.api.mobile.auth;

import java.security.NoSuchAlgorithmException;

import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.User;


public class TokenAuthenticator implements Authenticator{
	
	private String token = "";
	
	public TokenAuthenticator(String token)
	{
		this.token = token;
	}
	
	public User authenticate() throws Exception{
		UserDao userDAO = new UserDao();
		User user = userDAO.authenticateConsumer(token);
		return user;
	}
	
	public String getAuthenticationCredentials() throws NoSuchAlgorithmException{
		return "Token="+token;
	}
}
