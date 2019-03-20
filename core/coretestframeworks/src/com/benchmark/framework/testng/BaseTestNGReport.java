

package com.benchmark.framework.testng;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.benchmark.core.logger.Log;
import com.benchmark.core.util.CommonUtil;
import com.benchmark.core.util.FileOperations;

public abstract class BaseTestNGReport {

	protected static String m_summaryReportName = "summary.html";
	protected static String m_ViewDetailsReportName = "viewDetails.html";
	protected static String m_csvFileName = "summarydata.csv";
	protected static String m_csvKibanaFileName = "summaryKibanadata.csv";
	protected static String m_summaryImage = "summaryPiechart.png";

	// Days to remove the report folders.
	private int m_daysBack = 30;

	// Holds PrintWriter object
	protected PrintWriter m_writer;

	// String Builder
	protected StringBuilder m_builder;

	// Holds today folder name.
	protected String m_todayFolder;

	// Holds Base Directory details.
	protected String m_baseDirectory = String.format("%s/aq4report",
			System.getProperty("user.dir").replace("target", ""));

	// Holds Base Directory details.
	protected String m_zapReportsDirectory = System.getProperty("user.dir") + File.separator + "target" + File.separator
			+ "zap-reports";

	/**
	 * Constructor. We will delete the files which are older than 30 days and
	 * creates the new folder with today's date.
	 */
	public BaseTestNGReport() {

		// Create folder with today's date. In this folder we will be generating
		// the test results reports.
		m_todayFolder = CommonUtil.getFormatedDate("MMddyyyy");

		// Remove report files which are older than 30 days.
		FileOperations.removeFilesOlderThanNDays(m_baseDirectory, m_daysBack);

		// Create the directory with today date if its not available means..
		FileOperations.createDirectory(getBaseDirWithTodayDate());

		copyMarketShareImage();

	}

	/**
	 * Creates Writer
	 * 
	 * @param fileName
	 */
	protected void createWriter(String fileName) {
		try {
			m_writer = new PrintWriter(
					new BufferedWriter(new FileWriter(new File(getBaseDirWithTodayDate(), fileName))));
		} catch (IOException e) {
			Log.writeMessage(BaseTestNGReport.class.getName(), e.getMessage());
		}
	}

	/**
	 * Creates String Builder
	 */
	protected void createBuilder() {
		m_builder = new StringBuilder();
	}

	/**
	 * Flush and close the writer.
	 */
	protected void closeWriter() {
		if (m_writer != null) {
			m_writer.flush();
			m_writer.close();
			m_writer = null;
		}
	}

	/**
	 * This method will get the base folder with todays date.
	 * 
	 * @return
	 */
	public String getBaseDirWithTodayDate() {
		return String.format("%s/%s", m_baseDirectory, m_todayFolder).replace("\\", "/");
	}

	/**
	 * This method will get the Zap report folder with todays date.
	 * 
	 * @return
	 */
	public String getZapReportsDirWithTodayDate() {
		return String.format("%s", m_zapReportsDirectory).replace("\\", "/") + "-" + m_todayFolder;
	}

	/**
	 * This method will get the Zap report folder.
	 * 
	 * @return
	 */
	public String getZapReportsDirectory() {
		return String.format("%s", m_zapReportsDirectory).replace("\\", "/");

	}

	/**
	 * Method to get CSV file path
	 * 
	 * @return
	 */
	protected String getCSVFileName() {
		return String.format("%s/%s", getBaseDirWithTodayDate(), m_csvFileName);
	}

	

	/**
	 * Gets Summary PieChart File Name
	 */
	protected String getSummaryPieChartName() {
		return String.format("%s/%s", getBaseDirWithTodayDate(), m_summaryImage);
	}

	/**
	 * Create document start page.
	 */
	protected void writeDocumentStart() {
		m_writer.println(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
		m_writer.print("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
	}

	/**
	 * Create document start page.
	 */
	protected void writeBuilderStart() {
		m_builder.append(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
		m_builder.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
	}

	/**
	 * Create header for page.
	 * 
	 * @param title
	 */
	protected void writeHeader(String title) {
		m_writer.print("<head>");
		m_writer.print(String.format("<title>%s</title>", title));
		writeStylesheet();
		m_writer.print("</head>");
	}

	protected void writeBuilderHeader() {
		m_builder.append("<head>");
		writeBuilderStyleSheet();
		m_builder.append("</head>");
	}

	/**
	 * Creates Body Start tag
	 */
	protected void writeBodyStart() {
		m_writer.print("<body>");
	}

	protected void writeBuilderBodyStart() {
		m_builder.append("<body>");
	}

	/**
	 * Create body end tag
	 */
	protected void writeBodyEnd() {
		m_writer.print("</body>");
	}

	protected void writeBuilderBodyEnd() {
		m_builder.append("</body>");
	}

	/**
	 * Document end.
	 */
	protected void writeDocumentEnd() {
		m_writer.print("</html>");
	}

	protected void writeBuilderDocumentEnd() {
		m_builder.append("</html>");
	}

	/**
	 * Writes a TH element with the specified contents and CSS class names.
	 * 
	 * @param html
	 *            the HTML contents
	 * @param cssClasses
	 *            the space-delimited CSS classes or null if there are no
	 *            classes to apply
	 */
	protected void writeTableHeader(String html, String cssClasses) {
		writeTag("th", html, cssClasses);
	}

	/**
	 * Writes a TD element with the specified contents.
	 * 
	 * @param html
	 *            the HTML contents
	 */
	protected void writeTableData(String html) {
		writeTableData(html, null);
	}

	/**
	 * Writes a TD element with the specified contents and CSS class names.
	 * 
	 * @param html
	 *            the HTML contents
	 * @param cssClasses
	 *            the space-delimited CSS classes or null if there are no
	 *            classes to apply
	 */
	protected void writeTableData(String html, String cssClasses) {
		writeTag("td", html, cssClasses);
	}

	protected void writeBuilderTableData(String html, String cssClasses) {
		writeBuilderTag("td", html, cssClasses);
	}

	protected String getBuilderTableData(String html, String cssClasses) {
		return getBuilderTag("td", html, cssClasses);
	}

	/**
	 * This method will create common header for any page.
	 */
	protected void writeFrameHeader() {
		m_writer.print("<table style=\"width: 100%;\">");
		m_writer.print("<tr>");
		m_writer.print("<td>");
		m_writer.print("<img src=\"benchmark.png\" alt=\"Aq4\" />");
		m_writer.print("</td>");
		m_writer.print("<td style=\"text-indent: 20px; vertical-align: middle;\">");
		m_writer.print("<h1>Test Results Report</h1>");
		m_writer.print("</td>");
		m_writer.print("<td style=\"text-align: right; width: 300px; vertical-align: bottom;\">");
		m_writer.print(String.format("<br /> Generated on %s",
				CommonUtil.getFormatedDate("EEEEE dd MMMMM yyyy 'at' hh:mm a zzz")));
		m_writer.print("</td>");
		m_writer.print("</tr>");
		writeLine("");
	}

	/**
	 * This will method will create the common menu and content TD to store
	 * data.
	 */
	protected void writeFrameContentStart() {
		m_writer.print("<tr style=\"vertical-align: top;\">");
		m_writer.print("<td class=\"menu\">");
		m_writer.print(String.format("<a href=\"%s\">Overview</a>", m_summaryReportName));
		m_writer.print("<br />");
		m_writer.print(String.format("<a href=\"%s\">View Details</a>", m_ViewDetailsReportName));
		m_writer.print("</td>");
		m_writer.print("<td style=\"width: 90%; height: 500px;\" colspan=\"2\">");
	}

	/**
	 * This method will write the end of content
	 */
	protected void writeFrameContentEnd() {
		m_writer.print("</td>");
		m_writer.print("</tr>");
	}

	/**
	 * This method will create common footer for any page.
	 */
	protected void writeFrameFooter() {
		m_writer.print("<tr>");
		writeLine(String.format("Copyright (c) %s MarketShare. All rights reserved.",
				CommonUtil.getFormatedDate("yyyy")));
		m_writer.print("</tr>");
	}

	/**
	 * This method will write data after content written to page
	 */
	protected void afterWriteData() {
		// Continue after writing (Summary or View Details Data)
		writeFrameContentEnd();
		writeFrameFooter();
		// End of Write Data (Summary or View Details Data)
		writeBodyEnd();
		writeDocumentEnd();
		closeWriter();
	}

	/**
	 * This method will write data before content to be written on page.
	 * 
	 * @param pageName
	 * @param headerTitle
	 * @param applicationName
	 */
	protected void beforeWriteData(String pageName, String headerTitle, String applicationName) {
		createWriter(pageName);
		writeDocumentStart();
		writeHeader(String.format("%s - %s", applicationName, headerTitle));
		writeBodyStart();

		// Write Data (Summary or View Details Data)
		writeFrameHeader();
		writeFrameContentStart();
	}

	protected void writeEmptyLine() {
		m_writer.print("<tr>");
		m_writer.print("<td>");
		m_writer.print("&nbsp;</td>");
		m_writer.print("</tr>");
	}

	// Private methods

	// This method will verify market share image is available or not and copies
	// that image.
	private void copyMarketShareImage() {
		System.out.println();
		System.out.println();
		String imageSourcePath = String.format("%s/src/test/resources/benchmark.png",
				System.getProperty("user.dir").replace("target", ""));
		String imageDestinationPath = String.format("%s/benchmark.png", getBaseDirWithTodayDate());
		if (!FileOperations.isFileExists(imageDestinationPath))
			FileOperations.copyImageOrFile(imageSourcePath, imageDestinationPath);
	}

	/*
	 * This will draw a line
	 */
	private void writeLine(String data) {
		if (data != null)
			m_writer.print(String.format("<tr><td class=\"line\" colspan=\"5\">%s</td></tr>", data));
		else
			m_writer.print("<tr><td class=\"line\" colspan=\"5\"></td></tr>");

	}

	/*
	 * Creates styles for custom report pages.
	 */
	private void writeStylesheet() {
		m_writer.print("<style type=\"text/css\">");
		m_writer.print("* {padding: 0; margin: 0;}");
		m_writer.print("a {color: #006699; font-size: 9pt;}");
		m_writer.print("a:visited {color: #006699; font-size: 9pt}");
		m_writer.print(
				"body {font-family: Lucida Sans Unicode, Lucida Grande, sans-serif; line-height: 1.8em; font-size: 50%; margin: 1.8em 1em;}");
		m_writer.print(
				"h1 {font-family: Arial, Helvetica, sans-serif; font-weight: bold; font-size: 2.7em; margin-bottom: 0.6667em;}");
		m_writer.print(
				"h2 {font-family: Arial, Helvetica, sans-serif; font-weight: bold; font-size: 1.8em; margin-bottom: 0;}");
		m_writer.print("td {font-size: 1.3em;}");
		m_writer.print(".header {font-size: 1.4em; font-weight: bold; text-align: left;}");
		m_writer.print(".passed {background-color: #44aa44;}");
		m_writer.print(".skipped {background-color: #ffaa00;}");
		m_writer.print(".failed {background-color: #ff4444;}");
		m_writer.print(".totalLabel {font-weight: bold; background-color: #ffffff;}");
		m_writer.print(".suite {background-color: #999999; font-weight: bold;}");
		m_writer.print(".test {background-color: #eeeeee; padding-left: 2em;}");
		m_writer.print(".test .passed {background-color: #88ee88;}");
		m_writer.print(".test .skipped {background-color: #ffff77;}");
		m_writer.print(".test .failed {background-color: #ff8888;}");
		m_writer.print(".group {background-color: #cccccc; color: #000000; font-weight: bold;}");
		m_writer.print(".duration {text-align: right;}");
		m_writer.print(".thread {white-space: nowrap;}");
		m_writer.print(
				".resultsTable  {border: 0; width: 90%; margin-top: 1.8em; line-height: 1.7em; border-spacing: 0.1em; font-size: 8.5px;}");
		m_writer.print(".resultsTable .method    {width: 18em;}");
		m_writer.print(".resultsTable .duration  {width: 6em;}");
		m_writer.print(".resultsTable td {vertical-align: top; padding: 0 1em;}");
		m_writer.print(".resultsTable th {padding: 0 .5em;}");
		m_writer.print(".number {text-align: right;}");
		m_writer.print(".zero {font-weight: normal;}");
		m_writer.print(".columnHeadings          {font-size: 1em;}");
		m_writer.print(".columnHeadings th {font-weight: normal;}");
		m_writer.print("#sidebarHeader {padding: 1.8em 1em; margin: 0 -1em 1.8em -1em;}");
		m_writer.print("#suites {line-height: 1.7em; border-spacing: 0.1em; width: 100%;}");
		m_writer.print(".suiteLinks {float: right; font-weight: normal; vertical-align: middle;}");
		m_writer.print(".tests {display: table-row-group;}");
		m_writer.print(".header.suite {cursor: pointer; clear: right; height: 1.214em; margin-top: 1px;}");
		m_writer.print("div.test {margin-top: 0.1em; clear: right; font-size: 1.3em;}");
		m_writer.print(
				".toggle {font-family: monospace; font-weight: bold; padding-left: 2px; padding-right: 5px; color: #777777;}");
		m_writer.print(
				".successIndicator {float: right; font-family: monospace; font-weight: bold; padding-right: 2px; color: #44aa44;}");
		m_writer.print(
				".skipIndicator {float: right; font-family: monospace; font-weight: bold; padding-right: 2px; color: #ffaa00;}");
		m_writer.print(
				".failureIndicator {float: right; font-family: monospace; font-weight: bold; padding-right: 2px; color: #ff4444;}");
		m_writer.print(".result {font-size: 1.1em; vertical-align: middle;}");
		m_writer.print(".dependency {font-family: Lucida Console, Monaco, Courier New, monospace; font-weight: bold;}");
		m_writer.print(".arguments {font-family: Lucida Console, Monaco, Courier New, monospace; font-weight: bold;}");
		m_writer.print(".testOutput {font-family: Lucida Console, Monaco, Courier New, monospace; color: #666666;}");
		m_writer.print(".stackTrace {font-size: 0.9em; line-height: 1.2em; margin-left: 2em; display: none;}");
		m_writer.print(".stackTrace .stackTrace  {font-size: inherit;}");
		m_writer.print(".description {border-bottom: 1px dotted #006699;}");
		m_writer.print(
				"#log {font-family: Lucida Console, Monaco, Courier New, monospace; font-size: 1.3em; margin-top: 1.8em;}");
		m_writer.print(
				".overviewTable {width: 90%; margin-top: 1.8em; line-height: 1.7em; border-spacing: 0.1em; font-size: 9px;}");
		m_writer.print(".overviewTable td {padding: 0 1em;}");
		m_writer.print(".overviewTable th {padding: 0 .5em;}");
		m_writer.print(".overviewTable .duration {width: 6em;}");
		m_writer.print(".overviewTable .passRate {width: 6em;}");
		m_writer.print(".overviewTable .number   {width: 5em;}");
		m_writer.print(".overviewTable tr {height: 1.6em;}");
		m_writer.print(
				".line {text-align: center; border-top-color: #999999; border-top-style: solid; border-top-width: 0.5px}");
		m_writer.print(
				".menu {width: 10%; border-right-color: #eeeeee; border-right-style: solid; border-right-width: 1px; }");
		m_writer.print(".note {font-style: italic; font-family: sans-serif; font-size: 8pt; color: #006699;}");
		m_writer.print("</style>");
	}

	private void writeBuilderStyleSheet() {
		m_builder.append(
				"<style type=\"text/css\">body {font-family: Lucida Sans Unicode, Lucida Grande, sans-serif; line-height: 1em; font-size: 50%; margin: 0em 0em;} a {color: #006699; font-size: 9pt;} a:visited {color: #006699; font-size: 9pt} .tablecenter { margin: auto; width: 100%;} .overviewTable {width: 90%; border-spacing: 0.1em; font-size: 10px;} .overviewTable td {padding: 0 .5em;}.overviewTable th {padding: 0 .5em;}  .header {font-size: 1.4em; background-color: #999999; font-weight: bold; text-align: left;} .successIndicator {float: right; font-family: monospace; font-weight: bold; font-size: 10px; padding-left: 0px; color: #44aa44; width:30%} .test {background-color: #eeeeee; padding-left: 2em; color: #006699;} .test .passed {background-color: #88ee88; text-align: right;} .test .skipped {background-color: #ffff77; text-align: right;} .test .failed {background-color: #ff8888; text-align: right;} .number {text-align: right;} .zero {text-align: right;} .duration {text-align: right;} .columnHeadings{background-color: #999988;} .summarymsg{color: #006699; font-weight: bold; font-size: 9px;}</style>");
	}

	/*
	 * Writes an arbitrary HTML element with the specified contents and CSS
	 * class names.
	 * 
	 * @param tag the tag name
	 * 
	 * @param html the HTML contents
	 * 
	 * @param cssClasses the space-delimited CSS classes or null if there are no
	 * classes to apply
	 */
	private void writeTag(String tag, String html, String cssClasses) {
		m_writer.print("<");
		m_writer.print(tag);
		if (cssClasses != null) {
			m_writer.print(" class=\"");
			m_writer.print(cssClasses);
			m_writer.print("\"");
		}
		m_writer.print(">");
		m_writer.print(html);
		m_writer.print("</");
		m_writer.print(tag);
		m_writer.print(">");
	}

	private void writeBuilderTag(String tag, String html, String cssClasses) {
		m_builder.append("<");
		m_builder.append(tag);
		if (cssClasses != null) {
			m_builder.append(" class=\"");
			m_builder.append(cssClasses);
			m_builder.append("\"");
		}
		m_builder.append(">");
		m_builder.append(html);
		m_builder.append("</");
		m_builder.append(tag);
		m_builder.append(">");
	}

	private String getBuilderTag(String tag, String html, String cssClasses) {
		StringBuilder temp = new StringBuilder();

		temp.append("<");
		temp.append(tag);
		if (cssClasses != null) {
			temp.append(" class=\"");
			temp.append(cssClasses);
			temp.append("\"");
		}
		temp.append(">");
		temp.append(html);
		temp.append("</");
		temp.append(tag);
		temp.append(">");

		return temp.toString();
	}
}
