package com.codepark.openshredder.types;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import com.codepark.openshredder.exceptions.NotFolderException;
import com.codepark.openshredder.shred.MetaData;
import com.codepark.openshredder.shred.ShredObserver;
import com.codepark.openshredder.ui.ImageFactory;

public class Folder implements IFile {
	private static final Logger logger = Logger.getLogger(Folder.class.getName());
	protected File file;
	protected FileTime createdTime;
	protected FileTime lastAccessTime;
	protected FileTime lastModifiedTime;
	protected boolean isWritable;
	protected ShredObserver observer;
	protected WipeMethod method;

	/**
	 * public Folder(String path) throws FileNotFoundException,
	 * NotFolderException { Constructs and Initializes Folder class this class
	 * used for folders such as C:\ ,/etc/
	 * 
	 * @param path
	 * @throws FileNotFoundException
	 */
	public Folder(String path) throws IOException, FileNotFoundException {

		init(path);
		initMetaData();

	}

	protected void init(String path) throws FileNotFoundException {
		this.file = new File(path);
		checkExist();
		if (!file.isDirectory()) {

			try {
				throw new NotFolderException(file.getAbsolutePath());
			} catch (NotFolderException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}

		}

	}

	protected void checkExist() throws FileNotFoundException {

		if (this.file.exists() == false) {

			throw new FileNotFoundException(this.file.getAbsolutePath());
		}
	}

	protected void initMetaData() throws IOException {

		setCreatedTime((FileTime) Files.getAttribute(file.toPath(), "creationTime"));
		setLastModifiedTime((FileTime) Files.getAttribute(file.toPath(), "lastModifiedTime"));
		setLastAccessTime((FileTime) Files.getAttribute(file.toPath(), "lastAccessTime"));
		setWritable(Files.isWritable(file.toPath()));
	}

	/**
	 * checks two objects are have same path
	 * 
	 * @param other
	 *            Folder object to compare with this
	 * @return if both objects are same return true Compares this Folder to the
	 *         specified object. The result is true if and only if the argument
	 *         is not null and is a Folder object that represents the same path
	 *         as this object.
	 */
	public boolean equals(Folder other) {
		return this.getPath().equals(other.getPath());
	}

	/**
	 * Returns the absolute pathname string of this abstract pathname.
	 * 
	 */
	@Override
	public String getPath() {
		return file.getAbsolutePath();
	}

	/**
	 * Returns the length of the file denoted by this abstract pathname
	 */
	@Override
	public long getLength() {
		return file.length();
	}

	/**
	 * Returns the Virtual ImageIcon of the file denoted by this type
	 */
	@Override
	public ImageIcon getIcon() {
		return ImageFactory.IconType(getType());
	}

	/**
	 * Returns the Virtual FileType of the file denoted by Folder always return
	 * FileType.Folder
	 */
	@Override
	public FileType getType() {
		return FileType.Folder;
	}

	/**
	 * 
	 * @return created time as FileTime type of object
	 */
	public FileTime getCreatedTime() {
		return createdTime;
	}

	/**
	 * 
	 * @param createdTime
	 *            Filetime set created time of object
	 */
	private void setCreatedTime(FileTime createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * 
	 * @return last access time as FileTime type of object
	 */
	public FileTime getLastAccessTime() {
		return lastAccessTime;
	}

	/**
	 * 
	 * @param lastAccessTime
	 *            Filetime set last access time of object
	 */
	private void setLastAccessTime(FileTime lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	/**
	 * 
	 * @return last modified time as FileTime type of Type object
	 */
	public FileTime getLastModifiedTime() {
		return lastModifiedTime;
	}

	/**
	 * 
	 * @param lastAccessTime
	 *            Filetime set last modified time of object
	 */
	private void setLastModifiedTime(FileTime lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	/**
	 * 
	 * @return true if file is writable else returns false;
	 */
	private boolean isWritable() {
		return isWritable;
	}

	/**
	 * 
	 * @param isWritable
	 *            boolean set writable property
	 */
	private void setWritable(boolean isWritable) {
		this.isWritable = isWritable;
	}

	@Override
	public void shred() {
		Object[] values = (Object[]) getWipeMethod().getMethod();
		for (int i = 0; i < values.length; i++) {
			MetaData m = new MetaData(this.file);
			m.SetWritable();
			m.add(getObserver());
			m.start();
			m.finish();
		}
		file.delete();

	}

	public WipeMethod getWipeMethod() {
		return method;
	}

	public void setWipeMethod(WipeMethod method) {
		this.method = method;
	}

	public ShredObserver getObserver() {
		return observer;
	}

	public void setObserver(ShredObserver observer) {
		this.observer = observer;
	}

	@Override
	public String getName() {
		Path p = Paths.get(getPath());
		return p.getFileName().toString();
	}

}
