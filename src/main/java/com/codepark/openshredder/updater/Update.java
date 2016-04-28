package com.codepark.openshredder.updater;

import java.awt.Component;
import java.io.File;
import java.nio.file.Files;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import com.codepark.openshredder.common.MessageBox;
import com.codepark.openshredder.system.SystemUtil;
import com.codepark.openshredder.ui.BaseProgressPanel;
import com.codepark.openshredder.ui.Dialog;
import com.codepark.openshredder.ui.Program;
import com.codepark.openshredder.ui.UIFactory;

public class Update {
	/**
	 * arg[0] main application path arg[1] url to fetch new jar arg[2] App Name
	 * 
	 */
	private static final Logger logger = Logger.getLogger(Program.class);

	public static void main(String[] args) {
		checkMethod(args);
//		BasicConfigurator.configure();
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				SystemUtil.setProxy(true);
				try {
					UIManager.setLookAndFeel(new UIFactory().getDefaultUI());
				} catch (ClassNotFoundException e) {
					logger.debug(e.getMessage(), e);
				} catch (InstantiationException e) {
					logger.debug(e.getMessage(), e);
				} catch (IllegalAccessException e) {
					logger.debug(e.getMessage(), e);
				} catch (UnsupportedLookAndFeelException e) {
					logger.debug(e.getMessage(), e);
				}

				UpdatePanel frm = new UpdatePanel(args);
				Dialog d = new Dialog();
				d.createDialog((Component) frm, args[2] + " update", ((BaseProgressPanel) frm).getDimension());

			}

		});

	}

	private static void checkMethod(String[] args) {
		if (args.length != 3) {
			MessageBox.showMessage(
					"This application cannot run by directly launching the jar package.\n\nThe program will now terminate");
			System.exit(0);
		}
		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
			if (args[i].trim().isEmpty() || args[i] == null) {
				MessageBox.showMessage("Arguments are invalid\nPlease be sure arguments are correct.");
				System.exit(0);
			}
		}
		if (Files.exists(new File(args[0]).toPath()) == false) {
			MessageBox.showMessage(args[0] + "\n Couldn't find specified file!");
			System.exit(0);
		}
	}

}
