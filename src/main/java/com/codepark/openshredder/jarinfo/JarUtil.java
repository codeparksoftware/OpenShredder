package com.codepark.openshredder.jarinfo;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;

import org.apache.log4j.Logger;

import com.codepark.openshredder.common.MessageBox;
import com.codepark.openshredder.ui.MainForm;

public class JarUtil {

	private static final Logger logger = Logger.getLogger(JarUtil.class);

	public static String getExecutablePath() {
		CodeSource codeSource = MainForm.class.getProtectionDomain().getCodeSource();
		File jarFile;
		try {
			jarFile = new File(codeSource.getLocation().toURI().getPath());
		} catch (URISyntaxException e) {

			logger.debug(e.getMessage(), e);
			return "";
		}
		return jarFile.getAbsolutePath();
	}

	public static String getStartupPath() {
		return new File(getExecutablePath()).getParent();

	}

	public static URL makeJarURLforLocal(String str) {
		str = "jar:file:" + str + "!/";
		try {
			return new URL(str);
		} catch (MalformedURLException e) {
			logger.debug(e.getMessage(), e);
		}
		return null;
	}

	public static URL makeJarURLforRemote(String str) {
		str = "jar:" + str + "!/";
		try {
			return new URL(str);
		} catch (MalformedURLException e) {
			logger.debug(e.getMessage(), e);
		}
		return null;
	}

	private static boolean runJar(String str) {
		try {
			Runtime.getRuntime().exec(str);
			return true;
		} catch (IOException e) {

			logger.debug(e.getMessage(), e);
			return false;
		}
	}

	public static boolean runJarFile(String runnableJar, String[] params) {

		if (new File(runnableJar).exists() == false) {
			MessageBox.showMessage("Jar file not found.\nPlease be sure updater.jar file exist");
			return false;
		}
		String parameters = null;
		if (params != null) {
			parameters = String.join(" ", params);
		}
		return runJar("java -jar " + runnableJar + " " + parameters);

	}

}
