

package com.benchmark.core.constants;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.benchmark.core.util.CommonUtil;

public final class CommonConstants {

	

	// Driver configuration
	public final static String DRIVER_TYPE = "drivertype";
	public final static String IE_DRIVER_EXE = "iedriverexepath";
	public final static String CHROME_DRIVER_EXE = "chromedriverexepath";
	public final static String FIREFOX_DRIVER_EXE = "firefoxdriverexepath";

	// E-Mail configuration
	public final static String MAIL_HOST = "smtp.gmail.com"; // "mail.host";
	public final static String MAIL_PORT = "465"; // "mail.port";
	public final static String MAIL_SSL = "TLS v1-1.2";
	public final static String starttls = "true";
	public final static String auth = "true";
	public final static String socketFactoryClass = "javax.net.ssl.SSLSocketFactory";
	public final static String fallback = "true";
	public final static String MAIL_USER_NAME = "gurubai.patil@gmail.com"; // "mail.username";
	public final static String MAIL_PASSWORD = "3ae10cs01"; // "mail.password";
	public final static String MAIL_FROM = "mail.from";
	public final static String MAIL_TO = "mail.to";
	public final static String MAIL_CC = "mail.cc";
	public final static String MAIL_SUBJECT = "mail.subject";
	public final static String MAIL_CONTENT = "mail.content";
	public final static String MAIL_ENABLE = "mail.send";
	public final static String SECURITYTEST_MAIL_SUBJECT = "securitytest.mail.subject";
	public final static String SECURITYTEST_MAIL_CONTENT = "securitytest.mail.content";
	public final static String MAIL_TO_EMAIL_ID="gurubai.p@analyticsquad4.com";
	public final static String EMAIL_CC_EMAIL_ID = "gurubai.p@analyticsquad4.com";

	// Encryption key to encrypt the values in Properties file.
	public final static String ENCRYPTION_KEY = "Marketshare";

	// Variable Types
	public static final String CONTROL_VARTYPE = "CONTROL";
	public static final String TOUCHPOINT_VARTYPE = "TOUCHPOINT";

	

	public static final String FILE_DOWNLOAD_PATH = String.format("%s\\downloadedfiles\\", CommonUtil.getBatHomePath());
	public static final String FILE_NEVER_ASK_SAVE_TO_DISK = "application/zip";
	public static final List<String> TESTNG_SKIP_MESSAGE = new LinkedList<>(Arrays.asList("[pri:0"));
}
