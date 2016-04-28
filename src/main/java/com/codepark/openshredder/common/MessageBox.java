package com.codepark.openshredder.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class MessageBox {

	public static DialogResult showMessage(String message) {
		JOptionPane.showMessageDialog(null, message);
		return DialogResult.Ok;
	}

	public static DialogResult showMessage(String message, String title, int timeOut) {
		JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
		JDialog dialog = pane.createDialog(null, title);
		dialog.setModal(false);
		dialog.setVisible(true);

		new Timer(timeOut, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		}).start();
		return DialogResult.Ok;
	}

	public static DialogResult showMessage(String message, String title) {
		JOptionPane pane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
		int reply = pane.showConfirmDialog(null, title);
		if (reply == JOptionPane.YES_OPTION) {
			return DialogResult.Yes;
		} else
			return DialogResult.No;

	}

}
