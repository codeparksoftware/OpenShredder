package com.codepark.openshredder.types;

import java.security.SecureRandom;

import com.codepark.openshredder.shred.WipeValues;

public enum WipeMethod {

	Zero(new Object[] { WipeValues.ZERO }), DoD(
			new Object[] { WipeValues.PASS1, WipeValues.PASS2, new SecureRandom() });
	private final Object[] wipe;

	WipeMethod(Object[] wipe) {
		this.wipe = wipe;
	}

	public Object[] getMethod() {
		return wipe;
	}

	public static WipeMethod fromString(String text) {
		if (text != null) {
			for (WipeMethod b : WipeMethod.values()) {
				if (text.equalsIgnoreCase(b.toString())) {
					return b;
				}
			}
		}
		return null;
	}
}
