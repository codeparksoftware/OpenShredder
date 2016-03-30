package com.codepark.openshredder.ui;

public class DiskModel {
	public DiskModel(String[] f) {

		setType(f[0]);
		setLabel(f[1]);
		setdeviceName(f[2]);
		setSize(f[3]);
		setPath(f[4]);
		setMountPoint(f[5]);
		if (f[2].equals("part"))
			setdeviceName("    " + f[2]);

	}

	private static final short TYPE_INDEX = 0;
	private static final short LABEL_INDEX = 1;
	private static final short DEVICE_INDEX = 2;
	private static final short SIZE_INDEX = 3;
	private static final short PATH_INDEX = 4;
	private static final short MOUNT_POINT = 5;

	private String type;
	private String label;
	private String deviceName;
	private String Size;
	private String path;
	private String mountPoint;

	private String getType() {
		return type;
	}

	private void setType(String type) {
		this.type = type;
	}

	private String getLabel() {
		return label;
	}

	private void setLabel(String label) {
		this.label = label;
	}

	public String getdeviceName() {
		return deviceName;
	}

	public void setdeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getSize() {
		return Size;
	}

	private void setSize(String size) {
		Size = size;
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
		case TYPE_INDEX:
			return getType();
		case LABEL_INDEX:
			return getLabel();
		case DEVICE_INDEX:
			return getdeviceName();
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
		case TYPE_INDEX:
			setType((String) value);
		case LABEL_INDEX:
			setLabel((String) value);
		case DEVICE_INDEX:
			setdeviceName((String) value);
		case SIZE_INDEX:
			setSize((String) value);
		case PATH_INDEX:
			setPath((String) value);
		case MOUNT_POINT:
			setMountPoint((String) value);
		}

	}

}
