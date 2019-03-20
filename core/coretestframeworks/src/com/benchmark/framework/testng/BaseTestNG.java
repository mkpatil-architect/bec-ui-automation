

package com.benchmark.framework.testng;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.AssertJUnit;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.collections.Lists;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class BaseTestNG {

	/*
	 * Creates TestNG object with default listeners
	 */
	public static TestNG create() {
		TestNG testNG = new TestNG();
		testNG.setUseDefaultListeners(false);
		return testNG;
	}

	/*
	 * Creates TestNG instance and sets test classes
	 */
	public static TestNG create(Class<?> testClass) {
		TestNG testNG = create();
		testNG.setTestClasses(new Class[] { testClass });
		return testNG;
	}

	/*
	 * Compare a list of ITestResult with a list of String method names,
	 */
	public static void assertTestResultsEqual(List<ITestResult> results,
			List<String> methods) {
		List<String> resultMethods = Lists.newArrayList();
		for (ITestResult r : results) {
			resultMethods.add(r.getMethod().getMethodName());
		}
		AssertJUnit.assertEquals(resultMethods, methods);
	}

	/*
	 * Creates TestNG instance which sets multiple classes.
	 */
	protected TestNG create(Class<?>... testClasses) {
		TestNG testNG = create();
		testNG.setTestClasses(testClasses);
		return testNG;
	}

	/*
	 * Creates XML Suites.
	 */
	protected XmlSuite createXmlSuite(String name) {
		XmlSuite xmlSuite = new XmlSuite();
		xmlSuite.setName(name);
		return xmlSuite;
	}

	/*
	 * Creates XML test instances
	 */
	protected XmlTest createXmlTest(XmlSuite suite, String name,
			String... classes) {
		XmlTest xmlTest = new XmlTest(suite);
		int index = 0;
		xmlTest.setName(name);
		for (String c : classes) {
			XmlClass xc = new XmlClass(c, index++, true);/* load classes */
			xmlTest.getXmlClasses().add(xc);
		}
		return xmlTest;
	}

	/*
	 * Add XML class methods.
	 */
	protected void addMethods(XmlClass xmlClass, String... methods) {
		int index = 0;
		for (String m : methods) {
			XmlInclude xmlInclude = new XmlInclude(m, index++);
			xmlClass.getIncludedMethods().add(xmlInclude);
		}
	}

	/*
	 * get XML resource path to set.
	 */
	protected String getPathToResource(String fileName) {
		String TEST_RESOURCES_DIR = null;
		String result = System.getProperty(TEST_RESOURCES_DIR);
		if (result == null) {
			// Utils.log("SimpleBaseTest", 2, "Warning: System property "
			// + TEST_RESOURCES_DIR + " was not defined.");
			return "target/test-classes/" + fileName;
		} else {
			return result + File.separatorChar + fileName;
		}
	}

	/*
	 * Method to verify all passed tests.
	 */
	protected void verifyPassedTests(TestListenerAdapter testListenerAdapter,
			String... methodNames) {
		Iterator<ITestResult> testResults = testListenerAdapter
				.getPassedTests().iterator();
		AssertJUnit.assertEquals(testListenerAdapter.getPassedTests().size(),
				methodNames.length);

		int i = 0;
		while (testResults.hasNext()) {
			AssertJUnit.assertEquals(testResults.next().getName(), methodNames[i++]);
		}
	}

	/*
	 * Method to verify all passed tests.
	 */
	protected void verifyPassedClasses(TestListenerAdapter testListenerAdapter,
			Class<?>... methodNames) {
		Iterator<ITestResult> testResults = testListenerAdapter
				.getPassedTests().iterator();
		AssertJUnit.assertEquals(testListenerAdapter.getPassedTests().size(),
				methodNames.length);

		int i = 0;
		while (testResults.hasNext()) {
			AssertJUnit.assertEquals(testResults.next().getClass(), methodNames[i++]);
		}
	}

	/**
	 * Method to create XML Suite.
	 * 
	 * @param suiteName
	 * 
	 * @param testName
	 * 
	 * @param classes
	 * 
	 * @return TestNG object holding the tests to be executed in this suite
	 * 
	 * @throws TestNGException if one or more of the class names is invalid
	 * 
	 */
	protected TestNG createXMLSuite(String suiteName, String testName,
			String... classes) {

		TestNG myTestNG = new TestNG();

		XmlSuite mySuite = new XmlSuite();
		mySuite.setName(suiteName);

		XmlTest myTest = new XmlTest(mySuite);
		myTest.setName(testName);
		int index = 0;
		for (String c : classes) {
			XmlClass xc = new XmlClass(c, index++, true);
			myTest.getXmlClasses().add(xc);
		}
		List<XmlTest> myTests = new ArrayList<XmlTest>();
		myTests.add(myTest);

		mySuite.setTests(myTests);

		// List<XmlSuite> mySuites = new ArrayList<XmlSuite>();
		// mySuites.add(mySuite);
		// myTestNG.setXmlSuites(mySuites);

		myTestNG.setDefaultSuiteName(suiteName);

		myTestNG.setCommandLineSuite(mySuite);
		return myTestNG;
	}
	
}
