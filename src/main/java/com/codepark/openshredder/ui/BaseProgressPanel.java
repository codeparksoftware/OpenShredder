
package com.codepark.openshredder.ui;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import com.codepark.openshredder.shred.ShredObserver;

/**
 * @author Selami
 *
 */
public abstract class BaseProgressPanel extends JPanel implements IProgress, ShredObserver {

	protected JPanel panel;
	protected JLabel lblFile;
	protected JProgressBar pBarCurrent;
	protected JButton btnStop;
	private boolean finished;
	protected Worker worker;
	protected Object MUTEX;
	protected List<Long> tList;

	private static final Logger logger = Logger.getLogger(BaseProgressPanel.class);

	public BaseProgressPanel() {
		setContentUI();
		init();
	}

	protected void setLabel(String str) {
		this.lblFile.setText(str);
		this.lblFile.repaint();

	}

	protected void init() {
		MUTEX = new Object();
		this.tList = new ArrayList<Long>();
	}

	private void setContentUI() {
		setLayout(null);

		pBarCurrent = new JProgressBar(0, 100);
		pBarCurrent.setBounds(31, 54, 344, 20);
		pBarCurrent.setValue(0);
		pBarCurrent.setStringPainted(true);
		setBounds(20, 20, 453, 220);
		panel = new JPanel();
		panel.setBounds(20, 20, 410, 179);

		add(panel);
		panel.setLayout(null);
		panel.add(pBarCurrent);

		btnStop = new JButton("Cancel");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		btnStop.setBounds(284, 125, 91, 25);
		panel.add(btnStop);

		lblFile = new JLabel("...");
		lblFile.setBounds(31, 26, 344, 15);
		panel.add(lblFile);

		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}

	public Dimension getDimension() {
		return new Dimension(453, 210);

	}

	public void setIndeterminate(boolean val) {
		pBarCurrent.setIndeterminate(val);
		pBarCurrent.repaint();
		pBarCurrent.setValue(0);
	}

	protected void setProgress(JProgressBar bar, int val) {
		bar.setValue(val);
		bar.setString(String.valueOf(val) + "%");
		bar.repaint();
	}

	public abstract void work();

	public boolean isFinished() {
		return finished;
	}

	protected void setFinished(boolean finished) {
		this.finished = finished;
	}

	public void finished() {
		logger.info("Operation finished!");
		System.out.println("Operation finished!...");
		setFinished(true);
		this.btnStop.setEnabled(false);
		try {
			Thread.sleep(1000);
			Window wn = SwingUtilities.getWindowAncestor(this);
			wn.setVisible(false);
			wn.dispose();
			this.finalize();
		} catch (Throwable e) {

			logger.debug(e.getMessage(), e);
		}
	}

	class Worker extends SwingWorker<Void, Void> {

		@Override
		protected Void doInBackground() throws Exception {
			work();
			return null;

		}

		@Override
		protected void done() {
			finished();
		}

	}

	@Override
	public void start() {
		worker = new Worker();
		worker.execute();
		btnStop.setEnabled(true);

	}

	public void stop() {
		worker.cancel(true);
		Set<Thread> setOfThread = Thread.getAllStackTraces().keySet();
		// Iterate over set to find current running
		for (Thread thread : setOfThread) {
			for (int j = 0; j < tList.size(); j++) {
				if (thread.getId() == tList.get(j)) {
					thread.interrupt();
				}
			}
		}
		btnStop.setEnabled(false);
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

}
