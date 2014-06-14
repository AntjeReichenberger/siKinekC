package org.webdev.kpoint.bl.api.mapper.response;

import static org.webdev.kpoint.bl.api.mapper.response.util.CollectionConstructor.construct;

import java.util.Date;
import java.util.Set;

import org.webdev.kpoint.bl.api.mapper.response.kinekpoint.KinekPoint;

public class PackageReceipt {
	private KinekPoint kinekPoint;
	private Set<Package> packages;
	private String redirectReason;
	private Date receivedDate;
	private boolean requiresProofOfPurchase;

	public PackageReceipt(org.webdev.kpoint.bl.pojo.PackageReceipt blPackageReceipt) {
	
		if (blPackageReceipt.getKinekPoint() != null) {
			kinekPoint = new KinekPoint(blPackageReceipt.getKinekPoint());
		}
		packages = construct(blPackageReceipt.getPackages(), Package.class);
		if (blPackageReceipt.getRedirectReason() != null) {
			redirectReason = blPackageReceipt.getRedirectReason().getName();
		}
		receivedDate = blPackageReceipt.getReceivedDate();
		requiresProofOfPurchase = blPackageReceipt.isRequiresProofOfPurchase();
	}

	public KinekPoint getKinekPoint() {
		return kinekPoint;
	}

	public void setKinekPoint(KinekPoint kinekPoint) {
		this.kinekPoint = kinekPoint;
	}

	public Set<Package> getPackages() {
		return packages;
	}

	public void setPackages(Set<Package> packages) {
		this.packages = packages;
	}

	public String getRedirectReason() {
		return redirectReason;
	}

	public void setRedirectReason(String redirectReason) {
		this.redirectReason = redirectReason;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public void setRequiresProofOfPurchase(boolean requiresProofOfPurchase) {
		this.requiresProofOfPurchase = requiresProofOfPurchase;
	}

	public boolean isRequiresProofOfPurchase() {
		return requiresProofOfPurchase;
	}
}
