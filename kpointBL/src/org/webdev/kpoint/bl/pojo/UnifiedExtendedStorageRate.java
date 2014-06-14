package org.webdev.kpoint.bl.pojo;

import java.math.BigDecimal;

public class UnifiedExtendedStorageRate {
	private transient int id;
	private StorageWeightGroup storageWeightGroup;
	private StorageDuration storageDuration;
	private transient BigDecimal fee;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public StorageWeightGroup getStorageWeightGroup() {
		return storageWeightGroup;
	}
	public void setStorageWeightGroup(StorageWeightGroup storageWeightGroup) {
		this.storageWeightGroup = storageWeightGroup;
	}
	public StorageDuration getStorageDuration() {
		return storageDuration;
	}
	public void setStorageDuration(StorageDuration storageDuration) {
		this.storageDuration = storageDuration;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	
	
}