package org.webdev.kpoint.bl.pojo;

import java.util.Date;

public class InvoiceIssueHistory {
	private int invoiceIssueHistoryId;
	private Invoice invoice;
	private Date issueDate;
	private String email;
	
	/**
	 * @return the invoiceIssueHistoryId
	 */
	public int getInvoiceIssueHistoryId() {
		return invoiceIssueHistoryId;
	}
	/**
	 * @param invoiceIssueHistoryId the invoiceIssueHistoryId to set
	 */
	public void setInvoiceIssueHistoryId(int invoiceIssueHistoryId) {
		this.invoiceIssueHistoryId = invoiceIssueHistoryId;
	}
	/**
	 * @return the invoice
	 */
	public Invoice getInvoice() {
		return invoice;
	}
	/**
	 * @param invoice the invoice to set
	 */
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
	/**
	 * @return the issueDate
	 */
	public Date getIssueDate() {
		return issueDate;
	}
	/**
	 * @param issueDate the issueDate to set
	 */
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	/**
	 * @return the kpEmail
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param kpEmail the kpEmail to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}
