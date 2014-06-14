package org.webdev.kpoint.bl.util;

import java.math.BigDecimal;

import org.webdev.kpoint.bl.manager.ExternalSettingsManager;
import org.webdev.kpoint.bl.pojo.ConsumerCredit;
import org.webdev.kpoint.bl.pojo.KinekPoint;

public class ConsumerCreditCalculator {
	private KinekPoint depot;
	
	public ConsumerCreditCalculator(KinekPoint depot)
	{
		this.depot = depot;
	}
	
	public BigDecimal getCreditValue(ConsumerCredit credit)
	{
		if (isPercentCalculation(credit))
			return getDollarValueOfPercent(credit);
		else
			return credit.getPromotion().getConsumerCreditAmount();
	}
	
	private boolean isPercentCalculation(ConsumerCredit credit)
	{
		return credit.getPromotion().getConsumerCreditCalcType().getId() 
			== ExternalSettingsManager.getCreditCalculationType_Percent();
	}
	
	private BigDecimal getDollarValueOfPercent(ConsumerCredit credit)
	{
		BigDecimal percent = 
			credit.getPromotion().getConsumerCreditAmount()
			.divide(BigDecimal.valueOf(100));
		//TODO use to use depot.getReceivingFee()
		return percent.multiply(new BigDecimal(0));
	}

}
