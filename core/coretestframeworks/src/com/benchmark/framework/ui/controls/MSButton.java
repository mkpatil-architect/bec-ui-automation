

package com.benchmark.framework.ui.controls;

import org.openqa.selenium.WebElement;

/**
 * Class for button control.
 */
public class MSButton extends BaseControl {

	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSButton(String xpath) {
		super(xpath);
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSButton(String findByType, String valueToFindElement) {
		super(findByType, valueToFindElement);
	}

	/**
	 * Constructor to find the button from a particular control.
	 * 
	 * @param element
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSButton(WebElement element, String findByType,
			String valueToFindElement) {
		super(element, findByType, valueToFindElement);
	}

}
