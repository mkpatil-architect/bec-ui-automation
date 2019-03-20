
package com.benchmark.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.benchmark.constants.CommonConstants;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;
import com.benchmark.core.util.FileOperations;

/**
 * Class contains all global related information.
 */
public class Global {

	// -----------------------------------------------------------------------------
	// Public Variables
	// -----------------------------------------------------------------------------
	/**
	 * Holds all error messages while execution of that test case. Before to start
	 * that test every time need to instantiate. <br />
	 * Example: Global.TEST_CASE_ERROR_MESSAGES = new StringBuilder();
	 */
	public static StringBuilder TEST_CASE_ERROR_MESSAGES = null;

	// -----------------------------------------------------------------------------

	/**
	 * Builds all properties.
	 */
	public static boolean buildProperties() {
		try {
			m_appProperties = FileOperations.getProperties(CommonConstants.APP_PROPERTIES);
			bindResourceStrings();
			m_currencySymbol = getValueFromAppProperty(CommonConstants.DEFAULT_CURRENCY_SYMBOL);
			Log.writeMessage(LogLevel.INFO,
					String.format("Basic setup is completed and launching '%s' browser to run test suites...",
							getValueFromAppProperty(CommonConstants.BROWSER).toUpperCase()));
			return true;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, e.getLocalizedMessage());
		}
		return false;
	}

	/**
	 * Gets Client Name
	 */
	public static String getClientName() {
		return getValueFromAppProperty(CommonConstants.CLIENT);
	}

	/**
	 * Gets Is KIBANA enabled or not
	 */
	public static boolean getIsKibana() {
		return Boolean.parseBoolean(getValueFromAppProperty(CommonConstants.KIBANA));
	}

	/**
	 * Gets Is Smoke Suite
	 */
	public static boolean getIsSmokeSuite() {
		return Boolean.parseBoolean(getValueFromAppProperty(CommonConstants.IS_SMOKE_SUITE));
	}

	/**
	 * This method will return the resource value from the resource property file.
	 * 
	 * @param key
	 * @return -> Resource value
	 */
	public static String getResourceValue(String key) {
		return getPropertyValue(m_resourceProperties, key);
	}

	/**
	 * Gets SSO and StandAlone User Information
	 */
	public static List<String> getSSOAndStandaloneInfo() {
		List<String> ssoInfo = new ArrayList<String>();
		ssoInfo.add(getValueFromAppProperty(CommonConstants.SSOURL));
		ssoInfo.add(getValueFromAppProperty(CommonConstants.SSOUSERNAME));
		ssoInfo.add(getValueFromAppProperty(CommonConstants.SSOUSERPWD));
		ssoInfo.add(getValueFromAppProperty(CommonConstants.LOGIN_URL));
		ssoInfo.add(getValueFromAppProperty(CommonConstants.USER_NAME));
		ssoInfo.add(getValueFromAppProperty(CommonConstants.PASSWORD));
		return ssoInfo;
	}

	/**
	 * Gets Configured Test Suites
	 */
	public static String getTestSuites() {
		return getValueFromAppProperty(CommonConstants.TEST_SUITES);
	}

	/**
	 * Method to get SSO Login or normal login
	 */
	public static boolean isSSOLogin() {
		return Boolean.parseBoolean(getValueFromAppProperty(CommonConstants.ISSSOLOGIN));
	}

	/**
	 * Method to get value from APP Property file
	 */
	public static String getValueFromAppProperty(String key) {
		return getPropertyValue(m_appProperties, key);
	}

	/**
	 * Gets Currency Symbol
	 */
	public static String getCurrencySymbol() {
		return m_currencySymbol;
	}

	/**
	 * Gets Test Execution Start Time
	 */
	public static String getTestExecutionStartTime() {
		return m_testexecutiontime;
	}

	/**
	 * Sets Test Execution Start Time
	 */
	public static void setTestExecutionStartTime(String testexecutiontime) {
		m_testexecutiontime = testexecutiontime;
	}

	/**
	 * Sets basic query parameters
	 */
	public static void setBasicQueryParams(String userId, String sessionId) {
		m_basicQueryParams = new LinkedHashMap<String, Object>();
		m_basicQueryParams.put("userid", userId);
		m_basicQueryParams.put("sid", sessionId);
		m_basicQueryParams.put("language", getValueFromAppProperty(CommonConstants.LANGUAGE));
	}

	/**
	 * Gets Basic Query Parameters
	 */
	public static Map<String, Object> getBasicQueryParams() {
		return m_basicQueryParams;
	}

	/**
	 * Method to backup default profile before automation run
	 */
	public static boolean defaultProfileBackupBeforeRun() {

		return false;
	}

	public static void setShibSession(String key, String value) {
		if (m_shibSession == null) {
			m_shibSession = new HashMap<String, String>();
			m_shibSession.put(key, value);
		}
	}

	public static Map<String, String> getShibSession() {
		if (m_shibSession != null) {
			return m_shibSession;
		}
		return null;
	}

	public static void setDefaultProfile(String defaultProfile) {
		m_defaultProfile = defaultProfile;
	}

	public static String getDefaultProfile() {
		return m_defaultProfile;
	}

	// -----------------------------------------------------------------------------
	// Private Methods and variables
	// -----------------------------------------------------------------------------

	/**
	 * This method will bind the all resource strings based on the language.
	 * 
	 * @throws Exception
	 */
	private static void bindResourceStrings() throws Exception {
		String fileName = String.format("resourcestrings_%s.properties",
				getValueFromAppProperty(CommonConstants.LANGUAGE));
		m_resourceProperties = FileOperations.getProperties(fileName);
	}

	/**
	 * This method will return the value from the property file based on the key.
	 * 
	 * @param property
	 * @param key
	 * @return
	 */
	private static String getPropertyValue(Properties property, String key) {
		property.getProperty(key);
		return property.getProperty(key);
	}

	/**
	 * Holds the APP property values
	 */
	private static Properties m_appProperties = null;

	/**
	 * Holds the resource property values
	 */
	private static Properties m_resourceProperties = null;

	/**
	 * Holds Currency Symbol based on client
	 */
	private static String m_currencySymbol = null;

	/**
	 * Holds Test Execution start time
	 */
	private static String m_testexecutiontime = null;

	/**
	 * Holds Performance KIBANA Data.
	 */
	public static List<String[]> PERFORMANCEKIBANADATA = null;

	/**
	 * Holds reports are configured or not.
	 */
	public static List<Boolean> ISREPORTCONFIGURED = new ArrayList<Boolean>();

	/**
	 * Holds Basic Query Parameters
	 */
	private static Map<String, Object> m_basicQueryParams = null;

	private static Map<String, String> m_shibSession = null;

	private static String m_defaultProfile = null;

	// -----------------------------------------------------------------------------
}
