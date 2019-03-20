

package com.benchmark.reports;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;

/**
 * JfreeChart is an open source library developed in Java, which can be used
 * within Java based applications to create a wide range of charts.
 */
public abstract class BaseReport {

	/**
	 * Method to generate Pie Chart by using JFreeChart tool
	 * 
	 * @param title
	 *            -> Title
	 * @param dataset
	 *            -> org.jfree.data.general.PieDataset
	 * @param legend
	 *            -> True/False
	 * @param tooltips
	 *            -> True/False
	 * @param urls
	 *            -> True/False
	 * @return -> org.jfree.chart.JFreeChart
	 */
	protected JFreeChart generatePieChart(String title, PieDataset dataset,
			boolean legend, boolean tooltips, boolean urls) {
		return generateChart(title, dataset, null, null, null, null, legend,
				tooltips, urls, ReportType.PIE);
	}

	/**
	 * Method to generate Bar Chart by using JFreeChart tool
	 * 
	 * @param title
	 *            -> Title
	 * @param categoryAxisLabel
	 *            -> Category Axis Label
	 * @param valueAxisLabel
	 *            -> Value Axis Label
	 * @param dataset
	 *            -> org.jfree.data.general.PieDataset
	 * @param orientation
	 *            -> org.jfree.chart.plot.PlotOrientation
	 * @param legend
	 *            -> True/False
	 * @param tooltips
	 *            -> True/False
	 * @param urls
	 *            -> True/False
	 * @return -> org.jfree.chart.JFreeChart
	 */
	protected JFreeChart generateBarChart(String title,
			String categoryAxisLabel, String valueAxisLabel,
			CategoryDataset dataset, PlotOrientation orientation,
			boolean legend, boolean tooltips, boolean urls) {
		return generateChart(title, null, categoryAxisLabel, valueAxisLabel,
				dataset, orientation, legend, tooltips, urls, ReportType.BAR);
	}

	/**
	 * Method to convert Char to Image(PNG)
	 * 
	 * @param fileName
	 *            -> File Name to generate PNG image
	 * @param chart
	 *            -> org.jfree.chart.JFreeChart
	 * @param width
	 *            -> PNG image width
	 * @param height
	 *            -> PNG Image Height
	 * @return -> True/False
	 */
	protected boolean convertChartToImage(String fileName, JFreeChart chart,
			int width, int height, ReportType reportType) {
		try {
			if (ReportType.PIE.equals(reportType)) {
				final PiePlot plot = (PiePlot) chart.getPlot();
				PieSectionLabelGenerator generator = new StandardPieSectionLabelGenerator(
						"{0} = {2}", new DecimalFormat("0"), new DecimalFormat(
								"0.00%"));
				plot.setLabelGenerator(generator);
			}

			File file = new File(fileName);
			ChartUtilities.saveChartAsPNG(file, chart, width, height);
			return true;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, e.getMessage());
		}
		return false;
	}

	/**
	 * Method to generate Chart by using JFreeChart Tool
	 * 
	 * @return -> org.jfree.chart.JFreeChart
	 */
	private JFreeChart generateChart(String title, PieDataset pieDataset,
			String categoryAxisLabel, String valueAxisLabel,
			CategoryDataset categoryDataset, PlotOrientation orientation,
			boolean legend, boolean tooltips, boolean urls,
			ReportType reportType) {
		JFreeChart jFreeChart = null;
		try {
			switch (reportType) {
			case BAR:
				jFreeChart = ChartFactory.createBarChart3D(title,
						categoryAxisLabel, valueAxisLabel, categoryDataset,
						orientation, legend, tooltips, urls);
				break;
			case PIE:
				jFreeChart = ChartFactory.createPieChart(title, pieDataset,
						legend, tooltips, urls);
				final PiePlot plot = (PiePlot) jFreeChart.getPlot();
				plot.setStartAngle(270);
				plot.setForegroundAlpha(0.60f);
				plot.setInteriorGap(0.02);
				plot.setLabelBackgroundPaint(Color.WHITE);
				plot.setBackgroundPaint(Color.WHITE);
				plot.setNoDataMessage("No data available");
				plot.setLabelFont(new Font("Arial", Font.PLAIN, 9));
				break;
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, e.getMessage());
		}
		return jFreeChart;
	}

	/**
	 * Enum for Report Types
	 */
	protected enum ReportType {
		PIE, BAR
	}

	/**
	 * Inner class which will render PIE chart with custom colors
	 */
	protected static class PieChartRenderer {
		private Color[] colors;

		public PieChartRenderer(Color[] colors) {
			this.colors = colors;
		}

		@SuppressWarnings("unchecked")
		public void setColor(PiePlot3D plot, DefaultPieDataset dataset) {
			@SuppressWarnings("rawtypes")
			List<Comparable> keys = dataset.getKeys();
			int aInt;

			for (int i = 0; i < keys.size(); i++) {
				aInt = i % this.colors.length;
				plot.setSectionPaint(keys.get(i), this.colors[aInt]);
			}
		}
	}

}
