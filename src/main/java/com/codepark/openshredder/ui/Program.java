package com.codepark.openshredder.ui;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import com.codepark.openshredder.system.SystemUtil;

public class Program {
	private static final Logger logger = Logger.getLogger(Program.class);

	public static void main(String[] args) {

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
