package com.codepark.openshredder.shred;

import java.io.File;

/*
 * @author Selami
 * 23.05.2016
 * This class uses methods of Body and MetaData classes.This class used to clear a file store 
 * completely.And it clear file store by Zero Pass algorithm.
 */
public class ShredFileStore implements IShred {
	protected File f;

	public ShredFileStore(File file) {
		this.f = file;
	}

	public void Shred(ShredObserver sho) {
		MetaData m = new MetaData(f);
		m.SetWritable();
		m.add(sho);
		Body bd = new Body(f, (short) 0);
		bd.add(sho);
		bd.start();// Wipe Zero

		m.start();

	}

}
