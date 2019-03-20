

package com.benchmark.managers;

import com.benchmark.pages.HomePage;

/**
 * Factory Manager class for 360 D UI automation pages.
 */
public final class Pages {

	/*
	 * Variable to hold instance of Pages class object.
	 */
	private static volatile HomePage m_homePage = null;

	/**
	 * Singleton instance will be created for Home Page class.
	 * 
	 * @return Pages Instance.
	 */
	public static HomePage Home() {
		if (m_homePage == null) {
			synchronized (HomePage.class) {
				if (m_homePage == null) {
					m_homePage = new HomePage();
				}
			}
		}
		return m_homePage;
	}

}
