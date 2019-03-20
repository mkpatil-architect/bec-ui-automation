
package com.benchmark.test;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.testng.Assert;
import org.testng.TestListenerAdapter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.benchmark.common.Global;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.constants.TestNGParallelModes;
import com.benchmark.core.logger.Log;
import com.benchmark.core.util.CommonUtil;
import com.benchmark.framework.testng.CustomTestNGReport;
import com.benchmark.framework.ui.SeleniumWrapper;
import com.benchmark.pages.Default;
import com.benchmark.testngfactory.TestNGManager;

/**
 * Test class to run automation test suites using TestNG.
 */
public class BaseUITest {

	// -----------------------------------------------------------------------------
	// Configuration Settings

	/**
	 * Configuration setting before start test suites. We will validate that user
	 * page load is successful then proceed with running all test suites.
	 */
	@BeforeClass
	public void beforeClass() {
		try {
			Global.setTestExecutionStartTime(CommonUtil.getFormatedDate("yyyy-MM-dd'T'hh:mm:ssZ").replace("'", ""));
			Log.writeMessage(LogLevel.INFO, CLASS_NAME, "Started building basic setup to run test suites...");
			if (!Default.Start()) {
				SeleniumWrapper.stop();
				Assert.assertTrue(false, "Failed to load page. For more details please check logs.");
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, e.toString());
		}
	}

	/**
	 * Once all test suites execution is done then we will logout, close the browser
	 * and generate the report.
	 */
	@AfterClass
	public void afterClass() {
		try {
			CustomTestNGReport customTestNGReport = null;
			customTestNGReport = new CustomTestNGReport(String.format("UI Automation Report"), m_listeners,
					Global.getTestExecutionStartTime());
			customTestNGReport.generateReport();
			Log.writeMessage(LogLevel.INFO, String.format(
					"Test suites execution completed @ %s \n-------------------------------------------------------------------------------------------------------------------------",
					CommonUtil.getFormatedDate("yyyy-MM-dd HH:mm:ss.SSS ")));
		} catch (Exception e) {
			Log.writeMessage(CLASS_NAME, e.getMessage());

		} finally {
			Default.Stop();
		}
	}

	// -----------------------------------------------------------------------------
	// Test suites

	/**
	 * Executes test suites based on settings
	 */
	@Test(priority = 1, enabled = true)
	public void TestSuites() {
		String[] testSuites = Global.getTestSuites().split(",");
		if (testSuites[0].trim().toLowerCase().equals("all")) {
			executeTestSuites(getAllTestSuites(), true);
		} else {
			executeTestSuites(testSuites, false);
		}
	}

	// -----------------------------------------------------------------------------
	// Private methods

	/**
	 * This method will execute the all test suites which are defined in the APP
	 * property file.
	 */
	private void executeTestSuites(String[] testSuites, boolean isAll) {
		for (String testSuite : testSuites) {
			if (isAll)
				testSuite = testSuite.replace("ts_", "").replace(".properties", "");
			try {
				String[] testcases = CommonUtil.getTestClassesToRun(String.format("ts_%s", testSuite.toLowerCase()));
				if (testcases.length > 0) {
					TestListenerAdapter testListenerAdapter = TestNGManager.getTestNGInstance().runTestClasses(
							TestNGParallelModes.NONE, testSuite.toUpperCase(), testSuite.toUpperCase(), testcases);
					m_listeners.add(testListenerAdapter);
				}
			} catch (Exception e) {
				Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.getMessage());
			}
		}
	}

	/**
	 * Method to get all test suites
	 */
	private String[] getAllTestSuites() {
		String value = Thread.currentThread().getContextClassLoader().getResource("app.properties").getPath();
		List<String> files = new ArrayList<String>();
		File dir = new File(value.substring(0, value.lastIndexOf("/")));
		for (File nextFile : dir.listFiles()) {
			if (nextFile.isFile())
				if (nextFile.getName().startsWith("ts_")) {
					files.add(nextFile.getName());
				}
		}
		return files.toArray(new String[files.size()]);
	}
	// -----------------------------------------------------------------------------
	// Private variables

	/**
	 * Class Name
	 */
	private final static String CLASS_NAME = BaseUITest.class.getName();

	/**
	 * Variable to hold all results details, and will be used to generate custom
	 * report
	 */
	private LinkedHashSet<TestListenerAdapter> m_listeners = new LinkedHashSet<TestListenerAdapter>();
}