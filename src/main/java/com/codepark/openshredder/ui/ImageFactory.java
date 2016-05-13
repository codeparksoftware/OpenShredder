package com.codepark.openshredder.ui;

import javax.swing.ImageIcon;

import com.codepark.openshredder.types.FileType;

public class ImageFactory {
	public static ImageIcon IconType(FileType type) {

		Images img = new Images();
		if (type == FileType.RegularFile) {
			return img.getFileImg();
		} else if (type == FileType.Folder) {
			return img.getDirImg();
		} else if (type == FileType.FileStorage) {
			return img.getDiskImg();
		} else
			return img.defImg;

	}

}
