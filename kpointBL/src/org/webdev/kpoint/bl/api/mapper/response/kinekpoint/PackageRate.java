package org.webdev.kpoint.bl.api.mapper.response.kinekpoint;

import java.io.Serializable;
import java.math.BigDecimal;

import org.webdev.kpoint.bl.pojo.KPPackageRate;

public class PackageRate extends Rate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 639022158462443801L;

	protected BigDecimal minWeight;
	protected BigDecimal maxWeight;

	// Package constructor
	PackageRate() {
	}

	public PackageRate(KPPackageRate blPackageRate) {
		actualFee = blPackageRate.getActualFee();
		minWeight = blPackageRate.getUnifiedPackageRate()
				.getPackageWeightGroup().getMinWeight();
		maxWeight = blPackageRate.getUnifiedPackageRate()
				.getPackageWeightGroup().getMaxWeight();
	}

	public BigDecimal getMinWeight() {
		return minWeight;
	}

	public void setMinWeight(BigDecimal minWeight) {
		this.minWeight = minWeight;
	}

	public BigDecimal getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(BigDecimal maxWeight) {
		this.maxWeight = maxWeight;
	}
}