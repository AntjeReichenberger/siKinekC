package org.webdev.kpoint.bl.pojo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.joda.time.Interval;
import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.bl.persistence.KPExtendedStorageRateDao;

public class PackageReceipt implements Comparable<PackageReceipt>{
	private int id = -1;
	private String transactionId;
	private KinekPoint kinekPoint;
	private Set<Package> packages;
	private Set<User> packageRecipients;
	private RedirectReason redirectReason;
	private Date receivedDate;
	private String receivedDateStr;
	private String redirectLocation;
	private Date lastEmailDate;
	private boolean requiresProofOfPurchase = false;
	private int userId;
	private String app;
	
	public int getId() {
		return id;
	}
	
	public void setId(int i) {
		id = i;
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	public KinekPoint getKinekPoint() {
		return kinekPoint;
	}
	
	public void setKinekPoint(KinekPoint d) {
		this.kinekPoint = d;
	}
	
	public Set<Package> getPackages() {
		return packages;
	}
	
	public void setPackages(Set<Package> packages) {
		this.packages = packages;
	}
	
	public void populateExtendedStorageFeesForPackages()throws Exception {
		int days = calculateDays();
		
		//Packages are populated with fees as they are retrieved from a receipt
		KPExtendedStorageRateDao kpExtendedStorageRateDao = new KPExtendedStorageRateDao();
		
		for(Package packageElement: packages){
			if (packageElement.getPackageStatus(this) == ConfigurationManager.getPackageStatusInDepot()){
				if(!packageElement.getOnSkid()){
					packageElement.setSkidFee(new BigDecimal(0));
				}
				List<KPExtendedStorageRate> relevantExtended = kpExtendedStorageRateDao.fetch(kinekPoint, receivedDate, packageElement.getPackageWeightGroup(), days);
				//Sorted list
				BigDecimal result = new BigDecimal(0);
				for(int i = 0; i < relevantExtended.size(); i++){
					StorageDuration currentDuration = relevantExtended.get(i).getUnifiedExtendedStorageRate().getStorageDuration();
					int interval;
					if(i == relevantExtended.size()-1){
						interval = days - currentDuration.getMinDays() + 1;
					}
					else{
						interval = currentDuration.getMaxDays() - currentDuration.getMinDays();
					}
					relevantExtended.get(i).populateActualFee();
					result = result.add(relevantExtended.get(i).getActualFee().multiply(new BigDecimal(interval)));
				}
				packageElement.setExtendedDurationFee(result);
			}
		}
	}
	
	private int calculateDays(){
		Calendar arrived = Calendar.getInstance();
		arrived.setTime(this.getReceivedDate());

		//zero out hours minutes and seconds 
		arrived.set(Calendar.HOUR_OF_DAY , 0);
		arrived.set(Calendar.MINUTE, 0);
		arrived.set(Calendar.SECOND, 0);
		
		Interval interval = new Interval(arrived.getTime().getTime(), Calendar.getInstance().getTime().getTime());
		return interval.toDuration().toStandardSeconds().toStandardDays().getDays();
	}
	
	public Set<User> getPackageRecipients() {
		return packageRecipients;
	}
	
	public void setPackageRecipients(Set<User> packageRecipients) {
		this.packageRecipients = packageRecipients;
	}
	
	public Date getReceivedDate() {
		return receivedDate;
	}
		
	public void setReceivedDate(Date d) {
		this.receivedDate = d;
	}
	
	public void setReceivedDateStr(String s){
		this.receivedDateStr = s;
	}
	public String getReceivedDateStr(){
		if(receivedDate != null){
			String DATE_FORMAT = "MMMM dd,yyyy";
		    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		    receivedDateStr = sdf.format(receivedDate);		
		}		
		return receivedDateStr;
	}
	
	public String getRedirectLocation() {
		return redirectLocation;
	}
	
	public void setRedirectLocation(String s) {
		this.redirectLocation = s;
	}

	public RedirectReason getRedirectReason() {
		return redirectReason;
	}
	
	public void setRedirectReason(RedirectReason r) {
		this.redirectReason = r;
	}

	/**
	 * Retrieves the date the last email regarding this package was sent to a customer.
	 * This includes remainder emails, accept delivery emails, redirect emails, etc.
	 * @return The date the last email regarding this package was sent to a customer
	 */
	public Date getLastEmailDate() {
		return lastEmailDate;
	}

	/**
	 * Set's the date of the last email regarding this package was sent to it's associated customer.
	 * This includes remainder emails, accept delivery emails, redirect emails, etc.
	 * @param lastEmailDate The date of the last email sent to the customer.
	 */
	public void setLastEmailDate(Date lastEmailDate) {
		this.lastEmailDate = lastEmailDate;
	}

	public void setRequiresProofOfPurchase(boolean requiresProofOfPurchase) {
		this.requiresProofOfPurchase = requiresProofOfPurchase;
	}

	public boolean isRequiresProofOfPurchase() {
		return requiresProofOfPurchase;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getApp() {
		return app;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) return true;
		
		if ( !(o instanceof PackageReceipt) ) return false;
		
		PackageReceipt packageObj = (PackageReceipt)o;
		return this.getId() == packageObj.getId();
	}

	@Override
	public int compareTo(PackageReceipt o) {
		return o.getReceivedDate().compareTo(receivedDate);
	}
}
