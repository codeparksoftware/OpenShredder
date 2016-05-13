package com.codepark.openshredder.shred;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import com.codepark.openshredder.common.Level;
import com.codepark.openshredder.common.Logger;
import com.codepark.openshredder.system.OSDetector;
import com.codepark.openshredder.system.SystemUtil;

public class MetaData extends Job {

	File file;
	Random rand;

	private static final Logger logger = Logger.getLogger(MetaData.class.getName());

	public MetaData(File f) {

		MUTEX = new Object();
		this.file = f;
		rand = new Random();
		lst = new ArrayList<ShredObserver>();

	}

	public void doJob() {

		percent(10, 100);
		SetFlag();
		percent(30, 100);
		SetCreationTime();
		percent(60, 100);
		SetLastAccessedTime();
		percent(80, 100);
		SetLastModifiedTime();
		percent(100, 100);
	}

	private void SetFlag() {
		if (OSDetector.isUnix())
			SystemUtil.GetExec("chattr +s " + file.getAbsolutePath());

	}

	public String nameClear() {

		String ext = "zzz";

		String name = String.format("%s.%s", java.util.UUID.randomUUID().toString(), ext);
		File renamed = new File(file.getParent() + File.separator + name);
		Path target = renamed.toPath();
		Path path = null;
		try {
			path = Files.move(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			logger.log(Level.Error, e.getMessage());
		}

		return path.toString();
	}

	public void SetWritable() {
		file.setWritable(true);
	}

	private void SetLastModifiedTime() {

		try {
			String date = "01.01.1990 00:00:00";
			long milis = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss").parse(date).getTime();
			FileTime fileTime = FileTime.fromMillis(milis);
			Files.setLastModifiedTime(file.toPath(), fileTime);
		} catch (IOException | ParseException e) {

			logger.log(Level.Error, e.getMessage());
		}
	}

	private void SetLastAccessedTime() {

		try {
			String date = "01.01.1990 00:00:00";
			long milis = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss").parse(date).getTime();
			FileTime fileTime = FileTime.fromMillis(milis);
			/* Change Created Time Stamp */
			Files.setAttribute(this.file.toPath(), "lastAccessTime", fileTime);
		} catch (IOException | ParseException e) {
			logger.log(Level.Error, e.getMessage());
		}
	}

	private void SetCreationTime() {

		try {
			String date = "01.01.1990 00:00:00";
			long milis = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss").parse(date).getTime();
			FileTime fileTime = FileTime.fromMillis(milis);
			/* Change Created Time Stamp */
			Files.setAttribute(this.file.toPath(), "creationTime", fileTime);
		} catch (IOException | ParseException e) {
			logger.log(Level.Error, e.getMessage());
		}

	}

}
