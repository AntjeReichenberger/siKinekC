package org.webdev.kpoint.monitor;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.*;
import javax.mail.internet.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.EmailManager;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.persistence.TrackingDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.persistence.UserTrackingDao;
import org.webdev.kpoint.bl.pojo.Tracking;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.pojo.UserTracking;

import org.webdev.kpoint.bl.tracking.CanadaPostConnector;
import org.webdev.kpoint.bl.tracking.FedExConnector;
import org.webdev.kpoint.bl.tracking.TrackEmailElement;
import org.webdev.kpoint.bl.tracking.TrackedPackage;
import org.webdev.kpoint.bl.tracking.TrackingManager;
import org.webdev.kpoint.bl.tracking.UPSConnector;
import org.webdev.kpoint.bl.tracking.USPSConnector;
import org.webdev.kpoint.bl.util.ApplicationProperty;

/**
 * Servlet implementation class TrackPackageMonitor.  This is used to monitor a Kinek Gmail account for tracking emails
 * that are received via email.  When new tracking requests are received at the Gmail account, this servlet will process
 * them and add them the users account automatically.
 */
public class TrackPackageMonitor extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final KinekLogger logger = new KinekLogger(TrackPackageMonitor.class);
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TrackPackageMonitor() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("Beginning track package managing processing.");
		Folder folder = null;
		Folder processedFolder = null;
		Folder processingFailedFolder = null;
		Store store = null;
		try {
			Properties props = System.getProperties();
			props.setProperty("mail.store.protocol", "imaps");

			Session session = Session.getDefaultInstance(props, null);
			// session.setDebug(true);
			store = session.getStore("imaps");
			store.connect("imap.gmail.com", ExternalSettingsManager.getTrackingMailAddress(), ExternalSettingsManager.getTrackingMailPassword());
			folder = store.getFolder("Inbox");
			processedFolder = store.getFolder(ExternalSettingsManager.getTrackingMailProcessedFolder());
			processingFailedFolder = store.getFolder(ExternalSettingsManager.getTrackingMailProcessingFailedFolder());
			/* Others GMail folders :
			 * [Gmail]/All Mail   This folder contains all of your Gmail messages.
			 * [Gmail]/Drafts     Your drafts.
			 * [Gmail]/Sent Mail  Messages you sent to other people.
			 * [Gmail]/Spam       Messages marked as spam.
			 * [Gmail]/Starred    Starred messages.
			 * [Gmail]/Trash      Messages deleted from Gmail.
			 */
			folder.open(Folder.READ_WRITE);
			processedFolder.open(Folder.READ_WRITE);
			processingFailedFolder.open(Folder.READ_WRITE);
			Message messages[] = folder.getMessages();
			logger.info("No of Messages : " + folder.getMessageCount());
			logger.info("No of Unread Messages : " + folder.getUnreadMessageCount());

			for (int i=0; i < messages.length; ++i) {
				Message msg = messages[i];
				//Only want new emails
				if (!msg.isSet(Flags.Flag.SEEN)) {
					msg.setFlag(Flags.Flag.SEEN,true);
					Message[] copyMessage = new Message[1];
					copyMessage[0] = msg;
					String result = null;
					try{
						result = saveParts(msg.getContent());
					}
					catch(Exception ex){
						new EmailManager().sendTrackingUnexpected(getMessageFromUser(msg));
						folder.copyMessages(copyMessage, processingFailedFolder);
						msg.setFlag(Flags.Flag.DELETED, true);
					}
					if(result != null)
					{
						processEmail(result,msg);
						folder.copyMessages(copyMessage, processedFolder);
						msg.setFlag(Flags.Flag.DELETED, true);
					}
				}
			}
		}
		catch(Exception ex){
			logger.error(new ApplicationException("An error occured while parsing emails.", ex));
			throw new ServletException(ex);
		}
		finally {
			try
			{
				if (folder != null) { folder.close(true); }
				if (processedFolder != null) { processedFolder.close(true); }
				if (processingFailedFolder != null) { processingFailedFolder.close(true); }
				if (store != null) { store.close(); }
			}
			catch(Exception ex)
			{
				throw new ServletException(ex);
			}

		}
	}

	/**
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

	public static String saveParts(Object content) throws IOException, MessagingException
	{
		OutputStream out = null;
		InputStream in = null;
		String contentString = "";
		try {
			if (content instanceof Multipart) {
				Multipart multi = ((Multipart)content);
				int parts = multi.getCount();
				for (int j=0; j < parts; ++j) {
					MimeBodyPart part = (MimeBodyPart)multi.getBodyPart(j);
					if (part.getContent() instanceof Multipart) {
						// part-within-a-part, do some recursion...
						contentString += saveParts(part.getContent());
					}
					else {    
						in = part.getInputStream();
						contentString += convertStreamToString(in);
					}
				}
			}
			else{
				contentString = content.toString();		
			}
		}
		finally {
			if (in != null) { in.close(); }
			if (out != null) { out.flush(); out.close(); }
		}
		return contentString;
	}
	
	public static String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {        
			return "";
		}
	}
	
	/**
	 * Tracking numbers in emails must adher to the following rules:
	 * Begin with one of:
	 * 	 - whitespace character (newline, tab, or space)
	 *   - > followed by zero or more whitespace chars (to support numbers that are within HTML formatting tags)
	 * Consist of:
	 * 	 - characters that match one of the third party tracking number REGEX
	 * End with one of:
	 *   -  
	 * @param content
	 * @return
	 */
	public static List<String> findValidTrackingNumbers(String content){
		List<String> results = new ArrayList<String>();

		List<String> matches = matchMultipleData("(\\s+|>\\s*)" + CanadaPostConnector.TRACKING_NUMBER_REGEX + "(\\s|\\.|\\s*<)",content);
		results.addAll(matches);
		
		matches = matchMultipleData("(\\s+|>\\s*)" + USPSConnector.TRACKING_NUMBER_REGEX + "(\\s|\\.|\\s*<)",content);
		results.addAll(matches);
		
		matches = matchMultipleData("(\\s+|>\\s*)" + UPSConnector.TRACKING_NUMBER_REGEX + "(\\s|\\.|\\s*<)",content);
		results.addAll(matches);
		
		matches = matchMultipleData("(\\s+|>\\s*)" + FedExConnector.TRACKING_NUMBER_REGEX + "(\\s|\\.|\\s*<)",content);
		results.addAll(matches);
		
		return results;
	}

	public static String matchData(String expr, String source){
		Pattern patt = Pattern.compile(expr, Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.MULTILINE);		
		Matcher m = patt.matcher(source);
		if(m.find()){
			return m.group(2);
		}
		else{
			return null;
		}
	}

	public static List<String> matchMultipleData(String expr, String source){
		Pattern patt = Pattern.compile(expr, Pattern.DOTALL | Pattern.UNIX_LINES | Pattern.MULTILINE);		
		Matcher m = patt.matcher(source);
		List<String> trackingNumbers = new ArrayList<String>();
		String cleanTrackingNumber = "";
		while(m.find()){
			boolean found = false;
			String foundTrackingNumber = m.group();
			cleanTrackingNumber = foundTrackingNumber.replaceAll("\\s", "");
			cleanTrackingNumber = cleanTrackingNumber.replaceAll("\\.", "");
			cleanTrackingNumber = cleanTrackingNumber.replaceAll(">", "");
			cleanTrackingNumber = cleanTrackingNumber.replaceAll("<", "");
			cleanTrackingNumber = cleanTrackingNumber.trim();
			for(String trackingNumber : trackingNumbers){
				if(trackingNumber.equals(cleanTrackingNumber)){
					found = true;
					break;
				}
			}
			if(!found){
				trackingNumbers.add(cleanTrackingNumber);
				found = false;
			}
		}
		return trackingNumbers;
	}

	private static User getMessageFromUser(Message msg){
		UserDao userDao = new UserDao();
		User fromUser = null;
		try {
			InternetAddress[] fromAddr = (InternetAddress[]) msg.getFrom();
			fromUser = userDao.readByEmail(fromAddr[0].getAddress());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return fromUser;
	}
	
	public static void processEmail(String content, Message msg) throws Exception{
		UserDao userDao = new UserDao();
		TrackingDao trackingDao = new TrackingDao();
		UserTrackingDao userTrackingDao = new UserTrackingDao();
		UserTracking newUserTracking = new UserTracking();
		TrackedPackage activeTrackedPackage = null;
		Tracking tracking = new Tracking();
		EmailManager emailManager = new EmailManager();	
		User fromUser = null;
		InternetAddress[] fromAddr = null;
		String result = null;	
		String emailNote = "";	

		try {
			fromAddr = (InternetAddress[]) msg.getFrom();
			fromUser = userDao.readByEmail(fromAddr[0].getAddress());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		if(fromUser == null){
			//No user match, send email telling user to signup
			emailManager.sendTrackingSignUp(fromAddr[0].getAddress(), tracking.getTrackingNumber(), "");				
		}
		else{
			boolean partial = false;
			List<String> foundResults = findValidTrackingNumbers(content);
			
			if(foundResults.size() == 0){
				//No valid tracking numbers found in the tracking email, send email to user telling them this
				emailManager.sendTrackingInvalid(fromUser, emailNote);
				return;
			}
			
			List<TrackEmailElement> emailElements = new ArrayList<TrackEmailElement>();
			for(String foundTrackingNumber : foundResults){
				activeTrackedPackage = null;
				TrackEmailElement emailElement = new TrackEmailElement(foundTrackingNumber);
				Integer courier = TrackingManager.getCourier(foundTrackingNumber);
				Tracking checkTrack = trackingDao.read(foundTrackingNumber,courier);
				
				if(checkTrack != null){
					//this means the tracking number is in the system, but could be getting tracked by a different user
					UserTracking userTracking = userTrackingDao.read(checkTrack, fromUser);
					if(userTracking != null){
						partial = true;
						emailElement.setEmailStatus(ApplicationProperty.getInstance().getProperty("email.tracking.duplicate"));
					}
					else{
						newUserTracking.setUser(fromUser);
						newUserTracking.setPackageNickname(checkTrack.getCourier().getName() + "Package");
						newUserTracking.setTracking(checkTrack);
						
						result = userTrackingDao.createByEmail(newUserTracking);
						if(result != null){
							emailElement.setEmailStatus(ApplicationProperty.getInstance().getProperty("email.tracking.success"));
						}
					}
				}
				else{
					//this is a new package that has not been tracked by anyone yet
					try{
						activeTrackedPackage = TrackingManager.getTrackingDetails(foundTrackingNumber,0);
					}
					catch(Exception e){
						activeTrackedPackage = new TrackedPackage();
					}
					
					if(!activeTrackedPackage.getIsAvailableInCourierSystem()){
						emailElement.setEmailStatus(ApplicationProperty.getInstance().getProperty("email.tracking.added"));
					}
					tracking.setTrackingNumber(foundTrackingNumber);
					tracking.setCourier(activeTrackedPackage.getCourier()); 
					tracking.setCurrentLocation(activeTrackedPackage.getCurrentLocation());
					if(activeTrackedPackage.getIsAvailableInCourierSystem() && activeTrackedPackage.getHasMap()){
						tracking.setCurrentStatus(activeTrackedPackage.getPackageHistory().get(0).getActivity());
					}
					tracking.setEstimatedArrival(activeTrackedPackage.getArrivalDate());		
					if(activeTrackedPackage.getWeight() != null && activeTrackedPackage.getWeightType() != null){
						tracking.setWeight(Float.parseFloat(activeTrackedPackage.getWeight()));
						tracking.setWeightType(activeTrackedPackage.getWeightType());
					}

					newUserTracking.setUser(fromUser);
					newUserTracking.setPackageNickname(activeTrackedPackage.getCourier().getName() + "Package");
					newUserTracking.setTracking(tracking);
					
					result = userTrackingDao.createByEmail(newUserTracking);
					if(result != null){
						if(emailElement.getEmailStatus() == null)
							emailElement.setEmailStatus(ApplicationProperty.getInstance().getProperty("email.tracking.success"));
					}
				}
				emailElements.add(emailElement);
			}

			if(emailElements.size() > 0){
				if(partial){
					emailManager.sendTrackingPartialProcessed(fromUser, emailElements, emailNote);
				}
				else{
					emailManager.sendTrackingProcessed(fromUser, emailElements, emailNote);	
				}
			}
			else
			{
				emailManager.sendTrackingInvalid(fromUser, emailNote);
			}
		}
	}
}	
