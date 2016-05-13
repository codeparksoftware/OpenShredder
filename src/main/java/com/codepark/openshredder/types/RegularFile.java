package com.codepark.openshredder.types;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.codepark.openshredder.common.Level;
import com.codepark.openshredder.common.Logger;
import com.codepark.openshredder.exceptions.NotRegularFileException;
import com.codepark.openshredder.shred.Body;
import com.codepark.openshredder.shred.MetaData;

public class RegularFile extends Folder {
	private static final Logger logger = Logger.getLogger(RegularFile.class.getName());

	/**
	 * public RegularFile(String path) throws NotRegularFileException,
	 * FileNotFoundException { Constructs and Initializes RegularFile class this
	 * class used for regular files such as text,media,binary files.
	 * 
	 * @param path
	 *            is path of specified file @throws NotRegularFileException when
	 *            specified file is not regular file such as folder,file storage
	 * @throws IOException
	 */

	public RegularFile(String path) throws IOException {

		super(path);

	}

	/**
	 * @param path
	 *            is used to initialize new file
	 * @throws FileNotFoundException
	 * 
	 */
	protected void init(String path) throws FileNotFoundException {
		this.file = new File(path);
		checkExist();
		if (!file.isFile()) {

			try {
				throw new NotRegularFileException(file.getAbsolutePath());
			} catch (NotRegularFileException e) {
				logger.log(Level.Info, e.getMessage());
			}

		}

	}

	/**
	 * checks two objects are have same path
	 * 
	 * @param other
	 *            RegularFile object to compare with this
	 * @return if both objects are same return true Compares this RegularFile to
	 *         the specified object. The result is true if and only if the
	 *         argument is not null and is a RegularFile object that represents
	 *         the same path as this object.
	 */
	public boolean equals(RegularFile other) {
		return this.getPath().equals(other.getPath());
	}

	/**
	 * Returns the Virtual FileType of the file denoted by RegularFile always
	 * return FileType.File
	 */
	@Override
	public FileType getType() {
		return FileType.RegularFile;
	}

	public void shred() {

		MetaData m = new MetaData(file);
		m.SetWritable();
		m.add(this.observer);
		Body bd = null;
		Object[] values = (Object[]) method.getMethod();
		for (int i = 0; i < values.length; i++) {
			bd = new Body(file, values[i]);
			bd.add(this.observer);
			bd.start();// Wiping body

		}
		// bd.SetFileLength();
		m.start();
		file.delete();
	}

}
