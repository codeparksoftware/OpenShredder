package com.codepark.openshredder.ui;

import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Dialog {

	private JDialog dialog;

	public Dialog() {
	}

	public void createDialog(Component arg0, Component frm, String str, Dimension dim) {

		JComponent comp = (JComponent) arg0;
		Window win = (Window) SwingUtilities.getWindowAncestor(comp);
		if (win != null) {
			dialog = new JDialog(win, str, ModalityType.APPLICATION_MODAL);
			dialog.getContentPane().add(frm);
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setSize(dim);
			dialog.setLocationRelativeTo(comp);

		}

		dialog.setVisible(true); // here the modal dialog takes over
	}

	public void createDialog(Component arg0, BaseProgressPanel frm, String str, Dimension dim) {

		JComponent comp = (JComponent) arg0;
		Window win = (Window) SwingUtilities.getWindowAncestor(comp);
		if (win != null) {
			dialog = new JDialog(win, str, ModalityType.APPLICATION_MODAL);
			dialog.getContentPane().add(frm);
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setSize(dim);
			dialog.setLocationRelativeTo(comp);
			dialog.addWindowListener(new WindowAdapter() {
				public void windowClosed(WindowEvent e) {
					frm.stop();
				}
			});
		}

		dialog.setVisible(true); // here the modal dialog takes over
	}

	public void createDialog(BaseProgressPanel frm, String str, Dimension dim) {
		dialog = new JDialog();
		dialog.setTitle(str);
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		dialog.getContentPane().add(frm);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.setSize(dim);
		dialog.setLocationRelativeTo(null);
		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				frm.stop();
			}
		});
		dialog.setVisible(true);

	}
}
