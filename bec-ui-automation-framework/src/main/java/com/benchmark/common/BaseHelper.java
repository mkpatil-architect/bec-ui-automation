

package com.benchmark.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.SkipException;

import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;
import com.benchmark.core.util.CommonUtil;
import com.benchmark.framework.ui.SeleniumWrapper;
import com.benchmark.framework.ui.controls.MSCheckBox;
import com.benchmark.framework.ui.controls.MSGenericElement;

/**
 * Base Helper class which holds all common methods
 */
public abstract class BaseHelper {

	/**
	 * Method to append the error messages.
	 * 
	 * @param errorMessage
	 *            -> Error message to write the Global.TEST_CASE_ERROR_MESSAGES.
	 */
	public static void appendErrorMessage(String errorMessage) {
		if (Global.TEST_CASE_ERROR_MESSAGES != null) {
			Global.TEST_CASE_ERROR_MESSAGES.append(errorMessage);
			Global.TEST_CASE_ERROR_MESSAGES.append("<br />");
		} else {
			Log.writeMessage(LogLevel.ERROR, BaseHelper.class.getName(), errorMessage);
		}
		SeleniumWrapper.takeScreenShot();
	}

	/**
	 * Method to append the error messages.
	 * 
	 * @param errorMessage
	 *            -> Error message to write the Global.TEST_CASE_ERROR_MESSAGES.
	 */
	public static void appendErrorMessage(String errorMessage, Exception e) {
		if (e.getClass().getName().equals("org.testng.SkipException")) {
			Helper.skipTestCase(e.getMessage());
		}

		if (Global.TEST_CASE_ERROR_MESSAGES != null) {
			Global.TEST_CASE_ERROR_MESSAGES.append(errorMessage + " Exception message: " + e.getMessage());
			Global.TEST_CASE_ERROR_MESSAGES.append("<br />");
		}
		Log.writeMessage(LogLevel.ERROR, e.toString());
		SeleniumWrapper.takeScreenShot();
	}

	/**
	 * Method to append the error message with exception.
	 * 
	 * @param className
	 *            -> Class name
	 * @param methodName
	 *            -> Method Name
	 * @param e
	 *            -> Exception
	 */
	public static void appendErrorMessage(String className, String methodName, Exception e) {

		if (e.getClass().getName().equals("org.testng.SkipException")) {
			Helper.skipTestCase(e.getMessage());
		}

		String message = (e.getMessage() != null && e.getMessage().length() > 0) ? e.getMessage()
				: e.toString().substring(0, 50);
		if (Global.TEST_CASE_ERROR_MESSAGES != null) {
			Global.TEST_CASE_ERROR_MESSAGES.append(String.format(
					"Failed while processing method '%s' in class '%s'. Error message: %s \nPlease find logs for more error information.",
					methodName, className, message));
			Global.TEST_CASE_ERROR_MESSAGES.append("<br />");
		}
		Log.writeMessage(LogLevel.ERROR, className, message);
		SeleniumWrapper.takeScreenShot();
	}

	/**
	 * Waits for certain amount of time.
	 * 
	 * @param timeout
	 */
	public static void sleep(long timeout) {
		SeleniumWrapper.sleep(timeout);
	}

	/**
	 * Method to skip that particular test case.
	 * 
	 * @param skipMessage
	 *            -> Message why this test cases is skipped.
	 */
	public static void skipTestCase(String skipMessage) {
		throw new SkipException(String.format("[%s] - %s", Global.getClientName(), skipMessage));
	}

	/**
	 * Wait till loader icon got close
	 */
	public static boolean waitTillLoaderIconClose() {
		if (SeleniumWrapper.checkElementVisible(FindByTypeConstants.CSS_SELECTOR, "div.modal.fade.loading-screen")) {
			return waitTillLoaderIconLoading(FindByTypeConstants.CSS_SELECTOR, "div.modal.fade.loading-screen");
		} else if (SeleniumWrapper.checkElementVisible(FindByTypeConstants.CSS_SELECTOR, "div.MS-Loader")) {
			return waitTillLoaderIconLoading(FindByTypeConstants.CSS_SELECTOR, "div.MS-Loader");
		} else {
			return waitTillOldLoaderIconLoading();
		}
	}

	/**
	 * Wait till old loader icon closed
	 */
	private static boolean waitTillOldLoaderIconLoading() {
		for (int i = 1; i <= 60; i++) {
			if (SeleniumWrapper.checkElementVisible(FindByTypeConstants.ID, "screenBlocker")) {
				MSGenericElement locator = new MSGenericElement(FindByTypeConstants.ID, "screenBlocker");
				String style = locator.getAttributeValue("style");
				if (style.length() == 0) {
					return true;
				}
				if (style.contains("display: none;")) {
					if (i == 1) {
						sleep(8000);
					}
					return true;
				} else {
					sleep(3000);
				}
			}
		}
		SeleniumWrapper.takeScreenShot();
		Assert.fail(
				"After waiting for 3 mins also still loader icon not closed. Can�t proceed further to execute the next test step.");
		return false;
	}

	/**
	 * Wait till loaded icon got close
	 */
	private static boolean waitTillLoaderIconLoading(String findByType, String findByValue) {
		for (int i = 1; i <= 60; i++) {
			if (SeleniumWrapper.checkElementVisible(findByType, findByValue)) {
				sleep(3000);
			} else {
				sleep(4000);
				return true;
			}
			if (i == 60) {
				SeleniumWrapper.takeScreenShot();
				Assert.fail(
						"After waiting for 3 mins also still loader icon not closed. Can�t proceed further to execute the next test step.");
			}
		}
		return false;
	}

	/**
	 * Method to validate is Element available or not
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @return -> True/False
	 */
	public static boolean isElementAvailable(String findByType, String valueToFindElement) {
		return SeleniumWrapper.checkElementVisible(findByType, valueToFindElement);
	}

	/**
	 * Method to compare two boolean values
	 * 
	 * @param expected
	 *            -> True/False
	 * @param actual
	 *            -> True/False
	 * @param message
	 *            -> Error message.
	 * @return -> true/false
	 */
	public static boolean compareTwoBooleans(Boolean expected, Boolean actual, String message) {
		if (expected.equals(actual)) {
			return true;
		} else {
			appendErrorMessage(
					String.format("%s. Excepted: %s . Actual: %s", message, expected.toString(), actual.toString()));
			return false;
		}
	}

	/**
	 * Method to compare two Strings values.
	 * 
	 * @param expected
	 *            -> Expected String
	 * @param actual
	 *            -> Actual String
	 * @param message
	 *            -> Error message which strings are not matched. Example:
	 *            Scenario details are not matched.
	 * @return -> True/False
	 */
	public static boolean compareTwoStrings(String expected, String actual, String message) {
		boolean result = CommonUtil.compareTwoString(actual, expected);
		if (result == false) {
			appendErrorMessage(String.format("%s. Expected:%s, Actual:%s", message, expected, actual));
		}
		return result;
	}

	/**
	 * Method to get random value from the String List.
	 * 
	 * @param values
	 *            -> List[String]
	 * @return -> Random string value from the String List.
	 */
	public static String getRandomValueFromList(List<String> values) {
		Collections.shuffle(values);
		return values.get(0);
	}

	/**
	 * Method to get random values from list.
	 * 
	 * @param values
	 *            -> List[String]
	 * @return -> List[Random string value] from the String List.
	 */
	public static List<String> getRandomValuesFromList(List<String> values) {
		return getRandomValuesFromList(values, 3);
	}

	/**
	 * Method to get random values from list.
	 * 
	 * @param values
	 *            -> List[String]
	 * @return -> List[Random string value] from the String List.
	 */
	public static List<String> getRandomValuesFromList(List<String> values, int howManyValues) {
		Collections.shuffle(values);
		int i = 0;
		List<String> randomValues = new ArrayList<String>();
		for (String value : values) {
			randomValues.add(value);
			i++;
			if (i == howManyValues) {
				break;
			}
		}
		return randomValues;
	}

	/**
	 * Method to get random value from the Integer List.
	 * 
	 * @param values
	 *            -> Integer[]
	 * @return -> Random Integer value from the String List.
	 */
	public static int getRandomValueFromList(Integer[] values) {
		List<Integer> finalValues = Arrays.asList(values);
		Collections.shuffle(finalValues);
		return finalValues.get(0);
	}

	/**
	 * Method to compare two List<String>.
	 * 
	 * @param exceptedList
	 *            -> Expected List<String>
	 * @param actualList
	 *            -> Actual List<String>
	 * @param message
	 *            -> Scenario Names are not matched.
	 * @return -> True/False.
	 */
	public static boolean compareTwoList(List<String> exceptedList, List<String> actualList, String message) {
		boolean result = compareTwoList(exceptedList, actualList);
		if (result == false) {
			String finalmessage = String.format("%s. Excepted: %s . Actual: %s", message,
					Arrays.toString(exceptedList.toArray()), Arrays.toString(actualList.toArray()));
			appendErrorMessage(finalmessage);
			Log.writeMessage(LogLevel.ERROR, BaseHelper.class.getName(), finalmessage);
			return false;
		}
		return true;
	}

	/**
	 * Method to check isAngularJSCheckBox is checked or not
	 * 
	 * @param chkBox
	 *            
	 * @return -> True/False
	 */
	public static boolean isAngularJsCheckBoxChecked(MSCheckBox chkBox) {
		if (chkBox.getText().equals("check_box")) {
			return true;
		}
		return false;
	}

	/**
	 * Common Method to log all the info message across the product test cases
	 * execution
	 * 
	 * @param message
	 *            -> Message to log
	 */
	public static void log(String message) {
		Log.writeMessage(LogLevel.INFO, message);
	}

	/**
	 * Method to validate value difference with tolerance
	 * 
	 * @return -> True/False
	 */
	public static boolean validateValueDifferenceWithTolerance(String value1, String value2, String diffValue) {
		double diffSpend = convertFromKMBTtoDouble(diffValue);
		double dValue1 = convertFromKMBTtoDouble(value1);
		double dValue2 = convertFromKMBTtoDouble(value2);
		double diff = dValue1 - dValue2;
		Log.writeMessage(LogLevel.INFO, dValue1 + " - " + dValue2 + " = " + diff);

		String toleranceAmount = "10";
		String diffTolerance = withSuffix(Math.round(diff));
		String diffSpendTolerance = withSuffix(Math.round(diffSpend));
		if (diffTolerance.contains("k")) {
			toleranceAmount = "100";
		} else if (diffTolerance.contains("m")) {
			toleranceAmount = "100000";
		} else if (diffTolerance.contains("b")) {
			toleranceAmount = "100000000";
		} else if (diffTolerance.contains("t")) {
			toleranceAmount = "100000000000";
		} else {
			toleranceAmount = "100";
		}

		Double diffAmount = Math.abs(Double
				.parseDouble(diffTolerance.replace("k", "").replace("m", "").replace("b", "").replace("t", "").trim())
				- Double.parseDouble(
						diffSpendTolerance.replace("k", "").replace("m", "").replace("b", "").replace("t", "").trim()));
		Double Tolerance = Double.parseDouble(toleranceAmount);

		Log.writeMessage(LogLevel.INFO, "Diff amount is " + diffAmount * Tolerance + " Tolerance is " + Tolerance);
		return Helper.compareTwoBooleans(true, diffAmount <= Tolerance,
				String.format("Values are not matched: %s - %s = %s", value1, value2, diffValue)
						+ "\n After conversion:" + dValue1 + " - " + dValue2 + "=" + diff);
	}

	/**
	 * Method to convert KMBT to double
	 * 
	 * @param numInKMTformat
	 *            -> number in KMBT format
	 * @return -> Double
	 */
	public static Double convertFromKMBTtoDouble(String numInKMBTformat) {
		Double weight = 0.0;
		if (!numInKMBTformat.trim().equalsIgnoreCase("-")) {
			if (numInKMBTformat.contains("k")) {
				weight = Double.parseDouble(numInKMBTformat.replace("k", "")) * 1000;
			} else if (numInKMBTformat.contains("m")) {
				weight = Double.parseDouble(numInKMBTformat.replace("m", "")) * 1000000;
			} else if (numInKMBTformat.contains("b")) {
				weight = Double.parseDouble(numInKMBTformat.replace("b", "")) * 1000000000;
			} else if (numInKMBTformat.contains("t")) {
				weight = Double.parseDouble(numInKMBTformat.replace("t", "")) * 1000000000000L;
			} else {
				weight = Double.parseDouble(numInKMBTformat);
			}
		}
		return weight;
	}

	/**
	 * suffix the place magnitude designator, or convert to kmbt format
	 * 
	 * @param count
	 * @return
	 */
	public static String withSuffix(long count) {
		if (count < 1000)
			return "" + count;
		int exp = (int) (Math.log(count) / Math.log(1000));
		return String.format("%.1f%c", count / Math.pow(1000, exp), "kmgtpe".charAt(exp - 1));
	}

	// ------------------------------------------------------------------------------

	/**
	 * Gets value from App property file based on the key.
	 * 
	 * @param key
	 * @return
	 */
	protected static String getValue(String key) {
		return Global.getValueFromAppProperty(key);
	}

	/**
	 * Method to compare two String Lists. If both list size matches then only
	 * it will verify the data.
	 * 
	 * @param expected
	 *            -> Expected List<String>
	 * @param actual
	 *            -> Actual List<String>
	 * @return -> Returns True/False.
	 */
	private static boolean compareTwoList(List<String> expected, List<String> actual) {
		if (expected.size() != actual.size()) {
			appendErrorMessage("Size of both lists are not same. Expected Size: " + expected.size() + " Actual Size:"
					+ actual.size() + "<br />");
			return false;
		}
		if (expected.equals(actual)) {
			return true;
		} else {
			for (String value : actual) {
				if (!expected.contains(value)) {
					appendErrorMessage(value + ", data not found <br />");
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Method to get percentage progress of loader icon.
	 */
	protected static int getPercentage(String style) {
		try {
			String[] values = style.split(";");
			for (String value : values) {
				if (value.contains("width")) {
					double percentValue = Double.parseDouble(value.split(":")[1].replace("%", "").trim());
					return (int) percentValue;
				}
			}
		} catch (Exception e) {
			return -1;
		}
		return -1;
	}

	public static boolean waitTillOldLoaderIconClose() {
		int i = 0;
		for (i = 1; i <= 60; i++) {
			if (SeleniumWrapper.checkElementVisible(FindByTypeConstants.ID, "screenBlocker")) {
				try {
					MSGenericElement element = new MSGenericElement(FindByTypeConstants.ID, "screenBlocker");
					String style = element.getAttributeValue("style");
					if (style.contains("display: none;")) {
						return true;
					}
				} catch (NoSuchElementException e) {
					return true;
				}
			} else {
				sleep(3000);
			}
		}
		if (i == 60) {
			SeleniumWrapper.takeScreenShot();
			Assert.fail(
					"After waiting for 3 mins also Loader icon not closed. Can�t proceed further to execute the next test step.");
		}
		return false;
	}

}
