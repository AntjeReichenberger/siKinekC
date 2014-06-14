package org.webdev.kpoint.action;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.manager.InvoiceManager;
import org.webdev.kpoint.bl.persistence.InvoiceDao;
import org.webdev.kpoint.bl.persistence.InvoiceIssueHistoryDao;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.pojo.Invoice;
import org.webdev.kpoint.bl.pojo.InvoiceIssueHistory;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.ui.InvoiceUI;
import org.webdev.kpoint.converter.KinekPointConverter;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/InvoiceSearch.action")
public class InvoiceSearchActionBean extends SecureActionBean {
	
	private List<InvoiceUI> invoices = new ArrayList<InvoiceUI>();
	private String invoiceNumbers;
	private boolean invoicesFound = false;
	
	private List<String> checks;
	private String checkedNumbers;
	
	@Validate(converter=KinekPointConverter.class)
	private KinekPoint depot;
	private String month;
	private String year;
	
	//Details page fields	
	private String invoiceNumber;
	private InvoiceUI invoice;

	/**
	 * Default view shown when the user navigates to the forgotPassword.action
	 * @return
	 */
	@DefaultHandler @DontValidate
	public Resolution view(){
		if (!getActiveUser().getAdminAccessCheck()) {
			return new RedirectResolution(DeliveryActionBean.class);
		}
		
		return UrlManager.getInvoiceSearchForm();
	}
	
	@ValidationMethod(on="search")
	public void searchValidate(ValidationErrors errors) throws UnsupportedEncodingException {
	
	}
	
	public Resolution search() throws Exception{
		searchInvoices();
		return UrlManager.getInvoiceSearchResults();
	}
	
	public Resolution details() throws Exception {
		//Get invoice
		Invoice inv = new InvoiceDao().read(invoiceNumber);
		invoice = InvoiceManager.calculateFees(inv, false, false);
		
		return UrlManager.getInvoiceDetailsForm();
	}
	
	public Resolution continueSend() throws Exception {
		searchInvoices();
		
		return UrlManager.getInvoiceSearchResults();
	}
	
	public Resolution send() throws Exception {
		searchInvoices();
		if(checks != null) {
			for(String box: checks) {
				//Get invoice object
				Invoice inv = findInvoice(box);
				
				//Send invoice
				InvoiceManager.sendInvoice(inv);
				
				//Create history record
				InvoiceIssueHistory hist = new InvoiceIssueHistory();
				hist.setInvoice(inv);
				hist.setIssueDate(new Date());
				hist.setEmail(inv.getKinekPoint().getEmail());
				new InvoiceIssueHistoryDao().create(hist);
			}
		}
		
		return UrlManager.getInvoiceSearchSuccess();
	}
	
	@DontValidate
	public Resolution success() {
		return new RedirectResolution(ViewKinekPointActionBean.class);
	}

	/**
	 * Finds the invoice object in the invoices list corresponding
	 * to the given invoice number.
	 * @param invoiceNumber The invoice number to look for
	 * @return The invoice
	 */
	private Invoice findInvoice(String invoiceNumber) {
		for(InvoiceUI inv: invoices) {
			if (inv.getInvoiceNumber().equals(invoiceNumber)) {
				return inv.getInvoice();
			}
		}
		
		return null;
	}
	
	private void searchInvoices() throws Exception {
		int yearI = year == null ? 0 : Integer.parseInt(year);
		int monthI = month == null ? 0 : Integer.parseInt(month);
		int depotI = depot == null ? 0 : depot.getDepotId();
		
		List<InvoiceUI>invs = new ArrayList<InvoiceUI>();
		List<Invoice> invoicesTemp = new InvoiceDao().fetch(yearI, monthI, depotI);		
		for(Invoice inv: invoicesTemp) {			
			InvoiceUI invUI = new InvoiceUI(inv);
			invUI.setLastIssuedDate(new InvoiceIssueHistoryDao().getLastIssuedDate(inv));
			if(invUI.getLastIssuedDate() != null)
				invs.add(invUI);
		}
		
		setInvoices(invs);
	}

	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(String month) {
		this.month = month;
	}


	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}
	
	public List<KinekPoint> getDepots() throws Exception {
		return new KinekPointDao().fetch();
	}
	
	public List<String> getYears() {
		List<String> years = new ArrayList<String>();
		
		int curYear = new GregorianCalendar().get(Calendar.YEAR);
		for (int i = 0; i < 3; i++)	{
			years.add(0, String.valueOf(curYear));
			curYear--;
		}
		
		return years;
	}
	
	public Date getDate() throws ParseException {
		return new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month), 1).getTime();
	}

	/**
	 * @return the checks
	 */
	public List<String> getChecks() {
		return checks;
	}

	/**
	 * @param checks the checks to set
	 */
	public void setChecks(List<String> checks) {
		this.checks = checks;
	}

	/**
	 * @return the invoicesFound
	 */
	public boolean isInvoicesFound() {
		return invoicesFound;
	}

	/**
	 * @param invoicesFound the invoicesFound to set
	 */
	public void setInvoicesFound(boolean invoicesFound) {
		this.invoicesFound = invoicesFound;
	}

	/**
	 * @return the depot
	 */
	public KinekPoint getDepot() {
		return depot;
	}

	/**
	 * @param depot the depot to set
	 */
	public void setDepot(KinekPoint depot) {
		this.depot = depot;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getCheckedNumbers() {
		return checkedNumbers;
	}

	public void setCheckedNumbers(String checkedNumbers) {
		this.checkedNumbers = checkedNumbers;
	}
	
	/**
	 * @return the invoices
	 */
	public List<InvoiceUI> getInvoices() throws Exception {
		List<InvoiceUI> invs = new ArrayList<InvoiceUI>();
		String[] nums = invoiceNumbers.split(",");
		for(String num: nums) {
			InvoiceUI inv = new InvoiceUI(new InvoiceDao().read(num));
			inv.setLastIssuedDate(new InvoiceIssueHistoryDao().getLastIssuedDate(inv.getInvoice()));
			invs.add(inv);
		}
		
		return invs;
	}

	/**
	 * @param invoices the invoices to set
	 */
	public void setInvoices(List<InvoiceUI> invoices) {
		invoiceNumbers = "";
		for(InvoiceUI inv: invoices) {
			invoiceNumbers += inv.getInvoiceNumber() + ",";
		}
		if(invoiceNumbers.length() > 0)
			invoiceNumbers = invoiceNumbers.substring(0, invoiceNumbers.length()-1);
		invoicesFound = invoices.size() == 0 ? false : true;
		
		this.invoices = invoices;
	}

	public String getInvoiceNumbers() {
		return invoiceNumbers;
	}

	public void setInvoiceNumbers(String invoiceNumbers) {
		this.invoiceNumbers = invoiceNumbers;
	}

	public InvoiceUI getInvoice() {
		return invoice;
	}

	public void setInvoice(InvoiceUI invoice) {
		this.invoice = invoice;
	}
}
