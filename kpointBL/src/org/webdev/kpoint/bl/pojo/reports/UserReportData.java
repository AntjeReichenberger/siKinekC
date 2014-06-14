package org.webdev.kpoint.bl.pojo.reports;

/**
 * This class is used for passing user report data from the UserReportActionBean to the associated JSP 
 * @author jamie
 *
 */
public class UserReportData {
	private int year;
	private Integer[] totalUsersByMonth = {0,0,0,0,0,0,0,0,0,0,0,0};
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getTotalUsers() {
		int totalUsers = 0;
		for(Integer count : totalUsersByMonth)
			totalUsers += count;
		return totalUsers;
	}

	public Integer[] getTotalUsersByMonth() {
		return totalUsersByMonth;
	}

	public void setTotalUsersByMonth(Integer[] totalUsersByMonth) {
		this.totalUsersByMonth = totalUsersByMonth;
	}
}
