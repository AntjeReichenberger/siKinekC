package org.webdev.kpoint.action;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;

import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.persistence.PickupDao;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.Pickup;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.pojo.reports.ConsumerReportRecord;
import org.webdev.kpoint.converter.CalendarConverter;
import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.util.CsvBuilder;

@UrlBinding("/ExportSystemData.action")
public class ExportSystemDataActionBean extends SecureActionBean{
	private int signUpDate = 1;
	private int lastPackagePickupDate = 2;
	
	private int filterOption;
	@Validate(required=true, on={"fetchConsumerReport"}, converter=CalendarConverter.class)
	private Calendar endDate;
	@Validate(required=true, on={"fetchConsumerReport"}, converter=CalendarConverter.class)
	private Calendar startDate;

	/**
	 * The default page to show when navigating to this action
	 * @return
	 */
	@DefaultHandler
	public Resolution view() {
		return UrlManager.getConsumerReportForm();
	}
	
	/**
	 * Ensures all dates used in the consumer report have times 
	 * set such that the dates are inclusive
	 */
	@Before(on="fetchConsumerReport")
	public void prepareDates() {
		// Ensure dates are inclusive
		startDate.set(Calendar.HOUR, 00);
		startDate.set(Calendar.MINUTE, 00);
		startDate.set(Calendar.SECOND, 00);
		endDate.set(Calendar.HOUR, 23);
		endDate.set(Calendar.MINUTE, 59);
		endDate.set(Calendar.SECOND, 59);		
	}
	
	/**
	 * Returns a resolution that is a csv file containing the consumer report
	 * requested by the select items on the page. 
	 * @return
	 */
	public Resolution fetchConsumerReport() throws Exception {
		String consumerReportCSV = createConsumerReportCSV();
		StreamingResolution r = new StreamingResolution("text/csv", new StringReader(consumerReportCSV));
		r.setFilename("ConsumerReport.csv");
		return r;
	}

	public String createConsumerReportCSV() throws Exception {
		List<User> consumers = new UserDao().fetchConsumers();
		List<ConsumerReportRecord> records = createConsumerReportRecords(consumers);
		String csv = createCSV(records);
		return csv;
	}
	
	private List<ConsumerReportRecord> createConsumerReportRecords(List<User> consumers) throws Exception {
		List<ConsumerReportRecord> records = new ArrayList<ConsumerReportRecord>();
	
		PickupDao pickupDao = new PickupDao();
		
		//find all of the training Kinek numbers in external settings, do not return any of these
		List<String> trainingKinekNumbers = ExternalSettingsManager.getTrainingKinekNumbers();
		
		for(User user : consumers){
			// Skip training Kinek Numbers
			if(trainingKinekNumbers.contains(user.getKinekNumber())) continue;
			
			if(filterOption == signUpDate){
				// TODO: Move this logic to its own report query
				if(user.getCreatedDate().after(startDate) && user.getCreatedDate().before(endDate)){
					List<Pickup> pickups = pickupDao.fetch(user.getKinekNumber());
					if(!pickups.isEmpty()){
						records.add(createConsumerRecord(user, pickups));
					}
				}
			}
			else if(filterOption == lastPackagePickupDate){
				// TODO: Move this logic to its own report query
				List<Pickup> pickups = pickupDao.fetch(user.getKinekNumber());
				if(!pickups.isEmpty()){
					Calendar pickupDate = Calendar.getInstance();
					pickupDate.setTime(pickups.get(pickups.size() - 1).getPickupDate());
					if(pickupDate.after(startDate) && pickupDate.before(endDate)){
						records.add(createConsumerRecord(user, pickups));
					}
				}
			}
		}
		return records;
	}
	
	private ConsumerReportRecord createConsumerRecord(User consumer, List<Pickup> pickups) {
		ConsumerReportRecord record = new ConsumerReportRecord();
		record.setFirstName(consumer.getFirstName());
		record.setLastName(consumer.getLastName());
		record.setKinekNumber(consumer.getKinekNumber());
		
		Pickup firstPickup = pickups.get(0);
		record.setFirstPickupLocation(firstPickup.getKinekPoint().getName());
		Calendar firstPickupDate = Calendar.getInstance();
		firstPickupDate.setTime(firstPickup.getPickupDate());
		record.setFirstPickupDate(firstPickupDate);
		
		Pickup lastPickup = pickups.get(pickups.size() - 1);
		record.setLastPickupLocation(lastPickup.getKinekPoint().getName());
		Calendar lastPickupDate = Calendar.getInstance();
		lastPickupDate.setTime(lastPickup.getPickupDate());
		record.setLastPickupDate(lastPickupDate);
		
		int totalPackages = 0;
		for (Pickup pickup : pickups) {
			totalPackages += pickup.getPackages().size();
		}
		record.setTotalPickupCount(totalPackages);
		return record;
	}
	
	private String createCSV(List<ConsumerReportRecord> records) {
		CsvBuilder csv = new CsvBuilder();
		
		String[] header = {
				"First Name", "Last Name", "Kinek Number", 
				"First Pickup Date ", "First Pickup Location", 
				"Last Pickup Date ", "Last Pickup Location",
				"Total Pickup Count" };
		
		csv.setHeaderRow(header);
		
		DateFormat date = SimpleDateFormat.getDateTimeInstance();
		
		for(ConsumerReportRecord record : records) {
			String firstPickupDate = date.format(record.getFirstPickupDate().getTime());
			String lastPickupDate = date.format(record.getLastPickupDate().getTime());
			String[] row = {
					record.getFirstName(), record.getLastName(), record.getKinekNumber(), 
					firstPickupDate, record.getFirstPickupLocation(),
					lastPickupDate, record.getLastPickupLocation(),
					record.getTotalPickupCount().toString() };
			csv.appendRow(row);
		}
		
		return csv.export();
	}

	/**
	 * Accessor Method for filter option
	 * @return
	 */
	public int getFilterOption() {
		return filterOption;
	}

	/**
	 * Mutator Method for filter option
	 * @param filterOption
	 */
	public void setFilterOption(int filterOption) {
		this.filterOption = filterOption;
	}

	/**
	 * Accessor Method for end date
	 * @return
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * Mutator Method for end date
	 * @param endDate
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * Accessor Method for start date
	 * @return
	 */
	public Calendar getStartDate() {
		return startDate;
	}

	/**
	 * Mutator Method for Start Date
	 * @param startDate
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}
}
