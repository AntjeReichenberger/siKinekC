package org.webdev.kpoint.bl.api.mapper.response.kinekpoint;

import static org.webdev.kpoint.bl.api.mapper.response.util.CollectionConstructor.construct;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.webdev.kpoint.bl.pojo.CreditCard;
import org.webdev.kpoint.bl.pojo.Feature;
import org.webdev.kpoint.bl.pojo.Language;
import org.webdev.kpoint.bl.pojo.PayMethod;
import org.webdev.kpoint.bl.api.mapper.response.ReferralSource;
import org.webdev.kpoint.bl.api.mapper.response.State;

public class KinekPoint implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1526137483605399635L;

	private int depotId;
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
	private String association;
	private String status;
	private boolean acceptsDutyAndTax;
	private double geolat;
	private double geolong;
	private double distance;
	private Calendar createdDate;
	private Calendar enabledDate;

	private Set<String> languages;
	private Set<String> cards;
	private Set<String> payMethod;
	private Set<String> features;

	private Set<PackageRate> packageRates;
	private Set<ExtendedStorageRate> extendedStorageRates;
	private Set<Rate> skidRates;

	private OperatingHours operatingHours;
	private Set<State> borderStates;

	private String region;
	private Organization organization;

	public KinekPoint(org.webdev.kpoint.bl.pojo.KinekPoint blKinekPoint) {
		depotId = blKinekPoint.getDepotId();
		name = blKinekPoint.getName();
		address1 = blKinekPoint.getAddress1();
		address2 = blKinekPoint.getAddress2();
		city = blKinekPoint.getCity();
		if (blKinekPoint.getState() != null) {
			state = new State(blKinekPoint.getState());
		}
		zip = blKinekPoint.getZip();
		phone = blKinekPoint.getPhone();
		email = blKinekPoint.getEmail();
		extraInfo = blKinekPoint.getExtraInfo();
		if (blKinekPoint.getReferralSource() != null) {
			referralSource = new ReferralSource(
					blKinekPoint.getReferralSource());
		}
		if (blKinekPoint.getAssociation() != null) {
			association = blKinekPoint.getAssociation().getName();
		}
		if (blKinekPoint.getStatus() != null) {
			status = blKinekPoint.getStatus().getName();
		}
		acceptsDutyAndTax = blKinekPoint.getAcceptsDutyAndTax();
		geolat = blKinekPoint.getGeolat();
		geolong = blKinekPoint.getGeolong();
		distance = blKinekPoint.getDistance();
		createdDate = blKinekPoint.getCreatedDate();
		enabledDate = blKinekPoint.getEnabledDate();
		languages = languageSet(blKinekPoint.getLanguages());
		cards = cardSet(blKinekPoint.getCards());
		payMethod = payMethodSet(blKinekPoint.getPayMethod());
		features = featureSet(blKinekPoint.getFeatures());
		skidRates = construct(blKinekPoint.getKpSkidRate(), Rate.class);
		packageRates = construct(blKinekPoint.getKpPackageRates(),
				PackageRate.class);
		extendedStorageRates = construct(
				blKinekPoint.getKpExtendedStorageRates(),
				ExtendedStorageRate.class);
		/*
		 * minPackageRate = blKinekPoint.getMinPackageRate(); maxPackageRate =
		 * blKinekPoint.getMaxPackageRate(); minStorageRate =
		 * blKinekPoint.getMinStorageRate(); maxStorageRate =
		 * blKinekPoint.getMaxStorageRate(); minSkidRate =
		 * blKinekPoint.getMinSkidRate(); maxSkidRate =
		 * blKinekPoint.getMaxSkidRate();
		 */
		if (blKinekPoint.getOperatingHours() != null) {
			operatingHours = new OperatingHours(
					blKinekPoint.getOperatingHours());
		}
		borderStates = construct(blKinekPoint.getBorderStates(), State.class);
		if (region != null) {
			region = blKinekPoint.getRegion().getName();
		}
		if (organization != null) {
			organization = new Organization(blKinekPoint.getOrganization());
		}
	}

	private static Set<String> featureSet(Set<Feature> features) {
		if (features == null) {
			return null;
		}
		if (features.isEmpty()) {
			return Collections.emptySet();
		}

		Set<String> newFeatures = new HashSet<String>();
		for (Feature feature : features) {
			newFeatures.add(feature.getName());
		}

		return newFeatures;
	}

	private static Set<String> payMethodSet(Set<PayMethod> payMethods) {
		if (payMethods == null) {
			return null;
		}
		if (payMethods.isEmpty()) {
			return Collections.emptySet();
		}

		Set<String> newPayMethods = new HashSet<String>();
		for (PayMethod payMethod : payMethods) {
			newPayMethods.add(payMethod.getName());
		}

		return newPayMethods;
	}

	private static Set<String> cardSet(Set<CreditCard> cards) {
		if (cards == null) {
			return null;
		}
		if (cards.isEmpty()) {
			return Collections.emptySet();
		}

		Set<String> newCards = new HashSet<String>();
		for (CreditCard card : cards) {
			newCards.add(card.getName());
		}

		return newCards;
	}

	private static Set<String> languageSet(Set<Language> languages) {
		if (languages == null) {
			return null;
		}
		if (languages.isEmpty()) {
			return Collections.emptySet();
		}

		Set<String> newLanguages = new HashSet<String>();
		for (Language language : languages) {
			newLanguages.add(language.getName());
		}

		return newLanguages;
	}

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

	public String getAssociation() {
		return association;
	}

	public void setAssociation(String association) {
		this.association = association;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isAcceptsDutyAndTax() {
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

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
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

	public Set<String> getLanguages() {
		return languages;
	}

	public void setLanguages(Set<String> languages) {
		this.languages = languages;
	}

	public Set<String> getCards() {
		return cards;
	}

	public void setCards(Set<String> cards) {
		this.cards = cards;
	}

	public Set<String> getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(Set<String> payMethod) {
		this.payMethod = payMethod;
	}

	public Set<String> getFeatures() {
		return features;
	}

	public void setFeatures(Set<String> features) {
		this.features = features;
	}

	public OperatingHours getOperatingHours() {
		return operatingHours;
	}

	public void setOperatingHours(OperatingHours operatingHours) {
		this.operatingHours = operatingHours;
	}

	public Set<State> getBorderStates() {
		return borderStates;
	}

	public void setBorderStates(Set<State> borderStates) {
		this.borderStates = borderStates;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Set<PackageRate> getPackageRates() {
		return packageRates;
	}

	public void setPackageRates(Set<PackageRate> packageRates) {
		this.packageRates = packageRates;
	}

	public Set<ExtendedStorageRate> getExtendedStorageRates() {
		return extendedStorageRates;
	}

	public void setExtendedStorageRates(
			Set<ExtendedStorageRate> extendedStorageRates) {
		this.extendedStorageRates = extendedStorageRates;
	}

	public Set<Rate> getSkidRates() {
		return skidRates;
	}

	public void setSkidRates(Set<Rate> skidRates) {
		this.skidRates = skidRates;
	}

}