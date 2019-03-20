

package com.benchmark.reports;

import java.awt.Color;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;

/**
 * All common reports related to all projects will be created by using
 * JFreeChart tool
 */
public class MSReports extends BaseReport {

	/**
	 * Method to generate automation summary report
	 * 
	 * @return -> True/False
	 */
	public boolean generateAutomationSummaryReport(Map<String, String> values,
			String fileName) {
		try {
			DefaultPieDataset dataset = new DefaultPieDataset();
			for (Map.Entry<String, String> entry : values.entrySet()) {
				dataset.setValue(entry.getKey(),
						Double.parseDouble(entry.getValue()));
			}
			JFreeChart jFreeChart = generatePieChart("Automation Status",
					dataset, true, true, false);

			final PiePlot plot = (PiePlot) jFreeChart.getPlot();
			plot.setLabelGap(0.02);

			for (String key : values.keySet()) {
				if (key.equals("Passed")) {
					System.setProperty("Passed", "#44aa44");
					Color passed = Color.getColor("Passed");
					plot.setSectionPaint("Passed", passed);
				} else if (key.equals("Skipped")) {
					System.setProperty("Skipped", "#ffaa00");
					Color skipped = Color.getColor("Skipped");
					plot.setSectionPaint("Skipped", skipped);
				} else if (key.equals("Failed")) {
					System.setProperty("Failed", "#ff4444");
					Color failed = Color.getColor("Failed");
					plot.setSectionPaint("Failed", failed);
				}
			}

			return convertChartToImage(fileName, jFreeChart, 400, 350,
					ReportType.PIE);
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, e.getMessage());
		}
		return false;
	}
}
