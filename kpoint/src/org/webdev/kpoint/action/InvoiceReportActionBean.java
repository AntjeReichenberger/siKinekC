package org.webdev.kpoint.action;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.persistence.InvoiceDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.PackageReceiptDao;
import org.webdev.kpoint.bl.persistence.PaymentDao;
import org.webdev.kpoint.bl.persistence.StateDao;
import org.webdev.kpoint.bl.pojo.Invoice;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Package;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.Payment;
import org.webdev.kpoint.bl.pojo.State;
import org.webdev.kpoint.bl.pojo.ui.InvoiceUI;
import org.webdev.kpoint.bl.util.ApplicationProperty;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/InvoiceReport.action")
public class InvoiceReportActionBean extends SecureActionBean {
	
	//Filter fields
	private int depotId;
	private int stateId;
	private String status;
	private String city;
	private String month1;
	private String month2;
	private String year1;
	private String year2;
	
	//Totals fields
	private int packagesReceivedSum;
	private int packagesPickedUpSum;
	private BigDecimal amountPaidSum;
	private BigDecimal amountOwingSum;
	
	//Invoice list
	List<InvoiceUI> invoices = new ArrayList<InvoiceUI>();	

	@DontValidate @DefaultHandler
	public Resolution view() throws Exception {
		performSearch();
		return UrlManager.getInvoiceReport();
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
		return UrlManager.getInvoiceReport();
	}
	
	/**
	 * Performs a search based on the entered filters
	 */
	private void performSearch() throws Exception  {
		int startMonth = month1 == null ? 0 : Integer.parseInt(month1);
		int endMonth = month2 == null ? 0 :  Integer.parseInt(month2);
		int startYear = year1 == null ? 0 :  Integer.parseInt(year1);
		int endYear = year2 == null ? 0 :  Integer.parseInt(year2);
		
		//Search for invoices
		List<Invoice> invs = null;
		
		//If status is issued or not specified, date range search is on start date
		if(status == null || status.equals(ApplicationProperty.getInstance().getProperty("invoice.status.issued"))) {
			invs = new InvoiceDao().fetch(depotId, stateId, city, startMonth, startYear, endMonth, endYear);
		}
		//Otherwise, date range search is on payment date
		else {
			invs = new InvoiceDao().fetch(depotId, stateId, city, 0, 0, 0, 0);
		}
		
		for(Invoice inv : invs) {
			InvoiceUI invoice = new InvoiceUI(inv);
			
			GregorianCalendar startDate = new GregorianCalendar();
			startDate.setTime(invoice.getStartDate());
			GregorianCalendar endDate = new GregorianCalendar();
			endDate.setTime(invoice.getEndDate());
			
			//Get package info
			int packagesReceived = 0;
			int packagesPickedUp = 0;
			List<PackageReceipt> packageReceipts = new PackageReceiptDao().fetch(inv.getKinekPoint().getDepotId(), startDate, endDate);
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
			
			invoices.add(invoice);
		}
		filterInvoices();
		calculateTotals();
	}
	
	/**
	 * This method filters the list of invoices based on their statuses
	 */
	private void filterInvoices() {
		List<InvoiceUI> filtered = new ArrayList<InvoiceUI>();
		
		if(status == null) {
			//Don't do any filtering
			return;
		}
		
		else if(status.equals(ApplicationProperty.getInstance().getProperty("invoice.status.issued"))
				|| status.equals(ApplicationProperty.getInstance().getProperty("invoice.status.noCharges"))) {
			//Go through invoices, keeping those that have the correct status
			for(InvoiceUI invoice: invoices) {
				if(invoice.getStatus().equals(status))
				{
					filtered.add(invoice);
				}
			}
		}
		
		else {		
			Date startDate;
			Date endDate;
			
			//Set start date to either the first day of the month selected or an arbitrary past date
			if(month1 == null) {
				GregorianCalendar temp = new GregorianCalendar(2000, 1, 1);
				startDate = temp.getTime();
			}
			else {
				GregorianCalendar temp = new GregorianCalendar(Integer.parseInt(year1), Integer.parseInt(month1)-1, 1);
				startDate = temp.getTime();
			}
			
			//Set end date to either the first day of the month after the selected month or to the current date
			if(month2 == null) {
				endDate = new Date();
			}
			else {
				GregorianCalendar temp = new GregorianCalendar(Integer.parseInt(year2), Integer.parseInt(month2)-1, 1);
				temp.add(Calendar.MONTH, 1);
				endDate = temp.getTime();
			}
			
			//Go through invoices, keeping those that fall within the bounds and have the correct status
			for(InvoiceUI invoice: invoices) {
				
				if( invoice.getPaymentDate() != null && 
					invoice.getPaymentDate().compareTo(endDate) < 0 &&
					invoice.getPaymentDate().compareTo(startDate) >= 0 &&
					invoice.getStatus().equals(status)) 
				{
					filtered.add(invoice);
				}
			}
		}
		//Set invoices to new list
		invoices = filtered;
	}
	
	/**
	 * Calculates the total column totals
	 */
	private void calculateTotals() {
		packagesReceivedSum = 0;
		packagesPickedUpSum = 0;
		amountPaidSum = new BigDecimal(0);
		amountOwingSum = new BigDecimal(0);
		
		for (InvoiceUI invoice: invoices) {
			packagesReceivedSum += invoice.getPackagesReceived();
			packagesPickedUpSum += invoice.getPackagesPickedUp();
			amountPaidSum = amountPaidSum.add(invoice.getAmountPaid());
			amountOwingSum = amountOwingSum.add(invoice.getAmountOwing());
		}
	}
	
	/**
	 * Fetches the list of depots for the drop down
	 * @return The list of depots
	 */
	public List<KinekPoint> getDepots() throws Exception  {
		return new KinekPointDao().fetch();
	}
	
	/**
	 * Fetches the list of states for the drop down
	 * @return The list of states
	 */
	public List<State> getStates() throws Exception  {
		return new StateDao().fetch();
	}
	
	/**
	 * Fetches the list of statuses for the drop down
	 * @return The list of statuses
	 */
	public List<String> getStatuses() {
		List<String> statuses = new ArrayList<String>();
		statuses.add(ApplicationProperty.getInstance().getProperty("invoice.status.issued"));
		statuses.add(ApplicationProperty.getInstance().getProperty("invoice.status.paid"));
		statuses.add(ApplicationProperty.getInstance().getProperty("invoice.status.declined"));
		statuses.add(ApplicationProperty.getInstance().getProperty("invoice.status.noCharges"));
		return statuses;
	}
	
	/**
	 * Checks whether or not the payment date column should show
	 * If paid, declined or all statuses are searched, show.
	 * If issued status is searched, don't show
	 * @return The boolean value
	 */
	public boolean getShowPaymentDates() {
		if(status == null) {
			return true;
		}
		if(status.equals(ApplicationProperty.getInstance().getProperty("invoice.status.issued"))
				|| status.equals(ApplicationProperty.getInstance().getProperty("invoice.status.noCharges"))) {
			return false;
		}
		return true;
	}

	public int getDepotId() {
		return depotId;
	}

	public void setDepotId(int depotId) {
		this.depotId = depotId;
	}

	public int getStateId() {
		return stateId;
	}

	public void setStateId(int stateId) {
		this.stateId = stateId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public BigDecimal getAmountPaidSum() {
		return amountPaidSum;
	}

	public void setAmountPaidSum(BigDecimal amountPaidSum) {
		this.amountPaidSum = amountPaidSum;
	}

	public BigDecimal getAmountOwingSum() {
		return amountOwingSum;
	}

	public void setAmountOwingSum(BigDecimal amountOwingSum) {
		this.amountOwingSum = amountOwingSum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getListLength() {
		return invoices.size();
	}
}
