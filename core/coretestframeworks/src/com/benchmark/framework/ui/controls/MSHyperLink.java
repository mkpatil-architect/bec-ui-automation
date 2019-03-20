

package com.benchmark.framework.ui.controls;

import org.openqa.selenium.WebElement;

public class MSHyperLink extends BaseControl {
	
	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSHyperLink(String xpath) {
		super(xpath);
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType Locator strategy. Behavior is only defined when one of the constants 
	 * specified in {@link com.marketshare.core.constants.FindByTypeConstants FindByTypeConstants}
	 * is provided.
	 * @param valueToFindElement Locator string, parsed based on the specified strategy
	 */
	public MSHyperLink(String findByType, String valueToFindElement) {
		super(findByType, valueToFindElement);
	}
	
	
	public MSHyperLink(WebElement element, String findByType,
			String valueToFindElement) {
		super(element, findByType, valueToFindElement);
	}
	
}
