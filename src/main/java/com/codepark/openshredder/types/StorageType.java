package com.codepark.openshredder.types;

public class StorageType {
	private String storageType;
	private String storeName;
	private String label;
	private String mountPoint;
	private long size;

	public String getStorageType() {
		return null;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setStoreName(String name) {
		this.storeName = name;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setMountPoint(String mountPoint) {
		this.mountPoint = mountPoint;
	}

	public String getMountPoint() {
		return mountPoint;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
}
