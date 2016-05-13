package com.codepark.openshredder.updater;

import java.io.File;
import java.nio.file.Files;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.codepark.openshredder.common.Level;
import com.codepark.openshredder.common.Logger;
import com.codepark.openshredder.common.MessageBox;
import com.codepark.openshredder.system.SystemUtil;
import com.codepark.openshredder.ui.BaseProgressPanel;
import com.codepark.openshredder.ui.Dialog;
import com.codepark.openshredder.ui.UIFactory;

public class Update {
	/**
	 * arg[0] main application path arg[1] url to fetch new jar arg[2] App Name
	 * 
	 */
	private static final Logger logger = Logger.getLogger(Update.class.getName());

	public static void main(String[] args) {

		checkMethod(args);
		// BasicConfigurator.configure();
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {

				SystemUtil.setProxy(true);
				try {
					UIManager.setLookAndFeel(new UIFactory().getDefaultUI());
				} catch (ClassNotFoundException e) {
					logger.log(Level.Error, e.getMessage());
				} catch (InstantiationException e) {
					logger.log(Level.Error, e.getMessage());
				} catch (IllegalAccessException e) {
					logger.log(Level.Error, e.getMessage());
				} catch (UnsupportedLookAndFeelException e) {
					logger.log(Level.Error, e.getMessage());
				}

				UpdatePanel frm = new UpdatePanel(args);
				Dialog d = new Dialog();
				d.createDialog(frm, args[2] + " update", ((BaseProgressPanel) frm).getDimension());

			}

		});

	}

	/**
	 * checks argument are valid and correct.
	 * 
	 * @param args
	 */
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
