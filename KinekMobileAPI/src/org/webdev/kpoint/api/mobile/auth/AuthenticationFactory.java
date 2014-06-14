package org.webdev.kpoint.api.mobile.auth;

import java.util.StringTokenizer;

public class AuthenticationFactory {

	private static String basicAuth = "Basic";
	private static String tokenAuth = "Token";
	
	public static Authenticator getAuthenticator(String rawCredentials){
		StringTokenizer authTokens = new StringTokenizer(rawCredentials);
		if (authTokens.hasMoreTokens()){
			String authType = authTokens.nextToken();
			if (authType.equalsIgnoreCase(basicAuth)){
				return new BasicAuthenticator(authTokens.nextToken());
			}
			else if(authType.equalsIgnoreCase(tokenAuth)){
				return new TokenAuthenticator(authTokens.nextToken());
			}
		}
		
		return null;
	}
}
