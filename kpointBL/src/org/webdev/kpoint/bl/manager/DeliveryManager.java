package org.webdev.kpoint.bl.manager;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.persistence.CourierDao;
import org.webdev.kpoint.bl.persistence.KPPackageRateDao;
import org.webdev.kpoint.bl.persistence.KPSkidRateDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.PackageReceiptDao;
import org.webdev.kpoint.bl.persistence.PackageWeightGroupDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.KPPackageRate;
import org.webdev.kpoint.bl.pojo.KPSkidRate;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Package;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.PackageWeightGroup;
import org.webdev.kpoint.bl.pojo.User;

public class DeliveryManager {

	private static final KinekLogger logger = new KinekLogger(DeliveryManager.class);
	
	
	/**
	 * * KGG Oct 19, 2013
	 * 
	 * Accepts delivery for one or more packages.  This process does allow the same package to be received for more than 
	 * one user when unique user identification is not possible by examining the package (ie. package label is missing).  In this
	 * case, the package is flagged to require proof of purchase during the pickup.
	 * @param kinekPointId the unique identifier of the kinekpoint to fetch weight groups for
	 * @return a String representing a unique transaction id
	 * @throws Exception 
	 */
	public static PackageReceipt acceptDeliveryWithoutNotification (User depotUser, KinekPoint receivingKinekPoint, List<String> consumerIds, Set<Package> packages) throws Exception{
		//Hashtable<String, String> info = new Hashtable<String, String>();
		Calendar now = Calendar.getInstance();
		PackageReceipt receipt = new PackageReceipt();
		receipt.setUserId(depotUser.getUserId());
		receipt.setApp(depotUser.getApp().toString());
		receipt.setKinekPoint(receivingKinekPoint);
		receipt.setTransactionId(generateTransactionId(receivingKinekPoint.getDepotId(), now));
		
		Set<User> packageRecipients = new HashSet<User>();
		for (String selectedConsumer : consumerIds) {
			User recipient = new UserDao().readConsumer(selectedConsumer);
			packageRecipients.add(recipient);
		}
		
		receipt.setPackageRecipients(packageRecipients);
		receipt.setReceivedDate(now.getTime());
		receipt.setLastEmailDate(now.getTime());

		if (consumerIds.size() > 1) {
			receipt.setRequiresProofOfPurchase(true);
		}else {
			receipt.setRequiresProofOfPurchase(false);
		}
		receipt.setPackages(packages);
		
		//This is where we actually create the package receipt record
		PackageReceiptDao receiptDao = new PackageReceiptDao();
		receiptDao.create(receipt);
		
		System.out.println (String.format("DeliveryManager.java acceptDeliveryWithoutNotification - Transaction ID: %s",receipt.getTransactionId())) ;
		return receipt ;
		
	}	
	
	public static void  sendEmailDeliveryNotification (User depotUser, PackageReceipt receipt,List<String> consumerIds ) throws Exception{
		
		EmailManager m = new EmailManager();
		
		// KGG Adding try catch on sending email.  This is a quick patch to fix a customer problem that is escalating.  This will need to be modified
		// at a later date when we have a proper business process defined.
		try {
			if (consumerIds.size() > 1) {
				m.sendMultipleAcceptDeliveryEmail(receipt,depotUser);
			}
			else {					
				m.sendAcceptDeliveryEmail(receipt, depotUser);
			}
		} catch (Exception ex) {
			System.out.println (String.format("DeliveryManager.java - Failed to send Delivery Email. Transaction ID:%s due to %s",receipt.getTransactionId(),ex.getMessage())) ;
		}
	}
	
	public static void  sendSMSDeliveryNotification (User recipient, PackageReceipt receipt) throws Exception {
		
		if (recipient.getNotificationSummary().isDeliveryTextSupported()) {
				
			// KGG Adding try catch on sending SMS.  This is a quick patch to fix a customer problem that is escalating.  This will need to be modified
			// at a later date when we have a proper business process defined.
			try {
				SMSManager sms = new SMSManager();
				sms.sendParcelReceiptSMS(
						recipient, 
						receipt.getKinekPoint().getName(), 
						receipt.getKinekPoint().getAddress1(), 
						receipt.getKinekPoint().getPhone(),
						receipt.getPackages().size());					
			} catch (Exception ex) {
				System.out.println (String.format("DeliveryManager.java - Failed to sendParcelReceiptSMS. Transaction ID:%s recipient %s, due to %s",receipt.getTransactionId(),recipient.getKinekNumber(), ex.getMessage())) ;
			}
		}
			
		if (recipient.getNotificationSummary().isDeliveryPushSupported()) {
				
				// KGG Adding try catch on sending push.  This is a quick patch to fix a customer problem that is escalating.  This will need to be modified
				// at a later date when we have a proper business process defined.
			try {
				NotificationManager.sendDeliveryPushNotification(recipient,receipt);
			} catch (Exception ex) {
				System.out.println (String.format("DeliveryManager.java - Failed to sendDeliveryPushNotification. Transaction ID:%s recipient %s, due to %s ",receipt.getTransactionId(),recipient.getKinekNumber(), ex.getMessage())) ;
			}
		}
	}
	
	/**
	 * 
	 * Accepts delivery for one or more packages.  This process does allow the same package to be received for more than 
	 * one user when unique user identification is not possible by examining the package (ie. package label is missing).  In this
	 * case, the package is flagged to require proof of purchase during the pickup.
	 * @param kinekPointId the unique identifier of the kinekpoint to fetch weight groups for
	 * @return a String representing a unique transaction id
	 * @throws Exception 
	 */
	public static String acceptDelivery(User depotUser, KinekPoint receivingKinekPoint, List<String> consumerIds, Set<Package> packages) throws Exception{
		
		// TODO   This code is the same as above.  Refactor this.
		
		//Hashtable<String, String> info = new Hashtable<String, String>();
		Calendar now = Calendar.getInstance();
		PackageReceipt receipt = new PackageReceipt();
		receipt.setUserId(depotUser.getUserId());
		receipt.setApp(depotUser.getApp().toString());
		receipt.setKinekPoint(receivingKinekPoint);
		receipt.setTransactionId(generateTransactionId(receivingKinekPoint.getDepotId(), now));
		
		Set<User> packageRecipients = new HashSet<User>();
		for (String selectedConsumer : consumerIds) {
			User recipient = new UserDao().readConsumer(selectedConsumer);
			packageRecipients.add(recipient);
		}
		
		receipt.setPackageRecipients(packageRecipients);
		receipt.setReceivedDate(now.getTime());
		receipt.setLastEmailDate(now.getTime());

		if (consumerIds.size() > 1) {
			receipt.setRequiresProofOfPurchase(true);
		}else {
			receipt.setRequiresProofOfPurchase(false);
		}
		receipt.setPackages(packages);
		
		//This is where we actually create the package receipt record
		PackageReceiptDao receiptDao = new PackageReceiptDao();
		receiptDao.create(receipt);
		
		EmailManager m = new EmailManager();
		
		// KGG Adding try catch on sending email.  This is a quick patch to fix a customer problem that is escalating.  This will need to be modified
		// at a later date when we have a proper business process defined.
		try {
			if (consumerIds.size() > 1) {
				
				m.sendMultipleAcceptDeliveryEmail(receipt, depotUser);
			}
			else {					
				m.sendAcceptDeliveryEmail(receipt, depotUser);
			}
		} catch (Exception ex) {
			System.out.println (String.format("DeliveryManager.java - Failed to send Delivery Email. Transaction ID:%s due to %s",receipt.getTransactionId(),ex.getMessage())) ;
		}
		
		for (User packageRecipient : packageRecipients) {
			if (packageRecipient.getNotificationSummary().isDeliveryTextSupported()) {
				
				// KGG Adding try catch on sending SMS.  This is a quick patch to fix a customer problem that is escalating.  This will need to be modified
				// at a later date when we have a proper business process defined.
				try {
					SMSManager sms = new SMSManager();
					sms.sendParcelReceiptSMS(
							packageRecipient, 
							receipt.getKinekPoint().getName(), 
							receipt.getKinekPoint().getAddress1(), 
							receipt.getKinekPoint().getPhone(),
							receipt.getPackages().size());					
				} catch (Exception ex) {
					System.out.println (String.format("DeliveryManager.java - Failed to sendParcelReceiptSMS. Transaction ID:%s recipient %s, due to %s",receipt.getTransactionId(),packageRecipient.getKinekNumber(), ex.getMessage())) ;
				}
			}
			
			if (packageRecipient.getNotificationSummary().isDeliveryPushSupported()) {
				
				// KGG Adding try catch on sending push.  This is a quick patch to fix a customer problem that is escalating.  This will need to be modified
				// at a later date when we have a proper business process defined.
				try {
					NotificationManager.sendDeliveryPushNotification(packageRecipient,	receipt);
				} catch (Exception ex) {
					System.out.println (String.format("DeliveryManager.java - Failed to sendDeliveryPushNotification. Transaction ID:%s recipient %s, due to %s ",receipt.getTransactionId(),packageRecipient.getKinekNumber(), ex.getMessage())) ;
				}
			}
		}
	
		// info.clear();
		// info.put("TransactionID", receipt.getTransactionId());
		// logger.info("Accept Delivery - emails sent, delivery accepted", info);
		
		// KGG I suspect we have a memory leak in the logger.  Commenting this out until I can verify.
		logger.info("Accept Delivery - emails sent, delivery accepted");
		System.out.println (String.format("DeliveryManager.java - Transaction ID: %s",receipt.getTransactionId())) ;
		return receipt.getTransactionId();
	}
	
	public static BigDecimal getPackagePickupFee(KinekPoint kp, int packageWeightId) throws Exception{
		List<KPPackageRate> kpRates = new KPPackageRateDao().fetch(kp);
		BigDecimal fee = new BigDecimal(0.0);
		for (KPPackageRate kpRate : kpRates){
			kpRate.populateActualFee();
			if(kpRate.getUnifiedPackageRate().getPackageWeightGroup().getId() == packageWeightId){
				fee = kpRate.getActualFee();
				break;
			}
		}
		return fee;
	}
	
	public static BigDecimal getPackageSkidFee(KinekPoint kp, boolean isOnSkid) throws Exception{
		List<KPSkidRate> kpSkidRates = new KPSkidRateDao().fetch(kp);
		BigDecimal fee = new BigDecimal(0.0);
		if(isOnSkid && kpSkidRates.size() > 0){
			fee = kpSkidRates.get(0).getActualFee();
		}
		return fee;
	}
	
	private static String generateTransactionId(int kinekPointId, Calendar current){
		String timePortion = String.valueOf(current.get(Calendar.MONTH) + 1) //this is because Jan is returned as 0
							+ String.valueOf(current.get(Calendar.DAY_OF_MONTH))
							+ String.valueOf(current.get(Calendar.HOUR_OF_DAY))
							+ String.valueOf(current.get(Calendar.MINUTE))
							+ String.valueOf(current.get(Calendar.SECOND));
		
		String kinekPointIdPortion = String.format("%05d", kinekPointId);
		
		return kinekPointIdPortion+"-"+timePortion;
	}
}
