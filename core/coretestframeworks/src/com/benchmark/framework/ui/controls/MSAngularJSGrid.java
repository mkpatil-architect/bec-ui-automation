

package com.benchmark.framework.ui.controls;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;

import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;
import com.benchmark.framework.ui.SeleniumWrapper;

/**
 * MSAngularJSGrid control used across all products.
 */
public class MSAngularJSGrid extends BaseControl {

	/**
	 * Constructor
	 */
	public MSAngularJSGrid(String findHeaderType, String findHeaderValue,
			String findContentType, String findContentValue,
			String findHeaderColType, String findHeaderColValue) {
		m_headerType = findHeaderType;
		m_findHeaderValue = findHeaderValue;
		m_findContentType = findContentType;
		m_findContentValue = findContentValue;
		m_findHeaderColType = findHeaderColType;
		m_findHeaderColValue = findHeaderColValue;
	}

	// --------------------------------------------------------------------------------------------------------------------
	// Public Methods

	/**
	 * Method to get grid column names
	 */
	public List<String> getColumns() {
		try {
			List<String> values = new ArrayList<String>();
			List<WebElement> columns = header().getChildElements(
					createBy(m_findHeaderColType, m_findHeaderColValue));
			for (WebElement column : columns) {
				values.add(column.getAttribute("innerText"));
			}
			return values;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.toString());
		}
		return null;
	}

	/**
	 * Method to get the single column values based on index
	 * 
	 * @param index
	 *            -> Column Index
	 * @return -> List[String]
	 */
	public List<String> getSingleColumnValues(int index) {
		try {
			List<String> values = new ArrayList<String>();
			List<WebElement> elements = getChildElementsFromContent();
			for (WebElement element : elements) {
				List<WebElement> childElements = element.findElements(createBy(
						FindByTypeConstants.TAG_NAME, "div"));
				values.add(childElements.get(index).getAttribute("innerText")
						.trim());
			}
			return values;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.toString());
		}
		return null;
	}

	/**
	 * Method to get Rows Count
	 * 
	 * @return -> True/False
	 */
	public int getRowCount() {
		return getChildElementsFromContent().size();
	}

	/**
	 * Method to click item in pop-up holder
	 * 
	 * @return True/False
	 */
	public boolean clickItemInPopupHolder(String action, int rowIndex) {
		try {
			List<WebElement> elements = getChildElementsFromContent();
			WebElement element = elements.get(rowIndex);
			element.click();
			mouseOver(element);

			WebElement contextButton = element.findElement(createBy(
					m_contextButtonFindByType, m_contextButtonFindByValue));
			mouseOver(contextButton);
			contextButton.click();
			SeleniumWrapper.sleep(4000);

			clickChildElementInParentList(element, m_contextMenuFindByType,
					m_contextMenuFindByValue, FindByTypeConstants.TAG_NAME,
					"li", action.toString());

			SeleniumWrapper.sleep(1000);
			return true;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.toString());
		}
		return false;
	}

	/**
	 * Method to click on particular column of row based on index.
	 * 
	 * @param rowIndex
	 *            -> Row Index
	 * @param colIndex
	 *            -> Column Index
	 * @return -> True/False
	 */
	public boolean clickColumnInRow(int rowIndex, int colIndex) {
		try {
			List<WebElement> rows = getChildElementsFromContent();
			WebElement row = rows.get(rowIndex);
			List<WebElement> columns = row.findElements(createBy(
					FindByTypeConstants.TAG_NAME, "div"));
			WebElement column = columns.get(colIndex);
			WebElement element = column.findElement(createBy(
					FindByTypeConstants.TAG_NAME, "a"));
			element.click();
			return true;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.toString());
		}
		return false;
	}

	/**
	 * Method to get message from grid
	 * 
	 * @return -> Message
	 */
	public String getMessage() {
		return content().getElement(FindByTypeConstants.TAG_NAME, "p")
				.getAttributeValue("innerText");
	}

	// --------------------------------------------------------------------------------------------------------------------
	// Private Methods

	/**
	 * Gets Header Control
	 */
	private MSGenericElement header() {
		return new MSGenericElement(m_headerType, m_findHeaderValue);
	}

	/**
	 * Gets Content Control
	 */
	private MSGenericElement content() {
		return new MSGenericElement(m_findContentType, m_findContentValue);
	}

	/**
	 * Gets Child Elements from Content
	 */
	private List<WebElement> getChildElementsFromContent() {
		return content().getChildElements(
				createBy(FindByTypeConstants.CSS_SELECTOR,
						"div[class = 'fading ng-scope']"));
	}

	/**
	 * Method to click child element in parent control
	 * 
	 * @param parentFindByType
	 * @param parentFindByValue
	 * @param subchildFindByType
	 * @param subchildFindByValue
	 * @param text
	 * @return
	 */
	public boolean clickChildElementInParentList(WebElement rowElement,
			String parentFindByType, String parentFindByValue,
			String subchildFindByType, String subchildFindByValue, String text) {
		try {
			WebElement popupHolder = rowElement.findElement(createBy(
					FindByTypeConstants.CSS_SELECTOR,
					"div[class^='popHolder ']"));
			WebElement parentElement = popupHolder.findElement(createBy(
					parentFindByType, parentFindByValue));
			List<WebElement> childElements = parentElement
					.findElements(createBy(subchildFindByType,
							subchildFindByValue));
			for (WebElement childElement : childElements) {
				if (childElement.getAttribute("innerText").trim()
						.contains(text)) {
					SeleniumWrapper.scrollIntoView(childElement);
					childElement.click();
					return true;
				}
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.toString());
		}
		return false;
	}

	// --------------------------------------------------------------------------------------------------------------------
	// Private Variables

	private String m_headerType = null;
	private String m_findHeaderValue = null;
	private String m_findContentType = null;
	private String m_findContentValue = null;
	private String m_findHeaderColType = null;
	private String m_findHeaderColValue = null;
	private String m_contextButtonFindByType = FindByTypeConstants.CSS_SELECTOR;
	private String m_contextButtonFindByValue = "button[class = 'menuBtn fa fa-ellipsis-v dropdown-toggle']";
	private String m_contextMenuFindByType = FindByTypeConstants.CSS_SELECTOR;
	private String m_contextMenuFindByValue = "ul[class = 'dropdown-menu'][role = 'menu']";

	private final static String CLASS_NAME = MSAngularJSGrid.class
			.getSimpleName();
}
