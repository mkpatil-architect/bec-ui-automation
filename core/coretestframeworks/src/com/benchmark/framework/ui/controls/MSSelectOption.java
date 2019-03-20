

package com.benchmark.framework.ui.controls;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.framework.ui.SeleniumWrapper;

/**
 * Class for Select -> Option (DropDownList)
 */
public class MSSelectOption extends BaseControl {
	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSSelectOption(String xpath) {
		super(xpath);
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSSelectOption(String findByType, String valueToFindElement) {
		super(findByType, valueToFindElement);
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSSelectOption(WebElement element, String findByType,
			String valueToFindElement) {
		super(element, findByType, valueToFindElement);
	}

	/**
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @param childFindByType
	 * @param childValueToFindElement
	 * @param subChildFindByType
	 * @param subChildValueToFindElement
	 */
	public MSSelectOption(String findByType, String valueToFindElement,
			String parentFindByType, String parentValueToFindElement,
			String childFindByType, String childValueToFindElement,
			String subChildFindByType, String subChildValueToFindElement) {
		super(findByType, valueToFindElement);
		m_parentFindByType = parentFindByType;
		m_parentValueToFindElement = parentValueToFindElement;
		m_childFindByType = childFindByType;
		m_childValueToFindElement = childValueToFindElement;
		m_subChildFindByType = subChildFindByType;
		m_subChildValueToFindElement = subChildValueToFindElement;
	}

	/**
	 * Select option based on the given text.
	 * 
	 * @param item
	 */
	public void selectOption(String item) {
		List<WebElement> options = getChildElements(createBy(
				FindByTypeConstants.TAG_NAME, "option"));
		for (WebElement option : options) {
			String value = (option.getText().trim().length() > 0) ? option
					.getText().trim() : option.getAttribute("textContent")
					.trim();
			if (value.equals(item.trim())) {
				// SeleniumWrapper.getJavascriptExecutor().executeScript(
				// "arguments[0].click();", option);
				option.click();
				break;
			}
		}
	}

	/**
	 * Method to get all elements from DropDownList
	 */
	public List<String> getAllElements() {
		List<String> allElements = new ArrayList<String>();
		List<WebElement> options = getChildElements(createBy(
				FindByTypeConstants.TAG_NAME, "option"));
		for (WebElement option : options) {
			String value = (option.getText().trim().length() > 0) ? option
					.getText().trim() : option.getAttribute("textContent")
					.trim();
			allElements.add(value);
		}
		return allElements;
	}

	/**
	 * Method to get selected value
	 */
	public String getSelectedValue() {
		Select selectBox = new Select(webElement());
		String value = selectBox.getFirstSelectedOption().getText();
		return (value.trim().length() > 0) ? value : selectBox
				.getFirstSelectedOption().getAttribute("textContent");
	}

	public void selectByTextContent(String item) {
		WebElement parentElement = SeleniumWrapper.webDriver().findElement(
				createBy(m_parentFindByType, m_parentValueToFindElement));
		parentElement.click();
		WebElement childElement = SeleniumWrapper.webDriver().findElement(
				createBy(m_childFindByType, m_childValueToFindElement));
		List<WebElement> lis = childElement.findElements(createBy(
				m_subChildFindByType, m_subChildValueToFindElement));
		for (WebElement option : lis) {
			if (option.getAttribute("textContent").trim().equals(item.trim())) {
				option.click();
				break;
			}
		}
	}

	private String m_parentFindByType;
	private String m_parentValueToFindElement;
	private String m_childFindByType;
	private String m_childValueToFindElement;
	private String m_subChildFindByType;
	private String m_subChildValueToFindElement;
}
