
package com.benchmark.framework.testng;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.testng.IResultMap;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.benchmark.core.constants.CommonConstants;
import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;
import com.benchmark.core.util.CSVFileOperations;
import com.benchmark.core.util.CommonUtil;
import com.benchmark.core.util.FileOperations;
import com.benchmark.core.util.MailOperations;
import com.benchmark.core.util.ZIPFileOperations;
import com.benchmark.framework.ui.SeleniumWrapper;

/**
 * This class will generate the custom report using TestNG. Report will have
 * detailed summary report and module test methods details.
 */
public class CustomTestNGReport extends BaseTestNGReport {

	// Project name which we are running automation scripts. What ever we
	// mention that particular name will be displayed in HTML pages.
	private String m_applicationName;
	// This have module wise test listener adapter
	private LinkedHashSet<TestListenerAdapter> m_testListeners;
	// This will have modules wise test context objects
	private List<ITestContext> m_testContexts = new ArrayList<ITestContext>();
	// Total passed test for complete automation run.
	private int m_totalPassedTests = 0;
	// Total skipped tests for complete automation run.
	private int m_totalSkippedTests = 0;
	// Total failed tests for complete automation run.
	private int m_totalFailedTests = 0;
	// True - Generate KIBANA Report, False - No

	// Skipped Message List
	private List<String> m_skippedMessages = new ArrayList<String>();

	List<String[]> m_kibanaSummaryData = new ArrayList<String[]>();
	private String m_version = null;
	private String m_build = null;
	private String m_product = null;
	private String m_environment = null;
	private String m_url = null;
	private String m_type = null;
	private String m_client = null;
	private String m_browser = null;
	private String m_user = null;
	private String m_testExecutionTime = null;

	private long m_totalEndMillis = 0;
	private long m_totalStartMillis = 0;

	/**
	 * Constructor with execution time
	 * 
	 * @param applicatioName
	 * @param testListenerAdapters
	 * @param TestExecutionTime
	 */
	public CustomTestNGReport(String applicatioName, LinkedHashSet<TestListenerAdapter> testListenerAdapters,
			String TestExecutionTime) {
		super();
		m_applicationName = applicatioName;
		m_testListeners = testListenerAdapters;
		m_testExecutionTime = TestExecutionTime;

	}

	public CustomTestNGReport() {
		super();
	}

	/**
	 * This method will generate the custom report using TestNG listeners.
	 * 
	 * @param testListenerAdapters
	 */
	public boolean generateReport() {
		try {
			createBuilder();

			// Generate summary report
			generateSummaryReport();

			// Generate module detail report
			generateModuleDetailReport();

			// Copy Logs & Screenshots
			copyLogs();

			copyScreenshots();

			boolean isNotSkippedStatus = returnExecutionStatus();
			// Generate Email
			generateEmailSummaryReport(isNotSkippedStatus);

			// if (m_isKibana && isNotSkippedStatus) {
			if (m_totalFailedTests == 0) {
				return true;
			}
		} catch (Exception e) {
			Log.writeMessage(CustomTestNGReport.class.getName(), e.toString());
		} finally {
			// Finally close the writer object if any exception occurs means.
			closeWriter();
		}
		return false;
	}

	/**
	 * This method will generate the custom report using TestNG listeners.
	 * 
	 * @param testListenerAdapters
	 */
	public boolean generateSecurityTestingReport(String alertsSummary) {
		try {
			createBuilder();
			System.out.println("Mail Alert Summary Content: " + alertsSummary);

			generateEmailSummaryReportSecurityTest(alertsSummary);

			if (m_totalFailedTests == 0) {
				return true;
			}
		} catch (Exception e) {
			Log.writeMessage(CustomTestNGReport.class.getName(), e.toString());
			System.out.println();
		} finally {
			// Finally close the writer object if any exception occurs means.
			closeWriter();
		}
		return false;
	}

	// ------------------------------------------------------------------------------------------------------
	// Private Methods

	/*
	 * Create Summary Report
	 */
	private void generateSummaryReport() {
		beforeWriteData(m_summaryReportName, "Summary Report", m_applicationName);
		// Write summary report on content page.
		writeSummaryData();
		afterWriteData();
	}

	/*
	 * Create Modules detailed report.
	 */
	private void generateModuleDetailReport() {
		beforeWriteData(m_ViewDetailsReportName, "Modules Detail Report", m_applicationName);
		// Module Detailed Data
		writeViewDetailReportData();
		afterWriteData();
	}

	/*
	 * Copy Logs
	 */
	private void copyLogs() {
		String logPath = String.format("%s/logs/testlog.log", System.getProperty("user.dir"));
		File logFile = new File(logPath);
		String destinationLogPath = String.format("%s/testlog.log", getBaseDirWithTodayDate());
		File destinationLogFile = new File(destinationLogPath);
		if (logFile.exists())
			FileOperations.copyFile(logFile, destinationLogFile);
	}

	/*
	 * Copy Screenshots if available
	 */
	public void copyScreenshots() {
		File srcDir = SeleniumWrapper.screenshotFolder();
		File destDir = new File(getBaseDirWithTodayDate(), srcDir.getName());
		try {
			if (srcDir.exists()) {
				FileUtils.copyDirectory(srcDir, destDir);
				FileUtils.cleanDirectory(srcDir);
			}
		} catch (IOException e) {
			Log.writeMessage(LogLevel.ERROR, e.toString());
		} catch (NullPointerException e) {
			Log.writeMessage(LogLevel.ERROR, e.toString());
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, e.toString());
		}
	}

	/**
	 * Method to generate Email summary report to put in email.
	 * 
	 * @throws Exception
	 */
	private void generateEmailSummaryReport(boolean isNotSkippedStatus) throws Exception {
		// String exceptionMessage = "ChromeDriver exception caught hence,
		// results will not be pushed to Kibana. Please re-run the test suite.";
		System.out.println();
		String summaryData = m_builder.toString();
		createBuilder();
		writeBuilderStart();
		writeBuilderHeader();
		writeBuilderBodyStart();
		m_builder.append("<table class=\"tablecenter\"> <tr> <td>");
		generateSummaryHeaderTable();
		m_builder.append(summaryData);
		m_builder.append("</td> </tr> </table>");
		writeBuilderBodyEnd();
		writeBuilderDocumentEnd();

		// FileOperations.deleteFile(getBaseDirWithTodayDate(), "viewDetails.html",
		// FilterTypeConstants.Equals);
		// FileOperations.deleteFile(getBaseDirWithTodayDate(), "summarydata.csv",
		// FilterTypeConstants.Equals);

		ZIPFileOperations.zipFolder(getBaseDirWithTodayDate());
		String zipFilePath = String.format("%s.zip", getBaseDirWithTodayDate());
		// MailOperations.send(isNotSkippedStatus ? m_builder.toString() :
		// exceptionMessage, zipFilePath);
		MailOperations.send(m_builder.toString(), zipFilePath);
		CommonUtil.wait(2000);
		FileOperations.deleteFile(new File(zipFilePath));
	}

	/**
	 * Method to generate Email summary report to put in email.
	 */
	private void generateEmailSummaryReportSecurityTest(String alertsSummary) {
		try {

			createBuilder();
			writeBuilderStart();
			writeBuilderHeader();
			writeBuilderBodyStart();
			m_builder.append("<table class=\"tablecenter\"> <tr> <td>");
			generateSummaryHeaderTableForSecurityTest(alertsSummary);
			m_builder.append("</td> </tr> </table>");
			writeBuilderBodyEnd();
			writeBuilderDocumentEnd();

			String sourceFolder = m_zapReportsDirectory;
			String targetFolderName = getZapReportsDirWithTodayDate();
			ZIPFileOperations.zipFolder(sourceFolder, targetFolderName);
			String zipFilePath = String.format("%s.zip", targetFolderName);

			MailOperations.send(m_builder.toString(), zipFilePath);
			CommonUtil.wait(2000);
			FileOperations.deleteFile(new File(zipFilePath));

			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method to generate mail body with new template
	 */
	private void generateSummaryHeaderTable() {
		String finalTime = getDifferentTimesForDuration(m_totalEndMillis, m_totalStartMillis);
		int totalTestCases = m_totalPassedTests + m_totalSkippedTests + m_totalFailedTests;
		m_builder.append("<table class=\"overviewTable\">");
		m_builder.append("<tr>  <th class=\"header\"> %%%SUBJECTHEADER%%% </th> </tr>");
		m_builder.append("<tr> <td class=\"summarymsg\"> %%%SUMMARYNOTE%%% </td> </tr>");
		m_builder.append("<tr> <td align=\"right\">");

		m_builder.append("<table class=\"successIndicator\"> <tr>");
		m_builder.append(
				"<th/> <th>Duration</th> <th>Total</th> <th>Passed</th> <th>Skipped</th> <th>Failed</th> </tr>");

		m_builder.append("<tr class=\"test\">");
		m_builder.append("<td class=\"test\">TestCases</td>");
		m_builder.append(String.format("<td class=\"test\" style=\"text-align:right;\">%s</td>", finalTime));
		m_builder.append(String.format("<td class=\"test\" style=\"text-align:right;\">%s</td>",
				String.valueOf(totalTestCases)));
		m_builder.append(String.format("<td class=\"%s\" style=\"text-align:right;\">%s</td>",
				m_totalPassedTests > 0 ? "passed" : "test", String.valueOf(m_totalPassedTests)));
		m_builder.append(String.format("<td class=\"%s\" style=\"text-align:right;\">%s</td>",
				m_totalSkippedTests > 0 ? "skipped" : "test", String.valueOf(m_totalSkippedTests)));
		m_builder.append(String.format("<td class=\"%s\" style=\"text-align:right;\">%s</td>",
				m_totalFailedTests > 0 ? "failed" : "test", String.valueOf(m_totalFailedTests)));

		m_builder.append("<tr> <td colspan=\"6\" style=\"text-align:right;\">");
		m_builder.append("</td> </tr> </table>");

		m_builder.append("</td> </tr> </table>");
	}

	/**
	 * Method to generate mail body with new template
	 */
	private void generateSummaryHeaderTableForSecurityTest(String alertsSummary) {
		try {
			m_builder.append("<table class=\"overviewTable\">");
			m_builder.append("<tr> <th class=\"header\"> %%%SUBJECTHEADER%%% </th> </tr>");
			m_builder.append("<tr> <td class=\"summarymsg\"> %%%SUMMARYNOTE%%% </td> </tr>");
			m_builder.append("</table>");

			m_builder.append(alertsSummary);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * This method will write the summary report. This will display the all modules
	 * which we have executed with details like duration, passed, skipped, and
	 * failed.
	 */
	private void writeSummaryData() {
		double totalDuration = 0;

		long endMillis = 0;
		long startMillis = 0;
		// String txtPassed = "±P@SSED±";
		// String txtSkipped = "±SKIPPED±";
		// String txtFailed = "±F@ILED±";
		// String txtDuration = "±DUR@TION±";

		List<String[]> summaryData = new ArrayList<String[]>();

		for (TestListenerAdapter testListenerAdapter : m_testListeners) {
			m_testContexts.addAll(testListenerAdapter.getTestContexts());
		}

		m_writer.print("<table class=\"overviewTable\">");
		m_writer.print("<tr>");
		m_writer.print("<th class=\"header suite\" colspan=\"5\">");
		m_writer.print(String.format("%s </th>", m_applicationName));
		m_writer.print("</tr>");

		// m_writer.print("</br>");
		// m_writer.print("<tr>");
		// m_writer.print("<td>");
		// m_writer.print(String.format("<img src='%s'/>", m_summaryImage));
		// m_writer.print("</td>");
		// m_writer.print("</tr>");
		// m_writer.print("</br>");

		m_writer.print("<tr class=\"columnHeadings\">");
		m_writer.print("<th>Module</th>");
		m_writer.print("<th>Duration (s)</th>");
		m_writer.print("<th>Passed</th>");
		m_writer.print("<th>Skipped</th>");
		m_writer.print("<th>Failed</th>");
		m_writer.print("</tr>");

		// TO DO: Need update with builder later
		// ----------------------------------------------------------------------
		m_builder.append("<table class=\"overviewTable\">");
		// m_builder.append("<tr>");
		// m_builder.append("<th class=\"header suite\" colspan=\"5\">");
		// m_builder.append(String.format("%s </th>", m_applicationName));
		// m_builder.append("</tr>");

		m_builder.append("<tr class=\"columnHeadings\">");
		m_builder.append("<th style=\"width:40%;\">Module</th>");
		m_builder.append("<th>Duration (s)</th>");
		m_builder.append("<th>Passed</th>");
		m_builder.append("<th>Skipped</th>");
		m_builder.append("<th>Failed</th>");
		m_builder.append("</tr>");

		// m_builder.append("<tr class=\"suite\">");
		// m_builder.append("<td class=\"totalLabel\">Total</td>");
		// m_builder.append(txtDuration);
		// m_builder.append(txtPassed);
		// m_builder.append(txtSkipped);
		// m_builder.append(txtFailed);
		// m_builder.append("</tr>");
		// ----------------------------------------------------------------------

		// write data into List<String[]> which will written to CSV file
		// ---------------------------------------------------------------------------
		summaryData.add(new String[] { "Date", "Module", "Duration", "Passed", "Skipped", "Failed" });
		// ---------------------------------------------------------------------------

		for (ITestContext testContext : m_testContexts) {
			int passed = testContext.getPassedTests().size();
			int skipped = testContext.getSkippedTests().size();
			int failed = testContext.getFailedTests().size();

			m_totalPassedTests = m_totalPassedTests + passed;
			m_totalSkippedTests = m_totalSkippedTests + skipped;
			m_totalFailedTests = m_totalFailedTests + failed;
			endMillis = testContext.getEndDate().getTime();
			startMillis = testContext.getStartDate().getTime();

			m_totalEndMillis = m_totalEndMillis + endMillis;
			m_totalStartMillis = m_totalStartMillis + startMillis;

			double duration = getSeconds(endMillis, startMillis);
			long diffSeconds = TimeUnit.MILLISECONDS.toSeconds(endMillis - startMillis);
			totalDuration = totalDuration + duration;

			m_writer.print("<tr class=\"test\">");
			writeTableData(testContext.getName(), "test");
			writeTableData(String.valueOf(diffSeconds), "duration");
			writeTableData(String.valueOf(passed), (passed == 0) ? "zero number" : "passed number");
			writeTableData(String.valueOf(skipped), (skipped == 0) ? "zero number" : "skipped number");
			writeTableData(String.valueOf(failed), (failed == 0) ? "zero number" : "failed number");
			m_writer.print("</tr>");

			// Builder Code
			m_builder.append("<tr class=\"test\">");
			writeBuilderTableData(testContext.getName(), "test");
			writeBuilderTableData(String.valueOf(diffSeconds), "duration");
			writeBuilderTableData(String.valueOf(passed), (passed == 0) ? "zero number" : "passed number");
			writeBuilderTableData(String.valueOf(skipped), (skipped == 0) ? "zero number" : "skipped number");
			writeBuilderTableData(String.valueOf(failed), (failed == 0) ? "zero number" : "failed number");
			m_builder.append("</tr>");
			// Builder Code

			// CSV File data
			summaryData.add(getCSVData(testContext.getName(), String.valueOf(diffSeconds), String.valueOf(passed),
					String.valueOf(skipped), String.valueOf(failed)));
		}

		String finalTime = getDifferentTimesForDuration(m_totalEndMillis, m_totalStartMillis);
		// String finalTime = getFinalTime(totalEndMillis, totalStartMillis);

		m_writer.print("<tr class=\"suite\">");
		m_writer.print("<td class=\"totalLabel\">Total</td>");
		m_writer.print(String.format("<td class=\"duration\" title=\"%s\">%s</td>", finalTime, finalTime));
		writeTableData(String.valueOf(m_totalPassedTests), (m_totalPassedTests == 0) ? "zero number" : "passed number");
		writeTableData(String.valueOf(m_totalSkippedTests),
				(m_totalSkippedTests == 0) ? "zero number" : "skipped number");
		writeTableData(String.valueOf(m_totalFailedTests), (m_totalFailedTests == 0) ? "zero number" : "failed number");
		m_writer.print("</tr>");

		// ------------------------------------------------------------------------------
		// Generate PieChart and bind to report

		Map<String, String> values = new HashMap<String, String>();

		if (m_totalPassedTests > 0) {
			values.put("Passed", String.valueOf(m_totalPassedTests));
		}
		if (m_totalSkippedTests > 0) {
			values.put("Skipped", String.valueOf(m_totalSkippedTests));
		}
		if (m_totalFailedTests > 0) {
			values.put("Failed", String.valueOf(m_totalFailedTests));
		}

		// MSReports reports = new MSReports();
		// if (reports.generateAutomationSummaryReport(values,
		// getSummaryPieChartName())) {
		// Log.writeMessage(LogLevel.INFO, "PieChart Generated.");
		// }

		// ------------------------------------------------------------------------------

		m_writer.print("</table>");

		// ---------------------------------------
		// Builder Code

		// int durationIndex = m_builder.toString().indexOf(txtDuration);
		// m_builder.replace(durationIndex, durationIndex +
		// txtDuration.length(),
		// String.format("<td class=\"duration\" title=\"%s\">%s</td>",
		// finalTime, finalTime));
		//
		// int passedIndex = m_builder.toString().indexOf(txtPassed);
		// m_builder.replace(passedIndex, passedIndex + txtPassed.length(),
		// getBuilderTableData(
		// String.valueOf(m_totalPassedTests), (m_totalPassedTests == 0) ? "zero
		// number" : "passed number"));
		//
		// int skippedIndex = m_builder.toString().indexOf(txtSkipped);
		// m_builder.replace(skippedIndex, skippedIndex + txtSkipped.length(),
		// getBuilderTableData(
		// String.valueOf(m_totalSkippedTests), (m_totalSkippedTests == 0) ?
		// "zero number" : "skipped number"));
		//
		// int failedIndex = m_builder.toString().indexOf(txtFailed);
		// m_builder.replace(failedIndex, failedIndex + txtFailed.length(),
		// getBuilderTableData(
		// String.valueOf(m_totalFailedTests), (m_totalFailedTests == 0) ? "zero
		// number" : "failed number"));

		m_builder.append("</table>");
		// Builder Code
		// ---------------------------------------

		// Writing summary data into CSV file
		performCSVOperation(summaryData);
	}

	/*
	 * This method will write the detail report content to view detail page.
	 */
	private void writeViewDetailReportData() {

		// write data into List<String[]> which will written to CSV file
		// ---------------------------------------------------------------------------
		// ---------------------------------------------------------------------------

		m_writer.print(
				"<div class=\"note\">** On mouseover of table cells method name or error description, you can view TestMethod description or Error information.</div>");

		m_writer.print("<table class=\"resultsTable\">");
		// Write module wise failed test cases.
		if (m_totalFailedTests > 0) {
			writeMethodData("Failed");
		}
		// Write module wise passed test cases.
		if (m_totalPassedTests > 0) {
			writeMethodData("Passed");
		}
		// Write module wise skipped test cases.
		if (m_totalSkippedTests > 0) {
			writeMethodData("Skipped");
		}
		m_writer.print("</table>");

	}

	/*
	 * This method will write the module wise test method information which are
	 * executed.
	 * 
	 * @param status
	 */
	private void writeMethodData(String status) {
		m_writer.print("<tr>");
		m_writer.print(String.format("<th class=\"header %s\" colspan=\"3\">", status.toLowerCase()));
		m_writer.print(String.format("%s Tests</th>", status));
		m_writer.print("</tr>");
		for (ITestContext testContext : m_testContexts) {

			IResultMap resultMap = null;
			switch (status) {
			case "Passed":
				resultMap = testContext.getPassedTests();
				break;
			case "Skipped":
				resultMap = testContext.getSkippedTests();
				break;
			case "Failed":
				resultMap = testContext.getFailedTests();
				break;
			}

			if (resultMap != null) {
				if (resultMap.size() > 0) {
					m_writer.print("<tr>");
					m_writer.print("<td class=\"group\" colspan=\"3\">");
					m_writer.print(String.format("%s</td>", testContext.getName()));
					m_writer.print("</tr>");

					for (ITestResult testResult : resultMap.getAllResults()) {
						String testCaseName = null;
						if (testResult.getName().equals("test")) {
							testCaseName = testResult.getInstanceName();
							testCaseName = testCaseName.substring(testCaseName.lastIndexOf(".") + 1,
									testCaseName.length());
						} else {
							testCaseName = testResult.getName();
						}
						String description = testResult.getMethod().getDescription();
						if (description == null) {
							description = "Description is not added. Please add description for this test case.";
						}
						m_writer.print("<tr>");
						m_writer.print(String.format("<td class=\"method\" title=\"%s\">", description));
						m_writer.print(String.format("%s</td>", testCaseName));

						long duration = getSeconds(testResult.getEndMillis(), testResult.getStartMillis());

						m_writer.print("<td class=\"duration\">");
						m_writer.print(String.format("%s</td>",
								getMinAndSecondsForMSeconds(testResult.getEndMillis(), testResult.getStartMillis())));

						String errorMessage = testResult.getThrowable() == null ? testResult.getMethod() + status
								: testResult.getThrowable().getMessage();

						if (errorMessage == null) {
							errorMessage = testCaseName + " - " + status
									+ ": Please catch exception/add proper error message in your automation scripts, to understand for user, why this test case got failed.";
						}
						String code = "";
						String[] errorCodeMessages = errorMessage.split("~~");
						if (errorMessage != null) {
							if (errorCodeMessages.length > 1)
								code = errorCodeMessages[0];

						}
						errorMessage = errorCodeMessages.length > 1 ? errorCodeMessages[1] : errorCodeMessages[0];

						String result = (status.equals("Failed") || status.equals("Skipped"))
								? String.format("<td style=\"color: red;\" title=\"%s\">%s</td>",
										getStackTrace(testResult.getThrowable()), errorMessage)
								: "<td class=\"result\"></td>";

						m_writer.print(result);

						m_writer.print("</tr>");

						// ------------------------------------------------------------------------
						// Write KIBANA Summary Data
						if (m_testExecutionTime != null) {
							Date startDate = new Date(testResult.getStartMillis());
							Date endDate = new Date(testResult.getEndMillis());

							m_kibanaSummaryData.add(getKibanaCSVData(
									CommonUtil.getFormatedDate("yyyy-MM-dd'T'hh:mm:ssZ", startDate).replace("'", ""),
									CommonUtil.getFormatedDate("yyyy-MM-dd'T'hh:mm:ssZ", endDate).replace("'", ""),
									testContext.getName(), testCaseName, description.trim(), String.valueOf(duration),
									status, code,
									status.equalsIgnoreCase("Passed") ? "null" : errorMessage.trim().replace(",", "")));
						}
						if (status.equals("Skipped")) {
							m_skippedMessages.add(errorMessage);
						}
						// ------------------------------------------------------------------------
					}
					writeEmptyLine();
				}
			}
		}
		if (status != "Skipped") {
			writeEmptyLine();
		}
	}

	/*
	 * This method will return the difference between to milliseconds.
	 * 
	 * @param endMillis
	 * 
	 * @param startMillis
	 * 
	 * @return
	 */
	private long getSeconds(long endMillis, long startMillis) {
		long diffMillis = endMillis - startMillis;
		Long duration = TimeUnit.MILLISECONDS.toSeconds(diffMillis);
		return duration;
	}

	/*
	 * This method will return the print stack trace.
	 * 
	 * @param throwable
	 * 
	 * @return
	 */
	private String getStackTrace(Throwable throwable) {
		String finalValue = null;
		try {
			StringWriter errors = new StringWriter();
			throwable.printStackTrace(new PrintWriter(errors));
			finalValue = errors.toString();
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CustomTestNGReport.class.getName(), e.getMessage());
		}
		return finalValue;
	}

	/**
	 * This method will return duration time in minutes, hours and days.
	 * 
	 * @param endMillis
	 * 
	 * @param startMillis
	 * 
	 * @return
	 */
	private String getDifferentTimesForDuration(long endMillis, long startMillis) {
		long milliseconds = endMillis - startMillis;
		final String FORMAT = "%02d:%02d:%02d";
		return String.format(FORMAT, TimeUnit.MILLISECONDS.toHours(milliseconds),
				TimeUnit.MILLISECONDS.toMinutes(milliseconds)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)),
				TimeUnit.MILLISECONDS.toSeconds(milliseconds)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
	}

	/**
	 * Method to get Min and Second from MSeconds
	 * 
	 * @param endMillis
	 * @param startMillis
	 * @return
	 */
	private String getMinAndSecondsForMSeconds(long endMillis, long startMillis) {
		long milliseconds = endMillis - startMillis;
		final String FORMAT = "%02d:%02d";
		return String.format(FORMAT,
				TimeUnit.MILLISECONDS.toMinutes(milliseconds)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)),
				TimeUnit.MILLISECONDS.toSeconds(milliseconds)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
	}

	/**
	 * Method to populate CSV data
	 * 
	 * @param moduleName
	 * @param duration
	 * @param passed
	 * @param skipped
	 * @param failed
	 */
	private String[] getCSVData(String moduleName, String duration, String passed, String skipped, String failed) {
		List<String> csvData = new ArrayList<String>();
		csvData.add(CommonUtil.getFormatedDate("yyyy-MM-dd'T'hh:mm:ssZ").replace("'", ""));
		csvData.add(moduleName);
		csvData.add(duration);
		csvData.add(passed);
		csvData.add(skipped);
		csvData.add(failed);
		return csvData.toArray(new String[csvData.size()]);
	}

	/**
	 * Method to populate KIBANA data
	 * 
	 * @param testExecutionStart
	 * @param testExecutionEnd
	 * @param module
	 * @param testCaseName
	 * @param duration
	 * @param status
	 * @param errorMessage
	 * @return String[]
	 */
	private String[] getKibanaCSVData(String testExecutionStart, String testExecutionEnd, String module,
			String testCaseName, String testDescription, String duration, String status, String errorMessage,
			String code) {

		List<String> csvData = new ArrayList<String>();
		csvData.add(m_testExecutionTime);
		csvData.add(testExecutionStart);
		csvData.add(testExecutionEnd);
		csvData.add(m_client);
		csvData.add(m_version);
		csvData.add(m_build);
		csvData.add(m_browser);
		csvData.add(m_product);
		csvData.add(m_environment);
		csvData.add(m_url);
		csvData.add(m_type);
		csvData.add(m_user);
		csvData.add(module);
		if (testCaseName.length() >= 256) {
			csvData.add(String.format("\"%s\"", testCaseName.substring(0, 255)));
		} else {
			csvData.add(String.format("\"%s\"", testCaseName));
		}
		testDescription = testDescription.trim().replace(",", "");
		if (testDescription.length() >= 256) {
			csvData.add(String.format("\"%s\"", testDescription.substring(0, 255)));
		} else {
			csvData.add(String.format("\"%s\"", testDescription));
		}
		csvData.add(duration);
		csvData.add(status);
		errorMessage = errorMessage.trim().replace(",", "");
		if (errorMessage.length() >= 256) {
			csvData.add(String.format("\"%s\"", errorMessage.substring(0, 255)));
		} else {
			csvData.add(String.format("\"%s\"", errorMessage));
		}
		csvData.add(code);
		return csvData.toArray(new String[csvData.size()]);
	}

	/**
	 * Method to create CSV file and read data and remove double quotes from the
	 * file
	 */
	private void performCSVOperation(List<String[]> summaryData) {
		performCSVOperation(summaryData, getCSVFileName(), "CSV - Summary data file created successfully.");
	}

	/**
	 * Perform CSV Operation
	 * 
	 * @param summaryData
	 * @param fileName
	 * @param logMessage
	 */
	public void performCSVOperation(List<String[]> summaryData, String fileName, String logMessage) {
		try {
			CSVFileOperations.createCSVFile(fileName, summaryData);
			FileOperations.readFileDataAndReplaceText(fileName, "\"", "");
			Log.writeMessage(LogLevel.INFO, CLASS_NAME, logMessage);
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, "Failed while creating csv file");
		}
	}

	/**
	 * Get the execution status based on the error message "ERROR
	 * CustomTestNGReport"
	 * 
	 * @param logFile
	 * @return true or false
	 */
	private boolean returnExecutionStatus() {
		List<Boolean> results = new LinkedList<>();
		boolean flag = true;
		if (m_skippedMessages.size() > 0) {
			for (String message : m_skippedMessages) {
				for (String validation : CommonConstants.TESTNG_SKIP_MESSAGE) {
					flag = message.contains(validation);
					if (flag)
						results.add(!flag);
				}
			}
		}
		return results.size() > 2 ? false : true;
	}

	/**
	 * Class Name
	 */
	private final static String CLASS_NAME = CustomTestNGReport.class.getName();
}
