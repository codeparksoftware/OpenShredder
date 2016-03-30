package com.codepark.openshredder.ui;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Program {

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel( new UIFactory().getDefaultUI());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {

				MainForm frm = new MainForm();
				frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				 
				frm.setVisible(true);
			
			}

		});

	}

}
