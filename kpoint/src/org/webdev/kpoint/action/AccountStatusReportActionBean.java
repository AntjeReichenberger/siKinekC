package org.webdev.kpoint.action;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.persistence.InvoiceDao;
import org.webdev.kpoint.bl.persistence.InvoiceIssueHistoryDao;
import org.webdev.kpoint.bl.persistence.KinekPointCreditDao;
import org.webdev.kpoint.bl.persistence.PackageReceiptDao;
import org.webdev.kpoint.bl.persistence.PaymentDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.CreditCalculationType;
import org.webdev.kpoint.bl.pojo.Invoice;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.KinekPointCredit;
import org.webdev.kpoint.bl.pojo.Package;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.Payment;
import org.webdev.kpoint.bl.pojo.ui.InvoiceUI;
import org.webdev.kpoint.bl.util.ApplicationProperty;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/AccountStatusReport.action")
public class AccountStatusReportActionBean extends SecureActionBean {
	
	//Filter fields
	private String month1;
	private String month2;
	private String year1;
	private String year2;
	
	//Totals fields
	private int packagesReceivedSum;
	private int packagesPickedUpSum;
	private BigDecimal invoiceAmountSum;
	private BigDecimal amountOwingSum;
	private BigDecimal promotionalDiscountsSum;
	
	private int depotId;
	private List<KinekPoint> userDepots;
	
	//Invoice list
	List<InvoiceUI> invoices = new ArrayList<InvoiceUI>();

	@DefaultHandler
	public Resolution view() throws Exception
	{
		performSearch();
		return UrlManager.getAccountStatusReport();
	}
	
	@ValidationMethod(on="search")
	public void formValidate(ValidationErrors errors) {
		if(month1 != null && year1 == null)
			errors.add("month1", new SimpleError("A start year must be entered if a start month is selected."));
		
		if(month2 != null && year2 == null)
			errors.add("month2", new SimpleError("An end year must be entered if an end month is selected."));
		
		if(year1 != null && month1 == null)
			errors.add("year1", new SimpleError("A start month must be selected if a start year is entered."));
		
		if(year2 != null && month2 == null)
			errors.add("year2", new SimpleError("An end month must be selected if an end year is entered."));
	}
	
	public Resolution search() throws Exception {		
		performSearch();
		return UrlManager.getAccountStatusReport();
	}
	
	private void performSearch() throws Exception {		
		
		if(getActiveUser().getDepotAdminAccessCheck()){
			//----Fetch kinekpoints that are mapped to the depotAdmin
			UserDao userDao = new UserDao();
			int userId = getActiveUser().getUserId();
			userDepots = new ArrayList<KinekPoint>(userDao.fetchUserKinekPoints(userId)); 
			//------------------------------------------------------------
		}else{
			userDepots = new ArrayList<KinekPoint>();
		}
		
		int startMonth = month1 == null ? 0 : Integer.parseInt(month1);
		int endMonth = month2 == null ? 0 :  Integer.parseInt(month2);
		int startYear = year1 == null ? 0 :  Integer.parseInt(year1);
		int endYear = year2 == null ? 0 :  Integer.parseInt(year2);
		
		List<Invoice> invs = new ArrayList<Invoice>();
		if(depotId < 1){
			for (KinekPoint kp: userDepots){
				List<Invoice> depotPart = new InvoiceDao().fetch(kp.getDepotId(), 0, null, startMonth, startYear, endMonth, endYear);	
				invs.addAll(depotPart);	
			}
		}
		else{
			invs = new InvoiceDao().fetch(depotId, 0, null, startMonth, startYear, endMonth, endYear);
		}
		
		//Search for invoices

		for (Invoice inv: invs) {
			InvoiceUI invoice = new InvoiceUI(inv);
			
			//Find last issued date
			invoice.setLastIssuedDate(new InvoiceIssueHistoryDao().getLastIssuedDate(inv));
			
			//Convert times to Calendar objects
			GregorianCalendar startDate = new GregorianCalendar();
			startDate.setTime(invoice.getStartDate());
			GregorianCalendar endDate = new GregorianCalendar();
			endDate.setTime(invoice.getEndDate());
			
			//Get package info
			int packagesReceived = 0;
			int packagesPickedUp = 0;
			List <PackageReceipt> packageReceipts = new PackageReceiptDao().fetch(inv.getKinekPoint().getDepotId(), startDate, endDate);
			for (PackageReceipt receipt : packageReceipts) {
				packagesReceived += receipt.getPackages().size();
			}
			packageReceipts = new PackageReceiptDao().fetch(inv.getKinekPoint());
			for (PackageReceipt receipt : packageReceipts) {
				for (Package packageObj : receipt.getPackages()) {
					Calendar pickupDate = packageObj.getPickupDate();
					if (pickupDate != null && pickupDate.compareTo(startDate) >= 0 && pickupDate.compareTo(endDate) <= 0) {
						packagesPickedUp++;
					}
				}
			}
			
			invoice.setPackagesReceived(packagesReceived);
			invoice.setPackagesPickedUp(packagesPickedUp);
			
			//Get payment info
			BigDecimal amountOwing = invoice.getAmountDue();
			BigDecimal amountPaid = new BigDecimal(0);
			
			List<Payment> payments = new PaymentDao().fetch(invoice.getInvoiceNumber());
			
			if(payments == null || payments.size() == 0) {
				//if no payments have been made and amount due > 0, set status to issued
				if(invoice.getAmountDue().compareTo(new BigDecimal(0)) > 0)
					invoice.setStatus(ApplicationProperty.getInstance().getProperty("invoice.status.issued"));
				//if amount due is 0, set status to no charges
				else {
					invoice.setStatus(ApplicationProperty.getInstance().getProperty("invoice.status.noCharges"));
				}
			}
			
			for(Payment payment: payments) {
				if(payment.getStatus().getId() == ExternalSettingsManager.getPaymentStatusIdApproved()) {
					//if an approved payment, update amounts and set status accordingly
					amountOwing = amountOwing.subtract(payment.getAmount());
					amountPaid = amountPaid.add(payment.getAmount());
					invoice.setPaymentDate(payment.getPaymentDate());
					invoice.setStatus(ApplicationProperty.getInstance().getProperty("invoice.status.paid"));
				}
				else {
					//if a declined payment, set status accordingly
					invoice.setPaymentDate(payment.getPaymentDate());
					invoice.setStatus(ApplicationProperty.getInstance().getProperty("invoice.status.declined"));
				}
			}
			invoice.setAmountOwing(amountOwing);
			invoice.setAmountPaid(amountPaid);
			
			//Get promotions used on this invoice
			BigDecimal discounts = new BigDecimal(0);
			BigDecimal kinekFee = ConfigurationManager.getKinekFee();

			List<KinekPointCredit> credits = new KinekPointCreditDao().fetch(invoice.getInvoiceNumber());
			for(KinekPointCredit credit: credits) {
				BigDecimal amount = credit.getPromotion().getDepotCreditAmount();
				CreditCalculationType type = credit.getPromotion().getDepotCreditCalcType();
				
				if(type.getName().equals(ApplicationProperty.getInstance().getProperty("credit.calculation.type.percentage"))) {
					discounts = discounts.add(kinekFee.multiply(amount).divide(new BigDecimal(100)));
				}
				else {
					discounts = discounts.add(amount);
				}
			}
			invoice.setDiscountKinekTotal(discounts);
			
			invoices.add(invoice);
		}
		calculateTotals();
	}
	
	public int getDepotId() {
		return depotId;
	}

	public void setDepotId(int depotId) {
		this.depotId = depotId;
	}

	/**
	 * Calculates the total column totals
	 */
	private void calculateTotals() {
		packagesReceivedSum = 0;
		packagesPickedUpSum = 0;
		invoiceAmountSum = new BigDecimal(0);
		amountOwingSum = new BigDecimal(0);
		promotionalDiscountsSum = new BigDecimal(0);
		
		for (InvoiceUI invoice: invoices) {
			packagesReceivedSum += invoice.getPackagesReceived();
			packagesPickedUpSum += invoice.getPackagesPickedUp();
			invoiceAmountSum = invoiceAmountSum.add(invoice.getAmountDue());
			amountOwingSum = amountOwingSum.add(invoice.getAmountOwing());
			promotionalDiscountsSum = promotionalDiscountsSum.add(invoice.getDiscountKinekTotal());
		}
	}
	
	public boolean getInvoicesFound() {
		return invoices.size() > 0;
	}

	public String getMonth1() {
		return month1;
	}

	public void setMonth1(String month1) {
		this.month1 = month1;
	}

	public String getMonth2() {
		return month2;
	}

	public void setMonth2(String month2) {
		this.month2 = month2;
	}

	public String getYear1() {
		return year1;
	}

	public void setYear1(String year1) {
		this.year1 = year1;
	}

	public String getYear2() {
		return year2;
	}

	public void setYear2(String year2) {
		this.year2 = year2;
	}

	public BigDecimal getInvoiceAmountSum() {
		return invoiceAmountSum;
	}

	public void setInvoiceAmountSum(BigDecimal invoiceAmountSum) {
		this.invoiceAmountSum = invoiceAmountSum;
	}

	public BigDecimal getAmountOwingSum() {
		return amountOwingSum;
	}

	public void setAmountOwingSum(BigDecimal amountOwingSum) {
		this.amountOwingSum = amountOwingSum;
	}

	public List<InvoiceUI> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<InvoiceUI> invoices) {
		this.invoices = invoices;
	}

	public int getPackagesReceivedSum() {
		return packagesReceivedSum;
	}

	public void setPackagesReceivedSum(int packagesReceivedSum) {
		this.packagesReceivedSum = packagesReceivedSum;
	}

	public int getPackagesPickedUpSum() {
		return packagesPickedUpSum;
	}

	public void setPackagesPickedUpSum(int packagesPickedUpSum) {
		this.packagesPickedUpSum = packagesPickedUpSum;
	}

	public BigDecimal getPromotionalDiscountsSum() {
		return promotionalDiscountsSum;
	}

	public void setPromotionalDiscountsSum(BigDecimal promotionalDiscountsSum) {
		this.promotionalDiscountsSum = promotionalDiscountsSum;
	}
	
	public int getListLength() {
		return invoices.size();
	}
	
	public int getSelectedDepotId(){
		return depotId;
	}
	public void setSelectedDepotId(int depotId){
		this.depotId = depotId;
	}
	public List<KinekPoint> getUserDepots(){
		return userDepots;
	}
	public void setUserDepots(List<KinekPoint> userDepots){
		this.userDepots = userDepots;
	}
}
