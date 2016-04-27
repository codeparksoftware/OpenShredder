
package com.codepark.openshredder.shred;

import java.io.File;

/**
 * @author Selami
 * wipe free space multiple times by different values.
 *
 */
public class ShredFreeSpaceDod extends ShredFreeSpace {

	public ShredFreeSpaceDod(File mountPoint) {

		super(mountPoint);
	}

	public void Shred(ShredObserver sho) {
		for (short j = 1; j < 4; j++) {
			startEmptySpace(sho,(short) j);
		}
	}
}
