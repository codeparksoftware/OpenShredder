/**
 * 
 */
package com.codepark.openshredder.shred;

/*
 * @author selami
 *Observer pattern implementation.Observable class notify two things.
 *First notify current thread id to manage sub process
 *second notify work progress state  as percent value
 *
 */
public interface ShredObservable {
	public void notifyServer(long val);

	public void add(ShredObserver o);

	public void remove(ShredObserver o);

	public void addThreadId(long val);

	public void removeThreadId(long val);

}
