package com.codepark.openshredder.base;

/*
 @author Selami
  25.03.2016
This class take two parameter as file and wipe id
Parameter one may be disk drive path,two disk partition(volume) path,and regular file
This function doesn't guarantee file content will be clear.Because files may be protected to write
And this class has ShredObservable implements.Then it must send process completion info as percent value.
Also sends thread id information to interrupt thread from GUI.When interrupted this thread it throws FileChannelexception
This class has runnable implements and synchronized with mutex object and it supports multi thread safely
Wipe method calculates file size and how many loop needs.finally ,it wipes remainder spaces  of file.
For example, when file size is 100000 bytes and  buffer is 16384 bytes and loop will be size/buffer = 6 .
remainder space is 1696 bytes.And last part of file is not wiped yet.
when remainder of the file wipes ,shredding finishes.
*/

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class Body implements ShredObservable, Runnable {
	private final File f;
	private final short id;
	private ArrayList<ShredObserver> lst;
	private final Object MUTEX;
	private boolean changed;
	private Thread t;

	public Body(File f, short id) {
		MUTEX = new Object();
		lst = new ArrayList<ShredObserver>();
		this.f = f;
		this.id = id;
		t = new Thread(this);
	}

	public void startWipe() {

		t.start();

		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void percent(long currentValue, long totalMax) {
		this.changed = true;
		notifyServer((currentValue * 100) / totalMax);

	}

	public void finalize() {
		lst.clear();

	}

	public void SetFileLength() {
		try {
			RandomAccessFile rand = new RandomAccessFile(f, "rws");
			rand.setLength(0);
			rand.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void add(ShredObserver o) {
		if (o == null)
			throw new NullPointerException("Null Observer");
		synchronized (MUTEX) {
			if (!lst.contains(o))
				lst.add(o);
		}

	}

	@Override
	public void remove(ShredObserver o) {
		synchronized (MUTEX) {
			lst.remove(o);
		}

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

	@Override
	public void notifyServer(long val) {
		List<ShredObserver> list;
		synchronized (MUTEX) {
			list = new ArrayList<>(this.lst);
		}
		for (ShredObserver o : list) {
			o.update((int) val);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void Wipe() {
		RandomAccessFile out = null;
		FileChannel ch = null;
		try {
			out = new RandomAccessFile(f.getAbsolutePath(), "rws");
			ch = out.getChannel();
			int buffer = 8192 * 2;
			long call = 0;
			long fsize = out.length();
			if (fsize == 0)
				return;
			if (fsize < 8192 * 2)
				buffer = (int) fsize;
			int lastPart = (int) (fsize % buffer);
			long loop = (fsize - lastPart) / buffer;
			ch.position(0);
			ch.force(true);
			percent(call, fsize);
			addThreadId(Thread.currentThread().getId());
			for (int i = 0; i < loop && !Thread.currentThread().isInterrupted(); i++) {
				ch.write(new WipeValues(buffer, id).GenerateValue());
				call += buffer;
				percent(call, fsize);
			}
			ch.write(new WipeValues(lastPart, id).GenerateValue());
			call += lastPart;
			percent(call, fsize);
		} catch (IOException e2) {
			   Thread.currentThread().interrupt();
			e2.printStackTrace();
		} finally {
			try {
				removeThreadId(Thread.currentThread().getId());
				ch.close();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	public void run() {
		Wipe();
	}

}
