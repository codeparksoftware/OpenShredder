package com.codepark.openshredder.shred;

import java.io.File;
/*
*This class extends ShredFileStore class.
*this class used to clear file storage with different values multiple times .
*/
public class ShredFileStoreDod extends ShredFileStore {
	public ShredFileStoreDod(File f) {
		super(f);

	}

	public void Shred(ShredObserver sho) {
		MetaData m = new MetaData(f);
		m.SetWritable();
		m.add(sho);
		Body bd = null;
		for (int i = 1; i < 4; i++) {
			bd = new Body(f, (short) i);
			bd.add(sho);
 
			bd.start();// Wiping body
		 
			m.start();
		}

	}

}
