
package com.benchmark.constants;

/**
 * Class to hold all page Elements
 */
public final class PageElements {

	/**
	 * ENUM for Common Elements across the application
	 */
	public static enum Common {
		ACTIONLOGO
	}

	/**
	 * ENUM for Home Page Elements
	 */
	public static enum Home {
		OPPORTUNITYALERTICON, REVENUELIFT, VIEWRECOMMENDATION
	}

	/**
	 * ENUM for Recommendation Page Elements
	 */
	public static enum Recommendation {
		ADDTOPRECOMMENDATIONS, ANALYZETOPRECOMMENDATION, ADDTOACTIONITEMS, CALCULATE, KEEPPROJECTSPEND, SAVEANDREVIEW, STEP2MWCANCEL, STEP2MWDISCARD, STEP2MWSAVE, STEP1, STEP2, EDIT, SHARE, DOWNLOAD, VIEWRESULTS, EMAILMWCLOSE, EMAILMWCANCEL, EMAIL, REVERT, WARNINGCANCEL, WARNINGLEAVE, FULLRECOMMENDATION, WARNINGCONTINUE, WARNINGAPPLY, DONE
	}

	/**
	 * ENUM for Scenario Page Elements
	 */
	public static enum Scenario {
		STEP1NEXT, STEP1CANCEL, STEP2NEXT, STEP2CANCEL, STEP3NEXT, STEP3CANCEL, STEP4CREATE, CREATESCENARIOLIST, STEP4EDIT, DELETESCENARIO, DUPLICATESCENARIO, SAVEDSCENARIOAPPLY
	}

	/**
	 * ENUM for FullRecommendation Page Elements
	 */
	public static enum FullRecommendation {
		CALCULATE, SAVE, REVERT, REVERTAPPLY, REVERTCANCEL
	}

	/**
	 * ENUM for Scenario Action
	 */
	public static enum ScenarioAction {
		DUPLICATE, DELETE, EDIT
	}

	/**
	 * Custom Report Action
	 */
	public static enum CUSTOMREPORTACTION {
		CANCEL, SAVE, RUNNOW
	}

	/**
	 * Report Builder FTP Options
	 */
	public static enum FTPTYPE {
		FTP, SFTP
	}
	/**
	 * Custom report list page action
	 */
	public static enum ReportAction{
		DUPLICATE,DELETE
	}
}
