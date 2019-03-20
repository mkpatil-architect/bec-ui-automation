
package com.benchmark.framework.ui.controls;

import org.openqa.selenium.Alert;

import com.benchmark.core.constants.AlertWindowConstants;
import com.benchmark.framework.ui.SeleniumWrapper;

public class MSAlertBox extends BaseControl {

	private Alert m_alert;

	/**
	 * Constructor - Bind JS alert window.
	 */
	public MSAlertBox() {
		m_alert = SeleniumWrapper.webDriver().switchTo().alert();
	}

	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSAlertBox(String xpath) {
		super(xpath);
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSAlertBox(String findByType, String valueToFindElement) {
		super(findByType, valueToFindElement);
	}

	/**
	 * Closes the alert window.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public void close(String findByType, String valueToFindElement) {
		getChildElement(webElement(), createBy(findByType, valueToFindElement))
				.click();
	}

	/**
	 * Confirmation window. Clicks Yes or No button on the confirmation window.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public void confirm(String findByType, String valueToFindElement) {
		getChildElement(webElement(), createBy(findByType, valueToFindElement))
				.click();

	}

	/**
	 * Gets Header text
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @return
	 */
	public String getHeaderText(String findByType, String valueToFindElement) {
		return getChildElement(webElement(),
				createBy(findByType, valueToFindElement)).getText();
	}

	/**
	 * Gets Window Content
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @return
	 */
	public String getWindowContent(String findByType, String valueToFindElement) {
		return getChildElement(webElement(),
				createBy(findByType, valueToFindElement)).getText();
	}

	/**
	 * Method to click JS alert window OK or Cancel
	 */
	public void clickJSAlert(String okOrCancel) {
		if (okOrCancel.equals(AlertWindowConstants.OK)) {
			m_alert.accept();
		} else if (okOrCancel.equals(AlertWindowConstants.CANCEL)) {
			m_alert.dismiss();
		}
	}

	/**
	 * Method to get Alert window text
	 */
	public String getJSAlertText() {
		return m_alert.getText();
	}
}
