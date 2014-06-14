package org.webdev.kpoint.bl.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

public class KPExtendedStorageRate implements Serializable, Comparable<KPExtendedStorageRate> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7627119103389147073L;
	private UnifiedExtendedStorageRate unifiedExtendedStorageRate;
	private transient BigDecimal feeOverride;
	private transient Calendar createdDate;
	private BigDecimal actualFee;
	private transient int kinekPointId;	
	
	public Calendar getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}
	public UnifiedExtendedStorageRate getUnifiedExtendedStorageRate() {
		return unifiedExtendedStorageRate;
	}
	public void setUnifiedExtendedStorageRate(
		UnifiedExtendedStorageRate unifiedExtendedStorageRate) {
		this.unifiedExtendedStorageRate = unifiedExtendedStorageRate;
	}
	public BigDecimal getFeeOverride() {
		return feeOverride;
	}
	
	public BigDecimal getActualFee() {
		return actualFee;
	}
	public void setActualFee(BigDecimal actualFee) {
		this.actualFee = actualFee;
		this.feeOverride = actualFee;
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
	
	public void populateActualFee() {
		if(feeOverride == null)
			actualFee = unifiedExtendedStorageRate.getFee();
		else
			actualFee = feeOverride;
	}
	
	@Override
	public int compareTo(KPExtendedStorageRate arg0) {
		if(unifiedExtendedStorageRate.getId() >= arg0.getUnifiedExtendedStorageRate().getId()){
			if(unifiedExtendedStorageRate.getId() == arg0.getUnifiedExtendedStorageRate().getId()){
				if(createdDate.after(arg0.getCreatedDate())){
					return -1;
				}
				else{
					return 1;
				}
			}
			else{
				return 1;
			}
		}
		else{
			return -1;
		}
	}
}