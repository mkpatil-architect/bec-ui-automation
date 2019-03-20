
package com.benchmark.pages.elements;

import java.util.ArrayList;
import java.util.List;


import org.testng.Assert;

import com.benchmark.common.Global;
import com.benchmark.common.Helper;
import com.benchmark.constants.ResourceConstants;
import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.framework.ui.SeleniumWrapper;
import com.benchmark.framework.ui.controls.MSGenericElement;

/**
 * HomePageElements - Class which contains all Home page related to elements
 */
public class HomePageElements {

	// ------------------------------------------------------------------------------------
	// Public Element Operations

	/**
	 * Gets OpportunityAlert box message
	 */
	public String getOpportunityAlertMsg() {
		return opportunityAlert().getElement(FindByTypeConstants.TAG_NAME, "p").getText();
	}

	/**
	 * Gets Projected Value
	 */
	public String getProjected() {
		return getRecommendedOrProjected("chart-projected");
	}

	/**
	 * Gets Recommended Value
	 */
	public String getRecommended() {
		return getRecommendedOrProjected("chart-recommended");
	}

	/**
	 * Gets YourPlan Value
	 */
	public String getYourPlan() {
		MSGenericElement yourPlan = new MSGenericElement(FindByTypeConstants.CSS_SELECTOR,
				".info-left .chart-workspace h2");
		return yourPlan.getAttributeValue("innerText").trim();
	}

	/**
	 * Gets Calculated Projected value
	 */
	public String getCalculatedProjected() {
		MSGenericElement yourPlan = new MSGenericElement(FindByTypeConstants.CSS_SELECTOR,
				".info-left .chart-projected h2");
		return yourPlan.getAttributeValue("innerText").trim();
	}

	/**
	 * Gets Calculated Lift value
	 */
	public String getCalculatedLift() {
		MSGenericElement yourPlan = new MSGenericElement(FindByTypeConstants.CSS_SELECTOR,
				".info-right.calculated-info h2");
		return yourPlan.getAttributeValue("innerText").trim();
	}

	/**
	 * Gets Opportunity Value
	 */
	public String getOpportunityValue() {
		MSGenericElement element = opportunityAlert().getElement(FindByTypeConstants.CSS_SELECTOR, "p");
		return element.getAttributeValue("innerText").trim();
	}

	/**
	 * Gets Revenue Lift
	 */
	public String getRevenueLift() {
		return revenueLift().getAttributeValue("innerText");
	}

	/**
	 * Gets Visit Success Center is available or not
	 */
	public boolean isVisitSuccessCenter() {
		return Helper.isElementAvailable(FindByTypeConstants.CSS_SELECTOR, "footer.footer a.btn");
	}

	/**
	 * Method to get selected profile is Pre-calculated or post-calculated
	 * 
	 * @return True - Post-Calculated, False - Pre-Calculated
	 */
	public boolean isSelectedProfilePostCalculated() {
		String text = viewRecommendationOrReviewEdit().getText().replace("arrow_forward", "").trim().toLowerCase();
		String viewRecommendationText = Global.getResourceValue(ResourceConstants.OPALERT_VIEWRECOMMENDATION).trim()
				.toLowerCase();
		String editReviewText = Global.getResourceValue(ResourceConstants.OPALERT_REVIEWEDIT).trim().toLowerCase();
		if (text.equals(viewRecommendationText)) {
			return false;
		} else if (text.equals(editReviewText.trim().toLowerCase())) {
			return true;
		} else {
			Assert.assertTrue(false, "ViewRecommendation or Edit&Review links are not visible.");
			return false;
		}
	}

	/**
	 * Click On Recommendation Card Element
	 * 
	 * @return -> True/False
	 */
	public boolean getRecommendationcardLinks(String index) {
		try {
			getRecommendationsCard().mouseOver();
			MSGenericElement link = new MSGenericElement(FindByTypeConstants.CSS_SELECTOR,
					String.format("#recommendation .cardRevealContent .reportListItem:nth-child(%s) i", index));
			link.isVisible();
			return true;
		} catch (Exception e) {
			Helper.appendErrorMessage("Failed while clicking Recommendation Links of HomePage Recommendation card.", e);
		}
		return false;
	}

	/**
	 * Click On Recommendation Card Element
	 * 
	 * @return -> True/False
	 */
	public boolean clickOnRecommendationCardElements(String index) {
		try {
			getRecommendationsCard().mouseOver();
			Helper.sleep(30000);
			if (Helper.isElementAvailable(FindByTypeConstants.CSS_SELECTOR,
					"#recommendation .cardRevealContent .reportListItem")) {
				MSGenericElement link = new MSGenericElement(FindByTypeConstants.CSS_SELECTOR,
						String.format("#recommendation .cardRevealContent .reportListItem:nth-child(%s) i", index));
				link.click();
				return true;
			} else {
				Helper.skipTestCase("Get Recommendation card disabled for selected profile");
			}
		} catch (Exception e) {
			Helper.appendErrorMessage("Failed while clicking Recommendation Links of HomePage Recommendation card.", e);
		}
		return false;
	}

	/**
	 * Click On Attribution Card Element
	 * 
	 * @return -> True/False
	 */
	public boolean clickOnAttributionCardElements(String index) {
		try {
			getAttributionCard().mouseOver();
			MSGenericElement link = new MSGenericElement(FindByTypeConstants.CSS_SELECTOR,
					String.format("#report .cardRevealContent .reportListItem:nth-child(%s) i", index));
			link.click();
			return true;
		} catch (Exception e) {
			Helper.appendErrorMessage("Failed while clicking on Attribution Link on Atrribution Card.", e);
		}
		return false;
	}

	/**
	 * Click On Customer Journey Card Element
	 * 
	 * @return -> True/False
	 */
	public boolean clickOnCustomerJourneyCardElements(String index) {
		try {
			getCustomerJourneyCard().mouseOver();
			MSGenericElement link = new MSGenericElement(FindByTypeConstants.CSS_SELECTOR,
					String.format("#insights .cardRevealContent .reportListItem:nth-child(%s) i", index));
			link.click();
			return true;
		} catch (Exception e) {
			Helper.appendErrorMessage("Failed while clicking on Customer Journey Link on Atrribution Card.", e);
		}
		return false;
	}

	/**
	 * Gets Attribution or Customer Journey Reports
	 */
	public List<String> getAttributionOrCustomerJourneyLinks() {
		try {
			MSGenericElement parent = getAttributionOrCustomerJourneyPanel();
			if (parent != null) {
				int count = parent.getElementsCountFromParent(FindByTypeConstants.CSS_SELECTOR,
						".horizontal-nav .inner-with-max-min-width span span");
				List<String> reports = new ArrayList<String>();
				for (int i = 1; i <= count; i++) {
					{
						Helper.sleep(3000);
						MSGenericElement element = parent.getElement(FindByTypeConstants.CSS_SELECTOR,
								String.format(".horizontal-nav div:nth-of-type(1)  > span:nth-child(%d) > span", i));
						reports.add(element.getText().trim());
					}
				}
				return reports;
			}
		} catch (Exception e) {
			Helper.appendErrorMessage("Failed - getAttributionOrCustomerJourneyLinks", e);
		}
		return null;
	}

	/**
	 * Method to get attribution or customer journey panel
	 */
	private MSGenericElement getAttributionOrCustomerJourneyPanel() {
		if (SeleniumWrapper.checkElementVisible(FindByTypeConstants.CSS_SELECTOR,
				".horizontal-nav .inner-with-max-min-width")) {
			return new MSGenericElement(FindByTypeConstants.CSS_SELECTOR, ".horizontal-nav .inner-with-max-min-width");
		}
		return null;
	}
	// ------------------------------------------------------------------------------------
	// Public Elements

	/**
	 * Gets RevenueLift element
	 */
	public MSGenericElement revenueLift() {
		return opportunityAlertRightInfo().getElement(FindByTypeConstants.CSS_SELECTOR,
				".home-chart-info.pull-left h2");
	}
public MSGenericElement citrixLogo() {
	return new  MSGenericElement(FindByTypeConstants.XPATH,"//img[contains(@alt,'Citrix Ready logo')]");
}
	/**
	 * Gets OppurtunityAlert Image
	 */
	public MSGenericElement opportunityAlertIcon() {
		return opportunityAlert().getElement(FindByTypeConstants.CSS_SELECTOR, "div[class = 'chart-title-icon']");
	}

	/**
	 * Gets View Recommendation
	 */
	public MSGenericElement viewRecommendationOrReviewEdit() {
		return opportunityAlert().getElement(FindByTypeConstants.ID, "homeChartLink");
	}

	/**
	 * Gets Visit Success Center
	 */
	public MSGenericElement visitSuccessCenter() {
		return new MSGenericElement(FindByTypeConstants.CSS_SELECTOR, "footer.footer a.btn");
	}

	// ----------------------------------------------------------------------------------------------------------------------
	// Private Elements

	/**
	 * Gets Recommended Or Projected Value
	 */
	private String getRecommendedOrProjected(String findByValue) {
		MSGenericElement chartRecom = opportunityAlertLeftInfo().getElement(FindByTypeConstants.CSS_SELECTOR,
				String.format("div[class = '%s']", findByValue));
		MSGenericElement recommended = chartRecom.getElement(FindByTypeConstants.CSS_SELECTOR, "h2");
		return recommended.getAttributeValue("innerText");
	}

	/**
	 * Gets Opportunity Alert LeftInfo
	 */
	private MSGenericElement opportunityAlertLeftInfo() {
		return opportunityAlert().getElement(FindByTypeConstants.CSS_SELECTOR, "div[class = 'info-left']");
	}

	/**
	 * Gets Opportunity Alert RightInfo
	 */
	private MSGenericElement opportunityAlertRightInfo() {
		return opportunityAlert().getElement(FindByTypeConstants.CSS_SELECTOR, "div[class ^= 'info-right']");
	}

	/**
	 * Get Opportunity Alert Parent Control
	 */
	private MSGenericElement opportunityAlert() {
		if (Helper.waitTillElementAvailable(FindByTypeConstants.CSS_SELECTOR,
				"div[class = 'home-chart-info pull-left']"))
			return new MSGenericElement(FindByTypeConstants.CSS_SELECTOR, "div[class = 'home-chart-info pull-left']");
		else {
			Helper.appendErrorMessage("Element for oppurtunity alert not found in trendchart...!!");
			return null;
		}
	}

	/**
	 * Gets Recommendation Card
	 */
	public MSGenericElement getRecommendationsCard() {
		return new MSGenericElement(FindByTypeConstants.CSS_SELECTOR, "#recommendation h3");
	}

	/**
	 * Gets Recommendation Card
	 */
	public MSGenericElement getAttributionCard() {
		return new MSGenericElement(FindByTypeConstants.CSS_SELECTOR, "#report h3");
	}

	/**
	 * Gets Recommendation Card
	 */
	public MSGenericElement getCustomerJourneyCard() {
		return new MSGenericElement(FindByTypeConstants.CSS_SELECTOR, "#insights h3");
	}

}
