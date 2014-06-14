package org.webdev.kpoint.action;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.persistence.CourierDao;
import org.webdev.kpoint.bl.persistence.TrackingDao;
import org.webdev.kpoint.bl.persistence.UserTrackingDao;
import org.webdev.kpoint.bl.pojo.Courier;
import org.webdev.kpoint.bl.pojo.Tracking;
import org.webdev.kpoint.bl.pojo.UserTracking;
import org.webdev.kpoint.bl.tracking.PackageActivity;
import org.webdev.kpoint.bl.tracking.TrackedPackage;
import org.webdev.kpoint.bl.tracking.TrackingManager;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/Tracking.action")
public class TrackingActionBean extends AccountDashboardActionBean {

	@Validate(required=true, on="save")
	private String packageNickname;
	@Validate(required=true, on="save")
	private String trackingNumber;
	
	private Integer trackingId;
	
	private Integer courier;
	
	private List<UserTracking> userTrackings = null;
	private List<Integer> selectedTrackingNumbers = new ArrayList<Integer>();
	private List<Courier> couriers = new CourierDao().fetchTrackableCouriers();
	
	private Integer activeTrackingId;
	private String activePackageNickname;
	private TrackedPackage activeTrackedPackage = null;
	private Action action;
	public static enum Action { View, Create, Delete };

	private boolean isAccepted;
	
	public TrackingActionBean() throws Exception {}
	
    public Resolution changeNickname() throws Exception {
    	UserTrackingDao userTrackingDao = new UserTrackingDao();
    	TrackingDao trackingDao = new TrackingDao();
 
    	UserTracking trackNumber = userTrackingDao.read(new TrackingDao().read(trackingId),getActiveUser());
    	
    	if(trackNumber != null){
	    	if(isAccepted){
	    		trackNumber.setPackageNickname(packageNickname);
	    		userTrackingDao.update(trackNumber);
	    	}
	    	else{
	    		packageNickname = trackNumber.getPackageNickname();;
	    	}
    	}    	
    	StringReader reader = new StringReader(trackingId + "," + packageNickname);
		return new StreamingResolution("text/html", reader);
    }
    
    public Resolution submit() {
    	TrackingDao trackingDao = new TrackingDao();
    	try{	
	    	if(activeTrackingId != null){
	    		Tracking trackedPackage = trackingDao.read(activeTrackingId);
	    		UserTracking userEntry = new UserTrackingDao().read(trackedPackage,getActiveUser());
				activeTrackedPackage = TrackingManager.getTrackingDetails(trackedPackage.getTrackingNumber(),trackedPackage.getCourier().getCourierId());
				activeTrackedPackage.setNickname(userEntry.getPackageNickname());
			}
    	}
    	catch(Exception e){
    		 activeTrackedPackage = new TrackedPackage();
    	}
    	return new ForwardResolution("/WEB-INF/jsp/trackpackage.jsp");
    }	
   
	@DefaultHandler
	public Resolution view() throws Exception {
		if(action == Action.Create)
			return UrlManager.getAddTrackingList();
		
		UserTrackingDao userTrackingDao = new UserTrackingDao();		
		userTrackings = userTrackingDao.fetch(getActiveUser());

		return UrlManager.getViewTrackingList();
	}

    @ValidationMethod(on="save")
    public void validateSave() throws UnsupportedEncodingException {        
    	TrackedPackage testPackage = null;
		try {
			testPackage = TrackingManager.getTrackingDetails(trackingNumber,courier);
			
	        if(!testPackage.getIsValidTrackingNumber()){
	        	getContext().getValidationErrors().add("trackingNumber", new SimpleError("Our auto-detection could not validate your tracking number. Please try again or choose a specific courier below."));	
	        }
	        else{
	        	activeTrackedPackage = testPackage;
	        }
		} catch (Exception e) {
			getContext().getValidationErrors().add("trackingNumber", new SimpleError("There is an error with this tracking number. Please try manually selecting the courier or try again later."));
		}
    }
	
	public Resolution save() throws Exception {
		UserTrackingDao userTrackingDao = new UserTrackingDao();
		TrackingDao trackingDao = new TrackingDao();
		
		Tracking tracking;
		if(courier == null || courier == 0){
			tracking = trackingDao.read(trackingNumber, TrackingManager.getCourier(trackingNumber));
		}
		else{
			tracking = trackingDao.read(trackingNumber, courier);
		}
		
		if(tracking == null){
			tracking = new Tracking();
			tracking.setTrackingNumber(trackingNumber);
			tracking.setCourier(activeTrackedPackage.getCourier());
		}
		else{
			List<UserTracking> userTrackings = userTrackingDao.fetch(getActiveUser());
			for(UserTracking userTrack : userTrackings){
				if(tracking.getTrackingNumber().equals(userTrack.getTracking().getTrackingNumber())){
					getContext().getValidationErrors().add("trackingNumber", new SimpleError("You already have this tracking number added to your list."));
					return UrlManager.getViewTrackingList();
				}
			}
		}
			
		UserTracking userTracking = new UserTracking();
		userTracking.setPackageNickname(packageNickname);
		userTracking.setUser(getActiveUser());
		
		if(courier != null && courier != 0){
			userTracking.setAutoSelected(false);
		}
		
		if(!activeTrackedPackage.getIsAvailableInCourierSystem()){
			if(userTracking.getAutoSelected()){
				this.setSuccessMessage(new SimpleMessage("Your tracking number was added but it currently does not have any information associated with it yet."));
			}
			else{
				this.setSuccessMessage(new SimpleMessage("Your tracking number was added but it currently does not have any information associated with it yet. Please verify that you have chosen the correct courier."));
			}
        }
		else if(!activeTrackedPackage.getHasMap()){
			this.setSuccessMessage(new SimpleMessage("Your tracking number was added but it currently does not have any mapping information associated with it yet."));	
		}
		else{
			tracking.setCurrentLocation(activeTrackedPackage.getCurrentLocation());
			if(activeTrackedPackage.getHasMap()) tracking.setCurrentStatus(activeTrackedPackage.getPackageHistory().get(0).getActivity());
			tracking.setEstimatedArrival(activeTrackedPackage.getArrivalDate());
			
			if(activeTrackedPackage.getWeight() != null && activeTrackedPackage.getWeightType() != null){
				tracking.setWeight(Float.parseFloat(activeTrackedPackage.getWeight()));
				tracking.setWeightType(activeTrackedPackage.getWeightType());
			}
			this.setSuccessMessage(new SimpleMessage("Tracking number successfully added to your tracking list."));
		}
		userTracking.setTracking(tracking);
		userTrackingDao.create(userTracking);
		
		return UrlManager.getViewTrackingList();
	}
	
	public Resolution cancel(){
		return UrlManager.getViewTrackingList();
	}
	
	//@HandlesEvent("addPackage")
	public Resolution addPackage() throws Exception {
		setCouriers(new CourierDao().fetchTrackableCouriers());
		return UrlManager.getAddTrackingList();
	}
	
	public Resolution deletePackage() throws Exception {
		UserTrackingDao userTrackingDao = new UserTrackingDao();
		if(selectedTrackingNumbers.size() != 0){
			for(Integer trackingId:selectedTrackingNumbers){
				Tracking relevantTracking = new TrackingDao().read(trackingId);
				UserTracking userTracking = userTrackingDao.read(relevantTracking,getActiveUser());
				userTracking.setIsActive(false);
				
				if(userTracking != null){
					userTrackingDao.update(userTracking);
				}
			}
		}
		return UrlManager.getViewTrackingList();
	}
	
	public String getPackageNickname() {
		return packageNickname;
	}

	public void setPackageNickname(String packageNickname) {
		this.packageNickname = packageNickname;
	}
	
	public TrackedPackage getActiveTrackedPackage() {	
		return activeTrackedPackage;
	}
	
	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
		
	public List<UserTracking> getUserTrackings() throws Exception {
		if(userTrackings == null){
			UserTrackingDao userTrackingDao = new UserTrackingDao();
			userTrackings = userTrackingDao.fetch(getActiveUser());
		}
		return userTrackings;
	}

	public void setUserTrackings(List<UserTracking> userTrackings) {
		this.userTrackings = userTrackings;
	}

	public void setSelectedTrackingNumbers(List<Integer> selectedTrackingNumbers) {
		this.selectedTrackingNumbers = selectedTrackingNumbers;
	}

	public List<Integer> getSelectedTrackingNumbers() {
		return selectedTrackingNumbers;
	}

	public void setAction(Action action){
		this.action = action;
	}
	
	public Action getAction(){
		return action;
	}
	
	public Integer getActiveTrackingId() {
		return activeTrackingId;
	}

	public void setActiveTrackingId(Integer activeTrackingId) {
		this.activeTrackingId = activeTrackingId;
	}
	
	public String getActivePackageNickname() {
		return activePackageNickname;
	}

	public void setActivePackageNickname(String activePackageNickname) {
		this.activePackageNickname = activePackageNickname;
	}
	
	public String getPackageActivitiesJSON(){
		List<PackageActivity> packageActivities =  activeTrackedPackage.getPackageHistory();		
		StringBuilder builder = new StringBuilder();
		
		builder.append("{");
		builder.append("activities:[");
		
		int commaPos = 0;
		
		for(PackageActivity pkgActivity:packageActivities){
			builder.append("{");
	
			builder.append("\"country\":\""+pkgActivity.getCountry()+"\",");
			builder.append("\"address1\":\""+pkgActivity.getCity()+"\",");
			if(pkgActivity.getStateProv() != null){
				builder.append("\"stateProv\":\""+pkgActivity.getStateProv()+"\",");
			}else{
				builder.append("\"stateProv\":\"null\",");
			}

			String minute = String.valueOf(pkgActivity.getDateTime().get(Calendar.MINUTE));
			if(minute.length() == 1){
				minute = "0" + minute;
			}
			
			int am_pmi = pkgActivity.getDateTime().get(Calendar.AM_PM);
			String am_pms = "PM";
			if(am_pmi == 0){
				am_pms = "AM";
			}
			
			builder.append("\"activity\":\""+pkgActivity.getActivity()+"\",");
			builder.append("\"year\":\""+pkgActivity.getDateTime().get(Calendar.YEAR)+"\",");
			builder.append("\"month\":\""+(pkgActivity.getDateTime().get(Calendar.MONTH)+1)+"\",");
			builder.append("\"day\":\""+pkgActivity.getDateTime().get(Calendar.DATE)+"\",");			
			builder.append("\"hour\":\""+pkgActivity.getDateTime().get(Calendar.HOUR)+"\",");
			builder.append("\"minute\":\""+minute+"\",");
			builder.append("\"second\":\""+pkgActivity.getDateTime().get(Calendar.SECOND)+"\",");
			builder.append("\"pm\":\""+am_pms+"\"");
			builder.append("}");
			
			if(commaPos<(packageActivities.size()-1)){
				builder.append(",");
				commaPos++;
			}
		}
		
		builder.append("]}");
				
		return builder.toString();
		
	}
	
	public boolean getIsAccepted() {
		return isAccepted;
	}

	public void setIsAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

	public void setCourier(Integer courier) {
		this.courier = courier;
	}

	public Integer getCourier() {
		return courier;
	}

	public void setTrackingId(Integer trackingId) {
		this.trackingId = trackingId;
	}

	public Integer getTrackingId() {
		return trackingId;
	}

	public void setCouriers(List<Courier> couriers) {
		this.couriers = couriers;
	}

	public List<Courier> getCouriers() {
		return couriers;
	}
}
