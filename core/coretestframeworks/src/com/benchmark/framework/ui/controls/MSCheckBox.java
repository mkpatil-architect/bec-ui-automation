

package com.benchmark.framework.ui.controls;

import org.openqa.selenium.WebElement;

public class MSCheckBox extends BaseControl {

	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSCheckBox(String xpath) {
		super(xpath);
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSCheckBox(String findByType, String valueToFindElement) {
		super(findByType, valueToFindElement);
	}

	/**
	 * 
	 * @param element
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSCheckBox(WebElement element, String findByType,
			String valueToFindElement) {
		super(element, findByType, valueToFindElement);
	}

	/**
	 * Select the CheckBox
	 */
	public void check() {
		selectCheckbox(false);
	}

	/**
	 * Select the CheckBox
	 */
	public void check(boolean isJSExecutor) {
		selectCheckbox(true);
	}

	/**
	 * DeSelect the CheckBox
	 */
	public void unCheck() {
		deSelectCheckbox(false);
	}

	/**
	 * DeSelect the CheckBox
	 */
	public void unCheck(boolean isJSExecutor) {
		deSelectCheckbox(true);
	}

	/**
	 * Verifies whether CheckBox is checked or not
	 * 
	 * @return
	 */
	public boolean isChecked() {
		return webElement().isSelected();
	}

	/**
	 * Select CheckBox
	 * 
	 * @param isJSExecutor
	 */
	private void selectCheckbox(boolean isJSExecutor) {
		if (!isChecked()) {
			toggle(isJSExecutor);
		}
	}

	/**
	 * De-Select CheckBox
	 * 
	 * @param isJSExecutor
	 */
	private void deSelectCheckbox(boolean isJSExecutor) {
		if (isChecked()) {
			toggle(isJSExecutor);
		}
	}

	/**
	 * Clicks on the CheckBox
	 */
	private void toggle(boolean isJSExecutor) {
		if (isJSExecutor)
			click(true);
		else
			click();
	}
}
