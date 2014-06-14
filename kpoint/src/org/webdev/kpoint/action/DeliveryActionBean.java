package org.webdev.kpoint.action;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.sourceforge.stripes.validation.ValidationState;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.bl.manager.DeliveryManager;
import org.webdev.kpoint.bl.manager.EmailManager;
import org.webdev.kpoint.bl.manager.KinekPointManager;
import org.webdev.kpoint.bl.manager.NotificationManager;
import org.webdev.kpoint.bl.manager.SMSManager;
import org.webdev.kpoint.bl.persistence.CountryDao;
import org.webdev.kpoint.bl.persistence.CourierDao;
import org.webdev.kpoint.bl.persistence.KPPackageRateDao;
import org.webdev.kpoint.bl.persistence.KPSkidRateDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.PackageReceiptDao;
import org.webdev.kpoint.bl.persistence.PackageWeightGroupDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.Country;
import org.webdev.kpoint.bl.pojo.Courier;
import org.webdev.kpoint.bl.pojo.KPPackageRate;
import org.webdev.kpoint.bl.pojo.KPSkidRate;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Package;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.PackageWeightGroup;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.managers.MessageManager;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/Deliver.action")
public class DeliveryActionBean extends SecureActionBean {
	private User selectedUser;
	@Validate(required=true, on="search")
	private String kinekNumber;
	@Validate(required=true, on={"search", "kinekNumberSearch"})
	private List<Integer> courierIds;
	private List<Integer> weightIds;
	private List<Boolean> skidIds;
	private int depotId;
	
	private static final KinekLogger logger = new KinekLogger(DeliveryActionBean.class);

	@Validate(mask="\\d{0,5}(\\.\\d{0,2})?", minvalue=0)
	private List<BigDecimal> dutyAndTaxes;
	private List<String> customInfo;
	private List<User> userSearchResults = new ArrayList<User>();
	private String firstName;
	private String lastName;
	private String stateHidden;
	private String state;
	private List<String> selectedConsumer = new ArrayList<String>();
	private String phone;
	private List<String> shippedFrom;
	
	private List<KinekPoint> userDepots;
	List<PackageWeightGroup> kpWeightGroups = new ArrayList<PackageWeightGroup>();
	
	@DefaultHandler @DontValidate
	public Resolution view() {
		return UrlManager.getAcceptPackageForm();
	}
	
	@HandlesEvent("getDutyAndTaxesAjax")
	public Resolution getDutyAndTaxesAjax() throws Exception {
		UserDao userDao = new UserDao(); 
		int userId = getActiveUser().getUserId(); 
		userDepots = userDao.fetchUserKinekPoints(userId);			
		if(userDepots.size() == 1){
			depotId = userDepots.get(0).getDepotId();
		}
		
		StringBuilder b = new StringBuilder();
    	if(depotId > 0){	
			if(new KinekPointDao().read(depotId).getAcceptsDutyAndTax()){
				b.append("true");
			}
			else{
				b.append("false");
			}	
		}
		StringReader reader = new StringReader(b.toString());
		return new StreamingResolution("text/html", reader);
	}
	
	@HandlesEvent("getSkidRateAjax")
	public Resolution getSkidRateAjax() throws Exception {
		UserDao userDao = new UserDao(); 
		int userId = getActiveUser().getUserId(); 
		userDepots = userDao.fetchUserKinekPoints(userId);			
		if(userDepots.size() == 1){
			depotId = userDepots.get(0).getDepotId();
		}
		
		StringBuilder b = new StringBuilder();
    	if(depotId > 0){	
			List<KPSkidRate> skid = new ArrayList<KPSkidRate>(new KinekPointDao().read(depotId).getKpSkidRate());
			if(skid.size() > 0){
				b.append("true");
			}
			else{
				b.append("false");
			}	
		}
		StringReader reader = new StringReader(b.toString());
		return new StreamingResolution("text/html", reader);
	}
		
	public Resolution changeDepot() throws Exception{
		UserDao userDao = new UserDao(); 
		int userId = getActiveUser().getUserId(); 
		userDepots = userDao.fetchUserKinekPoints(userId);	
		
		ensurePackageDataExists();
		
		int numberOfPackages = Integer.parseInt(this.getSessionAttribute("numberOfPackages").toString());
		this.setSessionAttribute("numberOfPackages", numberOfPackages);
	 	
    	if(!(depotId > 0) && userDepots.size() == 1){
    		depotId = userDepots.get(0).getDepotId();
    	}
    	
		if(depotId > 0){
			kpWeightGroups = KinekPointManager.getWeightGroups(depotId);
		}
		weightIds = new ArrayList<Integer>();
		return UrlManager.getAcceptPackageVarification();
	}
	
	@ValidationMethod(on="userSearch")
	public void userSearchValidation(ValidationErrors errors) {
		if((firstName == null || firstName.trim().length() == 0)
				&& (lastName == null || lastName.trim().length() == 0)				
				&& (kinekNumber == null || kinekNumber.trim().length() == 0)
				&& (phone == null || phone.trim().length() == 0))
		{
			errors.add("nosearchcriteria", new SimpleError("You must enter at least one search criteria."));
		}
	}
	
	@ValidationMethod(on="accept",when=ValidationState.ALWAYS)
	public void acceptValidation(ValidationErrors errors) throws Exception {
		UserDao userDao = new UserDao(); 
		int userId = getActiveUser().getUserId(); 
		userDepots = userDao.fetchUserKinekPoints(userId);		
		
		if (courierIds == null) {
			courierIds = new ArrayList<Integer>();
		}
		
		int numberOfPackages = Integer.parseInt(this.getSessionAttribute("numberOfPackages").toString());
		if (courierIds.size() != numberOfPackages) {
			while (courierIds.size() < numberOfPackages) {
				courierIds.add(0);
			}
			
			errors.add("nosearchcriteria", new SimpleError("You must select a courier for each package."));
		}
		
		for (Integer courierId : courierIds) {
			if(courierId == null || courierId == 0){
				errors.add("nosearchcriteria", new SimpleError("You must select a courier for each package."));
			} 
		}
		
		if (weightIds == null) {
			weightIds = new ArrayList<Integer>();
		}

		if (weightIds.size() != numberOfPackages) {
			while (weightIds.size() < numberOfPackages) {
				weightIds.add(0);
			}
			errors.add("nosearchcriteria", new SimpleError("You must select a weight for each package."));
		}
		
		for (Integer weightId : weightIds) {
			if(weightId == null || weightId == 0){
				errors.add("nosearchcriteria", new SimpleError("You must select a weight for each package."));
			} 
		}
		
		if(userDepots.size() > 1 && depotId <= 0){
			errors.add("nosearchcriteria", new SimpleError("You must select a depot at which to accept the delivery."));
		}
		
		if(!(depotId > 0) && userDepots.size() == 1){
    		depotId = userDepots.get(0).getDepotId();
    	}	
		if(depotId > 0){
			kpWeightGroups = KinekPointManager.getWeightGroups(depotId);
		}
	}
	
	/**
	 * Event used when accepting the package
	 * @return
	 * @throws Exception 
	 */	
	@SuppressWarnings("unchecked")
	public Resolution accept() throws Exception {
		try{
			ensurePackageDataExists();
			
			KinekPoint receivingKinekPoint = getActiveUser().getKinekPoint();
			if(getActiveUser().getDepotAdminAccessCheck()){		
				UserDao userDao = new UserDao(); 
				int userId = getActiveUser().getUserId(); 
				userDepots = userDao.fetchUserKinekPoints(userId);	
				
				if(userDepots.size() > 1){
					KinekPointDao kinekPointDao = new KinekPointDao();
					receivingKinekPoint = kinekPointDao.read(depotId);
				}
				else{
					receivingKinekPoint = userDepots.get(0);
				}
			}
			
			List<String> selectedConsumerList = (List<String>)this.getSessionAttribute("selectedConsumerList");
			User depotAdmin = getActiveUser();
			
			Set<Package> packages = new HashSet<Package>();
			for (int i = 0; i < courierIds.size(); i++) {
				Package packageObj = new Package();
				packageObj.setCourier(new CourierDao().read(courierIds.get(i)));
				// When the dutyAndTax input field is not displayed, it is set to null
				// Set it to zero to prevent nulls from entering the DB
				if (dutyAndTaxes.get(i) == null || !receivingKinekPoint.getAcceptsDutyAndTax()) {
					dutyAndTaxes.set(i, new BigDecimal(0.00));
				}
				packageObj.setDutyAndTax(dutyAndTaxes.get(i));
				packageObj.setCustomInfo(customInfo.get(i));
				packageObj.setShippedFrom(shippedFrom.get(i));
				packageObj.setPackageWeightGroup(new PackageWeightGroupDao().read(weightIds.get(i)));
				packageObj.setPickupFee(DeliveryManager.getPackagePickupFee(receivingKinekPoint, weightIds.get(i)));
				packageObj.setOnSkid(skidIds.get(i));
				packageObj.setSkidFee(DeliveryManager.getPackageSkidFee(receivingKinekPoint, skidIds.get(i)));
								
				packages.add(packageObj);
			}
						
			//CALL BL
			DeliveryManager.acceptDelivery(getActiveUser(), receivingKinekPoint, selectedConsumerList, packages);
		}
		catch(Exception ex){
			//ensure that any exception that bubbles up in the accept delivery flow is logged as an exception
			logger.error(new ApplicationException("Error occurred during accept delivery", ex));	
			throw ex;
		}
		
		getContext().getMessages().add(MessageManager.getAcceptDelivery());		
		return new RedirectResolution(DeliveryActionBean.class, "view");
	}
	
	@DontValidate
	public Resolution addAnotherPackage() throws Exception {
		UserDao userDao = new UserDao(); 
		int userId = getActiveUser().getUserId(); 
		userDepots = userDao.fetchUserKinekPoints(userId);	
		
		if(userDepots.size() == 1){
			depotId = userDepots.get(0).getDepotId();
		}
		
		ensurePackageDataExists();
		
		int numberOfPackages = Integer.parseInt(this.getSessionAttribute("numberOfPackages").toString());
		numberOfPackages++;
		this.setSessionAttribute("numberOfPackages", numberOfPackages);
		
		dutyAndTaxes.add(new BigDecimal(0.00));
		courierIds.add(0);
		weightIds.add(0);
		customInfo.add("");
		shippedFrom.add("");
		
		if(depotId != 0){
			kpWeightGroups = KinekPointManager.getWeightGroups(depotId);
		}
		
		return UrlManager.getAcceptPackageVarification();
	}
	
	@DontValidate
	public Resolution removeLastPackage() throws Exception {
		UserDao userDao = new UserDao(); 
		int userId = getActiveUser().getUserId(); 
		userDepots = userDao.fetchUserKinekPoints(userId);	
		
		if(userDepots.size() == 1){
			depotId = userDepots.get(0).getDepotId();
		}
		
		ensurePackageDataExists();
		
		int numberOfPackages = Integer.parseInt(this.getSessionAttribute("numberOfPackages").toString());
		if (numberOfPackages > 1)
			numberOfPackages--;
		this.setSessionAttribute("numberOfPackages", numberOfPackages);
		
		dutyAndTaxes.remove(dutyAndTaxes.size() - 1);
		courierIds.remove(courierIds.size() - 1);
		weightIds.remove(weightIds.size() - 1);
		customInfo.remove(customInfo.size() - 1);
		shippedFrom.remove(shippedFrom.size() - 1);
		
		if(depotId != 0){
			kpWeightGroups = KinekPointManager.getWeightGroups(depotId);
		}
		
		return UrlManager.getAcceptPackageVarification();
	}
	
	//If field forms are blank in the table the Lists do not get populated with data,
	//this method makes sure the lists are instantiated and populated with their default values
	private void ensurePackageDataExists() {
		int numberOfPackages = Integer.parseInt(this.getSessionAttribute("numberOfPackages").toString());
		
		if (dutyAndTaxes == null) {
			dutyAndTaxes = new ArrayList<BigDecimal>();
		}
		while (dutyAndTaxes.size() < numberOfPackages) {
			dutyAndTaxes.add(new BigDecimal(0.00));
		}
		
		if (courierIds == null) {
			courierIds = new ArrayList<Integer>();
		}
		while (courierIds.size() < numberOfPackages) {
			courierIds.add(0);
		}
		
		if (weightIds == null) {
			weightIds = new ArrayList<Integer>();
		}
		while (weightIds.size() < numberOfPackages) {
			weightIds.add(0);
		}
		
		if (customInfo == null) {
			customInfo = new ArrayList<String>();
		}
		while (customInfo.size() < numberOfPackages) {
			customInfo.add(null);
		}
		
		if (shippedFrom == null) {
			shippedFrom = new ArrayList<String>();
		}
		while (shippedFrom.size() < numberOfPackages) {
			shippedFrom.add(null);
		}
	}
	
	/**
	 * Handles the user search event. This is responsible for searching for 
	 * kinek consumers based on the search criteria
	 * @return The search results page
	 */
	public Resolution userSearch() throws Exception {
		UserDao userDao = new UserDao();
		
		if(kinekNumber != null && kinekNumber.trim().length() != 0){
			//format kinek number before sending to backend
			//remove prefixes of KINEK or #
			userSearchResults = userDao.fetchConsumers(formatKinekNumber(kinekNumber));
		}
		else{			
			userSearchResults = userDao.fetchConsumersByNamesOrPhone(firstName, lastName, phone);
		}
				
		return UrlManager.getAcceptPackageForm();	
	}
	
	private String formatKinekNumber(String kinekNumber){
		String formattedKinekNumber = kinekNumber;
		String precedingText = ConfigurationManager.getKinekNumberPrecedingText();
		if (kinekNumber.toUpperCase().startsWith(precedingText, 0)) {
			formattedKinekNumber = kinekNumber.substring(5);
		}
		else if(kinekNumber.startsWith("#")){
			formattedKinekNumber = kinekNumber.substring(1);
		}
		
		return formattedKinekNumber;
	}
	
	/**
	 * Handles the user next  event. This is responsible for taking user to 
	 * next page to select courier, tax etc.
	 * @return The search results page
	 */
	public Resolution next() throws Exception {
		UserDao userDao = new UserDao(); 
		int userId = getActiveUser().getUserId(); 
		userDepots = userDao.fetchUserKinekPoints(userId);			
		if(userDepots.size() == 1){
			depotId = userDepots.get(0).getDepotId();
		}
		
		this.setSessionAttribute("selectedConsumerList",selectedConsumer);
		this.setSessionAttribute("numberOfPackages", 1);
		courierIds = new ArrayList<Integer>();
		courierIds.add(0);
		weightIds = new ArrayList<Integer>();
		weightIds.add(0);
		dutyAndTaxes = new ArrayList<BigDecimal>();
		dutyAndTaxes.add(new BigDecimal(0.00));
		customInfo = new ArrayList<String>();
		customInfo.add("");
		shippedFrom = new ArrayList<String>();
		shippedFrom.add("");
		
		if(depotId != 0){
			kpWeightGroups = KinekPointManager.getWeightGroups(depotId);
		}
		
		return UrlManager.getAcceptPackageVarification();
	}
	
	/**
	 * Initiates the kinek number search functionality
	 * @return Kinek search form resolution
	 */
	public Resolution kinekNumberSearch() {
		return UrlManager.getKinekNumberSearchForm();
	}

	public String getKinekNumber() {
		return kinekNumber;
	}

	public void setKinekNumber(String kinekNumber) {
		this.kinekNumber = kinekNumber;
	}

	public List<String> getCustomInfo() {
		return customInfo;
	}

	public void setCustomInfo(List<String> customInfo) {
		this.customInfo = customInfo;
	}

	public List<Integer> getCourierIds() {
		return courierIds;
	}

	public void setCourierIds(List<Integer> courierIds) {
		this.courierIds = courierIds;
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public List<Integer> getWeightIds() {
		return weightIds;
	}

	public void setWeightIds(List<Integer> weightIds) {
		this.weightIds = weightIds;
	}

	public List<Boolean> getSkidIds() {
		return skidIds;
	}

	public void setSkidIds(List<Boolean> skidIds) {
		this.skidIds = skidIds;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	public List<Courier> getCouriers() throws Exception {
		return new CourierDao().fetch();
	}
	
	public List<BigDecimal> getDutyAndTaxes() {
		return dutyAndTaxes;
	}

	public void setDutyAndTaxes(List<BigDecimal> dutyAndTaxes) {
		this.dutyAndTaxes = dutyAndTaxes;
	}
	
	public String getVia() {
		return ConfigurationManager.getViaAccept();
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public List<User> getUserSearchResults()
	{
		return userSearchResults;
	}
	
	public void setUserSearchResults(List<User> searchResults)
	{
		userSearchResults = searchResults;
	}
	
	public String getStateHidden() {
		return stateHidden;
	}

	public void setStateHidden(String stateHidden) {
		this.stateHidden = stateHidden;
	}	
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public List<Country> getCountries() throws Exception {
		return new CountryDao().fetch();
	}
	
	public List<String> getSelectedConsumer() {
		return selectedConsumer;
	}

	public void setSelectedConsumer(List<String> consumer) {
		this.selectedConsumer = consumer;
	}
	
	public void setPhone(String phone) {
		this.phone=phone;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setShippedFrom(List<String> shippedFrom) {
		this.shippedFrom = shippedFrom;
	}
	
	public List<String> getShippedFrom() {
		return shippedFrom;
	}
	
	public boolean getMoreThanOnePackage() {
		return Integer.parseInt(this.getSessionAttribute("numberOfPackages").toString()) > 1;
	}
	
	public List<KinekPoint> getUserDepots(){
		return userDepots;
	}
	
	public int getDepotId() {
		return depotId;
	}

	public void setDepotId(int depotId) {
		this.depotId = depotId;
	}

	public List<PackageWeightGroup> getKpWeightGroups() {
		return kpWeightGroups;
	}

	public void setKpWeightGroups(List<PackageWeightGroup> kpWeightGroups) {
		this.kpWeightGroups = kpWeightGroups;
	}
}
