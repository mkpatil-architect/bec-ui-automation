

package com.benchmark.core.util;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.core.MultivaluedMap;
import com.google.gson.Gson;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class CommonUtil {
	// Default constructor
	private CommonUtil() {
	}

	/**
	 * Method to compare two Map objects
	 * 
	 * @param a -> Map<String,Object>
	 * @param b -> Map<String,Object>
	 * @return -> True/false
	 */
	public static boolean compareTwoMapObjects(Map<String, Object> a, Map<String, Object> b) {
		if (a.size() != b.size())
			return false;
		for (Entry<String, Object> entry : a.entrySet()) {
			Object aValue = entry.getValue().toString();
			Object bValue = b.get(entry.getKey()).toString();
			if (!aValue.equals(bValue)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Method to compare two Map String
	 * 
	 * @param a -> Map<String,Object>
	 * @param b -> Map<String,Object>
	 * @return -> True/false
	 */
	public static boolean compareTwoMaps(Map<String, String> a, Map<String, String> b) {
		if (a.size() != b.size())
			return false;
		for (Entry<String, String> entry : a.entrySet()) {
			Object aValue = entry.getValue();
			Object bValue = b.get(entry.getKey());
			if (!aValue.equals(bValue)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Method will compare two strings
	 * 
	 * @param actual
	 * @param expected
	 * @return -> True/False
	 */
	public static boolean compareTwoString(String actual, String expected) {
		return actual.equals(expected);
	}

	/**
	 * This method will compare to List<String> collections.
	 * 
	 * @param a List<String>
	 * @param b List<String>
	 * @return True/False
	 */
	public static boolean compareTwoList(List<String> a, List<String> b) {
		if (a.size() != b.size()) {
			return false;
		}
		for (int i = 0; i < a.size(); i++) {
			if (!a.get(i).equals(b.get(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This method will compare two String[]
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean compareTwoStringArrays(String[] a, String[] b) {
		return compareTwoList(Arrays.asList(a), Arrays.asList(b));
	}

	/**
	 * This method will convert the double value to string value.
	 * 
	 * @param value
	 * @return
	 */
	public static String convertDoubleToString(double value) {
		String temp = Double.toString(value);
		return (temp.contains(".0")) ? Integer.toString((int) (value)) : temp;
	}

	/**
	 * Convert String List as String with given delimiter
	 * 
	 * @param values
	 * @param delimiter
	 * @return -> String
	 */
	public static String convertStringListAsAString(List<String> values, String delimiter) {
		StringBuilder joinedString = new StringBuilder();
		for (String value : values) {
			joinedString.append(value);
			joinedString.append(delimiter);
		}
		return joinedString.toString().substring(0, joinedString.toString().length() - 1);
	}

	/**
	 * This method will convert String[] as single string with joining all values
	 * with specified delimiter.
	 * 
	 * @param values
	 * @param delimiter
	 * @return
	 */
	public static String convertStringArrayAsAString(String[] values, String delimiter) {
		return convertStringListAsAString(Arrays.asList(values), delimiter);
	}

	/**
	 * This method will convert String[] to Integer[].
	 * 
	 * @param strings
	 */
	public static Integer[] convertStringArrayToIntegerArray(String[] values) {
		Integer[] intarray = new Integer[values.length];
		int i = 0;
		for (String value : values) {
			intarray[i] = Integer.parseInt(value.trim());
			i++;
		}
		return intarray;
	}

	/**
	 * This method will convert the value into KMBT format.
	 * 
	 * @param value
	 * @return
	 */
	public static String convertToKMBT(String value) {
		return convertValueToKMBT(value, false, 0);
	}

	/**
	 * This method will convert the value into KMBT format.
	 * 
	 * @param value
	 * @param divBy
	 * @return
	 */
	public static String convertToKMBT(String value, int divBy) {
		return convertValueToKMBT(value, true, divBy);
	}

	/**
	 * This method will convert the decimal value into custom format decimal value.
	 * 
	 * @param pattern
	 * @param value
	 * @return
	 */
	public static String customDecimalFormat(String pattern, double value) {
		DecimalFormat myFormatter = new DecimalFormat(pattern);
		String output = myFormatter.format(value);
		return output;
	}

	/**
	 * This method will extract currency symbol from value
	 * 
	 * @param value
	 * @return
	 */
	public static String extractCurrencySymbol(String value) {
		return (isStringNegative(value) ? value.substring(0, 2) : value.substring(0, 1));
	}

	/**
	 * This method will extract currency value from the string
	 * 
	 * @param str
	 * @return
	 */
	public static double extractCurrencyVal(String value) {
		return (isStringNegative(value) ? getDouble(value.substring(2, value.length()))
				: getDouble(value.substring(1, value.length())));
	}

	/**
	 * This method will format the number with commas.
	 * 
	 * @param value
	 * @return
	 */
	public static String formatNumber(String value) {
		if (value.contains(".")) {
			String temp1 = value.substring(value.indexOf("."), value.length());
			String temp2 = StringOperations.insertCommas(value.substring(0, value.indexOf(".")));
			return temp2 + temp1;
		} else {
			return (StringOperations.insertCommas(value));
		}
	}

	/**
	 * Gets Home Path
	 */
	public static String getBatHomePath() {
		return System.getProperty("user.dir");
	}

	/**
	 * Get double value
	 * 
	 * @param value
	 * @return
	 */
	public static double getDouble(String value) {
		return (Double.valueOf(value.trim()).doubleValue());
	}

	/**
	 * Gets environment variable values.
	 * 
	 * @param: environmentVariable
	 */
	public static String getEnvirnomentValue(String environmentVariable) {
		return (System.getenv(environmentVariable));
	}

	/**
	 * This method will return the export directory path.
	 * 
	 * @return
	 */
	public static String getExportDir() {
		return String.format("%s/exports/", getBatHomePath());
	}

	/**
	 * This method will return the formated date string based on the given format.
	 * 
	 * @param format (Ex: "dd-MM-yyyy-HH-mm-ss")
	 * @return formated String
	 */
	public static String getFormatedDate(String format) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	/**
	 * This method will return the String format date from the given Date.
	 * 
	 * @param format -> Format. Example: dd-MM-yy
	 * @param date   -> java.util.Date
	 * @return -> Formated String.
	 */
	public static String getFormatedDate(String format, Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	/**
	 * This method will the current TimeStamp.
	 * 
	 * @return -> TimeStamp.
	 */
	public static String getTimeStamp() {
		java.util.Date date = new java.util.Date();
		return new Timestamp(date.getTime()).toString();
	}

	/**
	 * This method will convert a string value to integer.
	 * 
	 * @param value
	 * @return
	 */
	public static int getInteger(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.getLocalizedMessage());
			return -1;
		}
	}

	/**
	 * This method will convert the integer to string.
	 * 
	 * @param value
	 * @return
	 */
	public static String getIntegerString(int value) {
		return Integer.toString(value);
	}

	/**
	 * This method will return the Screen shot folder path.
	 * 
	 * @return
	 */
	public static String getScrnshotFolder() {
		return "";
	}

	/**
	 * This method will return the temporary file path.
	 * 
	 * @return
	 */
	public static String getTempFilePath() {
		return String.format("%s/DownloadedFiles/", getBatHomePath());
	}

	/**
	 * This method will return all the test class names to execute.
	 * 
	 * @return String[] - arrays of classes to execute the scripts.
	 * @throws IOException
	 */
	public static String[] getTestClassesToRun(String fileName) throws IOException {
		List<String> testClassNames = new ArrayList<String>();

		Properties testClasses = FileOperations.getProperties(String.format("%s.properties", fileName));

		int skippedCount = 0;

		if (testClasses != null) {
			// If "All" key contains then check yes or no. If yes then run all
			// test cases irrespective of individual test case status.
			if (testClasses.containsKey("All")) {
				if (testClasses.get("All").toString().trim().toLowerCase().equals("yes")) {
					testClasses.remove("All");
					return sortTestCases(testClasses.keySet().toArray(new String[testClasses.keySet().size()]));
				} else {
					testClasses.remove("All");
				}
			}

			for (Object key : testClasses.keySet()) {
				if (testClasses.get(key).toString().trim().toLowerCase().equals("yes")) {
					testClassNames.add(key.toString());
				} else {
					if (skippedCount == 0) {
						Log.writeMessage(CLASS_NAME,
								"------------------------------------------------------------------------------------------");
						Log.writeMessage(CLASS_NAME, "Skipped Classes:");
					}
					skippedCount++;
					String testCase = String.format("com%s",
							key.toString().substring(key.toString().indexOf('.'), key.toString().length()));
					Log.writeMessage(CLASS_NAME, String.format("%s. %s", String.valueOf(skippedCount), testCase));
				}
			}
		}

		if (skippedCount > 0)
			Log.writeMessage(CLASS_NAME,
					"------------------------------------------------------------------------------------------");

		return sortTestCases(testClassNames.toArray(new String[testClassNames.size()]));
	}

	/**
	 * verifies object is integer type or not.
	 */
	public static boolean isObjectInteger(Object object) {
		return object instanceof Integer;
	}

	/**
	 * Convert the MAP to JSON format string. This JSON format string will be used
	 * to POST API with out query string
	 */
	public static String mapToJSONString(Map<String, Object> map) {
		Gson gson = new Gson();
		return gson.toJson(map);
	}

	/**
	 * Convert the java.lang.Object to JSON format string. This JSON format string
	 * will be used to POST API with out query string
	 */
	public static String objectToJSONString(Object object) {
		Gson gson = new Gson();
		return gson.toJson(object);
	}

	/**
	 * Helper method which will convert Map<String, String> to
	 * MultiValuedMap<String, String). In this project we used this method in
	 * RestApiServiceWithJAXRS.java under com.marketshare.api package.
	 */
	public static MultivaluedMap<String, String> mapToMultivaluedMap(Map<String, Object> map) {
		MultivaluedMap<String, String> multivaluedMap = new MultivaluedMapImpl();
		if (map != null) {
			for (Entry<String, Object> entry : map.entrySet()) {
				multivaluedMap.add(entry.getKey(), entry.getValue().toString());
			}
		}
		return multivaluedMap;
	}

	/**
	 * This method will round to one decimal value.
	 * 
	 * @param d
	 * @return
	 */
	public static Double roundToOneDecimal(Double value) {
		return ((double) Math.round(value * 10) / 10);
	}

	/**
	 * This method will round to nearest value
	 * 
	 * @param number
	 * @param nearest
	 * @return
	 */
	public static double roundToNearest(double number, double nearest) {
		return ((double) Math.round((number / nearest) * 100) / 100);
	}

	/**
	 * This method will return to nearest value and divides by the value given
	 * instead of 100.
	 * 
	 * @param number
	 * @param nearest
	 * @param divBy
	 * @return
	 */
	public static double roundToNearest(double number, double nearest, int divBy) {
		return ((double) Math.round((number / nearest) * divBy) / divBy);
	}

	/**
	 * This method causes the current thread to suspend execution for a specified
	 * period.
	 * 
	 * @param timeToWait
	 */
	public static void wait(int timeToWait) {
		try {
			Thread.sleep(timeToWait);
		} catch (InterruptedException e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.getLocalizedMessage());
		}
	}

	/**
	 * This method will convert the yens.
	 * 
	 * @param value
	 * @return
	 */
	public static String yenBasedConvertion(String value) {
		String curSym = extractCurrencySymbol(value);
		value = StringOperations.replaceCommas(value);
		try {
			long doubleValue = (long) extractCurrencyVal(value);
			long roundValue = Math.round(doubleValue / 1000.0);
			return (curSym + formatNumber(Long.toString(roundValue)));
		} catch (NumberFormatException nfe) {
			return ("none");
		}
	}

	/**
	 * This method will convert the yens.
	 * 
	 * @param value
	 * @return
	 */
	public static String joinArray(String[] stringArray, String delimiter) {
		String joinedArray = "";
		Boolean first = true;
		for (int i = 0; i < stringArray.length; i++) {
			if (first) {
				first = false;
			} else {
				joinedArray += delimiter;
			}
			joinedArray += stringArray[i];
		}
		return joinedArray;
	}

	/**
	 * This Method checks whether the elements are sorted or not in an string array
	 */
	public boolean sortArrayInAlphabeticalOrder(String[] values) {
		List<String> wordList = Arrays.asList(values);
		String[] actualSortedList = wordList.toArray(new String[wordList.size()]);
		Arrays.sort(values);
		return CommonUtil.compareTwoStringArrays(values, actualSortedList);
	}

	/***
	 * Method to get First Day of every month
	 * 
	 * @param year  -> Year
	 * @param month -> Month EX: Calendar.JANURARY
	 * @param week  -> Week Calendar.SUNDAY
	 * @return -> Date
	 */
	public static String getDateOfFirstWeekOfMonth(int year, int month, int week) {
		String dateOfFirstWeekOfMonth = null;
		DateFormat dateFormat = new SimpleDateFormat("MMM d yyyy");
		// SimpleDateFormat mdyFormat = new SimpleDateFormat("MMM d yyyy");

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.DAY_OF_WEEK, week);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.WEEK_OF_MONTH, 1);
		if (cal.get(Calendar.DAY_OF_MONTH) > 7) {
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.WEEK_OF_MONTH, 2);
			cal.set(Calendar.DAY_OF_WEEK, week);
			dateOfFirstWeekOfMonth = dateFormat.format(cal.getTime());
		} else {
			dateOfFirstWeekOfMonth = dateFormat.format(cal.getTime());
		}
		return dateOfFirstWeekOfMonth;
	}

	/**
	 * Method to get random integer number in range
	 */
	public static int getRandomInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	/**
	 * Method to get QA Final Configuration keys and respective values.
	 * 
	 * @param qaAndDevConfigKeys -> QA and DEV configuration mapping keys
	 * @return -> Final DEV and QA Key values
	 */
	public static Map<String, String> mapConfigKeyValues(String fileName, Map<String, String> devQAConfigKeys) {
		Map<String, String> finalDevQAConfigSet = new HashMap<String, String>();
		try {
			Properties properties = FileOperations.getProperties(fileName);
			Set<String> devKeys = devQAConfigKeys.keySet();
			for (String devKey : devKeys) {
				finalDevQAConfigSet.put(devQAConfigKeys.get(devKey), properties.getProperty(devKey));
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, "Failed while mapping DEV and QA configuration keys. " + e.getMessage());
			return null;
		}
		return (finalDevQAConfigSet.keySet().size() > 0) ? finalDevQAConfigSet : null;
	}

	/**
	 * Method to get random integer number in the given range
	 * 
	 * @param minimum -> Minimum Number
	 * @param maximum -> Maximum Number
	 * @return -> Random Integer Number
	 */
	public static int getRandomInteger(int minimum, int maximum) {
		int randomNum = minimum + (int) (Math.random() * maximum);
		return randomNum;
	}

	/**
	 * Method to build Error Message From Exception
	 * 
	 * @param e -> Exception
	 * @return -> Exception Message
	 */
	public static String buildErrorMessageFromException(Exception e) {
		try {
			StringBuilder exceptionMessage = new StringBuilder();
			exceptionMessage.append(String.format("Exception Message: %s\n", e.getMessage()));

			String exceptionType = e.getClass().getSimpleName();
			StackTraceElement[] eles = e.getStackTrace();
			String fullString = eles[0].toString();
			String methodName = fullString.substring(0, fullString.indexOf("("));
			String finalMethodName = methodName.substring(methodName.lastIndexOf(".") + 1);
			String className = fullString.substring(fullString.indexOf("(")).replace("(", "").replace(")", "").trim();
			String finalClassName = className.substring(0, className.indexOf("."));
			String finalLineNo = className.substring(className.indexOf(":") + 1);
			exceptionMessage.append(
					String.format("%sException Information: %s Location: ClassName:%s, MethodName:%s, LineNumber:%s",
							exceptionType.isEmpty() ? "Exception" : exceptionType, finalClassName, finalMethodName,
							finalLineNo));
			return exceptionMessage.toString();
		} catch (Exception ee) {
			return e.toString();
		}
	}

	// ---------------------------------------------------------------------------------
	// Private methods

	/**
	 * This method will convert the value into KMBT format.
	 * 
	 * @param value
	 * 
	 * @return
	 */
	private static String convertValueToKMBT(String value, boolean isDivBy, int divBy) {
		String currentSymbol = extractCurrencySymbol(value);
		value = StringOperations.replaceCommas(value);
		double currencyVal = extractCurrencyVal(value);

		if (Math.abs(currencyVal) >= 1000000000) {
			return (currentSymbol + roundValueKMBT(currencyVal, 1000000000, isDivBy, divBy) + "b");
		} else if (Math.abs(currencyVal) >= 1000000) {
			return (currentSymbol + roundValueKMBT(currencyVal, 1000000, isDivBy, divBy) + "m");
		} else if (Math.abs(currencyVal) >= 1000) {
			return (currentSymbol + roundValueKMBT(currencyVal, 1000, isDivBy, divBy) + "k");
		} else {
			return currentSymbol + formatNumber(convertDoubleToString(roundToOneDecimal(currencyVal)));
		}
	}

	/**
	 * 
	 * @param value
	 * 
	 * @param kmbt
	 * 
	 * @return
	 */
	private static String roundValueKMBT(double value, double kmbt, boolean isDivBy, int divBy) {
		double tempValue = (isDivBy == true) ? roundToNearest(value, kmbt, divBy) : roundToNearest(value, kmbt);
		return formatNumber(convertDoubleToString(roundToOneDecimal(tempValue)));
	}

	/**
	 * This method verifies value is negative or not.
	 * 
	 * @param value
	 * 
	 * @return
	 */
	private static boolean isStringNegative(String value) {
		return value.startsWith("-");
	}

	/**
	 * This method verifies value is negative or not.
	 * 
	 * @param value
	 * 
	 * @return
	 */
	public static boolean isStringFirstCharInteger(String value) {
		char c = value.charAt(0);
		boolean isDigit = (c >= '0' && c <= '9');
		return isDigit;
	}

	/**
	 * Get Java Version
	 */
	public static String getJavaVersion() {
		return System.getProperty("java.version");
	}

	/**
	 * Method to sort the test cases based on priority.
	 * 
	 * @param temp -> String[] - TestCases
	 * @return -> String[] - Sorted TestCases
	 */
	private static String[] sortTestCases(String[] temp) {
		Arrays.sort(temp);
		String[] testcases = new String[temp.length];
		int i = 0;
		for (String testCase : temp) {
			String finalTestCaseName = String.format("com%s",
					testCase.substring(testCase.indexOf('.'), testCase.length()));
			testcases[i] = finalTestCaseName;
			i++;
		}
		return testcases;
	}

	/**
	 * Method to get customized error code for a given errorMessage
	 * 
	 * @param errorMessage -> "Null Pointer Exception"
	 * @return String -> Code502-AUTO_UNHANDLED_EXCEPTION-
	 */
	public static String extractErrorCodeFromMessage(String errorMessage) {
		return errorMessage;
	}

	/**
	 * Method to match pattern in a string
	 * 
	 * @param patterns
	 * @param match
	 * @return boolean
	 */
	public static boolean isMatchExistInPattern(List<String> patterns, String match) {
		boolean flag = false;
		for (String pattern : patterns) {
			Pattern patternValues = Pattern.compile(pattern);
			Matcher matcher = patternValues.matcher(match);
			flag = matcher.find();
			if (flag) {
				break;
			}
		}

		return flag;
	}

	/**
	 * Method to append errorCode to Error message
	 * 
	 * @param e -> Exception
	 * @return -> Error message with error code
	 */
	public static String appendErrorCodeToMessage(Exception e) {
		return appendErrorCodeToMessage(buildErrorMessageFromException(e));
	}

	/**
	 * Method to append errorCode to Error message
	 * 
	 * @param errorMessage
	 * @return String
	 */

	public static String appendErrorCodeToMessage(String errorMessage) {
		return String.format("%s%s", CommonUtil.extractErrorCodeFromMessage(errorMessage), errorMessage);
	}

	/**
	 * Method to get automation code availability in a errorMessage
	 * 
	 * @param errorMessage
	 * @return boolean - true/false
	 */

	public static boolean isAutomationCodeExistInErrorCode(String errorMessage) {
		List<String> values = Arrays.asList("Code500", "Code501", "Code502", "Code400", "Code401", "Code404", "Code100",
				"Code101", "Code102", "Code103", "Code104", "Code105", "Code106", "Code107", "Code108", "Code109");
		return values.contains(errorMessage) ? true : false;

	}

	/**
	 * Method to execute CLI Command.
	 * 
	 * @param command -> CLI Command as List of String. <br />
	 *                EX: Arrays.asList("cmd", "/c", "java", "-jar", new
	 *                File(System.getProperty("user.dir").replace("target", "")) +
	 *                CommonConstants.DEFAULT_RESOURCE_DIR +
	 *                CommonConstants.SELENIUMJAR, "-role", "hub", "-port",
	 *                gridPort))
	 * @return -> True/False
	 */
	public static boolean executeCLICommand(List<String> command) {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			Process process = processBuilder.start();

			if (process.isAlive()) {
				Log.writeMessage(LogLevel.INFO, String.format("Process %s started...", command.toString()));
				return true;
			}
		} catch (IOException io) {
			Log.writeMessage(LogLevel.ERROR, buildErrorMessageFromException(io));
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, buildErrorMessageFromException(e));
		}
		return false;
	}

	/**
	 * Constant variable which holds class name
	 */
	private final static String CLASS_NAME = CommonUtil.class.getName();
}
