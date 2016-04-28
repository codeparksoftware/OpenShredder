package com.codepark.openshredder.types;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class FileStorage {
	File file;

	public FileStorage(File f) throws FileNotFoundException {
		this.file = f;
		RandomAccessFile file = new RandomAccessFile(f, "rws");
		if(file==null)
			throw new NullPointerException(f.getAbsolutePath());
	}

	public boolean equals(FileStorage other) {
		return this.getPath().equals(other.getPath());
	}

	private String getPath() {
		return file.getAbsolutePath();
	}
}
