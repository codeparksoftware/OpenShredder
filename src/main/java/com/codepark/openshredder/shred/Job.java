package com.codepark.openshredder.shred;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Job implements ShredObservable, Runnable, IJob {

	protected Thread t;
	protected ArrayList<ShredObserver> lst;
	protected Object MUTEX;
	private static final Logger logger = Logger.getLogger(Job.class.getName());

	public Job() {

		init();
	}

	public void percent(long currentValue, long totalMax) {

		notifyServer((currentValue * 100) / totalMax);

	}

	private void init() {

		this.t = new Thread(this);
		this.MUTEX = new Object();
		this.lst = new ArrayList<ShredObserver>();
	}

	@Override
	public void start() {
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override
	public abstract void doJob();

	@Override
	public void interrupt() {
		t.interrupt();

	}

	@Override
	public void finish() {
		lst.clear();

	}

	@Override
	public void add(ShredObserver o) {
		if (o == null)
			throw new NullPointerException("Null Observer");
		synchronized (MUTEX) {
			if (!lst.contains(o))
				lst.add(o);
		}

	}

	@Override
	public void remove(ShredObserver o) {
		synchronized (MUTEX) {
			lst.remove(o);
		}

	}

	public void addThreadId(long val) {

		List<ShredObserver> list;
		synchronized (MUTEX) {
			list = new ArrayList<>(this.lst);
		}
		for (ShredObserver o : list) {
			o.addThreadId(val);
		}
	}

	public void removeThreadId(long val) {

		List<ShredObserver> list;
		synchronized (MUTEX) {
			list = new ArrayList<>(this.lst);
		}
		for (ShredObserver o : list) {
			o.removeThreadId(val);
		}

	}

	@Override
	public void notifyServer(long val) {
		List<ShredObserver> list;
		synchronized (MUTEX) {
			list = new ArrayList<>(this.lst);
		}
		for (ShredObserver o : list) {
			o.update((int) val);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}

	}

	@Override
	public void run() {
		doJob();

	}

}
