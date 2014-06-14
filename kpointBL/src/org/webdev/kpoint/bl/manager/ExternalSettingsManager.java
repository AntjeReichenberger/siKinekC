package org.webdev.kpoint.bl.manager;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.util.ApplicationProperty;

public class ExternalSettingsManager {
	
	private static final KinekLogger logger = new KinekLogger(ExternalSettingsManager.class);
	
	/**
	 * Retrieves the external setting indicating whether the application is in debug mode or not.
	 * We always need this to have a value, even if the external setting is not found. 
	 * If it is missing, the stack trace will be printed to the screen, which could pose security problems in 
	 * a production environment.
	 * @return True if the application is in debug mode; otherwise False
	 */
	public static boolean getIsInDebugMode() {
		boolean isInDebugMode;
		try {
			isInDebugMode = Boolean.parseBoolean(ApplicationProperty.getInstance().getProperty("external.debugMode"));
		}
		catch (NullPointerException ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("ConfigKey", "external.debugMode");
            logger.error(new ApplicationException("Configuration Key not found.  IsInDebugMode will default to false.", ex), logProps);
            
            isInDebugMode = false;
		}
		return isInDebugMode;
	}
	
	public static String getProperty (String prop) {
		return ApplicationProperty.getInstance().getProperty(prop);
	}
	public static String getDatabaseConnectionString() {
		return ApplicationProperty.getInstance().getProperty("external.db.connectionurl");
	}

	public static String getDatabaseUsername() {
		return ApplicationProperty.getInstance().getProperty("external.db.username");
	}
	
	public static String getDatabasePassword() {
		return ApplicationProperty.getInstance().getProperty("external.db.password");
	}
	
	public static String getEmailSenderHost() {
		return ApplicationProperty.getInstance().getProperty("external.email.sender.host");	
	}

	public static String getEmailSenderMailer() {
		return ApplicationProperty.getInstance().getProperty("external.email.sender.mailer");
	}
	
	public static String getEmailSenderFrom() {
		return ApplicationProperty.getInstance().getProperty("external.email.sender.from");
	}
	
	public static String getEmailSenderPassword() {
		return ApplicationProperty.getInstance().getProperty("external.email.sender.pwd");
	}
	
	public static String getEmailSenderReplyTo() {
		return ApplicationProperty.getInstance().getProperty("external.email.sender.replyto");
	}

	public static String getEmailTargetContactUs() {
		return ApplicationProperty.getInstance().getProperty("external.email.target.contactus");
	}
	
	public static String getEmailTargetBCC() {
		return ApplicationProperty.getInstance().getProperty("external.email.target.bcc");
	}

	public static String getEmailBackgroundImage() {
		return ApplicationProperty.getInstance().getProperty("external.email.background.gif");
	}
	
	public static String getEmailHeaderImage() {
		return ApplicationProperty.getInstance().getProperty("external.email.header");
	}
	
	public static String getEmailFooterImage() {
		return ApplicationProperty.getInstance().getProperty("external.email.footer");
	}
	
	public static String getSMSUsername() {
		return ApplicationProperty.getInstance().getProperty("external.sms.user");
	}
	
	public static String getSMSPassword() {
		return ApplicationProperty.getInstance().getProperty("external.sms.password");
	}
	
	public static String getSMSApiId() {
		return ApplicationProperty.getInstance().getProperty("external.sms.apiId");
	}
	
	public static String getSMSToEmail() {
		return ApplicationProperty.getInstance().getProperty("external.sms.toEmail");
	}
	
	public static String getSMSReplyEmail() {
		return ApplicationProperty.getInstance().getProperty("external.sms.replyEmail");
	}

	public static String getHttpSMSURL() {
		return ApplicationProperty.getInstance().getProperty("external.sms.http.url");
	}
	
	public static String getAdminPortalUrl() {
		return ApplicationProperty.getInstance().getProperty("external.admin.portal.url");
	}
	
	public static String getConsumerPortalUrl() {
		return ApplicationProperty.getInstance().getProperty("external.consumer.portal.url");
	}
	
	public static String getKinekUrl() {
		return ApplicationProperty.getInstance().getProperty("external.url.kinek");
	}
	
	public static String getConsumerPasswordResetUrl() {
		return ApplicationProperty.getInstance().getProperty("external.consumer.password.reset.url");
	}
	
	public static String getAdminPasswordResetUrl() {
		return ApplicationProperty.getInstance().getProperty("external.admin.password.reset.url");
	}
	
	public static String getFAQUrl() {
		return ApplicationProperty.getInstance().getProperty("external.url.faq");
	}

	public static String getAdminPortalBaseUrl() {
		return ApplicationProperty.getInstance().getProperty("external.url.adminPortal");
	}
	
	public static String getConsumerPortalBaseUrl() {
		return ApplicationProperty.getInstance().getProperty("external.url.consumerPortal");
	}
	
	public static String getDepotPortalBaseUrl() {
		return ApplicationProperty.getInstance().getProperty("external.url.depotPortal");
	}

	public static String getAdminPortalGoogleGDataUrl() {
		return ApplicationProperty.getInstance().getProperty("external.adminPortal.gdata.url");
	}

	public static String getAdminPortalGoogleGDataV3Url() {
		return ApplicationProperty.getInstance().getProperty("external.adminPortal.gdataV3.url");
	}
	
	public static String getTermsAndConditionsUrl(){
		return ApplicationProperty.getInstance().getProperty("external.consumer.terms.url");
	}	

	public static String getAdminPortalGoogleGDataAuthorizationKey() {
		return ApplicationProperty.getInstance().getProperty("external.adminPortal.gdata.authorizationkey");
	}
	
	public static String getConsumerPortalGoogleGDataAuthorizationKey() {
		return ApplicationProperty.getInstance().getProperty("external.consumerPortal.gdata.authorizationkey");
	}
	public static String getConsumerPortalGoogleGDataV3AuthorizationKey() {
		return ApplicationProperty.getInstance().getProperty("external.consumerPortal.gdataV3.authorizationkey");
	}
	
	public static List<String> getTrainingKinekNumbers() {
		String trainingKinekNumbersString = ApplicationProperty.getInstance().getProperty("external.training.kinekNumber");
		String[] trainingKinekNumberArray = trainingKinekNumbersString.split(",");
		List<String> trainingKinekNumberList = Arrays.asList(trainingKinekNumberArray);
 		return trainingKinekNumberList; 
	}
	
	public static int getMessageMedium_Email() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagemedium.id.email"));
	}
	
	public static int getMessageMedium_SMS() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagemedium.id.sms"));
	}
	
	public static int getMessageTrigger_friend() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.friend"));
	}
	
	public static int getMessageTrigger_contactinfo() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.contactinfo"));
	}
	
	public static int getMessageTrigger_pickupnotice() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.pickupnotice"));
	}
	
	public static int getMessageTrigger_pickupreminder() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.pickupreminder"));
	}
	
	public static int getMessageTrigger_creditexpirationwarning() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.creditexpirationwarning"));
	}
	
	public static int getMessageTrigger_signup() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.signup"));
	}
	
	public static int getMessageTrigger_consumercontactus() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.consumercontactus"));
	}
	
	public static int getMessageTrigger_consumerpasswordreset() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.consumerpasswordreset"));
	}
	
	public static int getMessageTrigger_acceptdelivery() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.acceptdelivery"));
	}
	
	public static int getMessageTrigger_redirectdelivery() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.redirectdelivery"));
	}
	
	public static int getMessageTrigger_admincreation() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.admincreation"));
	}
	
	public static int getMessageTrigger_adminpasswordreset() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.adminpasswordreset"));
	}
	
	public static int getMessageTrigger_admincontactus() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.admincontactus"));
	}
	
	public static int getMessageTrigger_kppromonotification() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.kppromonotification"));
	}
	
	public static int getMessageTrigger_invoicesent() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.invoicesent"));
	}
	
	public static int getMessageTrigger_invoicesystemnotice() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.invoicesystemnotice"));
	}
	
	public static int getMessageTrigger_invoicepaid() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.invoicepaid"));
	}
	
	public static int getMessageTrigger_registrationReminderEmailNoProfile() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.registrationReminderEmailNoProfile"));
	}
	
	public static int getMessageTrigger_registrationReminderEmailNoDefaultKP() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.registrationReminderEmailNoDefaultKP"));
	}
		
	public static int getNewsTypeIdNews() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.newsType.id.news"));
	}
	
	public static int getNewsTypeIdPress() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.newsType.id.press"));
	}
	
	public static int getDepotStatusActive() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.depotStatus.id.active"));
	}
	
	public static int getDepotStatusInactive() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.depotStatus.id.inactive"));
	}
	
	public static int getDepotStatusPending() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.depotStatus.id.pending"));
	}
	
	public static int getDepotStatusDeclined() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.depotStatus.id.declined"));
	}
	
	public static int getCreditStatus_Available() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.creditStatus.id.available"));
	}
	
	public static int getCreditStatus_Redeemed() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.creditStatus.id.redeemed"));
	}
	
	public static int getCreditStatus_Expired() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.creditStatus.id.expired"));
	}
	
	/**
	 * The threshold (in days) that must remain before an email/sms is 
	 * sent to customers indicating that a promotion, or more specifically an unclaimed
	 * credit, of theirs is about to expire.
	 * If the value is 5, an email/sms will be sent to them for 
	 * every credit of theirs that will expire in 5 or less days.
	 * @return
	 */
	public static int getConsumerCreditNotificationDayThreashold() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.consumerCreditNotification.dayThreshold"));
	}
	
	public static int getCreditCalculationType_Percent() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.creditCalculationType.id.percent"));
	}
	
	public static int getCreditCalculationType_Dollar() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.creditCalculationType.id.dollar"));
	}
	
	public static String getInvoicePaypalURL() {
		return ApplicationProperty.getInstance().getProperty("external.invoice.paypal.url");
	}
	
	public static String getInvoicePaypalCertificateId() {
		return ApplicationProperty.getInstance().getProperty("external.invoice.paypal.certId");
	}
	
	public static String getInvoiceLocalPaypalCertificateName() {
		return ApplicationProperty.getInstance().getProperty("external.invoice.paypal.cert");
	}
	
	public static String getInvoicePaypalCertificateName() {
		return ApplicationProperty.getInstance().getProperty("external.invoice.paypal.paypalCert");
	}
	
	public static String getInvoicePrivateKeyName() {
		return ApplicationProperty.getInstance().getProperty("external.invoice.paypal.privateKey");
	}
	
	public static String getInvoiceKeyPassword() {
		return ApplicationProperty.getInstance().getProperty("external.invoice.paypal.keyPass");
	}
	
	public static String getInvoiceMerchantEmail() {
		return ApplicationProperty.getInstance().getProperty("external.invoice.merchantEmail");
	}
	
	public static String getInvoiceAdminEmail() {
		return ApplicationProperty.getInstance().getProperty("external.invoice.admin.email");
	}
	
	public static int getPaymentStatusIdApproved() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.paymentStatus.id.approved"));
	}
	
	public static int getPaymentStatusIdDeclined() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.paymentStatus.id.declined"));
	}
	
	public static String getDepotApplicationRecipientEmail() {
		return ApplicationProperty.getInstance().getProperty("external.email.depotApplication.Recipient");
	}
	
	public static String getSupportEmail() {
		return ApplicationProperty.getInstance().getProperty("external.email.support");
	}
	
	public static int getMessageTrigger_depotApplicationApproved() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.depotApplicationApproved"));
	}
	
	public static int getMessageTrigger_depotApplicationDeclined() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.depotApplicationDeclined"));
	}
	
	public static String getLatestNewsletterUrl() {
		return ApplicationProperty.getInstance().getProperty("external.url.latestnewsletter");
	}
	
	public static int getMessageTrigger_depotApplicationError() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.depotApplicationError"));
	}
	
	public static int getMessageTrigger_depotApplication() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.depotApplication"));
	}
	
	/**
	 * If set to true, the kpoint application will send 
	 * ConsumerCredit expiry notifications when it is initialized.
	 * 
	 * This will default to false if no external setting is found.
	 * We do not want the notifications to be sent in our dev environment or if there 
	 * are any startup problems. 
	 * @return True if credit notifications are to be sent; false otherwise
	 */
	public static boolean getConsumerCreditNotificationsEnabled() {
		boolean sendNotifications;
		try {
			sendNotifications = Boolean.parseBoolean(ApplicationProperty.getInstance().getProperty("external.consumerCreditNotification.enabled"));
		}
		catch (NullPointerException ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("ConfigKey", "external.consumerCreditNotification.enabled");
            logger.error(new ApplicationException("Configuration Key not found.  ConsumerCreditNotifications will default to false.", ex), logProps);

            sendNotifications = false;
		}
		return sendNotifications;
	}
	
	/**
	 * The time between runs of the ConsumerCreditNotification system
	 * @return 
	 */
	public static int getConsumerCreditNotificationsDelayTimeInHours() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.consumerCreditNotification.delayTimeInHours"));
	}
	
	/**
	 * If set to true, the kpoint application will set 
	 * expiry status when it is initialized.
	 * 
	 * This will default to false if no external setting is found.
	 * @return True if credit expiration status are to be set; false otherwise
	 */
	public static boolean getConsumerCreditExpirationCheckEnabled() {
		boolean sendNotifications;
		try {
			sendNotifications = Boolean.parseBoolean(ApplicationProperty.getInstance().getProperty("external.consumerCreditExpirationCheck.enabled"));
		}
		catch (NullPointerException ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("ConfigKey", "external.consumerCreditExpirationCheck.enabled");
            logger.error(new ApplicationException("Configuration Key not found.  ConsumerCreditExpirationCheck will default to false.", ex), logProps);

            sendNotifications = false;
		}
		return sendNotifications;
	}
	
	/**
	 * The time between runs of the ConsumerCreditExpirationCheck system
	 * @return 
	 */
	public static int getConsumerCreditExpirationCheckDelayTimeInHours() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.consumerCreditExpirationCheck.delayTimeInHours"));
	}

	public static String getEmailTableHeaderImage() {
		return ApplicationProperty.getInstance().getProperty("external.email.tableHeader.gif");
	}
	
	public static String getEmailTableOddImage() {
		return ApplicationProperty.getInstance().getProperty("external.email.tableOdd.gif");
	}
	
	public static String getEmailTableEvenImage() {
		return ApplicationProperty.getInstance().getProperty("external.email.tableEven.gif");
	}
	
	public static CharSequence getEmailButtonImage() {
		return ApplicationProperty.getInstance().getProperty("external.email.button.gif");
	}
	
	public static CharSequence getEmailDisabledButtonImage() {
		return ApplicationProperty.getInstance().getProperty("external.email.disabledButton.gif");
	}
	
	public static int getCreditIssueReason_Referral() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.creditIssueReason.id.referral"));
	}
	
	public static int getCreditIssueReason_Registration() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.creditIssueReason.id.registration"));
	}
	
	public static String getPromotionCode_Referral() {
		return ApplicationProperty.getInstance().getProperty("external.promotion.code.referral");
	}

	public static String getTwitterUrl() {
		return ApplicationProperty.getInstance().getProperty("external.url.twitter");
	}
	
	public static String getCMSMarketingPageUrl() {
		return ApplicationProperty.getInstance().getProperty("external.url.cms.marketing");
	}
	
	/**
	 * The number of days to wait since customer registration before sending a registration reminder email to a customer
	 */
	public static int getRegistrationReminderEmailWaitTime() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.email.registrationReminder.daysToWait"));
	}
	
	/**  
	 * MessageTrigger Id of the Reminder email that is sent to consumers who do not create a profile during the registration process
	 * @return MessageTrigger Id as int
	 */
	public static int getRegistrationReminderEmailNoProfile(){
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.registrationReminderEmailNoProfile"));
	}
	
	/**  
	 * MessageTrigger Id of the Reminder email that is sent to consumers who do not select a default KinekPoint during the registration process
	 * @return Registration Reminder Email - No Profile as int
	 */
	public static int getRegistrationReminderEmailNoDefaultKP(){
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.messagetrigger.id.registrationReminderEmailNoDefaultKP"));
	}	
	
	
	/**  
	 * The number of days to wait before sending a delivery reminder email to customer to pick up the package
	 * @return number of days as int
	 */
	public static int getDeliveryReminderEmailWaitTime(){
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.email.deliveryReminder.daysToWait"));
	}		
	
	/**  
	 * Standard delivery reminder email will not be sent to the customer after the certain number of days
	 * @return total number of days as int
	 */
	public static int getDeliveryReminderEmailEndDays(){
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.email.deliveryReminder.endDaysOfStandardEmail"));
	}	
	
	/**  
	 * After the specific number of days, a special "Last Chance" reminder email will be sent to the customer
	 * @return total number of days as int
	 */
	public static int getDeliveryReminderEmailSpecial(){
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.email.deliveryReminder.daysOfSpecialEmail"));
	}
	
	public static String getEmailMonitorUtilIPAddress(){
		return ApplicationProperty.getInstance().getProperty("external.emailmonitor.ipaddress");
	}
	
	public static boolean getConcatenateCss(){
		return Boolean.parseBoolean(ApplicationProperty.getInstance().getProperty("external.resources.concatenateCss"));
	}
	
	public static boolean isRecaptchaEnabled(){
		return Boolean.parseBoolean(ApplicationProperty.getInstance().getProperty("recaptcha.enabled"));
	}
	
	public static String getCaptchaPrivateKey(){
		return ApplicationProperty.getInstance().getProperty("recaptcha.privatekey");
	}
	
	public static String getCaptchaPublicKey(){
		return ApplicationProperty.getInstance().getProperty("recaptcha.publickey");
	}
	
	public static String getJanrainSignupTokenUrl(){
		return ApplicationProperty.getInstance().getProperty("janrain4j.token.url.signup");
	}
	
	public static String getJanrainApplicationId(){
		return ApplicationProperty.getInstance().getProperty("janrain4j.application.id");
	}
	
	public static String getFedexClientKey(){
		return ApplicationProperty.getInstance().getProperty("external.tracking.fedex.clientKey");
	}
	public static String getFedexPassword(){
		return ApplicationProperty.getInstance().getProperty("external.tracking.fedex.password");
	}
	public static String getFedexAccountNumber(){
		return ApplicationProperty.getInstance().getProperty("external.tracking.fedex.accountNumber");
	}
	public static String getFedexMeterNumber(){
		return ApplicationProperty.getInstance().getProperty("external.tracking.fedex.meterNumber");
	}
	public static String getFedexEndpoint(){
		return ApplicationProperty.getInstance().getProperty("external.tracking.fedex.endPoint");
	}
	
	public static String getUPSLicense(){
		return ApplicationProperty.getInstance().getProperty("external.tracking.ups.license");
	}
	public static String getUPSUsername(){
		return ApplicationProperty.getInstance().getProperty("external.tracking.ups.username");
	}
	public static String getUPSPassword(){
		return ApplicationProperty.getInstance().getProperty("external.tracking.ups.password");
	}
	public static String getUPSEndpoint(){
		return ApplicationProperty.getInstance().getProperty("external.tracking.ups.endpoint");
	}
	public static String getUPSDeliveredStatus(){
		return ApplicationProperty.getInstance().getProperty("external.tracking.ups.delivered.status");
	}
	
	public static String getUSPSUsername(){
		return ApplicationProperty.getInstance().getProperty("external.tracking.usps.username");
	}
	public static String getUSPSPassword(){
		return ApplicationProperty.getInstance().getProperty("external.tracking.usps.password");
	}
	public static String getUSPSEndpoint(){
		return ApplicationProperty.getInstance().getProperty("external.tracking.usps.endpoint");
	}
	public static String getUSPSDeliveredStatus(){
		return ApplicationProperty.getInstance().getProperty("external.tracking.usps.delivered.status");
	}
	
	public static String getCanadaPostUrl() {
		return ApplicationProperty.getInstance().getProperty("external.tracking.cp.url");
	}
	public static String getCanadaPostDeliveredStatus(){
		return ApplicationProperty.getInstance().getProperty("external.tracking.cp.delivered.status");
	}
	
	public static String getTrackingMailAddress(){
		return ApplicationProperty.getInstance().getProperty("external.tracking.mail.address");
	}	
	
	public static String getTrackingMailPassword(){
		return ApplicationProperty.getInstance().getProperty("external.tracking.mail.password");
	}
	
	public static String getTrackingMailProcessedFolder(){
		return ApplicationProperty.getInstance().getProperty("external.tracking.mail.processed.foldername");
	}
	
	public static String getTrackingMailProcessingFailedFolder(){
		return ApplicationProperty.getInstance().getProperty("external.tracking.mail.processing.failed.foldername");
	}
	
	/**
	 * Access to notifications that can be subscribed to
	 */
	public static int getTrackingEmailNotification(){
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("notification.tracking.email"));
	}	
	public static int getTrackingTextNotification(){
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("notification.tracking.text"));
	}
	public static int getTrackingPushNotification(){
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("notification.tracking.push"));
	}
	
	public static int getDeliveryTextNotification(){
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("notification.delivery.text"));
	}
	public static int getDeliveryPushNotification(){
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("notification.delivery.push"));
	}

	public static String getUrbanAirshipAPIEndpoint(){
		return ApplicationProperty.getInstance().getProperty("urbanairship.api.endpoint");
	}
	
	public static String getUrbanAirshipApplicationKey(){
		return ApplicationProperty.getInstance().getProperty("urbanairship.applicationkey");
	}
	
	public static String getUrbanAirshipApplicationMasterSecret(){
		return ApplicationProperty.getInstance().getProperty("urbanairship.applicationmastersecret");
	}
	
	public static int getUSPSId(){
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.tracking.usps.id"));
	}
	public static int getCanadaPostId() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.tracking.canadapost.id"));
	}	
	public static int getUPSId(){
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.tracking.ups.id"));
	}	
	public static int getFedexId(){
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.tracking.fedex.id"));
	}
	
	public static String getConsumerSignUpUrl(){
		return ApplicationProperty.getInstance().getProperty("external.consumer.signup.url");
	}
	
	public static String getIphoneUrl(){
		return ApplicationProperty.getInstance().getProperty("external.consumer.iphone.url");
	}

	public static String getPrivacyUrl() {
		return ApplicationProperty.getInstance().getProperty("external.consumer.privacy.url");
	}
	
	public static int getSMSLimit(){
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.sms.limit"));
	}

	public static int getPushLimit() {
		return Integer.parseInt(ApplicationProperty.getInstance().getProperty("external.push.limit"));
	}
	
	public static String getWordPressURL() {
		return ApplicationProperty.getInstance().getProperty("external.wordpress.url");
	}
	
	public static boolean getIsInLocalMode() {
		boolean isInLocalMode;
		try {
			isInLocalMode = Boolean.parseBoolean(ApplicationProperty.getInstance().getProperty("external.localMode"));
		}
		catch (NullPointerException ex) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("ConfigKey", "external.localMode");
            logger.error(new ApplicationException("Configuration Key not found.  IsInLocalMode will default to false.", ex), logProps);
            
            isInLocalMode = false;
		}
		return isInLocalMode;
	}
}
