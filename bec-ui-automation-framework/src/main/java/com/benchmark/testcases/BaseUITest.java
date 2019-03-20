

package com.benchmark.testcases;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.benchmark.common.Global;
import com.benchmark.common.Helper;
import com.benchmark.constants.CommonConstants;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;
import com.benchmark.core.util.CommonUtil;
import com.benchmark.framework.ui.SeleniumWrapper;

/**
 * Base class for UI test class. Writes before/After logs for any test cases.
 */
public abstract class BaseUITest {

	// --------------------------------------------------------------------------------------------
	// Constructors

	/**
	 * Constructor
	 */
	public BaseUITest(String testCaseDescription) {
		this(testCaseDescription, null);
	}

	/**
	 * Constructor
	 */
	public BaseUITest(String testCaseDescription, String testSteps) {
		m_currentTestCaseName = this.getClass().getSimpleName();
		m_currentTestCaseDescription = testCaseDescription;
		m_currentTestCaseTestSteps = testSteps != null ? testSteps : null;
	}

	/**
	 * Method to log all test steps for test case
	 */
	private void logFullTestDescription() {
		if (m_currentTestCaseTestSteps != null) {
			String[] fullDescription = m_currentTestCaseTestSteps.split("%%%");
			for (String testStep : fullDescription) {
				m_logMessage.append(" " + testStep + "\n");
			}
		}
	}

	// --------------------------------------------------------------------------------------------
	// Override method for Testng annotations

	/**
	 * Logs the test case name and description information before starting test
	 * case execution.
	 */
	@BeforeClass
	public void beforeClass() {
		// if (!m_isTestPassed)
		SeleniumWrapper.openPage(Global.getValueFromAppProperty(CommonConstants.LOGIN_URL));
//		SeleniumWrapper.openPage("https://benchmarkeducation.com/");
		m_logMessage = new StringBuilder();
		m_logMessage.append(String.format("TestCase - %s, started @ %s \n", m_currentTestCaseName,
				CommonUtil.getFormatedDate("yyyy-MM-dd HH:mm:ss.SSS ")));
		m_logMessage.append(
				"------------------------------------------------------------------------------------------------------------------------- \n");
		m_logMessage.append(String.format("Description: %s. \nTest Steps: \n", m_currentTestCaseDescription));
		logFullTestDescription();
		Log.writeMessage(LogLevel.INFO, m_logMessage.toString());
	}

	/**
	 * Logs the test case execution status after completion of test case run.
	 */
	@AfterClass
	public void afterClass() {
		m_logMessage = new StringBuilder();
		m_logMessage.append(String.format("TestCase - %s, execution completed", m_currentTestCaseName)
				+ getStatusMessage() + "@ " + CommonUtil.getFormatedDate("yyyy-MM-dd HH:mm:ss.SSS ") + "\n");
		m_logMessage.append(
				"-------------------------------------------------------------------------------------------------------------------------");
		Log.writeMessage(LogLevel.INFO, m_logMessage.toString());
	}

	/**
	 * Common code which will run before any method for that class.
	 */
	@BeforeMethod
	public void beforeMethod() {
		Helper.sleep(2000);
	}

	@AfterMethod
	public void afterMethod() {
		SeleniumWrapper.clickEscape();
		Helper.sleep(2000);
		Helper.verifyWarningOrOtherPopupAndClose();
		Helper.sleep(2000);
		Helper.verifyWarningOrOtherPopupAndClose();
		Helper.sleep(2000);
		SeleniumWrapper.clickEscape();
	}

	// --------------------------------------------------------------------------------------------
	// Protected Methods

	/**
	 * Method to get the final error messages if available.
	 * 
	 * @return -> Error message
	 */
	protected String getErrorMessage() {
		return (Global.TEST_CASE_ERROR_MESSAGES != null) ? String.format("[%s] - Error Information: %s ",
				Global.getClientName(), Global.TEST_CASE_ERROR_MESSAGES.toString()) : "";
	}

	// --------------------------------------------------------------------------------------------
	// Private methods & variables

	/**
	 * Method to bind the test cases status whether it passed or failed.
	 */
	private String getStatusMessage() {
		return (m_isTestPassed == true) ? " successfully..." : " with failures...";
	}

	// /**
	// * Current test class name
	// */
	// private String m_currentTestClassName = null;

	/**
	 * Current test case name
	 */
	private String m_currentTestCaseName = null;

	/**
	 * Current test case description
	 */
	private String m_currentTestCaseDescription = null;

	/**
	 * Current test cases test steps
	 */
	private String m_currentTestCaseTestSteps = null;

	/**
	 * Log message.
	 */
	private StringBuilder m_logMessage = null;

	/**
	 * Holds current test case is passed or failed.
	 */
	protected boolean m_isTestPassed = false;

}
