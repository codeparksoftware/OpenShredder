package com.codepark.openshredder.ui;

import javax.swing.ImageIcon;

public class Images {
 
	public ImageIcon defImg = new ImageIcon("/com/sun/java/swing/plaf/windows/icons/image-delayed.png");

	public ImageIcon getFileImg() {
		return new ImageIcon(getClass().getResource("/javax/swing/plaf/metal/icons/ocean/file.gif"));
	}

	public ImageIcon getDirImg() {
		return new ImageIcon(getClass().getResource("/javax/swing/plaf/metal/icons/ocean/directory.gif"));
	}

	public ImageIcon getPartImg() {
		return new ImageIcon(getClass().getResource("/images/Partition-Magic-icon.png"));

	}

	protected ImageIcon getDiskImg() {
		return new ImageIcon(getClass().getResource("/images/disk-icon.png"));
	}

	 
}
