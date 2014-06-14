package org.webdev.kpoint.api.mobile.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.webdev.kpoint.api.mobile.util.WSApplicationError;
import org.webdev.kpoint.api.mobile.util.WSApplicationException;
import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.bl.manager.DeliveryManager;
import org.webdev.kpoint.bl.persistence.CourierDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.PackageDao;
import org.webdev.kpoint.bl.persistence.PackageReceiptDao;
import org.webdev.kpoint.bl.persistence.PackageWeightGroupDao;
import org.webdev.kpoint.bl.persistence.PickupDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Package;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.Pickup;
import org.webdev.kpoint.bl.pojo.User;

public class PackagesDomain extends Domain{
	private final static String PACKAGE_NOT_FOUND = "INVALID_PACKAGE_ID";
	private final static String INVALID_PACKAGE_STATUS = "INVALID_PACKAGE_STATUS";

	public final static String PICKED_UP_STATUS = "PICKED_UP";
	public final static String RECEIVED_STATUS = "RECEIVED";
	
	public static List<PackageReceipt> getPackages(int userId, String packageStatus, User authenticatedUser) throws Exception{
		authorizationCheck(userId, authenticatedUser, "User does not have access to the target user's Packages. Target User ID: " + userId);
		
		if(!isValidPackageStatus(packageStatus)){
			WSApplicationError err = new WSApplicationError(INVALID_PACKAGE_STATUS, "Packages could not be retrieved, package status is invalid: " + packageStatus, Response.Status.BAD_REQUEST);
			throw new WSApplicationException(err, err.getResponse());
		}
		
		List<PackageReceipt> packageReceipts = new PackageReceiptDao().fetch(userId);

		List<PackageReceipt> filteredPackageReceipts = new ArrayList<PackageReceipt>();
		for (PackageReceipt packageReceipt : packageReceipts) {
			PackageReceipt filteredPackageReceipt = new PackageReceipt();
			filteredPackageReceipt.setPackages(new HashSet<Package>());
			int packageCount = 0;
			for (Package packageObj : packageReceipt.getPackages()) {
				String statusName = packageObj.getPackageStatus(packageReceipt);
				if (includeInResults(statusName, packageStatus)) {
					filteredPackageReceipt.getPackages().add(packageObj);
					packageCount++;
				}
			}
			
			//only need to add the package receipt to the list of returned value if there is at least one package 
			if(packageCount > 0)
			{
				filteredPackageReceipt.setKinekPoint(packageReceipt.getKinekPoint());
				filteredPackageReceipt.setReceivedDate(packageReceipt.getReceivedDate());
				filteredPackageReceipt.setRedirectReason(packageReceipt.getRedirectReason());
							
				filteredPackageReceipts.add(filteredPackageReceipt);
			}
		}
		
		return filteredPackageReceipts;
	}
	
	public static List<PackageReceipt> getPackages(int userId, String packageStatus, int kinekPointId, User authenticatedUser) throws Exception{
		isKPAdminAuthorizationCheck(authenticatedUser, "User does not have access to the target user's Packages. Target User ID: " + userId);
		
		if(!isValidPackageStatus(packageStatus)){
			WSApplicationError err = new WSApplicationError(INVALID_PACKAGE_STATUS, "Packages could not be retrieved, package status is invalid: " + packageStatus, Response.Status.BAD_REQUEST);
			throw new WSApplicationException(err, err.getResponse());
		}
		
		List<PackageReceipt> packageReceipts = new PackageReceiptDao().fetch(userId);

		List<PackageReceipt> filteredPackageReceipts = new ArrayList<PackageReceipt>();
		for (PackageReceipt packageReceipt : packageReceipts) {
			if(packageReceipt.getKinekPoint().getDepotId() == kinekPointId)
			{
				packageReceipt.populateExtendedStorageFeesForPackages();
				PackageReceipt filteredPackageReceipt = new PackageReceipt();
				filteredPackageReceipt.setPackages(new HashSet<Package>());
				int packageCount = 0;
				for (Package packageObj : packageReceipt.getPackages()) {
					String statusName = packageObj.getPackageStatus(packageReceipt);
					if (includeInResults(statusName, packageStatus)) {
						filteredPackageReceipt.getPackages().add(packageObj);
						packageCount++;
					}
				}
			
				//only need to add the package receipt to the list of returned value if there is at least one package 
				if(packageCount > 0)
				{
					filteredPackageReceipt.setReceivedDate(packageReceipt.getReceivedDate());
					filteredPackageReceipt.setRequiresProofOfPurchase(packageReceipt.isRequiresProofOfPurchase());
					filteredPackageReceipts.add(filteredPackageReceipt);
				}
			}
		}
		
		return filteredPackageReceipts;
	}
	
	public static Pickup updatePackage(org.webdev.kpoint.bl.api.mapper.request.Package pkg, User authenticatedUser) throws Exception{
		if(!pkg.getStatus().equals(PICKED_UP_STATUS)){
			WSApplicationError err = new WSApplicationError(INVALID_PACKAGE_STATUS, "Packages can only be updated to status of PICKED_UP. Package status is invalid: " + pkg.getStatus(), Response.Status.BAD_REQUEST);
			throw new WSApplicationException(err, err.getResponse());
		}
				
		PackageDao packageDao = new PackageDao();
		PackageReceiptDao packageReceiptDao = new PackageReceiptDao();
		PickupDao pickupDao = new PickupDao();
		
		Package tempPackage = packageDao.read(pkg.getPackageId());
		if(tempPackage == null){
			WSApplicationError err = new WSApplicationError(PACKAGE_NOT_FOUND, "Package not found, package ID is invalid: " + pkg.getPackageId(), Response.Status.BAD_REQUEST);
			throw new WSApplicationException(err, err.getResponse());
		}
		
		PackageReceipt receipt = packageReceiptDao.read(tempPackage);
		authorizationCheck(receipt.getPackageRecipients(), authenticatedUser, "User does not have access to update the target Package. Target Package ID: " + pkg.getPackageId());
		
		
		Calendar current = new GregorianCalendar();
		Pickup pickup = new Pickup();
		pickup.setConsumer(authenticatedUser);
		pickup.setKinekPoint(receipt.getKinekPoint());
		pickup.setPackages(new HashSet<Package>());
		pickup.getPackages().add(tempPackage);
		pickup.setPickupDate(current.getTime());
		pickup.setTransactionId(generatePickupTransactionId(receipt.getKinekPoint().getDepotId(), authenticatedUser.getUserId(), current));
		pickup.setUserId(authenticatedUser.getUserId());
		pickup.setApp(authenticatedUser.getApp().toString());
		
		pickupDao.create(pickup);
			
		//Before returning the pickup object set the consumer and kinekpoint to NULL so their data is not returned (a lot of data that is not required)
		pickup.setConsumer(null);
		pickup.setKinekPoint(null);
		
		return pickup;	
	}
	
	public static String createPackageReceipt(org.webdev.kpoint.bl.api.mapper.request.ReceivePackage pkg, User authenticatedUser) throws Exception{
		kinekPointAuthorizationCheck(pkg.getReceivingKinekPointId(), authenticatedUser, "User does not have permission to receive packages at the target KinekPoint. Target KinekPoint ID: " + pkg.getReceivingKinekPointId());
		
		KinekPoint kp = new KinekPointDao().read(pkg.getReceivingKinekPointId());
		Set<Package> packages = new HashSet<Package>();
		for (int i = 0; i < pkg.getPackages().size(); i++) {
			org.webdev.kpoint.bl.api.mapper.request.Package corePkgReceiptData = pkg.getPackages().get(i);
			
			Package packageObj = new Package();
			packageObj.setCourier(new CourierDao().read(corePkgReceiptData.getCourierCode()));
			packageObj.setDutyAndTax(corePkgReceiptData.getDutyAndTax());
			packageObj.setCustomInfo(corePkgReceiptData.getCustomInfo());
			packageObj.setShippedFrom(corePkgReceiptData.getShippedFrom());
			packageObj.setPackageWeightGroup(new PackageWeightGroupDao().read(corePkgReceiptData.getWeightId()));
			packageObj.setPickupFee(DeliveryManager.getPackagePickupFee(kp, corePkgReceiptData.getWeightId()));
			packageObj.setOnSkid(corePkgReceiptData.isOnSkid());
			packageObj.setSkidFee(DeliveryManager.getPackageSkidFee(kp, corePkgReceiptData.isOnSkid()));
							
	    	packages.add(packageObj);
		}
		
		String transactionId = DeliveryManager.acceptDelivery(authenticatedUser, kp, pkg.getUserList(), packages);
		return transactionId;
	}
	
	public static String createPackagePickup(org.webdev.kpoint.bl.api.mapper.request.PickupPackage pickupRequest, User authenticatedUser) throws Exception{
		kinekPointAuthorizationCheck(pickupRequest.getReceivingKinekPointId(), authenticatedUser, "User does not have permission to pickup packages at the target KinekPoint. Target KinekPoint ID: " + pickupRequest.getReceivingKinekPointId());
		
		PackageDao packageDao = new PackageDao();
		PickupDao pickupDao = new PickupDao();
		
		Set<Package> packages = new HashSet<Package>();
		for (int i = 0; i < pickupRequest.getPackageIdList().size(); i++) {
			String packageId = pickupRequest.getPackageIdList().get(i);
			Package tempPackage = packageDao.read(Integer.valueOf(packageId));
			packages.add(tempPackage);
		}
		
		Calendar current = new GregorianCalendar();
		UserDao userDao = new UserDao();
		User pickupUser = userDao.read(pickupRequest.getUserId());
		KinekPoint kp = new KinekPointDao().read(pickupRequest.getReceivingKinekPointId());
		
		Pickup pickup = new Pickup();
		pickup.setConsumer(pickupUser);
		pickup.setKinekPoint(kp);
		pickup.setPackages(packages);
		pickup.setPickupDate(current.getTime());
		pickup.setTransactionId(generatePickupTransactionId(kp.getDepotId(), pickupUser.getUserId(), current));
		pickup.setUserId(authenticatedUser.getUserId());
		pickup.setApp(authenticatedUser.getApp().toString());
		
		pickupDao.create(pickup);
	
		return pickup.getTransactionId();
	}
	
	
	public static PackageReceipt getPackageReceipt(int userId, int receiptId, User authenticatedUser) throws Exception{
		isKPAdminAuthorizationCheck(authenticatedUser, "User does not have access to the target user's Packages. Target User ID: " + userId);
		
		PackageReceipt packageReceipt = new PackageReceiptDao().read(receiptId);

		//verify that the provided user is actually a recipient for the receipt
		boolean validUser = false;
		for (User recipient : packageReceipt.getPackageRecipients())
		{
			if(recipient.getUserId() == userId)
			{
				validUser = true;
				break;
			}
		}
		
		if(!validUser)
		{
			WSApplicationError err = new WSApplicationError(UNAUTHORIZED, "The provided user is not a valid package recipient.", Response.Status.UNAUTHORIZED);
			throw new WSApplicationException(err, err.getResponse());
		}
		
		return packageReceipt;
	}
	
	private static String generatePickupTransactionId(int kinekPointId, int consumerId, Calendar current) throws Exception{
		String timePortion = String.valueOf(current.get(Calendar.MONTH) + 1) //this is because Jan is returned as 0
							+ String.valueOf(current.get(Calendar.DAY_OF_MONTH))
							+ String.valueOf(current.get(Calendar.HOUR_OF_DAY))
							+ String.valueOf(current.get(Calendar.MINUTE))
							+ String.valueOf(current.get(Calendar.SECOND));
		
		String kinekPointIdPortion = String.format("%05d", kinekPointId);
		String consumerIdPortion = String.format("%05d", consumerId);
		
		String transactionId = consumerIdPortion + "-" + kinekPointIdPortion + "-" + timePortion;
		return transactionId;
	}
	
	private static boolean isValidPackageStatus(String packageStatus){
		if(packageStatus.equals(PICKED_UP_STATUS) || packageStatus.equals(RECEIVED_STATUS))
			return true;
			
		return false;
	}
	
	private static boolean includeInResults(String pkgStatus, String desiredStatus){
		String internalPkgStatus = "";
		if(desiredStatus.equals(RECEIVED_STATUS))
			internalPkgStatus = ConfigurationManager.getPackageStatusInDepot();
		else if(desiredStatus.equals(PICKED_UP_STATUS))
			internalPkgStatus = ConfigurationManager.getPackageStatusPickedUp();
		
		if(internalPkgStatus.equals(pkgStatus))
			return true;
		
		return false;
	}
	
}
