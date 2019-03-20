package com.benchmark.framework.ui.controls;

import org.openqa.selenium.WebElement;

public class MSLabel extends BaseControl {
	
	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSLabel(String xpath) {
		super(xpath);
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSLabel(String findByType, String valueToFindElement) {
		super(findByType, valueToFindElement);
	}

	public MSLabel(WebElement element, String findByType,
			String valueToFindElement) {
		super(element, findByType, valueToFindElement);
	}
}
