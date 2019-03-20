package com.benchmark.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;

public class DateTimeFunctions {

	/**
	 * Verifies two dates are equal or not.
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean areTwoDateEqual(Date date1, Date date2) {
		return (compareDates(date1, date2) == 0 ? true : false);
	}

	/**
	 * This method will compare the two dates.
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareDates(Date date1, Date date2) {
		return date1.compareTo(date2);
	}

	/**
	 * This method will pull the day from the week,.
	 * 
	 * @return
	 */
	public static String findDayOfWeek() {
		switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
		case 1:
			return "Sunday";
		case 2:
			return "Monday";
		case 3:
			return "Tuesday";
		case 4:
			return "Wednesday";
		case 5:
			return "Thursday";
		case 6:
			return "Friday";
		case 7:
			return "Saturday";
		}
		return "";
	}

	/**
	 * This method will get the current Time with "dd-MM-yyyy-HH-mm-ss" format.
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		return CommonUtil.getFormatedDate("dd-MM-yyyy-HH-mm-ss");
	}

	/**
	 * This method will get the current Time with the specified format.
	 * 
	 * @return
	 */
	public static String getCurrentTime(String format) {
		return CommonUtil.getFormatedDate(format);
	}

	/**
	 * Verifies date1 is greater than date2
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isDate1GreaterThanDate2(Date date1, Date date2) {
		return (compareDates(date1, date2) > 0 ? true : false);
	}

	/**
	 * Verifies date1 is less than date2
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isDate1LessThanDate2(Date date1, Date date2) {
		return (compareDates(date1, date2) < 0 ? true : false);
	}

	/**
	 * This method will parse the string value to Date.
	 * 
	 * @param strDate
	 * @param strDateFormat
	 * @return
	 */
	public static Date parseStringToDate(String strDate, String strDateFormat) {
		SimpleDateFormat format = new SimpleDateFormat(strDateFormat);
		try {
			return format.parse(strDate);
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, DateTimeFunctions.class.getName(),
					"Either string or Date format input is wrong. Date - "
							+ strDate + ".Date format string - "
							+ strDateFormat);
		}
		return new Date();
	}
}
