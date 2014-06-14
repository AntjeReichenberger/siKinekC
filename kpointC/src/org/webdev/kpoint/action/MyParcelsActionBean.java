package org.webdev.kpoint.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.bl.persistence.PackageDao;
import org.webdev.kpoint.bl.persistence.PackageReceiptDao;
import org.webdev.kpoint.bl.persistence.PickupDao;
import org.webdev.kpoint.bl.pojo.Package;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.Page;
import org.webdev.kpoint.bl.pojo.Pickup;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/MyParcels.action")
public class MyParcelsActionBean extends AccountDashboardActionBean {
	private User user;
	private String packageStatus;
	private String criteria;
	private List<Package> packages;
	private List<PackageReceipt> filteredPackageReceipts;
	private int packageId;
	
	/* Paging data elements */
	private Page<Package> page; //current page of data
	private int currentPage; //current page number
	private int pageSize = ConfigurationManager.getMyParcelsPageSize(); //number of records per page
	private int resultStartIndex; //current resultset start index
	private int resultEndIndex; //current resultset end index
	private int totalRecords; //total number of results (across all pages)
	
	@DefaultHandler
	public Resolution view() throws Exception {
		if (packageStatus == null || packageStatus.isEmpty()) {
			packageStatus = ConfigurationManager.getPackageStatusInDepot();
		}
		fetchParcels(0);
		
		return UrlManager.getMyParcels();
	}

	public Resolution fetchParcels(int pageNumber) throws Exception {
		setupPageWithFilters(pageNumber);
		
		return UrlManager.getMyParcels();
	}
	
	/**
	 * Fetches the current page of results to display and sets
	 * up the required paging variables.  This method is used for
	 * fetching and displaying results when the Package Status
	 * filter is applied.
	 * @param pageNumber
	 */
	private void setupPageWithFilters(int pageNumber) throws Exception {
		user = getActiveUser();
		List<PackageReceipt> packageReceipts = new PackageReceiptDao().fetch(user.getKinekNumber());

		int firstPackage = pageNumber * getPageSize();
		int numOfPackagesLeft = getPageSize();
		int packageNumber = 0;
		filteredPackageReceipts = new ArrayList<PackageReceipt>();
		for (PackageReceipt packageReceipt : packageReceipts) {
			PackageReceipt filteredPackageReceipt = new PackageReceipt();
			filteredPackageReceipt.setKinekPoint(packageReceipt.getKinekPoint());
			filteredPackageReceipt.setReceivedDate(packageReceipt.getReceivedDate());
			filteredPackageReceipt.setRedirectReason(packageReceipt.getRedirectReason());
			filteredPackageReceipt.setPackages(new HashSet<Package>());
			
			for (Package packageObj : packageReceipt.getPackages()) {
				String statusName = packageObj.getPackageStatus(packageReceipt);
				if (statusName.equals(packageStatus)) {
					if (packageNumber >= firstPackage && packageBelongsToCustomer(packageObj, packageReceipt)) {
						filteredPackageReceipt.getPackages().add(packageObj);
						numOfPackagesLeft--;
					}
					packageNumber++;
				}
				if (numOfPackagesLeft == 0) {
					break;
				}
			}
			
			filteredPackageReceipts.add(filteredPackageReceipt);
			if (numOfPackagesLeft == 0) {
				break;
			}
		}
		
		setCurrentPage(pageNumber);
		setSessionAttribute("currentPage", String.valueOf(pageNumber));
		
		//totalRecords = getTotalRecords();
		for (PackageReceipt receipt : packageReceipts) {
			for (Package packageObj : receipt.getPackages()) {
				if ((packageStatus.equals("") || packageObj.getPackageStatus(receipt).equals(packageStatus)) && packageBelongsToCustomer(packageObj, receipt)) {
					totalRecords ++;
				}
			}
		}
		//totalRecords = packageNumber;
		
		if(totalRecords == 0){
			resultStartIndex = 0;	
		}
		else{
			resultStartIndex = (pageNumber * pageSize) + 1;
		}
		resultEndIndex = (pageNumber * pageSize) + pageSize <= totalRecords ? (pageNumber * pageSize) + pageSize : totalRecords;
	}
	
	private boolean packageBelongsToCustomer(Package packageObj, PackageReceipt packageReceipt) throws Exception {
		if (packageObj.getPackageStatus(packageReceipt) == ConfigurationManager.getPackageStatusPickedUp()) {
			Pickup pickup = new PickupDao().read(packageObj);
			if (!pickup.getConsumer().getKinekNumber().equals(getActiveUser().getKinekNumber())) {
				return false;
			}
		}
		
		return true;
	}
	
	public Resolution nextPage() throws Exception {
		int currPage = Integer.valueOf((String)getSessionAttribute("currentPage")).intValue();
		fetchParcels(currPage + 1);
		return UrlManager.getMyParcels();
	}
	
	public Resolution previousPage() throws Exception {
		int currPage = Integer.valueOf((String)getSessionAttribute("currentPage")).intValue();
		fetchParcels(currPage - 1);
		return UrlManager.getMyParcels();
	}

	public Resolution firstPage() throws Exception {
		fetchParcels(0);
		return UrlManager.getMyParcels();
	}
	
	public Resolution lastPage() throws Exception {
		user = getActiveUser();
		
		List<PackageReceipt> packageReceipts = new PackageReceiptDao().fetch(user.getKinekNumber());
		int numberOfPackages = 0;
		for (PackageReceipt receipt : packageReceipts) {
			for (Package packageObj : receipt.getPackages()) {
				if ((packageStatus.equals("") || packageObj.getPackageStatus(receipt).equals(packageStatus)) && packageBelongsToCustomer(packageObj, receipt)) {
					numberOfPackages ++;
				}
			}
		}
		int lastPage = (int)Math.floor(numberOfPackages / this.pageSize);
		if (numberOfPackages % this.pageSize == 0)
			lastPage--;
		fetchParcels(lastPage);
		
		return UrlManager.getMyParcels();
	}
	
	public Resolution packageAlreadyPickedUp() throws Exception {
		PackageDao packageDao = new PackageDao();
		PackageReceiptDao packageReceiptDao = new PackageReceiptDao();
		PickupDao pickupDao = new PickupDao();
		
		Package tempPackage = packageDao.read(packageId);
		PackageReceipt receipt = packageReceiptDao.read(tempPackage);
		
		Calendar current = new GregorianCalendar();
		
		Pickup pickup = new Pickup();
		pickup.setConsumer(getActiveUser());
		pickup.setKinekPoint(receipt.getKinekPoint());
		pickup.setPackages(new HashSet<Package>());
		pickup.getPackages().add(tempPackage);
		pickup.setPickupDate(current.getTime());
		pickup.setTransactionId(generatePickupTransactionId(getActiveUser().getKinekPoint().getDepotId(), getActiveUser().getUserId(), current));
		pickup.setUserId(getActiveUser().getUserId());
		pickup.setApp(getActiveUser().getApp().toString());
		
		pickupDao.create(pickup);
		
		return fetchParcels(0);
	}
	
	private String generatePickupTransactionId(int kinekPointId, int consumerId, Calendar current){
		String timePortion = String.valueOf(current.get(Calendar.MONTH) + 1) //this is because Jan is returned as 0
							+ String.valueOf(current.get(Calendar.DAY_OF_MONTH))
							+ String.valueOf(current.get(Calendar.HOUR_OF_DAY))
							+ String.valueOf(current.get(Calendar.MINUTE))
							+ String.valueOf(current.get(Calendar.SECOND));
		
		String kinekPointIdPortion = String.format("%05d", kinekPointId);
		String consumerIdPortion = String.format("%05d", consumerId);
		
		String transactionId = consumerIdPortion + "-" + kinekPointIdPortion + "-" + timePortion;
		return transactionId;
	}
	
	public String getPackageStatus() {
		return packageStatus;
	}

	public void setPackageStatus(String packageStatus) {
		this.packageStatus = packageStatus;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public List<Package> getPackages() {
		return packages;
	}

	public void setPackages(List<Package> packages) {
		this.packages = packages;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Used to determine where to return to if an action is taken that leads you from this page
	 * @return This class name
	 */
	public String getReferrer() {
		return this.getClass().getName();
	}
	
	public int getResultStartIndex() {
		return resultStartIndex;
	}

	public void setResultStartIndex(int resultStartIndex) {
		this.resultStartIndex = resultStartIndex;
	}

	public int getResultEndIndex() {
		return resultEndIndex;
	}

	public void setResultEndIndex(int resultEndIndex) {
		this.resultEndIndex = resultEndIndex;
	}

	public int getTotalRecords() throws Exception {
		return totalRecords;
		/**List<PackageReceipt> packageReceipts = new PackageReceiptDao().fetch(user.getKinekNumber());
		int numberOfPackages = 0;
		for (PackageReceipt receipt : packageReceipts) {
			for (Package packageObj : receipt.getPackages()) {
				if ((packageStatus.equals("") || packageObj.getPackageStatus(receipt).equals(packageStatus)) && packageBelongsToCustomer(packageObj, receipt)) {
					numberOfPackages ++;
				}
			}
		}
		return numberOfPackages;*/
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public Page<Package> getPage() {
		return page;
	}
	
	public List<PackageReceipt> getFilteredPackageReceipts() {
		return filteredPackageReceipts;
	}
	
	public void setFilteredPackageReceipts(List<PackageReceipt> filteredPackageReceipts) {
		this.filteredPackageReceipts = filteredPackageReceipts;
	}
	
	public int getPackageId() {
		return packageId;
	}
	
	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}
}
