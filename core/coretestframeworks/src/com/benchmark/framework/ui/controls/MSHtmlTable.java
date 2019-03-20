
package com.benchmark.framework.ui.controls;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.benchmark.core.constants.FindByTypeConstants;
import com.benchmark.core.util.CommonUtil;
import com.benchmark.framework.ui.SeleniumWrapper;

/**
 * Class for HTML Table, which holds grid control.
 */
public class MSHtmlTable extends BaseControl {

	private List<WebElement> m_rows = null;

	/**
	 * Constructor with XPath
	 * 
	 * @param xpath
	 */
	public MSHtmlTable(String xpath) {
		super(xpath);
		bindRows();
	}

	/**
	 * Constructor with findByType.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSHtmlTable(String findByType, String valueToFindElement) {
		super(findByType, valueToFindElement);
		bindRows();
	}

	/**
	 * Constructor with WebElement
	 * 
	 * @param element
	 * @param findByType
	 * @param valueToFindElement
	 */
	public MSHtmlTable(WebElement element, String findByType,
			String valueToFindElement) {
		super(element, findByType, valueToFindElement);
		bindRows();
	}

	// Public Methods

	/**
	 * Method to click CheckBox on grid body data.
	 * 
	 * @param text
	 *            (Text to identify the row.)
	 * @param findByType
	 *            (Type to identify CheckBox)
	 * @param valueToFindElement
	 *            (Value to find CheckBox)
	 * @param isCheck
	 *            (True to check, false to un-check the CheckBox
	 */
	public void clickCheckBoxOnDataRow(String text, String findByType,
			String valueToFindElement, boolean isCheck) {
		clickCheckBox(text, findByType, valueToFindElement, isCheck, "td", true);
	}

	/**
	 * Method to click CheckBox on grid Header.
	 * 
	 * @param text
	 *            (Text to identify the row.)
	 * @param findByType
	 *            (Type to identify CheckBox)
	 * @param valueToFindElement
	 *            (Value to find CheckBox)
	 * @param isCheck
	 *            (True to check, false to un-check the CheckBox
	 */
	public void clickCheckBoxOnHeaderRow(String text, String findByType,
			String valueToFindElement, boolean isCheck) {
		clickCheckBox(text, findByType, valueToFindElement, isCheck, "th", true);
	}

	/**
	 * Click on particular data cell on table.
	 * 
	 * @param text
	 */
	public void clickOnDataCell(String text) {
		clickOnCell(text, "td", false);
	}

	/**
	 * Method to click on Data Cell using JavaScript. This method can be used
	 * only when data cell having class attribute else we can't use this method.
	 * 
	 * @param text
	 *            -> Text to find data cell
	 * @param attribute
	 *            -> Attribute value to find
	 */
	public void clickOnDataCell(String text, String attribute) {
		WebElement element = getRowOrColumn(text, "td", false);
		String value = String
				.format("var elements = window.document.getElementsByClassName('%s'); for (var i = 0; i<elements.length; i++) { var element = elements[i]; if (element.%s == '%s') { element.click(); } }",
						element.getAttribute("class"), attribute,
						element.getAttribute(attribute));
		SeleniumWrapper.getJavascriptExecutor().executeScript(value);
	}

	/**
	 * Click on particular header cell on table
	 * 
	 * @param text
	 */
	public void clickOnHeaderCell(String text) {
		clickOnCell(text, "th", false);
	}

	/**
	 * Returns the values of particular column index values of all selected
	 * CheckBox rows as String Array.
	 * 
	 * @param findByType
	 *            Element to find CheckBox type
	 * @param valueToFindElement
	 *            Value to find CheckBox
	 * @param columnIndex
	 *            ColumnIndex
	 * @return String Array
	 */
	public String[] getSelectedCheckBoxCellValues(String findByType,
			String valueToFindElement, int columnIndex) {
		// System.out.println("Selected Rows");
		List<WebElement> rows = getSelectedCheckBoxRows(findByType,
				valueToFindElement);
		List<String> selectedCellValues = new ArrayList<String>();
		for (WebElement row : rows) {
			List<WebElement> cols = getChildElements(row,
					new By.ByTagName("td"));
			selectedCellValues.add(cols.get(columnIndex).getText().trim());
		}
		return selectedCellValues
				.toArray(new String[selectedCellValues.size()]);
	}

	/**
	 * Get selected CheckBox count
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @return
	 */
	public int getSelectedCheckBoxCount(String findByType,
			String valueToFindElement) {
		return getSelectedCheckBoxRows(findByType, valueToFindElement).size();
	}

	/**
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @param howManyToUnCheck
	 */
	public void deSelectChecBoxes(String findByType, String valueToFindElement,
			int howManyToUnCheck) {
		deSelectTheCheckBoxes(findByType, valueToFindElement, howManyToUnCheck);
	}

	/**
	 * Method to get number of rows in a table
	 */
	public int getRowCount() {
		if (m_rows != null) {
			return m_rows.size() - 1;
		}
		return 0;
	}

	/**
	 * Method to get number of columns in a row for that table
	 */
	public int getColumnCount() {
		if (m_rows != null) {
			WebElement row = m_rows.get(0);
			List<WebElement> cols = getChildElements(row,
					new By.ByTagName("th"));
			return cols.size();
		}
		return 0;
	}

	/**
	 * Method to get All Rows values by column index.
	 * 
	 * @param columnIndex
	 *            -> Column Index
	 * @return -> List[String]
	 */
	public List<String> getAllRowValuesByColumnIndex(int columnIndex) {
		if (m_rows != null) {
			List<String> values = new ArrayList<String>();
			int i = 0;
			for (WebElement row : m_rows) {
				if (i == 0) {
					i++;
					continue;
				}
				List<WebElement> cols = getChildElements(row, new By.ByTagName(
						"td"));
				values.add(cols.get(columnIndex).getText().trim());
			}
			return values;
		}
		return null;
	}

	/**
	 * Method to get All Rows values by column index.
	 * 
	 * @param columnIndex
	 *            -> Column Index
	 * @return -> List[String]
	 */
	public List<String> getAllRowValuesByColumnIndexFromTableBody(
			int columnIndex) {
		if (m_rows != null) {
			List<String> values = new ArrayList<String>();
			for (WebElement row : m_rows) {
				List<WebElement> cols = getChildElements(row, new By.ByTagName(
						"td"));
				values.add(cols.get(columnIndex).getText().trim());
			}
			return values;
		}
		return null;
	}

	/**
	 * Method to click Link on Data cell
	 */
	public void clickLinkOnDataCell(String columnValue) {
		clickOnHyperLink(getRowOrColumn(columnValue, "td", false), columnValue);
	}

	/**
	 * Method to perform action like Edit And Delete
	 */
	public void clickOnActionItem(String columnValue, String actionItem) {
		List<WebElement> cols = getSelectedRowColumns(columnValue);
		for (WebElement col : cols) {
			if (col.getText().trim().toLowerCase()
					.contains(actionItem.trim().toLowerCase())) {
				clickOnHyperLink(col, actionItem);
			}
		}
	}

	/**
	 * Method to get column names
	 */
	public List<String> getColumnNames() {
		List<WebElement> rows = getChildElements(new By.ByTagName("tr"));
		List<String> columnNames = new ArrayList<String>();
		for (WebElement row : rows) {
			List<WebElement> cols = row.findElements(new By.ByTagName("th"));
			for (WebElement col : cols) {
				columnNames.add(col.getText().trim());
			}
		}
		return columnNames;
	}

	/**
	 * Method to get the Table column index by column Name
	 */
	public int getColumnIndexByColumnName(String columnName) {
		List<String> columnNames = getColumnNames();
		for (int i = 0; i <= columnNames.size() - 1; i++) {
			if (columnNames.get(i).toLowerCase().trim()
					.equals(columnName.toLowerCase().trim())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Method to get Row labels
	 * 
	 * @return -> List of labels
	 */
	public List<String> getRowLabels() {
		// We can't use m_rows here because it includes tr elements from the
		// table header
		List<WebElement> rows = getChildElements(By.cssSelector("tbody tr"));
		// Use 'getColumnNames()' for cases where the tables are stored in TH
		// elements
		List<String> labels = new ArrayList<String>();
		for (WebElement row : rows) {
			labels.add(row.findElement(By.tagName("td")).getText());
		}
		return labels;
	}

	/**
	 * Find row and return element
	 * 
	 * @param cellValue
	 *            -> Cell Value
	 * @return -> MSGenericElement
	 */
	public MSGenericElement findRow(String cellValue) {
		return this.getChild(FindByTypeConstants.XPATH, ".//td[contains(.,'"
				+ cellValue + "')]/..", MSGenericElement.class);
	}

	/**
	 * Method to select the value in the DropDownList of given table row.
	 * 
	 * @param findRowByValue
	 *            -> Row value to get Row from the table
	 * @param columnIndex
	 *            -> column index to select the value
	 * @param selectValue
	 *            -> Value to select in Select Option list
	 */
	public void selectValueFromOptionList(String findRowByValue,
			String findByColumnValue, String selectValue) {
		getSelectOptionFromTableRow(findRowByValue, findByColumnValue)
				.selectOption(selectValue);
	}

	/**
	 * Gets default selected value from selectOption values
	 */
	public String getDefaultValueFromSelectOption(String findRowByValue,
			String findByColumnValue) {
		return getSelectOptionFromTableRow(findRowByValue, findByColumnValue)
				.getSelectedValue();
	}

	/**
	 * Gets all values from the DropDownList of particular table row.
	 */
	public List<String> getSelectOptionValuesFromTableRow(
			String findRowByValue, String findByColumnValue) {
		return getSelectOptionFromTableRow(findRowByValue, findByColumnValue)
				.getAllElements();
	}

	/**
	 * Method to check Header column CheckBox is checked or not
	 */
	public boolean isHeaderColumnCheckBoxChecked(String columnName) {
		return getHeaderColumnCheckBoxByColumnName(columnName).isChecked();
	}

	/**
	 * Method to click header column CheckBox
	 */
	public void clickHeaderColumnCheckBox(String columnName, boolean isChecked) {
		MSCheckBox checkBox = getHeaderColumnCheckBoxByColumnName(columnName);
		if (isChecked) {
			checkBox.check();
		} else {
			checkBox.unCheck();
		}
	}

	/**
	 * Method to check the CheckBox control in the table row.
	 * 
	 * @param findRowByValue
	 * @param findByColumnValue
	 * @param isChecked
	 */
	public void clickDataColumnCheckBox(String findRowByValue,
			String findByColumnValue, boolean isChecked) {
		MSCheckBox checkBox = getCheckBoxFromTableRow(findRowByValue,
				findByColumnValue);
		if (isChecked) {
			checkBox.check();
		} else {
			checkBox.unCheck();
		}
	}

	/**
	 * Method to verify CheckBox in the data cell is checked or not
	 * 
	 * @param findRowByValue
	 * @param findByColumnValue
	 * @return
	 */
	public boolean isDataCellCheckBoxChecked(String findRowByValue,
			String findByColumnValue) {
		return getCheckBoxFromTableRow(findRowByValue, findByColumnValue)
				.isChecked();
	}

	/**
	 * Method to verify CheckBox in the data cell is enabled or not
	 * 
	 * @param findRowByValue
	 * @param findByColumnValue
	 * @return
	 */
	public boolean isDataCellCheckBoxEnabled(String findRowByValue,
			String findByColumnValue) {
		return getCheckBoxFromTableRow(findRowByValue, findByColumnValue)
				.isEnabled();
	}

	/**
	 * Method to check the CheckBox control in the table row.
	 * 
	 * @param findRowByValue
	 * @param findByColumnValue
	 * @param isChecked
	 */
	public void clickDataColumnCheckBoxByJS(String findRowByValue,
			String findByColumnValue, boolean isChecked) {
		MSCheckBox checkBox = getCheckBoxFromTableRow(findRowByValue,
				findByColumnValue);
		if (isChecked) {
			checkBox.check(true);
		} else {
			checkBox.unCheck(true);
		}
	}

	/**
	 * Method to click header column CheckBox by JS click event
	 */
	public void clickHeaderColumnCheckBoxByJS(String columnName,
			boolean isChecked) {
		MSCheckBox checkBox = getHeaderColumnCheckBoxByColumnName(columnName);
		if (isChecked) {
			checkBox.check(true);
		} else {
			checkBox.unCheck(true);
		}
	}

	/**
	 * Method to check select option list is enabled or not.
	 */
	public boolean isSelectOptionListEnabled(String findRowByValue,
			String findByColumnValue) {
		return getSelectOptionFromTableRow(findRowByValue, findByColumnValue)
				.isEnabled();
	}

	/**
	 * Method to key-in value in the TextBox of particular row and column
	 * 
	 * @param findRowByValue
	 * @param findByColumnValue
	 * @param keysToSend
	 */
	public void inputValue(String findRowByValue, String findByColumnValue,
			String keysToSend) {
		MSTextBox textBox = getTextBoxFromTableRow(findRowByValue,
				findByColumnValue);
		textBox.clear();
		try {
			textBox.type(keysToSend);
		} catch (Exception e) {
			textBox.type(keysToSend);
		}
	}

	/**
	 * Method to get All CheckBoxes from particular column
	 * 
	 * @param findByColumnValue
	 * @return -> List<MSCheckBox>
	 */
	public List<MSCheckBox> getAllCheckBoxesFromColumn(String findByColumnValue) {
		int columnIndex = getColumnIndexByColumnName(findByColumnValue);
		List<MSCheckBox> finalCheckBoxes = new ArrayList<MSCheckBox>();
		List<WebElement> rows = getChildElements(new By.ByTagName("tr"));
		for (WebElement row : rows) {
			List<WebElement> cols = row.findElements(new By.ByTagName("td"));
			if (cols.size() > 0) {
				MSCheckBox checkBox = new MSCheckBox(cols.get(columnIndex),
						FindByTypeConstants.CSS_SELECTOR,
						"input[type='checkbox']");
				finalCheckBoxes.add(checkBox);
			}
		}
		return finalCheckBoxes;
	}

	/**
	 * Method to get All SelectOptions from particular column
	 * 
	 * @param findByColumnValue
	 * @return -> List<MSSelectOption>
	 */
	public List<MSSelectOption> getAllSelectOptionsFromColumn(
			String findByColumnValue) {
		int columnIndex = getColumnIndexByColumnName(findByColumnValue);
		List<MSSelectOption> finalList = new ArrayList<MSSelectOption>();
		List<WebElement> rows = getChildElements(new By.ByTagName("tr"));
		for (WebElement row : rows) {
			List<WebElement> cols = row.findElements(new By.ByTagName("td"));
			if (cols.size() > 0) {
				MSSelectOption selectOption = new MSSelectOption(
						cols.get(columnIndex), FindByTypeConstants.TAG_NAME,
						"select");
				finalList.add(selectOption);
			}
		}
		return finalList;
	}

	// -------------------------------------------------------------------------------------------------------------------------------
	// Private Methods

	/**
	 * Bind all table rows
	 */
	private void bindRows() {
		if (webElement() != null) {
			m_rows = getChildElements(new By.ByTagName("tr"));
		}
	}

	/**
	 * Method to get Header Column by header column name
	 */
	private MSCheckBox getHeaderColumnCheckBoxByColumnName(String columnName) {
		List<WebElement> rows = getChildElements(new By.ByTagName("tr"));
		for (WebElement row : rows) {
			List<WebElement> cols = row.findElements(new By.ByTagName("th"));
			if (cols.size() > 0) {
				WebElement col = cols
						.get(getColumnIndexByColumnName(columnName));
				return new MSCheckBox(col, FindByTypeConstants.CSS_SELECTOR,
						"input[type='checkbox']");
			}
		}
		return null;
	}

	/**
	 * Click particular CheckBox on table
	 */
	private void clickCheckBox(String text, String findByType,
			String valueToFindElement, boolean isCheck, String headerOrDataTag,
			boolean isRow) {
		MSCheckBox checkBox = new MSCheckBox(getRowOrColumn(text,
				headerOrDataTag, isRow), findByType, valueToFindElement);
		// System.out.println(checkBox.isChecked());
		if (isCheck) {
			// System.out.println("checkbox selected");
			checkBox.check(true);
		} else {
			// System.out.println("Checkbox de-selected");
			checkBox.unCheck(true);
		}
		// System.out.println(checkBox.isChecked());
	}

	/**
	 * Click on particular cell on table
	 * 
	 * @param text
	 * @param headerOrDataTag
	 * @param isRow
	 */
	private void clickOnCell(String text, String headerOrDataTag, boolean isRow) {
		getRowOrColumn(text, headerOrDataTag, isRow).click();
	}

	/**
	 * Gets Row/Column based on parameters.
	 * 
	 * @param text
	 * @param headerOrDataTag
	 * @param isRow
	 * @return
	 */
	private WebElement getRowOrColumn(String text, String headerOrDataTag,
			boolean isRow) {
		List<WebElement> rows = getChildElements(new By.ByTagName("tr"));
		// System.out.println("Rows: " + rows.size());
		for (WebElement row : rows) {
			// getChildElements returns every TD element on the page for table
			// header rows
			if (row.findElements(By.tagName(headerOrDataTag)).isEmpty()) {
				continue;
			}
			List<WebElement> cols = getChildElements(row, new By.ByTagName(
					headerOrDataTag));
			// System.out.println("Cols: " + cols.size());
			for (WebElement col : cols) {
				// System.out.println(col.getText());
				// System.out.println(col.getAttribute("title"));
				if (col.getText().trim().toLowerCase()
						.equals(text.trim().toLowerCase())) {
					if (isRow) {
						SeleniumWrapper.scrollIntoView(row);
						return row;
					} else {
						SeleniumWrapper.scrollIntoView(col);
						return col;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Gets selected CheckBoxes from the list.
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @return
	 */
	private List<WebElement> getSelectedCheckBoxRows(String findByType,
			String valueToFindElement) {
		List<WebElement> selectedRows = new ArrayList<>();
		for (WebElement row : m_rows) {
			MSCheckBox checkBox = null;
			try {
				checkBox = new MSCheckBox(row, findByType, valueToFindElement);
				// System.out.println(checkBox.isChecked());
				if (checkBox.isChecked()) {
					selectedRows.add(row);
				}
			} catch (Exception e) {
			}
		}
		return selectedRows;
	}

	/**
	 * 
	 * @param findByType
	 * @param valueToFindElement
	 * @param howManyToUnCheck
	 */
	private void deSelectTheCheckBoxes(String findByType,
			String valueToFindElement, int howManyToUnCheck) {
		int count = 0;
		for (WebElement row : m_rows) {
			MSCheckBox checkBox = null;
			try {
				checkBox = new MSCheckBox(row, findByType, valueToFindElement);
				if (count == howManyToUnCheck) {
					return;
				}
				checkBox.unCheck(true);
				count++;
			} catch (Exception e) {

			}
		}
	}

	/**
	 * Method selected row columns
	 */
	private List<WebElement> getSelectedRowColumns(String columnValue) {
		List<WebElement> rows = getChildElements(new By.ByTagName("tr"));
		for (WebElement row : rows) {
			List<WebElement> cols = row.findElements(new By.ByTagName("td"));
			for (WebElement col : cols) {
				if (col.getText().trim().toLowerCase()
						.equals(columnValue.trim().toLowerCase())) {
					return cols;
				}
			}
		}
		return null;
	}

	/**
	 * Method to click on HyperLink
	 */
	private void clickOnHyperLink(WebElement element, String linkText) {
		MSHyperLink link = new MSHyperLink(element,
				FindByTypeConstants.LINK_TEXT, linkText);
		link.click();
	}

	/**
	 * Method to find SelectOption in the table row by row and column values
	 */
	private MSSelectOption getSelectOptionFromTableRow(String findRowByValue,
			String findByColumnValue) {
		try {
			MSSelectOption option = new MSSelectOption(getDataCell(
					findRowByValue, findByColumnValue),
					FindByTypeConstants.TAG_NAME, "select");
			return option;
		} catch (NoSuchElementException nsee) {
			Assert.assertTrue(
					false,
					String.format(
							"Unable to locate SelectOptionList in the table row of %s and column %s",
							findRowByValue, findByColumnValue));
		} catch (Exception e) {
			Assert.assertTrue(
					false,
					"Exception: " + e.getMessage() != null ? e.getMessage() : e
							.toString());
		}
		return null;
	}

	/**
	 * Method to get CheckBox control from the table row
	 * 
	 * @param findRowByValue
	 * @param findByColumnValue
	 * @return
	 */
	private MSCheckBox getCheckBoxFromTableRow(String findRowByValue,
			String findByColumnValue) {
		try {
			MSCheckBox checkBox = new MSCheckBox(getDataCell(findRowByValue,
					findByColumnValue), FindByTypeConstants.CSS_SELECTOR,
					"input[type='checkbox']");
			return checkBox;
		} catch (NoSuchElementException nsee) {
			Assert.assertTrue(
					false,
					String.format(
							"Unable to locate checkbox in the table row of %s and column %s",
							findRowByValue, findByColumnValue));
		} catch (Exception e) {
			Assert.assertTrue(
					false,
					"Exception: " + e.getMessage() != null ? e.getMessage() : e
							.toString());
		}
		return null;
	}

	/**
	 * Method to get TextBox control from the particular table row and column
	 * 
	 * @param findRowByValue
	 * @param findByColumnValue
	 * @return
	 */
	private MSTextBox getTextBoxFromTableRow(String findRowByValue,
			String findByColumnValue) {
		try {
			return new MSTextBox(
					getDataCell(findRowByValue, findByColumnValue),
					FindByTypeConstants.CSS_SELECTOR, "input[type='text']");
		} catch (NoSuchElementException nsee) {
			Assert.assertTrue(
					false,
					CommonUtil.appendErrorCodeToMessage(String.format(
							"Unable to locate checkbox in the table row of %s and column %s",
							findRowByValue, findByColumnValue)));
		} catch (Exception e) {
			Assert.assertTrue(
					false,
					"Exception: " + e.getMessage() != null ? e.getMessage() : e
							.toString());
		}
		return null;
	}

	/**
	 * Method get data cell from the table row
	 * 
	 * @param findRowByValue
	 * @param findByColumnValue
	 * @return
	 */
	private WebElement getDataCell(String findRowByValue,
			String findByColumnValue) {
		List<WebElement> cols = getSelectedRowColumns(findRowByValue);
		return cols.get(getColumnIndexByColumnName(findByColumnValue));
	}

}
