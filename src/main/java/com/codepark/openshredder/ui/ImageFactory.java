package com.codepark.openshredder.ui;

import javax.swing.ImageIcon;

public class ImageFactory {
	public ImageIcon IconType(FileType type) {

		Images img = new Images();
		if (type == FileType.File) {
			return img.getFileImg();
		} else if (type == FileType.Directory) {
			return img.getDirImg();
		} else if (type == FileType.FileStorage) {
			return img.getDiskImg();
		} else
			return img.defImg;

	}

}
