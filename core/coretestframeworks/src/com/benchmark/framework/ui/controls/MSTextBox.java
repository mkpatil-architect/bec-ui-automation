

package com.benchmark.framework.ui.controls;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

/**
 * Class for TextBox control
 */
public class MSTextBox extends BaseControl {

	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSTextBox(String xpath) {
		super(xpath);
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSTextBox(String findByType, String valueToFindElement) {
		super(findByType, valueToFindElement);
	}

	/**
	 * Constructor to find element
	 * 
	 * @param element
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSTextBox(WebElement element, String findByType,
			String valueToFindElement) {
		super(element, findByType, valueToFindElement);
	}

	/**
	 * Method to enter keys.
	 * 
	 * @param keysToSend
	 */
	public void type(String keysToSend) {
		webElement().sendKeys(keysToSend);
	}

	/**
	 * Method to enter text and click enter
	 * 
	 * @param keysToSend
	 *            -> Text to enter.
	 */
	public void typeAndEnter(String keysToSend) {
		webElement().sendKeys(keysToSend);
		webElement().sendKeys(Keys.ENTER);
	}

	/**
	 * Clears text from the TextBox
	 */
	public void clear() {
		webElement().clear();
	}

	/**
	 * Overwrite base getText method.
	 */
	public String getText() {
		return getValue("value");
	}

	/**
	 * Gets formatted value
	 */
	public String getFormattedValue() {
		return getValue("formattedvalue");
	}

	/**
	 * Gets value based on attribute
	 * 
	 * @param attribute
	 * @return String
	 */
	private String getValue(String attribute) {
		return webElement().getAttribute(attribute);
	}
}
