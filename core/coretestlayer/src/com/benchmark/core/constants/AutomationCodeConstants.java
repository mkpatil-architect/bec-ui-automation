
package com.benchmark.core.constants;

public final class AutomationCodeConstants {
	/**
	 * Constructor
	 */
	public AutomationCodeConstants() {
		super();
	}

	public AutomationCodeConstants(String code) {
		this.code = code;

	}

	/**
	 * Gets instance of AutomationCodeConstants class.
	 */
	static AutomationCodeConstants instance = null;

	public static AutomationCodeConstants instance() {
		if (instance == null) {
			synchronized (AutomationCodeConstants.class) {
				if (instance == null) {
					instance = new AutomationCodeConstants();
				}
			}
		}
		return instance;
	}

	/**
	 * Enumeration type which holds Logging automation code strings which are used
	 * on application
	 *
	 */
	public enum AutomationCodeStrings {
		SYSTEM_ERROR, CODE_NOT_IMPLEMENTED, API_UNKNOWN_EXCEPTION, AUTO_BAD_REQUEST, UNAUTHORIZED_ACCESS,
		RESOURCE_NOT_FOUND, AUTO_UNHANDLED_EXCEPTION, RESOURCE_NOT_LOADED, DB_CONNECTION_FAILURE, AUTO_DATA_MISMATCH,
		AUTO_SKIPPED_EXCEPTION, ELEMENT_NOT_FOUND, KNOWN_FAILURES, DEPENDENT_FAILURE, BASIC_CHECK
	}

	public String code = null;
}
