

package com.benchmark.pages;

import java.util.List;

import com.benchmark.common.Global;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;
import com.benchmark.core.util.CommonUtil;
import com.benchmark.framework.ui.SeleniumWrapper;

/**
 * Default class which we use to start and stop Selenium WebDriver.
 */
public class Default {

	/**
	 * Loads all properties values and starts the Selenium WebDriver.
	 */
	public static boolean Start() {
		if (Global.buildProperties()) {
			if (loadHomepage()) {
				Log.writeMessage(LogLevel.INFO, String.format(
						"Page loaded successfully. Starting test suites execution @ %s\n------------------------------------------------------------------------------------------------------------------------- \nTest Cases Execution: \n",
						CommonUtil.getFormatedDate("yyyy-MM-dd HH:mm:ss.SSS ")));

				return true;
			} else {
				Log.writeMessage(LogLevel.ERROR, "Failed to load home page, Can't proceed with test execution.");
			}
		}
		return false;
	}

	/**
	 * Stops the Selenium WebDriver
	 */
	public static void Stop() {
		try {
			Log.writeMessage(LogLevel.INFO, "Stopping WebDriver");
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, e.toString());
		} finally {
			SeleniumWrapper.stop();
		}
	}

	/**
	 * Method to open page
	 */
	private static boolean loadHomepage() {
		try {

			List<String> loginInfo = Global.getSSOAndStandaloneInfo();
			SeleniumWrapper.start();
			SeleniumWrapper.openPage(loginInfo.get(3));
			return true;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, e.getMessage());
		}
		return false;
	}

}
