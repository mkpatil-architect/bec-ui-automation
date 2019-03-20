
package com.benchmark.framework.ui.controls;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;
import com.benchmark.core.util.CommonUtil;
import com.benchmark.framework.ui.SeleniumWrapper;

/**
 * BaseControl class for all custom controls
 */
public abstract class BaseControl {

	/**
	 * Holds WebElement
	 */
	protected WebElement m_webElement = null;

	public BaseControl() {

	}

	/**
	 * Base Constructor with XPath
	 * 
	 * @param xpath
	 */
	public BaseControl(String xpath) {
		this(FindByTypeConstants.XPATH, xpath);
	}

	/**
	 * Base Constructor with findByType
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public BaseControl(String findByType, String valueToFindElement) {
		try {
			SeleniumWrapper.scrollIntoView(findByType, valueToFindElement);
			m_webElement = SeleniumWrapper.webDriver().findElement(createBy(findByType, valueToFindElement));
			SeleniumWrapper.scrollIntoView(m_webElement);
		} catch (NoSuchElementException nsee) {
			SeleniumWrapper.takeScreenShot();
			Assert.assertTrue(false,
					CommonUtil.appendErrorCodeToMessage(String.format(
							"Unable to locate element: {method: %s, Selector: %s}. Can’t proceed to execute the next test step. Error occured in page: %s",
							findByType, valueToFindElement, currentURL())));
		} catch (Exception e) {
			SeleniumWrapper.takeScreenShot();
			Assert.assertTrue(false, "Exception: " + e.getMessage() != null ? e.getMessage() : e.toString());
		}
	}

	/**
	 * Base Constructor with findByType
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public BaseControl(WebElement element, String findByType, String valueToFindElement) {
		try {
			m_webElement = element.findElement(createBy(findByType, valueToFindElement));
		} catch (NoSuchElementException nsee) {
			SeleniumWrapper.takeScreenShot();
			Assert.fail(CommonUtil.appendErrorCodeToMessage(String.format(
					"Unable to locate element: {method: %s, Selector: %s}. Can’t proceed to execute the next test step. Error occured in page: %s",
					findByType, valueToFindElement, currentURL())));
		} catch (Exception e) {
			SeleniumWrapper.takeScreenShot();
			Assert.assertTrue(false, "Exception: " + e.getMessage() != null ? e.getMessage() : e.toString());
		}
	}

	/**
	 * Method to check element is available in control
	 * 
	 * @param element
	 * @param findByType
	 * @param valueToFindElement
	 * @return
	 */
	protected boolean isElementAvailableInControl(WebElement element, String findByType, String valueToFindElement) {
		try {
			element.findElement(createBy(findByType, valueToFindElement));
			return true;
		} catch (NoSuchElementException nsee) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Method to get Current URL
	 * 
	 * @return -> Current URL
	 */
	public String currentURL() {
		return SeleniumWrapper.webDriver().getCurrentUrl();
	}

	/**
	 * Get webElement
	 * 
	 * @return
	 */
	protected WebElement webElement() {
		return m_webElement;
	}

	/**
	 * Get Attribute value based on attribute
	 * 
	 * @param attribute
	 * @return Value
	 */
	public String getAttributeValue(String attribute) {
		return m_webElement.getAttribute(attribute);
	}

	public String getCssValue(String attribute) {
		return m_webElement.getCssValue(attribute);
	}

	/**
	 * Clicks on any WebElement
	 */
	public void click() {
		SeleniumWrapper.scrollIntoView(m_webElement);
		m_webElement.click();
	}

	/**
	 * Test to determine the the MS element is selected
	 * 
	 * @return true if the element is a selected checkbox, false otherwise
	 */
	public boolean isSelected() {
		return m_webElement.isSelected();
	}

	/**
	 * 
	 * @param isJSExecutor
	 */
	public void click(boolean isJSExecutor) {
		if (m_webElement.getAttribute("id").length() != 0) {
			SeleniumWrapper.getJavascriptExecutor().executeScript(
					String.format("window.document.getElementById('%s').click()", m_webElement.getAttribute("id")));
		} else if (m_webElement.getAttribute("class").length() != 0) {
			System.out.println(m_webElement.getAttribute("class"));
			String value = String.format(
					"var elements = window.document.getElementsByClassName('%s'); for (var i = 0; i<elements.length; i++) { var element = elements[i]; if (element.value == '%s') { element.click(); } }",
					m_webElement.getAttribute("class"), m_webElement.getAttribute("value"));
			// System.out.println(value);
			SeleniumWrapper.getJavascriptExecutor().executeScript(value);
		}
	}

	/**
	 * This method will return element count based on type and value
	 */
	public int getElementsCount(String findByType, String findByValue) {
		try {
			return SeleniumWrapper.webDriver().findElements(createBy(findByType, findByValue)).size();
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * This method will return the child Elements Count based on type and value
	 * from the parent control
	 * 
	 * @param childFindByType
	 *            -> ChildFindByType
	 * @param childFindByValue
	 *            -> ChildFindByValue
	 * @return -> Elements Count
	 */
	public int getElementsCountFromParent(String childFindByType, String childFindByValue) {
		try {
			return webElement().findElements(createBy(childFindByType, childFindByValue)).size();
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Gets innerText of the element.
	 * 
	 * @return
	 */
	public String getText() {
		return m_webElement.getText();
	}

	/**
	 * Get the text value from the current element without embedding child
	 * elements
	 * 
	 * @return The contents of the {@code TEXT_NODE}s contained in this element
	 */
	public String getTextWithoutChildren() {
		return (String) ((JavascriptExecutor) SeleniumWrapper.webDriver()).executeScript(
				"var parent = arguments[0];" + "var child = parent.firstChild;" + "var textValue = \"\";"
						+ "while(child) {" + " if (child.nodeType === Node.TEXT_NODE)"
						+ " textValue += child.textContent;" + " child = child.nextSibling;" + "}" + "return textValue",
				m_webElement);
	}

	/**
	 * Gets ToolTip for that control. If ToolTip is not defined then it will
	 * return null.
	 * 
	 * @return
	 */
	public String getTootTip() {
		return m_webElement.getAttribute("title").trim();
	}

	/**
	 * Returns true if the element is enabled, false otherwise.
	 * 
	 * @return
	 */
	public boolean isEnabled() {
		return m_webElement.isEnabled();
	}

	/**
	 * Return true if the element is displayed, false otherwise.
	 * 
	 * @return
	 */
	public boolean isVisible() {
		if (null == m_webElement)
			return false;
		try {
			SeleniumWrapper.sleep();
			return m_webElement.isDisplayed();
		} catch (StaleElementReferenceException elementHasDisappeared) {
			return false;
		}
	}

	/**
	 * Method to mouse over on particular control
	 * 
	 * @return -> True/False
	 */
	public boolean mouseOver() {
		return mouseOver(webElement());
	}

	/**
	 * Method to mouse over on particular control
	 * 
	 * @param element
	 *            -> WebElement
	 * @return -> True/False
	 */
	public boolean mouseOver(WebElement element) {
		try {
			SeleniumWrapper.actions().moveToElement(element).perform();
			return true;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, BaseControl.class.getName(), e.toString());
		}
		return false;
	}

	/**
	 * Mouse Click Event
	 * 
	 * @return -> True/False
	 */
	public boolean mouseClick() {
		return mouseClick(webElement());
	}

	/**
	 * Mouse Click Event
	 * 
	 * @return -> True/False
	 */
	public boolean mouseClick(WebElement element) {
		try {
			SeleniumWrapper.actions().click(element).perform();
			return true;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, BaseControl.class.getName(), e.toString());
		}
		return false;
	}

	/**
	 * Method will double click on particular element
	 * 
	 * @return -> True/False
	 */
	public boolean doubleClick() {
		try {
			SeleniumWrapper.actions().doubleClick(webElement()).build().perform();
			return true;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, BaseControl.class.getName(), e.toString());
		}
		return false;
	}

	/**
	 * Method to click on element based on relative position.
	 * 
	 * @param x
	 * @param y
	 * @return -> True/False
	 */
	public boolean clickOffset(int x, int y) {
		try {
			SeleniumWrapper.actions().moveToElement(webElement(), x, y).click().build().perform();
			return true;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, BaseControl.class.getName(), e.toString());
			return false;
		}
	}

	/**
	 * Gets width of the rendered element.
	 */
	public int width() {
		return webElement().getSize().getWidth();
	}

	/**
	 * Find a child node from the current element; return the child as whatever
	 * control element the user specifies
	 * 
	 * @author Zach Steele
	 * @since Dec 2, 2015
	 * 
	 * @param <T>
	 *            Data type of control element; must extend from BaseControl.
	 * @param findByType
	 *            Locator strategy (see {@link FindByTypeConstants})
	 * @param locator
	 *            String specifying the actual locator (relative to current
	 *            element)
	 * @param controlType
	 *            Class type for the return value
	 * @return MS control element of the specified type
	 */
	public <T extends BaseControl> T getChild(String findByType, String locator, Class<T> controlType) {
		try {
			if (webElement().findElements(createBy(findByType, locator)).size() == 0) {
				return null;
			}
			return controlType.getConstructor(WebElement.class, String.class, String.class).newInstance(webElement(),
					findByType, locator);
		} catch (NoSuchMethodException | SecurityException e) {
			Log.writeMessage(this.getClass().getName(), "Unexpected error");
			e.printStackTrace();
			Assert.fail(CommonUtil.appendErrorCodeToMessage("Error while attempting to find element: " + locator));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) {
			Log.writeMessage(this.getClass().getName(), "Unexpected error");
			e.printStackTrace();
			Assert.fail(CommonUtil.appendErrorCodeToMessage("Error while attempting to find element: " + locator));
		} catch (InvocationTargetException e) {
			Log.writeMessage(LogLevel.WARN, "Invocation Exception: ");
			Log.printStackTrace(e.getCause());
		}
		return null;
	}

	/**
	 * Find a list of child elements under the current element. This method
	 * provides a default delay of up to 5 seconds to find the child elements.
	 * 
	 * @param findByType
	 *            The locator strategy
	 * @param locator
	 *            The locator string for the child elements (should be relative
	 *            to the parent element)
	 * @param controlType
	 *            The subclass of {@link BaseControl} for the elements in the
	 *            returned list
	 * 
	 * @return MSElementList containing the child elements found, or null if the
	 *         parent element cannot be found
	 */
	public <T extends BaseControl> MSElementList<T> getChildList(String findByType, String locator,
			Class<T> controlType) {
		int delay = 0;
		int timeout = 5;
		while (!SeleniumWrapper.isElementAvailable(m_webElement, findByType, locator)) {
			SeleniumWrapper.sleep();
			if (delay++ > timeout) {
				Log.writeMessage(LogLevel.WARN, "Unable to find the expected child list");
				return null;
			}
		}
		return new MSElementList<T>(m_webElement, findByType, locator, controlType);
	}

	public <T extends BaseControl> T isChild(String findByType, String locator, Class<T> controlType) {
		try {
			if (webElement().findElements(createBy(findByType, locator)).size() == 0) {
				return null;
			}
			return controlType.getConstructor(WebElement.class, String.class, String.class).newInstance(webElement(),
					findByType, locator);
		} catch (NoSuchMethodException | SecurityException e) {
			Log.writeMessage(this.getClass().getName(), "Unexpected error");
		} catch (InvocationTargetException e) {
			// Not an error per se, WebDriver couldn't find a match
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) {
			Log.writeMessage(this.getClass().getName(), "Unexpected error");
		}
		return null;
	}

	public boolean hasChild(String findByType, String locator) {
		try {
			return !this.isChild(findByType, locator, MSGenericElement.class).equals(null);
		} catch (Exception e) {
			return false;
		}
	}

	// ------------------------------------------------------------------------------------------------------------------
	// Protected Methods

	/**
	 * Click WebElement
	 * 
	 * @param by
	 * @param text
	 */
	protected void clickWebElement(By by, String text) {
		clickOnWebElement(m_webElement, by, text, "value");
	}

	protected void clickWebElement(By by, String text, String attribute) {
		clickOnWebElement(m_webElement, by, text, attribute);
	}

	/**
	 * Click on web element
	 * 
	 * @param subChildelement
	 * @param by
	 * @param text
	 * @param attribute
	 */
	protected void clickWebElement(WebElement subChildelement, By by, String text, String attribute) {
		clickOnWebElement(subChildelement, by, text, attribute);
	}

	/**
	 * Get child WebElements
	 * 
	 * @param by
	 * @return
	 */
	protected List<WebElement> getChildElements(By by) {
		return m_webElement.findElements(by);
	}

	/**
	 * Get Child WebElement
	 * 
	 * @param by
	 * @return
	 */
	protected WebElement getChildElement(By by) {
		WebElement element = null;
		try {
			element = m_webElement.findElement(by);
			if (element != null) {
				return element;
			}
		} catch (Exception e) {
			element = SeleniumWrapper.webDriver().findElement(by);
		}
		return element;
	}

	/**
	 * Get Child WebElement
	 * 
	 * @param by
	 * @return
	 */
	protected WebElement getChildElement(WebElement webElement, By by) {
		return webElement.findElement(by);
	}

	/**
	 * Get SubChildElements
	 * 
	 * @param subChildelement
	 * @param by
	 * @return
	 */
	protected List<WebElement> getChildElements(WebElement subChildelement, By by) {
		List<WebElement> childElements = subChildelement.findElements(by);
		if (childElements.size() > 0) {
			// System.out.println("element from child");
			return childElements;
		} else {
			// System.out.println("element from driver");
			return SeleniumWrapper.webDriver().findElements(by);
		}
	}

	/**
	 * Gets parent element. Based on child element finds parent element
	 * 
	 * @return
	 */
	protected WebElement getParentElement() {
		return getParentElement(m_webElement);
	}

	/**
	 * Gets parent element. Based on child element finds parent element.
	 * 
	 * @param webElement
	 * @return
	 */
	protected WebElement getParentElement(WebElement webElement) {
		return webElement.findElement(By.xpath(".."));
	}

	/**
	 * Create By
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @return
	 */
	protected static By createBy(String findByType, String valueToFindElement) {
		switch (findByType) {
		case FindByTypeConstants.CLASS_NAME:
			// waitForElementToStaleAndRecreate(By.className(valueToFindElement));
			return By.className(valueToFindElement);
		case FindByTypeConstants.CSS_SELECTOR:
			// waitForElementToStaleAndRecreate(By.cssSelector(valueToFindElement));
			return By.cssSelector(valueToFindElement);
		case FindByTypeConstants.ID:
			// waitForElementToStaleAndRecreate(By.id(valueToFindElement));
			return By.id(valueToFindElement);
		case FindByTypeConstants.LINK_TEXT:
			// waitForElementToStaleAndRecreate(By.linkText(valueToFindElement));
			return By.linkText(valueToFindElement);
		case FindByTypeConstants.NAME:
			// waitForElementToStaleAndRecreate(By.name(valueToFindElement));
			return By.name(valueToFindElement);
		case FindByTypeConstants.PARTIAL_LINK_TEXT:
			// waitForElementToStaleAndRecreate(By
			// .partialLinkText(valueToFindElement));
			return By.partialLinkText(valueToFindElement);
		case FindByTypeConstants.TAG_NAME:
			// waitForElementToStaleAndRecreate(By.tagName(valueToFindElement));
			return By.tagName(valueToFindElement);
		case FindByTypeConstants.XPATH:
			// waitForElementToStaleAndRecreate(By.xpath(valueToFindElement));
			return By.xpath(valueToFindElement);
		}
		return null;
	}

	// /**
	// * waiting for element to be stale and re-create
	// */
	// private static void waitForElementToStaleAndRecreate(By byValue) {
	// boolean isExceptionCaught = true; // Default assuming exception true
	// for (int i = 0; i < 30 && isExceptionCaught; i++) {
	// try {
	// SeleniumWrapper.webDriver().findElement(byValue);
	// isExceptionCaught = false;
	// break;
	// } catch (NoSuchElementException | StaleElementReferenceException e) {
	// SeleniumWrapper.sleep();
	// isExceptionCaught = true;
	// }
	// }
	// }

	/**
	 * Click on WebElement
	 * 
	 * @param subChildelement
	 * @param by
	 * @param text
	 * @param attribute
	 */
	private void clickOnWebElement(WebElement subChildelement, By by, String text, String attribute) {
		List<WebElement> elements = getChildElements(subChildelement, by);
		// System.out.println(elements.size());
		for (WebElement element : elements) {
			SeleniumWrapper.scrollIntoView(element);
			String temp = attribute == "text" ? element.getText() : element.getAttribute(attribute);
			// System.out.println(temp);
			if (temp.trim().toLowerCase().equals(text.trim().toLowerCase())) {
				element.click();
				return;
			}
		}
	}

	/**
	 * Click on Multiple web elements
	 * 
	 * @param subChildelement
	 * @param by
	 * @param text
	 * @param attribute
	 */
	protected void clickOnMultipleWebElement(WebElement subChildelement, By by, String text[], String attribute) {
		List<WebElement> elements = getChildElements(subChildelement, by);
		// System.out.println(elements.size());
		for (int i = 0; i <= text.length - 1; i++) {
			for (WebElement element : elements) {
				String temp = attribute == "text" ? element.getText() : element.getAttribute(attribute);
				if (temp.trim().toLowerCase().equals(text[i].trim().toLowerCase())) {
					element.click();
					break;

				}
			}
		}
	}

	/**
	 * Click on multiple web elements and returns selected items
	 * 
	 * @param subChildelement
	 * @param by
	 * @param itemsCount
	 * @param attribute
	 * @return
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
			element.click();
			selectedItems.add(temp);
			i++;
		}
		return selectedItems.toArray(new String[selectedItems.size()]);
	}

	// private final static String CLASS_NAME = BaseControl.class.getName();
	protected void click(WebElement element) {
		if (element.getAttribute("id").length() != 0) {
			System.out.println(element.getAttribute("id"));
			System.out
					.println(String.format("window.document.getElementById('%s').click()", element.getAttribute("id")));
			SeleniumWrapper.getJavascriptExecutor().executeScript(
					String.format("window.document.getElementById('%s').click()", element.getAttribute("id")));
		} else if (element.getAttribute("class").length() != 0) {
			String value = String.format(
					"var elements = window.document.getElementsByClassName('%s'); for (var i = 0; i<elements.length; i++) { var element = elements[i]; if (element.value == '%s') { element.click(); } }",
					element.getAttribute("class"), element.getAttribute("value"));
			// System.out.println(value);
			SeleniumWrapper.getJavascriptExecutor().executeScript(value);
		}
	}

	/**
	 * Gets Object from execute script
	 */
	protected Object getObject(String script) {
		try {
			return SeleniumWrapper.getJavascriptExecutor().executeScript(script);
		} catch (Exception e) {
			Log.writeMessage(BaseControl.class.getName(), e.toString());
		}
		return null;
	}
}
