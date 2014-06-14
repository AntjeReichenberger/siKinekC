package com.kinek.worker.tube;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.webdev.kpoint.bl.manager.DeliveryManager;
import org.webdev.kpoint.bl.persistence.CourierDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.PackageWeightGroupDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Package;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.User;

import com.trendrr.beanstalk.BeanstalkClient;

public class QueueHandler {

	
	
	static public void ProcessPackage(BeanstalkClient client, JSONObject inputJsonObj, String emailTube, String smsTube) throws JSONException, Exception  {
		
		String userAPP = inputJsonObj.getString("app") ;				// app
		int userid = inputJsonObj.getInt("depotuserid") ;				// userid
		String version = inputJsonObj.getString("version") ;			// ignore version for now.
		String token = inputJsonObj.getString("token") ;
		int depotId = inputJsonObj.getInt("depotnumber") ;
		String kineknumber = inputJsonObj.getString("kineknumber") ;

		// Get the Depot.
		KinekPointDao kinekPointDao = new KinekPointDao();
		KinekPoint receivingKinekPoint = kinekPointDao.read(depotId) ;
				
		boolean skid = inputJsonObj.getBoolean("skid") ;
		BigDecimal taxes = new BigDecimal (inputJsonObj.getDouble("taxes")) ;
		int courier = inputJsonObj.getInt("courier") ;
		int weightId = inputJsonObj.getInt("weight") ;
		String shippedFrom = inputJsonObj.getString("shipped") ;
		String customInfo = inputJsonObj.getString("info") ;
		System.out.println ( inputJsonObj.toString()) ;
		
		Set<Package> packages = new HashSet<Package>();
		Package packageObj = new Package();
		
		packageObj.setCourier(new CourierDao().read(courier));

		packageObj.setDutyAndTax(taxes);
		packageObj.setCustomInfo(customInfo);
		packageObj.setShippedFrom(shippedFrom);
		packageObj.setPackageWeightGroup(new PackageWeightGroupDao().read(weightId));
		packageObj.setPickupFee(DeliveryManager.getPackagePickupFee(receivingKinekPoint, weightId));
		packageObj.setOnSkid(skid);
		packageObj.setSkidFee(DeliveryManager.getPackageSkidFee(receivingKinekPoint, skid));
		packages.add(packageObj);

		//CALL BL
		List<String> selectedConsumerList = new ArrayList<String>()  ;
		selectedConsumerList.add(kineknumber) ;
		
		User kpuser = new UserDao().read(userid) ;						// Authenticated user.
		kpuser.setApp(User.App.valueOf(userAPP)) ;						// Set the userapp.
		
		PackageReceipt receipt = DeliveryManager.acceptDeliveryWithoutNotification (kpuser, receivingKinekPoint, selectedConsumerList, packages);
		
		// Place receipt in email Queue
		client.useTube(emailTube) ;
		String emailJSON = "{ depotuserid: \"" + userid  +"\", packageid: \"" + receipt.getId() + "\", kineknumber: \"" + kineknumber + "\"}" ;
		client.put(1l, 0, 5000, emailJSON.getBytes());
		
		// Place receipt in SMS Queue
		client.useTube(smsTube) ;
		String smsJSON = "{ kineknumber: \"" + kineknumber +"\", packageid: \"" + receipt.getId() + "\"}" ;
		client.put(1l, 0, 5000, smsJSON.getBytes());


	}
}
