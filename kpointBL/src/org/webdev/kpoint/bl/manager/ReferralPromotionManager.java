package org.webdev.kpoint.bl.manager;

import java.util.Calendar;
import java.util.List;

import org.webdev.kpoint.bl.persistence.ConsumerCreditDao;
import org.webdev.kpoint.bl.persistence.CreditIssueReasonDao;
import org.webdev.kpoint.bl.persistence.CreditStatusDao;
import org.webdev.kpoint.bl.persistence.PackageReceiptDao;
import org.webdev.kpoint.bl.persistence.PromotionDao;
import org.webdev.kpoint.bl.persistence.ProspectDao;
import org.webdev.kpoint.bl.pojo.ConsumerCredit;
import org.webdev.kpoint.bl.pojo.Package;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.Promotion;
import org.webdev.kpoint.bl.pojo.Prospect;
import org.webdev.kpoint.bl.pojo.User;

public class ReferralPromotionManager {
	private User user;
	
	// Use getProspect() to retrieve the prospect
	private boolean prospectRead = false;
	private Prospect _prospect;
	
	// Use getPromotion() to retrieve the promotion
	private boolean promotionRead = false;
	private Promotion _promotion;
	
	public ReferralPromotionManager(User user)
	{
		this.user = user;
	}
	
	public void applyPromotion() throws Exception
	{
		if (userIsReferral() && usersFirstPackage())
		{
			applyCreditToReferrer();
		}
	}
	
	/**
	 * Determines if the provided user (the current customer) is a referral
	 * @return true if the user was referred by another customer; false otherwise
	 */
	private boolean userIsReferral() throws Exception
	{
		return new ProspectDao().read(user.getEmail()) != null;
	}
	
	/**
	 * Determines if the provided user has never picked up a package before.
	 * @return True if this is the 1st time the provided user has picked up a package; false otherwise 
	 */
	private boolean usersFirstPackage() throws Exception
	{
		List<PackageReceipt> packageReceipts = new PackageReceiptDao().fetch(user.getKinekNumber());
		boolean usersFirstPackage = true;
		for (PackageReceipt receipt : packageReceipts) {
			for (Package packageObj : receipt.getPackages()) {
				if (packageObj.getPackageStatus(receipt) == ConfigurationManager.getPackageStatusPickedUp()) {
					usersFirstPackage = false;
				}
			}
		}
		
		return usersFirstPackage;
	}
	
	private void applyCreditToReferrer() throws Exception
	{
		if (referralPromotionIsRunning())
		{
			ConsumerCredit credit = createReferralCredit();
			assignCreditToReferrer(credit);
			markProspectAsUsed(credit);
		}
	}
	
	private boolean referralPromotionIsRunning() throws Exception
	{		
		Promotion promotion=getPromotion();
		
		Calendar today = Calendar.getInstance();
		today.add(Calendar.HOUR, -today.get(Calendar.HOUR));
		today.clear(Calendar.MINUTE);
		today.clear(Calendar.SECOND);
		today.clear(Calendar.MILLISECOND);
		
		//1. check promotion is not null and Availability greater than zero 
		if(promotion!= null && promotion.getAvailabilityCount()>0){
			//2. check promotion start date must not be after today's date && check promotion end date must not be before today's date 
			if(!promotion.getStartDate().after(today.getTime()) && !promotion.getEndDate().before(today.getTime())){
				return true;
			}						
		}		
		return false;
	}
	
	private ConsumerCredit createReferralCredit() throws Exception
	{
		ConsumerCredit credit = new ConsumerCredit();
		credit.setUser(getProspect().getReferrer());
		credit.setIssueDate(Calendar.getInstance().getTime());
		credit.setPromotion(getPromotion());
		credit.setIssueReason(new CreditIssueReasonDao().read(ExternalSettingsManager.getCreditIssueReason_Referral()));
		credit.setCreditStatus(new CreditStatusDao().read(ExternalSettingsManager.getCreditStatus_Available()));
		return credit;
	}
	
	private void assignCreditToReferrer(ConsumerCredit credit) throws Exception
	{
		new ConsumerCreditDao().create(credit);
	}
	
	private void markProspectAsUsed(ConsumerCredit credit) throws Exception
	{
		Prospect prospect = getProspect();
		prospect.setCredit(credit);
		prospect.setCreditIssuedDate(Calendar.getInstance());
		new ProspectDao().update(prospect);
	}
	
	private Prospect getProspect() throws Exception
	{
		if (!prospectRead)
		{
			_prospect = new ProspectDao().read(user.getEmail());
			prospectRead = true;
		}
		return _prospect;
	}
	
	private Promotion getPromotion() throws Exception
	{
		if (!promotionRead)
		{
			_promotion = new PromotionDao().read(ExternalSettingsManager.getPromotionCode_Referral());
			promotionRead = true;
		}
		return _promotion;
	}
}
