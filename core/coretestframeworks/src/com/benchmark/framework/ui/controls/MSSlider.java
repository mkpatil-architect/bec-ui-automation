

package com.benchmark.framework.ui.controls;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.benchmark.framework.ui.SeleniumWrapper;

public class MSSlider extends BaseControl {

	/**
	 * Holds childBy value
	 */
	private By m_childByValue = null;

	/**
	 * Holds child percent
	 */
	private By m_childByPercent = null;

	private By m_parentDiv = null;

	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSSlider(String xpath) {
		super(xpath);
	}

	/**
	 * Constructor for slider element
	 */
	public MSSlider(String findByType, String valueToFindElement) {
		this(findByType, valueToFindElement, null, null);
	}

	/**
	 * Constructor for slider element
	 */
	public MSSlider(String findByType, String valueToFindElement,
			String childFindByType, String childValuToFindElement) {
		this(findByType, valueToFindElement, childFindByType,
				childValuToFindElement, null, null);
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSSlider(String findByType, String valueToFindElement,
			String childFindByType, String childValuToFindElement,
			String childPercentageFindByType, String childPercentageValueElement) {
		super(findByType, valueToFindElement);
		m_parentDiv = createBy(findByType, valueToFindElement.replace(">a", ""));
		if (childFindByType != null)
			m_childByValue = createBy(childFindByType, childValuToFindElement);
		if (childPercentageFindByType != null)
			m_childByPercent = createBy(childPercentageFindByType,
					childPercentageValueElement);
	}

	/**
	 * This method will move the slider
	 */
	public void dragSlider(int x) {
		SeleniumWrapper.actions().clickAndHold(webElement()).moveByOffset(x, 0)
				.release(webElement()).build().perform();
	}

	/**
	 * This method will get the slider value when value is available only the
	 * slider else returns 0
	 */
	public String dragSliderAndGetChangedValue(int x) {
		dragSlider(x);
		SeleniumWrapper.sleep(2000);
		return getSliderValue();
	}

	/**
	 * This method verifies slider is disabled or not
	 */
	public boolean isEnabled() {
		return webElement().getAttribute("disabled").trim().equals("false") ? true
				: false;
	}

	/**
	 * Gets Slider Value
	 */
	public String getSliderValue() {
		return getValueFromSlider(m_childByValue);
	}

	/**
	 * Gets Slider Percentage value
	 */
	public String getSliderPercentage() {
		return getValueFromSlider(m_childByPercent);
	}

	// /**
	// * This method will mouse over the control
	// */
	// public void mouseOver() {
	// SeleniumWrapper.actions().moveToElement(webElement()).click().perform();
	// }

	/**
	 * Gets slider value
	 */
	private String getValueFromSlider(By byElement) {
		try {
			WebElement sliderValue = getChildElement(
					getChildElement(m_parentDiv), byElement);
			return sliderValue.getText();
		} catch (Exception e) {
			return "0";
		}

	}
}
