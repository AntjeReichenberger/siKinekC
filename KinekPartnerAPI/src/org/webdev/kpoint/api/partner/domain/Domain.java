package org.webdev.kpoint.api.partner.domain;

import java.util.Random;

public class Domain {

	public static String generateRandomString(int length)
	{
	    Random rand = new Random();
	    String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
	    char[] text = new char[length];
	    for (int i = 0; i < length; i++)
	    {
	        text[i] = characters.charAt(rand.nextInt(characters.length()));
	    }
	    return new String(text);
	}
}
