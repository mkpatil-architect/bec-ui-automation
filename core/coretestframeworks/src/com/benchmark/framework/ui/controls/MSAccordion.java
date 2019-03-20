

package com.benchmark.framework.ui.controls;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;

/**
 * Class for Accordion panel
 */
public class MSAccordion extends BaseControl {

	// -----------------------------------------------------------------------------------------------------------------
	// Constructors

	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSAccordion(String xpath, String childFindByType, String childTag) {
		super(xpath);
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSAccordion(String findByType, String valueToFindElement,
			String childFindByType, String childTag) {
		super(findByType, valueToFindElement);
		m_childBy = createBy(childFindByType, childTag);
	}

	/**
	 * Constructor
	 */
	public MSAccordion(String parentFindType, String parentFindValue,
			String childFindType, String childFindValue,
			String subChildFindType, String subChildFindValue, String attribute) {
		super(parentFindType, parentFindValue);
		m_childBy = createBy(childFindType, childFindValue);
		m_subChildBy = createBy(subChildFindType, subChildFindValue);
		m_attribute = attribute;
	}

	/**
	 * Constructor
	 */
	public MSAccordion(WebElement element, String parentFindType,
			String parentFindValue, String childFindType,
			String childFindValue, String subChildFindType,
			String subChildFindValue, String attribute) {
		super(element, parentFindType, parentFindValue);
		m_childBy = createBy(childFindType, childFindValue);
		m_subChildBy = createBy(subChildFindType, subChildFindValue);
		m_attribute = attribute;
	}

	// -----------------------------------------------------------------------------------------------------------------
	// Control Methods

	/**
	 * Click on accordion panel
	 * 
	 * @param text
	 */
	public void clickAccordion(String text) {
		clickWebElement(webElement(), m_childBy, text, "text");
	}

	/**
	 * Click Element in Accordion
	 * 
	 * @param text
	 *            -> Text to select.
	 * @return -> True/False
	 */
	public boolean click(String text) {
		try {
			List<WebElement> childElements = webElement().findElements(
					m_childBy);
			for (WebElement childElement : childElements) {
				WebElement subChildElement = childElement
						.findElement(m_subChildBy);
				String value = subChildElement.getAttribute(m_attribute);
				if (value.contains(text)) {
					subChildElement.click();
					return true;
				}
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, "", e.toString());
		}
		return false;
	}

	/**
	 * Method to get Accordion Values
	 * 
	 * @return -> Accordion Values
	 */
	public List<String> getValues() {
		try {
			List<String> values = new ArrayList<String>();
			List<WebElement> childElements = webElement().findElements(
					m_childBy);
			for (WebElement childElement : childElements) {
				WebElement subChildElement = childElement
						.findElement(m_subChildBy);
				String value = subChildElement.getAttribute(m_attribute);
				values.add(value.replace("\n", ""));
			}
			return values;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, "", e.toString());
		}
		return null;
	}

	// -----------------------------------------------------------------------------------------------------------------
	// Private Variables

	/**
	 * Holds child element find expression
	 */
	private By m_childBy = null;
	/**
	 * Holds sub child element find expression
	 */
	private By m_subChildBy = null;
	/**
	 * Holds attribute value
	 */
	private String m_attribute = null;

}
