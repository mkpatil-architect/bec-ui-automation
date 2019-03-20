

package com.benchmark.framework.ui.controls;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;
import com.benchmark.framework.ui.SeleniumWrapper;

public class MSElementList<T extends BaseControl> extends BaseControl implements
		Iterable<T> {
	private List<WebElement> elementList = new ArrayList<WebElement>();
	private List<T> data = new ArrayList<T>();

	public MSElementList(String xpath, Class<T> clazz) {
		this(FindByTypeConstants.XPATH, xpath, clazz);
	}

	public MSElementList(String findByType, String locator, Class<T> clazz) {
		try {
			this.m_webElement = SeleniumWrapper.webDriver().findElement(
					createBy(findByType, locator));
		} catch (NoSuchElementException e) {
			this.m_webElement = null;
			return;
		}

		elementList = SeleniumWrapper.webDriver().findElements(
				createBy(findByType, locator));
		initializeList(findByType, locator, clazz);
	}

	public MSElementList(WebElement parent, String findByType, String locator, Class<T> clazz) {
		try {
			this.m_webElement = parent.findElement(createBy(findByType, locator));
		} catch (NoSuchElementException e) {
			this.m_webElement = null;
			return;
		}

		elementList = parent.findElements(createBy(findByType, locator));
		initializeList(findByType, locator, clazz);
	}

	public MSElementList(BaseControl parent, String findByType, String locator, Class<T> clazz) {
		try {
			this.m_webElement = parent.m_webElement.findElement(createBy(findByType, locator));
		} catch (NoSuchElementException e) {
			this.m_webElement = null;
			return;
		}

		elementList = parent.m_webElement.findElements(createBy(findByType, locator));
		initializeList(findByType, locator, clazz);
	}

	/**
	 * Build a derived sublist from an existing list
	 * See {@link ArrayList#subList(int, int)} for information on defining
	 * the range values
	 *
	 * @param source
	 * @param start
	 * @param end
	 */
	private MSElementList(MSElementList<T> source, int start, int end) {
		this.m_webElement = source.m_webElement;
		// Cap the ending index at the end of the list
		if (end > source.elementList.size()) {
			end = source.elementList.size();
		}
		// Cap the starting index at the start of the list
		if (start < 0) start = 0;
		try {
			this.elementList = source.elementList.subList(start, end);
			this.data = source.data.subList(start, end);
			Log.writeMessage(LogLevel.DEBUG, "Original size of list: " + source.data.size());
			Log.writeMessage(LogLevel.DEBUG, "Length of trimmed list: " + data.size());
		} catch (IndexOutOfBoundsException e) {
			elementList = new ArrayList<WebElement>();
		}
	}

	private void initializeList(String findByType, String locator, Class<T> clazz) {
		if (elementList.isEmpty())
			return;
		for (WebElement item : elementList) {
			try {
				T temp = clazz.getConstructor(String.class, String.class)
						.newInstance("XPATH", "//body");
				temp.m_webElement = item;
				data.add(temp);
			} catch (InstantiationException | IllegalAccessException e) {
				Log.writeMessage(this.getClass().getName(), "Unexpected error");
				e.printStackTrace();
			} catch (IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				Log.writeMessage(this.getClass().getName(), "Unexpected error");
				e.printStackTrace();
			}
		}
	}

	public int size() {
		return elementList.size();
	}

	/**
	 * Returns a single element from the list, selected by a 0-based index value
	 * @param index The 0-based index value of the element to be selected
	 * @return An element stored in the list
	 * @throws IndexOutOfBoundsException Exceptions from
	 * {@link java.util.List#get(int) java.util.List.get()} will bubble up 
	 * through this method
	 * @author Zach Steele
	 */
	public T getElement(int index) {
		return data.get(index);
	}

	public MSElementList<T> subList(int start, int end) {
		return new MSElementList<T>(this, start, end);
	}

	/**
	 * Extract the text values from all elements in the list
	 *
	 * @return
	 */
	public List<String> getAllTextValues() {
		List<String> textValues = new ArrayList<String>();
		for (T temp : data) {
			textValues.add(temp.getText());
		}
		return textValues;
	}

	@Override
	public String getText() {
		return getAllTextValues().toString();
	}

	@Override
	public Iterator<T> iterator() {
		// NOTE: it may be useful to consider moving the loop generating the
		// iterator here
		// so the list is automatically updated when the test attempts to access
		// the elements
		return data.iterator();
	}
}
