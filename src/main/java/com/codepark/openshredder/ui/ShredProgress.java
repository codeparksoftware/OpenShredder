
package com.codepark.openshredder.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JProgressBar;

import org.apache.log4j.Logger;

import com.codepark.openshredder.help.CheckVersion;
import com.codepark.openshredder.shred.IShred;
import com.codepark.openshredder.shred.ShredFactory;
import com.codepark.openshredder.shred.WipeMethod;

/**
 * @author Selami
 *
 */
public class ShredProgress extends BaseProgressPanel {

	private List<String> list;
	private final WipeMethod method;
	private JProgressBar pBarTotal;
	private JButton btnStart;
	private boolean wipeFreeSpace = false;
	private int tmp = 0;

	private static final Logger logger = Logger.getLogger(CheckVersion.class);

	public ShredProgress(List<String> lst, WipeMethod wip, boolean wipeFreeSpace) {

		setUIExtension();
		this.list = Collections.synchronizedList(lst);
		this.method = wip;
		this.wipeFreeSpace = wipeFreeSpace;
	}

	protected void setUIExtension() {

		panel.setLocation(17, 17);
		panel.setSize(415, 181);

		btnStop.setLocation(284, 123);

		pBarCurrent.setSize(343, 21);
		pBarCurrent.setLocation(33, 51);

		pBarTotal = new JProgressBar();
		pBarTotal.setStringPainted(true);
		pBarTotal.setBounds(33, 87, 343, 21);
		panel.add(pBarTotal);

		lblFile.setText("File");
		lblFile.setLocation(33, 31);

		btnStart = new JButton("Start");
		btnStart.setToolTipText("Start Operation");
		btnStart.setBounds(33, 123, 91, 25);
		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				start();
				btnStart.setEnabled(false);

			}
		});
		panel.add(btnStart);

	}

	@Override
	public void work() {
		System.out.println("Starting!...");
		logger.info("Shredding operation started");
		for (int i = tmp; i < list.size(); i++) {
			this.tmp = i;// For Resume operation
			File f = new File(list.get(i));// why dont we use List<File>
			this.lblFile.setText(f.getName());
			IShred shrd = new ShredFactory().ShredType(wipeFreeSpace, this.method, list.get(i).toString());
			shrd.Shred(this);
			int current = ((i + 1) * 100) / list.size();
			if (worker.isCancelled()) {
				return;
			}
			ShredProgress.this.setProgress(pBarTotal, current);// calculate all
																// bytes
																// refactor

		}
		finished();
	}

	public WipeMethod getMethod() {
		return method;
	}

}
