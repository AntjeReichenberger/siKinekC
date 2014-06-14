package org.webdev.kpoint.action;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.managers.MessageManager;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/MyKinekPoints.action")
public class MyKinekPointsActionBean extends AccountDashboardActionBean {
		
	private String actionType;
	private User user;	
	private int depotId;
	private String userAddress;
	private String userCity;
	
	private List<KinekPoint> kinekPoints = new ArrayList<KinekPoint>();
	
	@DefaultHandler @DontValidate
	public Resolution view() throws Exception {
		
		if (actionType != null && actionType.equals("addNewKP")) {
			if (String.valueOf(depotId) != null && depotId != 1) {
				addNewKinekPoint(depotId);
			}
		}
		user = getActiveUser();	
		if (user.getState() != null) {
			userAddress = user.getAddress1() + ", " + user.getCity() + ", " + user.getState().getName() + ", " + user.getState().getCountry().getName();
		}
		else {
			userAddress = user.getAddress1() + ", " + user.getCity();
		}
		userCity = user.getCity();		
		
		UserDao userDao = new UserDao();
		kinekPoints = userDao.fetchUserKinekPoints(user.getUserId());
		Collections.sort(kinekPoints);
		return UrlManager.getMyKinekPoints();
	}
	
	public Resolution addNewKinekPoint(int depotId) throws Exception {
		//add row into the user_kinekpoint table
		UserDao userDao = new UserDao();
		User user = getActiveUser();				
		List<KinekPoint>favKinekPoints = new ArrayList<KinekPoint>(userDao.fetchUserKinekPoints(user.getUserId()));
		
		KinekPoint favKinekPoint = new KinekPointDao().read(depotId);
		boolean alreadyExists = false;
		
		for (KinekPoint kp: favKinekPoints){
			if(kp.getDepotId() == favKinekPoint.getDepotId()){
				alreadyExists = true;
			}	
		}
		
		if(alreadyExists){
			getContext().getValidationErrors().add("depotId", new SimpleError("KinekPoint is already added to your list of KinekPoints"));
		}
		else{
			favKinekPoints.add(favKinekPoint);
			//If not commented, newest KinekPoint becomes favorite immediately 
			user.setDepot(favKinekPoint);
			Set<KinekPoint> myKinekPoints = new HashSet<KinekPoint>(favKinekPoints);
			user.setKinekPoints(myKinekPoints);
			setSuccessMessage(MessageManager.getAddNewKPSuccess());
			userDao.update(user);
		}	
		return new RedirectResolution(MyKinekPointsActionBean.class); 
	}
	
	@ValidationMethod(on="removeKinekPoint")
	public void validateRemoveKinekPoint(ValidationErrors errors) throws Exception {
		//Constraints to delete kp
		// 1) check whether kp is default or not.
		//  	1.1) If there is only one kinekpoint then Default kp cannot be deleted.
		//		1.2) If there is more than one kinekpoin then user must select default kp then delete the kp
		UserDao userDao = new UserDao();
		User user = getActiveUser();				
		List<KinekPoint>favKinekPoints = new ArrayList<KinekPoint>(userDao.fetchUserKinekPoints(user.getUserId()));
		if(favKinekPoints.size() == 1){
			getContext().getValidationErrors().add("depotId", new SimpleError("Default KinekPoint cannot be deleted"));
		}else if(favKinekPoints.size() > 1 && user.getDepot().getDepotId() == depotId){
			errors.add("depotId",new SimpleError("You cannot remove your active KinekPoint. Please make another KinekPoint active before removing this one."));
		}
		 view();
	}
	
	public Resolution removeKinekPoint() throws Exception {	
				
		//Call KinekPointDao to remove the row from user_kinekpoint table by the depotId
		UserDao userDao = new UserDao();		
		//User user = getActiveUser();				
		List<KinekPoint>favKinekPoints = new ArrayList<KinekPoint>(userDao.fetchUserKinekPoints(user.getUserId()));
		//remove the selected depot from the kps object and call user object to update to delete the row from mapping table		
		for(int i=0; i<favKinekPoints.size(); i++){
			if(favKinekPoints.get(i).getDepotId() == depotId){
				favKinekPoints.remove(i);
				break;
			}			
		}
		user.setKinekPoints(new HashSet<KinekPoint>(favKinekPoints));		
		userDao.update(user);		
		setSuccessMessage(MessageManager.getRemoveKPSuccess());
		return new RedirectResolution(MyKinekPointsActionBean.class); 
	}
		
	@HandlesEvent("removeFavoriteKinekPoint")
	public Resolution removeFavoriteKinekPoint() throws Exception {
		//Constraints to delete kp
		// 1) check whether kp is default or not.
		//  	1.1) If there is only one kinekpoint then Default kp cannot be deleted.
		//		1.2) If there is more than one kinekpoin then user must select default kp then delete the kp
		
		UserDao userDao = new UserDao();
		User user = getActiveUser();
		
		Boolean success = true;
		
		List<KinekPoint>favKinekPoints = new ArrayList<KinekPoint>(userDao.fetchUserKinekPoints(user.getUserId()));
		if (favKinekPoints.size() == 1) {
			success = false;
		}
		else if (favKinekPoints.size() > 1 && user.getDepot().getDepotId() == depotId) {
			success = false;
		}
		
		if (success) {
			//remove the selected depot from the kps object and call user object to update to delete the row from mapping table		
			for (int i = 0; i < favKinekPoints.size(); i++) {
				if (favKinekPoints.get(i).getDepotId() == depotId) {
					favKinekPoints.remove(i);
					success = true;
					break;
				}
				else if (i == favKinekPoints.size() - 1) {
					success = false;
				}
			}
		}
		
		user.setKinekPoints(new HashSet<KinekPoint>(favKinekPoints));		
		userDao.update(user);		
		
		StringReader reader = new StringReader(success.toString());
		return new StreamingResolution("text/html", reader);
	}
	
	public Resolution setDefaultKinekPoint() throws Exception {	
						
		//fetch the kinekpoint object by depotId from kpdepot table
		KinekPointDao favKinekPointDao = new KinekPointDao();
		KinekPoint favKinekPoint = favKinekPointDao.read(depotId);		
		
		//set the kinekpoint object into the user object which holds default kp
		User user = getActiveUser();
		user.setDepot(favKinekPoint);
		UserDao userDao = new UserDao();
		userDao.update(user);
		this.setSuccessMessage(new SimpleMessage("Your favorite KinekPoint has been changed."));	
		return new RedirectResolution(MyKinekPointsActionBean.class);
	}
	
	public Resolution addNewKinekPoint() {
		return new RedirectResolution(ChooseDefaultKinekPointActionBean.class);
	}

	public void setDepotId(int depotId) {
		this.depotId = depotId;
	}
	
	public int getDepotId() {
		return depotId;
	}
	
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
	public String getActionType() {
		return actionType;
	}

	public List<KinekPoint> getKinekPoints() {			
		return kinekPoints;
	}
	
	public void setKinekPoints(List<KinekPoint> kinekPoints) {
		this.kinekPoints = kinekPoints;
	}

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getUserAddress() {
		return userAddress;
	}
	
	public String getUserCity() {
		return userCity;
	}
}
