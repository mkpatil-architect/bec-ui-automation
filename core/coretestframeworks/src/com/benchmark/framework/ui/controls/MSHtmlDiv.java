

package com.benchmark.framework.ui.controls;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Class for HTMLDIV.
 */
public class MSHtmlDiv extends BaseControl {

	private By m_childBy = null;

	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSHtmlDiv(String xpath) {
		super(xpath);
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSHtmlDiv(String findByType, String valueToFindElement) {
		super(findByType, valueToFindElement);
	}

	/**
	 * Constructor - If we need to get all child HtmlDivs from parent HtmlDiv
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @param childFindByType
	 * @param childValuToFindElement
	 */
	public MSHtmlDiv(String findByType, String valueToFindElement,
			String childFindByType, String childValuToFindElement) {
		super(findByType, valueToFindElement);
		m_childBy = createBy(childFindByType, childValuToFindElement);
	}

	/**
	 * Constructor - If we need to get element from web element.
	 * 
	 * @param element
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSHtmlDiv(WebElement element, String findByType,
			String valueToFindElement) {
		super(element, findByType, valueToFindElement);
	}

	/**
	 * Gets multiple controls innerText in HtmlDiv control.
	 * 
	 * @param attribute
	 * @return
	 */
	public String[] getMultipleControlValues() {
		List<WebElement> controls = getChildElements(webElement(), m_childBy);
		// System.out.println("Total Controls: " + controls.size());
		List<String> controlsText = new ArrayList<String>();
		for (WebElement control : controls) {
			controlsText.add(control.getText());
		}
		return controlsText.toArray(new String[controlsText.size()]);
	}

	/**
	 * Gets multiple controls innerText in HtmlDiv control based on attribute
	 * 
	 * @param attribute
	 * @return
	 */
	public String[] getMultipleControlValues(String attribute) {
		List<WebElement> controls = getChildElements(webElement(), m_childBy);
		// System.out.println("Total Controls: " + controls.size());
		List<String> controlsText = new ArrayList<String>();
		for (WebElement control : controls) {
			controlsText.add(control.getAttribute(attribute));
		}
		return controlsText.toArray(new String[controlsText.size()]);
	}

}
