package com.codepark.openshredder.types;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.codepark.openshredder.ui.ShredProgress;

public class FileTypeFactory {
	private static final Logger logger = Logger.getLogger(ShredProgress.class.getName());

	public static IFile createIFile(FileType type, String path, WipeMethod method) {
		if (type == FileType.RegularFile) {
			RegularFile regularFile = null;
			try {
				regularFile = new RegularFile(path);
				regularFile.setWipeMethod(method);
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
			return regularFile;
		} else if (type == FileType.Folder) {
			Folder folder = null;
			try {
				folder = new Folder(path);
				folder.setWipeMethod(method);
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
			return folder;
		} else if (type == FileType.FileStorage) {
			FileStorage fileStorage = null;
			try {
				fileStorage = new FileStorage(path);
				fileStorage.setWipeMethod(method);
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}

			return fileStorage;
		} else if (type == FileType.EmptySpace) {
			
		}
		return null;
	}
}
