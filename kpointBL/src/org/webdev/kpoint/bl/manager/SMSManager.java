package org.webdev.kpoint.bl.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Hashtable;

import org.webdev.kpoint.bl.tracking.TrackedPackage;
import org.webdev.kpoint.bl.util.Emailer;
import org.webdev.kpoint.bl.util.Emailer.EmailType;
import org.webdev.kpoint.bl.util.MessageLogger;
import org.webdev.kpoint.bl.util.URIEncoder;
import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.persistence.CountryAreaCodeDao;
import org.webdev.kpoint.bl.pojo.Email;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.util.FileReader;

public class SMSManager {
	
	private static final KinekLogger logger = new KinekLogger(SMSManager.class);
	
	/**
	 * Sends an SMS message using the Clickatell API.  Separate API's are used for sending to Canadian and US
	 * phone numbers. This method determines the target country and then routes the text through
	 * the proper API.  The Canadian number API is SMTP-based, while the US number API is HTTP.
	 * @param user The target user for the text message.  This object contains the users cell phone number
	 * @param message The textual contents of the message
	 */
	public static void sendSMS(User user, String message) throws Exception
	{
		sendTextMessage(user, message);	
	}
	
	/**
	 * Sends an SMS message using the Clickatell API.  Separate API's are used for sending to Canadian and US
	 * phone numbers. This method determines the target country and then routes the text through
	 * the proper API.  The Canadian number API is SMTP-based, while the US number API is HTTP.
	 * @param number The target number for the text message
	 * @param message The textual contents of the message
	 */
	public static void sendSMS(String number, String message) throws Exception
	{
		sendTextMessage(number, message);
	}
	
	public void sendSMS(int triggerId, User user, String message) throws Exception{
		sendTextMessage(user, message);
		logMessage(triggerId, user.getCellPhone(), message);
	}
	
	private static void sendTextMessage(String number, String message) throws Exception
	{
		String countryCode = getCountryCodeFromPhoneNumber(number);
		if(countryCode != null && countryCode.equals("CA"))
		{
			sendTextToCanadianNumber(formatPhoneNumber(number), message);
		}
		else
		{
			//assume US phone number
			sendTextToUSNumber(formatPhoneNumber(number), message);
		}
	}
	
	private static void sendTextMessage(User user, String message) throws Exception
	{
		String number = user.getCellPhone();
		String countryCode = getCountryCodeFromPhoneNumber(number);
		if(countryCode != null && countryCode.equals("CA"))
		{
			sendTextToCanadianNumber(formatPhoneNumber(number), message);
		}
		else
		{
			//assume US phone number
			sendTextToUSNumber(user, message);
		}
	}
	
	public static void sendTextToUSNumber(User user, String message)
	{
		String number = "";
		try{
			number = formatPhoneNumber(user.getCellPhone());
			String finalUrl = ExternalSettingsManager.getHttpSMSURL();
			finalUrl += "&to="+number;
			finalUrl += "&text="+URIEncoder.encode(message);
			
			//send request using GET
			URL url = new URL(finalUrl);
			URLConnection urlc = url.openConnection();
			
			//get result
			BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
			String l = null;
			while ((l=br.readLine())!=null) {
				if(!l.contains("ID"))
					throw new Exception("Message could not be sent. Details: " + l);
			}
			br.close();
		}
		catch(Exception ex){
			//open id not successful, show login page with error
			Hashtable<String,String> logData = new Hashtable<String,String>();
			logData.put("TargetNumber", number);
			logData.put("TargetUserID", String.valueOf(user.getUserId()));
			logData.put("Message", message);
			logger.warn(new ApplicationException("Message could not be sent.", ex), logData);
		}
	}
	
	public static void sendTextToUSNumber(String number, String message)
	{
		try{
			String finalUrl = ExternalSettingsManager.getHttpSMSURL();
			finalUrl += "&to="+number;
			finalUrl += "&text="+URIEncoder.encode(message);
			
			//send request using GET
			URL url = new URL(finalUrl);
			URLConnection urlc = url.openConnection();
			
			//get result
			BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
			String l = null;
			while ((l=br.readLine())!=null) {
				if(!l.contains("ID"))
					throw new Exception("Message could not be sent. Details: " + l);
			}
			br.close();
		}
		catch(Exception ex){
			//open id not successful, show login page with error
			Hashtable<String,String> logData = new Hashtable<String,String>();
			logData.put("TargetNumber", number);
			logData.put("Message", message);
			logger.warn(new ApplicationException("Message could not be sent.", ex), logData);
		}
	}
		
	public static void sendTextToCanadianNumber(String number, String message)
	{
		String apiUser = ExternalSettingsManager.getSMSUsername();
		String password = ExternalSettingsManager.getSMSPassword();
		String apiId = ExternalSettingsManager.getSMSApiId();
		String toEmail = ExternalSettingsManager.getSMSToEmail();
		String replyEmail = ExternalSettingsManager.getSMSReplyEmail();
		
		String body = 	"user:" + apiUser + "\n" +
						"password:" + password + "\n" +
						"api_id:" +  apiId + "\n" +
						"to:" + formatPhoneNumber(number) + "\n" +
						"reply:" + replyEmail + "\n" +
						"text:" + message;
		Email email = new Email();
		email.setBody(body);
		email.setTo(toEmail);
		
		Emailer emailer = new Emailer();
		emailer.sendHtmlEmail(email, EmailType.PlainText);
	}
	
	public static String getCountryCodeFromPhoneNumber(String phoneNumber) throws Exception {
		//get the area code
		String countryCode = null;
		String formattedNumber = formatPhoneNumber(phoneNumber);
		if(formattedNumber.length() == 11)
		{
			String areaCode = formattedNumber.substring(1, 4);
			countryCode = new CountryAreaCodeDao().read(areaCode);
		}
	
		return countryCode;
	}
	
	/**
	 * Removes all non digits from the provided phone number. Also adds a preceeding "1" if required.
	 * @param number The number to format
	 * @return A number formatted to work with the current SMS client
	 */
	private static String formatPhoneNumber(String number)
	{
		String formattedNumber = number.replaceAll("\\D", "");
		if(formattedNumber == null)
		{
			Hashtable<String, String> props = new Hashtable<String, String>();
			props.put("originalNumber", number);
			logger.error("Formatted number is null", props);
			
			return "";
		}
		else if (formattedNumber.length() == 11)
		{
			return formattedNumber;
		}
		else if (formattedNumber.length() == 10)
		{
			return "1" + formattedNumber;
		}
		else
		{
			Hashtable<String, String> props = new Hashtable<String, String>();
			props.put("originalNumber", number);
			props.put("formattedNumber", formattedNumber);
			logger.error("Formatted number is incorrect length", props);
		}
		
		return formattedNumber;
	}
	
	public void sendConsumerCreditExpirySMS(User user) throws Exception
	{
		String smsContent = getConsumerCreditExpirySMS();
		SMSManager.sendSMS(user, smsContent);
		
		logMessage(ExternalSettingsManager.getMessageTrigger_creditexpirationwarning(), user.getCellPhone(), smsContent);
	}
	
	private String getConsumerCreditExpirySMS()
	{
		return FileReader.readFile("consumer_credit_expiry.sms");
	}
	
	public void sendConsumerTrackingUpdate(User user, TrackedPackage trackPackage) throws Exception
	{
		String smsContent = getConsumerTrackingUpdateSMS(user.getUsername(),trackPackage);
		SMSManager.sendSMS(user, smsContent);		
	}
	
	private String getConsumerTrackingUpdateSMS(String kpUser, TrackedPackage trackPackage)
	{
		String message = FileReader.readFile("consumer_tracking_update.sms");
		message = message.replace("attb_user", kpUser);
		message = message.replace("attb_trackingName", trackPackage.getNickname()); 
		message = message.replace("attb_trackingStatus", trackPackage.getStatus());
		
		if(trackPackage.getCurrentLocation() != null && !trackPackage.getCurrentLocation().equals("")){
			message = message.replace("attb_location", trackPackage.getCurrentLocation());
		}
		else{
			message = message.replace("attb_location", "");
		}
		
		if(message.length() > ExternalSettingsManager.getSMSLimit()){
			message = message.substring(0, ExternalSettingsManager.getSMSLimit()-1);
		}
		
		return message;
	}
	
	public void sendParcelReceiptSMS(User user, String kpName, 
											String kpAddress, String kpPhone, int numberOfPackages) throws Exception
	{
		String message = getParcelReceiptSMS(user.getKinekNumber(), kpName, kpAddress, kpPhone, numberOfPackages);
		SMSManager.sendSMS(user, message);
		
		logMessage(ExternalSettingsManager.getMessageTrigger_acceptdelivery(), user.getCellPhone(), message);
	}
	
	private String getParcelReceiptSMS(String kinekNumber, String kpName, String kpAddress, String kpPhone, int numberOfPackages)
	{
		String message = FileReader.readFile("parcel_receipt.sms");
		message = message.replace("attb_user_kinekNum", "#"+kinekNumber);
		message = message.replace("attb_kp_name", kpName); 
		message = message.replace("attb_kp_address", kpAddress);
		message = message.replace("attb_kp_phone", kpPhone);
		message = message.replace("attb_numberofpackages", String.valueOf(numberOfPackages));
				
		return message;
	}
	
	public void sendPackageReminderSMS(User user, String kpName, String kpAddress, String kpPhone, int numberOfPackages) throws Exception
	{
		String message = getPackageReminderSMS(user.getKinekNumber(), kpName, kpAddress, kpPhone, numberOfPackages);
		SMSManager.sendSMS(user, message);
		
		logMessage(ExternalSettingsManager.getMessageTrigger_acceptdelivery(), user.getCellPhone(), message);
	}
	
	private String getPackageReminderSMS(String kinekNumber, String kpName, String kpAddress, String kpPhone, int numberOfPackages)
	{
		String message = FileReader.readFile("package_reminder.sms");
		message = message.replace("attb_user_kinekNum", kinekNumber);
		message = message.replace("attb_kp_name", kpName); 
		message = message.replace("attb_kp_address", kpAddress);
		message = message.replace("attb_kp_phone", kpPhone);
		message = message.replace("attb_numberofpackages", String.valueOf(numberOfPackages));
				
		return message;
	}
	
	/**
	 * Writes a log entry for the provided sms message
	 * @param triggerId
	 * @param toNumber
	 * @param body
	 */
	private void logMessage(int triggerId, String toNumber, String body) throws Exception
	{
		new MessageLogger().logSMSMessage(
				triggerId,
				toNumber, 
				body);
	}
}