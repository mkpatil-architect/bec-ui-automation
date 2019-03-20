

package com.benchmark.testcases.home;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.benchmark.common.Global;
import com.benchmark.managers.Pages;
import com.benchmark.testcases.BaseUITest;

/**
 * Validate the home page Url. <br>
 * Test Steps: <br>
 * 1. Browse citrix url <br>
 * 2. Get the curremt url <br>
 * 3. Validates the current url <br>
 */
public class VerifyHomePage extends BaseUITest {

	/**
	 * Constructor
	 */
	public VerifyHomePage() {
		super(DESCRIPTION, TESTSTEPS);
	}

	@Test(description = DESCRIPTION)
	public void test() {
		Global.TEST_CASE_ERROR_MESSAGES = new StringBuilder();
		m_isTestPassed = Pages.Home().Operations.validateHomePageControls();
		Assert.assertTrue(m_isTestPassed, getErrorMessage());
	}

	private final static String DESCRIPTION = "Validates the curent url of HomePage Load.";
	private final static String TESTSTEPS = "1.  Browse Benchmark url .%%%2. Get the curremt url.%%%3.Validates the current url.";
}