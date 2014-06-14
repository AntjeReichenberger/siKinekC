package org.webdev.kpoint.action;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

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

@UrlBinding("/DepotSearch.action")
public class FindDepotActionBean extends BaseActionBean {
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
    private boolean isBorderSearch;
    private int depotId = 0;   
    private boolean isSelectedKpFromMapList=false;
    
    @DefaultHandler
    public Resolution view() throws Exception {
    	//Ajax call
    	
    	if(!isSelectedKpFromMapList){
    		if (maxLongitude != 0 || minLongitude != 0 || maxLatitude != 0 || minLatitude != 0) {
    			return findDepots();
    		}
    	
    		//Specific KP
    		else if(depotId != 0) {
    			KinekPoint depot = new KinekPointDao().read(depotId);
    			if(depot != null) {
    				radius = 5;
    				criteria = depot.getGeolat() + ", " + depot.getGeolong();
    				displayCriteria = depot.getAddress1() + ", " + depot.getCity() + ", " + depot.getState().getName();
    				return search();
    			}
    		}
    	}else if(isSelectedKpFromMapList){ 
    		
    		String json=getAdditionalInfoForSelectedKP(depotId);
    		
    		return new StreamingResolution("text", new StringReader(json));
    	}
    	
    	//Search page
    	//return new ForwardResolution("/WEB-INF/jsp/depotsearch.jsp");
    	return UrlManager.getWordPressUrl();
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
       	//return new ForwardResolution("/WEB-INF/jsp/depotsearch_results.jsp");
    	return UrlManager.getWordPressUrl();
    	
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
