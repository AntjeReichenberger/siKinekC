package org.webdev.kpoint.api.mobile.auth;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

import org.apache.commons.codec.binary.Base64;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.User;

public class BasicAuthenticator implements Authenticator {

	private String codedCredentials;
	private String username = "";
	private String password = "";

	public BasicAuthenticator(String credentials) {
		codedCredentials = credentials;
	}

	public User authenticate() throws Exception {
		decodeCredentials();

		UserDao userDAO = new UserDao();
		User user = userDAO.read(username, password);
		return user;
	}

	private void decodeCredentials() throws Exception {
		String decodedCredentialString = null;
		decodedCredentialString = new String(
				Base64.decodeBase64(codedCredentials));
		StringTokenizer authTokens = new StringTokenizer(
				decodedCredentialString, ":");

		if (authTokens.hasMoreTokens()) {
			username = authTokens.nextToken();
			password = authTokens.nextToken();
		}
	}

	public String getAuthenticationCredentials()
			throws NoSuchAlgorithmException {
		return "Username=" + username + ":Password=" + getMD5Password();
	}

	private String getMD5Password() throws NoSuchAlgorithmException {
		String md5Password = "";
		MessageDigest mdEnc;
		try {
			mdEnc = MessageDigest.getInstance("MD5");
			mdEnc.update(password.getBytes(), 0, password.length());
			md5Password = new BigInteger(1, mdEnc.digest()).toString(16); // Encrypted
																			// string
		} catch (NoSuchAlgorithmException e) {
			throw e;
		}

		return md5Password;
	}

}
