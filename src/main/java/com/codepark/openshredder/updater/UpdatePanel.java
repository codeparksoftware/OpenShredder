package com.codepark.openshredder.updater;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.log4j.Logger;

import com.codepark.openshredder.common.MessageBox;
import com.codepark.openshredder.jarinfo.JarUtil;
import com.codepark.openshredder.system.SystemUtil;
import com.codepark.openshredder.ui.BaseProgressPanel;

public class UpdatePanel extends BaseProgressPanel {

	boolean canceled = false;
	private static final Logger logger = Logger.getLogger(UpdatePanel.class);
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

	public void finished() {

		try {
			File tmpFile = new File(tmp);
			File runFile = new File(args[0]);
			if (Files.deleteIfExists(runFile.toPath())) {
				Files.copy(tmpFile.toPath(), runFile.toPath());
				JarUtil.runJarFile(runFile.getAbsolutePath(), null);
				this.setVisible(false);
				MessageBox.showMessage("Update succesfully finished.", args[2] + " update", 5000);

			} else {
				System.out.println("not deleted file " + args[0]);
			}
		} catch (IOException e) {

			logger.debug(e.getMessage(), e);
		}
		super.finished();

	}

}
