package org.webdev.kpoint.action;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import org.webdev.kpoint.converter.CalendarConverter;
import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.bl.persistence.ProspectDao;
import org.webdev.kpoint.bl.pojo.Prospect;

@UrlBinding("/ReferralConversionReport.action")
public class ReferralConversionReportActionBean extends SecureActionBean {

	//Prospect list
	private List<Prospect> prospectsForExport = new ArrayList<Prospect>();
	
	//Filter fields
	@Validate(required=true, on={"fetchReferralConversionReport", "exportReferralConversionReport"}, converter=CalendarConverter.class)
	private Calendar minDate;
	@Validate(required=true, on={"fetchReferralConversionReport", "exportReferralConversionReport"}, converter=CalendarConverter.class)
	private Calendar maxDate;
	private int filterOption;
	private int byReferralDate = 1;
	private int byConversionDate = 2;
	
	
	@DefaultHandler
    public Resolution view() throws Exception  {
		prospectsForExport = new ProspectDao().fetch();
		
        return UrlManager.getReferralConversionReport();
    }	
		
	public Resolution search() throws Exception  {
		
		if(filterOption == byReferralDate)
		{
			this.prepareDates();
			prospectsForExport = new ProspectDao().fetchByReferralDate(minDate, maxDate);
		}
		else if(filterOption == byConversionDate)
		{
			this.prepareDates();
			prospectsForExport = new ProspectDao().fetchByConversionDate(minDate, maxDate);
		}
		else
		{
			prospectsForExport = new ProspectDao().fetch();	
		}
		
        return UrlManager.getReferralConversionReport();
    }
		
	public Resolution export() throws Exception {
		StreamingResolution r = new StreamingResolution("text/csv", new StringReader(buildProspectExportCSV()));
		r.setFilename("ReferralConversionReport.csv");
		return r;
	}
	
	/**
	 * Returns a csv string of all Prospects
	 * @return
	 */
	public String buildProspectExportCSV() throws Exception {
		if(filterOption == byReferralDate)
		{
			this.prepareDates();
			prospectsForExport = new ProspectDao().fetchByReferralDate(minDate, maxDate);
		}
		else if(filterOption == byConversionDate)
		{
			this.prepareDates();
			prospectsForExport = new ProspectDao().fetchByConversionDate(minDate, maxDate);
		}
		else
		{
			prospectsForExport = new ProspectDao().fetch();	
		}
		
		StringBuilder csv = new StringBuilder();
				
		
		csv.append("Prospect Name, Prospect Email, Referrer, Referral Date, Conversion Date, Referral Credit Date\n");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		//Double quotes surround each value in order to handle any values that contain commas
		for (Prospect prospect : prospectsForExport) {
			addColumnValue(csv, prospect.getName(), false);
			addColumnValue(csv, prospect.getEmail(), false);
			addColumnValue(csv, prospect.getReferrer().getFirstName() + " " + prospect.getReferrer().getLastName(), false);
			
			addColumnValue(csv, sdf.format(prospect.getReferralDate().getTime()), false);
			if (prospect.getConversionDate() != null)
			{
				addColumnValue(csv, sdf.format(prospect.getConversionDate().getTime()), false);
			}
			else
			{
				addColumnValue(csv, "", false);
			}
			
			if (prospect.getCreditIssuedDate() != null)
			{
				addColumnValue(csv, sdf.format(prospect.getCreditIssuedDate().getTime()), false);
			}
			else
			{
				addColumnValue(csv, "", false);
			}	
			addColumnValue(csv, "\n", true);
		}
		
		return csv.toString();
	}
	
	/**
	 * Adds a column value to a CSV. 
	 * @param sb String Builder
	 * @param columnValue The column value
	 */
	private void addColumnValue(StringBuilder sb, Object columnValue, boolean isLastColumn) {
		String value = columnValue.toString();
		sb.append("\"");
		sb.append(value.replaceAll("\"", "\"\""));
		sb.append("\"");
		if (!isLastColumn)
			sb.append(",");
		else
			sb.append("\n");
	}
	
	/**
	 * Ensures dates used in the consumer report have times 
	 * set such that the dates are inclusive
	 */
	public void prepareDates() {
		// Ensure dates are inclusive
		minDate.set(Calendar.HOUR, 00);
		minDate.set(Calendar.MINUTE, 00);
		minDate.set(Calendar.SECOND, 00);
		maxDate.set(Calendar.HOUR, 23);
		maxDate.set(Calendar.MINUTE, 59);
		maxDate.set(Calendar.SECOND, 59);		
	}

	public List<Prospect> getProspectsForExport() {
		return prospectsForExport;
	}
	
	public void setProspectsForExport(List<Prospect> prospectForExport) {
		this.prospectsForExport = prospectForExport;
	}
	
	public int getFilterOption() {
		return filterOption;
	}
	
	public void setFilterOption(int filterOption) {
		this.filterOption = filterOption;
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
	
	public int getListLength() {
		return prospectsForExport.size();
	}
}
