 
package com.codepark.openshredder.ui;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import com.codepark.openshredder.base.IShred;
import com.codepark.openshredder.base.ShredFactory;
import com.codepark.openshredder.base.ShredObserver;
import com.codepark.openshredder.base.WipeMethod;

/**
 * @author Selami
 *
 */
public class ProgressPanel extends JPanel implements ShredObserver, Runnable {
	private Thread main;
	private List<String> list;
	private List<Long> tList;
	private final WipeMethod method;
	private JLabel lblFile;
	private JProgressBar pBarCurrent, pBarTotal;
	private JButton btnStop, startButton;
	private boolean finished = false, canceled = false,wipeFreeSpace = false;
	private IShred shrd;
	private Object MUTEX;
	

	public ProgressPanel(List<String> lst, WipeMethod wip,boolean wipeFreeSpace) {
		setContentUI();

		this.list = Collections.synchronizedList(lst);
		this.method = wip;
		this.tList = new ArrayList<Long>();
		this.wipeFreeSpace=wipeFreeSpace;
		MUTEX = new Object();
	}

	private void setContentUI() {
		setLayout(null);

		pBarCurrent = new JProgressBar(0, 100);
		pBarCurrent.setBounds(31, 54, 344, 20);
		pBarCurrent.setValue(0);
		pBarCurrent.setStringPainted(true);

		pBarTotal = new JProgressBar(0, 100);
		pBarTotal.setMaximum(100);
		pBarTotal.setBounds(31, 86, 344, 20);
		pBarTotal.setValue(0);
		pBarTotal.setStringPainted(true);
		setBounds(20, 20, 453, 210);
		JPanel panel = new JPanel();
		panel.setBounds(20, 20, 410, 179);

		add(panel);
		panel.setLayout(null);
		panel.add(pBarCurrent);
		panel.add(pBarTotal);

		startButton = new JButton("Start");
		
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canceled = false;
				main = new Thread(ProgressPanel.this);
				main.start();
				startButton.setEnabled(false);
				btnStop.setEnabled(true);
			}
		});

		startButton.setBounds(31, 125, 91, 25);
		panel.add(startButton);

		btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.stop();
				Set<Thread> setOfThread = Thread.getAllStackTraces().keySet();

				// Iterate over set to find yours
				for (Thread thread : setOfThread) {
					for (int j = 0; j < tList.size(); j++) {
						if (thread.getId() == tList.get(j)) {
							thread.interrupt();
						}
					}
				}
				btnStop.setEnabled(false);
				startButton.setEnabled(true);
			}
		});
		btnStop.setBounds(284, 125, 91, 25);
		btnStop.setEnabled(false);
		panel.add(btnStop);

		lblFile = new JLabel("  ");
		lblFile.setBounds(31, 26, 344, 15);
		panel.add(lblFile);

		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}

	public Dimension getDimension() {
		return new Dimension(453, 210);

	}

	public void setProgress(JProgressBar bar, int val) {
		bar.setValue(val);
		bar.setString(String.valueOf(val) + "%");
		bar.repaint();
	}

	public void Work() {
		System.out.println("Starting!...");

		for (int i = 0; i < list.size(); i++) {
			if (canceled == true)
				return;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			File f = new File(list.get(i));
			this.lblFile.setText(f.getName());

			shrd = new ShredFactory().ShredType(wipeFreeSpace, this.method, list.get(i).toString());
			shrd.Shred(this);
			int current = ((i + 1) * 100) / list.size();

			// System.out.println(current);
			ProgressPanel.this.setProgress(pBarTotal, current);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		System.out.println("Wipe finished!...");
		setFinished(true);
		this.startButton.setEnabled(true);
		this.btnStop.setEnabled(false);
		try {
			Thread.sleep(3000);
			this.setVisible(false);
			Window wn = SwingUtilities.getWindowAncestor(this);
			wn.setVisible(false);
			wn.dispose();
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		Work();

	}

	@Override
	public void update(int i) {
		setProgress(pBarCurrent, i);

	}

	public void add(Long o) {
		if (o == null)
			throw new NullPointerException("Null Observer");
		synchronized (MUTEX) {
			if (!tList.contains(o))
				tList.add(o);
		}

	}

	public void remove(Long o) {
		synchronized (MUTEX) {
			tList.remove(o);
		}

	}

	@Override
	public void addThreadId(long val) {
		add(val);
		System.out.println("added: " + val);
	}

	@Override
	public void removeThreadId(long val) {
		remove(val);
		System.out.println("removed: " + val);
	}

	public boolean isFinished() {
		return finished;
	}

	private void setFinished(boolean finished) {
		this.finished = finished;
	}
}
