package com.codepark.openshredder.updater;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.codepark.openshredder.shred.Job;
import com.codepark.openshredder.system.SystemUtil;

public class FileDownload extends Job {

	private static final Logger logger = Logger.getLogger(FileDownload.class);
	private String urlPath = null;
	private String savePath = null;
	private short BYTE_BUFFER = 4096;

	public FileDownload(String urlPath, String savePath) {
		this.urlPath = urlPath;
		this.savePath = savePath;

	}

	public boolean deleteOldFile(String oldFile) {
		File old = new File(oldFile);
		int count = 0;
		while (old.delete() != true && count < 1000) {//
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.debug(e.getMessage(), e);
			}
			count++;
			if (count == 1000)
				return false;
		}
		return true;
	}

	@Override
	public void doJob() {
		if (SystemUtil.isConnected() == false)
			return;
		OutputStream out = null;
		HttpURLConnection conn = null;
		InputStream in = null;
		try {
			addThreadId(Thread.currentThread().getId());
			URL url = new URL(urlPath);
			out = new BufferedOutputStream(new FileOutputStream(savePath));
			conn = (HttpURLConnection) url.openConnection();
			in = conn.getInputStream();
			long total = conn.getContentLengthLong();
			byte[] buffer = new byte[BYTE_BUFFER];
			int numRead;
			long numWritten = 0;

			while ((numRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
				numWritten += numRead;
				percent(numWritten, total);
			}
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
			return;
		} finally {
			removeThreadId(Thread.currentThread().getId());
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ioe) {
				logger.debug(ioe.getMessage(), ioe);
				return;
			}
		}
		return;

	}

}
