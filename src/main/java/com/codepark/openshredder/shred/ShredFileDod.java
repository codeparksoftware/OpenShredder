package com.codepark.openshredder.shred;
/*@author Selami
 * 25.03.2016
 * Regular file wipes with Dod algorithms.This class extends ShredFile.
 * Only shred method is overridden.
 * 
 */

import java.io.File;

public class ShredFileDod extends ShredFile {

	public ShredFileDod(File f) {
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
		}
		bd.SetFileLength();
		m.start();
		f.delete();
	}

}
