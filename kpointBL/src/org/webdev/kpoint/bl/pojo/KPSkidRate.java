package org.webdev.kpoint.bl.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

public class KPSkidRate implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5270596680071050659L;
	
	private transient UnifiedSkidRate unifiedSkidRate;
	private transient BigDecimal feeOverride;
	private BigDecimal actualFee;
	private transient int kinekPointId;
	
	
	public UnifiedSkidRate getUnifiedSkidRate() {
		return unifiedSkidRate;
	}
	public void setUnifiedSkidRate(UnifiedSkidRate unifiedSkidRate) {
		this.unifiedSkidRate = unifiedSkidRate;
	}
	public BigDecimal getFeeOverride() {
		return feeOverride;
	}
	public void setFeeOverride(BigDecimal feeOverride) {
		this.feeOverride = feeOverride;
	}
	
	public BigDecimal getActualFee(){
		if(feeOverride == null)
			actualFee = unifiedSkidRate.getFee();
		else
			actualFee = feeOverride;
		
		return actualFee;
	}
	public void setActualFee(BigDecimal actualFee2) {
		this.actualFee = actualFee2;
		this.feeOverride = actualFee2;
	}
	
	public void setKinekPointId(int kinekPointId) {
		this.kinekPointId = kinekPointId;
	}
	public int getKinekPointId() {
		return kinekPointId;
	}
	
	public void populateActualFee() {
		if(feeOverride == null)
			actualFee = unifiedSkidRate.getFee();
		else
			actualFee = feeOverride;
	}
}