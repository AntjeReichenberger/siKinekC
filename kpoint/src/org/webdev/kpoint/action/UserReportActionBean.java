package org.webdev.kpoint.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.bl.manager.ConfigurationManager;
import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.bl.persistence.UserDao;
import org.webdev.kpoint.bl.pojo.User;
import org.webdev.kpoint.bl.pojo.reports.UserReportData;

@UrlBinding("/UserReport.action")
public class UserReportActionBean extends SecureActionBean {
	private Map<Integer, UserReportData> userReportData = new HashMap<Integer, UserReportData>();
	private int year;
	
	@DefaultHandler
    public Resolution view() throws Exception {
		searchUsers();
        return UrlManager.getUserReport();
    }
	
	public Resolution viewReport() throws Exception {
		searchUsers();
		return UrlManager.getUserReport();
	}
	
	private void searchUsers() throws Exception {
		List<User> users;
		if (year != 0)
			users = new UserDao().fetchConsumers(year);
		else
			users = new UserDao().fetchConsumersForUserReport();
		
		for (User user : users) {
			int createdYear = user.getCreatedDate().get(Calendar.YEAR);
			
			// Ensure an entry exists for the selected year
			if (!userReportData.containsKey(createdYear)) {
				UserReportData newData = new UserReportData();
				newData.setYear(createdYear);
				userReportData.put(createdYear, newData);
			}
			
			int createdMonth = user.getCreatedDate().get(Calendar.MONTH);
			UserReportData yearData = userReportData.get(createdYear);
			Integer[] monthData = yearData.getTotalUsersByMonth();
			monthData[createdMonth]++;
		}
	}
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public Collection<UserReportData> getUserReportData() {
		// Return a sorted list for display purposes
		Map<Integer, UserReportData> sorted = new TreeMap<Integer, UserReportData>(userReportData);
		return sorted.values();
	}
	
	public List<Integer> getYears() {
		int startYear = ConfigurationManager.getReportsStartYear();
		Calendar now = Calendar.getInstance();
		List<Integer> years = new ArrayList<Integer>();
		for (int year = startYear; year <= now.get(Calendar.YEAR); year++)
			years.add(year);
		return years;
	}
}
