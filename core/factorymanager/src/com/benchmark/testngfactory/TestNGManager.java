package com.benchmark.testngfactory;

import com.benchmark.framework.testng.ExecuteTestNGTests;

public class TestNGManager {

	// Contains DataAccessLayer instance
	private static volatile ExecuteTestNGTests m_testNGInstance = null;

	/*
	 * Gets TestNG base class instance.
	 */
	public static ExecuteTestNGTests getTestNGInstance() {
		if (m_testNGInstance == null) {
			synchronized (ExecuteTestNGTests.class) {
				if (m_testNGInstance == null) {
					m_testNGInstance = new ExecuteTestNGTests();
				}
			}
		}
		return m_testNGInstance;
	}
}
