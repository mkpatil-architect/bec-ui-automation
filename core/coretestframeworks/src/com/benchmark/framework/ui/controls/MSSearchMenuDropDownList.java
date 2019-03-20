

package com.benchmark.framework.ui.controls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebElement;

import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;
import com.benchmark.framework.ui.SeleniumWrapper;

public class MSSearchMenuDropDownList extends BaseControl {

	/**
	 * Constructor
	 */
	public MSSearchMenuDropDownList(String parentFindByType,
			String parentValueToFind, String searchTextBoxFindByType,
			String searchTextBoxValueToFind, String menuListToFindByType,
			String menuListValueToFind) {
		super(parentFindByType, parentValueToFind);
		m_searchTextBoxFindByType = searchTextBoxFindByType;
		m_searchTextBoxValueToFind = searchTextBoxValueToFind;
		m_menuListToFindByType = menuListToFindByType;
		m_menuListValueToFind = menuListValueToFind;
	}

	/**
	 * Method to search the text to find the value
	 * 
	 * @param searchText
	 *            -> Search Text
	 * @return -> True/False
	 */
	public boolean enterTextToSearch(String searchText) {
		try {
			WebElement searchTextBox = webElement().findElement(
					createBy(m_searchTextBoxFindByType,
							m_searchTextBoxValueToFind));
			searchTextBox.sendKeys(searchText);
			return true;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME,
					"SearchTextBox element not found in parent SearchMenuDropDownList.");
		}
		return false;
	}

	/**
	 * Method to get list of values from the DropDownList
	 * 
	 * @return -> List[Values]
	 */
	public List<String> getList() {
		try {
			String[] values = webElement().getText().split("\n");
			List<String> finalValues = new ArrayList<String>();
			for (String value : values) {
				if (value.trim().length() > 0) {
					if (!finalValues.contains(value.trim())) {
						finalValues.add(value.trim());
					}
				}
			}
			return finalValues;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME,
					"List elements are not found in parent SearchMenuDropDownList");
		}
		return null;
	}

	/**
	 * Method to select a random value from list
	 * 
	 * @return -> Select Value
	 */
	public String selectARandomValueFromList() {
		try {
			webElement().click();
			Thread.sleep(1000);
			WebElement ul = SeleniumWrapper.webDriver().findElement(
					createBy(m_menuListToFindByType, m_menuListValueToFind));
			List<WebElement> lis = ul.findElements(createBy(
					FindByTypeConstants.TAG_NAME, "li"));
			if (lis.size() > 0) {
				Collections.shuffle(lis);
				WebElement li = lis.get(0);
				li.click();
				return webElement().getText().split("\n")[0].trim();
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME,
					"List element is not found in parent SearchMenuDropDownList");
		}
		return null;
	}

	private static String m_searchTextBoxFindByType = null;
	private static String m_searchTextBoxValueToFind = null;
	private static String m_menuListToFindByType = null;
	private static String m_menuListValueToFind = null;
	private final static String CLASS_NAME = MSSearchMenuDropDownList.class
			.getName();

}
