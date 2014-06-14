package org.webdev.kpoint.bl.pojo;

import java.util.Date;

public class Credit {
	private int id;	
	private Promotion promotion;
	private CreditIssueReason issueReason;
	private CreditStatus creditStatus;
	private Date issueDate;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public Promotion getPromotion() {
		return promotion;
	}

	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}

	public CreditIssueReason getIssueReason() {
		return issueReason;
	}	
	
	public void setIssueReason(CreditIssueReason issueReason) {
		this.issueReason = issueReason;
	}
	
	public CreditStatus getCreditStatus() {
		return creditStatus;
	}	
	
	public void setCreditStatus(CreditStatus status) {
		this.creditStatus = status;
	}
	
	public Date getIssueDate() {
		return issueDate;
	}
	
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
}
