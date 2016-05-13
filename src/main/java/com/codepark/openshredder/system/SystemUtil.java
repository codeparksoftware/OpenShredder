package com.codepark.openshredder.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.codepark.openshredder.common.Level;
import com.codepark.openshredder.common.Logger;
import com.codepark.openshredder.common.Strings;

public class SystemUtil {
	private SystemUtil() {
	}

	private static final Logger logger = Logger.getLogger(SystemUtil.class.getName());

	public static void setProxy(boolean val) {
		System.setProperty("java.net.useSystemProxies", String.valueOf(val));
	}

	public static String getTmpDir() {
		String property = "java.io.tmpdir";

		String tmpDir = System.getProperty(property);
		return tmpDir;
	}

	public static <T> List<T> GetExec(String param) {

		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		try {
			process = runtime.exec(param);
		} catch (IOException e) {
			logger.log(Level.Error, Strings.ERROR_CODE + ":3180" + e.getMessage());
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
			logger.log(Level.Error, Strings.ERROR_CODE + ":3181" + e.getMessage());
		}
		return (List<T>) lst;
	}

}
