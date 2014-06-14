package org.webdev.kpoint.bl.pojo;

import java.io.Serializable;

public class CreditCard implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1265323727458153082L;
	
	private int cardId;
	private String name;
	
	public int getCardId() {
		return cardId;
	}
	public void setCardId(int cardId) {
		this.cardId = cardId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
