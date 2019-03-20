

package com.benchmark.framework.testng;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.annotations.Test;

public class ExecuteTestNGTests extends BaseTestNG {

	public ExecuteTestNGTests() {

	}

	/**
	 * Executes the test in parallel
	 * 
	 * @param parallelMode
	 * @param testClass
	 */
	@Test
	public void runTestInParallel(String parallelMode, Class<?> testClass) {
		TestNG testNg = create(testClass);
		testNg.setParallel(parallelMode);
		TestListenerAdapter testListenerAdapter = new TestListenerAdapter();
		testNg.addListener(testListenerAdapter);
		testNg.run();
	}

	/**
	 * Executes the test classes in parallel mode.
	 * 
	 * @param parallelMode
	 * @param testClasses
	 */
	@Test
	public void runTestInParallel(String parallelMode, Class<?>... testClasses) {
		TestNG testNg = create(testClasses);
		testNg.setParallel(parallelMode);
		testNg.setThreadCount(10);
		TestListenerAdapter testListenerAdapter = new TestListenerAdapter();
		testNg.addListener(testListenerAdapter);
		testNg.run();
	}

	/**
	 * Execute the test suites and return test listener to generate custom
	 * report.
	 * 
	 * @param parallelMode
	 * @param suiteName
	 * @param testName
	 * @param classes
	 * @return
	 */
	@Test
	public TestListenerAdapter runTestClasses(String parallelMode,
			String suiteName, String testName, String... classes) {
		TestNG testNg = createXMLSuite(suiteName, testName, classes);
		testNg.setParallel(parallelMode);
		testNg.setThreadCount(10);
		testNg.setVerbose(2);
		TestListenerAdapter testListenerAdapter = new TestListenerAdapter();
		testNg.addListener(testListenerAdapter);
		testNg.run();
		return testListenerAdapter;

	}

}
