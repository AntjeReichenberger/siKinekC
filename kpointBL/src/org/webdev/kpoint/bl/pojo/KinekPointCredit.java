package org.webdev.kpoint.bl.pojo;

import java.util.Date;

import org.webdev.kpoint.bl.pojo.Invoice;

public class KinekPointCredit extends Credit {
	private KinekPoint depot;
	private Invoice redemptionInvoice;
	private Date redemptionDate;
	
	/**
	 * @return the depot
	 */
	public KinekPoint getDepot() {
		return depot;
	}
	
	/**
	 * @param depot the depot to set
	 */
	public void setDepot(KinekPoint depot) {
		this.depot = depot;
	}
	
	/**
	 * @return the redemptionInvoice
	 */
	public Invoice getRedemptionInvoice() {
		return redemptionInvoice;
	}
	
	/**
	 * @param redemptionInvoice the redemptionInvoice to set
	 */
	public void setRedemptionInvoice(Invoice redemptionInvoice) {
		this.redemptionInvoice = redemptionInvoice;
	}
	
	public Date getRedemptionDate() {
		return redemptionDate;
	}
	
	public void setRedemptionDate(Date redemptionDate) {
		this.redemptionDate = redemptionDate;
	}
}
