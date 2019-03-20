

package com.benchmark.framework.ui.controls;

import org.openqa.selenium.WebElement;

public class MSImage extends BaseControl {

	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSImage(String xpath) {
		super(xpath);
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSImage(String findByType, String valueToFindElement) {
		super(findByType, valueToFindElement);
	}

	/**
	 * Constructor to find Image from a particular control.
	 * 
	 * @param element
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSImage(WebElement element, String findByType,
			String valueToFindElement) {
		super(element, findByType, valueToFindElement);
	}

}
