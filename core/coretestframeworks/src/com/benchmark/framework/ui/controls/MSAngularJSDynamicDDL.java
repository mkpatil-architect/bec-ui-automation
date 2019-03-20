

package com.benchmark.framework.ui.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebElement;

import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;

/**
 * MSAngularJSDynamicDDL, Dynamic Angular JS DropDownList with checkbox.
 */
public class MSAngularJSDynamicDDL extends BaseControl {

	/**
	 * Constructor
	 */
	public MSAngularJSDynamicDDL(String findByTypeParentControl,
			String findByValueParentControl, String findByTypeChild,
			String findByValueChild, String findByTypeAdd,
			String findByValueAdd, String findByTypeDDLParent,
			String findByValueDDLParent, String findByTypeDDLChild,
			String findByValueDDLChild, String findByTypeButtonSec,
			String findByValueButtonSec, String findByTypeApply,
			String findByValueApply, String findByTypeCancel,
			String findByValueCancel, String findByTypeSelectedValue,
			String findByValueSelectedValue) {
		super(findByTypeParentControl, findByValueParentControl);
		m_findByTypeChild = findByTypeChild;
		m_findByValueChild = findByValueChild;
		m_findByTypeAdd = findByTypeAdd;
		m_findByValueAdd = findByValueAdd;
		m_findByTypeDDLParent = findByTypeDDLParent;
		m_findByValueDDLParent = findByValueDDLParent;
		m_findByTypeDDLChild = findByTypeDDLChild;
		m_findByValueDDLChild = findByValueDDLChild;
		m_findByTypeButtonSec = findByTypeButtonSec;
		m_findByValueButtonSec = findByValueButtonSec;
		m_findByTypeApply = findByTypeApply;
		m_findByValueApply = findByValueApply;
		m_findByTypeCancel = findByTypeCancel;
		m_findByValueCancel = findByValueCancel;
		m_findByTypeSelectedValue = findByTypeSelectedValue;
		m_findByValueSelectedValue = findByValueSelectedValue;

	}

	// -------------------------------------------------------------------------------------
	// Public Methods

	/**
	 * Method to get the values from the DropDownList
	 * 
	 * @return -> List of Values
	 */
	public List<String> getValues() {
		try {
			List<String> values = new ArrayList<String>();
			WebElement childElement = getChildElement();
			WebElement parentDDL = childElement.findElement(createBy(
					m_findByTypeDDLParent, m_findByValueDDLParent));
			List<WebElement> items = parentDDL.findElements(createBy(
					m_findByTypeDDLChild, m_findByValueDDLChild));
			for (WebElement item : items) {
				values.add(item.getAttribute("innerText")
						.replace("check_box_outline_blank", "")
						.replace("check_box", "").trim());
			}
			return values;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.toString());
		}
		return null;
	}

	/**
	 * Method to Click Add Button
	 */
	public boolean clickAdd() {
		try {
			WebElement childElement = getChildElement();
			WebElement add = childElement.findElement(createBy(m_findByTypeAdd,
					m_findByValueAdd));
			add.click();
			return true;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.toString());
		}
		return false;
	}

	/**
	 * Method to select value in DropDownList and click Apply/Cancel
	 * 
	 * @param values
	 *            -> Values
	 * @param isApply
	 *            -> True/False
	 * @return -> True/False
	 */
	public boolean selectValue(String value, boolean isApply) {
		return selectValue(Arrays.asList(new String[] { value }), isApply);
	}

	/**
	 * Method to select multiple values in DropDownList and click Apply/Cancel
	 * 
	 * @param values
	 *            -> Values
	 * @param isApply
	 *            -> True/False
	 * @return -> True/False
	 */
	public boolean selectValue(List<String> values, boolean isApply) {
		try {
			if (clickAdd()) {
				WebElement childElement = getChildElement(webElement(),
						createBy(m_findByTypeChild, m_findByValueChild));
				WebElement parentDDL = childElement.findElement(createBy(
						m_findByTypeDDLParent, m_findByValueDDLParent));
				List<WebElement> items = parentDDL.findElements(createBy(
						m_findByTypeDDLChild, m_findByValueDDLChild));

				for (String value : values) {
					for (WebElement item : items) {
						if (value.equals(item.getAttribute("innerText")
								.replace("check_box_outline_blank", "")
								.replace("check_box", "").trim())) {
							item.findElement(
									createBy(FindByTypeConstants.TAG_NAME,
											"span")).click();
							break;
						}
					}
				}
				return clickApplyOrCancel(isApply);
			} else {
				Log.writeMessage(LogLevel.ERROR, CLASS_NAME,
						"Failed while click Add button.");
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.toString());
		}
		return false;
	}

	/**
	 * Method to remove value from the selected list
	 * 
	 * @param value
	 *            -> Remove Value
	 * @return -> True/False
	 */
	public boolean removeValue(String value) {
		try {
			WebElement childElement = getChildElement(
					webElement(),
					createBy(m_findByTypeSelectedValue,
							m_findByValueSelectedValue));
			List<WebElement> childElements = childElement
					.findElements(createBy(FindByTypeConstants.TAG_NAME, "div"));
			for (WebElement innerElement : childElements) {
				WebElement span = innerElement.findElement(createBy(
						FindByTypeConstants.TAG_NAME, "span"));
				if (span.getAttribute("innerText").trim().contains(value)) {
					WebElement close = span.findElement(createBy(
							FindByTypeConstants.TAG_NAME, "i"));
					close.click();
					break;
				}
			}
			return true;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.toString());
		}
		return false;
	}

	// -------------------------------------------------------------------------------------
	// Private Methods

	/**
	 * Method to click Apply or Cancel button.
	 * 
	 * @param isApply
	 *            -> True/False
	 * @return -> True/False
	 */
	private boolean clickApplyOrCancel(boolean isApply) {
		try {
			WebElement childElement = getChildElement();
			WebElement parentDDL = childElement.findElement(createBy(
					m_findByTypeButtonSec, m_findByValueButtonSec));
			WebElement clickElement = null;
			if (isApply) {
				clickElement = parentDDL.findElement(createBy(
						m_findByTypeApply, m_findByValueApply));
			} else {
				clickElement = parentDDL.findElement(createBy(
						m_findByTypeCancel, m_findByValueCancel));
			}
			if (clickElement != null) {
				clickElement.click();
				return true;
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.toString());
		}
		return false;
	}

	/**
	 * Method to get Child element
	 */
	private WebElement getChildElement() {
		return getChildElement(webElement(),
				createBy(m_findByTypeChild, m_findByValueChild));
	}

	// -------------------------------------------------------------------------------------
	// Private Variables

	private String m_findByTypeChild = null;
	private String m_findByValueChild = null;
	private String m_findByTypeAdd = null;
	private String m_findByValueAdd = null;
	private String m_findByTypeDDLParent = null;
	private String m_findByValueDDLParent = null;
	private String m_findByTypeDDLChild = null;
	private String m_findByValueDDLChild = null;
	private String m_findByTypeApply = null;
	private String m_findByValueApply = null;
	private String m_findByTypeCancel = null;
	private String m_findByValueCancel = null;
	private String m_findByTypeSelectedValue = null;
	private String m_findByValueSelectedValue = null;
	private String m_findByTypeButtonSec = null;
	private String m_findByValueButtonSec = null;
	private final static String CLASS_NAME = MSAngularJSDynamicDDL.class
			.getSimpleName();

}
