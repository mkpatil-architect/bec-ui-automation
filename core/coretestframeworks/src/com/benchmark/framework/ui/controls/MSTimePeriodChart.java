package com.benchmark.framework.ui.controls;

import java.util.Calendar;

import com.benchmark.core.logger.Log;

public class MSTimePeriodChart extends BaseControl {

	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSTimePeriodChart(String xpath) {
		super(xpath);
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSTimePeriodChart(String findByType, String valueToFindElement) {
		super(findByType, valueToFindElement);
	}

	/**
	 * Change time period
	 */
	public void changeTimePeriod(String fromTimeStamp, String toTimeStamp) {
		// fromTimeStamp = "2013, 03, 05";
		// toTimeStamp = "2013, 09, 20";
		changeTimeStamp(fromTimeStamp, toTimeStamp);

	}

	/**
	 * Method to change TimeStamp in chart.
	 */
	private void changeTimeStamp(String fromTimeStamp, String toTimeStamp) {
		StringBuilder script = new StringBuilder();
		script.append("var timePeriodObj = timePeriodChartObj.axes[0];");
		script.append(String.format(
				" return timePeriodObj.setExtremes(Date.UTC(%s),Date.UTC(%s))",
				getUTCDateFromTimeStamp(fromTimeStamp),
				getUTCDateFromTimeStamp(toTimeStamp)));
		getObject(script.toString());
	}

	/**
	 * Method to convert TimeStamp to date
	 */
	private String getUTCDateFromTimeStamp(String value) {
		long timestamp = Long.parseLong(value);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		String timeStamp = String.format("%d, %d, %s",
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		Log.writeMessage(MSTimePeriodChart.class.getName(), timeStamp);
		return timeStamp;
	}
}
