package com.codepark.openshredder.types;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;

import javax.swing.ImageIcon;

import com.codepark.openshredder.common.Level;
import com.codepark.openshredder.common.Logger;
import com.codepark.openshredder.shred.Body;
import com.codepark.openshredder.shred.ShredObserver;
import com.codepark.openshredder.ui.ImageFactory;

public class FileStorage implements IFile {
	File file;
	RandomAccessFile randFile = null;
	private static final Logger logger = Logger.getLogger(FileStorage.class.getName());
	private ShredObserver observer;
	private WipeMethod method;

	/**
	 * Constructs and Initializes FileStorage class this class used for disk
	 * device or partition.
	 * 
	 * @param path
	 *            is path of specified FileStorage
	 * 
	 * @throws FileNotFoundException
	 */

	public FileStorage(String path) throws FileNotFoundException {
		this.file = new File(path);
		logger.log(Level.Info, path);
		logger.log(Level.Info, file.getAbsolutePath());

		randFile = new RandomAccessFile(path, "r");

	}

	/**
	 * checks two objects are have same path
	 * 
	 * @param other
	 *            FileStorage object to compare with this
	 * @return if both objects are same returns true. Compares this FileStorage
	 *         to the specified object. The result is true if and only if the
	 *         argument is not null and is a FileStorage object that represents
	 *         the same path as this object.
	 */
	public boolean equals(FileStorage other) {
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
	 * Returns the total space of the FileStorage denoted by this abstract
	 * pathname
	 */
	@Override
	public long getLength() {
		try {

			FileChannel fc = FileChannel.open(file.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE);
			// fc.write(new WipeValues(1024, 0).GenerateValue());
			System.out.println(fc.size());
			return fc.size();
		} catch (IOException e) {
			logger.log(Level.Error, e.getMessage());

			return 99;
		}
	}

	/**
	 * Returns the Virtual ImageIcon of the file denoted by this type
	 */
	@Override
	public ImageIcon getIcon() {
		return ImageFactory.IconType(getType());
	}

	/**
	 * Returns the Virtual FileType of the file denoted by FileStorage always
	 * return FileType.FileStorage
	 */
	@Override
	public FileType getType() {
		return FileType.FileStorage;
	}

	@Override
	public void shred() {

		Body bd = null;
		Object[] values = (Object[]) method.getMethod();
		for (int i = 0; i < values.length; i++) {
			bd = new Body(file, values[i]);
			bd.add(this.observer);
			bd.start();// Wiping body
		}
		bd.finish();

	}

	@Override
	public FileTime getLastModifiedTime() {

		return null;
	}

	@Override
	public FileTime getLastAccessTime() {

		return null;
	}

	@Override
	public FileTime getCreatedTime() {

		return null;
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

	public String getName() {
		Path p = Paths.get(getPath());
		return p.getFileName().toString();
	}

}
