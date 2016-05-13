package com.codepark.openshredder;

import com.codepark.openshredder.common.Level;
import com.codepark.openshredder.common.Logger;
import com.codepark.openshredder.shred.ShredObserver;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase implements ShredObserver {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
		testApp();
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {

//		System.out.println(new Date().toLocaleString());
		Logger.getLogger(AppTest.class.getName()).log(Level.Info, "selamlar");
	}

	@Override
	public void update(int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addThreadId(long val) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeThreadId(long val) {
		// TODO Auto-generated method stub

	}
}
