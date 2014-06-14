package org.webdev.kpoint.bl.pojo.ui;

import java.math.BigDecimal;

import org.webdev.kpoint.bl.pojo.ConsumerCredit;

public class CreditContainer {
	private ConsumerCredit credit;
	private BigDecimal dollarValue;
	
	public ConsumerCredit getCredit() {
		return credit;
	}
	
	public void setCredit(ConsumerCredit credit) {
		this.credit = credit;
	}
	
	public BigDecimal getDollarValue() {
		return dollarValue;
	}
	
	public void setDollarValue(BigDecimal dollarValue) {
		this.dollarValue = dollarValue;
	}
}
