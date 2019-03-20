

package com.benchmarkpages.operations;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import com.benchmark.common.Global;
import com.benchmark.common.Helper;
import com.benchmark.constants.ResourceConstants;
import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;
import com.benchmark.framework.ui.SeleniumWrapper;
import com.benchmark.framework.ui.controls.MSGenericElement;
import com.benchmark.managers.Pages;
import com.benchmark.pages.elements.HomePageElements;

/**
 * Class which contains all operations related to HomePage
 */
public class HomePageOperations extends BasePageOperations {

	// ------------------------------------------------------------------------------------------------------
	/**
	 * Constructor
	 */
	public HomePageOperations() {
		super();
		elements = new HomePageElements();
	}

	// ------------------------------------------------------------------------------------------------------
	// Home Page Validation Functions

	/**
	 * Method to navigate to Home page by click on Action Logo
	 * 
	 * @return -> True/False
	 */
	public boolean validateActionLogoNavigation() {
		try {
			List<Boolean> results = new ArrayList<Boolean>();
			results.add(navigateBackHomePageAndValidate(ResourceConstants.MENU_ATTRIBUTION));
			results.add(navigateBackHomePageAndValidate(ResourceConstants.MENU_CUSTOMER_JOURNEY));
			results.add(navigateBackHomePageAndValidate(ResourceConstants.MENU_RECOMMENDATIONS));
			clickActionLogo();
			results.add(isCurrentTab(ResourceConstants.MENU_HOME));
			return results.contains(false) ? false : true;
		} catch (Exception e) {
			Helper.appendErrorMessage(CLASSNAME, "validateActionLogoNavigation", e);
		}
		Helper.appendErrorMessage("User is not able to navigate to Home page by clicking on Action Logo.");
		return false;
	}

	/**
	 * Method to validates Home Page Controls
	 * 
	 * @return -> True/False
	 */
	public boolean validateHomePageControls() {
		try {
			String actual = SeleniumWrapper.getCurrentUrl();
		String expected = "https://benchmarkeducation.com/";
		assertEquals(actual, expected);
		return true;
		} catch (Exception e) {
			Helper.appendErrorMessage(CLASSNAME, "validateHomePageControls", e);
		}
		return false;
	}

	

	/**
	 * Mouse over method
	 */
	public void mouseOverRecommendationCard() {
		elements.getRecommendationsCard().mouseOver();
	}

	/**
	 * Method to validate profile which recommendation is selected/deselected
	 * scenario mapped
	 * 
	 */
	public boolean validateHompPage(boolean isAllDisabled, boolean isRecommendationchecked, boolean isSenario,
			boolean isRecommnedationUnChecked) {
		try {
			List<Boolean> results = new ArrayList<Boolean>();
			if (isAllDisabled == true) {
				// results.add(
				// Helper.compareTwoStrings(Global.getResourceValue(ResourceConstants.ATTRIBUTION_CARD_MESSAGE),
				// getAttribution(), "Attribution message not correct"));
				results.add(
						Helper.compareTwoStrings(Global.getResourceValue(ResourceConstants.RECOMMENDATION_CARD_MESSAGE),
								getRecommendation(), "Get Recommendation card message not correct"));
				results.add(Helper.compareTwoStrings(
						Global.getResourceValue(ResourceConstants.CUSTOMERJOURNEY_CARD_MESSAGE), getCustomerJourney(),
						"Attribution message not correct"));
			}
			if (isRecommendationchecked == true) {
				if (elements.opportunityAlertIcon().isVisible()) {
					results.add(true);
				}
				elements.getRecommendationsCard().mouseOver();
				results.add(Helper.compareTwoBooleans(true, elements.getRecommendationcardLinks("1"),
						"Create Scenario link is not present"));
				results.add(Helper.compareTwoBooleans(true, elements.getRecommendationcardLinks("2"),
						"Load Scenario Link not present"));
				results.add(Helper.compareTwoBooleans(true, elements.getRecommendationcardLinks("3"),
						"View Recommendation is not present"));
			}
			if (isSenario == true) {
				elements.getRecommendationsCard().mouseOver();
				results.add(Helper.compareTwoBooleans(true, elements.getRecommendationcardLinks("1"),
						"Create Scenario link is not present"));
				results.add(Helper.compareTwoBooleans(true, elements.getRecommendationcardLinks("2"),
						"Load scenario link not present"));
			}
			if (isRecommnedationUnChecked == true) {
				if (ValidateCjourneyAtrribution()) {
					results.add(true);
				}
			}
			return results.contains(false) ? false : true;
		} catch (Exception e) {
			Helper.appendErrorMessage(CLASSNAME, "validateHompPage", e);
		}
		return false;
	}

	/**
	 * 
	 */
	public String homecardMessageWhenDisabled(String cardType) {
		MSGenericElement homePageCard = new MSGenericElement(FindByTypeConstants.CSS_SELECTOR,
				String.format("#%s .cardRevealContent", cardType));
		String message = homePageCard.getText();
		return message;
	}

	/**
	 * Method to navigate to Home page by click on Action Logo
	 * 
	 * @return -> True/False
	 */
	public boolean ValidateCjourneyAtrribution() {
		try {
			List<Boolean> results = new ArrayList<Boolean>();
			results.add(navigateBackHomePageAndValidate(ResourceConstants.MENU_ATTRIBUTION));
			results.add(navigateBackHomePageAndValidate(ResourceConstants.MENU_CUSTOMER_JOURNEY));
			clickActionLogo();
			results.add(isCurrentTab(ResourceConstants.MENU_HOME));
			return results.contains(false) ? false : true;
		} catch (Exception e) {
			Helper.appendErrorMessage(CLASSNAME, "validateActionLogoNavigation", e);
		}
		Helper.appendErrorMessage("User is not able to navigate to Home page by clicking on Action Logo.");
		return false;
	}

	// ------------------------------------------------------------------------------------------------------
	// Home Page Operations

	

	/**
	 * Click New Scenario on HomePage cards
	 */
	public boolean clickNewScenario() {
		try {
			elements.clickOnRecommendationCardElements("1");
			Helper.log("Create New Scenario is clicked on Action Home page...");
			return Helper.waitTillElementAvailable(FindByTypeConstants.CSS_SELECTOR, "div.modal-header");
		} catch (Exception e) {
			Helper.appendErrorMessage(CLASSNAME, "clickNewScenario", e);
			Helper.appendErrorMessage("Failed while loading create scenario page");
			return false;
		}
	}

	/**
	 * Click Load Scenario on HomePage cards
	 */
	public boolean clickLoadScenario() {
		try {
			elements.clickOnRecommendationCardElements("2");
			Helper.sleep(2000);
			Helper.waitTillLoaderIconClose();
			Helper.sleep(30000);
			Helper.log("Load scenario is cliecked on Action Home Page...");
			return Helper.waitTillElementAvailable(FindByTypeConstants.CSS_SELECTOR, "div.modal.scenarioListModal.in");
		} catch (Exception e) {
			Helper.appendErrorMessage(CLASSNAME, "clickLoadScenario", e);
			Helper.appendErrorMessage("Failed while loading scenario list page");
			return false;
		}

	}

	/**
	 * Method to click View Recommendation link on Get Recommendation Card
	 */
	public boolean clickViewRecommendationOnGetRecoCard() {
		elements.clickOnRecommendationCardElements("3");
		return true;
	}

	

	/**
	 * Is View Recommendation Link available or not.
	 */
	public boolean isViewRecommendation() {
		if (!elements.isSelectedProfilePostCalculated()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Is Review & Edit Link available or not.
	 */
	public boolean isReviewEdit() {
		if (elements.isSelectedProfilePostCalculated()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets Attribution Links from Home Page Attribution Card
	 */
	public List<String> getAttributionLinks() {
		elements.getAttributionCard().mouseOver();
		return getAttributionOrCustomerJourneyLinks("report");
	}

	/**
	 * Gets Customer Journey Links from Home Page Attribution Card
	 */
	public List<String> getCustomerJourneyLinks() {
		elements.getCustomerJourneyCard().mouseOver();
		return getAttributionOrCustomerJourneyLinks("insights");
	}

	/**
	 * Gets Attribution card message when its disabled
	 */
	public String getAttribution() {
		elements.getAttributionCard().mouseOver();
		return homecardMessageWhenDisabled("report");
	}

	/**
	 * Gets Customer journey card message when disabled
	 */
	public String getCustomerJourney() {
		elements.getCustomerJourneyCard().mouseOver();
		return homecardMessageWhenDisabled("insights");
	}

	public String getRecommendation() {
		elements.getRecommendationsCard().mouseOver();
		return homecardMessageWhenDisabled("recommendation");
	}

	/**
	 * Gets recommended and projected values from home page.
	 * 
	 * @return -> Recommended, Projected and Lift Values
	 */
	public List<String> getRecommendedAndProjected() {
		List<String> values = new ArrayList<String>();
		values.add(elements.getRecommended());
		values.add(elements.getProjected());
		values.add(elements.getRevenueLift());
		return values;
	}

	/**
	 * Gets Calculated OP Alert values
	 * 
	 * @return -> YourPlan, Projected and Lift Values
	 */
	public List<String> getCalculatedOpAlertValues() {
		List<String> values = new ArrayList<String>();
		values.add(elements.getYourPlan());
		values.add(elements.getCalculatedProjected());
		values.add(elements.getCalculatedLift());
		return values;
	}

	

	/**
	 * Method to click on reports for Attribution and customer journey and
	 * verify user is navigating particular tab or not
	 */
	public boolean clickReportsInCardsAndValidate() {
		try {
			List<Boolean> results = new ArrayList<Boolean>();
			List<String> newAttributionLinks = Pages.Home().Operations.getAttributionLinks();
			String attributionReportIndex = "1";
			elements.clickOnAttributionCardElements(attributionReportIndex);
			Helper.sleep(2000);
			results.add(isCurrentTab(ResourceConstants.MENU_ATTRIBUTION));
			List<String> AttributionTabLinks = elements.getAttributionOrCustomerJourneyLinks();

			Log.writeMessage(LogLevel.INFO,
					String.format("Attribution Tab\nAttribution Links: %s", AttributionTabLinks));
			results.add(Helper.compareTwoStrings(String.valueOf(newAttributionLinks.size()),
					String.valueOf(AttributionTabLinks.size()),
					"Attribution data is not matching with home card Attribution."));
			clickActionLogo();
			Helper.sleep(2000);
			List<String> newcustomerJourneyLinks = Pages.Home().Operations.getCustomerJourneyLinks();
			String customerjourneyReportIndex = "1";
			elements.clickOnCustomerJourneyCardElements(customerjourneyReportIndex);
			Helper.sleep(2000);
			results.add(isCurrentTab(ResourceConstants.MENU_CUSTOMER_JOURNEY));
			List<String> CustomerJTabLinks = elements.getAttributionOrCustomerJourneyLinks();
			Log.writeMessage(LogLevel.INFO,
					String.format("Customer Journey Tab\nCustomerJourney Links Links: %s", CustomerJTabLinks));

			results.add(Helper.compareTwoStrings(String.valueOf(newcustomerJourneyLinks.size()),
					String.valueOf(CustomerJTabLinks.size()),
					"Customer journey data is not matching with home card Customer Journey."));
			clickActionLogo();
			Helper.sleep(4000);

			return results.contains(false) ? false : true;
		} catch (Exception e) {
			Helper.appendErrorMessage("Failed - getAttributionOrCustomerJourneyLinks", e);
		}
		return false;

	}

	// ------------------------------------------------------------------------------------------------------
	// Private Operations

	


	

	/**
	 * Method to navigate back to Home page from any page by clicking on Action
	 * Logo
	 * 
	 * @param tab
	 *            -> Ex: Global
	 *            .getResourceValue(ResourceConstants.MENU_ATTRIBUTION)
	 * @return -> True/False
	 */
	private boolean navigateBackHomePageAndValidate(String tab) {
	
		clickActionLogo();
		return Helper.compareTwoBooleans(true, isCurrentTab(ResourceConstants.MENU_HOME),
				String.format("From %s tab, user is not returned to home page after clicking Action Logo", tab));
	}

	/**
	 * Method to get Attribution or Customer Journey links
	 * 
	 * @param cardType
	 * @return -> Links
	 */
	private List<String> getAttributionOrCustomerJourneyLinks(String cardType) {
		try {
			List<String> links = new ArrayList<String>();
			MSGenericElement homePageCard = new MSGenericElement(FindByTypeConstants.CSS_SELECTOR,
					String.format("#%s .cardRevealContent", cardType));

			int count1 = homePageCard.getElementsCountFromParent(FindByTypeConstants.CSS_SELECTOR, ".reportListItem");

			for (int j = 1; j <= count1; j++) {
				if (homePageCard.isElementAvailableInControl(FindByTypeConstants.CSS_SELECTOR,
						String.format(".reportListItem:nth-child(%d) a", j))) {
					MSGenericElement childElement = homePageCard.getElement(FindByTypeConstants.CSS_SELECTOR,
							String.format(".reportListItem:nth-child(%d) a", j));
					links.add(childElement.getText().split("\n")[1].trim());
				}
			}
			if (homePageCard.isElementAvailableInControl(FindByTypeConstants.CSS_SELECTOR,
					String.format("#%s .cardRevealContent > div", cardType))) {
				MSGenericElement div = homePageCard.getElement(FindByTypeConstants.CSS_SELECTOR,
						String.format("#%s .cardRevealContent > div", cardType));
				String classValue = div.getAttributeValue("class");
				if (!classValue.trim().toLowerCase().equals("ng-hide")) {
					div.click();
					MSGenericElement moreReports = new MSGenericElement(FindByTypeConstants.CSS_SELECTOR,
							"ul.mdcSelectDropdown-dropdownList");
					int elements = moreReports.getElementsCountFromParent(FindByTypeConstants.CSS_SELECTOR,
							".mdcSelectDropdown-dropdownListItem");
					for (int i = 1; i <= elements; i++) {
						MSGenericElement element = moreReports.getElement(FindByTypeConstants.CSS_SELECTOR,
								String.format(".mdcSelectDropdown-dropdownListItem:nth-child(%d)", i));
						links.add(element.getText().split("\n")[1].trim());
					}
					div.click();
				}
			}

			return links;
		} catch (Exception e) {
			Helper.appendErrorMessage("Failed - getAttributionOrCustomerJourneyLinks", e);
		}
		return null;
	}

	
	/**
	 * Gets HomePage cards is available or not
	 */
	public boolean isHomePageCards() {
		return Helper.isElementAvailable(FindByTypeConstants.CSS_SELECTOR, "ul.cards li.card");
	}

	// ------------------------------------------------------------------------------------------------------
	// Private Variables

	private HomePageElements elements = null;
	private final static String CLASSNAME = HomePageOperations.class.getSimpleName();

	// ------------------------------------------------------------------------------------------------------

}