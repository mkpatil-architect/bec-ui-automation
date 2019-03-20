package com.benchmark.framework.ui.controls;

import org.openqa.selenium.WebElement;

public class MSSpan extends BaseControl {

	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSSpan(String xpath) {
		super(xpath);
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSSpan(String findByType, String valueToFindElement) {
		super(findByType, valueToFindElement);
	}

	public MSSpan(WebElement element, String findByType, String value) {
		super(element, findByType, value);
	}
}
