package com.codepark.openshredder.types;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

import com.codepark.openshredder.exceptions.NotFolderException;

public class Folder {
	private static final Logger logger = Logger.getLogger(Folder.class);
	private final File folder;

	public Folder(String path) throws FileNotFoundException, NotFolderException {
		this.folder = new File(path);
		if (!folder.isDirectory()) {

			throw new NotFolderException(folder.getAbsolutePath());

		}
		if (this.folder.exists())
			throw new FileNotFoundException(folder.getAbsolutePath());

	}

	public boolean equals(Folder other) {
		return this.getPath().equals(other.getPath());
	}

	private String getPath() {
		return folder.getAbsolutePath();
	}
}
