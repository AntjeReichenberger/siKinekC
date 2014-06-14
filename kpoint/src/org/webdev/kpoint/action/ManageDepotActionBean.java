package org.webdev.kpoint.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.bl.manager.EmailManager;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.persistence.CountryDao;
import org.webdev.kpoint.bl.persistence.CreditCardDao;
import org.webdev.kpoint.bl.persistence.FeatureDao;
import org.webdev.kpoint.bl.persistence.KPExtendedStorageRateDao;
import org.webdev.kpoint.bl.persistence.KPPackageRateDao;
import org.webdev.kpoint.bl.persistence.KPSkidRateDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.KinekPointHistoryDao;
import org.webdev.kpoint.bl.persistence.KinekPointStatusDao;
import org.webdev.kpoint.bl.persistence.LanguageDao;
import org.webdev.kpoint.bl.persistence.OrganizationDao;
import org.webdev.kpoint.bl.persistence.PayMethodDao;
import org.webdev.kpoint.bl.persistence.RegionDao;
import org.webdev.kpoint.bl.persistence.StateDao;
import org.webdev.kpoint.bl.persistence.UnifiedExtendedStorageRateDao;
import org.webdev.kpoint.bl.persistence.UnifiedPackageRateDao;
import org.webdev.kpoint.bl.persistence.UnifiedSkidRateDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.Country;
import org.webdev.kpoint.bl.pojo.CreditCard;
import org.webdev.kpoint.bl.pojo.Feature;
import org.webdev.kpoint.bl.pojo.KPExtendedStorageRate;
import org.webdev.kpoint.bl.pojo.KPPackageRate;
import org.webdev.kpoint.bl.pojo.KPSkidRate;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.KinekPointHistory;
import org.webdev.kpoint.bl.pojo.KinekPointStatus;
import org.webdev.kpoint.bl.pojo.Language;
import org.webdev.kpoint.bl.pojo.OperatingHours;
import org.webdev.kpoint.bl.pojo.Organization;
import org.webdev.kpoint.bl.pojo.PayMethod;
import org.webdev.kpoint.bl.pojo.Region;
import org.webdev.kpoint.bl.pojo.Role;
import org.webdev.kpoint.bl.pojo.State;
import org.webdev.kpoint.bl.pojo.UnifiedExtendedStorageRate;
import org.webdev.kpoint.bl.pojo.UnifiedPackageRate;
import org.webdev.kpoint.bl.pojo.UnifiedSkidRate;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.converter.EmailConverter;
import org.webdev.kpoint.converter.PhoneConverter;
import org.webdev.kpoint.converter.ZipConverter;
import org.webdev.kpoint.managers.ErrorManager;
import org.webdev.kpoint.managers.MessageManager;
import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.util.GeocodeRetriever;

@UrlBinding("/Depot.action")
public class ManageDepotActionBean extends SecureActionBean {
	// Various actions that can occur
	public static enum Action { Edit, Create };

	// Create / Edit a depot
	private Action action = Action.Edit;
	private boolean createDepot = false;
	
	// All form data will be populated in these objects. 
	// It must be copied to a database copy before saving
	@ValidateNestedProperties({
		@Validate(field="name",     on="saveContact", required=true),
	    @Validate(field="address1", on="saveContact", required=true),
	    @Validate(field="city",     on="saveContact", required=true),
	    @Validate(field="zip",      on="saveContact", required=true, converter=ZipConverter.class, minlength=5, maxlength=10),	    
	    @Validate(field="phone",    on="saveContact", required=true, converter=PhoneConverter.class),
	    @Validate(field="email",    on="saveContact", required=true, converter=EmailConverter.class),
	    @Validate(field="extraInfo", maxlength=1024),
	    @Validate(field="operatingHours.mondayStart", on="saveHours"),
	    @Validate(field="operatingHours.mondayEnd", on="saveHours"),
	    @Validate(field="operatingHours.tuesdayStart", on="saveHours"),
	    @Validate(field="operatingHours.tuesdayEnd", on="saveHours"),
	    @Validate(field="operatingHours.wednesdayStart", on="saveHours"),
	    @Validate(field="operatingHours.wednesdayEnd", on="saveHours"),
	    @Validate(field="operatingHours.thursdayStart", on="saveHours"),
	    @Validate(field="operatingHours.thursdayEnd", on="saveHours"),
	    @Validate(field="operatingHours.fridayStart", on="saveHours"),
	    @Validate(field="operatingHours.fridayEnd", on="saveHours"),
	    @Validate(field="operatingHours.saturdayStart", on="saveHours"),
	    @Validate(field="operatingHours.saturdayEnd", on="saveHours"),
	    @Validate(field="operatingHours.sundayStart", on="saveHours"),
	    @Validate(field="operatingHours.sundayEnd", on="saveHours")
	})
	private KinekPoint depot;
	private int depotId;

	private int statusId;
	
	@Validate(on="saveContact", required=true)
	private int stateId;
	
	@Validate(on="saveContact", required=true)
	private int countryId;
	
	// Selected features on edit feature page
	private List<Integer> languageIds = new ArrayList<Integer>();
	private List<Integer> creditCardIds = new ArrayList<Integer>();
	private List<Integer> paymentMethodIds = new ArrayList<Integer>();
	private List<Integer> featureIds = new ArrayList<Integer>();
	private boolean acceptsTaxAndDuty;
	
	private boolean hasExtendedRates = false;
	
	private String depotName;
		
	private int regionId;
	
	private int organizationId;
	
	private List<UnifiedPackageRate> unifiedRates = new ArrayList<UnifiedPackageRate>();
	private List<KPPackageRate> kpPackageRates = new ArrayList<KPPackageRate>();
	private List<Boolean> kpPackageAccepted;
	
	private List<UnifiedExtendedStorageRate> unifiedExtendedStorageRates = new ArrayList<UnifiedExtendedStorageRate>();
	private List<KPExtendedStorageRate> kpExtendedStorageRates = new ArrayList<KPExtendedStorageRate>();
	
	private UnifiedSkidRate unifiedSkidRate;
	private KPSkidRate kpSkidRate;
	private Boolean kpSkidRateAccepted;
	
	/**
	 * Setup form fields with previously saved data if it exists; 
	 */
	@Before(on={"view", "viewFeatures","viewPrices","viewHours"})
	public void setup() throws Exception {
		if (action == Action.Create && getActiveUser().getAdminAccessCheck()) {
			setCreateDepot(true);
			depot = new KinekPoint();
			KinekPointStatus status = new KinekPointStatus();
			status.setId(ExternalSettingsManager.getDepotStatusInactive());
			depot.setStatus(status);
			statusId = depot.getStatus().getId();
		}
		else if (action == Action.Edit && depotId > 0 && (getActiveUser().getAdminAccessCheck() || getActiveUser().getDepotAdminAccessCheck())) {
			// Get a depot if one is specified
			depot = new KinekPointDao().read(depotId);
			depot.filterExtendedRates();
			setupControls();
		}
		else {
			depot = null;
		}
	}
	
	/**
	 * This method is responsible for determining default values for various controls when editing
	 */
	private void setupControls() {
		for (Language language : depot.getLanguages()) {
			languageIds.add(language.getLanguageId());
		}
		
		for (CreditCard creditCard : depot.getCards()) {
			creditCardIds.add(creditCard.getCardId());
		}
		
		for (PayMethod method : depot.getPayMethod()) {
			paymentMethodIds.add(method.getPayMethodId());
		}
		
		for (Feature feature : depot.getFeatures()) {
			featureIds.add(feature.getFeatureId());
		}
		
		// Set country and state IDs
		// State and country will be null on "Add Depot" so we must check for that
		if (depot.getState() != null) {
			stateId = depot.getState().getStateId();
			if (depot.getState().getCountry() != null) {
				countryId = depot.getState().getCountry().getCountryId();		
			}
		}
		
		if(depot.getStatus() != null) {
			statusId = depot.getStatus().getId();
		}
		
		acceptsTaxAndDuty = depot.getAcceptsDutyAndTax();
		depotName = depot.getName();
		
		
		//set RegionId
		if(depot.getRegion() != null && depot.getRegion().getRegionId() != 0){
			regionId = depot.getRegion().getRegionId();
		}
		//set Organization
		if(depot.getOrganization() != null && depot.getOrganization().getOrganizationId() != 0){
			organizationId = depot.getOrganization().getOrganizationId();
		}		
		
	}
	
	@DefaultHandler
	public Resolution view() {
		if (depot == null) return new RedirectResolution(ViewKinekPointActionBean.class);
		return UrlManager.getManageDepotAddressForm();
	}
	
	/**
	 * Method called when step 1 of Edit Depot is submitted
	 */
	public Resolution saveContact() throws Exception {
		KinekPoint tempDepot;
		if (action == Action.Create) {
			tempDepot = new KinekPoint();
			
			// Set default opening/closing times
			tempDepot.getOperatingHours().setDefaultHours();
		}
		else {
			tempDepot = new KinekPointDao().read(depot.getDepotId());
		}
		
		if (action == Action.Edit) {
			String changes = "";
			
			if (!tempDepot.getName().equals(depot.getName())) {
				changes += "Depot Name Old value: " + tempDepot.getName() + " New value: " + depot.getName() + ";";
			}
			
			if (tempDepot.getStatus().getId() != statusId) {
				changes += "Depot Status Old value: " + tempDepot.getStatus().getId() + " New value: " + statusId + ";";
			}

			if (!tempDepot.getAddress1().equals(depot.getAddress1())) {
				changes += "Address Line 1 Old value: " + tempDepot.getAddress1() + " New value: " + depot.getAddress1() + ";";
			}
			
			if (tempDepot.getAddress2() != null || depot.getAddress2() != null) {
				if ((tempDepot.getAddress2() == null && depot.getAddress2() != null) ||
						(depot.getAddress2() == null && tempDepot.getAddress2() != null) ||
						(!tempDepot.getAddress2().equals(depot.getAddress2()))) {
					changes += "Address Line 2 " + (tempDepot.getAddress2() != null ? "Old value: " + tempDepot.getAddress2() : "No old value") + (depot.getAddress2() != null ? " New value: " + depot.getAddress2() : " No new value" ) + ";";
				}
			}
			
			if (!tempDepot.getCity().equals(depot.getCity())) {
				changes += "City Old value: " + tempDepot.getCity() + " New value: " + depot.getCity() + ";";
			}
			
			if (tempDepot.getState().getStateId() != (stateId)) {
				changes += "State Old value: " + tempDepot.getState().getName() + " New value: " + new StateDao().read(stateId).getName() + ";";
			}
			
			if (!tempDepot.getZip().equals(depot.getZip())) {
				changes += "Zip Code Old value: " + tempDepot.getZip() + " New value: " + depot.getZip() + ";";
			}
			
			if (!tempDepot.getPhone().equals(depot.getPhone())) {
				changes += "Phone Old value: " + tempDepot.getPhone() + " New value: " + depot.getPhone() + ";";
			}
			
			if (!tempDepot.getEmail().equals(depot.getEmail())) {
				changes += "Email Old value: " + tempDepot.getEmail() + " New value: " + depot.getEmail() + ";";
			}

			if (tempDepot.getRegion() != null && tempDepot.getRegion().getRegionId() != (regionId)) {
				changes += "Region Old value: " + tempDepot.getRegion().getName() + " New value: " + depot.getRegion().getName() + ";";
			}
			if (tempDepot.getOrganization() != null && tempDepot.getOrganization().getOrganizationId() != (organizationId)) {
				changes += "Organization Old value: " + tempDepot.getOrganization().getName() + " New value: " + depot.getOrganization().getName() + ";";
			}			

			if (!changes.isEmpty()) {
				KinekPointHistory depotHistory = new KinekPointHistory();
				depotHistory.setDepotId(depot.getDepotId());
				depotHistory.setChangedDate(Calendar.getInstance());
				depotHistory.setName(depot.getName());
				depotHistory.setTypeOfChange(ConfigurationManager.getKinekPointContactChange());
				depotHistory.setChangesMade(changes);
				
				new KinekPointHistoryDao().create(depotHistory);
			}
		}
		
		tempDepot.setName(depot.getName());
		if (getActiveUser().getAdminAccessCheck()) {
			KinekPointStatus status = new KinekPointStatus();
			status.setId(statusId);
			tempDepot.setStatus(status);
			
			// Logic for activating new depot
			if(statusId == ExternalSettingsManager.getDepotStatusActive() && tempDepot.getEnabledDate() == null) {
				UserDao kpd = new UserDao();
				
				tempDepot.setEnabledDate(new GregorianCalendar());
				List<User> admins = kpd.fetch(tempDepot, Role.DepotAdmin);
				
				// Set all depot admins to enabled and send approval email
				for (User admin: admins) {
					admin.setEnabled(true);
					admin.setEnabledDate(new GregorianCalendar());
					kpd.update(admin);
					new EmailManager().sendDepotRegistrationApprovedEmail(admin,depot);
				}
			}
			// Logic for declining a depot
			else if(statusId == ExternalSettingsManager.getDepotStatusDeclined()) {
				new EmailManager().sendDepotRegistrationDeclinedEmail(tempDepot);
			}
		}
		tempDepot.setAddress1(depot.getAddress1());
		tempDepot.setAddress2(depot.getAddress2());
		tempDepot.setCity(depot.getCity());
		tempDepot.setState(new StateDao().read(stateId));
		tempDepot.setZip(depot.getZip().replace(" ", ""));
		tempDepot.setPhone(depot.getPhone());
		tempDepot.setEmail(depot.getEmail());
		
		//set Region
		tempDepot.setRegion(new RegionDao().read(regionId));
		//set Organization
		tempDepot.setOrganization(new OrganizationDao().read(organizationId));
		
		GeocodeRetriever retriever = new GeocodeRetriever(this.getAddressString());
		tempDepot.setGeolat(retriever.getGeoLat());
		tempDepot.setGeolong(retriever.getGeoLong());
		
		
		
		if (action == Action.Create || (tempDepot.getKpPackageRates().isEmpty() && tempDepot.getKpExtendedStorageRates().isEmpty() && tempDepot.getKpSkidRate().isEmpty())) {
			if(action == Action.Create){
				depotId = new KinekPointDao().create(tempDepot);
			}
			
			UnifiedPackageRateDao unifiedPackageRateDao = new UnifiedPackageRateDao();		
			unifiedRates = unifiedPackageRateDao.fetch();			
			
			for(int i = 0; i < unifiedRates.size(); i++){	
				KPPackageRate newEntry = new KPPackageRate();
				newEntry.setUnifiedPackageRate(unifiedRates.get(i));
				newEntry.setKinekPointId(tempDepot.getDepotId());
				KPPackageRateDao kpPackageRateDao = new KPPackageRateDao();
				kpPackageRateDao.create(newEntry);
			}	
			
			UnifiedExtendedStorageRateDao unifiedExtendedStorageRateDao = new UnifiedExtendedStorageRateDao();		
			unifiedExtendedStorageRates = unifiedExtendedStorageRateDao.fetch();			
			
			for(int i = 0; i < unifiedExtendedStorageRates.size(); i++){	
				KPExtendedStorageRate newEntry = new KPExtendedStorageRate();
				newEntry.setUnifiedExtendedStorageRate(unifiedExtendedStorageRates.get(i));
				newEntry.setKinekPointId(tempDepot.getDepotId());
				newEntry.setFeeOverride(BigDecimal.ZERO);	
				//newEntry.setFeeOverride(unifiedExtendedStorageRates.get(i).getFee());	
				KPExtendedStorageRateDao kpExtendedStorageRateDao = new KPExtendedStorageRateDao();
				kpExtendedStorageRateDao.create(newEntry);
			}	
			
			UnifiedSkidRateDao unifiedSkidRateDao = new UnifiedSkidRateDao();		
			List<UnifiedSkidRate> unifiedSkidRates = unifiedSkidRateDao.fetch();			
			
			for(int i = 0; i < unifiedSkidRates.size(); i++){	
				KPSkidRate newEntry = new KPSkidRate();
				newEntry.setUnifiedSkidRate(unifiedSkidRates.get(i));
				newEntry.setKinekPointId(tempDepot.getDepotId());
				KPSkidRateDao kpSkidRateDao = new KPSkidRateDao();
				kpSkidRateDao.create(newEntry);
			}	
		}
		else {
			new KinekPointDao().update(tempDepot);
		}
		
		if(createDepot)
		{
			RedirectResolution resolution = new RedirectResolution(ManageDepotActionBean.class, "viewFeatures");
			resolution.addParameter("depotId", depotId);
			resolution.addParameter("createDepot", true);
			return resolution;
		}
			
		//assume edit by default
		setSuccessMessage(MessageManager.getSavedDepot());
		RedirectResolution resolution = new RedirectResolution(ManageDepotActionBean.class);
		resolution.addParameter("depotId", depotId);
		return resolution;
	}
	
	public Resolution viewFeatures() {
		return UrlManager.getManageDepotFeaturesForm();
	}
	
	@ValidationMethod(on="saveFeatures")
	public void saveFeaturesValidation(ValidationErrors errors) {
		//Ensure at least one credit card type is selected if credit card is a payment option
		int creditCardId = ConfigurationManager.getDepotPaymentTypeCreditCardId();
		if (getPaymentMethodIds().contains(creditCardId)
				&& getCreditCardIds().size() == 0) {
			String field = "payMethod_" + creditCardId;
			ValidationError error = ErrorManager.getManageDepotNoCreditCardsSelected();  
			errors.add(field, error);
		}
	}
	
	/**
	 * Method called when step 2 of edit depot is submitted
	 * @return
	 */
	public Resolution saveFeatures() throws Exception {
		KinekPoint tempDepot = new KinekPointDao().read(depotId);
		
		String changes = "";
						
		// languages
		List<Integer> oldLanguages = new ArrayList<Integer>();
		for (Language language : tempDepot.getLanguages())
			oldLanguages.add(language.getLanguageId());
		if (!oldLanguages.containsAll(getLanguageIds()) || !getLanguageIds().containsAll(oldLanguages)) {
			Map<Integer, Language> languages = new LanguageDao().fetchMap();
			for (Integer languageId : oldLanguages) {
				if (!getLanguageIds().contains(languageId))
					changes += "Removed Language: " + languages.get(languageId).getName() + ";";
			}
			for (Integer languageId : getLanguageIds()) {
				if (!oldLanguages.contains(languageId))
					changes += "Added Language: " + languages.get(languageId).getName() + ";";
			}
		}		
		
		tempDepot.getLanguages().clear();
		if (getLanguageIds().size() > 0) {
			Map<Integer, Language> languages = new LanguageDao().fetchMap();
			for (Integer langId : getLanguageIds()) {
				tempDepot.addLanguage(languages.get(langId));
			}
		}
		
		// Credit Cards
		List<Integer> oldCreditCards = new ArrayList<Integer>();
		for (CreditCard creditCard : tempDepot.getCards())
			oldCreditCards.add(creditCard.getCardId());
		if (!oldCreditCards.containsAll(getCreditCardIds()) || !getCreditCardIds().containsAll(oldCreditCards)) {
			Map<Integer, CreditCard> creditCards = new CreditCardDao().fetchMap();
			for (Integer creditCardId : oldCreditCards) {
				if (!getCreditCardIds().contains(creditCardId))
					changes += "Removed Credit Card: " + creditCards.get(creditCardId).getName() + ";";
			}
			for (Integer creditCardId : getCreditCardIds()) {
				if (!oldCreditCards.contains(creditCardId))
					changes += "Added Credit Card: " + creditCards.get(creditCardId).getName() + ";";
			}
		}
		
		tempDepot.getCards().clear();
		if (getCreditCardIds().size() > 0) {
			Map<Integer, CreditCard> creditCards = new CreditCardDao().fetchMap();
			for (Integer cardId : getCreditCardIds()) {
				tempDepot.addCard(creditCards.get(cardId));
			}
		}
		
		// pay methods
		List<Integer> oldPayMethods = new ArrayList<Integer>();
		for (PayMethod payMethod : tempDepot.getPayMethod())
			oldPayMethods.add(payMethod.getPayMethodId());
		if (!oldPayMethods.containsAll(getPaymentMethodIds()) || !getPaymentMethodIds().containsAll(oldPayMethods)) {
			Map<Integer, PayMethod> paymentMethods = new PayMethodDao().fetchMap();
			for (Integer payMethodId : oldPayMethods) {
				if (!getPaymentMethodIds().contains(payMethodId))
					changes += "Removed Payment Method: " + paymentMethods.get(payMethodId).getName() + ";";
			}
			for (Integer payMethodId : getPaymentMethodIds()) {
				if (!oldPayMethods.contains(payMethodId))
					changes += "Added Payment Method: " + paymentMethods.get(payMethodId).getName() + ";";
			}
		}
		
		tempDepot.getPayMethod().clear();
		if (getPaymentMethodIds().size() > 0) {
			Map<Integer, PayMethod> paymentMethods = new PayMethodDao().fetchMap();
			for (Integer paymentMethodId : getPaymentMethodIds()) {
				tempDepot.addPayMethod(paymentMethods.get(paymentMethodId));
			}
		}

		// feature
		List<Integer> oldFeatures = new ArrayList<Integer>();
		for (Feature feature : tempDepot.getFeatures())
			oldFeatures.add(feature.getFeatureId());
		if (!oldFeatures.containsAll(getFeatureIds()) || !getFeatureIds().containsAll(oldFeatures)) {
			Map<Integer, Feature> features = new FeatureDao().fetchMap();
			for (Integer featureId : oldFeatures) {
				if (!getFeatureIds().contains(featureId))
					changes += "Removed Feature: " + features.get(featureId).getName() + ";";
			}
			for (Integer featureId : getFeatureIds()) {
				if (!oldFeatures.contains(featureId))
					changes += "Added Feature: " + features.get(featureId).getName() + ";";
			}
		}
		
		tempDepot.getFeatures().clear();
		if (getFeatureIds().size() > 0) {
			Map<Integer, Feature> features = new FeatureDao().fetchMap();
			for (Integer featureId : getFeatureIds()) {
				tempDepot.addFeature(features.get(featureId));
			}
		}

		// Set whether or not duty and tax is accepted
		if (tempDepot.getAcceptsDutyAndTax() != acceptsTaxAndDuty) {
			changes += "Duty and Tax Acceptance Old value: " + tempDepot.getAcceptsDutyAndTax() + " New value: " + acceptsTaxAndDuty + ";";
		}
		tempDepot.setAcceptsDutyAndTax(acceptsTaxAndDuty);
		
		// Set the extra info
		if (tempDepot.getExtraInfo() != null || depot.getExtraInfo() != null) {
			if ((tempDepot.getExtraInfo() == null && depot.getExtraInfo() != null) ||
					(depot.getExtraInfo() == null && tempDepot.getExtraInfo() != null) ||
					(!tempDepot.getExtraInfo().equals(depot.getExtraInfo()))) {
				String formattedExtraInfo = formatAdditionalInfo(depot.getExtraInfo());
				changes += "Extra Info " + (tempDepot.getExtraInfo() != null ? "Old value: " + tempDepot.getExtraInfo() + "..." : "No old value") + (formattedExtraInfo != null ? " New value: " + formattedExtraInfo + "..." : " No new value" ) + ";";
			}
		}
		tempDepot.setExtraInfo(formatAdditionalInfo(depot.getExtraInfo()));
		
		new KinekPointDao().update(tempDepot);
		
		/* use to update user's kinekpoint, no longer necessary?
		if (tempDepot.getDepotId() == getActiveUser().getKinekPoint().getDepotId())
			getActiveUser().setKinekPoint(tempDepot);
		*/
		
		if (!changes.isEmpty()) {
			KinekPointHistory depotHistory = new KinekPointHistory();
			depotHistory.setDepotId(depotId);
			depotHistory.setName(tempDepot.getName());
			depotHistory.setChangedDate(Calendar.getInstance());
			depotHistory.setTypeOfChange(ConfigurationManager.getKinekPointServicesAndFeaturesChange());
			depotHistory.setChangesMade(changes);
			
			new KinekPointHistoryDao().create(depotHistory);
		}
		
		if(createDepot)
		{
			RedirectResolution resolution = new RedirectResolution(ManageDepotActionBean.class, "viewPrices");
			resolution.addParameter("depotId", depotId);
			resolution.addParameter("createDepot", true);
			return resolution;
		}
			
		//assume edit by default
		setSuccessMessage(MessageManager.getSavedDepot());
		RedirectResolution resolution = new RedirectResolution(ManageDepotActionBean.class, "viewFeatures");
		resolution.addParameter("depotId", tempDepot.getDepotId());
		return resolution;
	}
	
	public Resolution viewHours() {
		return UrlManager.getManageDepotHoursForm();
	}
	
	public Resolution viewPrices() throws Exception {
		//Setting up view prices
		if(getActiveUser().getAdminAccessCheck()){
			hasExtendedRates = true;
		}
		
		UnifiedPackageRateDao unifiedPackageRateDao = new UnifiedPackageRateDao();		
		unifiedRates = unifiedPackageRateDao.fetch();
		
		UnifiedExtendedStorageRateDao unifiedExtendedStorageRateDao = new UnifiedExtendedStorageRateDao();		
		unifiedExtendedStorageRates = unifiedExtendedStorageRateDao.fetch();
		
		UnifiedSkidRateDao unifiedSkidRateDao = new UnifiedSkidRateDao();		
		unifiedSkidRate = unifiedSkidRateDao.read(1);
				
		List<KPPackageRate> kpPackageRateList = new ArrayList<KPPackageRate>(depot.getKpPackageRates());
		
		int j = 0;
		for(int i = 0; i < unifiedRates.size() && j < kpPackageRateList.size();i++){
			if(kpPackageRateList.get(j).getUnifiedPackageRate().getId() == i+1){
				kpPackageRates.add(kpPackageRateList.get(j));
				j++;
			}
			else{
				kpPackageRates.add(null);
			}
		}
		
		List<KPExtendedStorageRate> kpExtendedStorageList = new ArrayList<KPExtendedStorageRate>(depot.getKpExtendedStorageRates());
		
		j = 0;
		for(int i = 0; i < unifiedExtendedStorageRates.size() && j < kpExtendedStorageList.size();i++){
			if(kpExtendedStorageList.get(j).getUnifiedExtendedStorageRate().getId() == i+1){
				kpExtendedStorageRates.add(kpExtendedStorageList.get(j));
				if(kpExtendedStorageList.get(j).getActualFee().compareTo(BigDecimal.ZERO) != 0){
					hasExtendedRates = true;
				}
				j++;
			}
			else{
				kpExtendedStorageRates.add(null);
			}
		}

		List<KPSkidRate> kpSkidRates = new ArrayList<KPSkidRate>(depot.getKpSkidRate());
		if(kpSkidRates.size() > 0){
			kpSkidRate = kpSkidRates.get(0);
			setKpSkidRateAccepted(true);
		}
		
		return UrlManager.getManageDepotPricesForm();
	}
	
	@ValidationMethod(on="saveHours")
	public void saveHoursValidation(ValidationErrors errors) {
		OperatingHours operatingHours = depot.getOperatingHours();
		boolean compareTimes = true;
		if (!operatingHours.getClosedMonday()) {
			if (operatingHours.getMondayStart() == null) {
				errors.add("depot.operatingHours.mondayStart", new SimpleError("Monday Opening Time is a required field."));
				compareTimes = false;
			}
			if (operatingHours.getMondayEnd() == null) {
				errors.add("depot.operatingHours.mondayEnd", new SimpleError("Monday Closing Time is a required field."));
				compareTimes = false;
			}
			if (compareTimes && operatingHours.getMondayStart().after(operatingHours.getMondayEnd())) {
				errors.add("depot.operatingHours.mondayStart", new SimpleError("Monday opening time must be before closing time."));
			}
		}
		
		compareTimes = true;
		if (!operatingHours.getClosedTuesday()) {
			if (operatingHours.getTuesdayStart() == null) {
				errors.add("depot.operatingHours.tuesdayStart", new SimpleError("Tuesday Opening Time is a required field."));
				compareTimes = false;
			}
			if (operatingHours.getTuesdayEnd() == null) {
				errors.add("depot.operatingHours.tuesdayEnd", new SimpleError("Tuesday Closing Time is a required field."));
				compareTimes = false;
			}
			if (compareTimes && operatingHours.getTuesdayStart().after(operatingHours.getTuesdayEnd())) {
				errors.add("depot.operatingHours.tuesdayStart", new SimpleError("Tuesday opening time must be before closing time."));
			}
		}
		
		compareTimes = true;
		if (!operatingHours.getClosedWednesday()) {
			if (operatingHours.getWednesdayStart() == null) {
				errors.add("depot.operatingHours.wednesdayStart", new SimpleError("Wednesday Opening Time is a required field."));
				compareTimes = false;
			}
			if (operatingHours.getWednesdayEnd() == null) {
				errors.add("depot.operatingHours.wednesdayEnd", new SimpleError("Wednesday Closing Time is a required field."));
				compareTimes = false;
			}
			if (compareTimes && operatingHours.getWednesdayStart().after(operatingHours.getWednesdayEnd())) {
				errors.add("depot.operatingHours.wednesdayStart", new SimpleError("Wednesday opening time must be before closing time."));
			}
		}
		
		compareTimes = true;
		if (!operatingHours.getClosedThursday()) {
			if (operatingHours.getThursdayStart() == null) {
				errors.add("depot.operatingHours.thursdayStart", new SimpleError("Thursday Opening Time is a required field."));
				compareTimes = false;
			}
			if (operatingHours.getThursdayEnd() == null) {
				errors.add("depot.operatingHours.thursdayEnd", new SimpleError("Thursday Closing Time is a required field."));
				compareTimes = false;
			}
			if (compareTimes && operatingHours.getThursdayStart().after(operatingHours.getThursdayEnd())) {
				errors.add("depot.operatingHours.thursdayStart", new SimpleError("Thursday opening time must be before closing time."));
			}
		}
		
		compareTimes = true;
		if (!operatingHours.getClosedFriday()) {
			if (operatingHours.getFridayStart() == null) {
				errors.add("depot.operatingHours.fridayStart", new SimpleError("Friday Opening Time is a required field."));
				compareTimes = false;
			}
			if (operatingHours.getFridayEnd() == null) {
				errors.add("depot.operatingHours.fridayEnd", new SimpleError("Friday Closing Time is a required field."));
				compareTimes = false;
			}
			if (compareTimes && operatingHours.getFridayStart().after(operatingHours.getFridayEnd())) {
				errors.add("depot.operatingHours.fridayStart", new SimpleError("Friday opening time must be before closing time."));
			}
		}
		
		compareTimes = true;
		if (!operatingHours.getClosedSaturday()) {
			if (operatingHours.getSaturdayStart() == null) {
				errors.add("depot.operatingHours.saturdayStart", new SimpleError("Saturday Opening Time is a required field."));
				compareTimes = false;
			}
			if (operatingHours.getSaturdayEnd() == null) {
				errors.add("depot.operatingHours.saturdayEnd", new SimpleError("Saturday Closing Time is a required field."));
				compareTimes = false;
			}
			if (compareTimes && operatingHours.getSaturdayStart().after(operatingHours.getSaturdayEnd())) {
				errors.add("depot.operatingHours.saturdayStart", new SimpleError("Saturday opening time must be before closing time."));
			}
		}
		
		compareTimes = true;
		if (!operatingHours.getClosedSunday()) {
			if (operatingHours.getSundayStart() == null) {
				errors.add("depot.operatingHours.sundayStart", new SimpleError("Sunday Opening Time is a required field."));
				compareTimes = false;
			}
			if (operatingHours.getSundayEnd() == null) {
				errors.add("depot.operatingHours.sundayEnd", new SimpleError("Sunday Closing Time is a required field."));
				compareTimes = false;
			}
			if (compareTimes && operatingHours.getSundayStart().after(operatingHours.getSundayEnd())) {
				errors.add("depot.operatingHours.sundayStart", new SimpleError("Sunday opening time must be before closing time."));
			}
		}
	}
	
	@ValidationMethod(on="savePrices")
	public void savePrices(ValidationErrors errors) throws Exception {
		
		UnifiedPackageRateDao unifiedPackageRateDao = new UnifiedPackageRateDao();		
		unifiedRates = unifiedPackageRateDao.fetch();
		
		UnifiedExtendedStorageRateDao unifiedExtendedStorageRatesDao = new UnifiedExtendedStorageRateDao();		
		unifiedExtendedStorageRates = unifiedExtendedStorageRatesDao.fetch();
		
		if(kpExtendedStorageRates.size() != 0){
			hasExtendedRates = true;
		}

		UnifiedSkidRateDao unifiedSkidRateDao = new UnifiedSkidRateDao();		
		unifiedSkidRate = unifiedSkidRateDao.read(1);

		if(kpPackageAccepted == null || kpPackageAccepted.size() == 0){
			errors.add("kpPackageRates", new SimpleError("One or more weight groups must be selected."));
		}
		
		//if the last list option is null, the size of the list returned by stripes is smaller than it should be
		if(kpPackageRates.size() < unifiedRates.size()){
			int u = unifiedRates.size();
			int k = kpPackageRates.size();	
			for(int i = 0; i < u-k; i++){
				kpPackageRates.add(null);
			}
		}
		
		//if the last list option is null, the size of the list returned by stripes is smaller than it should be
		if(kpExtendedStorageRates.size() < unifiedExtendedStorageRates.size()){
			int u = unifiedExtendedStorageRates.size();
			int k = kpExtendedStorageRates.size();	
			for(int i = 0; i < u-k; i++){
				kpExtendedStorageRates.add(null);
			}
		}
		
		for(int i = 0; i < unifiedRates.size(); i++){
			//if greater than unified
			if(kpPackageRates.get(i) != null && (kpPackageAccepted != null && kpPackageAccepted.get(i) != null)){
				if(kpPackageRates.get(i).getFeeOverride().compareTo(unifiedRates.get(i).getFee()) == 1){
					errors.add("kpPackageRates", new SimpleError("The receiving fee for " + unifiedRates.get(i).getPackageWeightGroup().getFriendlyLabel() + " cannot exceed $" + unifiedRates.get(i).getFee() + "."));
				}
			}
			else if(kpPackageRates.get(i) == null && (kpPackageAccepted != null && kpPackageAccepted.get(i) != null)){
				errors.add("kpPackageRates", new SimpleError("An accepted package rate has no value entered."));
			}
		}
		
		if(hasExtendedRates){
			for(int i = 0; i < unifiedExtendedStorageRates.size(); i++){
				//if greater than unified
				if(kpExtendedStorageRates.get(i) != null){
					if(kpExtendedStorageRates.get(i).getFeeOverride().compareTo(unifiedExtendedStorageRates.get(i).getFee()) == 1){
						errors.add("kpExtendedStorageRates", new SimpleError("The extended storage rate for " + unifiedExtendedStorageRates.get(i).getStorageWeightGroup().getFriendlyLabel() + " cannot exceed $" + unifiedExtendedStorageRates.get(i).getFee() + "."));
					}
				}
			}
		}
		
		if(kpSkidRateAccepted != null){
			if(kpSkidRate != null && kpSkidRate.getFeeOverride().compareTo(unifiedSkidRate.getFee()) == 1){
				errors.add("kpExtendedStorageRates", new SimpleError("Skid Rate cannot exceed $" + unifiedSkidRate.getFee() + "."));
			}
			else if (kpSkidRate == null){
				errors.add("kpExtendedStorageRates", new SimpleError("Skid rate has no value entered."));
			}
		}
	}
		
	/**
	 * Method called when step 3 of edit depot is submitted
	 */
	
	public Resolution savePrices() throws Exception {	
		String changes = "";
		
		KinekPoint tempDepot = new KinekPointDao().read(depot.getDepotId());
		
		List<KPPackageRate> oldKpPackageRates = new ArrayList<KPPackageRate>(tempDepot.getKpPackageRates());
		//Set<KPPackageRate> newKpPackageRates = new HashSet<KPPackageRate>();
		
		KPPackageRateDao kpPackageRateDao = new KPPackageRateDao();
		
		boolean packageExists = false;
		
		for(int i = 0; i < unifiedRates.size(); i++){	
			if(kpPackageAccepted != null && kpPackageAccepted.get(i) != null){		
				KPPackageRate newEntry = new KPPackageRate();
				newEntry.setUnifiedPackageRate(unifiedRates.get(i));
				newEntry.setKinekPointId(tempDepot.getDepotId());
				
				if(!unifiedRates.get(i).getFee().equals(kpPackageRates.get(i).getActualFee())){					
					newEntry.setFeeOverride(kpPackageRates.get(i).getFeeOverride());			
				}
				for(KPPackageRate kpPackage : oldKpPackageRates){
					if(kpPackage.getUnifiedPackageRate().getId() == newEntry.getUnifiedPackageRate().getId()){
						
						if(kpPackage.getActualFee().compareTo(kpPackageRates.get(i).getActualFee()) != 0){
							kpPackageRateDao.update(newEntry);
							
							changes += "Package rate for: " + unifiedRates.get(i).getPackageWeightGroup().getMinWeight() + " lbs - " +
							unifiedRates.get(i).getPackageWeightGroup().getMaxWeight() + " lbs." 
							+ " Old value: " + kpPackage.getActualFee() + ", new value: " + kpPackageRates.get(i).getActualFee() + ";";
						}
						packageExists = true;
					}
				}
				if(!packageExists){
					kpPackageRateDao.create(newEntry);
					changes += "Package rate for: " + unifiedRates.get(i).getPackageWeightGroup().getMinWeight() + " lbs - " +
					unifiedRates.get(i).getPackageWeightGroup().getMaxWeight() + " lbs." 
					+ " Old value: DNE, new value: " + kpPackageRates.get(i).getActualFee().toString() + ";";
				}
				//newKpPackageRates.add(newEntry);
			}
			
			else{		
				for(KPPackageRate kpPackage : oldKpPackageRates){
					if(kpPackage.getUnifiedPackageRate().getId() == unifiedRates.get(i).getId()){
						kpPackageRateDao.delete(kpPackage);
						changes += "Package rate for: " + unifiedRates.get(i).getPackageWeightGroup().getMinWeight() + " lbs - " +
						unifiedRates.get(i).getPackageWeightGroup().getMaxWeight() + " lbs." 
						+ " Old value: " + kpPackage.getActualFee() + ", new value: Not supported;";
					}
				}
			}
			packageExists = false;	
		}	
		//tempDepot.setKpPackageRates(newKpPackageRates);
		
		if(hasExtendedRates){
			KPExtendedStorageRateDao kpExtendedStorageRateDao = new KPExtendedStorageRateDao();
			List<KPExtendedStorageRate> oldKpExtendedStorageRates = new KPExtendedStorageRateDao().filterExtendedRates(new ArrayList<KPExtendedStorageRate>(tempDepot.getKpExtendedStorageRates()));
			//Set<KPExtendedStorageRate> newKpExtendedStorageRates = new HashSet<KPExtendedStorageRate>();
			
			boolean extendedExists = false;
			
			for(int i = 0; i < unifiedExtendedStorageRates.size(); i++){	
				
				//if(kpExtendedStorageAccepted.get(i) != null){
				if(kpExtendedStorageRates.get(i) != null){
					KPExtendedStorageRate newEntry = new KPExtendedStorageRate();
					newEntry.setUnifiedExtendedStorageRate(unifiedExtendedStorageRates.get(i));
					newEntry.setKinekPointId(depot.getDepotId());
					//newEntry.setKinekPoint(depot);
					newEntry.setCreatedDate(Calendar.getInstance());
					if(!unifiedExtendedStorageRates.get(i).getFee().equals(kpExtendedStorageRates.get(i).getFeeOverride())){
						newEntry.setFeeOverride(kpExtendedStorageRates.get(i).getFeeOverride());			
					}
					for(KPExtendedStorageRate kpPackage : oldKpExtendedStorageRates){
						if(kpPackage.getUnifiedExtendedStorageRate().getId() == newEntry.getUnifiedExtendedStorageRate().getId()){
							
							if(kpPackage.getActualFee().compareTo(kpExtendedStorageRates.get(i).getFeeOverride()) != 0){
								kpExtendedStorageRateDao.create(newEntry);
								
								changes += "Extended storage rate for: " + unifiedExtendedStorageRates.get(i).getStorageWeightGroup().getMinWeight() + " lbs - " +
								unifiedExtendedStorageRates.get(i).getStorageWeightGroup().getMaxWeight() + " lbs." 
								+ " Old value: " + kpPackage.getActualFee() + ", new value: " + kpExtendedStorageRates.get(i).getActualFee() + ";";
							}							
							extendedExists = true;
						}
					}
					if(!extendedExists){
						kpExtendedStorageRateDao.create(newEntry);
						
						changes += "Extended storage rate for: " + unifiedExtendedStorageRates.get(i).getStorageWeightGroup().getMinWeight() + " lbs - " +
						unifiedExtendedStorageRates.get(i).getStorageWeightGroup().getMaxWeight() + " lbs." 
						+ " Old value: DNE, new value: " + kpExtendedStorageRates.get(i).getFeeOverride().toString() + ";";
						
						extendedExists = false;
					}
					//newKpExtendedStorageRates.add(newEntry);
				}
			}
		}
		//tempDepot.setKpExtendedStorageRates(newKpExtendedStorageRates);
		
		KPSkidRateDao kpSkidRateDao = new KPSkidRateDao();
		List<KPSkidRate> oldKpSkidRate = new ArrayList<KPSkidRate>(tempDepot.getKpSkidRate());
		//Set<KPSkidRate> newKpSkidRate = new HashSet<KPSkidRate>();
		
		KPSkidRate newEntry = new KPSkidRate();
		newEntry.setUnifiedSkidRate(unifiedSkidRate);
		newEntry.setKinekPointId(depotId);
		
		if(kpSkidRateAccepted != null){
			if(oldKpSkidRate.size() != 0){
				//update
				if(!oldKpSkidRate.get(0).getActualFee().equals(kpSkidRate.getFeeOverride())){
					if(kpSkidRate.getFeeOverride().compareTo(oldKpSkidRate.get(0).getActualFee()) != 0){
						if(oldKpSkidRate.get(0).getUnifiedSkidRate().getFee().compareTo(kpSkidRate.getFeeOverride()) != 0){
							newEntry.setFeeOverride(kpSkidRate.getFeeOverride());
						}
						kpSkidRateDao.update(newEntry);
						changes += "Skid rate changed: " + " Old value: " + oldKpSkidRate.get(0).getActualFee() + ", new value:  " + kpSkidRate.getFeeOverride().toString() + ";";
					}
				}
				
			}
			else{
				//create
				newEntry.setFeeOverride(kpSkidRate.getFeeOverride());
				kpSkidRateDao.create(newEntry);
				changes += "Skid rate changed: Old value: DNE + new value:  " + kpSkidRate.getFeeOverride().toString() + ";";
			}
		}
		else{
			if(oldKpSkidRate.size() != 0){
				changes += "Removed skid support;";
				kpSkidRateDao.delete(oldKpSkidRate.get(0));
			}
		}
		//newKpSkidRate.add(newEntry);
		//tempDepot.setKpSkidRate(newKpSkidRate);
		
		if (!changes.isEmpty()) {
			KinekPointHistory depotHistory = new KinekPointHistory();
			depotHistory.setDepotId(depot.getDepotId());
			depotHistory.setName(tempDepot.getName());
			depotHistory.setChangedDate(Calendar.getInstance());
			depotHistory.setTypeOfChange(ConfigurationManager.getKinekPointPricesChange());
			depotHistory.setChangesMade(changes);
			
			new KinekPointHistoryDao().create(depotHistory);
		}
			
		if(createDepot)
		{
			RedirectResolution resolution = new RedirectResolution(ManageDepotActionBean.class, "viewHours");
			resolution.addParameter("depotId", depotId);
			resolution.addParameter("createDepot", true);
			return resolution;
		}
			
		//assume edit by default
		
		setSuccessMessage(MessageManager.getSavedDepot());
		RedirectResolution resolution = new RedirectResolution(ManageDepotActionBean.class, "viewPrices");
		resolution.addParameter("depotId", tempDepot.getDepotId());
		return resolution;
		
	} 
	

	/**
	 * Method called when step 4 of edit depot is submitted
	 */
	public Resolution saveHours() throws Exception {
		KinekPoint tempDepot = new KinekPointDao().read(depot.getDepotId());
		
		if (!getActiveUser().getAdminAccessCheck()) {
			KinekPointStatus status = new KinekPointStatus();
			status.setId(ExternalSettingsManager.getDepotStatusActive());
			tempDepot.setStatus(status);
		}
		
		String changes = "";
		
		if (tempDepot.getOperatingHours().getClosedSunday() != depot.getOperatingHours().getClosedSunday()) {
			if (depot.getOperatingHours().getClosedSunday())
				changes += "Now Closed Sundays;";
			else
				changes += "Now Opened Sundays;";
		}
		if (tempDepot.getOperatingHours().getClosedMonday() != depot.getOperatingHours().getClosedMonday()) {
			if (depot.getOperatingHours().getClosedMonday())
				changes += "Now Closed Mondays;";
			else
				changes += "Now Opened Mondays;";
		}
		if (tempDepot.getOperatingHours().getClosedTuesday() != depot.getOperatingHours().getClosedTuesday()) {
			if (depot.getOperatingHours().getClosedTuesday())
				changes += "Now Closed Tuesdays;";
			else
				changes += "Now Opened Tuesdays;";
		}
		if (tempDepot.getOperatingHours().getClosedWednesday() != depot.getOperatingHours().getClosedWednesday()) {
			if (depot.getOperatingHours().getClosedWednesday())
				changes += "Now Closed Wednesdays;";
			else
				changes += "Now Opened Wednesdays;";
		}
		if (tempDepot.getOperatingHours().getClosedThursday() != depot.getOperatingHours().getClosedThursday()) {
			if (depot.getOperatingHours().getClosedThursday())
				changes += "Now Closed Thursdays;";
			else
				changes += "Now Opened Thursdays;";
		}
		if (tempDepot.getOperatingHours().getClosedFriday() != depot.getOperatingHours().getClosedFriday()) {
			if (depot.getOperatingHours().getClosedFriday())
				changes += "Now Closed Fridays;";
			else
				changes += "Now Opened Fridays;";
		}
		if (tempDepot.getOperatingHours().getClosedSaturday() != depot.getOperatingHours().getClosedSaturday()) {
			if (depot.getOperatingHours().getClosedSaturday())
				changes += "Now Closed Saturdays;";
			else
				changes += "Now Opened Saturdays;";
		}
		
		if (!depot.getOperatingHours().getClosedSunday()) {
			if (tempDepot.getOperatingHours().getSundayEnd() == null || tempDepot.getOperatingHours().getSundayEnd().compareTo(depot.getOperatingHours().getSundayEnd()) != 0) {
				changes += "Sunday Closing Time Old value: " + tempDepot.getOperatingHours().getSundayEnd() + " New value: " + depot.getOperatingHours().getSundayEnd() + ";";
			}
			if (tempDepot.getOperatingHours().getSundayStart() == null || tempDepot.getOperatingHours().getSundayStart().compareTo(depot.getOperatingHours().getSundayStart()) != 0) {
				changes += "Sunday Opening Time Old value: " + tempDepot.getOperatingHours().getSundayStart() + " New value: " + depot.getOperatingHours().getSundayStart() + ";";
			}
		}
		
		if (!depot.getOperatingHours().getClosedMonday()) {
			if (tempDepot.getOperatingHours().getMondayEnd() == null || tempDepot.getOperatingHours().getMondayEnd().compareTo(depot.getOperatingHours().getMondayEnd()) != 0) {
				changes += "Monday Closing Time Old value: " + tempDepot.getOperatingHours().getMondayEnd() + " New value: " + depot.getOperatingHours().getMondayEnd() + ";";
			}
			if (tempDepot.getOperatingHours().getMondayStart() == null || tempDepot.getOperatingHours().getMondayStart().compareTo(depot.getOperatingHours().getMondayStart()) != 0) {
				changes += "Monday Opening Time Old value: " + tempDepot.getOperatingHours().getMondayStart() + " New value: " + depot.getOperatingHours().getMondayStart() + ";";
			}
		}
		
		if (!depot.getOperatingHours().getClosedTuesday()) {
			if (tempDepot.getOperatingHours().getTuesdayEnd() == null || tempDepot.getOperatingHours().getTuesdayEnd().compareTo(depot.getOperatingHours().getTuesdayEnd()) != 0) {
				changes += "Tuesday Closing Time Old value: " + tempDepot.getOperatingHours().getTuesdayEnd() + " New value: " + depot.getOperatingHours().getTuesdayEnd() + ";";
			}
			if (tempDepot.getOperatingHours().getTuesdayStart() == null || tempDepot.getOperatingHours().getTuesdayStart().compareTo(depot.getOperatingHours().getTuesdayStart()) != 0) {
				changes += "Tuesday Opening Time Old value: " + tempDepot.getOperatingHours().getTuesdayStart() + " New value: " + depot.getOperatingHours().getTuesdayStart() + ";";
			}
		}
		
		if (!depot.getOperatingHours().getClosedWednesday()) {
			if (tempDepot.getOperatingHours().getWednesdayEnd() == null || tempDepot.getOperatingHours().getWednesdayEnd().compareTo(depot.getOperatingHours().getWednesdayEnd()) != 0) {
				changes += "Wednesday Closing Time Old value: " + tempDepot.getOperatingHours().getWednesdayEnd() + " New value: " + depot.getOperatingHours().getWednesdayEnd() + ";";
			}
			if (tempDepot.getOperatingHours().getWednesdayStart() == null || tempDepot.getOperatingHours().getWednesdayStart().compareTo(depot.getOperatingHours().getWednesdayStart()) != 0) {
				changes += "Wednesday Opening Time Old value: " + tempDepot.getOperatingHours().getWednesdayStart() + " New value: " + depot.getOperatingHours().getWednesdayStart() + ";";
			}
		}
		
		if (!depot.getOperatingHours().getClosedThursday()) {
			if (tempDepot.getOperatingHours().getThursdayEnd() == null || tempDepot.getOperatingHours().getThursdayEnd().compareTo(depot.getOperatingHours().getThursdayEnd()) != 0) {
				changes += "Thursday Closing Time Old value: " + tempDepot.getOperatingHours().getThursdayEnd() + " New value: " + depot.getOperatingHours().getThursdayEnd() + ";";
			}
			if (tempDepot.getOperatingHours().getThursdayStart() == null || tempDepot.getOperatingHours().getThursdayStart().compareTo(depot.getOperatingHours().getThursdayStart()) != 0) {
				changes += "Thursday Opening Time Old value: " + tempDepot.getOperatingHours().getThursdayStart() + " New value: " + depot.getOperatingHours().getThursdayStart() + ";";
			}
		}
		
		if (!depot.getOperatingHours().getClosedFriday()) {
			if (tempDepot.getOperatingHours().getFridayEnd() == null || tempDepot.getOperatingHours().getFridayEnd().compareTo(depot.getOperatingHours().getFridayEnd()) != 0) {
				changes += "Friday Closing Time Old value: " + tempDepot.getOperatingHours().getFridayEnd() + " New value: " + depot.getOperatingHours().getFridayEnd() + ";";
			}
			if (tempDepot.getOperatingHours().getFridayStart() == null || tempDepot.getOperatingHours().getFridayStart().compareTo(depot.getOperatingHours().getFridayStart()) != 0) {
				changes += "Friday Opening Time Old value: " + tempDepot.getOperatingHours().getFridayStart() + " New value: " + depot.getOperatingHours().getFridayStart() + ";";
			}
		}
		
		if (!depot.getOperatingHours().getClosedSaturday()) {
			if (tempDepot.getOperatingHours().getSaturdayEnd() == null || tempDepot.getOperatingHours().getSaturdayEnd().compareTo(depot.getOperatingHours().getSaturdayEnd()) != 0) {
				changes += "Saturday Closing Time Old value: " + tempDepot.getOperatingHours().getSaturdayEnd() + " New value: " + depot.getOperatingHours().getSaturdayEnd() + ";";
			}
			if (tempDepot.getOperatingHours().getSaturdayStart() == null || tempDepot.getOperatingHours().getSaturdayStart().compareTo(depot.getOperatingHours().getSaturdayStart()) != 0) {
				changes += "Saturday Opening Time Old value: " + tempDepot.getOperatingHours().getSaturdayStart() + " New value: " + depot.getOperatingHours().getSaturdayStart() + ";";
			}
		}
		
		// Set the extra info
		if (tempDepot.getOperatingHours().getHoursInfo() != null || depot.getOperatingHours().getHoursInfo() != null) {
			if ((tempDepot.getOperatingHours().getHoursInfo() == null && depot.getOperatingHours().getHoursInfo() != null) ||
					(depot.getOperatingHours().getHoursInfo() == null && tempDepot.getOperatingHours().getHoursInfo() != null) ||
					(!tempDepot.getOperatingHours().getHoursInfo().equals(formatAdditionalInfo(depot.getOperatingHours().getHoursInfo())))) {
				String formattedExtraInfo = formatAdditionalInfo(depot.getOperatingHours().getHoursInfo());
				changes += "Hours Info " + (tempDepot.getExtraInfo() != null ? "Old value: " + tempDepot.getExtraInfo() + "..." : "No old value") + (formattedExtraInfo != null ? " New value: " + formattedExtraInfo + "..." : " No new value" ) + ";";
			}
		}
		
		if (!changes.isEmpty()) {
			KinekPointHistory depotHistory = new KinekPointHistory();
			depotHistory.setDepotId(depot.getDepotId());
			depotHistory.setName(tempDepot.getName());
			depotHistory.setChangedDate(Calendar.getInstance());
			depotHistory.setTypeOfChange(ConfigurationManager.getKinekPointHoursOfOperationChange());
			depotHistory.setChangesMade(changes);
			
			new KinekPointHistoryDao().create(depotHistory);
		}
		
		tempDepot.setOperatingHours(depot.getOperatingHours());
		tempDepot.getOperatingHours().setHoursInfo(formatAdditionalInfo(depot.getOperatingHours().getHoursInfo()));
		new KinekPointDao().update(tempDepot);
		
		// Set success Message
		setSuccessMessage(MessageManager.getSavedDepot());
		if(createDepot)
		{
			RedirectResolution resolution = new RedirectResolution(ViewKinekPointActionBean.class);
			resolution.addParameter("depotId", depotId);
			return resolution;
		}
			
		//assume edit by default
		RedirectResolution resolution = new RedirectResolution(ManageDepotActionBean.class, "viewHours");
		resolution.addParameter("depotId", tempDepot.getDepotId());
		return resolution;
	}

	private String formatAdditionalInfo(String extraInfo){
		String formattedExtraInfo = extraInfo;
		if(extraInfo.indexOf("<p>") > 0 || extraInfo.indexOf("</p>") > 0){
			formattedExtraInfo = extraInfo.replace("<p>", "");
			formattedExtraInfo = formattedExtraInfo.replace("</p>", "<br />");
		}
		formattedExtraInfo = formattedExtraInfo.replaceAll("[<br />]+$", "");
		
		return formattedExtraInfo;
	}
	
	public KinekPoint getDepot() {
		return depot;
	}

	public void setDepot(KinekPoint depot) {
		this.depot = depot;
	}

	public int getStateId() {
		return stateId;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public int getDepotId() {
		return depotId;
	}

	public void setDepotId(int depotId) {
		this.depotId = depotId;
	}
	
	public List<Integer> getLanguageIds() {
		if (languageIds == null)
			return new ArrayList<Integer>();
		return languageIds;
	}

	public List<Integer> getCreditCardIds() {
		if (creditCardIds == null)
			return new ArrayList<Integer>();
		return creditCardIds;
	}

	public void setCreditCardIds(List<Integer> creditCardIds) {
		this.creditCardIds = creditCardIds;
	}

	public List<Integer> getPaymentMethodIds() {
		if (paymentMethodIds == null)
			return new ArrayList<Integer>();
		return paymentMethodIds;
	}

	public void setPaymentMethodIds(List<Integer> paymentMethodIds) {
		this.paymentMethodIds = paymentMethodIds;
	}

	public List<Integer> getFeatureIds() {
		if (featureIds == null)
			return new ArrayList<Integer>();
		return featureIds;
	}

	public void setFeatureIds(List<Integer> featureIds) {
		this.featureIds = featureIds;
	}

	public void setLanguageIds(List<Integer> languageIds) {
		this.languageIds = languageIds;
	}
	
	public boolean isAcceptsTaxAndDuty() {
		return acceptsTaxAndDuty;
	}
	
	public String getDepotName() throws Exception {
		if (depotName == null) {
			KinekPoint d = new KinekPointDao().read(depotId);
			if (d != null) depotName = d.getName();
		}
		return depotName;
	}
	
	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	// ** Drop Down Box Methods **
	
	public void setAcceptsTaxAndDuty(boolean acceptsTaxAndDuty) {
		this.acceptsTaxAndDuty = acceptsTaxAndDuty;
	}

	public List<Language> getLanguages() throws Exception {
		return new LanguageDao().fetch();
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public List<Feature> getFeatures() throws Exception {
		return new FeatureDao().fetch();
	}
	
	public List<PayMethod> getPayMethods() throws Exception {
		return new PayMethodDao().fetch();
	}

	public List<CreditCard> getCreditCards() throws Exception {
		return new CreditCardDao().fetch();
	}
	
	public List<Country> getCountries() throws Exception {
		return new CountryDao().fetch();
	}

	public List<State> getStates() throws Exception {
		Country c = new CountryDao().read(countryId); 
		return new StateDao().fetch(c);
	}
	
	public List<KinekPointStatus> getStatuses() throws Exception {
		return new KinekPointStatusDao().fetch();
	}
	
	public void setRegionId(int regionId){
		this.regionId = regionId;
	}
	
	public int getRegionId(){
		return regionId;
	}	
	
	public void setOrganizationId(int organizationId){
		this.organizationId = organizationId;
	}

	public int getOrganizationId(){
		return organizationId;
	}
	
	public List<Region> getRegions() throws Exception {
		return new RegionDao().fetch();
	}

	public List<Organization> getOrganizations() throws Exception {
		return new OrganizationDao().fetch();
	}
	
	public List<KPPackageRate> getKpPackageRates() {
		return kpPackageRates;
	}

	public void setKpPackageRates(List<KPPackageRate> kpPackageRates) {
		this.kpPackageRates = kpPackageRates;
	}
	
	public List<Boolean> getKpPackageAccepted() {
		return kpPackageAccepted;
	}

	public void setKpPackageAccepted(List<Boolean> kpPackageAccepted) {
		this.kpPackageAccepted = kpPackageAccepted;
	}

	public List<KPExtendedStorageRate> getKpExtendedStorageRates() {
		return kpExtendedStorageRates;
	}
	public void setKpExtendedStorageRate(List<KPExtendedStorageRate> kpExtendedStorageRates) {
		this.kpExtendedStorageRates = kpExtendedStorageRates;
	}

	public List<UnifiedPackageRate> getUnifiedRates() throws Exception {
		UnifiedPackageRateDao unifiedPackageRateDao = new UnifiedPackageRateDao();		
		return unifiedPackageRateDao.fetch();
	}
	
	public List<UnifiedExtendedStorageRate> getUnifiedExtendedStorageRates() throws Exception {
		UnifiedExtendedStorageRateDao unifiedExtendedStorageRateDao = new UnifiedExtendedStorageRateDao();		
		return unifiedExtendedStorageRateDao.fetch();
	}
	
	public UnifiedSkidRate getUnifiedSkidRate() throws Exception {
		UnifiedSkidRateDao unifiedSkidRateDao = new UnifiedSkidRateDao();		
		return unifiedSkidRateDao.read(1);
	}
	
	public KPSkidRate getKpSkidRate() {
		return kpSkidRate;
	}
	
	public void setKpSkidRate(KPSkidRate kpSkidRate) {
		this.kpSkidRate = kpSkidRate;
	}
	
	/**
	 * Builds a string based on the address of the current depot.
	 * This is required to determine the latitude and longitude 
	 * @return String representation of the address of the current depot.
	 */
	private String getAddressString() throws Exception {
		State state = new StateDao().read(stateId);
		Country country = state.getCountry();
		String address = depot.getAddress1();
		if (depot.getAddress2() != null && depot.getAddress2().length() > 0)
			address += " " + depot.getAddress2();
		address += " " + depot.getCity();
		address += " " + state.getName();
		address += " " + country.getName();
		return address;
	}

	public void setKpSkidRateAccepted(Boolean kpSkidRateAccepted) {
		this.kpSkidRateAccepted = kpSkidRateAccepted;
	}

	public Boolean getKpSkidRateAccepted() {
		return kpSkidRateAccepted;
	}

	public void setHasExtendedRates(boolean hasExtendedRates) {
		this.hasExtendedRates = hasExtendedRates;
	}

	public boolean isHasExtendedRates() {
		return hasExtendedRates;
	}

	public void setCreateDepot(boolean createDepot) {
		this.createDepot = createDepot;
	}

	public boolean getCreateDepot() {
		return createDepot;
	}
}
