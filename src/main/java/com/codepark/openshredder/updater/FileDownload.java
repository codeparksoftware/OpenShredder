package com.codepark.openshredder.updater;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.codepark.openshredder.shred.Job;
import com.codepark.openshredder.system.SystemUtil;

public class FileDownload extends Job {

	private static final Logger logger = Logger.getLogger(FileDownload.class.getName());
	private String urlPath = null;
	private String savePath = null;
	private short BYTE_BUFFER = 4096;

	public FileDownload(String urlPath, String savePath) {
		this.urlPath = urlPath;
		this.savePath = savePath;
	}

	@Override
	public void doJob() {
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
			logger.log(Level.SEVERE, e.getMessage(), e);

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
				logger.log(Level.SEVERE, ioe.getMessage(), ioe);

			}
		}
		return;

	}

}
