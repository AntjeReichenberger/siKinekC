package org.webdev.kpoint.bl.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import sun.misc.BASE64Encoder;

import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.persistence.DeviceTokenDao;
import org.webdev.kpoint.bl.persistence.NotificationDao;
import org.webdev.kpoint.bl.pojo.DeviceToken;
import org.webdev.kpoint.bl.pojo.Notification;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.tracking.TrackedPackage;
import org.webdev.kpoint.bl.util.FileReader;

public class NotificationManager {

	private static final KinekLogger logger = new KinekLogger(NotificationManager.class);
	
	public static String encode (String source) {
		  BASE64Encoder enc = new sun.misc.BASE64Encoder();
		  return(enc.encode(source.getBytes()));
	}
	
	/**
	 * Sends a delivery push notification to a device via the Urban Airship API.
	 * @throws Exception 
	 */
	public static void sendDeliveryPushNotification(User recipient, PackageReceipt receipt) throws Exception{
		//build notification content
		String message = FileReader.readFile("parcel_receipt.push");
		message = message.replace("attb_user_kinekNum", "#"+recipient.getKinekNumber());
		message = message.replace("attb_kp_name", receipt.getKinekPoint().getName()); 
		message = message.replace("attb_kp_address", receipt.getKinekPoint().getAddress1());
		message = message.replace("attb_kp_phone", receipt.getKinekPoint().getPhone());
		message = message.replace("attb_numberofpackages", String.valueOf(receipt.getPackages().size()));
		
		sendPushNotification(recipient.getUserId(), message);
	}
	
	/**
	 * Sends a tracking push notification to a device via the Urban Airship API.
	 * @throws Exception 
	 */
	public static void sendTrackingPushNotification(User user, TrackedPackage trackPackage) throws Exception{
		//build notification content
		String message = FileReader.readFile("consumer_tracking_update.push");
		message = message.replace("attb_user", user.getUsername());
		message = message.replace("attb_trackingName", trackPackage.getNickname()); 
		message = message.replace("attb_trackingStatus", trackPackage.getStatus());
		
		if(trackPackage.getCurrentLocation() != null && !trackPackage.getCurrentLocation().equals("")){
			message = message.replace("attb_location", trackPackage.getCurrentLocation());
		}
		else{
			message = message.replace("attb_location", "");
		}
		
		if(message.length() > ExternalSettingsManager.getPushLimit()){
			message = message.substring(0, ExternalSettingsManager.getPushLimit()-1);
		}
		sendPushNotification(user.getUserId(),message);
	}
	
	/**
	 * Sends a push notification to a device via the Urban Airship API.
	 * @throws Exception 
	 */
	private static void sendPushNotification(int userId, String message) throws Exception{
		//get users device tokens and build token string
		DeviceTokenDao dao = new DeviceTokenDao();
		List<DeviceToken> tokens = dao.fetch(userId);
		String iosTokenString = "";
		String androidTokenString = "";
		if(tokens != null && tokens.size() > 0){
			ArrayList<DeviceToken> tokenList = (ArrayList<DeviceToken>)tokens;
			for(int i=0; i<tokenList.size()-1; i++){
				DeviceToken token = tokenList.get(i);
				if(token.getTargetDeviceType() != null && token.getTargetDeviceType().equals("IOS"))
					iosTokenString += token.getToken() + "\",\"";
				else if(token.getTargetDeviceType() != null && token.getTargetDeviceType().equals("Android"))
					androidTokenString += token.getToken() + "\",\"";
			}
			DeviceToken finalToken = tokenList.get(tokens.size()-1);
			if(finalToken.getTargetDeviceType() != null && finalToken.getTargetDeviceType().equals("IOS"))
				iosTokenString += finalToken.getToken();
			else if(finalToken.getTargetDeviceType() != null && finalToken.getTargetDeviceType().equals("Android"))
				androidTokenString += finalToken.getToken();
						
			if(iosTokenString.endsWith("\",\""))
				iosTokenString = iosTokenString.substring(0, iosTokenString.length()-3);
			
			if(androidTokenString.endsWith("\",\""))
				androidTokenString = androidTokenString.substring(0, androidTokenString.length()-3);

			if(!iosTokenString.equals(""))
				sendIOSPushNotification(iosTokenString, message);
			
			if(!androidTokenString.equals(""))
				sendAndroidPushNotification(androidTokenString, message);	
		}
	}
		
	private static void sendIOSPushNotification(String tokenString, String message) throws Exception{
		String content = "{\n";
		content += "\"device_tokens\": [\"" + tokenString + "\"],\n";
		content += "\"aps\": {\"alert\": \"" + message + "\", \"sound\": \"default\"}\n";
		content += "}";
		
		URL url = new URL(ExternalSettingsManager.getUrbanAirshipAPIEndpoint());
		URLConnection urlc = url.openConnection();
		urlc.setDoOutput(true); //use post mode
		urlc.setAllowUserInteraction(false);
		urlc.setRequestProperty("Content-Type", "application/json");
		urlc.setRequestProperty("Authorization", "Basic " + encode(ExternalSettingsManager.getUrbanAirshipApplicationKey() + ":" + ExternalSettingsManager.getUrbanAirshipApplicationMasterSecret()));
		
		PrintStream ps = new PrintStream(urlc.getOutputStream());
		ps.print(content);
		ps.close();

		//get result
		BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
		String text = null;
		String response = "";
		while ((text=br.readLine())!=null) {
			response += text;
		}
		br.close();
			
		/*bwr - removed... caused an exception every time.  Valid response is push_id:xxxxxxx.
		if(!response.equalsIgnoreCase("null"))
		{
			throw new Exception("Push could not be sent. Details: " + text);
		}
		*/
	}
	
	private static void sendAndroidPushNotification(String tokenString, String message) throws Exception{
		String content = "{\n";
		content += "\"apids\": [\"" + tokenString + "\"],\n";
		content += "\"android\": {\"alert\": \"" + message + "\", \"sound\": \"default\"}\n";
		content += "}";
		
		URL url = new URL(ExternalSettingsManager.getUrbanAirshipAPIEndpoint());
		URLConnection urlc = url.openConnection();
		urlc.setDoOutput(true); //use post mode
		urlc.setAllowUserInteraction(false);
		urlc.setRequestProperty("Content-Type", "application/json");
		urlc.setRequestProperty("Authorization", "Basic " + encode(ExternalSettingsManager.getUrbanAirshipApplicationKey() + ":" + ExternalSettingsManager.getUrbanAirshipApplicationMasterSecret()));
		
		PrintStream ps = new PrintStream(urlc.getOutputStream());
		ps.print(content);
		ps.close();

		//get result
		BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
		String text = null;
		String response = "";
		while ((text=br.readLine())!=null) {
			response += text;
		}
		br.close();
			
		/*kgg - removed... caused an exception every time.  Valid response is push_id:xxxxxxx.
		if(!response.equalsIgnoreCase("null"))
		{
			throw new Exception("Push could not be sent. Details: " + text);
		}
		*/
	}
	
	/**
	 * Determines if the provided notification is in the user's current list of notifications and adds or removes it
	 * based on the notificationSupported parameter.  
	 * @param currentUserNotifications
	 * @param notificationSupported
	 * @param notificationTypeId
	 * @return an updated list of user notification subscriptions
	 */
	public static List<Notification> mergeNotification(List<Notification> currentUserNotifications, boolean notificationSupported, int notificationTypeId) throws Exception{
		if(notificationSupported){
			//if notification is already in the users list, do not add it again
			boolean found = false;
			for(int i=0; i<currentUserNotifications.size(); i++){
				if(currentUserNotifications.get(i).getId() == notificationTypeId){
					found = true;
					break;
				}			
			}
			if(!found)
			{
				Notification notificationObj = new NotificationDao().read(notificationTypeId);
				currentUserNotifications.add(notificationObj);
			}
		}
		else{
			//remove the selected notification from the users list of notifications		
			for(int i=0; i<currentUserNotifications.size(); i++){
				if(((Notification)currentUserNotifications.get(i)).getId() == notificationTypeId){
					currentUserNotifications.remove(i);
				}			
			}
		}
	
		return currentUserNotifications;
	}
}
