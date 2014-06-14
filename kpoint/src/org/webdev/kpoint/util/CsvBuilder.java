package org.webdev.kpoint.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CsvBuilder {
	private final String seperator = ",";
	private final String newLine = "\n";
	
	private String[] headerRow = null;
	private List<String[]> dataRows = null;
	
	public CsvBuilder() {
		this(new String[0]);
	}
	
	public CsvBuilder(String[] headerRow) {
		this(headerRow, new ArrayList<String[]>());
	}
	
	public CsvBuilder(String[] headerRow, List<String[]> dataRows) {
		this.headerRow = headerRow;
		this.dataRows = dataRows;
	}
	
	public String export() {
		StringBuilder csv = new StringBuilder();
		
		if (headerRow != null) {
			addRow(csv, headerRow);
		}
		
		if (dataRows != null) {
			for(String[] row : dataRows) {
				addRow(csv, row);
			}
		}
		
		return csv.toString();
	}
	
	public void setHeaderRow(List<String> headerRow) {
		this.headerRow = (String[])headerRow.toArray();
	}
	
	public void setHeaderRow(String[] headerRow) {
		this.headerRow = headerRow;
	}

	public String[] getHeaderRow() {
		return headerRow;
	}
	
	public void appendRow(String[] row) {
		dataRows.add(row);
	}
	
	public void appendRows(Collection<String[]> rows) {
		dataRows.addAll(rows);
	}
	
	private void addRow(StringBuilder csv, String[] row) {
		for (int i=0; i<row.length - 1; i++) {
			String column = row[i]; 
			addColumn(csv, column, false);
		}
		String lastColumn = row[row.length - 1];
		addColumn(csv, lastColumn, true);
	}
	
	private void addColumn(StringBuilder csv, String columnValue, boolean isLastColumn) {
		String value = columnValue == null ? "" : columnValue;
		
		value = value.replace("\"", "\"\"");
		
		if (!value.contains(seperator)) {
			csv.append(value);
		}
		else {
			csv.append("\"");
			csv.append(value);
			csv.append("\"");
		}
		
		if (!isLastColumn) {
			csv.append(seperator);
		}
		else {
			csv.append(newLine);
		}
	}
}
