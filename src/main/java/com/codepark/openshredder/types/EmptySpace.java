package com.codepark.openshredder.types;

import java.io.File;
import java.io.FileNotFoundException;

import com.codepark.openshredder.exceptions.NotFolderException;

public class EmptySpace {
	File file;

	public EmptySpace(File f) throws FileNotFoundException, NotFolderException {
		this.file = f;
		if (f.isDirectory() == false)
			throw new NotFolderException(f.getAbsolutePath());
	}

	public boolean equals(EmptySpace other) {
		return this.getPath().equals(other.getPath());
	}

	private String getPath() {
		return file.getAbsolutePath();
	}
}
