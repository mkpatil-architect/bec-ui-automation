

package com.benchmark.framework.ui.controls;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;
import com.benchmark.framework.ui.SeleniumWrapper;

/**
 * Class for DropDownList custom control
 */
public class MSDropDownList extends BaseControl {

	private By m_childBy = null;
	private By m_subChildBy = null;
	private String m_attribute = null;
	private boolean m_isChildDiv = false;

	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSDropDownList(String xpath, String childFindByType, String childTag, String subChildFindBy,
			String subChildTag, String attribute) {
		super(xpath);
		m_childBy = createBy(childFindByType, childTag);
		m_subChildBy = createBy(subChildFindBy, subChildTag);
		m_attribute = attribute;
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSDropDownList(String findByType, String valueToFindElement, String childFindByType, String childTag,
			String subChildFindBy, String subChildTag, String attribute) {
		super(findByType, valueToFindElement);
		m_childBy = createBy(childFindByType, childTag);
		m_subChildBy = createBy(subChildFindBy, subChildTag);
		m_attribute = attribute;
	}

	/**
	 * Constructor to find dropdownlist with Div tags
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @param childFindByType
	 * @param childTag
	 * @param subChildFindBy
	 * @param subChildTag
	 * @param isChildDiv
	 */
	public MSDropDownList(String findByType, String valueToFindElement, String childFindByType, String childTag,
			String subChildFindBy, String subChildTag, boolean isChildDiv) {
		super(findByType, valueToFindElement);
		m_childBy = createBy(childFindByType, childTag);
		m_subChildBy = createBy(subChildFindBy, subChildTag);
		m_isChildDiv = isChildDiv;
		m_attribute = "textContent";
	}

	/**
	 * Method will DeSelect the item first and then select the item. This method
	 * is helpful for DropDownList which are having CheckBoxes
	 * 
	 * @param deselectItem
	 * @param selectItem
	 */
	public void deSelectAndSelectItem(String deselectItem, String selectItem) {
		String[] selectItems = new String[] { deselectItem, selectItem };
		webElement().click();
		clickOnMultipleWebElement(getChildElement(m_childBy), m_subChildBy, selectItems, m_attribute);
	}

	/**
	 * Method will get all values from the DropDownList
	 * 
	 * @return
	 */
	public String[] getAllValues() {
		List<String> ddlValues = getValues();
		return ddlValues.toArray(new String[ddlValues.size()]);
	}

	/**
	 * Convenience method to access the dropdown selection options
	 * 
	 * @return A list of the displayed text values of all dropdown options
	 */
	public List<String> getValues() {
		List<WebElement> elements = getChildElements(getChildElement(m_childBy), m_subChildBy);
		List<String> ddlValues = new ArrayList<String>();
		for (WebElement element : elements) {
			String nextValue = "";
			if (m_attribute.equals("text")) {
				nextValue = element.getText();
			}
			if (nextValue.equals("")) {
				nextValue = element.getAttribute(m_attribute);
			}
			ddlValues.add(nextValue.replaceAll("\u00A0", "").trim());
		}
		return ddlValues;
	}

	/**
	 * Select Single item in the DropDownList.
	 * 
	 * This code first checks to see if the root element for the dropdown list
	 * has an Angular click event defined; if so it clicks on the root element
	 * to expand the selection list. Otherwise, it searches for any sibling
	 * element with an Angular click event and clicks on that element.
	 * 
	 * The selection logic then uses the constructor-defined values for
	 * {@code m_childBy} and {@code m_subChildBy} to attempt to click on the
	 * specified selection
	 * 
	 * @param selectItem
	 *            The text value displayed in the UI for the element the test
	 *            will select
	 */
	public void select(String selectItem) {
		if (isChild(FindByTypeConstants.CSS_SELECTOR, ".selectDim", MSButton.class) == null) {
			webElement().click();
		} else {
			MSButton openPulldownMenu = getChild(FindByTypeConstants.CSS_SELECTOR, ".selectDim", MSButton.class);
			openPulldownMenu.click();
		}
		if (!m_isChildDiv) {
			clickOnWebElement(getChildElement(m_childBy), m_subChildBy, selectItem, m_attribute);
		} else {
			clickOnSubChildDiv(selectItem, getChildElement(m_childBy), m_subChildBy);
		}
	}

	/**
	 * Select multiple items in the DropDownList based on the array item passed
	 * by the user.
	 * 
	 * @param selectItems
	 *            (String[])
	 */
	public void selectMultiple(String selectItems[]) {
		webElement().click();
		clickOnMultipleWebElement(getChildElement(m_childBy), m_subChildBy, selectItems, m_attribute);
	}

	/**
	 * Select multiple items in the DropDownList. Based on the count it will
	 * select those many items from starting.
	 * 
	 * @param selectItems1Ton
	 *            (Example: if we pass 3 then in the DropDownList it will select
	 *            first 3 items.)
	 */
	public String[] selectMultiple(int selectItems1Ton) {
		webElement().click();
		return clickOnMultipleWebElement(getChildElement(m_childBy), m_subChildBy, selectItems1Ton, m_attribute);
	}

	/**
	 * Method to select element for dynamic DropDownList.
	 * 
	 * @param selectItem
	 *            -> Select Item
	 * @param dynamicCount
	 *            -> element to find from the list
	 * @return -> True/False
	 */
	public boolean select(String selectItem, int dynamicCount) {
		try {
			webElement().click();
			List<WebElement> uls = webElement().findElements(m_childBy);
			int i = 0;
			for (WebElement ul : uls) {
				if (i == dynamicCount) {
					List<WebElement> lis = ul.findElements(m_subChildBy);
					for (WebElement element : lis) {
						String temp = m_attribute == "text" ? element.getText() : element.getAttribute(m_attribute);
						if (temp.trim().toLowerCase().equals(selectItem.trim().toLowerCase())) {
							element.click();
							return true;
						}
					}
				} else {
					i++;
				}
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, MSDropDownList.class.getName(), e.toString());
		}
		return false;
	}

	/**
	 * Select Single item in the DropDownList
	 */
	public void selectOnlyItem(String selectItem) {
		for (int i = 0; i < 5; i++) {
			if (getChildElement(m_childBy).isDisplayed())
				break;
			SeleniumWrapper.sleep(100);
		}
		// Assert.assertTrue(getChildElement(m_childBy).isDisplayed(),
		// "Dropdown is not visible, cannot select item '" + selectItem + "'");
		if (!m_isChildDiv) {
			clickOnWebElement(getChildElement(m_childBy), m_subChildBy, selectItem, m_attribute);
		} else {
			clickOnSubChildDiv(selectItem, getChildElement(m_childBy), m_subChildBy);
		}
	}

	/**
	 * Select multiple items in the DropDownList based on the array item passed
	 * by the user.
	 * 
	 * @param selectItems
	 *            (String[])
	 */
	public void selectOnlyMultipleItems(String selectItems[]) {
		clickOnMultipleWebElement(getChildElement(m_childBy), m_subChildBy, selectItems, m_attribute);
	}

	/**
	 * This method was overridden for DropDownList with CheckBoxes
	 */
	protected void clickOnMultipleWebElement(WebElement subChildelement, By by, String text[], String attribute) {
		List<WebElement> elements = getChildElements(subChildelement, by);

		for (int i = 0; i <= text.length - 1; i++) {
			for (WebElement element : elements) {
				String temp = attribute == "text" ? element.getText() : element.getAttribute(attribute);
				if (temp.trim().toLowerCase().equals(text[i].trim().toLowerCase())) {
					try {
						MSHyperLink link = new MSHyperLink(element, FindByTypeConstants.TAG_NAME, "a");
						link.click();
					} catch (Exception e) {
						element.click();
					}
					break;
				}
			}
		}
	}

	/**
	 * This method overridden to select multiple elements for DropDownList with
	 * CheckBoxes
	 */
	protected String[] clickOnMultipleWebElement(WebElement subChildelement, By by, int itemsCount, String attribute) {
		List<String> selectedItems = new ArrayList<String>();
		List<WebElement> elements = getChildElements(subChildelement, by);
		int i = 0;
		for (WebElement element : elements) {
			if (i == itemsCount) {
				return selectedItems.toArray(new String[selectedItems.size()]);
			}
			String temp = attribute == "text" ? element.getText() : element.getAttribute(attribute);
			try {
				MSHyperLink link = new MSHyperLink(element, FindByTypeConstants.TAG_NAME, "a");
				link.click();
			} catch (Exception e) {
				element.click();
			}
			selectedItems.add(temp);
			i++;
		}
		return selectedItems.toArray(new String[selectedItems.size()]);
	}

	protected void clickOnWebElement(WebElement subChildelement, By by, String text, String attribute) {
		List<WebElement> elements = getChildElements(subChildelement, by);
		// System.out.println(elements.size());
		for (WebElement element : elements) {
			String temp = attribute == "text" ? element.getText() : element.getAttribute(attribute);
			String lowercase = temp.replaceAll("\u00A0", "").trim().toLowerCase();
			if (lowercase.equals(text.trim().toLowerCase())) {
				try {
					if (isElementAvailableInControl(element, FindByTypeConstants.TAG_NAME, "a")) {
						MSHyperLink link = new MSHyperLink(element, FindByTypeConstants.TAG_NAME, "a");

						link.click();
					} else {
						element.click();
					}
				} catch (Exception e) {
					element.click();
				}
				return;
			}
		}
	}

	/**
	 * Method to click on sub child div element.
	 * 
	 * @param text
	 * @param subChildelement
	 * @param by
	 */
	private void clickOnSubChildDiv(String text, WebElement subChildelement, By by) {
		List<WebElement> elements = getChildElements(subChildelement, by);
		for (WebElement element : elements) {
			String value = element.getText();
			if (value.isEmpty())
				value = element.getAttribute("textContent");
			value = value.trim().toLowerCase();
			text = text.replaceAll("\u00A0", "").trim().toLowerCase();
			if (value.equals(text)) {
				// scrollIntoView has been experimentally modified to use
				// window.scrollTo
				// to avoid triggering JS events
				SeleniumWrapper.scrollIntoView(element);
				element.click();
				return;
			}
		}
	}

	public boolean isDropdownVisible() {
		WebElement test = null;
		try {
			test = getChildElement(m_childBy);
		} catch (NoSuchElementException e) {
		}
		if (test == null)
			return false;
		return test.isDisplayed();
	}
}
