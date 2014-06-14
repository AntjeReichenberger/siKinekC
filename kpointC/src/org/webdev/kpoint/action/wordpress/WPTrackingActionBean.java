package org.webdev.kpoint.action.wordpress;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.action.AuthenticationActionBean;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.tracking.PackageActivity;
import org.webdev.kpoint.bl.tracking.TrackedPackage;
import org.webdev.kpoint.bl.tracking.TrackingManager;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/WPTracking.action")
public class WPTrackingActionBean extends AuthenticationActionBean {

	private String trackingNumber;
	private Integer courier;
	private TrackedPackage activeTrackedPackage = null;
	
	private boolean isTrackingNumberEmpty = false;
	private boolean isIncorrectCourier = false;
	private boolean isInvalidTrackingNumber = false;
	private boolean isInvalidCorrectTrackingNumber = false;

	public Resolution SendErrorsToWP(){		
	
		return new Resolution(){
		
			public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
				// TODO Auto-generated method stub				
			
				if(trackingNumber == null){
					trackingNumber = "";
				}
				
				String url = UrlManager.getWordPressBaseUrl()+"/track-package?error=formvalidation&trackingNumber="+trackingNumber+"&courier=" + courier;
										
				if(isTrackingNumberEmpty){
					url += "&trackingNumberRequired=true";
				}	

				if(isInvalidTrackingNumber){
					url += "&invalidTrackingNumber=true";
				}
				if(isIncorrectCourier){
					url += "&incorrectCourier=true";
				}	
				
				if(isInvalidCorrectTrackingNumber){
					url += "&invalidCorrectTrackingNumber=true";
				}
				
				res.sendRedirect(url);
			}
		};
	}
	
	public Resolution getTrackedPackage(){
		if(trackingNumber == null || trackingNumber.isEmpty() || trackingNumber.equals("Enter Your Tracking Number")){
			isTrackingNumberEmpty = true;			
			return SendErrorsToWP();
		}

		if(!courier.equals(0) && !courier.equals(ExternalSettingsManager.getUSPSId()) && !courier.equals(ExternalSettingsManager.getUPSId()) && !courier.equals(ExternalSettingsManager.getFedexId()) && !courier.equals(ExternalSettingsManager.getCanadaPostId())){
			isIncorrectCourier = true;
			return SendErrorsToWP();
		}

		//Check whether tracking number is valid or invalid
		
		TrackedPackage testPackage = new TrackedPackage();
		try{
	    	testPackage = TrackingManager.getTrackingDetails(trackingNumber,courier);    	
	        if(!testPackage.getIsValidTrackingNumber()){        	       
				isInvalidTrackingNumber = true;
				return SendErrorsToWP();
			}
		}
		catch(Exception e){
			testPackage = new TrackedPackage();
		}
		
        //In this scenario, the tracking manager found a match for the type of tracking number but the tracking number has no data
        if(!testPackage.getIsAvailableInCourierSystem()){
        	isInvalidCorrectTrackingNumber = true;
        	return SendErrorsToWP();
        }
		
		//Redirect Resolution does not work because they think any url is relative path and inside the stripes environment.
		//return new RedirectResolution(UrlManager.getWordPressBaseUrl()+"/track-package").addParameter("error", "formvalidate").addParameter("invalidTrackingField", "Invalid tracking number");		
		return new Resolution(){
			@Override
			public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
				// TODO Auto-generated method stub				
				res.sendRedirect(UrlManager.getWordPressBaseUrl()+"/track-your-package-result?trackingNumber="+trackingNumber+"&courier="+courier);
			}
			
		};
	}
	
	public Resolution getTrackingPackageResult(){
		try{		
			if(trackingNumber != null){
				activeTrackedPackage = TrackingManager.getTrackingDetails(trackingNumber,courier);
			}
		}
		catch(Exception e){
			activeTrackedPackage = new TrackedPackage();
		}
		return new ForwardResolution("/WEB-INF/jsp/wordpress/wp_tracking_package_result.jsp");
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
			builder.append("\"stateProv\":\""+pkgActivity.getStateProv()+"\",");
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
	
	public void setTrackingNumber(String trackingNumber){
		this.trackingNumber = trackingNumber;
	}
	
	public String getTrackingNumber(){
		return trackingNumber;
	}
	
	public TrackedPackage getActiveTrackedPackage() {	
		return activeTrackedPackage;
	}
	
	public void setCourier(Integer courier) {
		this.courier = courier;
	}
	
	public Integer getCourier() {
		return courier;
	}
}
