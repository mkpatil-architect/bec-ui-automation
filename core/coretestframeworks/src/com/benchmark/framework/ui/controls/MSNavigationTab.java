

package com.benchmark.framework.ui.controls;

import org.openqa.selenium.By;

public class MSNavigationTab extends BaseControl {

	/*
	 * Holds child control By
	 */
	private By m_childBy = null;

	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSNavigationTab(String xpath, String childFindByType, String childTag) {
		super(xpath);
		m_childBy = createBy(childFindByType, childTag);
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSNavigationTab(String findByType, String valueToFindElement,
			String childFindByType, String childTag) {
		super(findByType, valueToFindElement);
		m_childBy = createBy(childFindByType, childTag);
	}

	/**
	 * Click on the tab.
	 * 
	 * @param tabText
	 */
	public void clickTab(String tabText) {
		clickWebElement(m_childBy, tabText);
	}

	/**
	 * Click on the tab.
	 * 
	 * @param tabText
	 */
	public void clickTab(String tabText, String attribute) {
		clickWebElement(m_childBy, tabText, attribute);
	}

	public void click() {
		webElement().findElement(m_childBy).click();
	}

}
