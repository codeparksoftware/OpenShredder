package com.codepark.openshredder.shred;

/*
 * @author Selami
 * This class extends ShredMetaData.
 * This clears metadata of the directory file three times.
 * finally deletes the file.
 */
import java.io.File;

public class ShredMetaDataDod extends ShredMetaData {

	public ShredMetaDataDod(File f) {
		super(f);
	}

	public void Shred(ShredObserver sho) {

		for (int i = 0; i < 3; i++) {
			DeleteAll(sho);
		}
		this.f.delete();
	}
}
