package com.codepark.openshredder.base;
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
import java.util.List;
import java.util.Random;
import com.codepark.openshredder.system.OSDetector;
import com.codepark.openshredder.system.SystemUtil;

public class MetaData implements ShredObservable, Runnable {
	boolean changed;
	File file;
	Random rand;
	ArrayList<ShredObserver> lst;
	final Object MUTEX;
	private Thread t;

	public MetaData(File f) {

		MUTEX = new Object();
		this.file = f;
		rand = new Random();
		lst = new ArrayList<ShredObserver>();

	}

	public void startClear() {
		t = new Thread(this);
		this.t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void metaDataClear() {

		this.changed = true;
		percent(10);
		SetFlag();
		percent(30);
		SetCreationTime();
		percent(60);
		SetLastAccessedTime();
		percent(80);
		SetLastModifiedTime();
		percent(100);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void SetLastAccessedTime() {

		try {
			FileTime fileTime = FileTime.fromMillis(rand.nextLong());
			/* Change Created Time Stamp */
			Files.setAttribute(this.file.toPath(), "lastAccessTime", fileTime);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void SetCreationTime() {

		try {
			FileTime fileTime = FileTime.fromMillis(rand.nextLong());
			/* Change Created Time Stamp */
			Files.setAttribute(file.toPath(), "basic:creationTime", fileTime, NOFOLLOW_LINKS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void add(ShredObserver o) {
		if (o == null)
			throw new NullPointerException();
		synchronized (MUTEX) {
			if (lst.contains(o) == false)
				lst.add(o);
		}

	}

	@Override
	public void remove(ShredObserver o) {
		synchronized (MUTEX) {
			if (lst.contains(o) == false)
				lst.remove(o);
		}

	}

	public void percent(long currentValue) {
		this.changed = true;
		notifyServer(currentValue);

	}

	@Override
	public void notifyServer(long val) {
		List<ShredObserver> list;
		synchronized (MUTEX) {

			if (changed == false)
				return;// Hiç değişiklik yok demektir
			list = new ArrayList<>(this.lst);

		}
		for (ShredObserver o : list) {
			o.update((int) val);

		}

	}

	public void finalize() {
		lst.clear();

	}

	@Override
	public void run() {

		metaDataClear();

	}

	public void addThreadId(long val) {

		List<ShredObserver> list;
		synchronized (MUTEX) {

			list = new ArrayList<>(this.lst);

		}
		for (ShredObserver o : list) {
			o.addThreadId(val);
		}

	}

	public void removeThreadId(long val) {

		List<ShredObserver> list;
		synchronized (MUTEX) {

			list = new ArrayList<>(this.lst);

		}
		for (ShredObserver o : list) {
			o.removeThreadId(val);
		}

	}

}
