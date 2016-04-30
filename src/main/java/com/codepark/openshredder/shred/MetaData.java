package com.codepark.openshredder.shred;

/*
Author Selami
 25.03.2016
This class used to clean metadata of files such as last accestime,crationtime etc.This information
separately keeps from file content.This information used to find info about file in digital forensic analyses and criminal 
observation.a true secure deletion must provide to clear this metadata tags.
Therefore this file information must be cleaned.and
Also this class has ShredObservable implements.Then it must send process completion info as percent value.
Also sends thread id information to interrupt thread from GUI.
SetFlag is secure deletion mark and it is valid for unix system.
Name clear function is not implemented yet.
*/
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

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
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return path.toString();
	}

	public void SetWritable() {
		file.setWritable(true);
	}

	private void SetLastModifiedTime() {

		try {
			FileTime fileTime = FileTime.fromMillis(rand.nextLong());
			Files.setLastModifiedTime(file.toPath(), fileTime);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void SetLastAccessedTime() {

		try {
			FileTime fileTime = FileTime.fromMillis(rand.nextLong());
			/* Change Created Time Stamp */
			Files.setAttribute(this.file.toPath(), "lastAccessTime", fileTime);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	private void SetCreationTime() {

		try {
			FileTime fileTime = FileTime.fromMillis(rand.nextLong());
			/* Change Created Time Stamp */
			Files.setAttribute(file.toPath(), "basic:creationTime", fileTime, NOFOLLOW_LINKS);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
