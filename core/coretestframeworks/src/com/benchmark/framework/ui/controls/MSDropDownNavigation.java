

package com.benchmark.framework.ui.controls;

import static com.benchmark.core.constants.FindByTypeConstants.CSS_SELECTOR;

import java.util.List;

import org.openqa.selenium.WebElement;

import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;
import com.benchmark.framework.ui.SeleniumWrapper;

/**
 * MSDropDownNavigation, DropDownList navigation control
 */
public class MSDropDownNavigation extends BaseControl {

	/**
	 * Constructor
	 */
	public MSDropDownNavigation(String headerFindByType,
			String headerFindByValue, String headerChildFindByType,
			String headerChildFindByValue, String navFindByType,
			String navFindByValue, String childFindByType,
			String childFindByValue, String selectedFindByType,
			String selectedFindByValue, String listParentFindByType,
			String listParentFindByValue, String listChildFindByType,
			String listChildFindByValue) {
		super(headerFindByType, headerFindByValue);
		m_headerByType = headerChildFindByType;
		m_headerByValue = headerChildFindByValue;
		m_navFindByType = navFindByType;
		m_navFindByValue = navFindByValue;
		m_childFindByType = childFindByType;
		m_childFindByValue = childFindByValue;
		m_selectedFindByType = selectedFindByType;
		m_selectedFindByValue = selectedFindByValue;
		m_listParentFindByType = listParentFindByType;
		m_listParentFindByValue = listParentFindByValue;
		m_listChildFindByType = listChildFindByType;
		m_listChildFindByValue = listChildFindByValue;
	}

	// ------------------------------------------------------------------------------------------
	// Operations

	/**
	 * Method to select the navigation tab
	 * 
	 * @param nav
	 *            -> Navigation
	 * @return -> True/False
	 */
	public boolean select(String nav) {
		try {
			getNavigationChild().click();
			List<WebElement> elements = getList();
			for (WebElement element : elements) {
				String value = element.getAttribute("innerText").trim();
				if (value.contains(nav)) {
					boolean isSelected = false;
					element.findElement(createBy(CSS_SELECTOR, "a")).click();
					while(SeleniumWrapper.isElementAvailable(
							FindByTypeConstants.CSS_SELECTOR, "div.MS-Loader"));
					//TODO: Move Helper.waitTillScreenBlockerClose() into an
					// appropriate class in CoreTestFrameworks so we have the
					// ability to perform dynamic waits in component objects
					SeleniumWrapper.sleep(2000);
					String selectedValue = getSelectedTab();
					if (selectedValue.contains(nav)) {
						isSelected = true;
					}
					return isSelected;
				}
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, "", e.toString());
		}
		return false;
	}

	/**
	 * Method to get selected tab.
	 * 
	 * @return -> Selected Tab
	 */
	public String getSelectedTab() {
		return getSelectedChild().getText();
	}

	// ------------------------------------------------------------------------------------------
	// Private Methods and Variables

	/**
	 * Gets Navigation Control
	 */
	private WebElement getNavigation() {
		WebElement header = webElement().findElement(
				createBy(m_headerByType, m_headerByValue));
		return header.findElement(createBy(m_navFindByType, m_navFindByValue));
	}

	/**
	 * Gets Navigation Child Control
	 */
	private WebElement getNavigationChild() {
		return getNavigation().findElement(
				createBy(m_childFindByType, m_childFindByValue));
	}

	/**
	 * Gets Selected Tab Control
	 */
	private WebElement getSelectedChild() {
		return getNavigationChild().findElement(
				createBy(m_selectedFindByType, m_selectedFindByValue));
	}

	/**
	 * Gets UL Control
	 */
	private WebElement getUL() {
		return getNavigationChild().findElement(
				createBy(m_listParentFindByType, m_listParentFindByValue));
	}

	/**
	 * Gets List Control
	 */
	private List<WebElement> getList() {
		return getUL().findElements(
				createBy(m_listChildFindByType, m_listChildFindByValue));
	}

	private String m_headerByType = null;
	private String m_headerByValue = null;
	private String m_navFindByType = null;
	private String m_navFindByValue = null;
	private String m_childFindByType = null;
	private String m_childFindByValue = null;
	private String m_selectedFindByType = null;
	private String m_selectedFindByValue = null;
	private String m_listParentFindByType = null;
	private String m_listParentFindByValue = null;
	private String m_listChildFindByType = null;
	private String m_listChildFindByValue = null;

}
