package org.webdev.kpoint.monitor;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.EmailManager;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.manager.SMSManager;
import org.webdev.kpoint.bl.persistence.PackageReceiptDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.User;

/**
 * Servlet implementation class EmailMonitorUtil.  This is used to send reminder emails for specific events that
 * occur in the system, such as delivery reminders and registration reminders.
 */
public class ActivityReminderMonitor extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final KinekLogger logger = new KinekLogger(ActivityReminderMonitor.class);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActivityReminderMonitor() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("Beginning email monitor processing.");
			
		String remoteIPAddress = request.getRemoteAddr();
		logger.info("Remote IP Address: " + remoteIPAddress);
		
		if (!remoteIPAddress.equals(ExternalSettingsManager.getEmailMonitorUtilIPAddress())) {
			logger.error("Remote IP check failed.");
			return;
		}
		logger.info("Remote IP check passed.");
		
		
		//TODO
		try{
			processRegistrationReminderEmail();
		}
		catch(Exception ex){
			logger.error(new ApplicationException("An error occurred while processing the Registration Reminders.", ex));
		}
		/*
		try {
			processDeliveryReminderEmail();
		} catch (ParseException e) {
			logger.error(new ApplicationException("An error occurred while processing the Delivery Reminders.", e));
		}*/		
		logger.info("Finished processing all reminder emails.");
	}
    
    private void processRegistrationReminderEmail() throws Exception {
    	logger.info("Beginning registration reminder email processing.");
    	UserDao userDao = new UserDao();
		List<User> userList = new ArrayList<User>();
		
		Calendar cutOffDate = new GregorianCalendar();
		cutOffDate.add(Calendar.DATE, (ExternalSettingsManager.getRegistrationReminderEmailWaitTime() * -1));
		logger.info("Cut off time for registration reminders: " + cutOffDate.getTime().toString());
		
		userList = userDao.fetchConsumerForRegistrationReminder(cutOffDate);
		
		//check all users and send reminder email
		for(int i = 0; i < userList.size(); i++) {
			User currentUser = userList.get(i);
			EmailManager emailManager = new EmailManager();
			
			//if address line 1 is empty, then send an email reminding the user to 
			//fill out their profile and choose a default kp
			if(currentUser.getAddress1() == null || currentUser.getAddress1().trim().equals("")) 
			{
				emailManager.sendNoProfileRegReminderEmail(currentUser);
				updateRegReminderEmailDateTime(currentUser);
			}
			else
			{
				//this means the user filled out their profile, but did not choose a default kp
				//send an email reminding the user to choose a default kp
				emailManager.sendNoKPRegReminderEmail(currentUser);
				updateRegReminderEmailDateTime(currentUser);
			}
		}
		logger.info("Number of registration reminder emails sent: " + userList.size());
		logger.info("Finished processing delivery reminder emails.");
	}
    
    //send delivery reminder email every 5 days.
    private void processDeliveryReminderEmail() throws Exception {
    	logger.info("Delivery reminder email processing is temporarily disabled.");
    	
    	logger.info("Beginning delivery reminder email processing.");
    	boolean isSpecialReminderDeliveryEmail;
    	PackageReceiptDao packageReceiptDao = new PackageReceiptDao();
    	List<PackageReceipt> packageReceiptList = new ArrayList<PackageReceipt>();
    	List<PackageReceipt> packageReceiptSpecialList = new ArrayList<PackageReceipt>();
    	
		int endDaysOfStdDeleveryReminderEmail = ExternalSettingsManager.getDeliveryReminderEmailEndDays();
		int specialDeliveryReminderEmailDays = ExternalSettingsManager.getDeliveryReminderEmailSpecial();
		
		//fetch delivery packages which are exactly 27 days old only and send Special Delivery email. Suppose a package arrived on April 25th and current
		//date is May 21st. So the package has been staying in the depot for 26th days. But the next standard email will be send on 26th (5 days) so,
		//user will not get special email on May 22nd. For this reason this separate fetch method is necessary to send special email.
		packageReceiptSpecialList = packageReceiptDao.fetchOnlySpecialDaysOldPackageReceipt(specialDeliveryReminderEmailDays);    	    	
    	for (int i = 0; i < packageReceiptSpecialList.size(); i++) {
    		EmailManager manager = new EmailManager();
    		PackageReceipt packageReceipt = packageReceiptSpecialList.get(i);
    		
    		isSpecialReminderDeliveryEmail = true;
			manager.sendReminderEmail(packageReceipt, isSpecialReminderDeliveryEmail);
			updateDeliveryReminderEmailDateTime(packageReceipt);
			
			sendSMSReminder(packageReceipt);
    	}
    	logger.info("Number of final reminder emails sent: " + packageReceiptSpecialList.size());
    	
    	//fetch delivery packages that have last email dates of at least 5 days ago
    	Calendar cutOffDate = getCutOffDate();
    	logger.info("Cut off time for regular delivery reminders: " + cutOffDate.getTime().toString());
    	packageReceiptList = packageReceiptDao.fetch(cutOffDate);
    	int numberOfEmailRemindersSent = 0;
		for (int i = 0; i < packageReceiptList.size(); i++) {
			//send email to each recipient
			EmailManager manager = new EmailManager();
			PackageReceipt packageReceipt = packageReceiptList.get(i);
			Date receivedDate = packageReceipt.getReceivedDate();			
			//check received date from packagereceipt table against the current date which should be within EndDays to get standard delivery email
			if (substractCurrentDaysAndReceivedDays(receivedDate) <= endDaysOfStdDeleveryReminderEmail) {
				isSpecialReminderDeliveryEmail = false;
				manager.sendReminderEmail(packageReceipt, isSpecialReminderDeliveryEmail);
				updateDeliveryReminderEmailDateTime(packageReceipt);
				
				sendSMSReminder(packageReceipt);
				
				numberOfEmailRemindersSent++;
			}
		}
		logger.info("Number of regular reminder emails sent: " + numberOfEmailRemindersSent);
		logger.info("Finished processing delivery reminder emails.");
    }
    
    private void sendSMSReminder(PackageReceipt receipt) throws Exception  {
    	for (User packageRecipient : receipt.getPackageRecipients()) {
			if (packageRecipient.getNotificationSummary().isDeliveryTextSupported()) {
				SMSManager sms = new SMSManager();
				sms.sendPackageReminderSMS(
						packageRecipient,
						receipt.getKinekPoint().getName(),
						receipt.getKinekPoint().getAddress1(),
						receipt.getKinekPoint().getPhone(),
						receipt.getPackages().size());
			}
		}
    }
    
    private void updateRegReminderEmailDateTime(User user) throws Exception {
    	UserDao userDao = new UserDao();
    	user.setRegReminderEmailDate(new GregorianCalendar());
    	userDao.update(user);
    }
    
    private void updateDeliveryReminderEmailDateTime(PackageReceipt packageReceipt)  throws Exception  {
    	PackageReceiptDao packageReceiptDao = new PackageReceiptDao();
    	packageReceipt.setLastEmailDate(new GregorianCalendar().getTime());
    	packageReceiptDao.update(packageReceipt);
    }

    /**current date end 24Hrs format: e.g: 2010:05:15T00:00:00.000Z.*/
    private static Calendar getCutOffDate() {
		Calendar cutOffDateEnd24Hrs = Calendar.getInstance();		
		cutOffDateEnd24Hrs.add(Calendar.DATE, (ExternalSettingsManager.getDeliveryReminderEmailWaitTime() * -1) +1);
		if (cutOffDateEnd24Hrs.get(Calendar.AM_PM) == Calendar.PM) {
			cutOffDateEnd24Hrs.add(Calendar.HOUR, -(cutOffDateEnd24Hrs.get(Calendar.HOUR)+12));
		} else {
			cutOffDateEnd24Hrs.add(Calendar.HOUR, -cutOffDateEnd24Hrs.get(Calendar.HOUR));
		}
		cutOffDateEnd24Hrs.clear(Calendar.MINUTE); 
		cutOffDateEnd24Hrs.clear(Calendar.SECOND);
		
		return cutOffDateEnd24Hrs;
    }
    
    /**trimmed the time and timezone from the Date to get actual date only for error free calculation*/ 
    private Date formatDateOnly(Date receivedDate){
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = df.format(receivedDate);
		Date date = new Date();
		try {
			date=df.parse(dateStr);
		} catch(ParseException e) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("Date", dateStr);
			logger.error(new ApplicationException("Could not parse date.", e), logProps);
		}
		
		return date;
    }
    
    /**
     * Get the Difference between Current date and Received Date to know how many days it is in the KinekPoint
     * @return diffBwtCurrDaysAndRecievedDays as int
     * */
    private int substractCurrentDaysAndReceivedDays(Date receivedDate){
		Calendar receivedCal = Calendar.getInstance();
		receivedCal.setTime(formatDateOnly(receivedDate));
		long receivedDateMiliSec = receivedCal.getTimeInMillis();
					
		Calendar currentDate = Calendar.getInstance();
		long currentDateMiliSec=currentDate.getTimeInMillis();
					
		long diffBwtCurrDaysAndRecievedMiliSec = currentDateMiliSec-receivedDateMiliSec;
		long diffBwtCurrDaysAndRecievedDays = diffBwtCurrDaysAndRecievedMiliSec / (24 * 60 * 60 * 1000);
		
		return (int)diffBwtCurrDaysAndRecievedDays;
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
