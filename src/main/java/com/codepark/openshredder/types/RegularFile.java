package com.codepark.openshredder.types;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

import com.codepark.openshredder.exceptions.NotRegularFileException;

public class RegularFile {
	private static final Logger logger = Logger.getLogger(RegularFile.class.getName());
	private final File regularFile;

	public RegularFile(String path) throws NotRegularFileException, FileNotFoundException {
		this.regularFile = new File(path);
		if (!regularFile.isFile()) {

			throw new NotRegularFileException(regularFile.getAbsolutePath());

		}
		if (!regularFile.exists())
			throw new FileNotFoundException(regularFile.getAbsolutePath());

	}

	public boolean equals(RegularFile other) {
		return this.getPath().equals(other.getPath());
	}

	private String getPath() {
		return regularFile.getAbsolutePath();
	}

}
