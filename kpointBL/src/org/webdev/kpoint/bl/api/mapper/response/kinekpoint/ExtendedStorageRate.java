package org.webdev.kpoint.bl.api.mapper.response.kinekpoint;

import java.io.Serializable;

import org.webdev.kpoint.bl.pojo.KPExtendedStorageRate;

public class ExtendedStorageRate extends PackageRate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6769381370760320783L;

	private Integer minDays;
	private Integer maxDays;

	// Package constructor
	ExtendedStorageRate() {
	}

	public ExtendedStorageRate(KPExtendedStorageRate blExtendedStorageRate) {
		actualFee = blExtendedStorageRate.getActualFee();
		minDays = blExtendedStorageRate.getUnifiedExtendedStorageRate()
				.getStorageDuration().getMinDays();
		maxDays = blExtendedStorageRate.getUnifiedExtendedStorageRate()
				.getStorageDuration().getMaxDays();
		minWeight = blExtendedStorageRate.getUnifiedExtendedStorageRate()
				.getStorageWeightGroup().getMinWeight();
		maxWeight = blExtendedStorageRate.getUnifiedExtendedStorageRate()
				.getStorageWeightGroup().getMaxWeight();
	}

	public Integer getMinDays() {
		return minDays;
	}

	public void setMinDays(Integer minDays) {
		this.minDays = minDays;
	}

	public Integer getMaxDays() {
		return maxDays;
	}

	public void setMaxDays(Integer maxDays) {
		this.maxDays = maxDays;
	}

}