package com.benchmark.core.util;

import org.testng.Assert;
import org.testng.Reporter;

import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;
import com.thoughtworks.selenium.SeleneseTestBase;

public class SoftAssertion extends SeleneseTestBase {

	private StringBuffer assertionErrors;

	public SoftAssertion() {
		assertionErrors = new StringBuffer();
	}

	public void verifyTrue(Boolean flag, String errorMsg) {
		verifyTrue(flag,errorMsg,true);		
	}

	public void verifyTrue(Boolean flag, String errorMsg, Boolean appendStack) {
		try {
			Assert.assertTrue(flag);
		} catch (Error e) {
			if(appendStack){
				assertionErrors.append(errorMsg + " " + e + "\n");
			}else{
				assertionErrors.append(errorMsg +"\n");
			}			
			Reporter.log(errorMsg + " " + e + "\n");
			Log.writeMessage(LogLevel.DEBUG, CLASS_NAME,
					errorMsg + " - " + e.getMessage());
		}
	}

	public void verifyFalse(Boolean flag, String errorMsg) {
		try {
			Assert.assertFalse(flag);
		} catch (Error e) {
			assertionErrors.append(errorMsg + " " + e + "\n");
			Reporter.log(errorMsg + " " + e + "\n");
			Log.writeMessage(LogLevel.DEBUG, CLASS_NAME,
					errorMsg + " - " + e.getMessage());
		}
	}

	public void clearAssertionErrors() {
		assertionErrors = new StringBuffer();
	}

	public String getAssertionErrors(){
		return assertionErrors.toString();
	}

	public void checkForAssertionErrors() {
		String verificationErrorString = assertionErrors.toString();
		clearVerificationErrors();
		if (!verificationErrorString.equals("")) {
			fail(verificationErrorString);
		}
	}

	private final static String CLASS_NAME = SoftAssertion.class.getName();
}
