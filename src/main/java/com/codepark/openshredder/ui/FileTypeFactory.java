package com.codepark.openshredder.ui;

import java.io.File;

import com.codepark.openshredder.system.FileInfo;

public class FileTypeFactory {
	public FileType GetFileType(File f) {

		if (f.isDirectory())
			return FileType.Directory;
		else if (f.isFile())
			return FileType.File;
		else if (new FileInfo(f.getAbsolutePath()).isFileStorage()) {
			return FileType.FileStorage;
		} else
			return FileType.Unknown;
	}

}
