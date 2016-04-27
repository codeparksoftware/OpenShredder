
package com.codepark.openshredder.shred;

/**
 * @author Selami Observer pattern implementation.Observer class obeserves two
 *         thing. First observes thread id to manage sub process second observes
 *         work progress state
 */
public interface ShredObserver {
	public void update(int i);

	public void addThreadId(long val);

	public void removeThreadId(long val);

}
