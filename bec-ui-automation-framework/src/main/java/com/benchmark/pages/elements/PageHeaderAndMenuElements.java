

package com.benchmark.pages.elements;

import com.benchmark.common.Helper;
import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.framework.ui.controls.MSDropDownList;
import com.benchmark.framework.ui.controls.MSGenericElement;

/**
 * Class which holds the elements of Page Header and Menu control
 */
public class PageHeaderAndMenuElements {

	
	
	/**
	 * Gets UserName WebElement from header
	 */
	public MSGenericElement userName() {
		return new MSGenericElement(FindByTypeConstants.CSS_SELECTOR, ".profile.dropdown.open h5.ng-binding");
	}

	/**
	 * Gets UserEmail WebElement from header
	 */
	public MSGenericElement userEmailId() {
		return new MSGenericElement(FindByTypeConstants.CSS_SELECTOR, ".dropdown-menu>h6");
	}

	
	/**
	 

	/**
	 * Gets Profile List
	 */
	public MSDropDownList profiles() {
		if (Helper.isElementAvailable(FindByTypeConstants.CSS_SELECTOR, ".fa.fa-caret-down")) {
			return new MSDropDownList(FindByTypeConstants.CSS_SELECTOR, "div[class ^= 'option dropdown']",
					FindByTypeConstants.CSS_SELECTOR, "ul[class ='profile-list']", FindByTypeConstants.TAG_NAME, "li",
					"innerText");
		}
		return null;
	}

	

	public MSGenericElement citrixLogo() {
		return new  MSGenericElement(FindByTypeConstants.XPATH,"//img[contains(@alt,'Citrix Ready logo')]");
	}

	// ---------------------------------------------------------------------------------------------------------------
	// Need to delete the below code

	// /**
	// * Gets Admin link control
	// */
	// public MSHyperLink admin() {
	// // No attributes available to target these elements
	// return userActions().getChild(LINK_TEXT, "Action Admin",
	// MSHyperLink.class);
	// }
	//
	// /**
	// * Gets ChangePassword link control
	// */
	// public MSHtmlDiv changePassword() {
	// return new MSHtmlDiv(FindByTypeConstants.ID, "changePassword");
	// }
	//
	// /**
	// * Gets Help link control
	// */
	// public MSHyperLink help() {
	// return new MSHyperLink(FindByTypeConstants.ID, "helpLink");
	// }
	//
	// /**
	// * Gets SignOut link control
	// */
	// public MSGenericElement signOut() {
	// // String signoutUrl =
	// // Global.getAppPropertyValue(CommonConstants.SSOURL)
	// // .replace("login.jsp", "globalLogout.html");
	// return new MSGenericElement(FindByTypeConstants.CSS_SELECTOR,
	// "a[href = '" + "" + "']");
	// }
	//
	// /**
	// * Gets user account button
	// */
	// public MSGenericElement userAccount() {
	// SeleniumWrapper.waitTillElementLoaded(CSS_SELECTOR, "div.profile", 5);
	// return new MSGenericElement(CSS_SELECTOR, "div.profile");
	// }
	//
	// public MSElementList<MSHyperLink> userActions() {
	// return userAccount().getChildList(CSS_SELECTOR, "ul>li",
	// MSHyperLink.class);
	// }
	//
	// public MSHyperLink accountLink() {
	// return userAccount().getChild(CSS_SELECTOR, "a.accountLink",
	// MSHyperLink.class);
	// }
	//
	// /**
	// * Gets Navigation tab control
	// */
	// public MSNavigationTab tab() {
	// return new MSNavigationTab(FindByTypeConstants.CSS_SELECTOR,
	// "div[class = 'liveNavigationHeader']",
	// FindByTypeConstants.CSS_SELECTOR, "div[class = 'navElement']");
	// }
	//
	// // /**
	// // * Gets UserName control
	// // */
	// // public MSSpan userName() {
	// // return new MSSpan(FindByTypeConstants.ID, "dashboardWelcomeMsg");
	// // }
	//
	// public MSSpan planningTab() {
	// return new MSSpan(FindByTypeConstants.XPATH,
	// "//div[@class='navElement' and @id='planningHolder']/span");
	// }
	//
	// /**
	// * gets warning message control
	// *
	// * @return
	// */
	// public MSHtmlDiv message() {
	// return new MSHtmlDiv(FindByTypeConstants.CLASS_NAME, "msg-text");
	// }
	//
	// /**
	// * gets warning message control
	// *
	// * @return
	// */
	// public MSHtmlDiv errorMessage() {
	// return new MSHtmlDiv(FindByTypeConstants.XPATH,
	// "//div[@id='loginMsg']/span");
	// }
	//
	// /**
	// * gets warning message control
	// *
	// * @return
	// */
	// public MSHtmlDiv warningMessage() {
	// return new MSHtmlDiv(FindByTypeConstants.XPATH,
	// "//div[@class='msg-text']/p");
	// }
	//
	// /**
	// * gets get started button on the login page
	// *
	// * @return
	// */
	// public MSButton getStarted() {
	// return new MSButton(FindByTypeConstants.ID, "closeRebrandBtn");
	// }
	//
	// //
	// -------------------------------------------------------------------------------
	// // NEW UI ELEMENTS
	//
	// /**
	// * Gets App Switcher
	// */
	// public MSGenericElement appSwitcher() {
	// return header().getElement(FindByTypeConstants.CSS_SELECTOR,
	// "div[class ^= 'switchApp dropdown']");
	// }
	//
	/**
	 * Profile count element
	 */
	public int countOfProfiles() {
		return new MSGenericElement().getElementsCount(FindByTypeConstants.CSS_SELECTOR, "ul.profile-list>li");
	}

	public MSGenericElement getProfileNameFromDropDown(int Index) {
		return new MSGenericElement(FindByTypeConstants.XPATH,
				"//ul[@class='profile-list']/li[%Index%]".replaceAll("%Index%", Integer.toString(Index)));
	}

	public MSGenericElement expandCollapseProfile() {
		return new MSGenericElement(FindByTypeConstants.CSS_SELECTOR, ".option.dropdown>.dropdown-toggle");
	}

	public MSGenericElement profileDropDownOverlay() {
		return new MSGenericElement(FindByTypeConstants.CSS_SELECTOR, ".dropdown-menu>ul.profile-list");
	}

	/**
	 * Select profile by profile name
	 */
	public MSGenericElement profileListElement(String profileText) {
		if (!profileDropDownOverlay().isVisible()) {
			expandCollapseProfile().click();
			Helper.sleep(500);
		}
		return new MSGenericElement(FindByTypeConstants.XPATH,
				"//ul[@class='profile-list']/li[text()='%PName%']".replaceAll("%PName%", profileText));
	}
}
