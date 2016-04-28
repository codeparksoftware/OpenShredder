package com.codepark.openshredder.ui;

public interface IProgress {
	public void work();

	public void start();

	public void stop();

	public void finished();
}
