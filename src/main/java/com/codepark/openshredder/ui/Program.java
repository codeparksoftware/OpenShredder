package com.codepark.openshredder.ui;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.codepark.openshredder.common.Strings;
import com.codepark.openshredder.system.SystemUtil;

public class Program {
	private static final Logger logger = Logger.getLogger(Program.class.getName());

	public static void setLogger() {
		Handler consoleHandler = null;
		Handler fileHandler = null;
		consoleHandler = new ConsoleHandler();
		try {
			fileHandler = new FileHandler(Strings.PROD_NAME + ".log");
			logger.info(Strings.PROD_NAME + ".log");
		} catch (SecurityException | IOException e) {
			logger.severe(e.getMessage());
		}
		logger.addHandler(consoleHandler);
		logger.addHandler(fileHandler);
		consoleHandler.setLevel(Level.ALL);
		fileHandler.setLevel(Level.ALL);
		logger.setLevel(Level.ALL);
		logger.config("Configuration done.");

	}

	public static void main(String[] args) {
		setLogger();
		try {
			UIManager.setLookAndFeel(new UIFactory().getDefaultUI());
		} catch (ClassNotFoundException e) {
			logger.severe(e.getMessage());
		} catch (InstantiationException e) {
			logger.severe(e.getMessage());
		} catch (IllegalAccessException e) {
			logger.severe(e.getMessage());
		} catch (UnsupportedLookAndFeelException e) {
			logger.severe(e.getMessage());
		}

		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				SystemUtil.setProxy(true);
				MainForm frm = new MainForm();
				frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frm.setLocationRelativeTo(null);
				frm.setVisible(true);

			}

		});

	}

}
