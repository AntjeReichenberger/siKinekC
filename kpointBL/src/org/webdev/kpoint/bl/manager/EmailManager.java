package org.webdev.kpoint.bl.manager;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import org.webdev.kpoint.bl.persistence.CouponDao;
import org.webdev.kpoint.bl.persistence.CouponNotificationDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.MessageDao;
import org.webdev.kpoint.bl.persistence.PaymentDao;
import org.webdev.kpoint.bl.pojo.ConsumerCredit;
import org.webdev.kpoint.bl.pojo.Coupon;
import org.webdev.kpoint.bl.pojo.CouponNotification;
import org.webdev.kpoint.bl.pojo.Courier;
import org.webdev.kpoint.bl.pojo.CreditCalculationType;
import org.webdev.kpoint.bl.pojo.CreditCard;
import org.webdev.kpoint.bl.pojo.Email;
import org.webdev.kpoint.bl.pojo.Feature;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Language;
import org.webdev.kpoint.bl.pojo.Message;
import org.webdev.kpoint.bl.pojo.MessageMedia;
import org.webdev.kpoint.bl.pojo.MessageTrigger;
import org.webdev.kpoint.bl.pojo.Package;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.PayMethod;
import org.webdev.kpoint.bl.pojo.Payment;
import org.webdev.kpoint.bl.pojo.Promotion;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.tracking.PackageActivity;
import org.webdev.kpoint.bl.tracking.TrackEmailElement;
import org.webdev.kpoint.bl.util.ApplicationProperty;
import org.webdev.kpoint.bl.util.Emailer;
import org.webdev.kpoint.bl.util.FileReader;
import org.webdev.kpoint.bl.util.MessageLogger;
import org.webdev.kpoint.bl.util.NumberToEnglishConverter;
import org.webdev.kpoint.bl.util.Emailer.EmailType;

public class EmailManager {

	static SimpleDateFormat couponExpiryDateTime = new SimpleDateFormat("dd/MM/yyyy");

	private String getEmailLayout() {
		String emailLayout = FileReader.readFile("email_template.email");

		emailLayout = emailLayout.replace("img_header_bg",
				ExternalSettingsManager.getEmailHeaderImage());
		emailLayout = emailLayout.replace("img_footer_bg",
				ExternalSettingsManager.getEmailFooterImage());
		emailLayout = emailLayout.replace("img_content_bg",
				ExternalSettingsManager.getEmailBackgroundImage());
		return emailLayout;
	}

	// THIS IS FINAL SIGNUP EMAIL. IT SENTS JUST AFTER SELECTING THE KinekPoint.
	// BUT ONLY FIRST TIME
	public void sendFinalSignupEmail(User user)throws Exception {
		// read the welcome email message trigger id
		int welcomeMsgTriggerId = ExternalSettingsManager
				.getMessageTrigger_signup();
		Coupon coupon = getKinekPointCoupon(user, welcomeMsgTriggerId, user
				.getDepot().getDepotId());

		String target = user.getEmail();
		String bccTarget = ExternalSettingsManager.getEmailTargetBCC();
		String subject = ApplicationProperty.getInstance().getProperty(
				"email.user.signup.subject");
		String body = FileReader.readFile("create_consumer_signup.email");

		body = body.replace("img_header_bg",
				ExternalSettingsManager.getEmailHeaderImage());
		body = body.replace("img_footer_bg",
				ExternalSettingsManager.getEmailFooterImage());
		body = body.replace("img_content_bg",
				ExternalSettingsManager.getEmailBackgroundImage());
		body = body.replace("portal_link",
				ExternalSettingsManager.getConsumerPortalUrl());

		if(user.getFirstName() != null)
			body = body.replace("attb_firstName", user.getFirstName());
		else
			body = body.replace("attb_firstName", "to Kinek");
		
		if(user.getFullName() != null && !user.getFullName().equals(""))
			body = body.replace("user_name", user.getFullName());
		else
			body = body.replace("user_name", "your first and last name");
		body = body.replace("kp_address1", user.getDepot().getAddress1());
		body = body.replace("kp_number", user.getKinekNumber());
		body = body.replace("kp_name", user.getDepot().getName());
		body = body.replace("kp_city", user.getDepot().getCity());
		body = body.replace("kp_state_Or_province", user.getDepot().getState()
				.getName());
		body = body.replace("kp_zip_Or_postalcode", user.getDepot().getZip());

		body = body.replace("user_username", user.getUsername());

		body = body.replace("login_link",
				ExternalSettingsManager.getConsumerPortalUrl());
		body = body.replace("faq_link", ExternalSettingsManager.getFAQUrl());
		
		body = body.replaceAll("attb_coupon",buildCoupon(coupon));

		body = getHtmlWelcomeEmailTemplate(body);
		Email email = new Email();
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(target);
		email.setBcc(bccTarget);

		int signUpMessageTriggerId = ExternalSettingsManager
				.getMessageTrigger_signup();
		processEmailMsg(email, signUpMessageTriggerId);
	}

	public void sendPartnerSignupEmail(User user, int partnerId) throws Exception {
		// read the welcome email message trigger id
		int welcomeMsgTriggerId = ExternalSettingsManager.getMessageTrigger_signup();
		Coupon coupon = getKinekPointCoupon(user, welcomeMsgTriggerId, user.getDepot().getDepotId());

		String target = user.getEmail();
		String bccTarget = ExternalSettingsManager.getEmailTargetBCC();
		String subject = ApplicationProperty.getInstance().getProperty("email.user.signup.subject");
		String body = FileReader.readFile("create_consumer_signup.email");

		body = body.replace("img_header_bg", ExternalSettingsManager.getEmailHeaderImage());
		body = body.replace("img_footer_bg", ExternalSettingsManager.getEmailFooterImage());
		body = body.replace("img_content_bg", ExternalSettingsManager.getEmailBackgroundImage());
		body = body.replace("portal_link", ExternalSettingsManager.getConsumerPortalUrl());

		body = body.replace("attb_firstName", user.getFirstName());
		body = body.replace("user_name", user.getFullName());
		body = body.replace("kp_address1", user.getDepot().getAddress1());
		body = body.replace("kp_number", user.getKinekNumber());
		body = body.replace("kp_name", user.getDepot().getName());
		body = body.replace("kp_city", user.getDepot().getCity());
		body = body.replace("kp_state_Or_province", user.getDepot().getState().getName());
		body = body.replace("kp_zip_Or_postalcode", user.getDepot().getZip());
		body = body.replace("user_username", user.getUsername());

		body = body.replace("login_link", ExternalSettingsManager.getConsumerPortalUrl());
		body = body.replace("faq_link", ExternalSettingsManager.getFAQUrl());
		
		body = body.replaceAll("attb_coupon",buildCoupon(coupon));

		body = getHtmlWelcomeEmailTemplate(body);
		Email email = new Email();
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(target);
		email.setBcc(bccTarget);

		int signUpMessageTriggerId = ExternalSettingsManager.getMessageTrigger_signup();
		processEmailMsg(email, signUpMessageTriggerId);
	}

	public void sendNoProfileRegReminderEmail(User user) throws Exception {
		int noProfileRegMessageTriggerId = ExternalSettingsManager
				.getRegistrationReminderEmailNoProfile();
		Coupon coupon = getGenericCoupon(user, noProfileRegMessageTriggerId);

		sendRegisterReminderEmail(user,
				"email.user.reminder.profile.depotid.subject",
				"consumer_registration_reminder_noprofile.email",
				noProfileRegMessageTriggerId, coupon);
	}

	public void sendNoKPRegReminderEmail(User user) throws Exception {
		int noKPRegMessageTriggerId = ExternalSettingsManager
				.getRegistrationReminderEmailNoDefaultKP();
		Coupon coupon = getGenericCoupon(user, noKPRegMessageTriggerId);

		sendRegisterReminderEmail(user, "email.user.reminder.depotid.subject",
				"consumer_registration_reminder_nokp.email",
				noKPRegMessageTriggerId, coupon);
	}

	private void sendRegisterReminderEmail(User user, String sub,
			String emailTemplate, int messageTriggerId, Coupon coupon) throws Exception {

		String target = user.getEmail();
		String bccTarget = ExternalSettingsManager.getEmailTargetBCC();
		String subject = ApplicationProperty.getInstance().getProperty(sub);
		String body = FileReader.readFile(emailTemplate);

		body = body.replace("img_header_bg",
				ExternalSettingsManager.getEmailHeaderImage());
		body = body.replace("img_footer_bg",
				ExternalSettingsManager.getEmailFooterImage());
		body = body.replace("img_content_bg",
				ExternalSettingsManager.getEmailBackgroundImage());
		body = body.replace("portal_link",
				ExternalSettingsManager.getConsumerPortalUrl());

		body = body.replace("user_name", user.getFullName());
		body = body.replace("user_username", user.getUsername());

		if (coupon != null) {
			body = body.replace("attb_coupon_title", "KinekPoint Coupon:");
			body = body.replace("attb_coupon_image",
					"<img src=\"" + coupon.getImageUrl() + "\"/>");
		} else {
			body = body.replace("attb_coupon_title", "");
			body = body.replace("attb_coupon_image", "");
		}

		Email email = new Email();
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(target);
		email.setBcc(bccTarget);

		processEmailMsg(email, messageTriggerId);
	}

	private void processEmailMsg(Email email, int messageTriggerId) throws Exception {

		Emailer emailer = new Emailer();
		emailer.sendHtmlEmail(email, EmailType.HTML);

		// Create a Message object and it's supporting Media and Trigger
		// Then ... save the new message to the DB
		MessageMedia reminderMessageMedium = new MessageMedia();
		reminderMessageMedium.setId(ExternalSettingsManager
				.getMessageMedium_Email());

		MessageTrigger reminderMessageTrigger = new MessageTrigger();
		reminderMessageTrigger.setId(messageTriggerId);

		Message reminderMessageSent = new Message();
		reminderMessageSent.setMedium(reminderMessageMedium);
		reminderMessageSent.setTrigger(reminderMessageTrigger);
		reminderMessageSent.setRecipientEmail(email.getTo());
		reminderMessageSent.setContents(email.getBody());
		new MessageDao().create(reminderMessageSent);
	}

	public void sendPasswordResetEmail(User user, String url) throws Exception {
		String target = user.getEmail();
		String bccTarget = ExternalSettingsManager.getEmailTargetBCC();
		String subject = ApplicationProperty.getInstance().getProperty(
				"email.user.password.reset.subject");
		String body = FileReader.readFile("password_reset.email");

		body = body.replace("img_header_bg",
				ExternalSettingsManager.getEmailHeaderImage());
		body = body.replace("img_footer_bg",
				ExternalSettingsManager.getEmailFooterImage());
		body = body.replace("img_content_bg",
				ExternalSettingsManager.getEmailBackgroundImage());

		body = body.replace("user_name", user.getFullName());
		body = body.replace("user_username", user.getUsername());
		body = body.replace("reset_url", url);

		Email email = new Email();
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(target);
		email.setBcc(bccTarget);

		Emailer emailer = new Emailer();
		emailer.sendHtmlEmail(email, EmailType.HTML);

		// Create a Message object and it's supporting Media and Trigger
		// Then ... save the new message to the DB
		MessageMedia contactusMessageMedium = new MessageMedia();
		contactusMessageMedium.setId(ExternalSettingsManager
				.getMessageMedium_Email());

		MessageTrigger contactusMessageTrigger = new MessageTrigger();
		contactusMessageTrigger.setId(ExternalSettingsManager
				.getMessageTrigger_consumerpasswordreset());

		Message contactusMessageSent = new Message();
		contactusMessageSent.setMedium(contactusMessageMedium);
		contactusMessageSent.setTrigger(contactusMessageTrigger);
		contactusMessageSent.setRecipientEmail(email.getTo());
		contactusMessageSent.setContents(email.getBody());
		new MessageDao().create(contactusMessageSent);
	}

	public void sendContactInfoEmail(String toEmail, User user,
			String customMessage) throws Exception {
		String emailLayout = getEmailLayout();
		emailLayout = emailLayout.replaceAll("%email_body%",
				getContactInfoEmailBody(user, customMessage));

		String from = ExternalSettingsManager.getEmailSenderFrom();
		
		Email email = new Email();
		email.setTo(toEmail);
		email.setFr(from);
		String subject = ApplicationProperty.getInstance().getProperty(
				"email.user.contactInfo.subject");
		subject = subject.replaceAll("%first_name%", user.getFirstName());
		subject = subject.replaceAll("%last_name%", user.getLastName());
		email.setSubject(subject);
		email.setBody(emailLayout);

		Emailer emailer = new Emailer();
		emailer.sendHtmlEmail(email, EmailType.HTML);

		// Create a Message object and it's supporting Media and Trigger
		// Then ... save the new message to the DB
		MessageMedia contactinfoMessageMedium = new MessageMedia();
		contactinfoMessageMedium.setId(ExternalSettingsManager
				.getMessageMedium_Email());

		MessageTrigger contactinfoMessageTrigger = new MessageTrigger();
		contactinfoMessageTrigger.setId(ExternalSettingsManager
				.getMessageTrigger_contactinfo());

		Message contactinfoMessageSent = new Message();
		contactinfoMessageSent.setMedium(contactinfoMessageMedium);
		contactinfoMessageSent.setTrigger(contactinfoMessageTrigger);
		contactinfoMessageSent.setRecipientEmail(email.getTo());
		contactinfoMessageSent.setContents(email.getBody());
		new MessageDao().create(contactinfoMessageSent);
	}

	public String getContactInfoEmailBody(User user, String customMessage) {
		String emailBody = FileReader.readFile("send_contact_info.email");

		emailBody = emailBody.replaceAll("%first_name%", user.getFirstName());
		emailBody = emailBody.replaceAll("%last_name%", user.getLastName());
		emailBody = emailBody.replaceAll("%address%",
				getKinekPointFormattedAddress(user));
		String tempCustomMessage = customMessage.replaceAll("\\$",
				"\\\\\\\\\\\\\\$");
		emailBody = emailBody.replaceAll("%custom_message%",
				htmlifyString(tempCustomMessage));

		return emailBody;
	}

	private String getKinekPointFormattedAddress(User user) {
		String formattedAddress = user.getFirstName() + " "
				+ user.getLastName() + "<br />"
				+ user.getKinekPoint().getAddress1() + ", #"
				+ user.getKinekNumber() + "<br />"
				+ user.getKinekPoint().getName() + "<br />"
				+ user.getKinekPoint().getCity() + ", "
				+ user.getKinekPoint().getState().getName() + "<br />"
				+ user.getKinekPoint().getZip();
		return formattedAddress;
	}

	private String htmlifyString(String string) {
		return string.replaceAll("\r\n", "\n").replaceAll("\n", "<br />");
	}

	/** EMAILS SENT FROM ADMIN */
	public void sendMultipleAcceptDeliveryEmail(PackageReceipt receipt,	User depotAdmin) throws Exception {
		Set<User> pRList = receipt.getPackageRecipients();

		String kinekPointAddress1 = receipt.getKinekPoint().getAddress1();
		String kinekPointName = receipt.getKinekPoint().getName();
		boolean isMultiplePackages = (receipt.getPackages().size() > 1) ? true
				: false;

		for (User recipient : pRList) {
			int deliveryMsgTriggerId = ExternalSettingsManager
					.getMessageTrigger_acceptdelivery();
			Coupon coupon = getKinekPointCoupon(recipient,
					deliveryMsgTriggerId, receipt.getKinekPoint().getDepotId());

			String recipientFullName = recipient.getFullName();
			String content = "";
			if (!isMultiplePackages) {
				content = getAcceptDeliveryMultipleRecipientsEmailBody(receipt
						.getPackages().iterator().next(), recipientFullName,
						kinekPointAddress1, kinekPointName, receipt
								.getPackages().size(), coupon);
			} else {
				content = getAcceptDeliveryMultipleRecipientsAndPackagesEmailBody(
						receipt.getPackages(), recipientFullName,
						kinekPointAddress1, kinekPointName, receipt
								.getPackages().size(), coupon);
			}

			String body = getHtmlDeliveryEmailTemplate(content);
			Email email = new Email();
			email.setBody(body);
			email.setSubject(ApplicationProperty.getInstance().getProperty(
					"email.multiaccept.delivery.subject"));

			email.setTo(recipient.getEmail());

			email.setBcc(ExternalSettingsManager.getEmailTargetBCC());

			Emailer emailer = new Emailer();
			emailer.sendHtmlEmail(email, EmailType.HTML);

			logMessage(
					ExternalSettingsManager.getMessageTrigger_acceptdelivery(),
					email);
		}
	}

	private String getAcceptDeliveryMultipleRecipientsEmailBody(Package pkg,
			String recipientName, String kinekPointAddress,
			String kinekPointName, int numberOfParcels, Coupon coupon) {
		String body = FileReader
				.readFile("accept_delivery_singlepackage_multiplerecipients.email");
		SimpleDateFormat datef = new SimpleDateFormat("MMMM d, yyyy");
		body = body.replaceAll("attb_delivery_date", datef.format(new Date()));
		body = body.replaceAll("attb_courier", pkg.getCourier().getName()
				.equalsIgnoreCase("other") ? "a courier" : pkg.getCourier()
				.getName());
		body = body.replaceAll("attb_numberofparcels", NumberToEnglishConverter
				.convertNumberToEnglish(String.valueOf(numberOfParcels)));

		body = body.replaceAll("attb_name", recipientName);
		body = body.replaceAll("attb_depot_address", kinekPointAddress);
		body = body.replaceAll("attb_depot_name", kinekPointName);
		body = body.replaceAll("attb_iphoneurl",
				ExternalSettingsManager.getIphoneUrl());
		if (pkg.getShippedFrom() != null) {
			body = body.replace("attb_shipped_from", pkg.getShippedFrom());
		} else {
			body = body.replaceFirst("from", "");
			body = body.replace("attb_shipped_from", "");
		}

		body = body.replaceAll("attb_coupon",buildCoupon(coupon));

		return body;
	}

	private String getAcceptDeliveryMultipleRecipientsAndPackagesEmailBody(
			Set<Package> packageList, String recipientName,
			String kinekPointAddress, String kinekPointName,
			int numberOfParcels, Coupon coupon) {
		String body = FileReader
				.readFile("accept_delivery_multiplepackages_multiplerecipients.email");
		SimpleDateFormat datef = new SimpleDateFormat("MMMM d, yyyy");
		body = body.replaceAll("attb_delivery_date", datef.format(new Date()));
		body = body.replaceAll("attb_cells_stores_and_courier",
				getTableCellsStoresAndCouriers(packageList));
		body = body.replaceAll("attb_numberofparcels", NumberToEnglishConverter
				.convertNumberToEnglish(String.valueOf(numberOfParcels)));
		body = body.replaceAll("attb_name", recipientName);
		body = body.replaceAll("attb_depot_address", kinekPointAddress);
		body = body.replaceAll("attb_depot_name", kinekPointName);
		body = body.replaceAll("attb_iphoneurl",
				ExternalSettingsManager.getIphoneUrl());

		body = body.replaceAll("attb_coupon",buildCoupon(coupon));
		

		return body;
	}

	public void sendAcceptDeliveryEmail(PackageReceipt receipt, User depotAdmin) throws Exception {
		Set<User> pRList = receipt.getPackageRecipients();
		Iterator<User> pRIterator = pRList.iterator();
		User recipient = pRIterator.next();

		int deliveryMsgTriggerId = ExternalSettingsManager.getMessageTrigger_acceptdelivery();
		Coupon coupon = getKinekPointCoupon(recipient, deliveryMsgTriggerId, receipt.getKinekPoint().getDepotId());
		
		String content = "";
		boolean isMultiplePackages = (receipt.getPackages().size() > 1) ? true : false;

		if (!isMultiplePackages) {
			content = getAcceptDeliveryEmailBody(receipt, coupon);
		} else {
			content = getAcceptDeliveryMultiplePackagesEmailBody(receipt, coupon);
		}

		String body = getHtmlDeliveryEmailTemplate(content);
		String to = "";

		Email email = new Email();
		email.setBody(body);
		email.setSubject(ApplicationProperty.getInstance().getProperty("email.accept.delivery.subject"));

		to += recipient.getEmail();

		email.setTo(to);

		email.setBcc(ExternalSettingsManager.getEmailTargetBCC());

		Emailer emailer = new Emailer();
		emailer.sendHtmlEmail(email, EmailType.HTML);

		logMessage(ExternalSettingsManager.getMessageTrigger_acceptdelivery(), email);
	}

	private String getAcceptDeliveryEmailBody(PackageReceipt receipt, Coupon coupon) {
		String body = FileReader
				.readFile("accept_delivery_singlepackage_singlerecipient.email");
		SimpleDateFormat datef = new SimpleDateFormat("MMMM d, yyyy");
		body = body.replace("attb_delivery_date",
				datef.format(receipt.getReceivedDate()));

		Courier courier = receipt.getPackages().iterator().next().getCourier();
		body = body.replace("attb_courier",
				courier.getName().equalsIgnoreCase("other") ? "a courier"
						: courier.getName());

		Set<User> pRList = receipt.getPackageRecipients();
		body = body
				.replace("attb_name", pRList.iterator().next().getFullName());

		body = body.replace("attb_depot_city", receipt.getKinekPoint()
				.getCity());
		body = body.replace("attb_depot_state", receipt.getKinekPoint()
				.getState().getName());
		body = body.replace("attb_depot_address", receipt.getKinekPoint()
				.getAddress1());
		body = body.replace("attb_depot_name", receipt.getKinekPoint()
				.getName());

		body = body.replace("attb_depot_number", pRList.iterator().next()
				.getKinekNumber());
		body = body.replace("attb_portal_link",
				ExternalSettingsManager.getConsumerPortalUrl());
		body = body.replaceAll("attb_iphoneurl",
				ExternalSettingsManager.getIphoneUrl());

		Package packageObj = receipt.getPackages().iterator().next();
		if (packageObj.getShippedFrom() != null) {
			body = body.replace("attb_shipped_from",
					packageObj.getShippedFrom());
		} else {
			body = body.replaceFirst("from", "");
			body = body.replace("attb_shipped_from", "");
		}

		body = body.replaceAll("attb_coupon",buildCoupon(coupon));

		return body;
	}

	private String getAcceptDeliveryMultiplePackagesEmailBody(PackageReceipt receipt, Coupon coupon) {

		String body = FileReader.readFile("accept_delivery_multiplepackages_singlerecipient.email");
		SimpleDateFormat datef = new SimpleDateFormat("MMMM d, yyyy");
		body = body.replace("attb_delivery_date", datef.format(receipt.getReceivedDate()));

		Set<User> pRList = receipt.getPackageRecipients();
		Iterator<User> pRIterator = pRList.iterator();
		User recipient = pRIterator.next();
		body = body.replace("attb_name", recipient.getFullName());

		body = body.replace(
				"attb_numberofparcels",
				NumberToEnglishConverter.convertNumberToEnglish(
						String.valueOf(receipt.getPackages().size())));
		body = body.replace("attb_depot_city", receipt.getKinekPoint()
				.getCity());
		body = body.replace("attb_depot_state", receipt.getKinekPoint()
				.getState().getName());
		body = body.replace("attb_depot_address", receipt.getKinekPoint()
				.getAddress1());
		body = body.replace("attb_depot_name", receipt.getKinekPoint()
				.getName());

		body = body.replace("attb_depot_number", recipient.getKinekNumber());
		body = body.replace("attb_portal_link",
				ExternalSettingsManager.getConsumerPortalUrl());
		body = body.replaceAll("attb_iphoneurl",
				ExternalSettingsManager.getIphoneUrl());
		body = body.replaceAll("attb_cells_stores_and_courier",
				getTableCellsStoresAndCouriers(receipt.getPackages()));

		body = body.replaceAll("attb_coupon",buildCoupon(coupon));
	
		return body;
	}

	public void sendRedirectDeliveryEmail(PackageReceipt packageReceipt) throws Exception {
		String target = "";
		User recipient = packageReceipt.getPackageRecipients().iterator()
				.next();

		target = recipient.getEmail();

		String bccTarget = ExternalSettingsManager.getEmailTargetBCC();
		String name = recipient.getFirstName() + " " + recipient.getLastName();
		String address = packageReceipt.getKinekPoint().getFullAddress();
		String subject = ApplicationProperty.getInstance().getProperty(
				"email.declined.delivery.subject");
		String body = FileReader.readFile("parcel_declined.email");
		String location = packageReceipt.getRedirectLocation();
		String reason = packageReceipt.getRedirectReason().getName();

		body = body.replace("img_header_bg",
				ExternalSettingsManager.getEmailHeaderImage());
		body = body.replace("img_footer_bg",
				ExternalSettingsManager.getEmailFooterImage());
		body = body.replace("img_content_bg",
				ExternalSettingsManager.getEmailBackgroundImage());
		body = body.replace("faq_link", ExternalSettingsManager.getFAQUrl());

		body = body.replace("attb_redirect_location", location);
		body = body.replace("attb_redirect_reason", reason);
		body = body.replace("attb_name", name);
		body = body.replace("attb_depot_address", address);

		Email email = new Email();
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(target);
		email.setBcc(bccTarget);

		Emailer emailer = new Emailer();
		emailer.sendHtmlEmail(email, EmailType.HTML);

		logMessage(
				ExternalSettingsManager.getMessageTrigger_redirectdelivery(),
				email);
	}

	public void sendAdminCreationEmail(User user) throws Exception {
		String target = user.getEmail();
		String bccTarget = ExternalSettingsManager.getEmailTargetBCC();
		String subject = ApplicationProperty.getInstance().getProperty(
				"email.create.admin.subject");
		String body = FileReader.readFile("create_depotadmin.email");

		body = body.replace("img_header_bg",
				ExternalSettingsManager.getEmailHeaderImage());
		body = body.replace("img_footer_bg",
				ExternalSettingsManager.getEmailFooterImage());
		body = body.replace("img_content_bg",
				ExternalSettingsManager.getEmailBackgroundImage());
		body = body.replace("admin_portal_url",
				ExternalSettingsManager.getAdminPortalUrl());

		body = body.replace("user_name", user.getFullName());
		body = body.replace("user_username", user.getUsername());
		body = body.replace("user_password", user.getPassword());

		Email email = new Email();
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(target);
		email.setBcc(bccTarget);

		Emailer emailer = new Emailer();
		emailer.sendHtmlEmail(email, EmailType.HTML);

		logMessage(ExternalSettingsManager.getMessageTrigger_admincreation(),
				email);
	}

	public void sendNewKinekPointStaffEmail(User user) throws Exception {
		String target = user.getEmail();
		String bccTarget = ExternalSettingsManager.getEmailTargetBCC();
		String subject = ApplicationProperty.getInstance().getProperty(
				"email.create.kinekpointstaff.subject");
		String body = FileReader.readFile("create_depotstaff.email");

		body = body.replace("img_header_bg",
				ExternalSettingsManager.getEmailHeaderImage());
		body = body.replace("img_footer_bg",
				ExternalSettingsManager.getEmailFooterImage());
		body = body.replace("img_content_bg",
				ExternalSettingsManager.getEmailBackgroundImage());
		body = body.replace("depot_portal_url",
				ExternalSettingsManager.getDepotPortalBaseUrl()
						+ "/Login.action");

		body = body.replace("user_name", user.getFullName());
		body = body.replace("user_username", user.getUsername());
		body = body.replace("user_password", user.getPassword());

		Email email = new Email();
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(target);
		email.setBcc(bccTarget);

		Emailer emailer = new Emailer();
		emailer.sendHtmlEmail(email, EmailType.HTML);

		logMessage(ExternalSettingsManager.getMessageTrigger_admincreation(),
				email);
	}

	/**
	 * send delivery reminder email. It may either standard or special delivery
	 * reminder email
	 * 
	 * @param receipt
	 *            packageReceipt
	 * @param isSpecialReminderDeliverEmail
	 *            as boolean. If standard email then false otherwise true
	 * */
	public void sendReminderEmail(PackageReceipt receipt,
			boolean isSpecialReminderDeliveryEmail) throws Exception {
		if (receipt.getPackageRecipients().size() == 1) {
			sendReminderEmailSingleRecipient(receipt,
					isSpecialReminderDeliveryEmail);
		} else {
			sendReminderEmailMultipleRecipients(receipt,
					isSpecialReminderDeliveryEmail);
		}
	}

	private int calculateDaysRemaining(PackageReceipt receipt) {
		Calendar now = Calendar.getInstance();
		Long millisSinceArrived = now.getTimeInMillis()
				- receipt.getReceivedDate().getTime();
		long daysSinceArrived = millisSinceArrived / (1000 * 60 * 60 * 24);
		long daysRemaining = ConfigurationManager.getMaxDaysInDepot()
				- daysSinceArrived;
		// So people don't get emails with negative days.
		if (daysRemaining < 0) {
			daysRemaining = 0;
		}
		return (int) daysRemaining;
	}

	private void sendReminderEmailSingleRecipient(PackageReceipt receipt,
			boolean isSpecialReminderDeliveryEmail) throws Exception {
		int daysRemaining = calculateDaysRemaining(receipt);

		User recipient = receipt.getPackageRecipients().iterator().next();
		String content;

		if (receipt.getPackages().size() == 1) {
			content = getReminderEmailBodySinglePackageSingleRecipient(receipt,
					recipient.getFullName(), String.valueOf(daysRemaining),
					isSpecialReminderDeliveryEmail);
		} else {
			content = getReminderEmailBodyMultiplePackagesSingleRecipient(
					receipt, recipient.getFullName(),
					String.valueOf(daysRemaining),
					isSpecialReminderDeliveryEmail);
		}

		String body = getHtmlEmail(content);
		String subject = ApplicationProperty.getInstance().getProperty(
				"email.reminder.subject");
		String bcc = ExternalSettingsManager.getEmailTargetBCC();
		String from = ExternalSettingsManager.getEmailSenderFrom();

		Email email = new Email();
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(recipient.getEmail());
		email.setFr(from);
		email.setBcc(bcc);

		new Emailer().sendHtmlEmail(email, EmailType.HTML);

		logMessage(ExternalSettingsManager.getMessageTrigger_pickupreminder(),
				email);
	}

	private String getReminderEmailBodySinglePackageSingleRecipient(
			PackageReceipt receipt, String recipientName, String daysRemaining,
			boolean isSpecialReminderDeliveryEmail) {

		Set<User> pRList = receipt.getPackageRecipients();
		Iterator<User> pRIterator = pRList.iterator();
		User recipient = pRIterator.next();

		int deliveryReminderMsgTriggerId = ExternalSettingsManager
				.getMessageTrigger_pickupreminder();

		String body = "";
		if (isSpecialReminderDeliveryEmail)
			body = FileReader
					.readFile("special_reminder_singlepackage_singlerecipient.email");
		else
			body = FileReader
					.readFile("reminder_singlepackage_singlerecipient.email");

		Package pkg = receipt.getPackages().iterator().next();
		KinekPoint depot = receipt.getKinekPoint();
		SimpleDateFormat datef = new SimpleDateFormat("MMMM d, yyyy");
		body = body.replaceAll("attb_delivery_date",
				datef.format(receipt.getReceivedDate()));
		body = body.replaceAll("attb_user_name", recipientName);
		body = body.replaceAll("attb_depot_address", depot.getAddress1());
		body = body.replaceAll("attb_depot_name", depot.getName());
		body = body.replaceAll("attb_depot_phone", depot.getPhone());
		body = body.replaceAll("attb_days_remaining", daysRemaining);
		body = body.replaceAll("attb_courier", pkg.getCourier().getName()
				.equalsIgnoreCase("other") ? "a courier" : pkg.getCourier()
				.getName());
		if (pkg.getShippedFrom() != null) {
			body = body.replace("attb_shipped_from", pkg.getShippedFrom());
		} else {
			body = body.replaceFirst("from", "");
			body = body.replace("attb_shipped_from", "");
		}

		return body;
	}

	private String getReminderEmailBodyMultiplePackagesSingleRecipient(
			PackageReceipt receipt, String recipientName, String daysRemaining,
			boolean isSpecialReminderDeliveryEmail) {

		Set<User> pRList = receipt.getPackageRecipients();
		Iterator<User> pRIterator = pRList.iterator();
		User recipient = pRIterator.next();

		int deliveryReminderMsgTriggerId = ExternalSettingsManager
				.getMessageTrigger_pickupreminder();

		String body = "";
		if (isSpecialReminderDeliveryEmail)
			body = FileReader
					.readFile("special_reminder_multiplepackages_singlerecipient.email");
		else
			body = FileReader
					.readFile("reminder_multiplepackages_singlerecipient.email");

		KinekPoint depot = receipt.getKinekPoint();
		SimpleDateFormat datef = new SimpleDateFormat("MMMM d, yyyy");
		body = body.replaceAll(
				"attb_numberofparcels",
				NumberToEnglishConverter.convertNumberToEnglish(
						String.valueOf(receipt.getPackages().size()))
						.toLowerCase());
		body = body.replaceAll("attb_delivery_date",
				datef.format(receipt.getReceivedDate()));
		body = body.replaceAll("attb_user_name", recipientName);
		body = body.replaceAll("attb_depot_address", depot.getAddress1());
		body = body.replaceAll("attb_depot_name", depot.getName());
		body = body.replaceAll("attb_depot_phone", depot.getPhone());
		body = body.replaceAll("attb_days_remaining",
				String.valueOf(daysRemaining));
		body = body.replaceAll("attb_cells_stores_and_courier",
				getTableCellsStoresAndCouriers(receipt.getPackages()));

		return body;
	}

	private void sendReminderEmailMultipleRecipients(PackageReceipt receipt,
			boolean isSpecialReminderDeliveryEmail) throws Exception {
		int daysRemaining = calculateDaysRemaining(receipt);
		for (User recipient : receipt.getPackageRecipients()) {
			String content;

			// coupon
			int deliveryReminderMsgTriggerId = ExternalSettingsManager
					.getMessageTrigger_pickupreminder();
			if (receipt.getPackages().size() == 1) {
				content = getReminderEmailBodySinglePackageMultipleRecipients(
						receipt, recipient.getFullName(),
						String.valueOf(daysRemaining),
						isSpecialReminderDeliveryEmail);
			} else {
				content = getReminderEmailBodyMultiplePackagesMultipleRecipients(
						receipt, recipient.getFullName(),
						String.valueOf(daysRemaining),
						isSpecialReminderDeliveryEmail);
			}

			String body = getHtmlEmail(content);
			String subject = ApplicationProperty.getInstance().getProperty(
					"email.reminder.subject");
			String bcc = ExternalSettingsManager.getEmailTargetBCC();
			String from = ExternalSettingsManager.getEmailSenderFrom();

			Email email = new Email();
			email.setBody(body);
			email.setSubject(subject);
			email.setTo(recipient.getEmail());
			email.setFr(from);
			email.setBcc(bcc);

			new Emailer().sendHtmlEmail(email, EmailType.HTML);

			logMessage(
					ExternalSettingsManager.getMessageTrigger_pickupreminder(),
					email);
		}
	}

	private String getReminderEmailBodySinglePackageMultipleRecipients(
			PackageReceipt receipt, String recipientName, String daysRemaining,
			boolean isSpecialReminderDeliveryEmail) {

		String body = "";
		if (isSpecialReminderDeliveryEmail)
			body = FileReader
					.readFile("special_reminder_singlepackage_multiplerecipients.email");
		else
			body = FileReader
					.readFile("reminder_singlepackage_multiplerecipients.email");

		Package pkg = receipt.getPackages().iterator().next();
		KinekPoint depot = receipt.getKinekPoint();
		SimpleDateFormat datef = new SimpleDateFormat("MMMM d, yyyy");
		body = body.replaceAll("attb_delivery_date",
				datef.format(receipt.getReceivedDate()));
		body = body.replaceAll("attb_user_name", recipientName);
		body = body.replaceAll("attb_depot_address", depot.getAddress1());
		body = body.replaceAll("attb_depot_name", depot.getName());
		body = body.replaceAll("attb_depot_phone", depot.getPhone());
		body = body.replaceAll("attb_days_remaining",
				String.valueOf(daysRemaining));
		body = body.replaceAll("attb_courier", pkg.getCourier().getName()
				.equalsIgnoreCase("other") ? "a courier" : pkg.getCourier()
				.getName());
		if (pkg.getShippedFrom() != null) {
			body = body.replace("attb_shipped_from", pkg.getShippedFrom());
		} else {
			body = body.replaceFirst("from", "");
			body = body.replace("attb_shipped_from", "");
		}

		return body;
	}

	private String getReminderEmailBodyMultiplePackagesMultipleRecipients(
			PackageReceipt receipt, String recipientName, String daysRemaining,
			boolean isSpecialReminderDeliveryEmail) {

		String body = "";
		if (isSpecialReminderDeliveryEmail)
			body = FileReader
					.readFile("special_reminder_multiplepackages_multiplerecipients.email");
		else
			body = FileReader
					.readFile("reminder_multiplepackages_multiplerecipients.email");

		KinekPoint depot = receipt.getKinekPoint();
		SimpleDateFormat datef = new SimpleDateFormat("MMMM d, yyyy");
		body = body.replaceAll(
				"attb_numberofparcels",
				NumberToEnglishConverter.convertNumberToEnglish(
						String.valueOf(receipt.getPackages().size()))
						.toLowerCase());
		body = body.replaceAll("attb_delivery_date",
				datef.format(receipt.getReceivedDate()));
		body = body.replaceAll("attb_user_name", recipientName);
		body = body.replaceAll("attb_depot_address", depot.getAddress1());
		body = body.replaceAll("attb_depot_name", depot.getName());
		body = body.replaceAll("attb_depot_phone", depot.getPhone());
		body = body.replaceAll("attb_days_remaining",
				String.valueOf(daysRemaining));
		body = body.replaceAll("attb_cells_stores_and_courier",
				getTableCellsStoresAndCouriers(receipt.getPackages()));

		return body;
	}

	public String getPromotionEmailBody(KinekPoint depot, Promotion promotion,
			String emailNote) {
		String body = FileReader.readFile("promotion.email");

		String customMessage = emailNote == null ? "" : "<p>"
				+ emailNote.replaceAll("\n\r?", "<br />") + "</p>";

		// Calculate the number of days the package is permitted to remain at
		// the depot

		// Add images
		body = body.replace("img_header_bg",
				ExternalSettingsManager.getEmailHeaderImage());
		body = body.replace("img_footer_bg",
				ExternalSettingsManager.getEmailFooterImage());
		body = body.replace("img_content_bg",
				ExternalSettingsManager.getEmailBackgroundImage());

		String consumerCredit = getCreditStringWithPrefix(
				promotion.getConsumerCreditAmount(),
				promotion.getConsumerCreditCalcType());
		String depotCredit = getCreditStringWithPrefix(
				promotion.getDepotCreditAmount(),
				promotion.getDepotCreditCalcType());

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		body = body.replaceAll("attb_title", promotion.getTitle());
		body = body.replaceAll("attb_code", promotion.getCode());
		body = body.replaceAll("attb_description", promotion.getDescription());
		body = body.replaceAll("attb_start_date",
				df.format(promotion.getStartDate().getTime()));
		body = body.replaceAll("attb_end_date",
				df.format(promotion.getEndDate().getTime()));
		body = body.replaceAll("attb_consumer_value", consumerCredit);
		body = body.replaceAll("attb_depot_value", depotCredit);
		body = body.replaceAll("attb_quantity",
				String.valueOf(promotion.getAvailabilityCount()));
		body = body.replaceAll("attb_email_note", customMessage);

		return body;
	}

	private String getCreditStringWithPrefix(BigDecimal creditAmount,
			CreditCalculationType type) {
		if (type.getId() == ExternalSettingsManager
				.getCreditCalculationType_Dollar())
			return "\\$" + creditAmount.toPlainString();
		else if (type.getId() == ExternalSettingsManager
				.getCreditCalculationType_Percent())
			return creditAmount.toPlainString() + "%";
		else
			return creditAmount.toPlainString();
	}

	public void sendPromotionEmail(KinekPoint depot, Promotion promotion,
			String emailNote) throws Exception {
		String body = getPromotionEmailBody(depot, promotion, emailNote);
		String subject = ApplicationProperty.getInstance().getProperty(
				"email.promotion.subject");
		subject = subject.replaceAll("attb_title", promotion.getTitle());

		String bcc = ExternalSettingsManager.getEmailTargetBCC();
		String from = ExternalSettingsManager.getEmailSenderFrom();

		Email email = new Email();
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(depot.getEmail());
		email.setFr(from);
		email.setBcc(bcc);

		new Emailer().sendHtmlEmail(email, EmailType.HTML);

		logMessage(
				ExternalSettingsManager.getMessageTrigger_kppromonotification(),
				email);
	}

	/**
	 * Sends an email containg invoice error log information.
	 * 
	 * @param toEmail
	 *            The email to send it to
	 * @param message
	 *            The contents of the email
	 * @param subject
	 *            The subject of the email
	 */
	public void sendInvoiceLogEmail(String toEmail, String message,
			String subject) throws Exception {

		String body = FileReader.readFile("invoice_log.email");
		message = message.replace("\n", "<br/>");
		body = body.replace("attb_body", message);

		// Add images
		body = body.replace("img_header_bg",
				ExternalSettingsManager.getEmailHeaderImage());
		body = body.replace("img_footer_bg",
				ExternalSettingsManager.getEmailFooterImage());
		body = body.replace("img_content_bg",
				ExternalSettingsManager.getEmailBackgroundImage());

		String bcc = ExternalSettingsManager.getEmailTargetBCC();
		String from = ExternalSettingsManager.getInvoiceAdminEmail();

		Email email = new Email();
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(toEmail);
		email.setFr(from);
		email.setBcc(bcc);

		new Emailer().sendHtmlEmail(email, EmailType.HTML);

		logMessage(
				ExternalSettingsManager.getMessageTrigger_invoicesystemnotice(),
				email);
	}

	/**
	 * Sends an email to a KinekPoint saying their payment has been received.
	 * The email it is sent to is determined from the email of the depot of the
	 * payment's invoice
	 * 
	 * @param transactionNumber
	 *            The transaction number of the payment to send the email for
	 */
	public void sendInvoicePaidEmail(String transactionNumber) throws Exception {

		Payment payment = new PaymentDao().read(transactionNumber);
		if (payment.getStatus().getId() == ExternalSettingsManager
				.getPaymentStatusIdDeclined()) {
			return;
		}

		String body = FileReader.readFile("invoice_paid.email");
		String subject = ApplicationProperty.getInstance().getProperty(
				"email.invoice.paid.subject");
		subject = subject.replace("attb_inv_num", payment.getInvoice()
				.getInvoiceNumber());

		// Add images
		body = body.replace("img_header_bg",
				ExternalSettingsManager.getEmailHeaderImage());
		body = body.replace("img_footer_bg",
				ExternalSettingsManager.getEmailFooterImage());
		body = body.replace("img_content_bg",
				ExternalSettingsManager.getEmailBackgroundImage());

		SimpleDateFormat full = new SimpleDateFormat(
				"MMMM dd, yyyy h:mm:ss a z");
		String paymentDate = full.format(payment.getPaymentDate());

		SimpleDateFormat part = new SimpleDateFormat("MMMM d, yyyy");
		String startDate = part.format(payment.getInvoice().getStartDate());
		String endDate = part.format(payment.getInvoice().getEndDate());

		body = body.replace("attb_kp_name", payment.getInvoice()
				.getKinekPoint().getName());
		body = body.replace("attb_inv_start", startDate);
		body = body.replace("attb_inv_end", endDate);
		body = body.replace("attb_inv_amount", NumberFormat
				.getCurrencyInstance().format(payment.getAmount()));
		body = body.replace("attb_date", paymentDate);
		body = body.replace("attb_txn", payment.getTransactionId());

		String bcc = ExternalSettingsManager.getEmailTargetBCC();
		String from = ExternalSettingsManager.getEmailSenderFrom();

		Email email = new Email();
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(payment.getInvoice().getKinekPoint().getEmail());
		email.setFr(from);
		email.setBcc(bcc);

		new Emailer().sendHtmlEmail(email, EmailType.HTML);

		logMessage(ExternalSettingsManager.getMessageTrigger_invoicepaid(),
				email);
	}

	public void sendConsumerCreditExpiryEmail(ConsumerCredit credit) throws Exception {
		String body = getHtmlEmail(getConsumerCreditExpiryEmailBody(credit));

		String bcc = ExternalSettingsManager.getEmailTargetBCC();
		String from = ExternalSettingsManager.getEmailSenderFrom();

		Email email = new Email();
		email.setBody(body);
		email.setSubject(getConsumerCreditExpiryEmailSubject());
		email.setTo(credit.getUser().getEmail());
		email.setFr(from);
		email.setBcc(bcc);

		new Emailer().sendHtmlEmail(email, EmailType.HTML);

		logMessage(
				ExternalSettingsManager
						.getMessageTrigger_creditexpirationwarning(),
				email);
	}

	/**
	 * Sends an email to a kinek admin with the details of a new depot
	 * application
	 * 
	 * @param depot
	 *            The depot information
	 * @param user
	 *            The user information
	 */
	public boolean sendDepotRegistrationEmail(KinekPoint depot, User user) throws Exception {
		String body = FileReader.readFile("depot_registration.email");
		String subject = ApplicationProperty.getInstance().getProperty(
				"email.depotRegistration.subject");

		// Add images
		body = body.replace("img_header_bg",
				ExternalSettingsManager.getEmailHeaderImage());
		body = body.replace("img_footer_bg",
				ExternalSettingsManager.getEmailFooterImage());
		body = body.replace("img_content_bg",
				ExternalSettingsManager.getEmailBackgroundImage());

		body = body.replace("attb_first_name", user.getFirstName());
		body = body.replace("attb_last_name", user.getLastName());
		body = body.replace("attb_business_name", depot.getName());
		body = body.replace("attb_address1", depot.getAddress1());
		body = body.replace("attb_city", depot.getCity());
		body = body.replace("attb_country", depot.getState().getCountry()
				.getName());
		body = body.replace("attb_state", depot.getState().getName());
		body = body.replace("attb_postalcode", depot.getZip());
		body = body.replace("attb_email", depot.getEmail());
		body = body.replace("attb_username", user.getUsername());
		body = body.replace("attb_password", user.getPassword());
		body = body.replace("attb_phone", depot.getPhone());

		/*
		 * if(depot.getSizeAllowance() != null) body =
		 * body.replace("attb_parcel_size",
		 * depot.getSizeAllowance().getFriendlyName()); else body =
		 * body.replace("attb_parcel_size", "");
		 */

		if (user.getCellPhone() != null)
			body = body.replace("attb_mobilephone", user.getCellPhone());
		else
			body = body.replace("attb_mobilephone", "");

		if (depot.getAddress2() != null)
			body = body.replace("attb_address2", depot.getAddress2());
		else
			body = body.replace("attb_address2", "");

		if (depot.getReferralSource() != null)
			body = body.replace("attb_referenceSource", depot
					.getReferralSource().getDisplayName());
		else
			body = body.replace("attb_referenceSource", "");

		if (depot.getAssociation() != null)
			body = body.replace("attb_association", depot.getAssociation()
					.getName());
		else
			body = body.replace("attb_association", "");

		/*
		 * TODO if(depot.getReceivingFee() != null) body =
		 * body.replace("attb_receiving_fee",
		 * depot.getReceivingFee().toPlainString()); else body =
		 * body.replace("attb_receiving_fee", "");
		 */

		if (depot.getLanguages() != null) {
			String str = "";
			for (Language lang : depot.getLanguages()) {
				str += lang.getName() + ", ";
			}
			if (str.length() > 0)
				str = str.substring(0, str.length() - 2);

			body = body.replace("attb_languages", str);
		} else
			body = body.replace("attb_languages", "");

		if (depot.getPayMethod() != null) {
			String str = "";
			for (PayMethod method : depot.getPayMethod()) {
				str += method.getName() + ", ";
			}
			if (str.length() > 0)
				str = str.substring(0, str.length() - 2);

			body = body.replace("attb_payment_methods", str);
		} else
			body = body.replace("attb_payment_methods", "");

		if (depot.getCards() != null) {
			String str = "";
			for (CreditCard card : depot.getCards()) {
				str += card.getName() + ", ";
			}
			if (str.length() > 0)
				str = str.substring(0, str.length() - 2);

			body = body.replace("attb_cards", str);
		} else
			body = body.replace("attb_cards", "");

		if (depot.getFeatures() != null) {
			String str = "";
			for (Feature feature : depot.getFeatures()) {
				str += feature.getName() + ", ";
			}
			if (str.length() > 0)
				str = str.substring(0, str.length() - 2);

			body = body.replace("attb_features", str);
		} else
			body = body.replace("attb_features", "");

		body = body.replace("attb_duty",
				String.valueOf(depot.getAcceptsDutyAndTax()));
		body = body.replace("attb_monday_hours", depot.getOperatingHours()
				.getMondayStart()
				+ " to "
				+ depot.getOperatingHours().getMondayEnd());
		body = body.replace("attb_monday_closed",
				String.valueOf(depot.getOperatingHours().getClosedMonday()));
		body = body.replace("attb_tuesday_hours", depot.getOperatingHours()
				.getTuesdayStart()
				+ " to "
				+ depot.getOperatingHours().getTuesdayEnd());
		body = body.replace("attb_tuesday_closed",
				String.valueOf(depot.getOperatingHours().getClosedTuesday()));
		body = body.replace("attb_wednesday_hours", depot.getOperatingHours()
				.getWednesdayStart()
				+ " to "
				+ depot.getOperatingHours().getWednesdayEnd());
		body = body.replace("attb_wednesday_closed",
				String.valueOf(depot.getOperatingHours().getClosedWednesday()));
		body = body.replace("attb_thursday_hours", depot.getOperatingHours()
				.getThursdayStart()
				+ " to "
				+ depot.getOperatingHours().getThursdayEnd());
		body = body.replace("attb_thursday_closed",
				String.valueOf(depot.getOperatingHours().getClosedThursday()));
		body = body.replace("attb_friday_hours", depot.getOperatingHours()
				.getFridayStart()
				+ " to "
				+ depot.getOperatingHours().getFridayEnd());
		body = body.replace("attb_friday_closed",
				String.valueOf(depot.getOperatingHours().getClosedFriday()));
		body = body.replace("attb_saturday_hours", depot.getOperatingHours()
				.getSaturdayStart()
				+ " to "
				+ depot.getOperatingHours().getSaturdayEnd());
		body = body.replace("attb_saturday_closed",
				String.valueOf(depot.getOperatingHours().getClosedSaturday()));
		body = body.replace("attb_sunday_hours", depot.getOperatingHours()
				.getSundayStart()
				+ " to "
				+ depot.getOperatingHours().getSundayEnd());
		body = body.replace("attb_sunday_closed",
				String.valueOf(depot.getOperatingHours().getClosedSunday()));

		body = body.replace("attb_error", "");

		String bcc = ExternalSettingsManager.getEmailTargetBCC();
		String from = ExternalSettingsManager.getEmailSenderFrom();

		Email email = new Email();
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(ExternalSettingsManager.getDepotApplicationRecipientEmail());
		email.setFr(from);
		email.setBcc(bcc);

		boolean success = new Emailer().sendHtmlEmail(email, EmailType.HTML);

		logMessage(
				ExternalSettingsManager.getMessageTrigger_depotApplication(),
				email);

		return success;
	}

	/**
	 * Sends an email to a kinek admin with the details of a new depot
	 * application and a message stating there was an error and it must be
	 * created manually
	 * 
	 * @param depot
	 *            The depot information
	 * @param user
	 *            The user information
	 */
	public void sendDepotRegistrationErrorEmail(KinekPoint depot, User user) throws Exception {
		String body = FileReader.readFile("depot_registration.email");
		String subject = ApplicationProperty.getInstance().getProperty(
				"email.depotRegistration.error.subject");

		// Add images
		body = body.replace("img_header_bg",
				ExternalSettingsManager.getEmailHeaderImage());
		body = body.replace("img_footer_bg",
				ExternalSettingsManager.getEmailFooterImage());
		body = body.replace("img_content_bg",
				ExternalSettingsManager.getEmailBackgroundImage());

		body = body.replace("attb_first_name", user.getFirstName());
		body = body.replace("attb_last_name", user.getLastName());
		body = body.replace("attb_business_name", depot.getName());
		body = body.replace("attb_address1", depot.getAddress1());
		body = body.replace("attb_city", depot.getCity());
		body = body.replace("attb_country", depot.getState().getCountry()
				.getName());
		body = body.replace("attb_state", depot.getState().getName());
		body = body.replace("attb_postalcode", depot.getZip());
		body = body.replace("attb_email", depot.getEmail());
		body = body.replace("attb_username", user.getUsername());
		body = body.replace("attb_password", user.getPassword());
		body = body.replace("attb_phone", depot.getPhone());

		/*
		 * TODO if(depot.getSizeAllowance() != null) body =
		 * body.replace("attb_parcel_size",
		 * depot.getSizeAllowance().getFriendlyName()); else body =
		 * body.replace("attb_parcel_size", "");
		 */

		if (user.getCellPhone() != null)
			body = body.replace("attb_mobilephone", user.getCellPhone());
		else
			body = body.replace("attb_mobilephone", "");

		if (depot.getAddress2() != null)
			body = body.replace("attb_address2", depot.getAddress2());
		else
			body = body.replace("attb_address2", "");

		if (depot.getReferralSource() != null)
			body = body.replace("attb_referenceSource", depot
					.getReferralSource().getDisplayName());
		else
			body = body.replace("attb_referenceSource", "");

		if (depot.getAssociation() != null)
			body = body.replace("attb_association", depot.getAssociation()
					.getName());
		else
			body = body.replace("attb_association", "");

		/*
		 * TODO if(depot.getReceivingFee() != null) body =
		 * body.replace("attb_receiving_fee",
		 * depot.getReceivingFee().toPlainString()); else body =
		 * body.replace("attb_receiving_fee", "");
		 */

		if (depot.getLanguages() != null) {
			String str = "";
			for (Language lang : depot.getLanguages()) {
				str += lang.getName() + ", ";
			}
			if (str.length() > 0)
				str = str.substring(0, str.length() - 2);

			body = body.replace("attb_languages", str);
		} else
			body = body.replace("attb_languages", "");

		if (depot.getPayMethod() != null) {
			String str = "";
			for (PayMethod method : depot.getPayMethod()) {
				str += method.getName() + ", ";
			}
			if (str.length() > 0)
				str = str.substring(0, str.length() - 2);

			body = body.replace("attb_payment_methods", str);
		} else
			body = body.replace("attb_payment_methods", "");

		if (depot.getCards() != null) {
			String str = "";
			for (CreditCard card : depot.getCards()) {
				str += card.getName() + ", ";
			}
			if (str.length() > 0)
				str = str.substring(0, str.length() - 2);

			body = body.replace("attb_cards", str);
		} else
			body = body.replace("attb_cards", "");

		if (depot.getFeatures() != null) {
			String str = "";
			for (Feature feature : depot.getFeatures()) {
				str += feature.getName() + ", ";
			}
			if (str.length() > 0)
				str = str.substring(0, str.length() - 2);

			body = body.replace("attb_features", str);
		} else
			body = body.replace("attb_features", "");

		body = body.replace("attb_duty",
				String.valueOf(depot.getAcceptsDutyAndTax()));
		body = body.replace("attb_monday_hours", depot.getOperatingHours()
				.getMondayStart()
				+ " to "
				+ depot.getOperatingHours().getMondayEnd());
		body = body.replace("attb_monday_closed",
				String.valueOf(depot.getOperatingHours().getClosedMonday()));
		body = body.replace("attb_tuesday_hours", depot.getOperatingHours()
				.getTuesdayStart()
				+ " to "
				+ depot.getOperatingHours().getTuesdayEnd());
		body = body.replace("attb_tuesday_closed",
				String.valueOf(depot.getOperatingHours().getClosedTuesday()));
		body = body.replace("attb_wednesday_hours", depot.getOperatingHours()
				.getWednesdayStart()
				+ " to "
				+ depot.getOperatingHours().getWednesdayEnd());
		body = body.replace("attb_wednesday_closed",
				String.valueOf(depot.getOperatingHours().getClosedWednesday()));
		body = body.replace("attb_thursday_hours", depot.getOperatingHours()
				.getThursdayStart()
				+ " to "
				+ depot.getOperatingHours().getThursdayEnd());
		body = body.replace("attb_thursday_closed",
				String.valueOf(depot.getOperatingHours().getClosedThursday()));
		body = body.replace("attb_friday_hours", depot.getOperatingHours()
				.getFridayStart()
				+ " to "
				+ depot.getOperatingHours().getFridayEnd());
		body = body.replace("attb_friday_closed",
				String.valueOf(depot.getOperatingHours().getClosedFriday()));
		body = body.replace("attb_saturday_hours", depot.getOperatingHours()
				.getSaturdayStart()
				+ " to "
				+ depot.getOperatingHours().getSaturdayEnd());
		body = body.replace("attb_saturday_closed",
				String.valueOf(depot.getOperatingHours().getClosedSaturday()));
		body = body.replace("attb_sunday_hours", depot.getOperatingHours()
				.getSundayStart()
				+ " to "
				+ depot.getOperatingHours().getSundayEnd());
		body = body.replace("attb_sunday_closed",
				String.valueOf(depot.getOperatingHours().getClosedSunday()));

		body = body
				.replace(
						"attb_error",
						"<p>An error occured when creating this KinekPoint -- it must be manually created.</p>");

		String bcc = ExternalSettingsManager.getEmailTargetBCC();
		String from = ExternalSettingsManager.getEmailSenderFrom();

		Email email = new Email();
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(ExternalSettingsManager.getDepotApplicationRecipientEmail());
		email.setFr(from);
		email.setBcc(bcc);

		new Emailer().sendHtmlEmail(email, EmailType.HTML);

		logMessage(
				ExternalSettingsManager
						.getMessageTrigger_depotApplicationError(),
				email);
	}

	/**
	 * Sends an email to a new depot admin, welcoming them and giving some basic
	 * information
	 * 
	 * @param user
	 *            The user to send to
	 */
	public void sendDepotRegistrationApprovedEmail(User user, KinekPoint depot) throws Exception {
		String body = FileReader.readFile("depot_registration_approved.email");
		String subject = ApplicationProperty.getInstance().getProperty(
				"email.depotRegistration.approved.subject");

		body = body.replace("img_header_bg",
				ExternalSettingsManager.getEmailHeaderImage());
		body = body.replace("img_footer_bg",
				ExternalSettingsManager.getEmailFooterImage());
		body = body.replace("img_content_bg",
				ExternalSettingsManager.getEmailBackgroundImage());

		body = body.replace("business_name", depot.getName());
		body = body.replace("admin_portal_url",
				ExternalSettingsManager.getAdminPortalUrl());
		body = body.replace("latest_newsletter_url",
				ExternalSettingsManager.getLatestNewsletterUrl());
		body = body.replace("user_username", user.getUsername());
		body = body.replace("user_password", user.getPassword());

		String bcc = ExternalSettingsManager.getEmailTargetBCC();
		String from = ExternalSettingsManager.getEmailSenderFrom();

		Email email = new Email();
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(user.getEmail());
		email.setFr(from);
		email.setBcc(bcc);

		new Emailer().sendHtmlEmail(email, EmailType.HTML);

		logMessage(
				ExternalSettingsManager
						.getMessageTrigger_depotApplicationApproved(),
				email);
	}

	/**
	 * Sends an email to a recent depot applicant saying they were declined
	 * 
	 * @param depot
	 *            The depot to send to
	 */
	public void sendDepotRegistrationDeclinedEmail(KinekPoint depot) throws Exception {
		String body = FileReader.readFile("depot_registration_declined.email");
		String subject = ApplicationProperty.getInstance().getProperty(
				"email.depotRegistration.declined.subject");

		body = body.replace("img_header_bg",
				ExternalSettingsManager.getEmailHeaderImage());
		body = body.replace("img_footer_bg",
				ExternalSettingsManager.getEmailFooterImage());
		body = body.replace("img_content_bg",
				ExternalSettingsManager.getEmailBackgroundImage());

		body = body.replace("business_name", depot.getName());
		body = body.replace("contact_email",
				ExternalSettingsManager.getSupportEmail());

		String bcc = ExternalSettingsManager.getEmailTargetBCC();
		String from = ExternalSettingsManager.getEmailSenderFrom();

		Email email = new Email();
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(depot.getEmail());
		email.setFr(from);
		email.setBcc(bcc);

		new Emailer().sendHtmlEmail(email, EmailType.HTML);

		logMessage(
				ExternalSettingsManager
						.getMessageTrigger_depotApplicationDeclined(),
				email);
	}

	private String getConsumerCreditExpiryEmailBody(ConsumerCredit credit) {
		String body = FileReader.readFile("consumer_credit_expiry.email");
		body = body.replaceAll("attb_name", credit.getUser().getFullName());
		body = body.replaceAll("attb_kinek_consumer_url",
				ExternalSettingsManager.getConsumerPortalUrl());
		return body;
	}

	private String getConsumerCreditExpiryEmailSubject() {
		return ApplicationProperty.getInstance().getProperty(
				"email.consumerCreditExpiry.subject");
	}

	private String getHtmlEmail(String bodyContent) {
		String body = FileReader.readFile("html_email_layout.email");
		body = body.replaceAll("attb_img_header_bg",
				ExternalSettingsManager.getEmailHeaderImage());
		body = body.replaceAll("attb_img_footer_bg",
				ExternalSettingsManager.getEmailFooterImage());
		body = body.replaceAll("attb_img_content_bg",
				ExternalSettingsManager.getEmailBackgroundImage());
		body = body.replaceAll("attb_content", bodyContent);
		return body;
	}
	
	private String getHtmlDeliveryEmailTemplate(String bodyContent) {
		String body = FileReader.readFile("html_email_delivery_layout.email");
		body = body.replaceAll("attb_iphoneurl",ExternalSettingsManager.getIphoneUrl());
		body = body.replaceAll("attb_content", bodyContent);
		return body;
	}
	
	private String getHtmlWelcomeEmailTemplate(String bodyContent) {
		String body = FileReader.readFile("html_email_welcome_layout.email");
		body = body.replaceAll("attb_iphoneurl",ExternalSettingsManager.getIphoneUrl());
		body = body.replaceAll("attb_content", bodyContent);
		return body;
	}

	private String getHtmlTrackingEmailTemplate(String bodyContent) {
		String body = FileReader.readFile("html_email_tracking_layout.email");
		body = body.replaceAll("attb_iphoneurl",ExternalSettingsManager.getIphoneUrl());
		body = body.replaceAll("attb_content", bodyContent);
		return body;
	}

	private void logMessage(int triggerId, Email email) throws Exception {
		new MessageLogger().logEmailMessage(triggerId, email.getTo(),
				email.getBody());
	}

	private String getTableCellsStoresAndCouriers(Set<Package> packageList) {

		StringBuilder tableCell = new StringBuilder();
		for (Package packageObj : packageList) {
			tableCell.append("<tr>");
			tableCell
					.append("<td align=\"center\" style=\"border:1px solid #777777;\"><font size=\"2\" color=\"#777777\">");
			tableCell.append(packageObj.getShippedFrom() != null ? packageObj
					.getShippedFrom() : "Not specified");
			tableCell.append("</font></td>");
			tableCell
					.append("<td align=\"center\" style=\"border:1px solid #777777;\"><font size=\"2\" color=\"#777777\">");
			tableCell.append(packageObj.getCourier().getName());
			tableCell.append("</font></td>");
			tableCell.append("</tr>");
		}

		return tableCell.toString();
	}

	/**
	 * Determines if a Coupon should be included as part of the email otherwise
	 * return null
	 * 
	 * @param user
	 *            is User object need to be passed to check coupon is in the
	 *            CouponNotification table
	 * @param depot
	 *            is User's KinekPoint object need to be passed to get filtered
	 *            coupons
	 * @param messageTriggerId
	 *            is message trigger id need to be passed to get filtered
	 *            coupons
	 */
	private Coupon getGenericCoupon(User user, int messageTriggerId) throws Exception {
		CouponDao couponDao = new CouponDao();
		List<Coupon> filteredCoupons = couponDao
				.fetchGenericCoupons(messageTriggerId);

		return getCoupon(user, filteredCoupons, messageTriggerId);
	}

	/**
	 * Determines if a Coupon should be included as part of the email otherwise
	 * return null
	 * 
	 * @param user
	 *            is User object need to be passed to check coupon is in the
	 *            CouponNotification table
	 * @param depot
	 *            is User's KinekPoint object need to be passed to get filtered
	 *            coupons
	 * @param messageTriggerId
	 *            is message trigger id need to be passed to get filtered
	 *            coupons
	 */
	private Coupon getKinekPointCoupon(User user, int messageTriggerId,
			int depotId) throws Exception {
		CouponDao couponDao = new CouponDao();
		KinekPoint depot = null;
		if (depotId > 1) {
			depot = new KinekPointDao().read(depotId);
		}

		List<Coupon> filteredCoupons = couponDao.fetchKinekPointCoupons(depot,
				messageTriggerId);

		return getCoupon(user, filteredCoupons, messageTriggerId);
	}

	/**
	 * Determines if a Coupon should be included as part of the email otherwise
	 * return null
	 * 
	 * @param user
	 *            is User object need to be passed to check coupon is in the
	 *            CouponNotification table
	 * @param depot
	 *            is User's KinekPoint object need to be passed to get filtered
	 *            coupons
	 * @param messageTriggerId
	 *            is message trigger id need to be passed to get filtered
	 *            coupons
	 */
	private Coupon getCoupon(User user, List<Coupon> filteredCoupons,
			int messageTriggerId) throws Exception {

		Coupon coupon = null;
		if (filteredCoupons.size() > 0) {

			boolean isShowAlways = false;

			// check coupon is set to YES, If yes then the Coupon will always be
			// displayed in target emails, regardless of which other coupons may
			// be mapped.
			for (Coupon filteredCoupon : filteredCoupons) {
				if (filteredCoupon.isAlwaysShowCoupon()) {
					isShowAlways = true;
					coupon = filteredCoupon;
					break;
				}
			}
			if (!isShowAlways) {
				coupon = filteredCouponByNotReceivedOrReceivedAll(
						filteredCoupons, user);
			}

			insertCouponIntoCouponNotificationTable(coupon, user);
		}

		return coupon;
	}

	/***
	 * 1) Retrieve the list of CouponNotifications by userid and coupons 2) If
	 * there is no coupon in the CouponNotification table then retrieves closest
	 * expiry date coupon 3) else if there is coupon(s) in the
	 * CouponNotificationt table then 4) search the coupon is in the
	 * CouponNotification table if coupon is avaiable then return the coupon for
	 * email and insert into the CouponNotification table else retrieves the
	 * oldest (longest) received date's coupon
	 * */
	@SuppressWarnings("unchecked")
	private Coupon filteredCouponByNotReceivedOrReceivedAll(
			List<Coupon> coupons, User user) throws Exception {

		boolean isReceived = false;
		CouponDao couponDao = new CouponDao();
		CouponNotificationDao couponNDao = new CouponNotificationDao();
		Coupon couponForEmail = null;

		if (coupons.size() > 0) {

			List<CouponNotification> couponNotifications = couponNDao
					.fetchCouponNotificationsByUserIdAndCoupons(
							user.getUserId(), coupons);

			for (Coupon coupon : coupons) {

				// if coupon is in the couponnotification list then customer
				// already received the coupon
				isReceived = isCouponAvailableInCounponNotificationList(
						coupon.getCouponId(), couponNotifications);

				// if coupon is not in the couponnotification list then email
				// the coupon
				if (!isReceived) {
					couponForEmail = coupon;
					break;
				}
			}

			// if all coupons are received then send the coupon which has the
			// oldest received date (longest)
			if (isReceived) {
				int couponId = couponNotifications.get(0).getCoupon()
						.getCouponId();
				couponForEmail = couponDao.read(couponId);
			}

		}

		return couponForEmail;
	}

	// Search whether coupon is available in the CouponNotification table
	private boolean isCouponAvailableInCounponNotificationList(int couponId,
			List<CouponNotification> couponNotifications) {

		boolean isFound = false;

		for (CouponNotification couponN : couponNotifications) {
			if (couponId == couponN.getCoupon().getCouponId()) {
				isFound = true;
				break;
			}
		}

		return isFound;
	}

	// The System stores coupon into the CouponNotification table that is
	// included in the email
	private void insertCouponIntoCouponNotificationTable(Coupon coupon,
			User user) throws Exception {
		// store coupon into the couponnotification table that is emailed
		CouponNotificationDao couponNDao = new CouponNotificationDao();
		CouponNotification couponNotification = new CouponNotification();
		couponNotification.setCoupon(coupon);
		couponNotification.setUser(user);
		couponNDao.create(couponNotification);
	}

	public void sendTrackingUpdate(User user, String trackingNumber,
			String trackingName, String emailNote, PackageActivity latestUpdate) {
		String body = FileReader
				.readFile("consumer_tracking_single_update.email");

		String subject = ApplicationProperty.getInstance().getProperty(
				"email.tracking.update.subject");

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		String address = latestUpdate.getCity() + ","
				+ latestUpdate.getStateProv();

		body = body.replaceAll("attb_firstName", user.getFirstName());
		body = body.replaceAll("attb_trackingName", trackingName);
		body = body.replaceAll("attb_trackingStatus",
				latestUpdate.getActivity());
		
		
		if(latestUpdate.getCity() != null && latestUpdate.getStateProv() != null){
			body = body.replaceAll("attb_trackinglocation", latestUpdate.getCity()
					+ ", " + latestUpdate.getStateProv() + "<br/><br/>");
		}
		else{
			body = body.replaceAll("attb_trackinglocation", "");	
		}

		body = body.replaceAll("attb_consumerurl",
				ExternalSettingsManager.getConsumerPortalUrl());
		body = body.replaceAll("attb_iphoneurl",
				ExternalSettingsManager.getIphoneUrl());

		String bcc = ExternalSettingsManager.getEmailTargetBCC();
		String from = ExternalSettingsManager.getEmailSenderFrom();

		Email email = new Email();
		body = getHtmlTrackingEmailTemplate(body);
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(user.getEmail());
		email.setFr(from);
		email.setBcc(bcc);

		new Emailer().sendHtmlEmail(email, EmailType.HTML);
	}

	public void sendTrackingConfirmation(User user, String trackingNumber,
			String emailNote) {
		String body = FileReader
				.readFile("consumer_tracking_confirmation.email");

		// String customMessage = emailNote == null ? "" : "<p>" +
		// emailNote.replaceAll("\n\r?", "<br />") + "</p>";

		String subject = "Tracking Confirmation";// ApplicationProperty.getInstance().getProperty("email.tracking.update.subject");

		body = body.replaceAll("attb_name", user.getFullName());
		body = body.replaceAll("attb_trackingNumber", trackingNumber);
		body = body.replaceAll("attb_emailNote", emailNote);

		String bcc = ExternalSettingsManager.getEmailTargetBCC();
		String from = ExternalSettingsManager.getEmailSenderFrom();

		Email email = new Email();
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(user.getEmail());
		email.setFr(from);
		email.setBcc(bcc);

		new Emailer().sendHtmlEmail(email, EmailType.HTML);
	}

	public void sendTrackingProcessed(User user,
			List<TrackEmailElement> trackingElements, String emailNote) {
		String body = FileReader.readFile("consumer_tracking_processed.email");

		String subject = ApplicationProperty.getInstance().getProperty(
				"email.tracking.processed.subject");

		body = body.replaceAll("attb_firstName", user.getFirstName());
		body = body.replaceAll("attb_consumerurl",
				ExternalSettingsManager.getConsumerPortalUrl());
		body = body.replaceAll("attb_iphoneurl",
				ExternalSettingsManager.getIphoneUrl());

		String trackingRows = "";

		for (TrackEmailElement element : trackingElements) {
			String trackingRow = "<tr><td>" + element.getTrackingNumber()
					+ " </td>" + "<td>- " + element.getEmailStatus()
					+ "</td></tr>";
			trackingRows += trackingRow;
		}
		body = body.replaceAll("attb_trackingResults", trackingRows);
		
		String bcc = ExternalSettingsManager.getEmailTargetBCC();
		String from = ExternalSettingsManager.getEmailSenderFrom();

		Email email = new Email();
		body = getHtmlTrackingEmailTemplate(body);
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(user.getEmail());
		email.setFr(from);
		email.setBcc(bcc);

		new Emailer().sendHtmlEmail(email, EmailType.HTML);
	}

	public void sendTrackingPartialProcessed(User user,
			List<TrackEmailElement> trackingElements, String emailNote) {
		String body = FileReader
				.readFile("consumer_tracking_partial_processed.email");

		String subject = ApplicationProperty.getInstance().getProperty(
				"email.tracking.partial.subject");

		body = body.replaceAll("attb_firstName", user.getFirstName());
		body = body.replaceAll("attb_consumerurl",
				ExternalSettingsManager.getConsumerPortalUrl());
		body = body.replaceAll("attb_iphoneurl",
				ExternalSettingsManager.getIphoneUrl());

		String trackingRows = "";

		for (TrackEmailElement element : trackingElements) {
			String trackingRow = "<tr><td>" + element.getTrackingNumber()
					+ " </td>" + "<td>- " + element.getEmailStatus()
					+ "</td></tr>";
			trackingRows += trackingRow;
		}
		body = body.replaceAll("attb_trackingResults", trackingRows);
		
		String bcc = ExternalSettingsManager.getEmailTargetBCC();
		String from = ExternalSettingsManager.getEmailSenderFrom();

		Email email = new Email();
		body = getHtmlTrackingEmailTemplate(body);
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(user.getEmail());
		email.setFr(from);
		email.setBcc(bcc);

		new Emailer().sendHtmlEmail(email, EmailType.HTML);
	}

	public void sendTrackingInvalid(User user, String emailNote) {
		String body = FileReader.readFile("consumer_tracking_invalid.email");
		String subject = ApplicationProperty.getInstance().getProperty("email.tracking.invalid.subject");

		body = body.replaceAll("attb_firstName", user.getFirstName());
		body = body.replaceAll("attb_consumerurl",
				ExternalSettingsManager.getConsumerPortalUrl());
		body = body.replaceAll("attb_iphoneurl",
				ExternalSettingsManager.getIphoneUrl());

		String bcc = ExternalSettingsManager.getEmailTargetBCC();
		String from = ExternalSettingsManager.getEmailSenderFrom();

		Email email = new Email();
		body = getHtmlTrackingEmailTemplate(body);
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(user.getEmail());
		email.setFr(from);
		email.setBcc(bcc);

		new Emailer().sendHtmlEmail(email, EmailType.HTML);
	}

	public void sendTrackingSignUp(String senderEmail, String trackingNumber,
			String emailNote) {
		String body = FileReader.readFile("consumer_tracking_signup.email");

		String subject = ApplicationProperty.getInstance().getProperty(
				"email.tracking.unknown.subject");

		body = body.replaceAll("attb_consumerurl",
				ExternalSettingsManager.getConsumerPortalUrl());
		body = body.replaceAll("attb_consumersignupurl",
				ExternalSettingsManager.getConsumerSignUpUrl());
		body = body.replaceAll("attb_iphoneurl",
				ExternalSettingsManager.getIphoneUrl());

		String bcc = ExternalSettingsManager.getEmailTargetBCC();
		String from = ExternalSettingsManager.getEmailSenderFrom();

		Email email = new Email();
		body = getHtmlTrackingEmailTemplate(body);
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(senderEmail);
		email.setFr(from);
		email.setBcc(bcc);

		new Emailer().sendHtmlEmail(email, EmailType.HTML);
	}

	public void sendTrackingUnexpected(User user) {
		String body = FileReader.readFile("consumer_tracking_unexpected.email");
		String subject = ApplicationProperty.getInstance().getProperty("email.tracking.unexpected.subject");

		body = body.replaceAll("attb_firstName", user.getFirstName());
		body = body.replaceAll("attb_consumerurl",
				ExternalSettingsManager.getConsumerPortalUrl());
		body = body.replaceAll("attb_iphoneurl",
				ExternalSettingsManager.getIphoneUrl());

		String bcc = ExternalSettingsManager.getEmailTargetBCC();
		String from = ExternalSettingsManager.getEmailSenderFrom();

		Email email = new Email();
		body = getHtmlTrackingEmailTemplate(body);
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(user.getEmail());
		email.setFr(from);
		email.setBcc(bcc);

		new Emailer().sendHtmlEmail(email, EmailType.HTML);
	}
	
	private String buildCoupon(Coupon coupon){
		String couponString =
			"<b>attb_coupon_title</b><br/>"+
			"attb_coupon_image"+
			"<br /><br />";
		
		if (coupon != null) {
			couponString = couponString.replace("attb_coupon_title", "KinekPoint Coupon:");
			couponString = couponString.replace("attb_coupon_image",
					"<img src=\"" + coupon.getImageUrl() + "\"/>");
		} else {
			couponString = "";
		}
		return couponString;	
	}
	
}