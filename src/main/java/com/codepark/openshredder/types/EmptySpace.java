package com.codepark.openshredder.types;

/*
 * This class used to wipe empty space in file storage that is mounted.
 * And clear by Zero Pass.
 * Calculates how many files needed And calculates remainder space.
 * And Creates file by this size.And wipe these files.
 * Max File Limit is Fat32 max file limit .That is equal 4294967296L -1
 * Error code:2180
 */
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import com.codepark.openshredder.common.Level;
import com.codepark.openshredder.common.Logger;
import com.codepark.openshredder.common.Strings;
import com.codepark.openshredder.shred.Body;
import com.codepark.openshredder.shred.MetaData;
import com.codepark.openshredder.shred.ShredObserver;

public class EmptySpace extends Folder {
	File file;
	private final long MAX_LIMITS = 4294967295L;// This's Fat32 File system
												// limit
	private static final Logger logger = Logger.getLogger(EmptySpace.class.getName());
	private ShredObserver observer;
	private WipeMethod method;

	public EmptySpace(String path) throws IOException {
		super(path);
	}

	public FileType getType() {
		return FileType.EmptySpace;
	}

	@Override
	public long getLength() {
		return file.getFreeSpace();
	}

	public File RandomFile() {

		String ext = "zzz";

		String name = String.format("%s.%s", java.util.UUID.randomUUID().toString(), ext);

		File file = new File(this.file, name);
		logger.log(Level.Info, file.getAbsolutePath());
		try {
			file.createNewFile();
		} catch (IOException e) {
			logger.log(Level.Error, Strings.ERROR_CODE + ":2180" + e.getMessage());

		}
		return file;
	}

	protected void startShredEmptySpace() {
		List<File> tmp = createFilesToWrite();
		for (int i = 0; i < tmp.size(); i++) {
			shredEmptySpace(tmp.get(i));
		}
		for (int i = 0; i < tmp.size(); i++) {
			tmp.get(i).delete();
		}
	}

	public void shred() {
		startShredEmptySpace();
	}

	private void shredEmptySpace(File tmp) {
		logger.log(Level.Info, "size: " + file.length());

		MetaData m = new MetaData(tmp);
		m.add(this.observer);
		m.SetWritable();
		Body bd = null;
		Object[] values = (Object[]) method.getMethod();
		for (int i = 0; i < values.length; i++) {
			bd = new Body(tmp, values[i]);
			bd.add(this.observer);
			bd.start();// Wiping body
		}
		m.start();

	}

	private List<File> createFilesToWrite() {
		List<RandomAccessFile> randList = new ArrayList<RandomAccessFile>();
		List<File> lst = new ArrayList<File>();
		RandomAccessFile tmpRand;
		try {
			long size = file.getFreeSpace();

			logger.log(Level.Info, "total size: " + size);
			logger.log(Level.Info, "" + this.file.getFreeSpace());
			long loop = size / MAX_LIMITS;
			logger.log(Level.Info, "loop: " + loop);
			long slack = size - (loop * MAX_LIMITS);
			logger.log(Level.Info, "slack: " + slack);
			for (int i = 0; i < loop; i++) {
				File tmp = RandomFile();
				lst.add(tmp);

				tmpRand = new RandomAccessFile(tmp, "rws");
				randList.add(tmpRand);
				tmpRand.setLength(MAX_LIMITS);
				tmpRand.close();
			}
			File tmp = RandomFile();
			tmpRand = new RandomAccessFile(tmp, "rws");
			tmpRand.setLength(slack - (1024 * (loop + 1)) - 1);
			tmpRand.close();
			lst.add(tmp);
			return lst;

		} catch (IOException e) {
			logger.log(Level.Error, Strings.ERROR_CODE + ":2182" + e.getMessage());
		}
		return lst;
	}

}
