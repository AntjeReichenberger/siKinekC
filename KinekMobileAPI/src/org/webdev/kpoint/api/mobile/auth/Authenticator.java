package org.webdev.kpoint.api.mobile.auth;

import java.security.NoSuchAlgorithmException;

import org.webdev.kpoint.bl.pojo.User;

public interface Authenticator {

	public User authenticate() throws Exception;

	public String getAuthenticationCredentials() throws NoSuchAlgorithmException;
}
