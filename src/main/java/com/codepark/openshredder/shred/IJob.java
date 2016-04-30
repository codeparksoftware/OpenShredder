package com.codepark.openshredder.shred;

public interface IJob {
	public void start();

	public void doJob();

	public void finish();

	public void interrupt();

}
