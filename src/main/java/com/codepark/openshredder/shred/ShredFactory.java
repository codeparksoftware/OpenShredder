package com.codepark.openshredder.shred;

/*
 * This class is factory class of shred classes.Class uses detection file types such as regular file,
 * directory and etc... then creates instance  of related class finally sends this created instance.
 * All class has IShred implementation thus all class has Shred function.User should only send 
 *  the path of the file to be wiped.
 *  And second parameter is WipeMethod.This information used to create instance of related class again.
 *  
 *  */

import java.io.File;

import com.codepark.openshredder.system.FileInfo;

public class ShredFactory {
	
	public IShred ShredType(boolean wipeFreeSpace, WipeMethod wipe, String path) {
		File f = new File(path);
		if (!f.exists()) {
			return null;
		}
		if (f.isFile()) {
			if (wipe == WipeMethod.Zero) {
				return new ShredFile(f);
			} else if (wipe == WipeMethod.DoD) {
				return new ShredFileDod(f);
			}
		} else if (f.isDirectory() && wipeFreeSpace == false) {
			if (wipe == WipeMethod.Zero) {
				return new ShredMetaData(f);
			} else if (wipe == WipeMethod.DoD) {
				return new ShredMetaDataDod(f);
			}
		} else if (f.isDirectory() && wipeFreeSpace && new FileInfo().isMountPoint(path)) {
			if (wipe == WipeMethod.Zero)
				return new ShredFreeSpace(f);
			else if (wipe == WipeMethod.DoD)
				return new ShredFreeSpaceDod(f);

		} else if (new FileInfo(path).isFileStorage()) {
			if (wipe == WipeMethod.Zero) {
				return new ShredFileStore(f);
			} else if (wipe == WipeMethod.DoD) {
				return new ShredFileStoreDod(f);
			}
		}
		return null;
	}

}
