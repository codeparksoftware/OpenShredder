package com.codepark.openshredder.types;

import java.io.File;
import java.io.FileNotFoundException;

public class FileStorage {
	File file;

	public FileStorage(File f) throws FileNotFoundException {
		this.file = f;
		
	}

	public boolean equals(FileStorage other) {
		return this.getPath().equals(other.getPath());
	}

	private String getPath() {
		return file.getAbsolutePath();
	}
}
