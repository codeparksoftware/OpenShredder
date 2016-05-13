package com.codepark.openshredder.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

public class Logger {
	private static Object MUTEX = new Object();
	private String className;
	private static String path = Strings.PROD_NAME + ".log";

	private Logger(String className) {
		this.setClassName(className);
	}

	private static Logger logger;

	public static Logger getLogger(String className) {
		if (logger == null) {

			synchronized (MUTEX) {
				logger = new Logger(className);
			}

		}
		return logger;
	}

	public void log(Level level, String msg) {
		if (!Files.exists(Paths.get(path))) {
			try {
				Files.createFile(Paths.get(path));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {

			Files.write(Paths.get(path),(new Date().toLocaleString()+"\n").getBytes(), StandardOpenOption.APPEND);
			System.out.println(new Date().toString());
			Files.write(Paths.get(path), (level.toString() + ":  " + getClassName()+"\n").getBytes(),
					StandardOpenOption.APPEND);
			System.out.println((level.toString() + ":  " + getClassName()));
			Files.write(Paths.get(path), (msg+"\n").getBytes(), StandardOpenOption.APPEND);
			System.out.println(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getClassName() {
		return className;
	}

	private void setClassName(String className) {
		this.className = className;
	}
}
