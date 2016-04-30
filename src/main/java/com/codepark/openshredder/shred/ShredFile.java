package com.codepark.openshredder.shred;

import java.io.File;

/**
 * @author Selami This file uses methods of Body class and MetaData .It used to
 *         wipe regular file with Zero pass algorithm.
 */

public class ShredFile implements IShred {

	protected File f;

	public ShredFile(File file) {
		this.f = file;
	}

	public void Shred(ShredObserver sho) {

		MetaData m = new MetaData(f);
		m.SetWritable();
		m.add(sho);
		Body bd = new Body(f, (short) 0);
		bd.add(sho);
		bd.start();// Wipe Zero
		bd.SetFileLength();
		m.start();
		f.delete();

	}

}