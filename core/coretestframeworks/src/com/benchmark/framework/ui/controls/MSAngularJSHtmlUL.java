

package com.benchmark.framework.ui.controls;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;

import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;

/**
 * MSHtmlUL - Control for UL
 */
public class MSAngularJSHtmlUL extends BaseControl {

	/**
	 * Constructor
	 */
	public MSAngularJSHtmlUL(String findULByType, String findULByValue,
			String findLabelByType, String findLabelByValue,
			String findSelectByType, String findSelectByValue) {
		super(findULByType, findULByValue);
		m_findLabelByType = findLabelByType;
		m_findLabelByValue = findLabelByValue;
		m_findSelectByType = findSelectByType;
		m_findSelectByValue = findSelectByValue;
	}

	/**
	 * Get ALL LI Values
	 * 
	 * @return -> List Of LI Values
	 */
	public List<String> getValues() {
		try {
			List<String> values = new ArrayList<String>();
			List<WebElement> lis = webElement().findElements(
					createBy(FindByTypeConstants.TAG_NAME, "li"));
			for (WebElement li : lis) {
				values.add(li.getAttribute("innerText")
						.replace("check_box_outline_blank", "")
						.replace("check_box", "").trim());
			}
			return values;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, "", e.toString());
		}
		return null;
	}

	/**
	 * Get Selected Values
	 * 
	 * @param selected
	 *            -> Selected Text
	 * @return -> Selected Values
	 */
	public List<String> getSelectedValues(String selected) {
		try {
			List<String> values = new ArrayList<String>();
			List<WebElement> lis = webElement().findElements(
					createBy(FindByTypeConstants.TAG_NAME, "li"));
			for (WebElement li : lis) {
				WebElement label = li.findElement(createBy(m_findLabelByType,
						m_findLabelByValue));
				WebElement i = label.findElement(createBy(m_findSelectByType,
						m_findSelectByValue));
				if (i.getAttribute("innerText").equals(selected)) {
					values.add(li.getAttribute("innerText")
							.replace("check_box_outline_blank", "")
							.replace("check_box", "").trim());
				}
			}
			return values;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, "", e.toString());
		}
		return null;
	}

	/**
	 * Method to select to values in list
	 * 
	 * @param values
	 *            -> Values to select in list
	 * @return -> True/False
	 */
	public boolean select(String value) {
		List<WebElement> lis = webElement().findElements(
				createBy(FindByTypeConstants.TAG_NAME, "li"));
		for (WebElement li : lis) {
			if (li.getAttribute("innerText").contains(value)) {
				WebElement label = li.findElement(createBy(m_findLabelByType,
						m_findLabelByValue));
				WebElement i = label.findElement(createBy(m_findSelectByType,
						m_findSelectByValue));
				i.click();
				return true;
			}
		}
		return false;
	}

	private String m_findLabelByType = null;
	private String m_findLabelByValue = null;
	private String m_findSelectByType = null;
	private String m_findSelectByValue = null;
}
