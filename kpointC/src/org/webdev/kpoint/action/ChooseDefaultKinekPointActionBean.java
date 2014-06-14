package org.webdev.kpoint.action;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.manager.EmailManager;
import org.webdev.kpoint.bl.persistence.ConsumerCreditDao;
import org.webdev.kpoint.bl.persistence.CountryDao;
import org.webdev.kpoint.bl.persistence.CreditIssueReasonDao;
import org.webdev.kpoint.bl.persistence.CreditStatusDao;
import org.webdev.kpoint.bl.persistence.KinekPointCreditDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.PromotionDao;
import org.webdev.kpoint.bl.persistence.StateDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.ConsumerCredit;
import org.webdev.kpoint.bl.pojo.Country;
import org.webdev.kpoint.bl.pojo.CreditCard;
import org.webdev.kpoint.bl.pojo.Feature;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.KinekPointCredit;
import org.webdev.kpoint.bl.pojo.Language;
import org.webdev.kpoint.bl.pojo.OperatingHours;
import org.webdev.kpoint.bl.pojo.PayMethod;
import org.webdev.kpoint.bl.pojo.Promotion;
import org.webdev.kpoint.bl.pojo.State;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.pojo.comparator.DistanceComparator;
import org.webdev.kpoint.bl.util.ApplicationProperty;
import org.webdev.kpoint.bl.util.GeoCalculator;
import org.webdev.kpoint.bl.util.GeoCalculator.NumberSystem;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/ChooseDefaultKinekPoint.action")
public class ChooseDefaultKinekPointActionBean extends AccountDashboardActionBean {
	private String criteria;
    private String displayCriteria;
	private String province;	
	private String city;
	private String postalCode;
	private int borderProvinceId;
	private double radius; 
	private double cityRadius; 
	private double postalRadius; 
	private double maxLongitude;
    private double minLongitude;
    private double maxLatitude;
    private double minLatitude;
    private double searchPointLatitude;
    private double searchPointLongitude;    
    private int depotId;
	private String userProfileAddress;
	private boolean isSelectedKpFromMapList = false;
	private boolean showDirectionsTab = false;
	private boolean returnFromResults = false;
	
    @DefaultHandler
    public Resolution view() throws Exception {
    	//Ajax call
    	if (!isSelectedKpFromMapList) {
    		if (maxLongitude != 0 || minLongitude != 0 || maxLatitude != 0 || minLatitude != 0) {
    			return findDepots();
    		}
    		//Specific KP
    		else if (depotId != 0) {
    			KinekPoint depot = new KinekPointDao().read(depotId);
    			if (depot != null) {
    				radius = 5;
    				criteria = depot.getGeolat() + ", " + depot.getGeolong();
    				displayCriteria = depot.getAddress1() + ", " + depot.getCity() + ", " + depot.getState().getName();
    				return search();
    			}
    		}
    	}
    	else if (isSelectedKpFromMapList) {
    		String json = getAdditionalInfoForSelectedKP(depotId);
    		
    		return new StreamingResolution("text", new StringReader(json));
    	}

    	return UrlManager.getChooseDefaultKinekPointSearch();
    }
    
    @ValidationMethod(on="search")
	public void validateSearch(ValidationErrors errors) throws Exception {
		radius = cityRadius; 
		
		//Postal code search
		if (postalCode != null) {
			criteria = postalCode;
			displayCriteria = criteria;
			radius = postalRadius;  
		} 
		
		//Province / City search
		else if (province != null && city != null) {
			criteria = city + ", " + province;
			displayCriteria = criteria;
		} 
		
		//Border Search
		else if (borderProvinceId != 0) {
			State state = new StateDao().read(borderProvinceId);
			criteria = state.getLatitude() + ", " + state.getLongitude();
    		displayCriteria = state.getName() + ", " + state.getCountry().getName();    		
		} 
		
		//Province, no city
		else if (province != null && city == null) {
			errors.add("city", new SimpleError("Please type in the name of a City to continue"));
		} 
		
		//City, no province
		else if (province == null && city != null) {
			errors.add("postalCode", new SimpleError("Please enter State/Province"));
		} 
		
		//Nothing entered
		else {
			errors.add("postalCode", new SimpleError("Please enter State and City or Zip code"));
		}
	}

    public Resolution search() throws UnsupportedEncodingException {
    	depotId = getActiveUser().getDepot().getDepotId();
        return UrlManager.getChooseDefaultKinekPointResults();
    }
    
    public Resolution showDirections() {
    	radius = cityRadius = 10;
    	province = getActiveUser().getState().getName();
    	city = getActiveUser().getCity();
		criteria = city + ", " + province;
		displayCriteria = criteria;
    	showDirectionsTab = true;
    	depotId = getActiveUser().getDepot().getDepotId();
		
        return UrlManager.getChooseDefaultKinekPointResults();
    }
    
    public Resolution findDepots() throws Exception {
		boolean border = borderProvinceId != 0;
		double maxLat = maxLatitude > minLatitude ? maxLatitude : minLatitude;
    	double minLat = maxLatitude > minLatitude ? minLatitude : maxLatitude;
    	double maxLong = maxLongitude > minLongitude ? maxLongitude : minLongitude;
    	double minLong = maxLongitude > minLongitude ? minLongitude : maxLongitude;

		List<KinekPoint> depots = new KinekPointDao().fetch(maxLat, minLat, maxLong, minLong, border);
	    Collections.sort(depots, new DistanceComparator<KinekPoint>(searchPointLatitude, searchPointLongitude, NumberSystem.IMPERIAL));

    	String json = buildJSON(depots);
    	return new StreamingResolution("text", new StringReader(json));
    }
    
    public Resolution setDefault() throws Exception {
    	if(depotId != 0) {
			User activeUser = getActiveUser();
									
			//send final sign up email only once at first time (when depot id from db is 1)
			//insert valid promotion only once at first time (when depot id from db is 1)
			UserDao userDao = new UserDao();
			int dbDepotId = userDao.read(activeUser.getUserId()).getDepot().getDepotId();
			if (dbDepotId == 1) {
				String promoCode = activeUser.getRegistrationPromoCode();
				//promoCode can be empty because it is not required data field on the registration page
				if (promoCode != null && !promoCode.trim().equals("")){
					processConsumerCredit(promoCode,activeUser);
				}
				
				KinekPoint selectedDepot = new KinekPointDao().read(depotId);
				activeUser.setKinekPoint(selectedDepot);
				
				EmailManager emailManager = new EmailManager();
				emailManager.sendFinalSignupEmail(activeUser);
				
				//update only once (At the first time new user does not have any kp so the first one will be default one during registration)
				userDao.update(activeUser); 
			}
			
			RedirectResolution resolution = new RedirectResolution(MyKinekPointsActionBean.class);
			resolution.addParameter("actionType", "addNewKP");
			resolution.addParameter("depotId", depotId);
			
			return resolution;	
    	}
    	return UrlManager.getChooseDefaultKinekPointResults();
	}
    
	public String buildJSON(List<KinekPoint> depots) {
    	StringBuilder json = new StringBuilder();    
    	
    	//Add depots
    	json.append("{ \"depots\": [");
    	boolean addComma = false;
    	for (KinekPoint depot : depots) {
    		if (addComma) {
    			json.append(",");
    		}
    		json.append(buildDepotJSON(depot));
    		addComma = true;
    	}
    	json.append("]}");	
    	
    	return json.toString();
    }
    
    private String buildDepotJSON(KinekPoint depot) {
    	GeoCalculator cal = new GeoCalculator(NumberSystem.IMPERIAL);
    	StringBuilder json = new StringBuilder();
    	
    	json.append(" { ");
		json.append(" \"depotId\" : " + depot.getDepotId() + ",");
		json.append(" \"name\" : \"" + cleanString(depot.getName()) + "\",");
		json.append(" \"address1\" : \"" + depot.getAddress1() + "\",");
		json.append(" \"address2\" : \"" + depot.getAddress2() + "\",");
		json.append(" \"city\" : \"" + depot.getCity() + "\",");
		json.append(" \"state\" : \"" + depot.getState().getName() + "\",");
		json.append(" \"geolat\" : " + depot.getGeolat() + ",");
		json.append(" \"geolong\" : " + depot.getGeolong() + ",");
		
		double distance = cal.getDistance(this.searchPointLatitude, this.searchPointLongitude, depot.getGeolat(), depot.getGeolong());
		json.append(" \"distance\" : " + distance);
		json.append(" } ");
		
		return json.toString().replace("\n", "").replace("\r", "").replace("\t", " ");
    }
    
    public String getAdditionalInfoForSelectedKP(int depotId) throws Exception {
    	
    	//fetch depot by depotId
    	KinekPointDao depotDao=new KinekPointDao();
    	KinekPoint depot=depotDao.read(depotId);
    	
    	StringBuilder json = new StringBuilder();
    	
    	json.append(" { ");
    	json.append(" \"state\" : \"" + depot.getState().getName() + "\",");
		json.append(" \"zip\" : \"" + depot.getZip() + "\",");
		json.append(" \"phone\" : \"" + depot.getPhone() + "\",");
		json.append(" \"email\" : \"" + depot.getEmail() + "\",");
		json.append(" \"extraInfo\" : \"" + depot.getExtraInfo() + "\",");
		json.append(" \"dutyAndTax\" : \"" + ( depot.getAcceptsDutyAndTax() ? "Accepts" : "Does not accept" ) + "\",");    		
		json.append(" \"receivingFee\" : \"" + "test" + "\",");
		json.append(" \"sizeAllowance\" : \"" + "test" + "\",");

		OperatingHours hours = depot.getOperatingHours();
		json.append(" \"operatingHours\" : {");
    		json.append(" \"sundayStart\" : \"" + hours.getSundayStart() + "\",");
    		json.append(" \"sundayEnd\" : \"" + hours.getSundayEnd() + "\",");
    		json.append(" \"mondayStart\" : \"" + hours.getMondayStart() + "\",");
    		json.append(" \"mondayEnd\" : \"" + hours.getMondayEnd() + "\",");
    		json.append(" \"tuesdayStart\" : \"" + hours.getTuesdayStart() + "\",");
    		json.append(" \"tuesdayEnd\" : \"" + hours.getTuesdayEnd() + "\",");
    		json.append(" \"wednesdayStart\" : \"" + hours.getWednesdayStart() + "\",");
    		json.append(" \"wednesdayEnd\" : \"" + hours.getWednesdayEnd() + "\",");
    		json.append(" \"thursdayStart\" : \"" + hours.getThursdayStart() + "\",");
    		json.append(" \"thursdayEnd\" : \"" + hours.getThursdayEnd() + "\",");
    		json.append(" \"fridayStart\" : \"" + hours.getFridayStart() + "\",");
    		json.append(" \"fridayEnd\" : \"" + hours.getFridayEnd() + "\",");
    		json.append(" \"saturdayStart\" : \"" + hours.getSaturdayStart() + "\",");
    		json.append(" \"saturdayEnd\" : \"" + hours.getSaturdayEnd() + "\",");
    		json.append(" \"closedSunday\" : \"" + hours.getClosedSunday() + "\",");
    		json.append(" \"closedMonday\" : \"" + hours.getClosedMonday() + "\",");
    		json.append(" \"closedTuesday\" : \"" + hours.getClosedTuesday() + "\",");
    		json.append(" \"closedWednesday\" : \"" + hours.getClosedWednesday() + "\",");
    		json.append(" \"closedThursday\" : \"" + hours.getClosedThursday() + "\",");
    		json.append(" \"closedFriday\" : \"" + hours.getClosedFriday() + "\",");
    		json.append(" \"closedSaturday\" : \"" + hours.getClosedSaturday() + "\",");
    		json.append(" \"hoursInfo\" : \"" + hours.getHoursInfo() + "\"");
		json.append("},");
		
		json.append(" \"payMethods\" : [");
		for(PayMethod method : depot.getPayMethod()) {
			json.append("\""+method.getName()+"\",");
		}
		json.append("],");
		
		json.append(" \"languages\" : [");
		for(Language lang : depot.getLanguages()) {
			json.append("\""+lang.getName()+"\",");
		}
		json.append("],");

		json.append(" \"features\" : [");
		for(Feature feat : depot.getFeatures()) {
			json.append("\""+feat.getName()+"\",");
		}
		json.append("],");
		
		json.append(" \"creditCards\" : [");
		for(CreditCard card : depot.getCards()) {
			json.append("\""+card.getName()+"\",");
		}
		json.append("]");			
		json.append(" } ");
			   
		return json.toString().replace("\n", "").replace("\r", "").replace("\t", " ").replace(",]", "]");
    }    
    
    //Valdiate promo code and insert the valid promotion into the consumer credit table by calling applyPromo method.
    private void processConsumerCredit(String code, User user) throws Exception {
    	boolean isValidPromo=true;
    	
		Calendar today = Calendar.getInstance();
		today.add(Calendar.HOUR, -today.get(Calendar.HOUR));
		today.clear(Calendar.MINUTE);
		today.clear(Calendar.SECOND);
		today.clear(Calendar.MILLISECOND);
		
		Promotion promotion = new PromotionDao().read(code);		
		List<ConsumerCredit> consumerCredits = new ConsumerCreditDao().fetchByPromotion(promotion.getId());
				
		if (promotion.getStartDate().after(today.getTime()) || promotion.getEndDate().before(today.getTime())) {			
			isValidPromo=false;
		}
		else if (consumerCredits.size() >= promotion.getAvailabilityCount()) {			
			isValidPromo=false;
		}
		else if (promotion.getState() != null && promotion.getState().getStateId() != user.getState().getStateId()) {			
			isValidPromo=false;
		}
		else if (promotion.getAssociation() != null) {			
			isValidPromo=false;
		}
		    	
		if(isValidPromo){
			applyPromo(promotion);
		}
    }

    //create comsumerCredit object and insert the valid promotion into the consumerCredit object
    private void applyPromo(Promotion promotion) throws Exception {
		ConsumerCredit consumerCredit = new ConsumerCredit();
		consumerCredit.setUser(getActiveUser());
		consumerCredit.setPromotion(promotion);
		consumerCredit.setIssueDate(new Date());
		consumerCredit.setIssueReason(new CreditIssueReasonDao().read(ApplicationProperty.getInstance().getProperty("credit.issue.reason.registration")));
		consumerCredit.setCreditStatus(new CreditStatusDao().read(ApplicationProperty.getInstance().getProperty("credit.status.available")));
		new ConsumerCreditDao().create(consumerCredit);
		
		if (promotion.getDepot() != null) {
			KinekPointCredit depotCredit = new KinekPointCredit();
			depotCredit.setDepot(promotion.getDepot());
			depotCredit.setPromotion(promotion);
			depotCredit.setIssueDate(new Date());
			depotCredit.setIssueReason(new CreditIssueReasonDao().read(ApplicationProperty.getInstance().getProperty("credit.issue.reason.registration")));
			depotCredit.setCreditStatus(new CreditStatusDao().read(ApplicationProperty.getInstance().getProperty("credit.status.available")));
			new KinekPointCreditDao().create(depotCredit);
		}    	
    }
    
    private String cleanString(String str) {
    	if(str != null) {
    		return str.replace("\"", "\\\"");
    	}
    	else {
    		return "";
    	}
    }
    
	@Override
	public boolean getHideSearch() {
		return true;
	}
	
	public String getSelectedCountryName() {
		return getActiveUser().getState().getCountry().getName();
	}
	
	public int getDepotId() {
		return depotId;
	}
	
	public void setDepotId(int depotId) {
		this.depotId = depotId;
	}

	public String getCriteria() {
		return criteria.replaceAll("'", "\\\\'");
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getDisplayCriteria() {
		return displayCriteria;
	}

	public void setDisplayCriteria(String displayCriteria) {
		this.displayCriteria = displayCriteria;
	}

    public double getMaxLongitude() {
		return maxLongitude;
	}

	public void setMaxLongitude(double maxLongitude) {
		this.maxLongitude = maxLongitude;
	}

	public double getMinLongitude() {
		return minLongitude;
	}

	public void setMinLongitude(double minLongitude) {
		this.minLongitude = minLongitude;
	}

	public double getMaxLatitude() {
		return maxLatitude;
	}

	public void setMaxLatitude(double maxLatitude) {
		this.maxLatitude = maxLatitude;
	}

	public double getMinLatitude() {
		return minLatitude;
	}

	public void setMinLatitude(double minLatitude) {
		this.minLatitude = minLatitude;
	}

	public double getSearchPointLatitude() {
		return searchPointLatitude;
	}

	public void setSearchPointLatitude(double searchPointLatitude) {
		this.searchPointLatitude = searchPointLatitude;
	}

	public double getSearchPointLongitude() {
		return searchPointLongitude;
	}

	public void setSearchPointLongitude(double searchPointLongitude) {
		this.searchPointLongitude = searchPointLongitude;
	}
	
	public List<Country> getCountries() throws Exception {
		return new CountryDao().fetch();
	}

	public List<State> getStates() throws Exception {		
		return new StateDao().fetch(new CountryDao().read(1));	
	}
	
	public List<State> getProvinces() throws Exception {
		return new StateDao().fetch(new CountryDao().read(2));
	}
	
	public List<State> getBorderProvinces() throws Exception {
		return new StateDao().fetchBorderStates(new CountryDao().read(2));
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	public String getRadiusStr() {
		return String.valueOf(Math.round(radius));
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public int getBorderProvinceId() {
		return borderProvinceId;
	}

	public void setBorderProvinceId(int borderProvinceId) {
		this.borderProvinceId = borderProvinceId;
	}

	public double getCityRadius() {
		return cityRadius;
	}

	public void setCityRadius(double cityRadius) {
		this.cityRadius = cityRadius;
	}

	public double getPostalRadius() {
		return postalRadius;
	}

	public void setPostalRadius(double postalRadius) {
		this.postalRadius = postalRadius;
	}
	
	public void setUserProfileAddress(String userProfileAddress){
		this.userProfileAddress = userProfileAddress;
	}	
	
	public String getUserProfileAddress(){
		if (userProfileAddress != null) {
			return userProfileAddress;
		}
		else {
			userProfileAddress = "";
			
			if(getActiveUser().getAddress1() != null && !getActiveUser().getAddress1().trim().equals(""))
				userProfileAddress += getActiveUser().getAddress1() + ", ";
			
			if(getActiveUser().getAddress2() != null && !getActiveUser().getAddress2().trim().equals(""))
				userProfileAddress += getActiveUser().getAddress2() + ", ";
			
			if(getActiveUser().getCity() != null && !getActiveUser().getCity().trim().equals(""))
				userProfileAddress += getActiveUser().getCity()+", ";
			
			if(getActiveUser().getZip() != null && !getActiveUser().getZip().trim().equals("")) {
				String zip = getActiveUser().getZip();
				if(zip.length() == 6) {
					zip = zip.substring(0, 3) + " " + zip.substring(3);
				}
				userProfileAddress += zip + ", ";
			}
			
			if(getActiveUser().getState() != null) {
				if(getActiveUser().getState().getName() != null && !getActiveUser().getState().getName().trim().equals(""))
					userProfileAddress += getActiveUser().getState().getName() + ", ";
				
				if(getActiveUser().getState().getCountry() != null && getActiveUser().getState().getCountry().getName() != null && !getActiveUser().getState().getCountry().getName().trim().equals(""))
					userProfileAddress += getActiveUser().getState().getCountry().getName();
			}
			
			// sanitize
			userProfileAddress = userProfileAddress.replaceAll("'", "\\\\'");
			
			return userProfileAddress.trim();
		}
	}
	
	public String getUserProfileCity() {
		return getActiveUser().getCity();
	}
	
	public boolean getIsSelectedKpFromMapList() {
		return isSelectedKpFromMapList;
	}

	public void setIsSelectedKpFromMapList(boolean isSelectedKpFromMapList) {
		this.isSelectedKpFromMapList = isSelectedKpFromMapList;
	}
	
	public boolean getShowDirectionsTab() {
		return showDirectionsTab;
	}

	public void setShowDirectionsTab(boolean showDirectionsTab) {
		this.showDirectionsTab = showDirectionsTab;
	}
	
	public String getdirectionsDestination() {
		KinekPoint kp = getActiveUser().getKinekPoint();
		
		return kp.getAddress1() + ", " + kp.getCity() + ", " + kp.getState().getName();
	}
	
	public boolean getReturnFromResults() {
		return returnFromResults;
	}

	public void setReturnFromResults(boolean returnFromResults) {
		this.returnFromResults = returnFromResults;
	}
}
