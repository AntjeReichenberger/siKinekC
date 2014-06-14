package org.webdev.kpoint.bl.api.mapper.response.kinekpoint;

import java.io.Serializable;
import java.math.BigDecimal;

import org.webdev.kpoint.bl.pojo.KPSkidRate;

public class Rate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6795144279858616718L;

	protected BigDecimal actualFee;

	// Package constructor
	Rate() {
	}

	public Rate(KPSkidRate blRate) {
		actualFee = blRate.getActualFee();
	}

	public BigDecimal getActualFee() {
		return actualFee;
	}

	public void setActualFee(BigDecimal actualFee) {
		this.actualFee = actualFee;
	}
}