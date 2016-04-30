package com.codepark.openshredder.updater;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.codepark.openshredder.common.MessageBox;
import com.codepark.openshredder.jarinfo.JarUtil;
import com.codepark.openshredder.system.SystemUtil;
import com.codepark.openshredder.ui.BaseProgressPanel;

public class UpdatePanel extends BaseProgressPanel {

	boolean canceled = false;
	private static final Logger logger = Logger.getLogger(UpdatePanel.class.getName());
	public final String[] args;
	private String tmp = null;

	public UpdatePanel(String[] args) {
		this.args = args;
		setUIExtension();
		initializeTask();

	}

	protected void initializeTask() {
		start();

	}

	protected void setUIExtension() {
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stop();
				System.exit(0);// ?
			}
		});
		setIndeterminate(true);
	}

	public void work() {
		setLabel("Update starting!..");
		setIndeterminate(false);
		setLabel("File downloading!..");
		tmp = SystemUtil.getTmpDir() + "tmp.tmp";
		FileDownload fil = new FileDownload(args[1], tmp);
		fil.add(UpdatePanel.this);
		fil.start();
	}

	public boolean deleteOldFile(String oldFile) {
		File old = new File(oldFile);
		int count = 0;
		while (old.delete() != true && count < 1000) {//
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
			count++;
			if (count == 1000)
				return false;
		}
		return true;
	}

	public void finished() {

		try {
			File tmpFile = new File(tmp);
			File runFile = new File(args[0]);
			if (deleteOldFile(runFile.getAbsolutePath())) {
				Files.copy(tmpFile.toPath(), runFile.toPath());
				JarUtil.runJarFile(runFile.getAbsolutePath(), null);
				this.setVisible(false);
				MessageBox.showMessage("Update succesfully finished.", args[2] + " update", 5000);

			} else {
				System.out.println("not deleted file " + args[0]);
			}
		} catch (IOException e) {

			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		super.finished();

	}

}
