package org.webdev.kpoint.action;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.bl.persistence.KinekPointDao;
import org.webdev.kpoint.bl.persistence.PackageReceiptDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.Package;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.reports.Report;
import org.webdev.kpoint.managers.UrlManager;

@UrlBinding("/ParcelReport.action")
public class ParcelReportActionBean extends SecureActionBean
{

	private final KinekLogger logger = new KinekLogger(ParcelReportActionBean.class);

	//Resolution constant
	private final Resolution PARCEL_REPORT_PAGE = UrlManager.getParcelReport();
	
	//Package list
	private List<PackageReceipt> filteredPackageReceipts = new ArrayList<PackageReceipt>();
	
	//Filter fields	
    private int depotId;
    private int reportId;
    private String startDate;
    private String endDate;
    
    private String filename;
	private BigDecimal totalReceivingFees = BigDecimal.ZERO;
	private BigDecimal totalKinekFees = BigDecimal.ZERO;
	private BigDecimal totalRevenue = BigDecimal.ZERO;
	private int totalItems;
	
	private List<KinekPoint> userDepots;
	
	

	@DefaultHandler
    public Resolution view() throws Exception 
	{
		
        return search();
    }

	@ValidationMethod(on={"search"})
	public void searchValidation(ValidationErrors errors)
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date start = null;
		Date end = null;

		try
		{
			if (startDate != null && !startDate.isEmpty()) start = df.parse(startDate);
		}
		catch(Exception e)
		{
			Hashtable<String,String> logValues = new Hashtable<String,String>();
			logValues.put("StartDate", startDate);
            logger.error(new ApplicationException("Error occurred processing the start date", e), logValues);
            
            String field = "startDate";
			ValidationError error = new SimpleError("The Start Date value must entered in a valid Date format");
			errors.add(field, error);
		}

		try
		{
			if (endDate != null && !endDate.isEmpty()) end = df.parse(endDate);
		}
		catch(Exception e)
		{
			Hashtable<String,String> logValues = new Hashtable<String,String>();
			logValues.put("EndDate", endDate);
            logger.error(new ApplicationException("Error occurred processing the end date", e), logValues);

            String field = "endDate";
			ValidationError error = new SimpleError("The End Date value must entered in a valid Date format");
			errors.add(field, error);
		}

		if (start != null && end != null && start.after(end))
		{
			String field = "startDate";
			ValidationError error = new SimpleError("Invalid Start Date was entered");
			errors.add(field, error);
		}
	}

	public Resolution search() throws Exception 
	{	

		//----Fetch kinekpoints that are mapped to the depotAdmin
		if(getActiveUser().getDepotAdminAccessCheck()){
			UserDao userDao = new UserDao(); 
			int userId = getActiveUser().getUserId(); 
			userDepots = new ArrayList<KinekPoint>(userDao.fetchUserKinekPoints(userId));			
		}else{
			userDepots = new ArrayList<KinekPoint>();
		}	
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date start = null;
		Date end = null;

		try
		{
			if (startDate != null && !startDate.isEmpty())
				start = df.parse(startDate);
			if (endDate != null && !endDate.isEmpty())
				end = df.parse(endDate);
		}
		catch(Exception e){
			Hashtable<String,String> logValues = new Hashtable<String,String>();
			logValues.put("StartDate", startDate);
            logValues.put("EndDate", endDate);
            logger.error(new ApplicationException("Error occurred processing the dates", e), logValues);
		}
		
		List<PackageReceipt> packageReceipts = new ArrayList<PackageReceipt>();
		if(depotId == -1 && getActiveUser().getDepotAdminAccessCheck()){
			packageReceipts = new PackageReceiptDao().fetchAll(userDepots);
		}
		else if(depotId >= 1){
			//depotId = getActiveUser().getKinekPoint().getDepotId();
			KinekPoint depot = new KinekPointDao().read(depotId);
			packageReceipts = new PackageReceiptDao().fetch(depot);
		}
			
		filteredPackageReceipts = new ArrayList<PackageReceipt>();

		totalReceivingFees = BigDecimal.ZERO;
		
		for (PackageReceipt receipt : packageReceipts) {
			PackageReceipt filteredPackageReceipt = new PackageReceipt();
			filteredPackageReceipt.setKinekPoint(receipt.getKinekPoint());
			filteredPackageReceipt.setReceivedDate(receipt.getReceivedDate());
			filteredPackageReceipt.setRedirectReason(receipt.getRedirectReason());
			filteredPackageReceipt.setPackageRecipients(receipt.getPackageRecipients());
			filteredPackageReceipt.setPackages(new HashSet<Package>());
			
			for (Package packageObj : receipt.getPackages()) {
				boolean validPackage = false;
				if (reportId == 0){
					validPackage = true;
				}	
				if (reportId == 1 && packageObj.getPackageStatus(receipt) == ConfigurationManager.getPackageStatusInDepot()) {
					validPackage = true;
				}
				if (reportId == 2 && packageObj.getPackageStatus(receipt) == ConfigurationManager.getPackageStatusPickedUp()) {
					validPackage = true;
				}
				if (reportId == 3 && packageObj.getPackageStatus(receipt) == ConfigurationManager.getPackageStatusRedirected()) {
					validPackage = true;
				}
				if (reportId == 4 && packageObj.getPackageStatus(receipt) == ConfigurationManager.getPackageStatusPickedUp()) {
					validPackage = true;
				}
				
				if (validPackage) {
					boolean include = true;
					Date testDate = null;

					if (reportId == 0 || reportId == 1 || reportId == 3)
					{
						Calendar c = new GregorianCalendar();
						c.setTime(receipt.getReceivedDate());
						try
						{
							testDate = df.parse(c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR));
						}
						catch (Exception e) {
							logger.error(new ApplicationException("Error occurred processing the test date", e));
						}
					}

					if (reportId == 2 || reportId == 4)
					{
						Calendar c = new GregorianCalendar();
						c.setTime(packageObj.getPickupDate().getTime());
						try
						{
							testDate = df.parse(c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR));
						}
						catch (Exception e) {
							logger.error(new ApplicationException("Error occurred processing the test date", e));
						}
					}

					if (start != null && testDate.before(start))
						include = false;
					if (end != null && testDate.after(end))
						include = false;

					if (include)
					{
						if(!(packageObj.getPackageStatus(receipt).equals(ConfigurationManager.getPackageStatusRedirected())))
						{
							totalReceivingFees = totalReceivingFees.add(packageObj.getKinekFee(receipt));
							totalKinekFees = totalKinekFees.add(packageObj.getKinekFee(receipt));
						}

						filteredPackageReceipt.getPackages().add(packageObj);
					}
				}
			}
			
			if (filteredPackageReceipt.getPackages().size() > 0)
			{
				filteredPackageReceipt.populateExtendedStorageFeesForPackages();
				filteredPackageReceipts.add(filteredPackageReceipt);
			}
		}
		
		totalRevenue = totalReceivingFees.subtract(totalKinekFees);
		for (PackageReceipt receipt : filteredPackageReceipts) {
			totalItems += receipt.getPackages().size();
		}

		// String path = getContext().getServletContext().getRealPath("/") + "resource" + File.separator + "reports" + File.separator;
		// filename = ReportManager.generateParcelReport(new Date(), new Date(), filteredPackageReceipts, path, totalItems, totalReceivingFees, totalKinekFees, totalRevenue);
		
		return PARCEL_REPORT_PAGE;
	}
    
	public String getFilename()
	{
		return filename;
	}

	public BigDecimal getTotalReceivingFees()
	{
		return totalReceivingFees;
	}

	public BigDecimal getTotalKinekFees()
	{
		return totalKinekFees;
	}

	public BigDecimal getTotalRevenue()
	{
		return totalRevenue;
	}

	public int getTotalItems()
	{
		return totalItems;
	}

	public void setTotalItems(int totalItems)
	{
		this.totalItems = totalItems;
	}
    
	public void setTotalReceivingFees(BigDecimal totalReceivingFees)
	{
		this.totalReceivingFees = totalReceivingFees;
	}

	public void setTotalKinekFees(BigDecimal totalKinekFees)
	{
		this.totalKinekFees = totalKinekFees;
	}

	public void setTotalRevenue(BigDecimal totalRevenue)
	{
		this.totalRevenue = totalRevenue;
	}
	
	public List<PackageReceipt> getFilteredPackageReceipts()
	{
		return filteredPackageReceipts;
	}

	public void setFilteredPackageReceipts(List<PackageReceipt> filteredPackageReceipts)
	{
		this.filteredPackageReceipts = filteredPackageReceipts;
	}

	public int getDepotId()
	{
		return depotId;
	}

	public void setDepotId(int depotId)
	{
		this.depotId = depotId;
	}

	public String getStartDate()
	{
		return startDate;
	}

	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	public String getEndDate()
	{
		return endDate;
	}

	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	public List<KinekPoint> getDepots() throws Exception 
	{
		return new KinekPointDao().fetchLight();
	}

	public int getReportId()
	{
		return reportId;
	}

	public void setReportId(int reportId)
	{
		this.reportId = reportId;
	}

	public List<Report> getReports()
	{
		List<Report> reports = new ArrayList<Report>();
		Report report;
		report = new Report();
		report.setReportId(0);
		report.setName("All");
		reports.add(report);
		report = new Report();
		report.setReportId(1);
		report.setName("Pending Pick-Up");
		reports.add(report);
		report = new Report();
		report.setReportId(2);
		report.setName("Picked-Up");
		reports.add(report);
		report = new Report();
		report.setReportId(3);
		report.setName("Redirected");
		reports.add(report);
		/*
		report = new Report();
		report.setReportId(4);
		report.setName("Invoiced");
		reports.add(report);
		*/
		report = new Report();
		report.setReportId(5);
		report.setName("Enroute");
		reports.add(report);
		
		return(reports);
	}
	
	public int getListLength() {
		int listLength = 0;
		for (PackageReceipt receipt : filteredPackageReceipts) {
			listLength += receipt.getPackages().size();
		}
		return listLength;
	}
	
	public void setUserDepots(List<KinekPoint> userDepots){
		this.userDepots = userDepots;
	}
	
	public List<KinekPoint> getUserDepots(){
		return userDepots;
	}
	
	
	
}
