package org.webdev.kpoint.bl.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

public class UnifiedSkidRate implements Serializable{ /**
	 * 
	 */
	private static final long serialVersionUID = 3966171968313853490L;

	private int id;
	private BigDecimal fee;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
}