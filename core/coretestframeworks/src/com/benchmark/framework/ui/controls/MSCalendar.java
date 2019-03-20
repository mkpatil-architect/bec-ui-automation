
package com.benchmark.framework.ui.controls;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;

import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;
import com.benchmark.framework.ui.SeleniumWrapper;

/**
 * Class for Custom Calendar.
 */
public class MSCalendar extends BaseControl {

	/**
	 * Holds Calendar properties
	 */
	private Map<String, String> m_calendar = null;

	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSCalendar(String xpath, Map<String, String> calendar) {
		super(xpath);
		m_calendar = calendar;
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSCalendar(String findByType, String valueToFindElement,
			Map<String, String> calendar) {
		super(findByType, valueToFindElement);
		m_calendar = calendar;
	}

	/**
	 * Select Date
	 * 
	 * @param month
	 * @param date
	 * @param year
	 */
	public void selectDate(String month, String date, String year) {
		selectDate(month, date, year, 0);
	}

	/**
	 * Select date.
	 * 
	 * @param month
	 * @param date
	 * @param year
	 * @param waitAfterClick
	 */
	public boolean selectDate(String month, String date, String year,
			int waitAfterClick) {

		try {

			webElement().click();
			if (waitAfterClick > 0) {
				SeleniumWrapper.sleep(waitAfterClick);
			}
			WebElement calendar = getChildElement(createBy(
					m_calendar.get("calFindType"),
					m_calendar.get("calFindValue")));

			// System.out.println(calendar.getAttribute("id"));
			// System.out.println(calendar.getAttribute("class"));
			MSSelectOption calYear = new MSSelectOption(calendar,
					m_calendar.get("calYearFindType"),
					m_calendar.get("calYearFindValue"));
			calYear.selectOption(year);

			if (waitAfterClick > 0) {
				SeleniumWrapper.sleep(waitAfterClick);
			}

			MSSelectOption calMonth = new MSSelectOption(calendar,
					m_calendar.get("calMonthFindType"),
					m_calendar.get("calMonthFindValue"));
			calMonth.selectOption(month);

			if (waitAfterClick > 0) {
				SeleniumWrapper.sleep(waitAfterClick);
			}

			WebElement calDates = getChildElement(
					calendar,
					createBy(m_calendar.get("calDateFindType"),
							m_calendar.get("calDateFindValue")));

			List<WebElement> dates = getChildElements(calDates,
					createBy(FindByTypeConstants.TAG_NAME, "td"));

			for (WebElement dateElement : dates) {
				if (dateElement.getText().equals(date)) {
					WebElement dateLink = getChildElement(dateElement,
							createBy(FindByTypeConstants.LINK_TEXT, date));
					String className = dateLink.getAttribute("class");
					// System.out.println(className);
					if (className.equals(m_calendar.get("calDateLinkCSS"))) {
						System.out.println("Selected :" + className);
						dateLink.click();
						break;
					}
				}
			}
			return true;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR,
					"Failed to select date. " + e.getMessage());
		}
		return false;
	}

	/**
	 * Gets selected date.
	 */
	public String getText() {
		return webElement().getAttribute("value");
	}
}
