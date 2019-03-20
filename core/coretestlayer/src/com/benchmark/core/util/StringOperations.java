package com.benchmark.core.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;

public class StringOperations {

	/**
	 * This method will replaces the chars and, hash, digits, colons in the
	 * given text with empty string.
	 * 
	 * @param text
	 * @return
	 */
	public static String andHashDigitsColonText(String text) {
		return (text.replaceAll("&#[\\d]+;", ""));
	}

	/**
	 * This method will convert the String Array to Integer array
	 * 
	 * @param elements
	 * @return
	 */
	public static Integer[] convertStringArrayToIntegerArray(String[] elements) {
		List<Integer> integerList = new ArrayList<Integer>();
		for (String element : elements) {
			integerList.add(Integer.parseInt(element));
		}
		return integerList.toArray(new Integer[integerList.size()]);
	}

	/**
	 * Gets string array values
	 * 
	 * @param values
	 * @param count
	 * @return
	 */
	public static String[] getArrayValues(String[] values, int count) {
		List<String> arrayValuesList = new ArrayList<String>();
		for (int i = 0; i <= values.length - 1; i++) {
			arrayValuesList.add(values[i]);
			if (arrayValuesList.size() == count) {
				break;
			}
		}
		return arrayValuesList.toArray(new String[arrayValuesList.size()]);
	}

	/**
	 * This method will return the random value from the given string array
	 * 
	 * @param values
	 * @return
	 */
	public static String getRandomValue(String[] values) {
		return values[new Random().nextInt(values.length)];
	}

	/**
	 * This method will return the array of random values from the given string
	 * array for particular count value.
	 * 
	 * @param values
	 * @param count
	 * @return
	 */
	public static String[] getRandomValues(String[] values, int count) {
		List<String> randomValuesList = new ArrayList<String>();
		for (int i = 1; i <= values.length; i++) {
			randomValuesList.add(getRandomValue(values));
			if (randomValuesList.size() == count) {
				break;
			}
		}
		return randomValuesList.toArray(new String[randomValuesList.size()]);
	}

	/**
	 * This method will verify input text is available in the String array or
	 * not.
	 * 
	 * @param elements
	 * @param inputString
	 * @return
	 */
	public static boolean isPartialStringPresentStringArray(String[] elements,
			String inputString) {
		for (String element : elements) {
			if (element.contains(inputString)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method will verify string is null or not
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isStringNotNull(String value) {
		return (!value.isEmpty());
	}

	/**
	 * This method will concatenate all array values into a string variable.
	 * 
	 * @param arrayValues
	 * @param delimiter
	 * @return
	 */
	public static String joinArray(String[] arrayValues, String delimiter) {
		return concatenateArray(arrayValues, delimiter, false);
	}

	/**
	 * This method will concatenate all array values into a string variable. For
	 * each value in the array it will bind with single quote.
	 * 
	 * @param arrayValues
	 * @param delimiter
	 * @return
	 */
	public static String joinStringArray(String[] arrayValues, String delimiter) {
		return concatenateArray(arrayValues, delimiter, true);
	}

	/**
	 * This method will verifies regular expression will match with formatted
	 * string input.
	 * 
	 * @param input
	 * @param regex
	 * @return
	 */
	public static boolean matchFormattedStrings(String input, String regex) {
		regex = replaceArgsToMatchAll(regex);
		return isMatchRegexString(input, regex);
	}

	/**
	 * This method will verify regular expression match with given input.
	 * 
	 * @param input
	 * @param regex
	 * @return
	 */
	public static boolean matchRegString(String input, String regex) {
		return isMatchRegexString(input, regex);
	}

	/**
	 * This method will verify two strings are equal or not.
	 * 
	 * @param text1
	 * @param text2
	 * @return
	 */
	public static boolean matchStrings(String text1, String text2) {
		return (text1.equals(text2));
	}

	/**
	 * This method will remove the null value element from the String array and
	 * return the elements which are not null.
	 * 
	 * @param elements
	 * @return
	 */
	public static String[] removeNullFromArray(String[] elements) {
		List<String> arrayValuesList = new ArrayList<String>();
		for (String element : elements) {
			if (!element.equals("")) {
				arrayValuesList.add(element);
			}
		}
		return arrayValuesList.toArray(new String[arrayValuesList.size()]);
	}

	/**
	 * This method will replace the Application name.
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceAppName(String str) {
		String appName = null;
		try {
			appName = FileOperations.getPropertyValue("app.propeties",
					"app.name");
		} catch (IOException e) {
			Log.writeMessage(LogLevel.ERROR, StringOperations.class.getName(),
					e.getLocalizedMessage());
		}
		return (replaceAllArg(str, "%appName%", appName));
	}

	/**
	 * This method will replace the & in the given text.
	 * 
	 * @param text
	 * @return
	 */
	public static String replaceAllAmp(String text) {
		return (replaceAllArg(text, "&[\\w]+;", "&"));
	}

	/**
	 * This method will replace all regular expression text with replacement
	 * text.
	 * 
	 * @param text
	 * @param regex
	 * @param replacement
	 * @return
	 */
	public static String replaceAllArg(String text, String regex,
			String replacement) {
		return (text.replaceAll(regex, replacement));
	}

	/**
	 * This method will replace the target text with replacement text.
	 * 
	 * @param text
	 * @param target
	 * @param replacement
	 * @return
	 */
	public static String replaceArg(String text, String target,
			String replacement) {
		return (text.replace(target, replacement));
	}

	/**
	 * This method will replace the regular expression with replacement text.
	 * 
	 * @param text
	 * @param replacement
	 * @return
	 */
	public static String replaceArg(String text, String replacement) {
		return (text.replaceFirst("%[\\w\\d]+%", replacement));
	}

	/**
	 * This method will replace all the match text.
	 * 
	 * @param text
	 * @return
	 */
	public static String replaceArgsToMatchAll(String text) {
		return (text.replaceAll("%[\\w\\d]+%", ".*"));
	}

	/**
	 * This method will replace all quotes.
	 * 
	 * @param text
	 * @return
	 */
	public static String replaceAllQuote(String text) {
		return (replaceAllArg(text, "&[\\w]+;", "\""));
	}

	/**
	 * This method will replace the commas with empty in the given text.
	 * 
	 * @param text
	 * @return
	 */
	public static String replaceCommas(String text) {
		return (text.replaceAll(",", ""));
	}

	/**
	 * This method will insert commas.
	 * 
	 * @param str
	 * @return
	 */
	public static String insertCommas(String str) {
		String regex = "(\\d)(?=(\\d{3})+$)";
		return (str.replaceAll(regex, "$1,"));
	}

	/**
	 * This method will replace the first & in the given text.
	 * 
	 * @param text
	 * @return
	 */
	public static String replaceFirstAmp(String text) {
		return (replaceFirstArg(text, "&[\\w]+;", "&"));
	}

	/**
	 * This method will first argument with replacement text.
	 * 
	 * @param text
	 * @param regex
	 * @param replacement
	 * @return
	 */
	public static String replaceFirstArg(String text, String regex,
			String replacement) {
		return (text.replaceFirst(regex, replacement));
	}

	/**
	 * This method will replace the first quote.
	 * 
	 * @param text
	 * @return
	 */
	public static String replaceFirstQuote(String text) {
		return (replaceFirstArg(text, "&[\\w]+;", "\""));
	}

	/**
	 * This method will replace the last value.
	 * 
	 * @param text
	 * @param from
	 * @param to
	 * @return
	 */
	public static String replaceLast(String text, String from, String to) {
		int lastIndex = text.lastIndexOf(from);
		if (lastIndex < 0)
			return text;
		String tail = text.substring(lastIndex).replaceFirst(from, to);
		return text.substring(0, lastIndex) + tail;
	}

	/*
	 * Verifies Regular expression matches with the given input.
	 * 
	 * @param input
	 * 
	 * @param regex
	 * 
	 * @return
	 */
	private static boolean isMatchRegexString(String input, String regex) {
		return Pattern.compile(regex).matcher(input).matches();
	}

	/*
	 * This method will concatenate the array values with given delimiter
	 * 
	 * @param stringArray
	 * 
	 * @param delimiter
	 * 
	 * @param isString
	 * 
	 * @return
	 */
	private static String concatenateArray(String[] arrayValues,
			String delimiter, boolean isStringArray) {
		String joinedArray = "";
		Boolean first = true;
		for (int i = 0; i < arrayValues.length; i++) {
			if (first) {
				first = false;
			} else {
				joinedArray += delimiter;
			}
			String value = arrayValues[i];
			joinedArray += (isStringArray == true ? "'" + value + "'" : value);
		}
		return joinedArray;
	}
}
