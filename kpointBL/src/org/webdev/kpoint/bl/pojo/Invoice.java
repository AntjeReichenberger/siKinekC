package org.webdev.kpoint.bl.pojo;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;

public class Invoice {

	private String invoiceNumber;
	private KinekPoint depot;
	private Date startDate;
	private Date endDate;
	private BigDecimal amountDue;
	private String currency;
	private Date dueDate;
	private Date createdDate;
	private String createdBy;
	
	/**
	 * @return the invoiceNumber
	 */
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	/**
	 * @param invoiceNumber the invoiceNumber to set
	 */
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	
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
	 * @return the depot
	 */
	public KinekPoint getKinekPoint() {
		return depot;
	}
	/**
	 * @param depot the depot to set
	 */
	public void setKinekPoint(KinekPoint depot) {
		this.depot = depot;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the amountDue
	 */
	public BigDecimal getAmountDue() {
		return amountDue;
	}
	/**
	 * @return amountDue in currency string format
	 */
	public String getAmountDueStr() {
		NumberFormat nf = NumberFormat.getCurrencyInstance();
	    return nf.format(amountDue);
	}
	/**
	 * @param amountDue the amountDue to set
	 */
	public void setAmountDue(BigDecimal amountDue) {
		this.amountDue = amountDue;
	}
	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	/**
	 * @return the dueDate
	 */
	public Date getDueDate() {
		return dueDate;
	}
	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	

}
