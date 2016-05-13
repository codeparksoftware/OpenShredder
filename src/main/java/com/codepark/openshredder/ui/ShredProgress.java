
package com.codepark.openshredder.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JProgressBar;

import com.codepark.openshredder.types.IFile;

/**
 * @author Selami
 *
 */
public class ShredProgress extends BaseProgressPanel {

	private List<IFile> list;
	private JProgressBar pBarTotal;
	private JButton btnStart;
	int tmp = 0;

	private static final Logger logger = Logger.getLogger(ShredProgress.class.getName());

	public ShredProgress(List<IFile> lst) {

		setUIExtension();
		this.list = Collections.synchronizedList(lst);

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
		long totalSpace = 0, finished = 0;
		for (int i = 0; i < list.size(); i++) {
			totalSpace += (list.get(i).getLength() * list.get(i).getWipeMethod().getMethod().length);
		}

		logger.info("totalSpace:" + String.valueOf(totalSpace));
		for (int i = 0; i < list.size(); i++) {

			IFile file = list.get(i);
			file.setObserver(this);
			this.lblFile.setText(file.getName());
			finished += (list.get(i).getLength() * list.get(i).getWipeMethod().getMethod().length);
			setProgress(pBarTotal, (int) (finished * 100 / totalSpace));

			file.shred();

			if (worker.isCancelled()) {
				return;
			}

		}
		finished();
	}

}
