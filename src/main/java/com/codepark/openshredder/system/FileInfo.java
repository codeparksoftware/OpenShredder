package com.codepark.openshredder.system;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileInfo {
	private String filePath;
	private static final Logger logger = Logger.getLogger(FileInfo.class.getName());

	public FileInfo(String path) {
		this.filePath = path;
	}

	public FileInfo() {

	}

	public boolean isFileStorage() {// JUnit test yapılabilinir
		for (FileStore store : FileSystems.getDefault().getFileStores()) {
			if (store.name().contains(filePath)) {
				return true;
			}
		}
		return false;
	}

	public String getMountPoint() {
		// For cleaning empty space in storage
		for (FileStore store : FileSystems.getDefault().getFileStores()) {
			if (store.name().contains(filePath)) {
				return store.name();
			}
		}
		return "";
	}

	public String getPath(String mountPoint, String name) {// bu burada olmamalı
		if (mountPoint.trim().isEmpty())
			return "";
		for (FileStore store : FileSystems.getDefault().getFileStores()) {
			if (store.toString().contains(mountPoint) && store.toString().contains(name)) {
				return store.name();
			}
		}
		return "";
	}

	public boolean isMountPoint(String mountpoint) {
		for (FileStore store : FileSystems.getDefault().getFileStores()) {
			if (store.toString().contains(mountpoint)) {
				return true;
			}
		}
		return false;
	}

	public long sizeOfMountPoint(String mountpoint) {
		for (FileStore store : FileSystems.getDefault().getFileStores()) {
			if (store.toString().contains(mountpoint)) {
				try {
					return store.getUsableSpace();
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
		return 0;
	}
}