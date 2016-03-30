package com.codepark.openshredder.base;
/*
 * Clear Directories' meta data.
 */
import java.io.File;

public class ShredMetaData implements IShred {
	File f = null;
	private Thread t;

	public ShredMetaData(File f) {
		this.f = f;
	}

	public Thread getThread() {
		return this.t;
	}

	protected void setThread(Thread t) {
		this.t = t;
	}

	@Override
	public void Shred(ShredObserver sho) {
		DeleteAll(sho);
		f.delete();
	}

	protected void DeleteAll(ShredObserver sho) {

		MetaData m = new MetaData(f);
		m.SetWritable();
		m.add(sho);
		m.startClear();
		m.finalize();
	}
}
