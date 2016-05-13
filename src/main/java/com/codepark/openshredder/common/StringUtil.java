package com.codepark.openshredder.common;

public class StringUtil {
	public static boolean stringIsNullorEmpty(String str) {
		if (str == null || str.trim().isEmpty())
			return true;
		return false;
	}
}
