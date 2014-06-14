package org.webdev.kpoint.action.wordpress;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.action.AuthenticationActionBean;
import org.webdev.kpoint.bl.persistence.CountryDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.StateDao;
import org.webdev.kpoint.bl.pojo.Country;
import org.webdev.kpoint.bl.pojo.CreditCard;
import org.webdev.kpoint.bl.pojo.Feature;
import org.webdev.kpoint.bl.pojo.KPExtendedStorageRate;
import org.webdev.kpoint.bl.pojo.KPPackageRate;
import org.webdev.kpoint.bl.pojo.KPSkidRate;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Language;
import org.webdev.kpoint.bl.pojo.OperatingHours;
import org.webdev.kpoint.bl.pojo.PayMethod;
import org.webdev.kpoint.bl.pojo.State;
import org.webdev.kpoint.bl.pojo.StorageDuration;
import org.webdev.kpoint.bl.pojo.comparator.DistanceComparator;
import org.webdev.kpoint.bl.util.GeoCalculator;
import org.webdev.kpoint.bl.util.GeoCalculator.NumberSystem;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/WPFindKinekPoint.action")
public class WPFindKinekPointActionBean extends AuthenticationActionBean {
    private String criteria = "";
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
    private boolean isBorderSearch;
    private int depotId = 0;   
    private boolean isSelectedKpFromMapList=false;
    
    StringBuilder builder = null;
    
    @ValidationMethod(on="getSearchResult")
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
	}
    
    public Resolution search() throws UnsupportedEncodingException {    	
       	return new Resolution(){

			@Override
			public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
				// TODO Auto-generated method stub
				String url = UrlManager.getWordPressBaseUrl()+"/find-a-kinekpoint-result";
				if(postalCode != null && !postalCode.isEmpty()){
					String encodedPostalCode = URLEncoder.encode(postalCode,"UTF-8");
					url += "?postalCode="+encodedPostalCode+"&postalRadius="+postalRadius;
				}else if((city != null && !city.isEmpty()) && (province != null && !province.isEmpty())){
					String encodedCity = URLEncoder.encode(city,"UTF-8");
					String encodedProvince = URLEncoder.encode(province,"UTF-8");					
					url += "?city="+encodedCity+"&province="+encodedProvince+"&cityRadius="+cityRadius;					
				}else if(borderProvinceId != 0){
					url += "?borderProvinceId="+borderProvinceId;					
				}
				
				res.sendRedirect(url);
			}
       		
       	};
    }

    public Resolution getSearchResult() throws UnsupportedEncodingException {    	
       	return new ForwardResolution("/WEB-INF/jsp/wordpress/wp_findkp_result.jsp");
       		
    }    
    
	public Resolution getKpAdditionalDetails() throws Exception {
		String json = getAdditionalInfoForSelectedKP(depotId);
		return new StreamingResolution("text", new StringReader(json));
	}
    
 public String getAdditionalInfoForSelectedKP(int depotId) throws Exception {
    	
    	//fetch depot by depotId
    	KinekPointDao depotDao=new KinekPointDao();
    	KinekPoint depot=depotDao.read(depotId);
    	
    	StringBuilder json = new StringBuilder();
    	
    	json.append(" { ");
    	json.append(" \"state\" : \"" + cleanString(depot.getState().getName()) + "\",");
		json.append(" \"zip\" : \"" + cleanString(depot.getZip()) + "\",");
		json.append(" \"phone\" : \"" + cleanString(depot.getPhone()) + "\",");
		json.append(" \"email\" : \"" + cleanString(depot.getEmail()) + "\",");
		json.append(" \"extraInfo\" : \"" + cleanString(depot.getExtraInfo()) + "\",");
		json.append(" \"dutyAndTax\" : \"" + ( depot.getAcceptsDutyAndTax() ? "Accepts" : "Does not accept" ) + "\",");    		
		//TODO json.append(" \"receivingFee\" : \"" + depot.getReceivingFee() + "\",");
		//TODO json.append(" \"sizeAllowance\" : \"" + depot.getSizeAllowance().getFriendlyName() + "\",");

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
    		json.append(" \"hoursInfo\" : \"" + cleanString(hours.getHoursInfo()) + "\"");
		json.append("},");
		
		List<KPPackageRate> rates = new ArrayList<KPPackageRate>(depot.getKpPackageRates());
		json.append(" \"kpPackageRates\" : [");
			for(KPPackageRate rate : rates){
	    		json.append("{ \"weightLabel\" : \"" + rate.getUnifiedPackageRate().getPackageWeightGroup().getFriendlyLabel() + "\",");
				json.append(" \"feeOverride\" : \"" + rate.getActualFee() + "\"},");
			}
		json.append("],");
		
		List<KPSkidRate> skidRates = new ArrayList<KPSkidRate>(depot.getKpSkidRate());
		if(skidRates.size() > 0){
			json.append(" \"kpSkidRate\" : " + skidRates.get(0).getActualFee() + ",");
		}
		else{
			json.append(" \"kpSkidRate\" : " + "\"\"" + ",");
		}
		
		List<KPExtendedStorageRate> extendedRates = new ArrayList<KPExtendedStorageRate>(depot.getKpExtendedStorageRates());
		json.append(" \"kpExtendedStorageRate\" : [");
			for(KPExtendedStorageRate extendedRate : extendedRates){
				StorageDuration extendedStorage = extendedRate.getUnifiedExtendedStorageRate().getStorageDuration();
				json.append("{ \"dayLabel\" : \"" + extendedStorage.getMinDays() + " days - " + extendedStorage.getMaxDays() + " days" + "\",");
    			json.append(" \"weightLabel\" : \"" + extendedRate.getUnifiedExtendedStorageRate().getStorageWeightGroup().getFriendlyLabel() + "\",");
    			json.append(" \"feeOverride\" : \"" + extendedRate.getActualFee() + "\"},");
			}
    
		json.append("],");
		
		json.append(" \"payMethods\" : [");
		for(PayMethod method : depot.getPayMethod()) {
			json.append("\""+cleanString(method.getName())+"\",");
		}
		json.append("],");
		
		json.append(" \"languages\" : [");
		for(Language lang : depot.getLanguages()) {
			json.append("\""+cleanString(lang.getName())+"\",");
		}
		json.append("],");

		json.append(" \"features\" : [");
		for(Feature feat : depot.getFeatures()) {
			json.append("\""+cleanString(feat.getName())+"\",");
		}
		json.append("],");
		
		json.append(" \"creditCards\" : [");
		for(CreditCard card : depot.getCards()) {
			json.append("\""+cleanString(card.getName())+"\",");
		}
		json.append("]");			
		json.append(" } ");
		return json.toString().replace("\n", "").replace("\r", "").replace("\t", " ").replace(",]","]");
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
    	return new StreamingResolution("application/json", new StringReader(json));
    }
    
    public String buildJSON(List<KinekPoint> depots) {
    	StringBuilder json = new StringBuilder();    

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
		json.append(" \"address1\" : \"" + cleanString(depot.getAddress1()) + "\",");
		json.append(" \"address2\" : \"" + cleanString(depot.getAddress2()) + "\",");
		json.append(" \"city\" : \"" + cleanString(depot.getCity()) + "\",");		
		json.append(" \"geolat\" : " + depot.getGeolat() + ",");
		json.append(" \"geolong\" : " + depot.getGeolong() + ",");   		
		
		double distance = cal.getDistance(this.searchPointLatitude, this.searchPointLongitude, depot.getGeolat(), depot.getGeolong());
		json.append(" \"distance\" : " + distance);    		
		json.append(" } ");
		
		return json.toString().replace("\n", "<br>").replace("\r", "").replace("\t", " ");
    }
    
    private String cleanString(String str) {
    	if(str != null) {
    		return str.replace("\"", "\\\"");
    	}
    	else {
    		return "";
    	}
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
		
			this.postalCode = postalCode.trim().replace("(/r/n|/n/r|n|r)", "");
		
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

	public Resolution getHTMLOptionsStatesProvinces() throws Exception {		
		List<State> states = new StateDao().fetch(new CountryDao().read(1));
		List<State> provinces = new StateDao().fetch(new CountryDao().read(2));
		
		builder = new StringBuilder();
		builder.append("<option value=\"\">- United States -</option>");
		for(State state:states){
			builder.append("<option value='"+state.getName()+"' >"+state.getName()+"</option>");
		}
		builder.append("<option value=\"\">- Canada -</option>");
		for(State province:provinces){
			builder.append("<option value='"+province.getName()+"' >"+province.getName()+"</option>");
		}		
		
		return new ForwardResolution("/WEB-INF/jsp/wordpress/wp_statesprovinces_htmlbody.jsp");
	}	
	
	public Resolution getHtmlOptionsBorderProvince() throws Exception {		
		List<State> provinces = new StateDao().fetch(new CountryDao().read(2));
		
		builder = new StringBuilder();
		builder.append("<option value=\"\">Select your Province</option>");
		for(State province:provinces){
			builder.append("<option value='"+province.getStateId()+"' >"+province.getName()+"</option>");
		}		
		
		return new ForwardResolution("/WEB-INF/jsp/wordpress/wp_provinces_htmlbody.jsp");		
	}	
		
	public String getHtmlOptions(){
		return builder.toString();
	}
	
	public List<State> getBorderProvinces() throws Exception {
		return new StateDao().fetchBorderStates(new CountryDao().read(2));
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

	public int getDepotId() {
		return depotId;
	}

	public void setDepotId(int depotId) {
		this.depotId = depotId;
	}

	public boolean getIsBorderSearch() {
		return isBorderSearch;
	}

	public void setIsBorderSearch(boolean isBorderSearch) {
		this.isBorderSearch = isBorderSearch;
	}

	public boolean getIsSelectedKpFromMapList() {
		return isSelectedKpFromMapList;
	}

	public void setIsSelectedKpFromMapList(boolean isSelectedKpFromMapList) {
		this.isSelectedKpFromMapList = isSelectedKpFromMapList;
	}
	
}
