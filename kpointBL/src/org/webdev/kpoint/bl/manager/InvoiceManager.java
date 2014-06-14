package org.webdev.kpoint.bl.manager;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import org.webdev.kpoint.bl.persistence.ConsumerCreditDao;
import org.webdev.kpoint.bl.persistence.KinekPointCreditDao;
import org.webdev.kpoint.bl.persistence.MessageDao;
import org.webdev.kpoint.bl.persistence.PackageReceiptDao;
import org.webdev.kpoint.bl.persistence.PickupDao;
import org.webdev.kpoint.bl.pojo.ConsumerCredit;
import org.webdev.kpoint.bl.pojo.CreditCalculationType;
import org.webdev.kpoint.bl.pojo.CreditStatus;
import org.webdev.kpoint.bl.pojo.Email;
import org.webdev.kpoint.bl.pojo.Invoice;
import org.webdev.kpoint.bl.pojo.KinekPointCredit;
import org.webdev.kpoint.bl.pojo.Message;
import org.webdev.kpoint.bl.pojo.MessageMedia;
import org.webdev.kpoint.bl.pojo.MessageTrigger;
import org.webdev.kpoint.bl.pojo.Package;
import org.webdev.kpoint.bl.pojo.PackageReceipt;
import org.webdev.kpoint.bl.pojo.Pickup;
import org.webdev.kpoint.bl.pojo.ui.InvoiceUI;
import org.webdev.kpoint.bl.util.ApplicationProperty;
import org.webdev.kpoint.bl.util.Emailer;
import org.webdev.kpoint.bl.util.Encryption;
import org.webdev.kpoint.bl.util.FileReader;
import org.webdev.kpoint.bl.util.Emailer.EmailType;

public class InvoiceManager {
	
	/**
	 * Sends an invoice email to the given customer containing invoice
	 * information and a secure PayPal payment button.
	 */
	public static void sendInvoice(Invoice invoice) throws Exception
	{		
		InvoiceUI inv = calculateFees(invoice, false, false);
		
		String target = inv.getDepot().getEmail();
		String bccTarget = ExternalSettingsManager.getEmailTargetBCC();
		String subject = ApplicationProperty.getInstance().getProperty("email.invoice.subject");
		subject = subject.replace("attb_inv_num", invoice.getInvoiceNumber());
		String body = FileReader.readFile("invoice.email");
		
		Encryption enc = new Encryption(ApplicationProperty.getInstance().getProperty("encryption.key"));
		String encryptedNum = enc.encrypt(inv.getInvoiceNumber());
		encryptedNum = Encryption.removeURLReservedChars(encryptedNum);
		String payURL = ExternalSettingsManager.getAdminPortalBaseUrl() + "/Payment.action?num=" + encryptedNum;
		
		if(inv.getRevenueKinekTotal().doubleValue() == 0.0) {
			body = body.replace("attb_button_style", "style=\"background: transparent url(img_button_disabled) no-repeat scroll left center; cursor: default;\"");
			body = body.replace("attb_no_pay", "You have no outstanding charges for this month.");
			body = body.replace("attb_redirectURL", "");
		}
		else {
			body = body.replace("attb_button_style", "");
			body = body.replace("attb_no_pay", "");
			body = body.replace("attb_redirectURL", payURL);
		}

		body = body.replace("img_logo", ExternalSettingsManager.getEmailHeaderImage());
		body = body.replace("img_table_header", ExternalSettingsManager.getEmailTableHeaderImage());
		body = body.replace("img_table_odd", ExternalSettingsManager.getEmailTableOddImage());
		body = body.replace("img_table_even", ExternalSettingsManager.getEmailTableEvenImage());
		body = body.replace("img_button_disabled", ExternalSettingsManager.getEmailDisabledButtonImage());	
		body = body.replace("img_button", ExternalSettingsManager.getEmailButtonImage());			
		
		SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
		body = body.replace("attb_inv_num", inv.getInvoiceNumber());
		body = body.replace("attb_inv_date", df.format(new Date()));
		body = body.replace("attb_inv_due", df.format(inv.getDueDate()));
		body = body.replace("attb_inv_start", df.format(inv.getStartDate()));
		body = body.replace("attb_inv_end", df.format(inv.getEndDate()));
		
		
		String address = inv.getDepot().getAddress1();
		if(inv.getDepot().getAddress2() != null && !inv.getDepot().getAddress2().isEmpty())
			address += "<br>" + inv.getDepot().getAddress2();
		
		body = body.replace("attb_kp_name", inv.getDepot().getName());
		body = body.replace("attb_kp_address", address);
		body = body.replace("attb_kp_cityprov", inv.getDepot().getCity() + ", " + inv.getDepot().getState().getName());
		body = body.replace("attb_kp_zip", inv.getDepot().getZip());

		body = body.replace("attb_totals_subrec", NumberFormat.getCurrencyInstance().format(inv.getFeeReceivingTotal()));
		body = body.replace("attb_totals_subkinek", NumberFormat.getCurrencyInstance().format(inv.getFeeKinekTotal()));
		body = body.replace("attb_totals_lessrec", NumberFormat.getCurrencyInstance().format(inv.getDiscountReceivingTotal()));
		body = body.replace("attb_totals_lesskinek", NumberFormat.getCurrencyInstance().format(inv.getDiscountKinekTotal()));
		body = body.replace("attb_totals_revrec", NumberFormat.getCurrencyInstance().format(inv.getRevenueReceivingTotal()));
		body = body.replace("attb_totals_revkinek", NumberFormat.getCurrencyInstance().format(inv.getRevenueKinekTotal()));
		body = body.replace("attb_totals_owing", NumberFormat.getCurrencyInstance().format(inv.getRevenueKinekTotal()));
		
		body = body.replace("attb_packages_count", String.valueOf(inv.getPackageCount()));
		body = body.replace("attb_packages", buildPackageTable(inv.getFilteredPackageReceipts()));	

		Email email = new Email();
		email.setBody(body);
		email.setSubject(subject);
		email.setTo(target);
		email.setBcc(bccTarget);
		

		Emailer emailer = new Emailer();
		emailer.sendHtmlEmail(email, EmailType.HTML);
		
		
		// Create supporting Media and Trigger for the Message
		MessageMedia invoicesentMedium = new MessageMedia();
		invoicesentMedium.setId(ExternalSettingsManager.getMessageMedium_Email());
		
		MessageTrigger invoicesentTrigger = new MessageTrigger();
		invoicesentTrigger.setId(ExternalSettingsManager.getMessageTrigger_invoicesent());
		
		// Create a Message 
		Message invoicesentMessageSent = new Message();
		invoicesentMessageSent.setMedium(invoicesentMedium);
		invoicesentMessageSent.setTrigger(invoicesentTrigger);
		invoicesentMessageSent.setRecipientEmail(email.getTo());
		invoicesentMessageSent.setContents(email.getBody());
		
		// Save the new message to the DB
		new MessageDao().create(invoicesentMessageSent);
	}

	/**
	 * Builds an html string for the rows in the package table
	 * containing the given packages
	 * @param packages
	 * @return
	 */
	private static String buildPackageTable(List<PackageReceipt> packageReceipts) {
		SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
		
		String table = new String();
		for (PackageReceipt receipt : packageReceipts) {
			for(Package pack : receipt.getPackages()) {
				table += "<tr>";
				table += "<td>" + pack.getPackageId() + "</td>";
				table += "<td>" + pack.getCourier().getName() + "</td>";
				table += "<td>" + df.format(receipt.getReceivedDate()) + "</td>";
				table += "<td>" + df.format(pack.getPickupDate().getTime()) + "</td>";
				//TODO table += "<td>" + NumberFormat.getCurrencyInstance().format(pack.getReceivingFee(receipt)) + "</td>";
				table += "<td>" + NumberFormat.getCurrencyInstance().format(pack.getKinekFee(receipt)) + "</td>";
				table += "</tr>";
			}
		}
		
		return table;
	}
	
	/**
	 * Calculates all of the fees associated with the given depot for the selected month
	 * @param invoice The invoice to calculate for
	 * @param useCredits If true, credits will be updated to be redeemed in the database. If false, credits will still be calculated but not used.
	 * @param searchUnusedCredits If true, unused credits will be applied. If false, only credits that have already been redeemed on this invoice will be used
	 * @return An InvoiceUI object containing all of the calculations
	 */
	public static InvoiceUI calculateFees(Invoice invoice, boolean useCredits, boolean searchUnusedCredits) throws Exception {
		InvoiceUI inv = new InvoiceUI(invoice);
		
		//Get kinek fee
		inv.setKinekFee(ConfigurationManager.getKinekFee());
		
		//Get packages
		GregorianCalendar startDate = new GregorianCalendar();
		startDate.setTime(inv.getStartDate());
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.setTime(inv.getEndDate());
		
		List<Pickup> pickups = new PickupDao().fetch(inv.getDepot().getDepotId(), startDate, endDate);
		List<PackageReceipt> filteredPackageReceipts = new ArrayList<PackageReceipt>();
		PackageReceiptDao receiptDao = new PackageReceiptDao();
		for (Pickup pickup : pickups) {
			for (Package packageObj : pickup.getPackages()) {
				PackageReceipt receipt = receiptDao.read(packageObj);
				
				if (!filteredPackageReceipts.contains(receipt)) {
					PackageReceipt filteredPackageReceipt = new PackageReceipt();
					filteredPackageReceipt.setId(receipt.getId());
					filteredPackageReceipt.setKinekPoint(receipt.getKinekPoint());
					filteredPackageReceipt.setReceivedDate(receipt.getReceivedDate());
					filteredPackageReceipt.setPackages(new HashSet<Package>());
					
					filteredPackageReceipts.add(filteredPackageReceipt);
				}
				
				filteredPackageReceipts.get(filteredPackageReceipts.indexOf(receipt)).getPackages().add(packageObj);
			}
		}
		inv.setFilteredPackageReceipts(filteredPackageReceipts);
		
		//Calculate totals
		BigDecimal feeReceivedTotal = new BigDecimal(0.00);
		for (PackageReceipt receipt : inv.getFilteredPackageReceipts()) {
			//TODO feeReceivedTotal = feeReceivedTotal.add(new BigDecimal(receipt.getPackages().size()).multiply(inv.getDepot().getReceivingFee()));
		}
		inv.setFeeReceivingTotal(feeReceivedTotal);
		
		BigDecimal feeKinekTotal = new BigDecimal(0.00);
		for (PackageReceipt receipt : inv.getFilteredPackageReceipts()) {
			feeKinekTotal = feeKinekTotal.add(new BigDecimal(receipt.getPackages().size()).multiply(inv.getKinekFee()));
		}
		inv.setFeeKinekTotal(feeKinekTotal);
		
		inv.setRevenueKinekTotal(inv.getFeeKinekTotal());
		inv.setRevenueReceivingTotal(inv.getFeeReceivingTotal());
		
		//Look for depot credits already redeemed against this invoice
		List<KinekPointCredit> depotCredits = new KinekPointCreditDao().fetch(inv.getInvoiceNumber());
		for(int i = 0; i < depotCredits.size(); i++) {
			BigDecimal amount = depotCredits.get(i).getPromotion().getDepotCreditAmount();
			CreditCalculationType type = depotCredits.get(i).getPromotion().getDepotCreditCalcType();
			
			if(type.getName().equals(ApplicationProperty.getInstance().getProperty("credit.calculation.type.percentage"))) {
				inv.setRevenueKinekTotal(inv.getRevenueKinekTotal().subtract(inv.getKinekFee().multiply(amount).divide(new BigDecimal(100))));
			}
			else {
				inv.setRevenueKinekTotal(inv.getRevenueKinekTotal().subtract(amount));
			}
		}		
		
		if(searchUnusedCredits) {
			//Look for unused credits to apply
			depotCredits = new KinekPointCreditDao().fetchUnused(inv.getDepot().getDepotId(), inv.getEndDate());
			for(int i = 0; i < depotCredits.size()  && inv.getRevenueKinekTotal().compareTo(new BigDecimal(0)) == 1; i++) {
				if(depotCredits.get(i).getCreditStatus().getId() == ExternalSettingsManager.getCreditStatus_Available()) {
					
					BigDecimal amount = depotCredits.get(i).getPromotion().getDepotCreditAmount();
					CreditCalculationType type = depotCredits.get(i).getPromotion().getDepotCreditCalcType();
					
					if(type.getName().equals(ApplicationProperty.getInstance().getProperty("credit.calculation.type.percentage"))) {
						inv.setRevenueKinekTotal(inv.getRevenueKinekTotal().subtract(inv.getKinekFee().multiply(amount).divide(new BigDecimal(100))));
					}
					else {
						inv.setRevenueKinekTotal(inv.getRevenueKinekTotal().subtract(amount));
					}
					
					if(useCredits) {
						depotCredits.get(i).setRedemptionDate(new GregorianCalendar().getTime());
						depotCredits.get(i).setRedemptionInvoice(inv.getInvoice());
						CreditStatus status = new CreditStatus();
						status.setId(ExternalSettingsManager.getCreditStatus_Redeemed());
						depotCredits.get(i).setCreditStatus(status);
						new KinekPointCreditDao().update(depotCredits.get(i));
					}
				}
			}	
		}
		
		//Check if total is below 0
		if(inv.getRevenueKinekTotal().compareTo(new BigDecimal(0)) == -1) {
			inv.setRevenueKinekTotal(new BigDecimal(0));
		}		
		inv.setDiscountKinekTotal(inv.getFeeKinekTotal().subtract(inv.getRevenueKinekTotal()));
		
		
		//Calculate consumer credits
		List<ConsumerCredit> consumerCredits = new ConsumerCreditDao().fetchByPickups(pickups);
		for(int i = 0; i < consumerCredits.size()  && inv.getRevenueReceivingTotal().compareTo(new BigDecimal(0)) == 1; i++) {
			BigDecimal amount = consumerCredits.get(i).getPromotion().getConsumerCreditAmount();
			CreditCalculationType type = consumerCredits.get(i).getPromotion().getConsumerCreditCalcType();
			
			if(type.getName().equals(ApplicationProperty.getInstance().getProperty("credit.calculation.type.percentage"))) {
				//TODO BigDecimal recieving = consumerCredits.get(i).getPickup().getKinekPoint().getReceivingFee();
				//TODO inv.setRevenueReceivingTotal(inv.getRevenueReceivingTotal().subtract(recieving.multiply(amount).divide(new BigDecimal(100))));
			}
			else {
				inv.setRevenueReceivingTotal(inv.getRevenueReceivingTotal().subtract(amount));
			}
		}	
		
		//Check if total is below 0
		if(inv.getRevenueReceivingTotal().compareTo(new BigDecimal(0)) == -1) {
			inv.setRevenueReceivingTotal(new BigDecimal(0));
		}		
		inv.setDiscountReceivingTotal(inv.getFeeReceivingTotal().subtract(inv.getRevenueReceivingTotal()));
		
		return inv;
	}
}
