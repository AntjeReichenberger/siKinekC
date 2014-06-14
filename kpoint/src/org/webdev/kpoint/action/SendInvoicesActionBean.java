package org.webdev.kpoint.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;

import org.webdev.kpoint.bl.manager.InvoiceManager;
import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.bl.util.ApplicationProperty;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.InvoiceDao;
import org.webdev.kpoint.bl.persistence.InvoiceIssueHistoryDao;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Invoice;
import org.webdev.kpoint.bl.pojo.InvoiceIssueHistory;
import org.webdev.kpoint.bl.pojo.ui.InvoiceUI;

@UrlBinding("/SendInvoices.action")
public class SendInvoicesActionBean extends SecureActionBean {
	
	private String invoiceNumbers;
	private List<Invoice> invoices = new ArrayList<Invoice>();	
	private boolean invoicesFound = false;
	private boolean monthsAvailable = true;
	
	@Validate(required=true)
	private String month;
	
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
		
		return UrlManager.getSendInvoicesForm();
	}

	/**
	 * Generates invoices for all KPs for the selected month
	 * @throws ParseException 
	 */
	public Resolution generate() throws Exception {
		
		//Check for records already created for this month
		InvoiceDao invd = new InvoiceDao();
		GregorianCalendar tempGreg = new GregorianCalendar();
		tempGreg.setTime(parseMonth());
		setInvoices(invd.fetch(tempGreg.get(Calendar.YEAR), tempGreg.get(Calendar.MONTH)+1, 0));
		
		//If records have not been generated for this month, make new ones
		if(invoices == null || invoices.size() == 0)
		{
			invoiceNumbers = "";
			List<KinekPoint> depots = new KinekPointDao().fetch(true);
			Date createdDate = new Date();
			
			Date startDate = tempGreg.getTime();
			tempGreg.add(Calendar.MONTH, 1);
			tempGreg.add(Calendar.DAY_OF_YEAR, -1);
			Date endDate = tempGreg.getTime();
			
			int nextNum = invd.getNextInvoiceNumber();
			
			List<Invoice> invList = new ArrayList<Invoice>();
			for (KinekPoint depot : depots) {
				Invoice inv = new Invoice();
				inv.setKinekPoint(depot);
				inv.setCreatedDate(createdDate);
				inv.setCreatedBy(getActiveUser().getUsername());
				inv.setCurrency(depot.getState().getCountry().getCurrencyCode());
				inv.setStartDate(startDate);
				inv.setEndDate(endDate);
				inv.setInvoiceNumber(String.valueOf(nextNum));
				invoice = InvoiceManager.calculateFees(inv, false, true);
				inv.setAmountDue(invoice.getRevenueKinekTotal());
				invd.create(inv);
				// Calculate again to use credits
				invoice = InvoiceManager.calculateFees(inv, true, true);
				invList.add(inv);
					
				nextNum++;
			}
			setInvoices(invList);
		}
		
		return UrlManager.getSendInvoicesPreview();
	}

	public Resolution details() throws Exception {
		//Get invoice
		Invoice inv = new InvoiceDao().read(invoiceNumber);
	
		// Set invoice date and due date based on current date since not issued yet
		GregorianCalendar temp = new GregorianCalendar();
		temp.setTime(new Date());
		temp.add(Calendar.DAY_OF_YEAR, Integer.parseInt(ApplicationProperty.getInstance().getProperty("invoice.daysToDue")));
		inv.setDueDate(temp.getTime());
		inv.setCreatedDate(new Date());
	
		invoice = InvoiceManager.calculateFees(inv, false, false);
		
		return UrlManager.getInvoiceDetailsForm();
	}
	
	public Resolution continueSend() throws Exception {
		GregorianCalendar tempGreg = new GregorianCalendar();
		tempGreg.setTime(parseMonth());		
		setInvoices(new InvoiceDao().fetch(tempGreg.get(Calendar.YEAR), tempGreg.get(Calendar.MONTH)+1, 0));
		
		return UrlManager.getSendInvoicesPreview();
	}
	
	public Resolution send() throws Exception {	
		GregorianCalendar tempGreg = new GregorianCalendar();
		tempGreg.setTime(parseMonth());	
		
		List<Invoice> invs = new InvoiceDao().fetch(tempGreg.get(Calendar.YEAR), tempGreg.get(Calendar.MONTH)+1, 0);
		for(Invoice inv: invs) {
			
			//Update due date on invoice record
			GregorianCalendar temp = new GregorianCalendar();
			temp.setTime(new Date());
			temp.add(Calendar.DAY_OF_YEAR, Integer.parseInt(ApplicationProperty.getInstance().getProperty("invoice.daysToDue")));
			inv.setDueDate(temp.getTime());
			new InvoiceDao().update(inv);
			
			//Send email
			InvoiceManager.sendInvoice(inv);
			
			//Create history record
			InvoiceIssueHistory hist = new InvoiceIssueHistory();
			hist.setInvoice(inv);
			hist.setEmail(inv.getKinekPoint().getEmail());
			hist.setIssueDate(new Date());
			new InvoiceIssueHistoryDao().create(hist);
		}
		
		return UrlManager.getSendInvoicesSuccess();
	}
	
	@DontValidate
	public Resolution success() {
		return new RedirectResolution(ViewKinekPointActionBean.class);
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
	 * Creates a Date object for the first day of the selected month
	 * @return The Date of the first day of the month
	 * @throws ParseException
	 */
	public Date parseMonth() throws ParseException {
		String[] parts = month.split(" ");
		SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy");
		//return DateFormat.getDateInstance().parse(parts[0] + " 1, " + parts[1]);
		return format.parse(parts[0] + " 1, " + parts[1]);
	}

	/**
	 * @return the invoicesFound
	 */
	public boolean getInvoicesFound() {
		return invoicesFound;
	}

	/**
	 * @param invoicesFound the invoicesFound to set
	 */
	public void setInvoicesFound(boolean invoicesFound) {
		this.invoicesFound = invoicesFound;
	}

	/**
	 * @return the monthsAvailable
	 */
	public boolean getMonthsAvailable() {
		return monthsAvailable;
	}

	/**
	 * @param monthsAvailable the monthsAvailable to set
	 */
	public void setMonthsAvailable(boolean monthsAvailable) {
		this.monthsAvailable = monthsAvailable;
	}

	/**
	 * @return the months
	 */
	public List<String> getMonths() throws Exception {
		List<String> months = new ArrayList<String>();
		
		int curMonth = new GregorianCalendar().get(Calendar.MONTH);
		int curYear = new GregorianCalendar().get(Calendar.YEAR);
		for (int i = 0; i < 3; i++) {
			if(curMonth == 0)
			{
				curMonth = 12;
				curYear--;
			}
			GregorianCalendar tempgc = new GregorianCalendar(curYear, curMonth-1, 1);
			Date tempDate = tempgc.getTime();
			
			List<InvoiceIssueHistory> records = new InvoiceIssueHistoryDao().fetchByMonth(tempDate);
			if(records == null || records.isEmpty()) {
				months.add(tempgc.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) + " " + tempgc.get(Calendar.YEAR));
			}
			
			curMonth--;
		}
		
		if(months.size() == 0) {
			months.add("No invoice periods available");
			monthsAvailable = false;
		}
		return months;
	}

	/**
	 * @return the invoiceNumber
	 */
	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	/**
	 * @param invoiceNumber the invoiceNumber to set
	 */
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getInvoiceNumbers() {
		return invoiceNumbers;
	}

	public void setInvoiceNumbers(String invoiceNumbers) {
		this.invoiceNumbers = invoiceNumbers;
	}
	
	/**
	 * @return the invoices
	 */
	public List<Invoice> getInvoices() throws Exception  {
		List<Invoice> invs = new ArrayList<Invoice>();
		String[] nums = invoiceNumbers.split(",");
		for(String num: nums) {
			invs.add(new InvoiceDao().read(num));
		}
		
		return invs;
	}

	/**
	 * @param invoices the invoices to set
	 */
	public void setInvoices(List<Invoice> invoices) {
		invoiceNumbers = "";
		for(Invoice inv: invoices) {
			invoiceNumbers += inv.getInvoiceNumber() + ",";
		}
		if(invoiceNumbers.length() > 0)
			invoiceNumbers = invoiceNumbers.substring(0, invoiceNumbers.length()-1);
		invoicesFound = invoices.size() == 0 ? false : true;
		
		this.invoices = invoices;
	}

	public InvoiceUI getInvoice() {
		return invoice;
	}

	public void setInvoice(InvoiceUI invoice) {
		this.invoice = invoice;
	}
}
