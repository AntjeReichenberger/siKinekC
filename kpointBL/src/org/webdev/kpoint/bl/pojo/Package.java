package org.webdev.kpoint.bl.pojo;

import java.math.BigDecimal;
import java.util.Calendar;

import org.webdev.kpoint.bl.manager.ConfigurationManager;

public class Package {
	private final transient String REDIRECTED = ConfigurationManager.getPackageStatusRedirected();
	private final transient String IN_DEPOT = ConfigurationManager.getPackageStatusInDepot();
	private final transient String PICKED_UP = ConfigurationManager.getPackageStatusPickedUp();
	
	// the following attributes are populated when the package is received
	private int packageId = -1;
	private Courier courier;
	private String customInfo;
	private BigDecimal dutyAndTax = BigDecimal.ZERO;
	private BigDecimal kinekFee = ConfigurationManager.getKinekFee();
	private String shippedFrom;
	private BigDecimal pickupFee;
	private PackageWeightGroup packageWeightGroup;
	private boolean onSkid;
	private BigDecimal skidFee;
	// the following attributes are populated when the package is picked up
	private Calendar pickupDate;
	private BigDecimal extendedDurationFee;
	
	public int getPackageId() {
		return packageId;
	}
	
	public void setPackageId(int i) {
		this.packageId = i;
	}
	
	public String getCustomInfo() {
		return customInfo;
	}
	
	public void setCustomInfo(String p) {
		this.customInfo = p;
	}
	
	public Courier getCourier() {
		return courier;
	}
	
	public void setCourier(Courier c) {
		this.courier = c;
	}
	
	public BigDecimal getDutyAndTax() {
		return dutyAndTax;
	}
	
	public void setDutyAndTax(BigDecimal dutyAndTax) {
		// TODO: This null check shouldn't be necessary, but there are null values
		// in the DB for some reason. The reason they are there should be determined 
		// and this fix should be removed following that.
		if (dutyAndTax == null)
			dutyAndTax = BigDecimal.ZERO;
		this.dutyAndTax = dutyAndTax;
	}
	
	public BigDecimal getKinekFee(PackageReceipt receipt){
		if(getPackageStatus(receipt).equals(REDIRECTED))
			return ConfigurationManager.getRedirectFee();
		return kinekFee;
	}
	
	public String getPackageStatus(PackageReceipt receipt) {
		if (receipt.getRedirectReason() != null)
			return REDIRECTED;
		
		if (pickupDate == null)
			return IN_DEPOT;
		
		return PICKED_UP;
	}
	
	@Override
	public boolean equals(Object o) {
		if ( this == o ) return true;
		
		if ( !(o instanceof Package) ) return false;
		
		Package packageObj = (Package)o;
		return this.getPackageId() == packageObj.getPackageId();
	}

	public String getShippedFrom() {
		return shippedFrom;
	}

	public void setShippedFrom(String shippedFrom) {
		this.shippedFrom = shippedFrom;
	}
	
	public Calendar getPickupDate() {
		return pickupDate;
	}
	
	public void setPickupDate(Calendar pickupDate) {
		this.pickupDate = pickupDate;
	}

	public void setPackageWeightGroup(PackageWeightGroup packageWeightGroup) {
		this.packageWeightGroup = packageWeightGroup;
	}

	public PackageWeightGroup getPackageWeightGroup() {
		return packageWeightGroup;
	}

	public void setOnSkid(boolean onSkid) {
		this.onSkid = onSkid;
	}

	public boolean getOnSkid() {
		return onSkid;
	}
	
	public BigDecimal getPickupFee() {
		return pickupFee;
	}

	public void setPickupFee(BigDecimal pickupFee) {
		this.pickupFee = pickupFee;
	}

	public BigDecimal getSkidFee() {
		return skidFee;
	}

	public void setSkidFee(BigDecimal skidFee) {
		this.skidFee = skidFee;
	}
	
	public BigDecimal getExtendedDurationFee() {
		return extendedDurationFee;
	}

	public void setExtendedDurationFee(BigDecimal extendedDurationFee) {
		this.extendedDurationFee = extendedDurationFee;
	}
}
