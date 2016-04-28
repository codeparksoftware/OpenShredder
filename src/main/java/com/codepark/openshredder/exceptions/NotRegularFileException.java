package com.codepark.openshredder.exceptions;

public class NotRegularFileException extends Exception {

	private String path = null;

	public NotRegularFileException() {
		super("It is not regular file at the specified path");
	}

	public NotRegularFileException(String path) {
		super("It is not regular file at the specified path." + path + " is not regular file");
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}
