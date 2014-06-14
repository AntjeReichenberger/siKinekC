package org.webdev.kpoint.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.webdev.kpoint.bl.manager.ExternalSettingsManager;

/**
 * This class will read the marketing content from LifeRay portal and 
 * grab the only content between <!-- CONTENTWRAPPER --> and <!-- /CONTENTWRAPPER --> 
 * */

public class MarketingContentReader {

	public static StringBuffer contentBuffer;
	
	/**
	 *  
	 * <P>It will read html content of the marketing page from LifeRay portal of kinek</P>
	 * */
	public void readContentFromLifeRay() throws IOException{
			contentBuffer=new StringBuffer();
	        URL marketingContentUrl = new URL(ExternalSettingsManager.getCMSMarketingPageUrl());
	        URLConnection urlConn = marketingContentUrl.openConnection();
	        BufferedReader in = new BufferedReader( new InputStreamReader(urlConn.getInputStream()));
	        
	        String inputLine=""; 
	        while ((inputLine = in.readLine()) != null){ 
	        	contentBuffer.append(inputLine+System.getProperty("line.separator"));
	        }
	        in.close();
	}
	
	/**
	 * @return HTML tags with contents will be returned as String data type.
	 */
	public String grabMarker(){
		
		int startCommentPos=-1;
		int endCommentPos=-1;
		String conentStr="";
		String startMarker="<!-- MARKETING.MARKER CONTENTWRAPPER -->";
		String endMarker="<!-- /MARKETING.MARKER CONTENTWRAPPER -->";
		
		if(contentBuffer!=null && !contentBuffer.toString().trim().equals("")){
				
			conentStr=contentBuffer.toString();
			startCommentPos=conentStr.indexOf(startMarker);
			endCommentPos=conentStr.indexOf(endMarker)+endMarker.length();
			
			if(startCommentPos !=-1 && endCommentPos!=-1){
				conentStr=conentStr.substring(startCommentPos, endCommentPos);				
			}	
			return conentStr;	
		}
		return contentBuffer.toString(); //return all html tag if startMarker and endMarker are invalid tag
	}
}
