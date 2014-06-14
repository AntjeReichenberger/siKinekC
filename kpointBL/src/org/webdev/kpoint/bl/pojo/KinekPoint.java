package org.webdev.kpoint.bl.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class KinekPoint implements Serializable, Comparable<KinekPoint> {
	private static final long serialVersionUID = -1770997745746540514L;
	private int depotId = -1;
	private String name;
	private String address1;
	private String address2;
	private String city;
	private State state;
	private String zip;
	private String phone;
	private String email;
	private String extraInfo;
	private ReferralSource referralSource;
	private Association association;
	private KinekPointStatus status;
	private boolean acceptsDutyAndTax;
	private double geolat;
	private double geolong;
	private double distance;
	private Calendar createdDate;
	private Calendar enabledDate;
	
	private Set<Language> languages = new HashSet<Language>();
	private Set<CreditCard> cards = new HashSet<CreditCard>();
	private Set<PayMethod> payMethod = new HashSet<PayMethod>();
	private Set<Feature> features = new HashSet<Feature>();
	
	private Set<KPPackageRate> kpPackageRates = new HashSet<KPPackageRate>();
	private Set<KPExtendedStorageRate> kpExtendedStorageRates = new HashSet<KPExtendedStorageRate>();
	private Set<KPSkidRate> kpSkidRate;

	private OperatingHours operatingHours = new OperatingHours();
	private Set<State> borderStates=new HashSet<State>();
	
	private Region region = new Region();
	private Organization organization = new Organization();

	public int getDepotId() {
		return depotId;
	}
	
	public void setDepotId(int depotId) {
		this.depotId = depotId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	//Used to populate drop down labels for depot lists
	public String getNameAddress1City(){
		return name + " (" + address1 + ", " + city + ")";
	}
	
	public String getAddress1() {
		return address1;
	}
	
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	
	public String getAddress2() {
		return address2;
	}
	
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public String getZip() {
		return zip;
	}
	
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getExtraInfo() {
		return extraInfo;
	}
	
	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}
	
	public ReferralSource getReferralSource() {
		return referralSource;
	}

	public void setReferralSource(ReferralSource referralSource) {
		this.referralSource = referralSource;
	}

	public Association getAssociation() {
		return association;
	}

	public void setAssociation(Association association) {
		this.association = association;
	}

	public KinekPointStatus getStatus() {
		return status;
	}

	public void setStatus(KinekPointStatus status) {
		this.status = status;
	}

	public Set<Language> getLanguages() {
		return languages;
	}
	
	public void setLanguages(Set<Language> value) {
		this.languages = value;
	}
	
	public void addLanguage(Language value)
	{
		this.languages.add(value);
	}
	
	public Set<CreditCard> getCards() {
		return cards;
	}
	
	public void setCards(Set<CreditCard> cards) {
		this.cards = cards;
	}
	
	public void addCard(CreditCard value) {
		this.cards.add(value);
	}
	
	public Set<PayMethod> getPayMethod() {
		return payMethod;
	}
	
	public void setPayMethod(Set<PayMethod> payMethod) {
		this.payMethod = payMethod;
	}
	
	public void addPayMethod(PayMethod value) {
		this.payMethod.add(value);
	}
	
	public Set<Feature> getFeatures() {
		return features;
	}
	
	public void setFeatures(Set<Feature> features) {
		this.features = features;
	}
	
	public void addFeature(Feature value)
	{ 
		this.features.add(value);
	}
	
	public ArrayList<String> getCardVals() {
		ArrayList<String> vals = new ArrayList<String>(); 
		for (CreditCard c : cards)
			vals.add(c.getCardId() + "");
		
		return vals;
	}
	
	public String getFullAddress() {
		String add2 = "";
		if (address2 != null)
			add2 = address2;
		return address1 + " " + add2 + " " + city + ", " + getState().getName() + " " + getState().getCountry().getName();
	}
	
	public boolean getAcceptsDutyAndTax() {
		return acceptsDutyAndTax;
	}

	public void setAcceptsDutyAndTax(boolean acceptsDutyAndTax) {
		this.acceptsDutyAndTax = acceptsDutyAndTax;
	}

	public double getGeolat() {
		return geolat;
	}

	public void setGeolat(double geolat) {
		this.geolat = geolat;
	}

	public double getGeolong() {
		return geolong;
	}

	public void setGeolong(double geolong) {
		this.geolong = geolong;
	}
	
	/** This is the distance from a given user location to the KinekPoint.  It is not a base attribute of KinekPoints and
	 * is only used during a KP Search.
	 */
	public double getDistance() {
		return distance;
	}

	/** This is the distance from a given user location to the KinekPoint.  It is not a base attribute of KinekPoints and
	 * is only used during a KP Search.
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public OperatingHours getOperatingHours() {
		return operatingHours;
	}
	
	public void setOperatingHours(OperatingHours operatingHours) {
		this.operatingHours = operatingHours;
	}

	public Calendar getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}

	public Calendar getEnabledDate() {
		return enabledDate;
	}

	public void setEnabledDate(Calendar enabledDate) {
		this.enabledDate = enabledDate;
	}

	
	public Set<State> getBorderStates() {
		return borderStates;
	}
	
	public void setBorderStates(Set<State> borderStates) {
		this.borderStates = borderStates;
	}
	
	public void addBorderStates(State value)
	{ 
		this.borderStates.add(value);
	}
	
	public Region getRegion() {
		return region;
	}	
	public void setRegion(Region region) {
		this.region = region;
	}

	public Organization getOrganization() {
		return organization;
	}	
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public void setKpPackageRates(Set<KPPackageRate> kpPackageRates) {
		this.kpPackageRates = kpPackageRates;
	}

	public Set<KPPackageRate> getKpPackageRates() {
		return kpPackageRates;
	}

	public void setKpExtendedStorageRates(Set<KPExtendedStorageRate> kpExtendedStorageRates) {
		this.kpExtendedStorageRates = kpExtendedStorageRates;
	}

	public Set<KPExtendedStorageRate> getKpExtendedStorageRates() {
		return kpExtendedStorageRates;
	}

	public void setKpSkidRate(Set<KPSkidRate> kpSkidRate) {
		this.kpSkidRate = kpSkidRate;
	}

	public Set<KPSkidRate> getKpSkidRate() {
		return kpSkidRate;
	}

	public void populateFees(){
		for(KPPackageRate packageRate : getKpPackageRates()){
			packageRate.populateActualFee();
		}
		for(KPExtendedStorageRate extendedRate : getKpExtendedStorageRates()){
			extendedRate.populateActualFee();
		}
		for(KPSkidRate skidRate : getKpSkidRate()){
			skidRate.populateActualFee();
		}
	}
	
	public void filterExtendedRates(){
		List<KPExtendedStorageRate> kpExtended = new ArrayList<KPExtendedStorageRate>(kpExtendedStorageRates);
		
		//newer dates appear before older dates. The second, third, fourth, etc entries for the same id will not be added back to the set
		Collections.sort(kpExtended);
		
		List<KPExtendedStorageRate> filteredRates = new ArrayList<KPExtendedStorageRate>();
		for(int i = 0; i < kpExtended.size();i++){
			if(i > 0){
				//The second, third, fourth, etc entries for the same id will not be added back to the set
				if(kpExtended.get(i).getUnifiedExtendedStorageRate().getId() != kpExtended.get(i-1).getUnifiedExtendedStorageRate().getId()){					
					filteredRates.add(kpExtended.get(i));
				}
			}
			else{
				//Always add the first list element
				filteredRates.add(kpExtended.get(i));
			}	
		}
		kpExtendedStorageRates = new TreeSet<KPExtendedStorageRate>(filteredRates);
	}
	
	@Override
	public int compareTo(KinekPoint arg0) {
		String s1 = name.toLowerCase();
		String s2 = arg0.getName().toLowerCase();	
		return s1.compareTo(s2);
	}
}
