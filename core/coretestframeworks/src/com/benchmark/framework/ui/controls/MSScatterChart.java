

package com.benchmark.framework.ui.controls;

import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.core.logger.Log;

/**
 * Class for Scatter Chart
 */
public class MSScatterChart extends BaseControl {

	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSScatterChart(String xpath) {
		super(xpath);
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSScatterChart(String findByType, String valueToFindElement) {
		super(findByType, valueToFindElement);
	}

	/**
	 * Method to click Allocation Scenario
	 * 
	 * @param scenarioId
	 */
	public void clickAllocateScenario(String scenarioId) {
		clickPlot(1, scenarioId);
	}

	/**
	 * Method to click Simulation Scenario
	 * 
	 * @param scenarioId
	 */
	public void clickSimulateScenario(String scenarioId) {
		clickPlot(0, scenarioId);
	}

	/**
	 * Method to click on scatter plot.
	 * 
	 * @param simulateOrAllocate
	 * @param scenarioId
	 */
	private void clickPlot(int simulateOrAllocate, String scenarioId) {

		StringBuilder script = new StringBuilder();
		script.append(String
				.format("var objects = scatterPlotChart.axes[0].chart.series[%d].data;",
						simulateOrAllocate));
		script.append(String
				.format("for (var i = 0; i<objects.length; i++) { var object = objects[i]; if (object.scenarioId == '%s') { return object.series.data[i].graphic.d.concat('±').concat(i); } }",
						scenarioId));
		Object value = getObject(script.toString());
		if (value == null) {
			Assert.assertNotNull(value, String.format(
					"ScenarioId,'%s' is not available", scenarioId));
		}
		String d = value.toString().split("±")[0];
		int i = Integer.parseInt(value.toString().split("±")[1]);
		Log.writeMessage(CLASS_NAME, d.toString());
		WebElement svg = webElement().findElement(
				createBy(FindByTypeConstants.TAG_NAME, "svg"));

		List<WebElement> markers = svg.findElements(createBy(
				FindByTypeConstants.CSS_SELECTOR,
				"g[class = 'highcharts-markers']"));

		List<WebElement> paths = markers.get(simulateOrAllocate).findElements(
				createBy(FindByTypeConstants.TAG_NAME, "path"));

		Collections.reverse(paths);
		int count = 0;
		for (WebElement path : paths) {
			if (path.getAttribute("d").equals(d) && count == i) {
				Log.writeMessage(CLASS_NAME, path.getAttribute("d"));
				path.click();
				break;
			}
			count++;
		}
	}

	// Constant variable
	private final static String CLASS_NAME = MSScatterChart.class.getName();
}
