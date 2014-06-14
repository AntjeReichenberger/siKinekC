package org.webdev.kpoint.bl.pojo;

import java.math.BigDecimal;
import java.util.Date;


public class Promotion {

	private int id;
	private String code;
	private String title;
	private String description;
	private int availabilityCount;
	private CreditCalculationType consumerCreditCalcType;
	private BigDecimal consumerCreditAmount;
	private CreditCalculationType depotCreditCalcType;
	private BigDecimal depotCreditAmount;
	private Date startDate;
	private Date endDate;
	private State state;
	private Association association;
	private KinekPoint depot;
	private Date createdDate;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getAvailabilityCount() {
		return availabilityCount;
	}
	
	public void setAvailabilityCount(int availabilityCount) {
		this.availabilityCount = availabilityCount;
	}

	public CreditCalculationType getConsumerCreditCalcType() {
		return consumerCreditCalcType;
	}

	public void setConsumerCreditCalcType(CreditCalculationType consumerCreditCalcType) {
		this.consumerCreditCalcType = consumerCreditCalcType;
	}
	
	public BigDecimal getConsumerCreditAmount() {
		return consumerCreditAmount;
	}

	public void setConsumerCreditAmount(BigDecimal consumerCreditAmount) {
		this.consumerCreditAmount = consumerCreditAmount;
	}
	
	public CreditCalculationType getDepotCreditCalcType() {
		return depotCreditCalcType;
	}

	public void setDepotCreditCalcType(CreditCalculationType depotCreditCalcType) {
		this.depotCreditCalcType = depotCreditCalcType;
	}
	
	public BigDecimal getDepotCreditAmount() {
		return depotCreditAmount;
	}

	public void setDepotCreditAmount(BigDecimal depotCreditAmount) {
		this.depotCreditAmount = depotCreditAmount;
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}	
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
		
	
	public Association getAssociation() {
		return association;
	}

	public void setAssociation(Association association) {
		this.association = association;
	}
	
	public KinekPoint getDepot() {
		return depot;
	}

	public void setDepot(KinekPoint depot) {
		this.depot = depot;
	}
		
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
