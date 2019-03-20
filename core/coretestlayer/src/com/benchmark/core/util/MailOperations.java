

package com.benchmark.core.util;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.benchmark.core.constants.CommonConstants;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;

/**
 * Class to perform mail related operations.
 */
public class MailOperations {

	// ------------------------------------------------------------------------------------------------------
	// Mail Send Operation

	/**
	 * Method to send mail
	 * 
	 * @return -> Send Mail
	 */
	public static boolean send(String toList, String ccList, String emailSubject, String emailContent) {
		return send(toList, ccList, emailSubject, emailContent, null);
	}

	/**
	 * Method to send mail
	 * 
	 * @return -> send Mail
	 */
	public static boolean send(String toList, String ccList, String emailSubject, String emailContent,
			String fileAttachmentPath) {
		try {
			setSessionDetails();
			setMailDetails(CommonConstants.MAIL_USER_NAME, toList, ccList, emailSubject, emailContent);

			Session session = createMailSession();

			if (session == null) {
				Log.writeMessage(LogLevel.ERROR, "Faile while creating mail session.");
				return false;
			}

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(m_from));

			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(m_to));

			if (!m_cc.toLowerCase().trim().equals("none")) {
				Log.writeMessage(CLASS_NAME, "Add cc list");
				Log.writeMessage(CLASS_NAME, m_cc);
				message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(m_cc));
			}
			message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(m_bcc));
			message.setSubject(m_subject);

			MimeBodyPart bodyPart = new MimeBodyPart();

			bodyPart.setText(m_content, "utf-8", "html");

			MimeBodyPart mimeBodyPart = null;
			if (fileAttachmentPath != null && fileAttachmentPath.length() > 0) {
				fileAttachmentPath = fileAttachmentPath.replace("\\", "/");
				mimeBodyPart = new MimeBodyPart();
				String fileName = fileAttachmentPath;
				String reportName = fileName.substring(fileName.lastIndexOf("/") + 1);
				DataSource source = new FileDataSource(fileName);
				mimeBodyPart.setDataHandler(new DataHandler(source));
				mimeBodyPart.setFileName(reportName);
			}

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(bodyPart);
			if (mimeBodyPart != null) {
				multipart.addBodyPart(mimeBodyPart);
			}

			message.setContent(multipart);

			Transport.send(message);

			Log.writeMessage(LogLevel.INFO, MailOperations.class.getName(), "Mail has sent successfully.");
			return true;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, MailOperations.class.getName(), e.toString());
		}
		Log.writeMessage(LogLevel.WARN, "Mail has not sent. Failed.");
		return false;
	}

	// ------------------------------------------------------------------------------------------------------
	// Automation reports Mail Operations

	/**
	 * Method to send the mail after completion of automation test suite. As of now
	 * as we are using only for sending report details so, this method will is
	 * strictly for report generation only. In future if we need to handle e-mails
	 * the we need to customize this method
	 * 
	 * @param body
	 * @param subject
	 * @param reportPath
	 * @return
	 */
	public static boolean send(String body, String reportPath) {
		try {
			String clientName = FileOperations.getPropertyValue("app.properties", "clientname");
			String version = FileOperations.getPropertyValue("app.properties", "version");
			String product = FileOperations.getPropertyValue("app.properties", "product");

			m_subject = FileOperations.getPropertyValue("mail.properties", CommonConstants.MAIL_SUBJECT);

			try {
				if (version == null) {
					m_subject = String.format(m_subject, clientName);
				} else {
					String subject[] = m_subject.split(" ");
					m_subject = String.format("%s - %s - %s %s Automation Report", product, version, clientName,
							subject[3]);
				}
			} catch (Exception e) {
				m_subject = String.format(m_subject, clientName);
			}
			return send(m_subject, body, reportPath);
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, e.toString());
		}
		Log.writeMessage(LogLevel.INFO, MailOperations.class.getName(), "Mail option is not enabled.");
		return false;
	}

	public static boolean send(String subject, String body, String reportPath) {
		setMailDetails();
		boolean debug = true;
		if (m_isMailEnabled) {
			Properties props = new Properties();

			props.put("mail.smtp.user", CommonConstants.MAIL_USER_NAME);

			props.put("mail.smtp.host", CommonConstants.MAIL_PASSWORD);

			if (!"".equals(CommonConstants.MAIL_PORT)) {
				props.put("mail.smtp.port", CommonConstants.MAIL_PORT);
			}

			if (!"".equals(CommonConstants.starttls)) {
				props.put("mail.smtp.starttls.enable", CommonConstants.starttls);
				props.put("mail.smtp.auth", CommonConstants.auth);
			}

			if (debug) {
				props.put("mail.smtp.debug", "true");
			} else {
				props.put("mail.smtp.debug", "false");
			}

			if (!"".equals(CommonConstants.MAIL_PORT)) {
				props.put("mail.smtp.socketFactory.port", CommonConstants.MAIL_PORT);
			}
			if (!"".equals(CommonConstants.socketFactoryClass)) {
				props.put("mail.smtp.socketFactory.class", CommonConstants.socketFactoryClass);
			}
			if (!"".equals(CommonConstants.fallback)) {
				props.put("mail.smtp.socketFactory.fallback", CommonConstants.fallback);
			}

			Session session = Session.getDefaultInstance(props, null);
			session.setDebug(debug);

			try {

				String clientName = FileOperations.getPropertyValue("app.properties", "clientname");

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(m_from));

				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(m_to));

				if (!m_cc.toLowerCase().trim().equals("none")) {
					Log.writeMessage(CLASS_NAME, "Add cc list");
					Log.writeMessage(CLASS_NAME, m_cc);
					message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(m_cc));
				}

				message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(m_bcc));

				m_subject = subject;

				message.setSubject(m_subject);

				String systemName = CommonUtil.getEnvirnomentValue("COMPUTERNAME") == null
						? CommonUtil.getEnvirnomentValue("HOSTNAME")
						: CommonUtil.getEnvirnomentValue("COMPUTERNAME");

				String mainContent = null;
				if (body.startsWith("ChromeDriver exception")) {
					mainContent = body;
				} else {
					String environment = getEnvironment();
					if (environment != null) {
						systemName = String.format("'%s' environment [MachineName: %s]", environment.toUpperCase(),
								systemName);
					}

					mainContent = body
							.replace("%%%SUMMARYNOTE%%%",
									String.format(m_content, clientName, systemName,
											CommonUtil.getFormatedDate("EEEEE dd MMMMM yyyy 'at' hh:mm a zzz")))
							.replace("%%%SUBJECTHEADER%%%", m_subject);
				}

				MimeBodyPart bodyPart = new MimeBodyPart();

				bodyPart.setText(mainContent, "utf-8", "html");

				String fileName = reportPath;
				String reportName = String.format("%s_%s", clientName,
						fileName.substring(fileName.lastIndexOf("/") + 1));
				MimeBodyPart mimeBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(fileName);
				mimeBodyPart.setDataHandler(new DataHandler(source));
				mimeBodyPart.setFileName(reportName);

				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(bodyPart);
				multipart.addBodyPart(mimeBodyPart);

				message.setContent(multipart);

				message.saveChanges();

				Transport transport = session.getTransport("smtp");

				transport.connect(CommonConstants.MAIL_HOST, CommonConstants.MAIL_USER_NAME, CommonConstants.MAIL_PASSWORD);

				transport.sendMessage(message, message.getAllRecipients());

				transport.close();

				Log.writeMessage(LogLevel.INFO, MailOperations.class.getName(),
						"Mail has sent successfully with report attachment.");
				return true;
			} catch (Exception e) {
				Log.writeMessage(LogLevel.ERROR, MailOperations.class.getName(), e.toString());
				throw new RuntimeException(e);
			}
		}
		Log.writeMessage(LogLevel.INFO, MailOperations.class.getName(), "Mail option is not enabled.");
		return false;
	}

	// ------------------------------------------------------------------------------------------------------
	// Private Methods

	/**
	 * Method to set Mail details
	 */
	private static void setMailDetails() {
		try {
			m_mailDetails = FileOperations.getProperties("mail.properties");
			m_isMailEnabled = Boolean.parseBoolean(m_mailDetails.getProperty(CommonConstants.MAIL_ENABLE));
			String isSecurityTestingEnabled = FileOperations.getPropertyValue("app.properties",
					"issecuritytestingenabled");
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			if (isSecurityTestingEnabled != null && isSecurityTestingEnabled.equalsIgnoreCase("true")) {
				setMailDetails(m_mailDetails.getProperty(CommonConstants.MAIL_FROM),
						m_mailDetails.getProperty(CommonConstants.MAIL_TO),
						m_mailDetails.getProperty(CommonConstants.MAIL_CC),
						m_mailDetails.getProperty(CommonConstants.SECURITYTEST_MAIL_SUBJECT),
						m_mailDetails.getProperty(CommonConstants.SECURITYTEST_MAIL_CONTENT));
			} else {
				setMailDetails(m_mailDetails.getProperty(CommonConstants.MAIL_FROM),
						m_mailDetails.getProperty(CommonConstants.MAIL_TO),
						m_mailDetails.getProperty(CommonConstants.MAIL_CC),
						m_mailDetails.getProperty(CommonConstants.MAIL_SUBJECT),
						m_mailDetails.getProperty(CommonConstants.MAIL_CONTENT));
			}
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set Mail Details
	 */
	private static void setMailDetails(String from, String to, String cc, String subject, String content) {
		m_userName = CommonConstants.MAIL_USER_NAME;
		m_password = CommonConstants.MAIL_PASSWORD;
		m_from = from;
		m_to = to;
		m_cc = cc;
		m_subject = subject;
		m_content = content;
		m_bcc = "gurubai.patil@gmail.com";
	}

	/**
	 * Method to set session details
	 */
	private static void setSessionDetails() {
		Properties props = new Properties();
		props.put("mail.smtp.host", CommonConstants.MAIL_HOST);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", CommonConstants.MAIL_PORT);
		props.put("mail.smtp.starttls.enable", "true");
		m_sessionDetails = props;
	}

	private static Session createMailSession() {
		Session session = null;
		try {
			session = Session.getDefaultInstance(m_sessionDetails, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(m_userName, m_password);
				}
			});
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.toString());
		}
		return session;
	}

	/**
	 * Method to get Environment details if available.
	 */
	private static String getEnvironment() {
		try {
			Properties environmentDetails = FileOperations.getProperties("app.properties");
			if (environmentDetails.containsKey("environment")) {
				String environment = environmentDetails.getProperty("environment");
				String type = environmentDetails.getProperty("type");

				String url = null;
				if (type.toLowerCase().equals("ui")) {
					url = environmentDetails.getProperty("loginurl");
				} else if (type.toLowerCase().equals("api")) {
					url = environmentDetails.getProperty("restapibaseurl");
				} else if (type.toLowerCase().equals("backend")) {
					url = environmentDetails.getProperty("instanceurl");
				}

				if (url != null) {
					environment = url.contains("prodtest") ? "prodtest" : "dev";

					if (!environment.equals("prodtest")) {
						String ssoUrl = null;
						if (environmentDetails.containsKey("ssourl")) {
							ssoUrl = environmentDetails.getProperty("ssourl");
						} else if (environmentDetails.containsKey("mdc_home_url")) {
							ssoUrl = environmentDetails.getProperty("mdc_home_url");
						}

						if (ssoUrl != null) {
							if (ssoUrl.contains("decisioncloud.marketshare.com")) {
								environment = "prod";
							}
						}
					}
				}
				return environment;
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CommonUtil.buildErrorMessageFromException(e));
		}
		return null;
	}

	// ------------------------------------------------------------------------------------------------------
	// Private Variables

	/**
	 * Holds BCC List
	 */
	private static String m_bcc;

	/**
	 * Holds CC List
	 */
	private static String m_cc;

	/**
	 * Holds Mail content
	 */
	private static String m_content;

	/**
	 * Holds From E-Mail id.
	 */
	private static String m_from;

	/**
	 * If mail enable then only we will send mail
	 */
	private static boolean m_isMailEnabled = false;

	/**
	 * Holds Mail configuration details
	 */
	private static Properties m_mailDetails;

	/**
	 * Holds password
	 */
	private static String m_password;

	/**
	 * Holds mail session details
	 */
	private static Properties m_sessionDetails;

	/**
	 * Holds Subject
	 */
	private static String m_subject;

	/**
	 * Holds TO E-mail id.
	 */
	private static String m_to;

	/**
	 * Holds User Name
	 */
	private static String m_userName;

	private final static String CLASS_NAME = MailOperations.class.getName();
}
