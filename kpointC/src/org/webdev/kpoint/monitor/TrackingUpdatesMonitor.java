package org.webdev.kpoint.monitor;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.EmailManager;
import org.webdev.kpoint.bl.manager.NotificationManager;
import org.webdev.kpoint.bl.manager.SMSManager;
import org.webdev.kpoint.bl.persistence.TrackingDao;
import org.webdev.kpoint.bl.persistence.UserTrackingDao;
import org.webdev.kpoint.bl.pojo.Tracking;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.pojo.UserTracking;
import org.webdev.kpoint.bl.tracking.TrackedPackage;
import org.webdev.kpoint.bl.tracking.TrackingManager;

/**
 * Servlet implementation class TrackingUpdatesMonitor.  This is used to monitor for updates to tracking numbers that
 * are registered with the Kinek system.  When updates are available, the Kinek system will process them and notify the
 * user.
 */
public class TrackingUpdatesMonitor extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final KinekLogger logger = new KinekLogger(TrackingUpdatesMonitor.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TrackingUpdatesMonitor() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Tracking> trackings = null;
		TrackingDao trackingDao = new TrackingDao();
		EmailManager emailManager = new EmailManager();
		SMSManager smsManager = new SMSManager();
		
		try{
			trackings = trackingDao.fetchTrackingRecordsForAutoUpdate();
		}
		catch (Exception e) {
			logger.error(new ApplicationException("Unable to fetch tracking records for auto updates.", e));
		}

		for(Tracking trackPackage : trackings){
			try{
				UserTrackingDao userTrackingDao = new UserTrackingDao();
				List<UserTracking> userTrackings = userTrackingDao.fetch(trackPackage);

				if(userTrackings != null && userTrackings.size() > 0){
					TrackedPackage newUpdate = TrackingManager.getTrackingDetails(trackPackage.getTrackingNumber(),trackPackage.getCourier().getCourierId());

					if(newUpdate.getHasChanged()){
						for(UserTracking userTracking:userTrackings){
							User currentUser = userTracking.getUser();
							currentUser.setNotificationSummary();
							
							newUpdate.setNickname(userTracking.getPackageNickname());
							if(currentUser.getNotificationSummary().isTrackingEmailSupported()){
								emailManager.sendTrackingUpdate(currentUser, trackPackage.getTrackingNumber(), 
										userTracking.getPackageNickname(), null, newUpdate.getPackageHistory().get(0));
							}
							if(currentUser.getNotificationSummary().isTrackingTextSupported()){
								smsManager.sendConsumerTrackingUpdate(currentUser,newUpdate);
							}
							if(currentUser.getNotificationSummary().isTrackingPushSupported()){
								NotificationManager.sendTrackingPushNotification(currentUser,newUpdate);
							}
						}
					}
				}
			} 
			catch (ApplicationException ae) {
				//No need to log these exceptions again here, they have already been logged by the backend
			}
			catch (Exception e) {
				Hashtable<String,String> logData = new Hashtable<String,String>();
				logData.put("TrackingID", trackPackage.getId().toString());
				logger.error(new ApplicationException("Unable to update tracking number.",e),logData);
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
}
