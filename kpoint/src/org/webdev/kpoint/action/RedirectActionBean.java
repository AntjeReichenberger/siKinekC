package org.webdev.kpoint.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationError;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.bl.manager.EmailManager;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.manager.NotificationManager;
import org.webdev.kpoint.bl.manager.SMSManager;
import org.webdev.kpoint.bl.persistence.CourierDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.PackageReceiptDao;
import org.webdev.kpoint.bl.persistence.RedirectReasonDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.Courier;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Package;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.RedirectReason;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.util.FileReader;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/Redirect.action")
public class RedirectActionBean extends SecureActionBean {
	
	private static final KinekLogger logger = new KinekLogger(RedirectActionBean.class);

	@Validate(required=true)
	private int courierId;
	
	@Validate(required=true)
	private String redirectLocation;
	
	@Validate(required=true)
	private int reasonId;
	
	@Validate(required=true, on="!kinekNumberSearch")
	private String kinekNumber;
	
	private String customInfo;
	
	private Package kpPackage;
	private List<KinekPoint> userDepots;
	
	private Integer depotId;
	
	private PackageReceipt packageReceipt;
	
	@DontValidate
	@DefaultHandler
	public Resolution view() throws Exception {
		setUserDepots(new UserDao().fetchUserKinekPoints(getActiveUser().getUserId()));
		return UrlManager.getRedirectForm();
	}

	/**
	 * Handles searching for a user if supplied a Kinek#.
	 * This is step 1 in the process
	 * @return
	 */
	public Resolution search() throws Exception {
		setUserDepots(new UserDao().fetchUserKinekPoints(getActiveUser().getUserId()));
		packageReceipt = populatePackageReceipt(getKinekNumber(), getCourierId(), getReasonId(), getCustomInfo(),getDepotId());
		
		if (getContext().getValidationErrors().size() > 0)
			return UrlManager.getRedirectForm();
		
		return UrlManager.getRedirectVarification();
		
	}

	public Resolution redirect() throws Exception {
		packageReceipt = populatePackageReceipt(getKinekNumber(), getCourierId(), getReasonId(), getCustomInfo(),getDepotId());
		
		packageReceipt.setRedirectLocation(redirectLocation);
		Calendar now = Calendar.getInstance();
		packageReceipt.setReceivedDate(now.getTime());
		packageReceipt.setLastEmailDate(now.getTime());

		new PackageReceiptDao().create(packageReceipt);
		
		//Send notifications
		EmailManager m = new EmailManager();
		m.sendRedirectDeliveryEmail(packageReceipt);
		
		//Send Text
		if (packageReceipt.getPackageRecipients().iterator().next().getNotificationSummary().isDeliveryTextSupported()) {
			String message = FileReader.readFile("redirect_delivery.sms");
			message = message.replace("attb_user_kinekNum", kinekNumber);
			message = message.replace("attb_kp_name", packageReceipt.getKinekPoint().getName()); 
			message = message.replace("attb_kp_address", packageReceipt.getKinekPoint().getAddress1());
			message = message.replace("attb_kp_phone", packageReceipt.getKinekPoint().getPhone());
					
			SMSManager sms = new SMSManager();		
			sms.sendSMS(ExternalSettingsManager.getMessageTrigger_redirectdelivery(), packageReceipt.getPackageRecipients().iterator().next(), message); 
		}
		
		getContext().getMessages().add(new SimpleMessage("Successfully redirected package to " + packageReceipt.getRedirectLocation()));		

		return new RedirectResolution(DeliveryActionBean.class, "view");
	}
	
	private String generateTransactionId(int kinekPointId, Calendar current){
		String timePortion = String.valueOf(current.get(Calendar.HOUR_OF_DAY))
							+ String.valueOf(current.get(Calendar.MINUTE))
							+ String.valueOf(current.get(Calendar.SECOND));
		
		String kinekPointIdPortion = String.format("%05d", kinekPointId);
		
		return kinekPointIdPortion+"-"+timePortion;
	}

	/**
	 * This is an entry point from the Kinek Number Search Functionality.
	 * Ideally I should be able to post the tagsearch_step2 form to this bean, but I can't get it to work.
	 */
	@DontValidate @SuppressWarnings("unchecked")
	public Resolution validateSearchResults() throws Exception {
		List<String> selectedUserIds = (List<String>)(this.getSessionAttribute("selectedUserIds"));
		redirectLocation = (String)this.getSessionAttribute("redirectLocation");
		courierId = (Integer)this.getSessionAttribute("courierId");
		reasonId = (Integer)this.getSessionAttribute("reasonId");
		customInfo = (String)this.getSessionAttribute("customInfo");
		depotId = (Integer)this.getSessionAttribute("depotId");

		
		// Get all the users
		List<User> selectedUsers = new ArrayList<User>();
		for (String userIdString : selectedUserIds) {
			int userId;
			try {
				userId = Integer.parseInt(userIdString);
			}
			catch (NumberFormatException ex) {
				Hashtable<String,String> logValues = new Hashtable<String,String>();
				logValues.put("UserID", userIdString);
	            logger.error(new ApplicationException("User ID is not valid", ex), logValues);
	            
				continue;
			}
			User p = new UserDao().read(userId);
			selectedUsers.add(p);
		}

		
		// If there is one user...
		if (selectedUsers.size() == 1) {
			kinekNumber = selectedUsers.get(0).getKinekNumber();
			return search();
		}
		
		//Handle if many users are selected 
		/*
		for (User user : selectedUsers) {
			Package p = new Package();
			p.setRedirectLocation(redirectLocation);
			p.setRecipient(user);
			p.setKinekPoint(user.getKinekPoint());

			if (reasonId > 0) {
				p.setRedirectReason(new RedirectReasonDao().read(reasonId));
			}

			if (courierId > 0) {
				p.setCourier(new CourierDao().read(courierId));
			}

			EmailManager m = new EmailManager();
			m.sendMultipleRedirectDeliveryEmail(p);
		} */

		return UrlManager.getAcceptPackageForm();
	}
	
	public Resolution kinekNumberSearch() {
		return UrlManager.getKinekNumberSearchForm();
	}
	
	private PackageReceipt populatePackageReceipt(String kinekNumber, Integer courierId, Integer reasonId, String customInfo, Integer depotId) 
		throws Exception {
		Package packageObj = new Package();
		PackageReceipt packageReceipt = new PackageReceipt();
		// Set recipient
		User recipient = new UserDao().readConsumer(kinekNumber);
		if (recipient == null) {
			ValidationError error = new SimpleError("There was no user found matching the supplied Kinek#.");
			getContext().getValidationErrors().add("kinekNumber", error);
		}
		else {
			packageReceipt.setPackageRecipients(new HashSet<User>());
			packageReceipt.getPackageRecipients().add(recipient);
		}
		
		// Set Courier
		Courier courier = new CourierDao().read(courierId);
		if (courier == null) {
			ValidationError error = new SimpleError("There was no courier found matching the supplied courier id.");
			getContext().getValidationErrors().add("recipientId", error);
		}
		else {
			packageObj.setCourier(courier);
		}
		
		// Set Custom Info
		if (customInfo != null) {
			packageObj.setCustomInfo(customInfo);
		}
		
		// Set Reason
		RedirectReason reason = new RedirectReasonDao().read(reasonId);
		if (reason == null)
		{
			ValidationError error = new SimpleError("There was no redirect reason found matching the supplied redirect reason id");
			getContext().getValidationErrors().add("courierId", error);
		}
		else {
			packageReceipt.setRedirectReason(reason);
		}
		
		
		//Set depot
		if(depotId == null){
			setUserDepots(new UserDao().fetchUserKinekPoints(getActiveUser().getUserId()));
			if(userDepots.size() == 1){
				depotId = userDepots.get(0).getDepotId();
			}
			else{
				ValidationError error = new SimpleError("There was no depot provided.");
				getContext().getValidationErrors().add("depotId", error);
			}	
		}	
		if(depotId != null){
			packageReceipt.setKinekPoint(new KinekPointDao().read(depotId));
			packageReceipt.setTransactionId(generateTransactionId(depotId, Calendar.getInstance()));	
		}
		
		
		packageReceipt.setUserId(getActiveUser().getUserId());
		packageReceipt.setApp(getActiveUser().getApp().toString());
		
		packageReceipt.setPackages(new HashSet<Package>());
		packageReceipt.getPackages().add(packageObj);
		
		return packageReceipt;
	}	

	
	// *** Form Elements ***
	public int getCourierId() {
		return courierId;
	}

	public void setCourierId(int courierId) {
		this.courierId = courierId;
	}

	public String getRedirectLocation() {
		return redirectLocation;
	}

	public void setRedirectLocation(String s) {
		this.redirectLocation = s;
	}

	public int getReasonId() {
		return reasonId;
	}

	public void setReasonId(int reasonId) {
		this.reasonId = reasonId;
	}

	public String getKinekNumber() {
		return kinekNumber;
	}

	public void setKinekNumber(String kinekNumber) {
		this.kinekNumber = kinekNumber;
	}

	public String getCustomInfo() {
		return customInfo;
	}

	public void setCustomInfo(String customInfo) {
		this.customInfo = customInfo;
	}
	
	public String getVia() {
		return ConfigurationManager.getViaRedirect();
	}
	
	// *** FORM POPULATOR METHODS ***
	
	public List<Courier> getCouriers() throws Exception {
		return new CourierDao().fetch();	
	}

	public List<RedirectReason> getRedirectReasons() throws Exception {
		return new RedirectReasonDao().fetch();	
	}
	
	public Package getPackage() {
		return kpPackage;
	}
	
	public void setPackage(Package p) {
		this.kpPackage = p;
	}
	
	public void setPackageReceipt(PackageReceipt packageReceipt){
		this.packageReceipt = packageReceipt;
	}
	
	public PackageReceipt getPackageReceipt(){
		return packageReceipt;
	}

	public void setUserDepots(List<KinekPoint> userDepots) {
		this.userDepots = userDepots;
	}

	public List<KinekPoint> getUserDepots() {
		return userDepots;
	}

	public void setDepotId(Integer depotId) {
		this.depotId = depotId;
	}

	public Integer getDepotId() {
		return depotId;
	} 
}
