package org.webdev.kpoint.api.partner.auth;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

import org.webdev.kpoint.bl.persistence.KinekPartnerDao;
import org.webdev.kpoint.bl.pojo.KinekPartner;


import sun.misc.BASE64Decoder;

public class BasicAuthenticator {

	private String codedCredentials = null;
	private String token = "";
	private String password = "";
	
	public BasicAuthenticator(String credentials) {
		codedCredentials = credentials;
	}
	
	public KinekPartner authenticate() throws Exception{
		decodeCredentials();
		KinekPartnerDao kinekPartnerDao = new KinekPartnerDao();
		KinekPartner kinekPartner = kinekPartnerDao.read(token, getMD5Password());
		return kinekPartner;
	}
	
	private void decodeCredentials() throws Exception{
		String decodedCredentialString = null;
		BASE64Decoder decoder = new BASE64Decoder();
		decodedCredentialString = new String(decoder.decodeBuffer(codedCredentials));
		StringTokenizer authTokens = new StringTokenizer(decodedCredentialString,":");
	
		if (authTokens.hasMoreTokens()){
			token = authTokens.nextToken();
			password = authTokens.nextToken();
		}
	}
	
	public String getAuthenticationCredentials() throws NoSuchAlgorithmException{
		return "Token="+token+":Password="+getMD5Password();
	}
	
	private String getMD5Password() throws NoSuchAlgorithmException{
		String md5Password = "";
	    MessageDigest mdEnc;
		try {
			mdEnc = MessageDigest.getInstance("MD5");
			mdEnc.update(password.getBytes(), 0, password.length());
			md5Password = new BigInteger(1, mdEnc.digest()).toString(16); // Encrypted string
		} catch (NoSuchAlgorithmException e) {
			throw e;
		}
		
		return md5Password;
	}
	
}
