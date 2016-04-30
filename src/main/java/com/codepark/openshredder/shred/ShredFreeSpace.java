package com.codepark.openshredder.shred;

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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.codepark.openshredder.common.Strings;
import com.codepark.openshredder.system.FileInfo;

public class ShredFreeSpace implements IShred {
	private final long MAX_LIMITS;
	protected File f;
	private static final Logger logger = Logger.getLogger(ShredFreeSpace.class.getName());

	public ShredFreeSpace(File mountPoint) {
		this.f = mountPoint;
		this.MAX_LIMITS = 4294967295L;
	}

	public File RandomFile() {

		String ext = "zzz";

		String name = String.format("%s.%s", java.util.UUID.randomUUID().toString(), ext);

		File file = new File(this.f, name);
		try {
			file.createNewFile();
		} catch (IOException e) {
			logger.log(Level.SEVERE, Strings.ERROR_CODE + ":2180" + e.getMessage(), e);

		}
		return file;
	}

	@Override
	public void Shred(ShredObserver sho) {
		startEmptySpace(sho, (short) 0);

	}

	protected void startEmptySpace(ShredObserver sho, short val) {
		List<File> tmp = SetFileToWrite();
		for (int i = 0; i < tmp.size(); i++) {
			EmptySpace(tmp.get(i), sho, (short) val);
		}
		for (int i = 0; i < tmp.size(); i++) {
			tmp.get(i).delete();
		}
	}

	protected void EmptySpace(File file, ShredObserver sho, short id) {
		logger.info("size: " + file.length());
		MetaData m = new MetaData(f);
		m.add(sho);
		m.SetWritable();
		Body bd = new Body(file, id);
		bd.add(sho);
		bd.start();// Wipe Zero
		m.start();

	}

	protected List<File> SetFileToWrite() {
		List<RandomAccessFile> randList = new ArrayList<RandomAccessFile>();
		List<File> lst = new ArrayList<File>();
		RandomAccessFile tmpRand;
		try {
			long size = new FileInfo().sizeOfMountPoint(this.f.getAbsolutePath());
			logger.info("total size " + size);
			long loop = size / MAX_LIMITS;
			logger.info("loop " + loop);
			long slack = size - (loop * MAX_LIMITS);
			logger.info("slack " + slack);
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
			logger.log(Level.SEVERE, Strings.ERROR_CODE + ":2182" + e.getMessage(), e);
		}
		return lst;
	}

}
