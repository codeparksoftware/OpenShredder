package com.codepark.openshredder.ui;

import com.codepark.openshredder.types.StorageType;

public class DiskModel {
	public DiskModel(StorageType storage) {

	 
		setSize(storage.getSize());
		setPath(storage.getStoreName());
		setMountPoint(storage.getMountPoint());

	}

	private static final short PATH_INDEX = 0;
	private static final short SIZE_INDEX = 1;
	private static final short MOUNT_POINT = 2;

	private long size;
	private String path;
	private String mountPoint;

	public long getSize() {
		return size;
	}

	private void setSize(long size) {
		this.size = size;
	}

	public String getPath() {
		return path;
	}

	private void setPath(String path) {
		this.path = path;
	}

	public String getMountPoint() {
		return mountPoint;
	}

	private void setMountPoint(String mountPoint) {
		this.mountPoint = mountPoint;
	}

	public Object GetValue(int colIndex) {
		switch (colIndex) {

		case SIZE_INDEX:
			return getSize();
		case PATH_INDEX:
			return getPath();
		case MOUNT_POINT:
			return getMountPoint();
		default:
			return new Object();
		}

	}

	public void setValue(Object value, int colIndex) {
		switch (colIndex) {

		case SIZE_INDEX:
			setSize((long) value);
		case PATH_INDEX:
			setPath((String) value);
		case MOUNT_POINT:
			setMountPoint((String) value);
		}

	}

}
