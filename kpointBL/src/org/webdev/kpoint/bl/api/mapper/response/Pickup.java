package org.webdev.kpoint.bl.api.mapper.response;

import static org.webdev.kpoint.bl.api.mapper.response.util.CollectionConstructor.construct;

import java.util.Date;
import java.util.Set;

import org.webdev.kpoint.bl.api.mapper.response.kinekpoint.KinekPoint;

public class Pickup {

	private Integer pickupId = -1;
	private String transactionId;
	private Date pickupDate;
	private KinekPoint kinekPoint;
	private User consumer;
	private Set<Package> packages;
	private boolean isFlaggedByConsumer;

	public Pickup(org.webdev.kpoint.bl.pojo.Pickup pickup) {
		pickupId = pickup.getPickupId();
		transactionId = pickup.getTransactionId();
		pickupDate = pickup.getPickupDate();
		if (pickup.getKinekPoint() != null) {
			kinekPoint = new KinekPoint(pickup.getKinekPoint());
		}
		if (pickup.getConsumer() != null) {
			consumer = new User(pickup.getConsumer());
		}
		packages = construct(packages, Package.class);
	}

	public Integer getPickupId() {
		return pickupId;
	}

	public void setPickupId(Integer pickupId) {
		this.pickupId = pickupId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Date getPickupDate() {
		return pickupDate;
	}

	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}

	public KinekPoint getKinekPoint() {
		return kinekPoint;
	}

	public void setKinekPoint(KinekPoint kinekPoint) {
		this.kinekPoint = kinekPoint;
	}

	public User getConsumer() {
		return consumer;
	}

	public void setConsumer(User consumer) {
		this.consumer = consumer;
	}

	public Set<Package> getPackages() {
		return packages;
	}

	public void setPackages(Set<Package> packages) {
		this.packages = packages;
	}

	public boolean getIsFlaggedByConsumer() {
		return isFlaggedByConsumer;
	}

	public void setIsFlaggedByConsumer(boolean isFlaggedByConsumer) {
		this.isFlaggedByConsumer = isFlaggedByConsumer;
	}
}