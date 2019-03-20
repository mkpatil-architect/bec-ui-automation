

package com.benchmarkpages.operations;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.benchmark.common.Global;
import com.benchmark.common.Helper;
import com.benchmark.constants.ResourceConstants;
import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.framework.ui.SeleniumWrapper;
import com.benchmark.framework.ui.controls.MSDropDownList;
import com.benchmark.framework.ui.controls.MSGenericElement;
import com.benchmark.pages.elements.PageHeaderAndMenuElements;

/**
 * Base class which contains all common operations of all pages.
 * 
 * This class also defines all of the methods used to interact with the global
 * header banner.
 */
public class BasePageOperations {

	/**
	 * Holds page header and menu elements.
	 */
	private PageHeaderAndMenuElements elements = null;

	// ------------------------------------------------------------------------------------------------
	/**
	 * Constructor - With PageLoad verification
	 */
	public BasePageOperations() {
		// if (isPageLoaded(findByType, findByValue)) {
		// Log.writeMessage(LogLevel.INFO, pageName + " loaded successfully.");
		elements = new PageHeaderAndMenuElements();
		// } else {
		// Log.writeMessage(LogLevel.ERROR, pageName + " not loaded");
		// SeleniumWrapper.takeScreenShot();
		// Assert.assertTrue(false, pageName
		// + " not loaded. Can't proceed with test execution");
		// }
	}

	// ------------------------------------------------------------------------------------------------
	// BasePage Operations

	

	

	/**
	 

	
	/**
	 * Method to validate expected is already on.
	 * 
	 * @param tab
	 *            -> Menu
	 * @return -> True/False
	 */
	public boolean isCurrentTab(String tab) {
		String location = SeleniumWrapper.getCurrentLocation();
		switch (tab) {
		case ResourceConstants.MENU_HOME:
			return location.contains(Global.getResourceValue(ResourceConstants.MENU_HOME_LOCATION).trim());
		case ResourceConstants.MENU_RECOMMENDATIONS:
			String[] values = Global.getResourceValue(ResourceConstants.MENU_RECOMMENDATIONS_LOCATION).split("%");
			if (location.contains(values[0].trim()) || location.contains(values[1].trim())) {
				return true;
			}
		case ResourceConstants.MENU_ATTRIBUTION:
			return location.contains(Global.getResourceValue(ResourceConstants.MENU_ATTRIBUTION_LOCATION).trim());
		case ResourceConstants.MENU_CUSTOMER_JOURNEY:
			return location.contains(Global.getResourceValue(ResourceConstants.MENU_CUSTOMER_JOURNEY_LOCATION).trim());
		default:
			return false;
		}
	}

	/**
	 * Method to validate Header Controls
	 * 
	 * @return -> True/False
	 */
	protected boolean validateHeaderControls() {
		try {
			List<Boolean> results = new ArrayList<Boolean>();
			results.add(Helper.compareTwoBooleans(true, elements.citrixLogo().isVisible(),
					"citrix Logo Element not found"));
			if (elements.profiles() != null) {
				results.add(
						Helper.compareTwoBooleans(true, elements.profiles().isVisible(), "Profiles control not found"));
			}
			
			return results.contains(false) ? false : true;
		} catch (Exception e) {
			Helper.appendErrorMessage("BasePageOperations", "validateHeaderControls", e);
		}
		return false;
	}

	/**
	 * Method to check that profiles are available or not
	 * 
	 * @return -> True/False
	 */
	public boolean isProfileAvailable() {
		return getProfiles().size() > 0;
	}

	/**
	 * Gets Selected Profile
	 * 
	 * @return Display text of active profile as displayed in the dropdown
	 */
	public String selectedProfile() {
		MSGenericElement element = new MSGenericElement(FindByTypeConstants.CSS_SELECTOR,
				"div[class ^= 'option dropdown']");
		MSGenericElement selectedElement = element.getElement(FindByTypeConstants.CSS_SELECTOR,
				"a[class^='dropdown-toggle']");
		return selectedElement.getText().trim();
	}

	

	
	/**
	
	/**
	 * Click citrix Logo
	 */
	public boolean clickActionLogo() {
		elements.citrixLogo().click();
		Helper.log("Action Logo is clicked.");
		Helper.log("Navigated to Action Home Page...");
		return true;
	}

	/**
	 * Gets All Profiles
	 */
	public List<String> getAllProfiles() {
		MSDropDownList listElements = elements.profiles();
		List<String> profiles = new ArrayList<String>();
		if (listElements != null) {
			String[] values = listElements.getAllValues();
			for (String value : values) {
				profiles.add(value.trim());
			}
		}
		return profiles;
	}

	// ------------------------------------------------------------------------------------------------
	// Base Page Element Functions

	

	/**
	 * Method to get available profiles for a particular user
	 * 
	 * @return -> List of Profiles
	 */
	private List<String> getProfiles() {
		String selectedProfile = selectedProfile();
		List<String> profiles = new ArrayList<String>();
		if (elements.profiles() == null) {
			if (selectedProfile.length() > 0) {
				profiles.add(selectedProfile);
				return profiles;
			}
		} else {
			String[] values = elements.profiles().getAllValues();
			for (String value : values) {
				if (!selectedProfile.equals(value.trim())) {
					profiles.add(value.trim());
				}
			}
			if (profiles.size() == 0) {
				if (selectedProfile.length() > 0) {
					profiles.add(selectedProfile);
				}
			}
		}
		return profiles;
	}

	
	/**
	 * Is selected profile is calculated or not. Returns true if profile is a
	 * calculated else false
	 */
	public boolean isProfileCalculated() {
		String selectedProfileName = selectedProfile();
		String optionText = new MSGenericElement(FindByTypeConstants.ID, "homeChartLink")
				.getAttributeValue("innerText");
		optionText = optionText.replace("arrow_forward ", "");
		if (!optionText.trim()
				.equalsIgnoreCase(Global.getResourceValue(ResourceConstants.OPALERT_VIEWRECOMMENDATION))) {
			Helper.log("----------------------------------------------------------------------------");
			Helper.log("Calculated Profile Name -> " + selectedProfileName);
			return true;
		} else {
			Helper.log("----------------------------------------------------------------------------");
			Helper.log("Non Calculated Profile -> " + selectedProfileName);
			return false;
		}
	}

	

	/**
	 * Select profile
	 */
	public List<String> getListOfProfiles() {
		List<String> profileList = new LinkedList<>();
		if (!elements.profileDropDownOverlay().isVisible())
			elements.expandCollapseProfile().click();
		int profileCount = elements.countOfProfiles();
		for (int i = 2; i <= profileCount; i++) {
			profileList.add(elements.getProfileNameFromDropDown(i).getText());
		}
		return profileList;
	}

	/**
	 * Select Profile
	 */
	public void selectProfileBasedOnProfileName(String profileName) {
		elements.profileListElement(profileName).click();
	}

	
	

	// /**
	// * Select profile based on calculated or non-calculated.
	// *
	// * @param isCalculated
	// * -> true/false
	// * @return -> true/false
	// */
	// private boolean selectProfile(boolean isCalculated) {
	// try {
	// Helper.log(String.format("Searching for %s profile.", isCalculated ?
	// "calculated" : "non-calculated"));
	// MSDropDownList ddl = elements.profiles();
	// List<String> profiles = getAutomationProfiles(true);
	// if (profiles != null) {
	// if (profiles.size() > 0) {
	// for (String profile : profiles) {
	// String selectedProfile = selectedProfile();
	// if (profile.equals(selectedProfile)) {
	// Helper.log("Selected Profile: " + selectedProfile);
	// } else {
	// ddl.select(profile);
	//
	// Helper.waitTillPageLoad();
	// if (Helper.isElementAvailable(FindByTypeConstants.CSS_SELECTOR,
	// ".home-chart-info.pull-left")) {
	// if (isCalculated == isProfileCalculated()) {
	// if (profile.equals(selectedProfile))
	// Helper.log("Selected Profile: " + selectedProfile);
	//
	// return true;
	// }
	// }
	// }
	// }
	// }
	// }
	//
	// } catch (Exception e) {
	// Helper.appendErrorMessage("BasePageOperations", "selectCalulatedProfile",
	// e);
	// }
	// String message = String.format(
	// "%s UI Automation profiles are not available.So, can't proceed with test
	// execution.",
	// isCalculated ? "Calculated" : "Non-calculated");
	// Helper.log(message);
	// Helper.skipTestCase(message);
	// return false;
	// }

	/**
	 * Check for profile created from automation in Viewing as drop down list
	 * 
	 * @param isScenarioMaped
	 * @return
	 */
	public List<String> getAutomationProfiles(boolean isScenarioMaped) {
		try {
			List<String> profiles = getAllProfiles();
			List<String> automationProfiles = new ArrayList<String>();
			MSDropDownList ddl = elements.profiles();
			for (String profile : profiles) {
				if (profile.startsWith("UI_AUTO")) {
					if (isScenarioMaped == true) {
						String profileSelected = selectedProfile();
						if (Helper.compareTwoStrings(profile, profileSelected, "seleceted profile is not macting")) {
							if (Helper.isElementAvailable(FindByTypeConstants.CSS_SELECTOR,
									".home-chart-info.pull-left")) {
								automationProfiles.add(profile);
							} else {
								ddl.select(profile);

								if (Helper.isElementAvailable(FindByTypeConstants.CSS_SELECTOR,
										".home-chart-info.pull-left")) {
									automationProfiles.add(profile);
								}
							}

						}
					} else {
						automationProfiles.add(profile);
					}
				}
			}
			return automationProfiles.size() > 0 ? automationProfiles : null;
		} catch (Exception e) {
			Helper.appendErrorMessage("Failed while selecting automation profile.", e);
		}
		return null;
	}

	
}
