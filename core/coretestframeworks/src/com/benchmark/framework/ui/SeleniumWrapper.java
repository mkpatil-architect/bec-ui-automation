
package com.benchmark.framework.ui;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;
import com.benchmark.core.util.CommonUtil;

public class SeleniumWrapper extends BaseSelenium {

	/**
	 * Gets Session Id from cookie
	 */
	public static String getSessionIdFromCookie() {
		return getCookieValueByCookieName("authSid");
	}

	/**
	 * Opens the page in current browser based on the URL given by the user
	 * 
	 * @param url
	 */
	public static void openPage(String url) {
		webDriver().get(url);
	}

	/**
	 * Opens the page in current browser based on the URL given by the user
	 * 
	 * @param url
	 * @return
	 */
	public static String getCurrentUrl() {
		return webDriver().getCurrentUrl();
	}

	/**
	 * Method to refresh the page.
	 */
	public static void refreshPage() {
		webDriver().navigate().refresh();
	}

	/**
	 * Method to take screen shot
	 * 
	 * @return {@link File} object referencing the saved screenshot
	 * @throws WebDriverException if the Selenium Driver is unable to record a
	 *                            screenshot
	 */
	public static File takeScreenShot() {
		try {
			File screenShot = ((TakesScreenshot) webDriver()).getScreenshotAs(OutputType.FILE);
			BufferedImage source = ImageIO.read(screenShot);
			int scaledWidth = new Double(source.getWidth() * 0.75).intValue();
			int scaledHeight = new Double(source.getHeight() * 0.75).intValue();
			BufferedImage scaled = new BufferedImage(scaledWidth, scaledHeight, source.getType());
			Graphics2D converter = scaled.createGraphics();
			converter.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			converter.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			converter.drawImage(source, 0, 0, scaledWidth, scaledHeight, null);
			converter.dispose();

			File images = screenshotFolder();
			// If the screenshot folder is missing, attempt to create it
			images.mkdirs();
			if (!images.isDirectory()) {
				Log.writeMessage(LogLevel.ERROR, "",
						String.format("Unable to use '%s' as a screenshot folder", images.toString()));
			}

			int count = images.listFiles().length + 1;
			String imageName = String.format("Image_%d.png", count);
			File imageFile = new File(images, imageName);
			ImageIO.write(scaled, "png", imageFile);
			return imageFile;
		} catch (IOException ioe) {
			// Short log message to document cases where the testbed is unable
			// to save the screenshot
			Log.writeMessage(LogLevel.ERROR, "Error in File I/O while capturing screenshot" + ioe.toString());
		} catch (WebDriverException wde) {
			Log.writeMessage(LogLevel.ERROR, "WebDriverException while capturing screenshot" + wde.toString());
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, "Unrecognized exception while capturing screenshot" + e.toString());
		}
		return new File("");
	}

	/**
	 * Attempt to record a screenshot, only returning the final filename
	 *
	 * @return The name of the screenshot file, or "" if unable to save a screenshot
	 *         file
	 */
	public static String screenshotName() {
		File screenshot;
		try {
			screenshot = takeScreenShot();
		} catch (WebDriverException e) {
			Log.writeMessage(LogLevel.WARN,
					"Unexpected error while taking screenshot; " + "please inspect the test instance");
			return "";
		}
		if (null == screenshot)
			return "";
		else
			return screenshot.getName();
	}

	public static File screenshotFolder() {
		try {
			return new File(CommonUtil.getBatHomePath(), "UIIMAGES");
		} catch (NullPointerException e) {
			e.printStackTrace();
			Log.writeMessage(LogLevel.ERROR, "", "Error while building path to screenshot folder");
			return null;
		}
	}

	/**
	 * Method to switch web driver to IFrame
	 * 
	 * @param iFrameId -> IFrameId
	 */
	public static void switchToIFrame(String iFrameId) {
		webDriver().switchTo().frame(webDriver().findElement(By.id(iFrameId)));
	}

	/**
	 * Method to switch web driver to IFrame
	 * 
	 * @param iFrameId -> IFrameId
	 */
	public static void switchToIFrame(String findByType, String valueToFindElement) {
		webDriver().switchTo().frame(webDriver().findElement(createBy(findByType, valueToFindElement)));
	}

	/**
	 * Method to switch back to default content.
	 */
	public static void switchBackToDefaultContent() {
		webDriver().switchTo().defaultContent();
	}

	/**
	 * Gets Cookie value from cookies based on cookie name
	 */
	public static String getCookieValueByCookieName(String cookieName) {
		Set<Cookie> cookies = webDriver().manage().getCookies();
		if (!cookies.isEmpty()) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * Gets all available cookies.
	 * 
	 * @return -> Map<String, String>
	 */
	public static Map<String, String> getCookies() {
		Set<Cookie> cookies = webDriver().manage().getCookies();
		Map<String, String> finalCookies = new HashMap<String, String>();
		for (Cookie cookie : cookies) {
			finalCookies.put(cookie.getName(), cookie.getValue());
		}
		return finalCookies;
	}

	/**
	 * Method to get Object by executing java script.
	 * 
	 * @param findByType         -> FindByType
	 * @param valueToFindElement -> Value to find element.
	 * @param jScript            -> JavaScript
	 * @return -> True/False
	 */
	public static Object getObjectByExecutingScript(String findByType, String valueToFindElement, String jScript) {
		try {
			WebElement element = SeleniumWrapper.webDriver().findElement(createBy(findByType, valueToFindElement));
			return SeleniumWrapper.getJavascriptExecutor().executeScript(jScript, element);
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, SeleniumWrapper.class.getName(), e.toString());
		}
		return null;
	}

	/**
	 * Method will return values from flash object
	 * 
	 * @param findValueByType
	 * @param valueToFindElement
	 * @param jScript
	 * @return
	 */
	public static Object getValueFromFlashObject(String findByType, String valueToFindElement, String jScript) {
		return getObjectByExecutingScript(findByType, valueToFindElement, jScript);
	}

	/**
	 * Method will scroll until the element is in view
	 * 
	 * @param findValueByType
	 * @param valueToFindElement
	 * @return
	 */
	public static void scrollIntoView(String findByType, String valueToFindElement) {
		try {
			WebElement element = SeleniumWrapper.webDriver().findElement(createBy(findByType, valueToFindElement));
			SeleniumWrapper.getJavascriptExecutor().executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, SeleniumWrapper.class.getName(), e.toString());
		}
	}

	/**
	 * Method will scroll until the element is in view
	 * 
	 * @param WebElement
	 * @return
	 */
	public static void scrollIntoView(WebElement element) {
		try {
			// scrollIntoView causes some js dropdowns to close
			// Using window.scrollTo() instead
			SeleniumWrapper.getJavascriptExecutor().executeScript("window.scrollTo" + element.getLocation(), "");
			/*
			 * SeleniumWrapper.getJavascriptExecutor().executeScript(
			 * "arguments[0].scrollIntoView(true);", element);
			 */
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, SeleniumWrapper.class.getName(), e.toString());
		}
	}

	/**
	 * Method to get Object by executing java script.
	 * 
	 * @param jScript -> JavaScript
	 * @return -> True/False
	 */
	public static Object getObjectByExecutingScript(String jScript) {
		try {
			return SeleniumWrapper.getJavascriptExecutor().executeScript(jScript);
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, SeleniumWrapper.class.getName(), e.toString());
		}
		return null;
	}

	public static boolean clickEscape() {
		WebElement body = webDriver().findElement(By.tagName("body"));
		body.sendKeys(Keys.ESCAPE);
		return true;
	}

	public static void switchToDifferentTab(int index) {
		List<String> windows = new ArrayList<String>(webDriver().getWindowHandles());
		webDriver().switchTo().window(windows.get(index - 1));
	}
}
