package com.codepark.openshredder.exceptions;

public class NotFolderException extends Exception {

	private String path = null;

	public NotFolderException() {
		super("It is not folder at the specified path");
	}

	public NotFolderException(String path) {
		super("It is not folder at specified path." + path + " is not valid folder");
		this.path = path;
	}

	public String getPath() {
		return path;
	}

}
