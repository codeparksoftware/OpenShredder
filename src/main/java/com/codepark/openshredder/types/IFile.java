package com.codepark.openshredder.types;

import java.nio.file.attribute.FileTime;

import javax.swing.ImageIcon;

import com.codepark.openshredder.shred.ShredObserver;

public interface IFile {
	public String getPath();

	public String getName();

	public long getLength();

	public ImageIcon getIcon();

	public FileType getType();

	public void shred();

	public FileTime getLastModifiedTime();

	public FileTime getLastAccessTime();

	public FileTime getCreatedTime();

	public WipeMethod getWipeMethod();

	public void setWipeMethod(WipeMethod method);

	public void setObserver(ShredObserver observer);
}
