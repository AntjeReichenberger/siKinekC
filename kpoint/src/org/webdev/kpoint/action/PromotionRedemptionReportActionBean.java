package org.webdev.kpoint.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.bl.persistence.ConsumerCreditDao;
import org.webdev.kpoint.bl.persistence.CreditIssueReasonDao;
import org.webdev.kpoint.bl.persistence.CreditStatusDao;
import org.webdev.kpoint.bl.persistence.KinekPointCreditDao;
import org.webdev.kpoint.bl.pojo.ConsumerCredit;
import org.webdev.kpoint.bl.pojo.CreditIssueReason;
import org.webdev.kpoint.bl.pojo.CreditStatus;
import org.webdev.kpoint.bl.pojo.KinekPointCredit;

@UrlBinding("/PromotionRedemptionReport.action")
public class PromotionRedemptionReportActionBean extends SecureActionBean {

	private List<ConsumerCredit> issuedConsumerCredits = new ArrayList<ConsumerCredit>();
	private List<KinekPointCredit> issuedKinekPointCredits = new ArrayList<KinekPointCredit>();
	private int issueReason;
	private boolean onlyRedeemed;
	private int onlyReferrals = 1;
	private int onlyRegistrations = 2;
	private Calendar minDate;
	private Calendar maxDate;
	private String creditReportType = "consumer";
	
	@DefaultHandler
    public Resolution view() throws Exception {
		issuedConsumerCredits = new ConsumerCreditDao().fetch();		
		return UrlManager.getPromotionRedemptionReport();
    }
	
	public Resolution search() throws Exception {
		
		this.prepareDates();
		
		CreditStatus redeemedStatus = new CreditStatus ();
		redeemedStatus = null;
		
		CreditIssueReason selectedReason = new CreditIssueReason();
		selectedReason = null;
			
		if(onlyRedeemed == true)
		{
			redeemedStatus = new CreditStatusDao().read(ExternalSettingsManager.getCreditStatus_Redeemed());	
		}
		
		if(issueReason == onlyReferrals)
		{
			selectedReason = new CreditIssueReasonDao().read(ExternalSettingsManager.getCreditIssueReason_Referral());
		}
		else if(issueReason == onlyRegistrations)
		{
			selectedReason = new CreditIssueReasonDao().read(ExternalSettingsManager.getCreditIssueReason_Registration());
		}
		
		if(creditReportType.equals("depot"))
		{
			fetchFilteredKinekPointCredits(selectedReason, redeemedStatus);
		}
		else if(creditReportType.equals("consumer"))
		{
			fetchFilteredConsumerCredits(selectedReason, redeemedStatus);
		}
			
        return UrlManager.getPromotionRedemptionReport();
    }
	
	/**
	 * Fetches the filtered credits for the consumer report 
	 */
	public void fetchFilteredConsumerCredits(CreditIssueReason selectedReason, CreditStatus redeemedStatus) throws Exception {
		issuedKinekPointCredits = null;
		issuedConsumerCredits = new ConsumerCreditDao().fetch(selectedReason, redeemedStatus);
	}
	
	/**
	 * Fetches the filtered credits for the depot report 
	 */
	public void fetchFilteredKinekPointCredits(CreditIssueReason selectedReason, CreditStatus redeemedStatus) throws Exception {
		issuedConsumerCredits = null;
		issuedKinekPointCredits = new KinekPointCreditDao().fetch(selectedReason, redeemedStatus);
	}
	
	/**
	 * Ensures dates used in the consumer report have times 
	 * set such that the dates are inclusive
	 */
	public void prepareDates() {
		// Ensure Min Date is set it to the very first second of the 
		// selected day ... to ensure it includes the entire day  
		if(minDate != null)
		{
			minDate.set(Calendar.HOUR, 00);
			minDate.set(Calendar.MINUTE, 00);
			minDate.set(Calendar.SECOND, 00);
		}

		// Ensure max Date is set it to the very last second of the 
		// selected day ... to ensure it includes the entire day
		if(maxDate != null)
		{
			maxDate.set(Calendar.HOUR, 23);
			maxDate.set(Calendar.MINUTE, 59);
			maxDate.set(Calendar.SECOND, 59);
		}
	}
	
	public List<ConsumerCredit> getIssuedConsumerCredits() {
		return issuedConsumerCredits;
	}
	
	public void setIssuedConsumerCredits(List<ConsumerCredit> issuedConsumerCredits) {
		this.issuedConsumerCredits = issuedConsumerCredits;
	}
	
	public List<KinekPointCredit> getIssuedKinekPointCredits() {
		return issuedKinekPointCredits;
	}
	
	public void setIssuedKinekPointCredits(List<KinekPointCredit> issuedKinekPointCredits) {
		this.issuedKinekPointCredits = issuedKinekPointCredits;
	}
	
	public int getIssueReason() {
		return issueReason;
	}
	
	public void setIssueReason(int issueReason) {
		this.issueReason = issueReason;
	}
	
	public boolean getOnlyRedeemed() {
		return onlyRedeemed;
	}
	
	public void setOnlyRedeemed(boolean onlyRedeemed) {
		this.onlyRedeemed = onlyRedeemed;
	}
	
	public Calendar getMinDate() {
		return minDate;
	}
	
	public void setMinDate(Calendar minDate) {
		this.minDate = minDate;
	}
	
	public Calendar getMaxDate() {
		return maxDate;
	}
	
	public void setMaxDate(Calendar maxDate) {
		this.maxDate = maxDate;
	}
	
	public String getCreditReportType()
	{
		return creditReportType;
	}

	public void setCreditReportType(String creditReportType)
	{
		this.creditReportType = creditReportType;
	}
	
	public int getListLength() {
		if (issuedConsumerCredits != null)
			return issuedConsumerCredits.size();
		if (issuedKinekPointCredits != null)
			return issuedKinekPointCredits.size();
		return 0;
	}
}