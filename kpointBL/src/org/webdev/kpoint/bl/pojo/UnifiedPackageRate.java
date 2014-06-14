package org.webdev.kpoint.bl.pojo;

import java.math.BigDecimal;

public class UnifiedPackageRate{
	private transient int id;
	private PackageWeightGroup packageWeightGroup;
	private transient BigDecimal fee;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public PackageWeightGroup getPackageWeightGroup() {
		return packageWeightGroup;
	}
	public void setPackageWeightGroup(PackageWeightGroup packageWeightGroup) {
		this.packageWeightGroup = packageWeightGroup;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
}