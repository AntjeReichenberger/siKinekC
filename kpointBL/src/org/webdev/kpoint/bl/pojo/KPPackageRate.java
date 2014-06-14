package org.webdev.kpoint.bl.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

public class KPPackageRate implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3848887410336580694L;
	private UnifiedPackageRate unifiedPackageRate;
	private transient BigDecimal feeOverride;
	private BigDecimal actualFee;
	private transient int kinekPointId;
	
	public UnifiedPackageRate getUnifiedPackageRate() {
		return unifiedPackageRate;
	}
	public void setUnifiedPackageRate(UnifiedPackageRate unifiedPackageRate) {
		this.unifiedPackageRate = unifiedPackageRate;
	}
	public BigDecimal getFeeOverride() {
		return feeOverride;
	}	
	public void setFeeOverride(BigDecimal feeOverride) {
		this.feeOverride = feeOverride;
	}
	public void setKinekPointId(int kinekPointId) {
		this.kinekPointId = kinekPointId;
	}
	public int getKinekPointId() {
		return kinekPointId;
	}
	public BigDecimal getActualFee() {
		return actualFee;
	}
	public void setActualFee(BigDecimal actualFee) {
		this.actualFee = actualFee;
		this.feeOverride = actualFee;
	}
	
	public void populateActualFee() {
		if(feeOverride == null)
			actualFee = unifiedPackageRate.getFee();
		else
			actualFee = feeOverride;
	}
}