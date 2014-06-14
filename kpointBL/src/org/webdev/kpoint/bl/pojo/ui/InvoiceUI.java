package org.webdev.kpoint.bl.pojo.ui;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import org.webdev.kpoint.bl.pojo.Invoice;
import org.webdev.kpoint.bl.pojo.KinekPoint;
import org.webdev.kpoint.bl.pojo.PackageReceipt;

public class InvoiceUI {
	private String invoiceNumber;
	private KinekPoint depot;
	private Date startDate;
	private Date endDate;
	private BigDecimal amountDue;
	private String currency;
	private Date createdDate;
	private String createdBy;
	private Date dueDate;
	
	//For search page
	private Date lastIssuedDate;
	
	//For details page
	private List<PackageReceipt> filteredPackageReceipts;
	private BigDecimal kinekFee;
	private BigDecimal feeReceivingTotal;
	private BigDecimal feeKinekTotal;
	private BigDecimal discountReceivingTotal;
	private BigDecimal discountKinekTotal;
	private BigDecimal revenueReceivingTotal;
	private BigDecimal revenueKinekTotal;
	
	//For report page
	private int packagesReceived;
	private int packagesPickedUp;
	private BigDecimal amountPaid;
	private BigDecimal amountOwing;
	private String status;
	private Date paymentDate;

	public InvoiceUI(Invoice inv) {
		this.invoiceNumber = inv.getInvoiceNumber();
		this.depot = inv.getKinekPoint();
		this.dueDate = inv.getDueDate();
		this.startDate = inv.getStartDate();
		this.endDate = inv.getEndDate();
		this.amountDue = inv.getAmountDue();
		this.currency = inv.getCurrency();
		this.createdDate = inv.getCreatedDate();
		this.createdBy = inv.getCreatedBy();
	}
	
	public Invoice getInvoice() {
		Invoice inv = new Invoice();
		inv.setInvoiceNumber(invoiceNumber);
		inv.setKinekPoint(depot);
		inv.setDueDate(dueDate);
		inv.setStartDate(startDate);
		inv.setEndDate(endDate);
		inv.setAmountDue(amountDue);
		inv.setCurrency(currency);
		inv.setCreatedDate(createdDate);
		inv.setCreatedBy(createdBy);
		return inv;
	}
	
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
	
	/**
	 * @return the lastIssuedDate
	 */
	public Date getLastIssuedDate() {
		return lastIssuedDate;
	}
	/**
	 * @param lastIssuedDate the lastIssuedDate to set
	 */
	public void setLastIssuedDate(Date lastIssuedDate) {
		this.lastIssuedDate = lastIssuedDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	public List<PackageReceipt> getFilteredPackageReceipts() {
		return filteredPackageReceipts;
	}

	public void setFilteredPackageReceipts(List<PackageReceipt> filteredPackageReceipts) {
		this.filteredPackageReceipts = filteredPackageReceipts;
	}

	public int getPackageCount() {
		int packageCount = 0;
		for (PackageReceipt receipt : filteredPackageReceipts) {
			packageCount += receipt.getPackages().size();
		}
		return packageCount;
	}

	public BigDecimal getKinekFee() {
		return kinekFee;
	}

	public void setKinekFee(BigDecimal kinekFee) {
		this.kinekFee = kinekFee;
	}

	public BigDecimal getFeeReceivingTotal() {
		return feeReceivingTotal;
	}

	public void setFeeReceivingTotal(BigDecimal feeReceivingTotal) {
		this.feeReceivingTotal = feeReceivingTotal;
	}

	public BigDecimal getFeeKinekTotal() {
		return feeKinekTotal;
	}

	public void setFeeKinekTotal(BigDecimal feeKinekTotal) {
		this.feeKinekTotal = feeKinekTotal;
	}

	public BigDecimal getDiscountReceivingTotal() {
		return discountReceivingTotal;
	}

	public void setDiscountReceivingTotal(BigDecimal discountReceivingTotal) {
		this.discountReceivingTotal = discountReceivingTotal;
	}

	public BigDecimal getDiscountKinekTotal() {
		return discountKinekTotal;
	}

	public void setDiscountKinekTotal(BigDecimal discountKinekTotal) {
		this.discountKinekTotal = discountKinekTotal;
	}

	public BigDecimal getRevenueReceivingTotal() {
		return revenueReceivingTotal;
	}

	public void setRevenueReceivingTotal(BigDecimal revenueReceivingTotal) {
		this.revenueReceivingTotal = revenueReceivingTotal;
	}

	public BigDecimal getRevenueKinekTotal() {
		return revenueKinekTotal;
	}

	public void setRevenueKinekTotal(BigDecimal revenueKinekTotal) {
		this.revenueKinekTotal = revenueKinekTotal;
	}

	public int getPackagesReceived() {
		return packagesReceived;
	}

	public void setPackagesReceived(int PackagesReceived) {
		this.packagesReceived = PackagesReceived;
	}

	public int getPackagesPickedUp() {
		return packagesPickedUp;
	}

	public void setPackagesPickedUp(int PackagesPickedUp) {
		this.packagesPickedUp = PackagesPickedUp;
	}

	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	public BigDecimal getAmountOwing() {
		return amountOwing;
	}

	public void setAmountOwing(BigDecimal amountOwing) {
		this.amountOwing = amountOwing;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
}
