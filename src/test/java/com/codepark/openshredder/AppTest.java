package com.codepark.openshredder;

import java.io.FileNotFoundException;

import com.codepark.openshredder.exceptions.NotRegularFileException;
import com.codepark.openshredder.types.RegularFile;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
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
		try {
			RegularFile reg = null, reg1 = null;
			try {
				reg1 = new RegularFile("C:\\Users\\ab170460\\Desktop\\ar-ge.docx");

				reg = new RegularFile("C:\\Users\\ab170460\\Desktop\\Updater.jar");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(reg.toString());
			System.out.println(reg1.toString());
			System.out.println(reg1.equals(reg));
			System.out.println(reg.hashCode());
			System.out.println(reg1.hashCode());
		} catch (NotRegularFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
