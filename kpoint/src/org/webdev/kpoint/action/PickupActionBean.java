package org.webdev.kpoint.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.manager.ReferralPromotionManager;
import org.webdev.kpoint.bl.persistence.ConsumerCreditDao;
import org.webdev.kpoint.bl.persistence.CountryDao;
import org.webdev.kpoint.bl.persistence.CreditStatusDao;
import org.webdev.kpoint.bl.persistence.KPPackageRateDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.PackageDao;
import org.webdev.kpoint.bl.persistence.PackageReceiptDao;
import org.webdev.kpoint.bl.persistence.PackageWeightGroupDao;
import org.webdev.kpoint.bl.persistence.PickupDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.ConsumerCredit;
import org.webdev.kpoint.bl.pojo.Country;
import org.webdev.kpoint.bl.pojo.CreditStatus;
import org.webdev.kpoint.bl.pojo.KPPackageRate;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Package;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.PackageWeightGroup;
import org.webdev.kpoint.bl.pojo.Pickup;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.pojo.ui.CreditContainer;
import org.webdev.kpoint.bl.util.CalendarUtil;
import org.webdev.kpoint.bl.util.ConsumerCreditCalculator;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/Pickup.action")
public class PickupActionBean extends SecureActionBean {
	private static final Resolution SELECTCREDITS_RESOLUTION = new ForwardResolution("/WEB-INF/jsp/pickup_choosecredits.jsp");
	
	private String kinekNumber;
	private String firstName;
	private String lastName;
	private String state;
	private String transactionId;
	private BigDecimal totalReceivingFee = BigDecimal.ZERO;
	private BigDecimal totalTax = BigDecimal.ZERO;
	private BigDecimal totalConsumerCredits = BigDecimal.ZERO;
	private String selectedConsumer;
	private List<User> userSearchResults = new ArrayList<User>();
	private List<PackageReceipt> receiptsAvailableForPickup = new ArrayList<PackageReceipt>();
	//User for display purposes on step 3 (the success page)
	private List<PackageReceipt> filteredPackageReceiptsSelectedForPickup = new ArrayList<PackageReceipt>();
	private List<Package> packagesSelectedForPickup = new ArrayList<Package>();
	private List<ConsumerCredit> consumerCredits = new ArrayList<ConsumerCredit>();
	private String stateHidden;
	private int totalPackage = 0;
	private List<KinekPoint> userDepots;
	private Integer depotId;
	
	private boolean hasExtendedFees = false;

	@DefaultHandler @DontValidate
	public Resolution view() throws Exception {
		setUserDepots(new UserDao().fetchUserKinekPoints(getActiveUser().getUserId()));
		return UrlManager.getPickUpForm();
	}

	/** First tab ... search for and identify user that is picking up packages */
	
	/**
	 * Handles the validation for the user search event
	 */
	@ValidationMethod(on="userSearch")
	public void userSearchValidation(ValidationErrors errors) throws Exception {
		if((firstName == null || firstName.trim().length() == 0)
				&& (lastName == null || lastName.trim().length() == 0)
				&& (state == null || state.trim().length() == 0)
				&& (kinekNumber == null || kinekNumber.trim().length() == 0))
		{
			errors.add("nosearchcriteria", new SimpleError("You must enter at least one search criteria."));
		}
		
		List<KinekPoint> kinekP = new UserDao().fetchUserKinekPoints(getActiveUser().getUserId());
		
		if(depotId == null && new UserDao().fetchUserKinekPoints(getActiveUser().getUserId()).size() > 1){
			errors.add("depot", new SimpleError("You must select a depot."));
		}
		else if(depotId == null &&  kinekP.size() == 1){
			depotId = kinekP.get(0).getDepotId();
		}
		setUserDepots(new UserDao().fetchUserKinekPoints(getActiveUser().getUserId()));
	}
	
	/**
	 * Handles the user search event. This is responsible for searching for 
	 * kinek consumers based on the search criteria
	 * @return The search results page
	 */
	public Resolution userSearch() throws Exception {
		PackageReceiptDao packageReceiptDao = new PackageReceiptDao();
		UserDao userDao = new UserDao();
		
		List<User> potentialUsers = new ArrayList<User>();
		
		if(kinekNumber != null && kinekNumber.trim().length() != 0){
			//format kinek number before sending to backend
			//remove prefixes of KINEK or #
			potentialUsers = userDao.fetchConsumers(formatKinekNumber(kinekNumber));
		}
		else{
			int stateId = state == null || state == "" ? -1 : Integer.parseInt(state);
			
			potentialUsers = userDao.fetchConsumers(firstName, lastName, stateId);
		}
		
		userSearchResults = new ArrayList<User>();
		
		KinekPoint kp = new KinekPointDao().read(depotId);
		for(User user : potentialUsers) {
			if(packageReceiptDao.fetch(user.getKinekNumber(), kp).size() > 0) {
				userSearchResults.add(user);
			}
		}
		
		setUserDepots(new UserDao().fetchUserKinekPoints(getActiveUser().getUserId()));
		return UrlManager.getPickUpForm();
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
	
	/** Second tab ... display the packages that are available for the selected user */
	
	/**
	 * Displays the packages that are available for pickup
	 * @return The search results form
	 */
	public Resolution getPackagesForUser() throws Exception {
		retrievePackagesForUser();
		
		packagesSelectedForPickup = new ArrayList<Package>();
		for (PackageReceipt receipt : receiptsAvailableForPickup) {
			if(!receipt.getPackages().isEmpty()){
				receipt.populateExtendedStorageFeesForPackages();
			}
			for (Package packageObj : receipt.getPackages()) {
				if (packageObj.getPackageStatus(receipt) == ConfigurationManager.getPackageStatusInDepot()){
					packagesSelectedForPickup.add(packageObj);
					if(packageObj.getExtendedDurationFee().compareTo(BigDecimal.ZERO) == 1){
						hasExtendedFees = true;
					}
				}
			}
		}
		
		return UrlManager.getPickUpResults();	
	}
	
	private void retrievePackagesForUser() throws Exception {
		String userKinekNumber = selectedConsumer;  //this is the Kinek Number of the selected user
				
		receiptsAvailableForPickup = new ArrayList<PackageReceipt>();
		List<PackageReceipt> packageReceipts = new PackageReceiptDao().fetch(userKinekNumber, getKinekPoint());
		for (PackageReceipt receipt : packageReceipts) {
			if (receipt.getRedirectReason() == null)
				receiptsAvailableForPickup.add(receipt);
		} 
		calculateReceivingFee();
		calculateTotalTax();
		calculateTotalCredits();
	}


	/** Third tab ... pick is complete.  Process the pickup and show a confirmation page */
	
	/**
	 * Handles the complete pickup event.
	*/
	public Resolution completePickup() throws Exception {
		Integer pickupId = createPickupRecord();
				
		markCreditsAsRedeemed(pickupId);
		
		PackageReceiptDao receiptDao = new PackageReceiptDao();
		for (Package packageObj : packagesSelectedForPickup) {
			PackageReceipt receipt = receiptDao.read(packageObj);
			if (!filteredPackageReceiptsSelectedForPickup.contains(receipt)) {
				PackageReceipt filteredPackageReceipt = new PackageReceipt();
				filteredPackageReceipt.setId(receipt.getId());
				filteredPackageReceipt.setKinekPoint(receipt.getKinekPoint());
				filteredPackageReceipt.setReceivedDate(receipt.getReceivedDate());
				filteredPackageReceipt.setRedirectReason(receipt.getRedirectReason());
				filteredPackageReceipt.setPackages(new HashSet<Package>());
				
				filteredPackageReceiptsSelectedForPickup.add(filteredPackageReceipt);
			}
			
			filteredPackageReceiptsSelectedForPickup.get(filteredPackageReceiptsSelectedForPickup.indexOf(receipt)).getPackages().add(packageObj);
		}
		
		return UrlManager.getPickUpSummary();
	}
	
	private int createPickupRecord() throws Exception {
		UserDao userDao = new UserDao();
		User customer = userDao.readConsumer(selectedConsumer);
		Calendar current = new GregorianCalendar();
		PickupDao pickupDao = new PickupDao();
		
		Pickup pickup = new Pickup();
		pickup.setTransactionId(generatePickupTransactionId(getKinekPoint().getDepotId(), customer.getUserId(), current));
		pickup.setPickupDate(current.getTime());
		pickup.setKinekPoint(getKinekPoint());
		pickup.setConsumer(customer);
		pickup.setUserId(getActiveUser().getUserId());
		pickup.setApp(getActiveUser().getApp().toString());
		pickup.setPackages(new HashSet<Package>());
		
		applyReferralPromotion();
		
		pickup.getPackages().addAll(packagesSelectedForPickup);
		
		Integer pickupId = pickupDao.create(pickup);
		
		setFirstName(customer.getFirstName());
		setLastName(customer.getLastName());
		setKinekNumber(customer.getKinekNumber());
		
		return pickupId;
	}
	
	private String generatePickupTransactionId(int kinekPointId, int consumerId, Calendar current){
		String timePortion = String.valueOf(current.get(Calendar.MONTH) + 1) //this is because Jan is returned as 0
							+ String.valueOf(current.get(Calendar.DAY_OF_MONTH))
							+ String.valueOf(current.get(Calendar.HOUR_OF_DAY))
							+ String.valueOf(current.get(Calendar.MINUTE))
							+ String.valueOf(current.get(Calendar.SECOND));
		
		String kinekPointIdPortion = String.format("%05d", kinekPointId);
		String consumerIdPortion = String.format("%05d", consumerId);
		
		transactionId = consumerIdPortion+"-"+kinekPointIdPortion+"-"+timePortion;
		return transactionId;
	}
	
	private void applyReferralPromotion() throws Exception {
		UserDao userDao = new UserDao();
		User customer = userDao.readConsumer(selectedConsumer);
		ReferralPromotionManager manager = new ReferralPromotionManager(customer);
		manager.applyPromotion();		
	}
	
	private void markCreditsAsRedeemed(Integer pickupId) throws Exception {
		CreditStatus redeemed = new CreditStatusDao().read(ExternalSettingsManager.getCreditStatus_Redeemed());
		ConsumerCreditDao dao = new ConsumerCreditDao();
		for (ConsumerCredit credit : consumerCredits) {
			credit.setCreditStatus(redeemed);
			credit.setPickup(new Pickup(pickupId));
			dao.update(credit);
		}
	}
		
	/**
	 * Handles the choose credit event
	 */
	public Resolution chooseCredits() throws Exception {
		retrievePackagesForUser();
		
		return UrlManager.getPickUpResults();
	}
	
	private void calculateReceivingFee() {
		for (PackageReceipt receipt : receiptsAvailableForPickup) {
			for (Package packageObj : receipt.getPackages()) {
				if (packageObj.getPackageStatus(receipt) == ConfigurationManager.getPackageStatusInDepot())
					totalReceivingFee = totalReceivingFee.add(packageObj.getPickupFee());
			}
		}
	}
	
	private void calculateTotalTax() {
		for (PackageReceipt receipt : receiptsAvailableForPickup) {
			for (Package packageObj : receipt.getPackages()) {
				if (packageObj.getPackageStatus(receipt) == ConfigurationManager.getPackageStatusInDepot())
					totalTax = totalTax.add(packageObj.getDutyAndTax());
			}
		}
	}
	
	private void calculateTotalCredits() throws Exception {
		if (packagesSelectedForPickup.size() == 0) return;
		
		ConsumerCreditCalculator calculator = new ConsumerCreditCalculator(getKinekPoint());
		for (ConsumerCredit credit : consumerCredits) {
			totalConsumerCredits = totalConsumerCredits.add(calculator.getCreditValue(credit));
		}
		
		ensureCreditsDoNotExceedTotalFee();
	}
	
	private void ensureCreditsDoNotExceedTotalFee() {
		if (totalConsumerCredits.compareTo(totalReceivingFee) > 0)
			totalConsumerCredits = totalReceivingFee;
	}
	
	public Resolution selectCredits()
	{
		return SELECTCREDITS_RESOLUTION;
	}
	
	public String getKinekNumber() {
		return kinekNumber;
	}

	public void setKinekNumber(String kinekNumber) {
		this.kinekNumber = kinekNumber;
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
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSelectedConsumer() {
		return selectedConsumer;
	}

	public void setSelectedConsumer(String consumer) {
		this.selectedConsumer = consumer;
	}
	
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transId) {
		this.transactionId = transId;
	}

	public List<Country> getCountries() throws Exception {
		return new CountryDao().fetch();
	}
	
	public List<User> getUserSearchResults()
	{
		return userSearchResults;
	}
	
	public void setUserSearchResults(List<User> searchResults)
	{
		userSearchResults = searchResults;
	}
	
	public List<PackageReceipt> getReceiptsAvailableForPickup()
	{
		return receiptsAvailableForPickup;
	}
	
	public void setReceiptsAvailableForPickup(List<PackageReceipt> receiptsAvailableForPickup)
	{
		this.receiptsAvailableForPickup = receiptsAvailableForPickup;
	}

	public BigDecimal getTotalReceivingFee() {
		return totalReceivingFee;
	}

	/**
	 * Retrieves the total receiving fees for all packages currently selected.
	 * @return The total receiving fees for all packages currently selected
	 */
	public BigDecimal getTotalFees() {
		BigDecimal step1 = totalReceivingFee.add(totalTax); 
		return step1.subtract(totalConsumerCredits);
	}
	
	public BigDecimal getTotalCredits() {
		return totalConsumerCredits;
	}
	
	public boolean getConsumerCreditsExist() throws Exception {
		boolean indepotPackageExists = false;
		for (PackageReceipt receipt : receiptsAvailableForPickup) {
			for (Package packageObj : receipt.getPackages()) {
				if (packageObj.getPackageStatus(receipt) == ConfigurationManager.getPackageStatusInDepot())
					indepotPackageExists = true;
			}
		}
		
		if (indepotPackageExists)
		{
			UserDao userDao = new UserDao();
			User currentConsumer = userDao.readConsumer(selectedConsumer);
			
			CreditStatus available = new CreditStatusDao().read(ExternalSettingsManager.getCreditStatus_Available());
			List<ConsumerCredit> consumerCredits = new ConsumerCreditDao().fetch(currentConsumer, available);
			if(consumerCredits.size() > 0)
				return true;
		}
		return false;
	}
	
	public void setSelectedPackagesIds(List<Integer> packageIds) throws Exception 
	{
		packagesSelectedForPickup = new PackageDao().fetch(packageIds);
	}
	
	public List<Integer> getSelectedPackagesIds() {
		List<Integer> packageIds = new ArrayList<Integer>();
		for (Package pack : packagesSelectedForPickup) {
			packageIds.add(pack.getPackageId());
		}
		return packageIds;
	}
	
	public List<Package> getPackagesSelectedForPickup(){
		return packagesSelectedForPickup;
	}
	
	public int getTotalPackage(){
		totalPackage = packagesSelectedForPickup.size();
		return totalPackage;
	}
	
	public List<CreditContainer> getAvailableConsumerCredits() throws Exception {
		List<CreditContainer> credits = new ArrayList<CreditContainer>();
		ConsumerCreditCalculator calculator = new ConsumerCreditCalculator(getKinekPoint());
			
		UserDao userDao = new UserDao();
		User currentConsumer = userDao.readConsumer(selectedConsumer);
		
		CreditStatus available = new CreditStatusDao().read(ExternalSettingsManager.getCreditStatus_Available());
		List<ConsumerCredit> consumerCredits = new ConsumerCreditDao().fetch(currentConsumer, available);
		for (ConsumerCredit consumerCredit : consumerCredits) {
			CreditContainer container = new CreditContainer();
			container.setCredit(consumerCredit);
			container.setDollarValue(calculator.getCreditValue(consumerCredit));
			credits.add(container);
		}
		
		return credits;
	}
	
	public List<Integer> getConsumerCreditIds() {
		List<Integer> consumerCreditIds = new ArrayList<Integer>();
		for (ConsumerCredit credit : consumerCredits) {
			consumerCreditIds.add(credit.getId());
		}
		return consumerCreditIds;
	}
	
	/**
	 * This method retrieves the consumer credit ids and converts 
	 * them to credits objects.
	 * This checks to ensure the credit is assigned 
	 * to the current user before adding it to the list, preventing 
	 * people from modifying the POST and using other people's credits.
	 * @param consumerCreditIds
	 */
	public void setConsumerCreditIds(List<Integer> consumerCreditIds) throws Exception {
		List<ConsumerCredit> availableCredits = new ConsumerCreditDao().fetchByIds(consumerCreditIds);
		for (ConsumerCredit c : availableCredits) {
			if (creditIsForCurrentRecipient(c))
				consumerCredits.add(c);
		}
	}
	
	public String getStateHidden() {
		return stateHidden;
	}

	public void setStateHidden(String stateHidden) {
		this.stateHidden = stateHidden;
	}
	
	public List<PackageReceipt> getFilteredPackageReceiptsSelectedForPickup() {
		return filteredPackageReceiptsSelectedForPickup;
	}

	public void setFilteredPackageReceiptsSelectedForPickup(List<PackageReceipt> filteredPackageReceiptsSelectedForPickup) {
		this.filteredPackageReceiptsSelectedForPickup = filteredPackageReceiptsSelectedForPickup;
	}
	
	private boolean creditIsForCurrentRecipient(ConsumerCredit credit) throws Exception {
		UserDao userDao = new UserDao();		
		return credit.getUser().getUserId() == userDao.readConsumer(selectedConsumer).getUserId();
	}
	
	public KinekPoint getKinekPoint() throws Exception {
		UserDao userDao = new UserDao();
		List<KinekPoint> kinekPoints = userDao.fetchUserKinekPoints(getActiveUser().getUserId());
		if(kinekPoints.size() > 1){
			return new KinekPointDao().read(depotId);
		}
		else if(kinekPoints.size() == 1){
			return kinekPoints.get(0);
		}
		else{
			return null;
		}
	}

	public void setUserDepots(List<KinekPoint> userDepots) {
		this.userDepots = userDepots;
	}

	public List<KinekPoint> getUserDepots() {
		return userDepots;
	}
	
	public int getDepotId() {
		return depotId;
	}

	public void setDepotId(int depotId) {
		this.depotId = depotId;
	}

	public void setHasExtendedFees(boolean hasExtendedFees) {
		this.hasExtendedFees = hasExtendedFees;
	}

	public boolean isHasExtendedFees() {
		return hasExtendedFees;
	}
}