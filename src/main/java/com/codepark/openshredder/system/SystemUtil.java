package com.codepark.openshredder.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class SystemUtil {
	private SystemUtil() {
	}

	private static final Logger logger = Logger.getLogger(SystemUtil.class);

	public static void setProxy(boolean val) {
		System.setProperty("java.net.useSystemProxies", String.valueOf(val));
	}

	public static String getTmpDir() {
		String property = "java.io.tmpdir";

		String tmpDir = System.getProperty(property);
		return tmpDir;
	}

	public static boolean isConnected() {

		try {
			final URL url = new URL("https://www.github.com");
			final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			return true;
		} catch (IOException e) {
			logger.debug(e.getMessage());

			return false;
		}
	}

	public static <T> List<T> GetExec(String param) {

		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		try {
			process = runtime.exec(param);
		} catch (IOException e) {
			logger.debug(e.getMessage());
		}
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;

		List<String> lst = new ArrayList<String>();

		try {
			while ((line = br.readLine()) != null) {

				lst.add(line);
			}
		} catch (IOException e) {
		logger.debug(e.getMessage());
		}
		return (List<T>) lst;
	}

}
