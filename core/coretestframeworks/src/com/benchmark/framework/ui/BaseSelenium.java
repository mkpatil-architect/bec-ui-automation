

package com.benchmark.framework.ui;

import java.io.File;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.benchmark.core.constants.CommonConstants;
import com.benchmark.core.constants.DriverTypeConstants;
import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;
import com.benchmark.core.util.CommonUtil;
import com.benchmark.core.util.FileOperations;


/**
 * Base class for Selenium WebDriver
 */
public class BaseSelenium {

	/**
	 * Variable holds the WebDriver instance.
	 */
	protected static WebDriver m_webDriver = null;

	/**
	 * Variable to hold JavascriptExecutor which will be used to execute java
	 * scripts.
	 */
	protected static JavascriptExecutor m_jsExecutor = null;

	/**
	 * Variable to hold Action instance, which will be used to perform actions
	 * like mouse events, key up, key down, move, drag etc..
	 */
	protected static Actions m_actions = null;
	/**
	 * Variable to hold parallel grid run flag
	 */
	protected static Boolean multipleclient = false;
	/**
	 * Variable to hold node start port
	 */
	protected static String gridnodeport = null;

	// Public Methods

	/**
	 * Gets Action instance.
	 * 
	 * @return Actions
	 */
	public static Actions actions() {
		return m_actions;
	}

	/**
	 * Gets current browser name
	 * 
	 * @return current browser name
	 */
	public static String getBrowserName() {
		return (m_webDriver != null) ? getBrowserCapabilities().getBrowserName().trim() : "";
	}

	/**
	 * Gets current browser version
	 * 
	 * @return current browser version
	 */
	public static String getBrowserVersion() {
		return (m_webDriver != null) ? getBrowserCapabilities().getVersion().trim() : "";
	}

	/**
	 * Gets current location
	 * 
	 * @return current location
	 */
	public static String getCurrentLocation() {
		return m_webDriver != null ? m_webDriver.getCurrentUrl() : "";
	}

	/**
	 * Gets current page title
	 * 
	 * @return current page title
	 */
	public static String getPageTitle() {
		return m_webDriver != null ? m_webDriver.getTitle().trim() : "";
	}

	/**
	 * Get JavaScriptExecutor object
	 * 
	 * @return
	 */
	public static JavascriptExecutor getJavascriptExecutor() {
		return m_jsExecutor;
	}

	/**
	 * 
	 */
	public static void start() {
		setup();
	}

	/**
	 * Quits current driver, closing every associated window
	 */
	public static void stop() {
		if (m_webDriver != null) {
			m_webDriver.close();
			m_webDriver.quit();
		}
		if (multipleclient) {
			Log.writeMessage(LogLevel.INFO, "Stoping grid node on port " + gridnodeport);
		
			Log.writeMessage(LogLevel.INFO, "Stopped grid node on port " + gridnodeport + " successfully.!");
		}
	}

	/**
	 * Method to wait to till page loaded or element loaded.
	 * 
	 * @param elementId
	 *            -> ElementId
	 * @param counter
	 *            -> Counter
	 * @return {@code true} if the page loaded successfully, {@code false}
	 *         otherwise
	 */
	public static boolean waitToLoadPage(String elementId, int counter) {
		return attemptToLoadPage(FindByTypeConstants.ID, elementId, counter);
	}

	/**
	 * Method to wait to till screen loader is visible.
	 * 
	 * @param findByType
	 *            -> FindByType
	 * @param valueToFindElement
	 *            -> Value to find element
	 * @param counter
	 *            -> Counter
	 */
	public static void waitToLoadPage(String findByType, String valueToFindElement, int counter) {
		if (attemptToLoadPage(findByType, valueToFindElement, counter))
			return;
		SeleniumWrapper.takeScreenShot();
		String message = String.format("After waiting for %d mins also still we see screen blocker."
				+ " Can�t proceed further to execute the next test step.", TimeUnit.SECONDS.toMinutes(counter));
		Assert.fail(CommonUtil.appendErrorCodeToMessage(message));
	}

	/**
	 * Wait for the screen loader to clear. If the blocker remains visible for
	 * longer than {@code counter} seconds, flag the delay attempt as a failure.
	 * 
	 * @param findByType
	 *            Selector strategy for the screen blocker
	 * @param valueToFindElement
	 *            Locator string referencing the screen blocker
	 * @param counter
	 *            Maximum delay (in seconds) to wait for the blocker to clear
	 * @return {@code true} if the pageload succeeds, {@code false} otherwise
	 */
	public static boolean attemptToLoadPage(String findByType, String valueToFindElement, int counter) {
		By locator = createBy(findByType, valueToFindElement);
		for (int i = 1; i <= counter; i++) {
			if (isElementAvailable(findByType, valueToFindElement)) {
				try {
					WebElement element = webDriver().findElement(locator);
					String style = element.getAttribute("style");
					sleep();
					if (style.contains("display: none;")) {
						return true;
					}
				} catch (NoSuchElementException e) {
					return true;
				} catch (StaleElementReferenceException e) {
					Log.writeMessage(LogLevel.WARN,
							"Stale element exception occured while searching for '" + valueToFindElement + "'");
					sleep();
				}
			} else {
				sleep();
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to wait till page load (loader icon progress is completed)
	 * 
	 * @param parentFindType
	 * @param parentFindValue
	 * @param childFindType
	 * @param childFindValue
	 * @param counter
	 * @return -> True/False
	 */
	public static boolean waitToLoadPage(String parentFindType, String parentFindValue, String childFindType,
			String childFindValue, int counter) {
		WebElement parentElement = null;
		WebElement childElement = null;
		try {
			for (int i = 1; i <= counter; i++) {
				if (isElementAvailable(parentFindType, parentFindValue)) {
					parentElement = webDriver().findElement(createBy(parentFindType, parentFindValue));
					childElement = parentElement.findElement(createBy(childFindType, childFindValue));
					String style = childElement.getAttribute("style");
					int percentage = getPercentage(style);
					sleep();
					if (percentage == 0 || percentage == 100) {
						return true;
					}
				} else {
					sleep();
				}
				if (i == counter) {
					String message = String.format(
							"After waiting for %d mins also still we see screen blocker."
									+ " Can�t proceed further to execute the next test step.",
							TimeUnit.SECONDS.toMinutes(counter));
					Assert.fail(CommonUtil.appendErrorCodeToMessage(message));
					SeleniumWrapper.takeScreenShot();
				}
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.toString());
		}
		return false;
	}

	/**
	 * Method to wait till element loaded in particular page.
	 * 
	 * @param findByType
	 *            -> FindByType
	 * @param valueToFindElement
	 *            -> FindByValue
	 * @param counter
	 *            -> Counter.
	 */
	public static void waitTillElementLoaded(String findByType, String valueToFindElement, int counter) {
		for (int i = 1; i <= counter; i++) {
			if (isElementAvailable(findByType, valueToFindElement)) {
				return;
			}
			sleep();
		}
		// If the locator could not be found within the set time limit,
		// mark the test as 'failed'
		SeleniumWrapper.takeScreenShot();
		Assert.fail(CommonUtil.appendErrorCodeToMessage(String.format(
				"After waiting for %d mins also page/element is loaded."
						+ " Can�t proceed further to execute the next test step.",
				TimeUnit.MILLISECONDS.toMinutes(counter * 1000))));
	}

	/**
	 * Method to verify that element is available or not
	 */
	public static boolean isElementAvailable(String valueToFindElement) {
		return isElementAvailable(FindByTypeConstants.XPATH, valueToFindElement);
	}

	/**
	 * Method to verify that element is available or not
	 */
	public static boolean isElementAvailable(String findByType, String valueToFindElement) {
		try {
			WebElement element = webDriver().findElement(createBy(findByType, valueToFindElement));
			return (element != null);
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * Provide an optional timeout value for isElementAvailable. The timeout
	 * value is measured in seconds.
	 * 
	 * @param findByType {@link FindByTypeConstants} element specifying the
	 * locator strategy
	 * 
	 * @param valueToFindElement Locator referencing the element of interest
	 * 
	 * @param timeout Maximum duration (int seconds) to wait for the element
	 * 
	 * @return {@code true} if the element is found, {@code false} otherwise
	 */
	public static boolean isElementAvailable(String findByType, String valueToFindElement, int timeout) {
		for (int i = 0; i < timeout; i++) {
			if (isElementAvailable(findByType, valueToFindElement))
				return true;
			sleep();
		}
		return false;
	}

	/**
	 * Method to validate that element is available inside a WebElement
	 * 
	 * @param webElement
	 * @param findByType
	 * @param valueToFindElement
	 * @return -> True/False
	 */
	public static boolean isElementAvailable(WebElement webElement, String findByType, String valueToFindElement) {
		try {
			WebElement element = webElement.findElement(createBy(findByType, valueToFindElement));
			return (element != null);
		} catch (NoSuchElementException nsee) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Test to determine if the specified element is visible in the browser
	 * without any delays.
	 * 
	 * @param findByType
	 *            Locator strategy
	 * @param valueToFindElement
	 *            Locator string
	 * @return {@code true} if the element is present and visible, {@code false}
	 *         otherwise
	 */
	public static boolean isElementVisible(String findByType, String valueToFindElement) {
		if (!isElementAvailable(findByType, valueToFindElement)) {
			return false;
		}
		try {
			WebElement element = webDriver().findElement(createBy(findByType, valueToFindElement));
			if (element == null)
				return false;
			return element.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Test to determine if the specified element is visible, with an optional
	 * retry timer. If the element is not immediately visible, this method waits
	 * one second and retries (to a limit of `timeout` seconds)
	 * 
	 * @param findByType
	 *            Locator strategy
	 * @param valueToFindElement
	 *            Locator string
	 * @param timeout
	 *            The maximum number of seconds to delay while waiting
	 * @return {@code true} if the element is found before the timeout expires,
	 *         {@code false} if the timeout expires first
	 */
	public static boolean isElementVisible(String findByType, String valueToFindElement, int timeout) {
		for (int i = 0; i < timeout; i++) {
			if (isElementVisible(findByType, valueToFindElement))
				return true;
			sleep();
		}
		return false;
	}

	public static boolean checkElementVisible(String findByType, String valueToFindElement) {
		try {
			WebElement element = SeleniumWrapper.webDriver().findElement(createBy(findByType, valueToFindElement));
			if (element != null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Method to maximize page
	 */
	public static void maximizePage() {
		m_webDriver.manage().window().maximize();
	}

	/**
	 * Wait for a second
	 */
	public static void sleep() {
		sleep(1000);
	}

	/**
	 * Wait for particular seconds
	 * 
	 * @param timeOut
	 */
	public static void sleep(long timeOut) {
		try {
			Thread.sleep(timeOut);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the WebDriver instance.
	 * 
	 * @return WebDriver
	 */
	public static WebDriver webDriver() {
		if (m_webDriver == null) {
			setup();
		}
		return m_webDriver;
	}

	/**
	 * Method for implicit wait
	 * 
	 * @param timeout
	 *            -> Timeout (In MilliSeconds)
	 */
	public static void implicitWait(long timeout) {
		webDriver().manage().timeouts().implicitlyWait(timeout, TimeUnit.MILLISECONDS);
	}

	/**
	 * Method to scroll to particular position
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @return -> True/False
	 */
	public static boolean scrollToPostion(String findByType, String valueToFindElement) {
		try {
			WebElement mainMenu = SeleniumWrapper.webDriver().findElement(createBy(findByType, valueToFindElement));
			SeleniumWrapper.getJavascriptExecutor()
					.executeScript("window.scrollTo(0," + (mainMenu.getLocation().y + ")"));
			return true;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.toString());
		}
		return false;
	}

	/**
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @return
	 */
	protected static By createBy(String findByType, String valueToFindElement) {
		switch (findByType) {
		case FindByTypeConstants.CLASS_NAME:
			// waitForElementToStaleAndRecreate(By.className(valueToFindElement));
			return By.className(valueToFindElement);
		case FindByTypeConstants.CSS_SELECTOR:
			// waitForElementToStaleAndRecreate(By.cssSelector(valueToFindElement));
			return By.cssSelector(valueToFindElement);
		case FindByTypeConstants.ID:
			// waitForElementToStaleAndRecreate(By.id(valueToFindElement));
			return By.id(valueToFindElement);
		case FindByTypeConstants.LINK_TEXT:
			// waitForElementToStaleAndRecreate(By.linkText(valueToFindElement));
			return By.linkText(valueToFindElement);
		case FindByTypeConstants.NAME:
			// waitForElementToStaleAndRecreate(By.name(valueToFindElement));
			return By.name(valueToFindElement);
		case FindByTypeConstants.PARTIAL_LINK_TEXT:
			// waitForElementToStaleAndRecreate(By
			// .partialLinkText(valueToFindElement));
			return By.partialLinkText(valueToFindElement);
		case FindByTypeConstants.TAG_NAME:
			// waitForElementToStaleAndRecreate(By.tagName(valueToFindElement));
			return By.tagName(valueToFindElement);
		case FindByTypeConstants.XPATH:
			// waitForElementToStaleAndRecreate(By.xpath(valueToFindElement));
			return By.xpath(valueToFindElement);
		}
		return null;
	}

	// Private Methods

	/**
	 * This method will configure the driver information and set the browser
	 * capabilities.
	 */
	private static void setup() {
		String driverType, ieDriverExePath, chromeExePath, firefoxExePath;
		try {
			Properties appProperties = FileOperations.getProperties("app.properties");
			driverType = appProperties.getProperty(CommonConstants.DRIVER_TYPE);
			Log.writeMessage(CLASS_NAME, "Testing with " + driverType + " browser.");
			DesiredCapabilities cap = null;
			multipleclient = false;
			String nodeBindUrl = null;

			if (appProperties.containsKey("ismulticlientrun")) {
				gridnodeport = appProperties.getProperty("nodeport").trim();
				if (appProperties.get("ismulticlientrun").toString().trim().equals("true")) {
					multipleclient = true;
					nodeBindUrl = ("http://localhost:" + gridnodeport + "/wd/hub").trim();
					System.out.println("nodebind url is " + nodeBindUrl);
				}
			}

			switch (driverType) {
			case DriverTypeConstants.CHROME:
				chromeExePath = null;
				if (System.getProperty("os.name").startsWith("Windows")) {
					chromeExePath = appProperties.getProperty(CommonConstants.CHROME_DRIVER_EXE);
					cap = DesiredCapabilities.chrome();
					if (multipleclient)
						m_webDriver = new RemoteWebDriver(new URL(nodeBindUrl), cap);
					else {
						System.setProperty("webdriver.chrome.driver", chromeExePath);
						m_webDriver = new ChromeDriver();
						break;
					}
				} else {
					chromeExePath = new File(new File(CommonUtil.getBatHomePath()).getParent().toString()).getParent()
							+ "/msqaautomationjars/chromedriverforlinux";
					System.setProperty("webdriver.chrome.driver", chromeExePath);
					ChromeOptions op = new ChromeOptions();
					//op.setBinary("/usr/bin/google-chrome");
					// op.addArguments("headless");
					op.addArguments("--disable-setuid-sandbox");
					op.addArguments("--no-sandbox");
					m_webDriver = new ChromeDriver(op);
				}
			case DriverTypeConstants.HTML_UNIT_DRIVER:
				// m_webDriver = new HtmlUnitDriver(BrowserVersion.CHROME);
				break;
			case DriverTypeConstants.INTERNET_EXPLORER:
				ieDriverExePath = appProperties.getProperty(CommonConstants.IE_DRIVER_EXE);
				System.setProperty("webdriver.ie.driver", ieDriverExePath);
				DesiredCapabilities desiredCapabilities = DesiredCapabilities.internetExplorer();
				desiredCapabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
				desiredCapabilities
						.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				if (appProperties.containsKey("ssourl")) {
					desiredCapabilities.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL,
							appProperties.getProperty("ssourl").trim());
				}
				desiredCapabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
				if (multipleclient)
					m_webDriver = new RemoteWebDriver(new URL(nodeBindUrl), desiredCapabilities);
				else
					m_webDriver = new InternetExplorerDriver(desiredCapabilities);
				break;
			case DriverTypeConstants.ZAP_BROWSER:

				// Set the location of the ChromeDriver
				chromeExePath = appProperties.getProperty(CommonConstants.CHROME_DRIVER_EXE);
				System.setProperty("webdriver.chrome.driver", chromeExePath);

				// Create a new desired capability
				DesiredCapabilities capabilities = DesiredCapabilities.chrome();

				// Create a new proxy object and set the proxy
				Proxy proxy = new Proxy();
				proxy.setHttpProxy("localhost:8080");
				proxy.setSslProxy("localhost:8080");

				// Add the proxy to our capabilities
				capabilities.setCapability("proxy", proxy);

				// Start a new ChromeDriver using the capabilities object we
				// created and added the proxy to
				m_webDriver = new ChromeDriver(capabilities);

				// m_webDriver.navigate().to("http://bbc.co.uk");

				

				// System.out.println(System.getProperties());

				// m_webDriver = new
				// FirefoxDriver(loadFirefoxProfile("ZAPUser"));
				// chromeExePath =
				// appProperties.getProperty(CommonConstants.CHROME_DRIVER_EXE);
				// System.setProperty("webdriver.chrome.driver", chromeExePath);
				// m_webDriver = new ChromeDriver();

				// Proxy proxy = new Proxy();
				// proxy.setProxyType(ProxyType.MANUAL);
				// proxy.setHttpProxy("localhost:8080");
				// proxy.setFtpProxy("localhost:8080");
				// proxy.setSslProxy("localhost:8080");
				// proxy.setAutodetect(false);
				// DesiredCapabilities dc = new DesiredCapabilities();
				// dc.setCapability(CapabilityType.PROXY, proxy);

				break;

			case DriverTypeConstants.FIREFOX:

			default:
				firefoxExePath = appProperties.getProperty(CommonConstants.FIREFOX_DRIVER_EXE);
				System.setProperty("webdriver.gecko.driver", firefoxExePath);
				cap = DesiredCapabilities.firefox();
				if (multipleclient)
					m_webDriver = new RemoteWebDriver(new URL(nodeBindUrl), cap);
				else
					m_webDriver = new FirefoxDriver(createFirefoxProfile());
			}
			Log.writeMessage(CLASS_NAME, driverType + " driver instantiated.");
			// creates instance for JavaScriptExecutor
			m_jsExecutor = (JavascriptExecutor) m_webDriver;
			m_actions = new Actions(m_webDriver);
			m_webDriver.manage().window().maximize();
			// Log.writeMessage(CLASS_NAME, "Driver setup completed.");
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.getLocalizedMessage());
			System.out.println(e.getLocalizedMessage());
		}
	}

	/*
	 * Gets browser capabilities
	 * 
	 * @return Capabilities
	 */
	private static Capabilities getBrowserCapabilities() {
		return ((RemoteWebDriver) m_webDriver).getCapabilities();
	}

	/**
	 * Method to create firefox profile
	 */
	private static FirefoxProfile createFirefoxProfile() {
		FirefoxProfile firefoxProfile = new FirefoxProfile();
		firefoxProfile.setPreference("browser.download.folderList", 2);
		firefoxProfile.setPreference("browser.download.manager.showWhenStarting", false);
		firefoxProfile.setPreference("browser.download.dir", CommonConstants.FILE_DOWNLOAD_PATH);
		firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk",
				CommonConstants.FILE_NEVER_ASK_SAVE_TO_DISK);
		return firefoxProfile;
	}

	/**
	 * Method to load firefox profile
	 */
	public static FirefoxProfile loadFirefoxProfile(String profileName) {
		FirefoxProfile targetProfile = null;
		try {
			ProfilesIni profile = new ProfilesIni();
			targetProfile = profile.getProfile(profileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return targetProfile;
	}

	/**
	 * Method to get percentage progress of loader icon.
	 */
	private static int getPercentage(String style) {
		try {
			String[] values = style.split(";");
			for (String value : values) {
				if (value.contains("width")) {
					double percentValue = Double.parseDouble(value.split(":")[1].replace("%", "").trim());
					return (int) percentValue;
				}
			}
		} catch (Exception e) {
			return -1;
		}
		return -1;
	}

	/*
	 * Constant variable which holds the class name
	 */
	private final static String CLASS_NAME = BaseSelenium.class.getName();
}