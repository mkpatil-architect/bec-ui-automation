

package com.benchmark.framework.ui.controls;

import java.util.List;

import org.openqa.selenium.WebElement;

import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.core.logger.Log;

/**
 * Class for ListView Control
 */
public class MSListView extends BaseControl {

	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSListView(String xpath) {
		super(xpath);
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSListView(String findByType, String valueToFindElement) {
		super(findByType, valueToFindElement);
	}

	/**
	 * Gets required value from the control
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @return
	 */
	public String getItemValue(String findByType, String valueToFindElement,
			String childFindByType, String childValueToFindElement) {
		return getWebElement(findByType, valueToFindElement, childFindByType,
				childValueToFindElement).getText();
	}

	/**
	 * Gets required value from the control based on attribute
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @param attribute
	 * @return
	 */
	public String getItemValue(String findByType, String valueToFindElement,
			String childFindByType, String childValueToFindElement,
			String attribute) {
		return getWebElement(findByType, valueToFindElement, childFindByType,
				childValueToFindElement).getAttribute(attribute);
	}

	/**
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @return
	 */
	public String getItemValue(String findByType, String valueToFindElement) {
		return getChildElement(webElement(),
				createBy(findByType, valueToFindElement)).getText();
	}

	/**
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @return
	 */
	public String getItemValue(String findByType, String valueToFindElement,
			String attribute) {
		return getChildElement(webElement(),
				createBy(findByType, valueToFindElement)).getAttribute(
				attribute);
	}

	/**
	 * Selects the particular row
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public void selectRow(String findByType, String valueToFindElement) {
		getChildElement(webElement(), createBy(findByType, valueToFindElement))
				.click();
	}

	/**
	 * Verifies element is visible or not
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @return
	 */
	public boolean isItemVisible(String findByType, String valueToFindElement) {
		try {
			WebElement element = getChildElement(webElement(),
					createBy(findByType, valueToFindElement));
			return element.isDisplayed();
		} catch (Exception e) {
			Log.writeMessage(MSListView.class.getName(),
					e.getLocalizedMessage());
		}
		return false;
	}

	/**
	 * Method to get Web Element
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @param rowNumber
	 * @return
	 */
	public WebElement getWebElement(String findByType,
			String valueToFindElement, String childFindByType,
			String childValueToFindElement) {
		return getChildElement(
				getChildElement(webElement(),
						createBy(findByType, valueToFindElement)),
				createBy(childFindByType, childValueToFindElement));
	}

	/**
	 * Method to get First row from the list. This method will return the
	 * MSHtmlDiv element if parent has all child elements as div.
	 * 
	 * @return
	 */
	public MSHtmlDiv getRowFromList(int rowIndex) {
		List<WebElement> rows = webElement().findElements(
				createBy(FindByTypeConstants.TAG_NAME, "div"));
		if (rows.size() > 0) {
			System.out.println(rows.get(rowIndex).getAttribute("id"));
			return new MSHtmlDiv(webElement(), FindByTypeConstants.ID, rows
					.get(rowIndex).getAttribute("id"));
		}
		return null;
	}

	public boolean clickOnRowFromList(int rowIndex) {
		List<WebElement> rows = webElement().findElements(
				createBy(FindByTypeConstants.TAG_NAME, "div"));
		if (rows.size() > 0) {
			WebElement row = webElement().findElement(
					createBy(FindByTypeConstants.ID, rows.get(rowIndex)
							.getAttribute("id")));
			row.findElement(createBy(FindByTypeConstants.TAG_NAME, "a"))
					.click();
			return true;
		}
		return false;
	}
}
