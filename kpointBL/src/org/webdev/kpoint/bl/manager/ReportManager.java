package org.webdev.kpoint.bl.manager;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.webdev.kpoint.bl.logging.ApplicationException;
import org.webdev.kpoint.bl.logging.KinekLogger;
import org.webdev.kpoint.bl.pojo.Package;
import org.webdev.kpoint.bl.pojo.PackageReceipt;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class ReportManager {
	
	private static final KinekLogger logger = new KinekLogger(ReportManager.class);
	
	public static String generateParcelReport(Date startDate, Date endDate, List<PackageReceipt> packageReceipts,
			String path, int totalItems, BigDecimal totalReceivingFees, BigDecimal totalKinekFees, BigDecimal revenue) {

		String filename = System.currentTimeMillis() + ".pdf";

		// step 1: creation of a document-object
		Document document = new Document(PageSize.A4.rotate());

		try {
			// step 2:
			// we create a writer that listens to the document
			// and directs a PDF-stream to a file

			path = path + filename;
			PdfWriter.getInstance(document, new FileOutputStream(path));

			// step 3: we open the document
			document.open();

			PdfPTable table = new PdfPTable(7);
			PdfPTable tableTotals = new PdfPTable(3);
			PdfPCell cell = new PdfPCell();

			tableTotals.setSpacingBefore(10);
			tableTotals.setWidthPercentage(25);
			tableTotals.setHorizontalAlignment(3);

			Font f = new Font();
			f.setColor(Color.WHITE);
			f.setStyle("bold");
			Paragraph p = new Paragraph();
			p.setFont(f);

			p.add("Line #");
			cell = new PdfPCell(p);
			cell.setBackgroundColor(Color.decode("#B7DB7F"));
			table.addCell(cell);

			Paragraph p2 = (Paragraph) p.clone();
			p2.clear();
			p2.add("Custom Info");
			cell = new PdfPCell(p2);
			cell.setBackgroundColor(Color.decode("#B7DB7F"));
			table.addCell(cell);

			Paragraph p3 = (Paragraph) p.clone();
			p3.clear();
			p3.add("Courier");
			cell = new PdfPCell(p3);
			cell.setBackgroundColor(Color.decode("#B7DB7F"));
			table.addCell(cell);

			Paragraph p4 = (Paragraph) p.clone();
			p4.clear();
			p4.add("Delivery Date");
			cell = new PdfPCell(p4);
			cell.setBackgroundColor(Color.decode("#B7DB7F"));
			table.addCell(cell);

			Paragraph p5 = (Paragraph) p.clone();
			p5.clear();
			p5.add("Status");
			cell = new PdfPCell(p5);
			cell.setBackgroundColor(Color.decode("#B7DB7F"));
			table.addCell(cell);

			Paragraph p6 = (Paragraph) p.clone();
			p6.clear();
			p6.add("Receiving Fee");
			cell = new PdfPCell(p6);
			cell.setBackgroundColor(Color.decode("#B7DB7F"));
			table.addCell(cell);

			Paragraph p7 = (Paragraph) p.clone();
			p7.clear();
			p7.add("Kinek Fee");
			cell = new PdfPCell(p7);
			cell.setBackgroundColor(Color.decode("#B7DB7F"));
			table.addCell(cell);

			int i = 0;
			for (PackageReceipt receipt : packageReceipts) {
				for (Package packageObj : receipt.getPackages()) {
					table.addCell("" + i);
					table.addCell("" + packageObj.getCustomInfo());
					table.addCell("" + packageObj.getCourier().getName());
					table.addCell("" + receipt.getReceivedDate());
					table.addCell("" + packageObj.getPackageStatus(receipt));
					//TODO table.addCell("" + NumberFormat.getCurrencyInstance().format(packageObj.getReceivingFee(receipt)));
					table.addCell("" + NumberFormat.getCurrencyInstance().format(packageObj.getKinekFee(receipt)));
					i++;
				}
			}
			

			table.addCell("");
			table.addCell("");
			table.addCell("");
			table.addCell("");
			table.addCell("");
			table.addCell("");
			table.addCell("");

			table.addCell("Total Items:");
			table.addCell("");
			table.addCell("");
			table.addCell("");
			table.addCell("");
			table.addCell("");
			table.addCell(totalItems + "");

			table.addCell("Total Fees:");
			table.addCell("");
			table.addCell("");
			table.addCell("");
			table.addCell("");
			table.addCell(NumberFormat.getCurrencyInstance().format(totalReceivingFees));
			table.addCell(NumberFormat.getCurrencyInstance().format(totalKinekFees));

			table.addCell("Total Revenue:");
			table.addCell("");
			table.addCell("");
			table.addCell("");
			table.addCell("");
			table.addCell("");
			table.addCell(NumberFormat.getCurrencyInstance().format(revenue));

			Paragraph p1 = new Paragraph("Parcel Report - " + startDate + " to " + endDate);
			p1.add("");
			p1.add("");
			document.add(p1);
			p1.clear();
			p1.add(" ");

			document.add(p1);
			document.add(table);
			document.add(tableTotals);
		}
		catch (DocumentException de) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("FileName", filename);
            logger.error(new ApplicationException("Could not create parcel report.", de), logProps);
		}
		catch (IOException ioe) {
			Hashtable<String,String> logProps = new Hashtable<String,String>();
			logProps.put("FileName", filename);
            logger.error(new ApplicationException("Could not create parcel report.", ioe), logProps);
		}

		// step 5: we close the document
		document.close();

		return filename;
	}
}