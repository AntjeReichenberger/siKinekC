package org.webdev.kpoint.bl.pojo;

import java.util.Calendar;

public class Prospect {

	private int id;
	private String name;
	private String email;
	private User referrer;
	private Calendar referralDate;
	private String referralMessage;
	private ConsumerCredit credit;
	private Calendar creditIssuedDate;
	private Calendar conversionDate;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public User getReferrer() {
		return referrer;
	}
	
	public void setReferrer(User referrer) {
		this.referrer = referrer;
	}
	
	public Calendar getReferralDate() {
		return referralDate;
	}

	public void setReferralDate(Calendar referralDate) {
		this.referralDate = referralDate;
	}

	public String getReferralMessage() {
		return referralMessage;
	}

	public void setReferralMessage(String referralMessage) {
		this.referralMessage = referralMessage;
	}

	public ConsumerCredit getCredit() {
		return credit;
	}

	public void setCredit(ConsumerCredit credit) {
		this.credit = credit;
	}

	public Calendar getCreditIssuedDate() {
		return creditIssuedDate;
	}

	public void setCreditIssuedDate(Calendar creditIssuedDate) {
		this.creditIssuedDate = creditIssuedDate;
	}
	
	public Calendar getConversionDate() {
		return conversionDate;
	}

	public void setConversionDate(Calendar conversionDate) {
		this.conversionDate = conversionDate;
	}
}
