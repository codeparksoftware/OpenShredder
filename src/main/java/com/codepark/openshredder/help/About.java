/**
 * 
 */
package com.codepark.openshredder.help;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * @author root
 *
 */
public class About extends JFrame {

	/**
	 * @throws HeadlessException
	 */
	public About() throws HeadlessException {
		this.setSize(new Dimension(600, 350));
		setResizable(false);
		setTitle("About Open Shredder");
		getContentPane().setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(About.class.getResource("../images/flat-paper-shredder-icon.png")));
		getContentPane().add(lblNewLabel, BorderLayout.WEST);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);

		JLabel label = new JLabel("Product Name");
		label.setBounds(12, 12, 113, 15);
		label.setVerticalAlignment(SwingConstants.TOP);
		label.setHorizontalAlignment(SwingConstants.LEFT);

		JLabel label_1 = new JLabel("Version");
		label_1.setBounds(12, 32, 113, 15);

		JLabel label_8 = new JLabel("");
		label_8.setBounds(199, 12, 0, 0);

		JLabel label_2 = new JLabel("Licence");
		label_2.setBounds(12, 52, 113, 15);

		JLabel label_9 = new JLabel("");
		label_9.setBounds(262, 12, 0, 0);

		JLabel label_3 = new JLabel("Company");
		label_3.setBounds(12, 72, 113, 15);

		JLabel label_4 = new JLabel("Imece Software");
		label_4.setBounds(125, 72, 159, 15);
		label_4.setHorizontalAlignment(SwingConstants.LEFT);

		JLabel label_5 = new JLabel("GPL v2");
		label_5.setBounds(125, 32, 159, 15);
		label_5.setHorizontalAlignment(SwingConstants.LEFT);

		JLabel label_10 = new JLabel("");
		label_10.setBounds(240, 32, 0, 0);

		JLabel label_11 = new JLabel("");
		label_11.setBounds(245, 32, 0, 0);

		JLabel label_6 = new JLabel("v 0.0.1");
		label_6.setBounds(125, 52, 159, 15);
		label_6.setHorizontalAlignment(SwingConstants.LEFT);

		JLabel label_7 = new JLabel("Open Shredder");
		label_7.setBounds(125, 12, 159, 15);
		label_7.setHorizontalAlignment(SwingConstants.LEFT);

		JLabel label_12 = new JLabel("");
		label_12.setBounds(193, 52, 0, 0);

		JLabel label_15 = new JLabel("");
		label_15.setBounds(198, 52, 0, 0);

		JLabel label_13 = new JLabel("");
		label_13.setBounds(203, 52, 0, 0);

		JLabel label_17 = new JLabel("");
		label_17.setBounds(208, 52, 0, 0);

		JLabel label_16 = new JLabel("");
		label_16.setBounds(213, 52, 0, 0);

		JLabel label_14 = new JLabel("");
		label_14.setBounds(218, 52, 0, 0);
		panel_1.setLayout(null);
		panel_1.add(label);
		panel_1.add(label_1);
		panel_1.add(label_8);
		panel_1.add(label_2);
		panel_1.add(label_9);
		panel_1.add(label_3);
		panel_1.add(label_4);
		panel_1.add(label_5);
		panel_1.add(label_10);
		panel_1.add(label_11);
		panel_1.add(label_6);
		panel_1.add(label_7);
		panel_1.add(label_12);
		panel_1.add(label_15);
		panel_1.add(label_13);
		panel_1.add(label_17);
		panel_1.add(label_16);
		panel_1.add(label_14);

		JTextArea txtrOpenShredderIs = new JTextArea();
		txtrOpenShredderIs.setWrapStyleWord(true);
		txtrOpenShredderIs.setBackground(UIManager.getColor("Button.background"));
		txtrOpenShredderIs.setText(
				"Open Shredder is secure file deletion tool. It uses one pass ZERO  and three pass DoD standard algorithms. Before using it,be careful. You can not recover your files after deletion.\nIt is free  personal using.\nPlease let us know your feedback.\nHope enjoys.\nThanks for using.\nImece Software ");
		txtrOpenShredderIs.setEditable(false);
		txtrOpenShredderIs.setLineWrap(true);
		txtrOpenShredderIs.setBounds(12, 94, 402, 173);
		panel_1.add(txtrOpenShredderIs);

		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				About.this.setVisible(false);
			}
		});
		btnOk.setBounds(12, 272, 117, 25);
		panel_1.add(btnOk);

	}

	/**
	 * @param gc
	 */
	public About(GraphicsConfiguration gc) {
		super(gc);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param title
	 * @throws HeadlessException
	 */
	public About(String title) throws HeadlessException {
		super(title);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param title
	 * @param gc
	 */
	public About(String title, GraphicsConfiguration gc) {
		super(title, gc);
		// TODO Auto-generated constructor stub
	}
}
