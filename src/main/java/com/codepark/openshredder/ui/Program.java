package com.codepark.openshredder.ui;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.codepark.openshredder.common.Level;
import com.codepark.openshredder.common.Logger;
import com.codepark.openshredder.system.SystemUtil;

public class Program {
	private static final Logger logger = Logger.getLogger(Program.class.getName());

	public static void main(String[] args) {

		

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
