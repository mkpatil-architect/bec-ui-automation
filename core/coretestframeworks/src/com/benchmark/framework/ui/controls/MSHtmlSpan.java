

package com.benchmark.framework.ui.controls;

/**
 * Class for HTML Span custom control.
 */
public class MSHtmlSpan extends BaseControl {

	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSHtmlSpan(String xpath) {
		super(xpath);
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSHtmlSpan(String findByType, String valueToFindElement) {
		super(findByType, valueToFindElement);
	}
}
