

package com.benchmark.framework.ui.controls;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;
import com.benchmark.framework.ui.SeleniumWrapper;

public class MSGenericElement extends BaseControl {

	public MSGenericElement() {

	}

	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSGenericElement(String xpath) {
		super(xpath);
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSGenericElement(String findByType, String valueToFindElement) {
		super(findByType, valueToFindElement);
	}

	/**
	 * Constructor to find GenericElement from a particular control.
	 * 
	 * @param element
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSGenericElement(WebElement element, String findByType, String valueToFindElement) {
		super(element, findByType, valueToFindElement);
	}

	/**
	 * Method to get an Image control from the parent control.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @return MSIMage.
	 */
	public MSImage getImage(String findByType, String valueToFindElement) {
		return new MSImage(webElement(), findByType, valueToFindElement);
	}

	/**
	 * Get inner element from the parent control
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @return -> MSGenericElement
	 */
	public MSGenericElement getElement(String findByType, String valueToFindElement) {
		return new MSGenericElement(webElement(), findByType, valueToFindElement);
	}

	/**
	 * Method to verify element is available in control or not.
	 * 
	 * @param findByType
	 *            -> FindByType
	 * @param valueToFindElement
	 *            -> Value to find element
	 * @return -> True/False
	 */
	public boolean isElementAvailableInControl(String findByType, String valueToFindElement) {
		return isElementAvailableInControl(webElement(), findByType, valueToFindElement);
	}

	/**
	 * Method to set text.
	 * 
	 * @param text
	 *            -> Text to set
	 */
	public void setText(String text) {
		webElement().clear();
		webElement().sendKeys(text);
	}

	/**
	 * Method to set text and click enter key
	 * 
	 * @param text
	 *            -> Text to set
	 */
	public void setTextAndEnter(String text) {
		webElement().clear();
		webElement().sendKeys(text);
		webElement().sendKeys(Keys.ENTER);
	}

	/**
	 * Method to verify text in child elements
	 * 
	 * @param subChildFindByType
	 * @param subChildFindByValue
	 * @param childFindByType
	 * @param childFindByValue
	 * @param elementToFindCount
	 * @return -> True/False
	 */
	public List<String> getChildElementsListFromParentControl(String parentFindByType, String parentFindByValue,
			String subchildFindByType, String subchildFindByValue, int elementToFindCount) {
		try {
			List<String> childElementValues = new ArrayList<String>();
			List<WebElement> parentElements = webElement().findElements(createBy(parentFindByType, parentFindByValue));
			int i = 0;
			for (WebElement parentElement : parentElements) {
				if (i == elementToFindCount) {
					List<WebElement> childElements = parentElement
							.findElements(createBy(subchildFindByType, subchildFindByValue));
					for (WebElement childElement : childElements) {
						childElementValues.add(childElement.getAttribute("innerText").trim());
					}
					return childElementValues;
				} else {
					i++;
				}
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, "", e.toString());
		}
		return null;
	}

	/**
	 * Method to get child element values from parent control
	 * 
	 * @param parentFindByType
	 * @param parentFindByValue
	 * @param subchildFindByType
	 * @param subchildFindByValue
	 * @return List of values
	 */
	public List<String> getChildElementsListFromParentControl(String parentFindByType, String parentFindByValue,
			String subchildFindByType, String subchildFindByValue) {
		try {
			List<String> childElementValues = new ArrayList<String>();
			WebElement parentElement = webElement().findElement(createBy(parentFindByType, parentFindByValue));
			List<WebElement> childElements = parentElement
					.findElements(createBy(subchildFindByType, subchildFindByValue));

			for (WebElement childElement : childElements) {
				childElementValues.add(childElement.getAttribute("innerText").trim());
			}
			return childElementValues;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, "", e.toString());
		}
		return null;
	}

	/**
	 * Method to get child elements list from multiple parent control
	 * 
	 * @param parentFindByType
	 * @param parentFindByValue
	 * @param subchildFindByType
	 * @param subchildFindByValue
	 * @return list
	 */
	public List<String> getChildElementsListFromMultiParentControl(String parentFindByType, String parentFindByValue,
			String subchildFindByType, String subchildFindByValue) {
		try {
			List<String> childElementValues = new ArrayList<String>();
			List<WebElement> parentElements = webElement().findElements(createBy(parentFindByType, parentFindByValue));
			for (WebElement parentElement : parentElements) {
				List<WebElement> childElements = parentElement
						.findElements(createBy(subchildFindByType, subchildFindByValue));
				for (WebElement childElement : childElements) {
					childElementValues.add(childElement.getAttribute("innerText").trim());
				}
			}
			return childElementValues;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, "", e.toString());
		}
		return null;
	}

	/**
	 * Method to click child element in parent control
	 * 
	 * @param parentFindByType
	 * @param parentFindByValue
	 * @param subchildFindByType
	 * @param subchildFindByValue
	 * @param text
	 * @return
	 */
	public boolean clickChildElementInParentList(String parentFindByType, String parentFindByValue,
			String subchildFindByType, String subchildFindByValue, String text) {
		try {
			WebElement parentElement = webElement().findElement(createBy(parentFindByType, parentFindByValue));
			List<WebElement> childElements = parentElement
					.findElements(createBy(subchildFindByType, subchildFindByValue));
			for (WebElement childElement : childElements) {
				if (childElement.getAttribute("innerText").trim().contains(text)) {
					SeleniumWrapper.scrollIntoView(childElement);
					childElement.click();
					return true;
				}
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, "", e.toString());
		}
		return false;
	}

	/**
	 * Method to click on child element in the Parent List
	 * 
	 * @param subchildFindByType
	 * @param subchildFindByValue
	 * @param text
	 * @return -> True/False
	 */
	public boolean clickChildElementInParentList(String subchildFindByType, String subchildFindByValue, String text) {
		try {
			List<WebElement> childElements = webElement()
					.findElements(createBy(subchildFindByType, subchildFindByValue));
			for (WebElement childElement : childElements) {
				if (childElement.getAttribute("innerText").trim().contains(text)) {
					SeleniumWrapper.scrollIntoView(childElement);
					childElement.click();
					return true;
				}
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, "", e.toString());
		}
		return false;
	}

	/**
	 * Method to click child element in parent list
	 * 
	 * @param findByType
	 * @param findByValue
	 * @param index
	 * @return -> True/False
	 */
	public boolean clickChildElementInParentList(String parentFindByType, String parentFindByValue,
			String childFindByType, String childFindByValue, String elementFindByType, String elementFindByValue,
			int index) {
		try {
			WebElement parentElement = webElement().findElement(createBy(parentFindByType, parentFindByValue));
			List<WebElement> childElements = parentElement.findElements(createBy(childFindByType, childFindByValue));

			int elementsSize = childElements.size();
			if (elementsSize > 0) {
				if (index > elementsSize - 1) {
					index = 0;
				}
				WebElement webElement = childElements.get(index)
						.findElement(createBy(elementFindByType, elementFindByValue));
				SeleniumWrapper.scrollIntoView(webElement);
				webElement.click();
				return true;
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, "", e.toString());
		}
		return false;
	}

	/**
	 * Method to get Element by value from list
	 * 
	 * @param findByChildType
	 *            -> FindByType
	 * @param findByChildValue
	 *            -> Value to find element
	 * @param childValue
	 *            -> Value to select in the list
	 * @return -> MSGenericElement
	 */
	public MSGenericElement getElement(String findByChildType, String findByChildValue, String childValue) {
		try {
			List<WebElement> elements = webElement().findElements(createBy(findByChildType, findByChildValue));
			for (WebElement element : elements) {
				String value = element.getAttribute("innerText").trim();
				if (value.equals(childValue)) {
					MSGenericElement finalElement = new MSGenericElement(webElement(), FindByTypeConstants.CLASS_NAME,
							element.getAttribute("class"));
					return finalElement;
				}
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, "", e.toString());
		}
		return null;
	}

	/**
	 * Method to HTML Table
	 * 
	 * @param findByType
	 * @param findByValue
	 * @return -> MSHTMLTABLE
	 */
	public MSHtmlTable getHtmlTable(String findByType, String findByValue) {
		return new MSHtmlTable(webElement(), findByType, findByValue);
	}

	/**
	 * Get Accordion Control
	 * 
	 * @param parentFindType
	 * @param parentFindValue
	 * @param childFindType
	 * @param childFindValue
	 * @param subChildFindType
	 * @param subChildFindValue
	 * @param attribute
	 * @return -> MSAccordion
	 */
	public MSAccordion getAccordion(String parentFindType, String parentFindValue, String childFindType,
			String childFindValue, String subChildFindType, String subChildFindValue, String attribute) {
		return new MSAccordion(webElement(), parentFindType, parentFindValue, childFindType, childFindValue,
				subChildFindType, subChildFindValue, attribute);
	}

	/**
	 * Method to get Element by value from list
	 * 
	 * @param findByChildType
	 *            -> FindByType
	 * @param findByChildValue
	 *            -> Value to find element
	 * @param childValue
	 *            -> Value to select in the list
	 * @return -> MSGenericElement
	 */
	public List<MSGenericElement> getElements(String findByChildType, String findByChildValue, String findSubChildType,
			String findSubChildValue) {
		try {
			List<MSGenericElement> genericElements = new ArrayList<MSGenericElement>();
			List<WebElement> elements = webElement().findElements(createBy(findByChildType, findByChildValue));
			for (WebElement element : elements) {
				SeleniumWrapper.scrollIntoView(element);
				genericElements.add(new MSGenericElement(element, findSubChildType, findSubChildValue));
			}
			return genericElements;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, "", e.toString());
		}
		return null;
	}

	/**
	 * Method to get multiple child elements list from multiple parent control
	 * 
	 * @param parentFindByType
	 * @param parentFindByValue
	 * @param subchildFindByType
	 * @param subchildFindByValue
	 * @return list
	 */
	public List<String> getMultiChildElementsListFromMultiParentControl(String parentFindByType,
			String parentFindByValue, String subchildFindByType, String subchildFindByValue) {
		try {
			List<String> childElementValues = new ArrayList<String>();
			List<WebElement> parentElements = webElement().findElements(createBy(parentFindByType, parentFindByValue));
			for (WebElement parentElement : parentElements) {
				List<WebElement> childElements = parentElement
						.findElements(createBy(subchildFindByType, subchildFindByValue));
				for (WebElement childElement : childElements) {
					if (childElement.getAttribute("innerText") == null) {
						childElementValues.add(childElement.getText().trim());
					} else {
						childElementValues.add(childElement.getAttribute("innerText").trim());
					}

				}
			}
			return childElementValues;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, "", e.toString());
		}
		return null;
	}
}
