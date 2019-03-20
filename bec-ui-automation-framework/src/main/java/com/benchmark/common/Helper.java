

package com.benchmark.common;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.testng.Assert;

import com.benchmark.constants.CommonConstants;
import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;
import com.benchmark.core.util.CommonUtil;
import com.benchmark.core.util.FileOperations;
import com.benchmark.framework.ui.SeleniumWrapper;
import com.benchmark.framework.ui.controls.MSGenericElement;



/**
 * Helper class which will be used across all pages.
 */
public class Helper extends BaseHelper {
	static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

	/**
	 * Gets First and last name of user
	 * 
	 * @return -> User Name
	 */
	public static String getFirstAndLastName() {
		return String.format("%s %s", getValue(CommonConstants.FIRSTNAME), getValue(CommonConstants.LASTNAME)).trim()
				.toLowerCase();
	}

	/**
	 * Gets Previous or Next Month
	 */
	public static String getPreviousOrNextMonth(String sourceDate, String format, int noOfMonths) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date ddd = new Date();
			ddd = sdf.parse(sourceDate);
			Calendar c = Calendar.getInstance();
			c.setTime(ddd);
			c.add(Calendar.MONTH, noOfMonths);
			sourceDate = sdf.format(c.getTime());
			return sourceDate.toString();
		} catch (Exception e) {
			appendErrorMessage(e.toString());
		}
		return null;
	}

	/**
	 * convertsDate into required format
	 * 
	 * @param dateParam
	 *            ('23-Apr-19')
	 * @param dateFormat
	 *            ('DD-MM-YYYY')
	 * @return 23-04-2019
	 */
	public static String convertDateFormat(String sourceDate, String fromFormat, String toFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(fromFormat);
		Date ddd = new Date();
		try {
			ddd = sdf.parse(sourceDate);
		} catch (Exception e) {

			e.printStackTrace();
		}
		sdf = new SimpleDateFormat(toFormat);
		return sdf.format(ddd);
	}

	/**
	 * Get UserEmailId
	 */
	public static String getUserEmailId() {
		return getValue(CommonConstants.USER_NAME).trim().toLowerCase();
	}

	/**
	 * Method to wait till element is available or loaded to perform next action
	 * 
	 * @return -> True/False
	 */
	public static boolean waitTillElementAvailable(String findByType, String findByValue) {
		return waitTillElementAvailable(findByType, findByValue, true);
	}

	/**
	 * Method to wait till element is available or loaded to perform next action
	 * 
	 * @return -> True/False
	 */
	public static boolean waitTillElementAvailable(String findByType, String findByValue, boolean withAssert) {
		try {
			boolean isElementFound = false;
			for (int i = 0; i <= 60; i++) {
				if (SeleniumWrapper.checkElementVisible(findByType, findByValue)) {
					isElementFound = true;
					return true;
				} else {
					sleep(3000);
				}
			}
			if (isElementFound == false) {
				SeleniumWrapper.takeScreenShot();
				String message = "Page not loaded. Element not found on the page after waiting for 3 mins. Locator: "
						+ findByValue;
				if (withAssert) {
					Assert.assertTrue(isElementFound, message);
				}
				Log.writeMessage(LogLevel.INFO, message);
				return false;
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, e.toString());
		}
		return false;
	}

	/**
	 * Wait for 3 minutes if report loader icon present
	 */
	public static void waitIfReportLoaderIconPresent(int milliSeconds) {
		boolean waitDone = false;
		Log.writeMessage(LogLevel.INFO, "Wait for " + (milliSeconds != 0 ? milliSeconds / 1000 : 180)
				+ " seconds for the loader icon to disappear");
		for (int i = 0; i < (milliSeconds != 0 ? milliSeconds / 1000 : 180); i++) {
			if (SeleniumWrapper.isElementVisible(FindByTypeConstants.CSS_SELECTOR,
					"form .white-frame .form-field:nth-of-type(3) .btn.rp-ui-btn")) {
				waitDone = true;
				sleep(1000);
			} else {
				Log.writeMessage(LogLevel.INFO,
						"Waited " + (waitDone ? i : 0) + " seconds before continuing to next step");
				sleep(1000);
				break;
			}
		}

	}

	/**
	 * Method to wait till element is available or loaded to perform next action
	 * 
	 * @return -> True/False
	 */
	public static boolean waitTillElementAvailable(String findByType, String findByValue, String errorMessage) {
		try {
			boolean isElementFound = false;
			for (int i = 0; i <= 60; i++) {
				if (SeleniumWrapper.checkElementVisible(findByType, findByValue)) {
					isElementFound = true;
					return true;
				} else {
					sleep(3000);
				}
			}
			if (isElementFound == false) {
				SeleniumWrapper.takeScreenShot();
				Helper.skipTestCase(errorMessage + " Element not found on the page after waiting for 3 mins. Locator: "
						+ findByValue);
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, e.toString());
		}
		return false;
	}

	/**
	 * Method to wait till element is available or loaded to perform next action
	 * 
	 * @return -> True/False
	 */
	public static boolean waitTillElementAvailable(String parentFindByType, String parentFindByValue,
			String childFindByType, String childFindByValue) {
		try {
			boolean isElementFound = false;
			MSGenericElement parent = new MSGenericElement(parentFindByType, parentFindByValue);
			for (int i = 0; i <= 60; i++) {
				if (parent.isElementAvailableInControl(childFindByType, childFindByValue)) {
					return true;
				} else {
					sleep(2000);
				}
			}
			if (isElementFound == false) {
				SeleniumWrapper.takeScreenShot();
				Assert.assertTrue(isElementFound,
						"Page not loaded. Element not found on the page after waiting for 2 mins");
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, e.toString());
		}
		return false;
	}

	

	
	
//	/**
//	 * Method to wait till page load
//	 */
//	public static boolean waitTillPageLoad() {
//		Helper.sleep(4000);
//		String value = Pages.Home().Operations.getCurrentMenuText().trim().toLowerCase();
//		if (value.equals(Global.getResourceValue(ResourceConstants.MENU_HOME).toLowerCase().trim())) {
//			if (!isElementAvailable(FindByTypeConstants.CSS_SELECTOR, "div.home-chart-holder")) {
//				return true;
//			}
//		}
//		if (waitTillElementAvailable(FindByTypeConstants.ID, "ngProgress-container")) {
//			for (int i = 1; i <= 60; i++) {
//				MSGenericElement progress = new MSGenericElement(FindByTypeConstants.ID, "ngProgress");
//				String style = progress.getAttributeValue("style");
//				int percentage = getPercentage(style);
//				// TODO: Need to change after 5.5 version to percentage == 100
//				if (percentage == 0 || percentage >= 100) {
//					return true;
//				} else {
//					sleep(2000);
//				}
//				if (i == 60) {
//					SeleniumWrapper.takeScreenShot();
//					Assert.fail(
//							"After waiting for 2 mins also still page progress bar not reached 100%. Can�t proceed further to execute the next test step.");
//				}
//			}
//		} else {
//			if (waitTillOldLoaderIconClose()) {
//				return true;
//			}
//			Helper.sleep(8000);
//		}
//		Helper.sleep(2000);
//		return false;
//	}

	
	

	/**
	 * Method to close all Warning pop-ups
	 * 
	 * @return -> True/False
	 */
	public static boolean verifyWarningOrOtherPopupAndClose() {
		try {
			if (SeleniumWrapper.checkElementVisible(FindByTypeConstants.CSS_SELECTOR, "body.modal-open")) {

				if (SeleniumWrapper.checkElementVisible(FindByTypeConstants.CSS_SELECTOR,
						"div [class ^= 'modal-dialog']")) {
					if (SeleniumWrapper.checkElementVisible(FindByTypeConstants.CSS_SELECTOR,
							".modal-footer .btn.ms-btn.btn-confirm")) {
						createElementAndClick(FindByTypeConstants.CSS_SELECTOR,
								".modal-footer .btn.ms-btn.btn-confirm");
					}
				} else if (SeleniumWrapper.checkElementVisible(FindByTypeConstants.CSS_SELECTOR,
						".ctrl-btn-holder button.btn.save")) {
					createElementAndClick(FindByTypeConstants.CSS_SELECTOR, ".ctrl-btn-holder button.btn.save");
				}
				Helper.sleep(4000);
			}
			if (SeleniumWrapper.checkElementVisible(FindByTypeConstants.CSS_SELECTOR, "div.static-modal")
					|| SeleniumWrapper.checkElementVisible(FindByTypeConstants.CSS_SELECTOR, "#download-upload")) {
				createElementAndClick(FindByTypeConstants.CSS_SELECTOR, ".material-icons.closeBtn");
			}
			Helper.sleep(4000);
			return true;
		} catch (Exception e) {
			Helper.appendErrorMessage("Faile while closing warning popup", e);
		}
		return false;
	}

	/**
	 * Method to Wait till file download completes
	 */
	public static String waitTillFileDowloadAndGetFile(String searchFileName, String fileExtension, int waitTime) {
		try {
			String sourcePath = getDownloadPath();
			String fileName = null;
			for (int i = 0; i <= 60; i++) {
				fileName = FileOperations.getFileFromDirectory(sourcePath, searchFileName);
				if (fileName != null) {
					if (fileName.indexOf(fileExtension) > 0) {
						String ext = fileName.substring(fileName.indexOf(fileExtension) + 1);
						Log.writeMessage(LogLevel.INFO, "ext: " + ext);
						if (ext.equals(fileExtension.replace(".", "").trim())) {
							File fName = new File(fileName);
							String newFilePath = FileOperations.copyFile(fileName, getClientDownloadPath(),
									fName.getName());
							FileOperations.deleteFile(fileName);
							return newFilePath;
						}
					}
				}
				Helper.sleep(waitTime);
			}

			long finalMseconds = 60 * waitTime;
			Helper.appendErrorMessage(String.format("After waiting for %02d mins also file not downloaded.",
					TimeUnit.MILLISECONDS.toMinutes(finalMseconds)));
		} catch (Exception e) {
			Helper.appendErrorMessage("Failed - waitTillFileDowloadAndGetFile()", e);
		}
		return null;
	}

	/**
	 * Method to wait till
	 */
	public static boolean waitTillUploadModalClose() {
		for (int i = 0; i <= 60; i++) {
			if (SeleniumWrapper.checkElementVisible(FindByTypeConstants.CSS_SELECTOR, ".static-modal")) {
				Helper.sleep(1000);
			} else {
				return true;
			}
		}
		long finalMseconds = 60 * 1000;
		Helper.appendErrorMessage(String.format("After waiting for %02d mins also file not downloaded.",
				TimeUnit.MILLISECONDS.toMinutes(finalMseconds)));
		return false;
	}

	/**
	 * Method to create element and click to close pop-up
	 * 
	 * @param findByType
	 * @param findByValue
	 */
	private static void createElementAndClick(String findByType, String findByValue) {
		Log.writeMessage(LogLevel.INFO, "Pop-up Element found - " + findByType + ":" + findByValue);
		MSGenericElement element = new MSGenericElement(findByType, findByValue);
		element.click();
	}

	/**
	 * Gets Download path
	 */
	public static String getDownloadPath() {
		return System.getProperty("user.home") + "\\Downloads\\";
	}

	/**
	 * Gets Client specific Download path
	 */
	public static String getClientDownloadPath() {
		return getDownloadPath() + Global.getClientName() + "\\";
	}

	

	
	/**
	 * Waits till loader is closes for a specific iframe
	 * 
	 * @param iFrame
	 * @return
	 */
	public static String waitTillArcadiaLoaderIconClose() {
		int i = 0;
		boolean flag = true;
		String errorMessage = "";
		if (isElementAvailable(FindByTypeConstants.CSS_SELECTOR, ".arcadiaReport-iframe")) {
			Log.writeMessage(LogLevel.INFO, "IFrame = .arcadiaReport-iframe");
			while (!isElementAvailable(FindByTypeConstants.CSS_SELECTOR, ".arcadiaReport-iframe")) {
				Helper.log("Waiting for Report IFrame");
				if (i == 2000) {
					Helper.appendErrorMessage("Reports are not configured.");
					flag = false;
					errorMessage = errorMessage.equals("") ? CommonConstants.ERROR1 : errorMessage;
					break;
				}
				i++;
			}
		}
		if (isElementAvailable(FindByTypeConstants.CSS_SELECTOR, "iframe[ng-if='!isArcadiaReport']")) {
			Log.writeMessage(LogLevel.INFO, "IFrame = iframe[ng-if='!isArcadiaReport']");
			flag = true;
			while (!isElementAvailable(FindByTypeConstants.CSS_SELECTOR, "iframe[ng-if='!isArcadiaReport']")) {
				Helper.log("Waiting for Report IFrame");
				if (i == 2000) {
					Helper.appendErrorMessage("Reports are not configured.");
					errorMessage = errorMessage.equals("") ? CommonConstants.ERROR1 : errorMessage;
					flag = false;
					break;
				}
				i++;
			}
		}
		if (flag) {
			while (isElementAvailable(FindByTypeConstants.CSS_SELECTOR,
					".arcadiaReport-spinner.animated.spinnerFadeIn")) {
				Helper.log("Acadia Spinner.");
			}
			Helper.sleep(1500);
			if (isElementAvailable(FindByTypeConstants.CSS_SELECTOR, "#reportConfigList")) {
				Helper.log("Custom report page is opened by default.");
				errorMessage = "Custom report page is opened by default.";
			}
			if (isElementAvailable(FindByTypeConstants.CSS_SELECTOR, ".arcadiaReport-iframe")) {
				SeleniumWrapper.switchToIFrame(FindByTypeConstants.CSS_SELECTOR, ".arcadiaReport-iframe");
				while (isElementAvailable(FindByTypeConstants.CSS_SELECTOR, "li[class='gs-w viz-dashboard-chart']")) {
					Helper.log("Waiting for widget data to be load.");
				}
				if (isElementAvailable(FindByTypeConstants.CSS_SELECTOR, "#main")) {
					if (isElementAvailable(FindByTypeConstants.CSS_SELECTOR, "#app-selected-list")) {
						MSGenericElement parent = new MSGenericElement(FindByTypeConstants.CSS_SELECTOR,
								"#app-selected-list");
						if (parent.isElementAvailableInControl(FindByTypeConstants.CSS_SELECTOR, "ul")) {
							int count = parent.getElementsCountFromParent(FindByTypeConstants.CSS_SELECTOR, "ul li");
							log("Drilldown explorer report is configured." + count);
						}
						int count = parent.getElementsCountFromParent(FindByTypeConstants.CSS_SELECTOR,
								"div[id^=chart-refresh][style *= 'display: block;']");
						int totalCount = parent.getElementsCountFromParent(FindByTypeConstants.CSS_SELECTOR,
								"div[id^=chart-refresh]");

						while (count != 0) {
							count = parent.getElementsCountFromParent(FindByTypeConstants.CSS_SELECTOR,
									"div[id^=chart-refresh][style *= 'display: block;']");
							Helper.log("Widget Loading Count : " + count);
						}
						int divCount = parent.getElementsCountFromParent(FindByTypeConstants.CSS_SELECTOR,
								"div[class='gridster-container'] .gs-w .viz-permanent.viz-chart > div");
						int tableCount = parent.getElementsCountFromParent(FindByTypeConstants.CSS_SELECTOR,
								"div[class='gridster-container'] .gs-w .viz-permanent.viz-chart > table");
						long timecounter = 0;
						while (totalCount != (divCount + tableCount)) {
							divCount = parent.getElementsCountFromParent(FindByTypeConstants.CSS_SELECTOR,
									"div[class='gridster-container'] .gs-w .viz-permanent.viz-chart > div");
							tableCount = parent.getElementsCountFromParent(FindByTypeConstants.CSS_SELECTOR,
									"div[class='gridster-container'] .gs-w .viz-permanent.viz-chart > table");
							int actualWidgets = divCount + tableCount;
							Helper.log("Widget Loading is in progress : ExpectedWidgets=" + totalCount
									+ " : ActualWidgets=" + actualWidgets);
							Helper.sleep(1000);
							timecounter++;
							if (timecounter == 30) {
								if (actualWidgets > 0 && actualWidgets < totalCount) {
									Helper.log("Some of the widgets are not loaded.");
									errorMessage = errorMessage.equals("") ? CommonConstants.ERROR2 : errorMessage;
									flag = false;
									break;
								}
								Helper.log("Waited for widget to load. But didnt loaded after waiting for 1 minute.");
								errorMessage = errorMessage.equals("") ? CommonConstants.ERROR2 : errorMessage;
								flag = false;
								break;
							}
						}
					} else if (isElementAvailable(FindByTypeConstants.CSS_SELECTOR, "#view-chart-container")) {
						while (!isElementAvailable(FindByTypeConstants.CSS_SELECTOR, "div[class='inmodal']")) {
							Helper.log("Loading chart is in progress...");
							Helper.sleep(1000);
						}
					} else if (isElementAvailable(FindByTypeConstants.CSS_SELECTOR, "#alert-error-messages")) {
						Helper.sleep(2000);
						errorMessage = new MSGenericElement(FindByTypeConstants.CSS_SELECTOR, "#alert-error-messages")
								.getAttributeValue("innerText");
						Helper.log("Error:: " + errorMessage);
					} else {
						errorMessage = errorMessage.equals("") ? CommonConstants.ERROR5 : errorMessage;
						Helper.log("Error:: " + errorMessage);
					}
				}
				SeleniumWrapper.switchBackToDefaultContent();
			}
		}
		return errorMessage;

	}

	/**
	 * Gets Environment for Kibana
	 */
	public static String getEnvironmentForKibana(String url) {
		String environment = url.contains("prodtest") ? "prodtest" : "dev";
		if (!environment.equals("prodtest")) {
			String ssoUrl = Global.getValueFromAppProperty("ssourl");

			if (ssoUrl != null) {
				if (ssoUrl.contains("decisioncloud.marketshare.com")) {
					environment = "prod";
				}
			}
		}
		return environment;
	}

	

	/**
	 * Method to check the CheckBox
	 */
	public static boolean checkCheckBox(MSGenericElement element, boolean isCheck) {
		try {
			String value = element.getAttributeValue("textContent").toLowerCase().trim();

			if (isCheck && (value.equals("check_box_outline_blank"))) {
				element.click();
			} else if (!isCheck && (value.equals("check_box"))) {
				element.click();
			}
			return true;
		} catch (Exception e) {
			appendErrorMessage("Failed - checkCheckBox", e);
		}
		return false;
	}

	public static String getDynamicCustomReportNameToCreate() {
		return String.format("UI_AUTO_RB_%s",
				CommonUtil.getTimeStamp().replace("-", "").replace(":", "").replace(".", "").replace(" ", "_").trim());
	}

	/**
	 * get the random number with in a range as specified by the values of min
	 * and max
	 * 
	 * @param max
	 * @param min
	 * @return Random integer
	 */
	// Min + (int)(Math.random() * ((Max - Min) + 1)) random number in range
	public static int getRandNumberInRange(int max, int min) {
		// Random num = new Random();
		// int randomNum = num.nextInt((count - startlimit) + 1) + startlimit;
		int randomNum = min + (int) (Math.random() * ((max - min) + 1));
		return randomNum;
	}

	/**
	 * Check if element is available and visible
	 */
	public static boolean isElementAvailable(String selector, String elementIdentifier) {
		boolean flag = false;
		try {
			flag = SeleniumWrapper.isElementVisible(selector, elementIdentifier);
		} catch (NoSuchElementException | StaleElementReferenceException e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * Return boolean (logical AND) result from an boolean list
	 * 
	 * @param results
	 * @return
	 */
	public static boolean returnResults(List<Boolean> results) {
		return results.contains(false) ? false : true;
	}

	/**
	 * subtract the number of days to date in java
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static String subtractDays(String date, int days) {
		// SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		GregorianCalendar cal = new GregorianCalendar();
		try {
			cal.setTime(sdf.parse(date));
		} catch (ParseException e) {
			skipTestCase("Parsing of date failed with error " + e.getMessage());
			e.printStackTrace();
		}
		cal.add(Calendar.DATE, -days);
		return sdf.format(cal.getTime()).toString();
	}

	/**
	 * add the number of days to date
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static String addDays(String date, int days) {
		// SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		GregorianCalendar cal = new GregorianCalendar();
		try {
			cal.setTime(sdf.parse(date));
		} catch (ParseException e) {
			skipTestCase("Parsing of date failed with error " + e.getMessage());
			e.printStackTrace();
		}
		cal.add(Calendar.DATE, days);
		return sdf.format(cal.getTime()).toString();
	}

	/**
	 * get week number based on the date passed
	 */
	public static float getWeekNumberForDate(String dateToProcess) {
		int week = 0, day = 0;
		try {
			Date date = sdf.parse(dateToProcess);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			week = cal.get(Calendar.WEEK_OF_YEAR);
			day = cal.get(Calendar.DAY_OF_WEEK);
		} catch (ParseException e) {
			skipTestCase("Parsing of date failed with error " + e.getMessage());
			e.printStackTrace();
		}
		return Float.parseFloat(Integer.toString(week) + "." + Integer.toString(day));
	}

	/**
	 * convert string format date to date
	 */
	public static Date convertDateFromString(String dateInString) {
		try {
			return sdf.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
			Log.writeMessage(LogLevel.INFO, "Conversion failed for date String " + dateInString);
		}
		return null;
	}

	/**
	 * get difference weeks based on the from and to dates
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public static int getWeeksBetween(Date fromDate, Date toDate) {
		if (toDate.before(fromDate)) {
			return -getWeeksBetween(toDate, fromDate);
		}
		fromDate = resetTime(fromDate);
		toDate = resetTime(toDate);

		Calendar cal = new GregorianCalendar();
		cal.setTime(fromDate);
		int weeks = 0;
		while (cal.getTime().before(toDate)) {
			// add another week
			cal.add(Calendar.WEEK_OF_YEAR, 1);
			weeks++;
		}
		return weeks;
	}

	/**
	 * reset time based on Gregorian calendar
	 * 
	 * @param d
	 * @return
	 */
	public static Date resetTime(Date dateToReset) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(dateToReset);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * Remove any currency from number and return number in string format
	 */
	public static String removesAnyCurrencySymbolFromAString(String numberWithCurrencySymbol) {
		NumberFormat format = NumberFormat.getCurrencyInstance();
		Number number = null;
		try {
			number = format.parse(numberWithCurrencySymbol);
			return number.toString();
		} catch (ParseException e) {
			Log.writeMessage(LogLevel.INFO,
					"Curreny is not a valid one in the number string " + numberWithCurrencySymbol);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Remove currency based on app properties in one shot along with comma
	 * formatting
	 */
	public static String removeCurrencyBasedonCurrencySetFromAppAproperties(String stringWithCurrency) {
		return stringWithCurrency.trim().replaceAll("[" + Global.getCurrencySymbol().trim().toString() + ",]", "");
	}

	/**
	 * Convert a number in decimal format to kmbt number
	 */
	public static String convertFromDecimalToKMBTforamt(Double numberToConvert) {
		String newValue = null, kilo = "1000", millions = "1000000", billions = "1000000000",
				trillions = "1000000000000", quadrillion = "1000000000000000";
		DecimalFormat df = new DecimalFormat("#.#");
		if (numberToConvert <= 1000) {
			newValue = df.format((numberToConvert));
		} else if ((numberToConvert / new Double(kilo)) <= new Double(kilo)) {
			newValue = df.format((numberToConvert / new Double(kilo))) + "K";
		} else if ((numberToConvert / new Double(millions)) <= new Double(kilo)) {
			newValue = df.format((numberToConvert / new Double(millions))) + "M";
		} else if ((numberToConvert / new Double(billions)) <= new Double(kilo)) {
			newValue = df.format((numberToConvert / new Double(billions))) + "B";
		} else if ((numberToConvert / new Double(trillions)) <= new Double(kilo)) {
			newValue = df.format((numberToConvert / new Double(trillions))) + "T";
		} else if ((numberToConvert / new Double(quadrillion)) <= new Double(kilo)) {
			newValue = df.format((numberToConvert / new Double(quadrillion))) + "P";
		}
		return newValue;
	}

	

	/**
	 * Method to upload file using sendKeys
	 */
	public static void uploadFileUsingSendKeys(String cssSelectorPath, String fileNameWithPath) {
		SeleniumWrapper.webDriver().findElement(By.cssSelector(cssSelectorPath)).sendKeys(fileNameWithPath);
	}

	/**
	 * Waits till the report loader completes
	 */
	public static void waitTillReportRunLoader() {
		boolean isLoaderPresent = isElementAvailable(FindByTypeConstants.CSS_SELECTOR, ".spinner-circular-loader-path");
		Log.writeMessage(LogLevel.DEBUG, "isLoaderPresent: " + isLoaderPresent);
		while (isElementAvailable(FindByTypeConstants.CSS_SELECTOR, ".spinner-circular-loader-path")) {
			String message = new MSGenericElement(FindByTypeConstants.CSS_SELECTOR, ".loader-message.ng-binding")
					.getAttributeValue("innerText");
			Log.writeMessage(LogLevel.DEBUG, "Loader icon is available...Message is : " + message);
		}
	}

	public static boolean checkElementAvailable(String elementType, String elementValue) {
		return SeleniumWrapper.checkElementVisible(elementType, elementValue);
	}

}
