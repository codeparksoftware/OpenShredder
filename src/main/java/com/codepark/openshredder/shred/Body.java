package com.codepark.openshredder.shred;

/*
 @author Selami
  25.03.2016
This class takes two parameter as file and wipe id
Parameter one, may be disk drive path or   disk partition(volume) path or regular file
This function doesn't guarantee file content will be clear.Because files may be protected to write operation.
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class Body extends Job {

	private static final Logger logger = Logger.getLogger(Body.class.getName());
	private File f;
	private short id;
	private short BYTE_BUFFER = 16384;

	public Body(File f, short id) {

		this.f = f;
		this.id = id;

	}

	public void SetFileLength() {
		try {
			RandomAccessFile rand = new RandomAccessFile(f, "rws");
			rand.setLength(0);
			rand.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

	}

	@Override
	public void doJob() {
		RandomAccessFile out = null;
		FileChannel ch = null;
		try {
			addThreadId(Thread.currentThread().getId());
			out = new RandomAccessFile(f.getAbsolutePath(), "rws");
			ch = out.getChannel();
			int buffer = BYTE_BUFFER;
			long call = 0;
			long fsize = out.length();
			if (fsize == 0)
				return;
			if (fsize < BYTE_BUFFER)
				buffer = (int) fsize;
			int lastPart = (int) (fsize % buffer);
			long loop = (fsize - lastPart) / buffer;
			ch.position(0);
			ch.force(true);
			percent(call, fsize);
			for (int i = 0; i < loop && !Thread.currentThread().isInterrupted(); i++) {
				ch.write(new WipeValues(buffer, id).GenerateValue());
				call += buffer;
				percent(call, fsize);
			}
			ch.write(new WipeValues(lastPart, id).GenerateValue());
			call += lastPart;
			percent(call, fsize);
		} catch (IOException e) {
			Thread.currentThread().interrupt();
			logger.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			try {
				removeThreadId(Thread.currentThread().getId());
				ch.close();
				out.close();
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}

		}

	}

}
