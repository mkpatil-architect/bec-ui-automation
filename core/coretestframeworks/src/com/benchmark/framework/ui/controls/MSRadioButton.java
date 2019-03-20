

package com.benchmark.framework.ui.controls;

public class MSRadioButton extends BaseControl {

	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSRadioButton(String xpath) {
		super(xpath);
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSRadioButton(String findByType, String valueToFindElement) {
		super(findByType, valueToFindElement);
	}

	/**
	 * Select the CheckBox
	 */
	public void select() {
		click();
	}

	/**
	 * Verifies whether radio button is selected or not
	 * 
	 * @return
	 */
	public boolean isSelected() {
		return webElement().isSelected();
	}
}
