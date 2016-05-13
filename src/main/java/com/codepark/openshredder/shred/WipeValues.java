
package com.codepark.openshredder.shred;
/*
 *  Wipe Values is factory byte array is used in wipe process. This program uses Zero and DoD algorithms.
 *  And Wipe function needs this value array.all values are static but only Random Values  are dynamically 
 *	created.It use SecureRandom class to generate random byte values.
 *	This class takes two parameter id and len.id identifies which value of array is created.
 *	len determines length of byte array to return
 */

import java.nio.ByteBuffer;
import java.security.SecureRandom;

public class WipeValues {
	private int Length;
	private Object val;

	public WipeValues(int len, Object val) {
		this.Length = len;
		this.val = val;

	}

	public WipeValues(int len) {
		this.Length = len;
	}

	public ByteBuffer GenerateValue() {
		if (this.val.getClass().getTypeName() == SecureRandom.class.getTypeName())
			return SetRawBuf(Length);

		else
			return SetRawBuf(Length, (byte) val);

	}

	public static byte ZERO = (0x00), PASS1 = (byte) (0X55), PASS2 = (byte) (0XAA);

	private ByteBuffer SetRawBuf(int len, byte val) {
		ByteBuffer b = ByteBuffer.allocate(len);

		for (int i = 0; i < len; i++) {
			b.array()[i] = val;
		}
		return b;

	}

	private ByteBuffer SetRawBuf(int len) {
		ByteBuffer b = ByteBuffer.allocate(len);
		new SecureRandom().nextBytes(b.array());
		return b;

	}

}
