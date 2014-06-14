package org.webdev.kpoint.action;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.webdev.kpoint.managers.UrlManager;
import org.webdev.kpoint.bl.persistence.KinekPointHistoryDao;
import org.webdev.kpoint.bl.pojo.KinekPointHistory;

@UrlBinding("/KinekPointChangeReport.action")
public class KinekPointChangeReportActionBean extends SecureActionBean {

	private List<KinekPointHistory> depotHistory = new ArrayList<KinekPointHistory>();
	
	@DefaultHandler
    public Resolution view() throws Exception {
		depotHistory = new KinekPointHistoryDao().fetch();
		
		String[] changes;
		String formatedChanges;
		for (KinekPointHistory history : depotHistory) {
			changes = history.getChangesMade().split(";");
			formatedChanges = "";
			
			for (int i = 0; i < changes.length; i++) {
				formatedChanges += changes[i] + "<br>";
			}
			
			history.setChangesMade(formatedChanges);
		}
		
        return UrlManager.getKinekPointChangeReport();
    }
	
	public Resolution export() throws Exception {
		StreamingResolution r = new StreamingResolution("text/csv", new StringReader(buildKinekPointChangeCSV()));
		r.setFilename("KinekPointChangeReport.csv");
		return r;
	}
	
	/**
	 * Returns a csv string of all KinekPoint Changes 
	 * @return
	 */
	public String buildKinekPointChangeCSV() throws Exception {
		depotHistory = new KinekPointHistoryDao().fetch();
		
		StringBuilder csv = new StringBuilder();
		csv.append("Depot Name, Depot ID, Date of Change, Type of Change, Changes Made \n");
		
		//Double quotes surround each value in order to handle any values that contain commas
		for (KinekPointHistory history : depotHistory) {
			addColumnValue(csv, history.getName(), false);
			addColumnValue(csv, history.getDepotId(), false);
			addColumnValue(csv, history.getChangedDate().getTime(), false);
			addColumnValue(csv, history.getTypeOfChange(), false);
			String changes = history.getChangesMade();
			changes = removeIfLastChar(changes, ";");
			addColumnValue(csv, changes.replace(";", "\n"), true);
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
	 * If the supplied string ends in the string to remove, it is removed from the string.
	 * @param s The string to test
	 * @param toRemove The string to remove
	 * @return
	 */
	private String removeIfLastChar(String s, String toRemove) {
		if (s.lastIndexOf(toRemove) == s.length() - toRemove.length()) {
			return s.substring(0, s.lastIndexOf(toRemove));
		}
		return s;
	}
	
	public List<KinekPointHistory> getDepotHistory() {
		return depotHistory;
	}
	
	public void setDepotHistory(List<KinekPointHistory> depotHistory) {
		this.depotHistory = depotHistory;
	}
}
