package com.codepark.openshredder.help;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.codepark.openshredder.common.DialogResult;
import com.codepark.openshredder.common.Level;
import com.codepark.openshredder.common.Logger;
import com.codepark.openshredder.common.MessageBox;
import com.codepark.openshredder.common.StringUtil;
import com.codepark.openshredder.jarinfo.JarAttributes;
import com.codepark.openshredder.jarinfo.JarUtil;
import com.codepark.openshredder.system.SystemUtil;
import com.codepark.openshredder.ui.BaseProgressPanel;

public class CheckVersion extends BaseProgressPanel {

	private String localFile;
	private String remoteFile;
	private boolean connected = true;
	private boolean newVersionFound = false;
	public static final String CHECK_CONNECTION = "Checking connection";
	public static final String FAIL_CONNECTION_MSG = "Internet connection not found!\nPlease check your internet connection and try again.";
	public static final String MSG_NEW_VERSION_FOUND = "New version found!\nDo you want to update now?";
	public static final String CHECK_VERSION = "Checking version";
	public static final String MSG_VERSION_NOT_FOUND = "New version not found!";
	private static final Logger logger = Logger.getLogger(CheckVersion.class.getName());

	public CheckVersion(String local, String remote) throws FileNotFoundException {

		if (StringUtil.stringIsNullorEmpty(local) || StringUtil.stringIsNullorEmpty(remote))
			throw new FileNotFoundException();
		setLocalFile(local);
		setRemoteFile(remote);
		initializeTask();
		setUIExtension();
	}

	private void setUIExtension() {
		setIndeterminate(true);
	}

	private void initializeTask() {
		CheckInternetTask check = new CheckInternetTask();
		check.execute();
	}

	@Override
	public void work() {
		setLabel(CHECK_VERSION);
		logger.log(Level.Info, CHECK_VERSION);
		newVersionFound = isNewVersionFound();
		if (newVersionFound == true) {

			DialogResult sonuc = MessageBox.showMessage(CHECK_VERSION, MSG_NEW_VERSION_FOUND);// Check-if-yes
			if (sonuc == DialogResult.Yes) {
				runUpdate();

			}
			finished();

		} else {
			finished();
			MessageBox.showMessage(MSG_VERSION_NOT_FOUND);

		}

	}

	private void runUpdate() {
		String oldFile = JarUtil.getExecutablePath();
		String newFile = "https://raw.githubusercontent.com/codeparksoftware/OpenShredder/master/Shredder.jar";
		String appName = new JarAttributes(JarUtil.makeJarURLforLocal(JarUtil.getExecutablePath())).getAppName();
		String updaterPath = JarUtil.getStartupPath() + "\\Updater.jar";
		logger.log(Level.Info, updaterPath);
		String[] params = new String[3];
		params[0] = oldFile;
		params[1] = newFile;
		params[2] = appName;
		if (JarUtil.runJarFile(updaterPath, params) == true) {
			System.exit(0);
		}
	}

	public String getLocalFile() {
		return localFile;
	}

	public void setLocalFile(String localFile) {
		this.localFile = localFile;
	}

	public String getRemoteFile() {
		return remoteFile;
	}

	private void setRemoteFile(String remoteFile) {
		this.remoteFile = remoteFile;
	}

	public void finished() {
		super.finished();
	}

	public boolean isConnected() {

		try {
			final URL url = new URL("https://www.github.com");
			final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			return true;
		} catch (IOException e) {
			logger.log(Level.Error, e.getMessage());

			return false;
		}
	}

	class CheckInternetTask extends SwingWorker<Boolean, Void> {
		@Override
		protected Boolean doInBackground() throws Exception {

			logger.log(Level.Info, CHECK_CONNECTION);
			setLabel(CHECK_CONNECTION);
			return isConnected();

		}

		@Override
		protected void done() {

			super.done();
			try {
				connected = get();
				if (!connected) {
					CheckVersion.this.setVisible(false);
					finished();
					MessageBox.showMessage(FAIL_CONNECTION_MSG, CHECK_CONNECTION, 3000);
					logger.log(Level.Info, FAIL_CONNECTION_MSG);

				} else {
					start();
				}

			} catch (ExecutionException | InterruptedException e) {

				logger.log(Level.Error, e.getMessage());
			}

		}
	}

	public boolean isNewVersionFound() {
		try {
			SystemUtil.setProxy(true);
			URL url = null;
			url = JarUtil.makeJarURLforLocal(localFile);
			String oldJarVersion = null;
			JarAttributes jar = new JarAttributes(url);
			oldJarVersion = jar.getVersion();
			url = JarUtil.makeJarURLforRemote(remoteFile);
			jar = new JarAttributes(url);
			String newJarVersion = jar.getVersion();
			System.out.println(newJarVersion + "<-->" + oldJarVersion);
			logger.log(Level.Info, newJarVersion + "<-->" + oldJarVersion);
			return newJarVersion.compareTo(oldJarVersion) > 0;
		} catch (Exception e) {

			logger.log(Level.Error, e.getMessage());
			return false;
		}
	}

	private void setNewVersionFound(boolean newVersionFound) {
		this.newVersionFound = newVersionFound;
	}

	public boolean getNewVersionFound() {
		return newVersionFound;
	}
}
