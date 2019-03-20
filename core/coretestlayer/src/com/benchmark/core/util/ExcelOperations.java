
package com.benchmark.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.constants.WorkbookTypeConstants;
import com.benchmark.core.logger.Log;

public class ExcelOperations implements AutoCloseable {

	/*
	 * Holds the current sheet details of workbook which is in memory
	 */
	private Sheet m_sheet = null;
	/*
	 * Holds the number of columns in the current sheet of workbook which is in
	 * memory
	 */
	private int m_sheetColumns = -1;
	/*
	 * This will contains the number of row available in the current sheet which
	 * is in memory.
	 */
	private int m_sheetRows = -1;
	/*
	 * This will holds the current workbook.
	 */
	private Workbook m_workbook = null;
	/*
	 * Holds the file path.
	 */
	private String m_filePath = null;

	// Constructors

	/**
	 * ctor - create instance with XSSFWORKBOOK type.
	 * 
	 * @throws Exception
	 */
	public ExcelOperations() throws Exception {
		this("", "", WorkbookTypeConstants.XSSFWORKBOOK);
	}

	/**
	 * ctor- use this constructor when we need to create a new excel file.
	 * 
	 * @param workbookType
	 * @throws Exception
	 */
	public ExcelOperations(WorkbookTypeConstants workbookType) throws Exception {
		this("", "", workbookType);
	}

	/**
	 * ctor- To read existing excel file.
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	public ExcelOperations(String fileName) throws Exception {
		this(fileName, "", WorkbookTypeConstants.FACTORY);
	}

	/**
	 * ctor - To read existing excel file and particular sheet.
	 * 
	 * @param fileName
	 * @param sheetName
	 * @throws Exception
	 */
	public ExcelOperations(String fileName, String sheetName) throws Exception {
		this(fileName, sheetName, WorkbookTypeConstants.FACTORY);
	}

	/**
	 * ctor - To read existing excel file and particular sheet.
	 * 
	 * @param fileName
	 * @param sheetName
	 * @param workbookType
	 * @throws Exception
	 */
	public ExcelOperations(String fileName, String sheetName, WorkbookTypeConstants workbookType) throws Exception {
		createWorkbook(fileName, workbookType);
		getSheetDetails(sheetName);
	}

	// End of Constructors

	// Public Methods

	/**
	 * This method will convert the CSV file to excel file. By default sheet
	 * name will be sheet1 and extension will be ".xlsx"
	 * 
	 * @param csvFilePath
	 *            (Absolute path of csv file)
	 * @return Newly created excel file path with file name.
	 * @throws Exception
	 */
	public String convertCSVToExcel(String csvFilePath) throws Exception {
		return convertCSVToExcel(csvFilePath, "sheet1");
	}

	/**
	 * This method will convert the CSV file to excel file. By default extension
	 * will be ".xlsx"
	 * 
	 * @param csvFileName
	 *            (Absolute path of csv file)
	 * @param sheetName
	 *            (input sheet name)
	 * @return Newly created excel file path with file name.
	 * @throws Exception
	 */
	public String convertCSVToExcel(String csvFilePath, String sheetName) throws Exception {
		return convertCSVToExcel(csvFilePath, sheetName, ".xlsx");
	}

	/**
	 * This method will convert the CSV file to Excel file.
	 * 
	 * @param csvFilePath
	 *            (Absolute path of csv file)
	 * @param sheetName
	 *            (input sheet name)
	 * @param excelExtn
	 *            (.xls or .xlsx)
	 * @return Newly created excel file path with file name.
	 * @throws Exception
	 */
	public String convertCSVToExcel(String csvFilePath, String sheetName, String excelExtn) throws Exception {
		try {
			String currentLine = null;
			int RowNum = 0;

			try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFilePath))) {
				m_sheet = m_workbook.createSheet(sheetName);
				while ((currentLine = bufferedReader.readLine()) != null) {
					String str[] = currentLine.split(",");
					Row currentRow = m_sheet.createRow(RowNum);
					RowNum++;
					for (int i = 0; i < str.length; i++) {
						Double doubleValue = null;
						String cellValue = str[i];
						try {
							doubleValue = Double.parseDouble(cellValue);
							currentRow.createCell(i).setCellValue(doubleValue.doubleValue());
						} catch (NumberFormatException ex) {
							currentRow.createCell(i).setCellValue(cellValue);
						}
					}
				}
			}

			try (FileOutputStream fileOutputStream = new FileOutputStream(m_filePath)) {
				m_workbook.write(fileOutputStream);
			}
			return m_filePath;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.getMessage());
		}
		return null;
	}

	/**
	 * This method will convert the excel file to CSV file and store the file in
	 * default location.
	 * 
	 * @return new CSV File Path.
	 * @throws Exception
	 */
	public String convertExcelToCSV() throws Exception {
		return convertExcelToCSV(TEST_DATA_PATH);
	}

	/**
	 * This method will covert the excel file to CSV file and store the file in
	 * particular destination folder.
	 * 
	 * @param destinationFolder
	 * @return new CSV file path.
	 * @throws Exception
	 */
	public String convertExcelToCSV(String destinationFolder) throws Exception {
		if (m_filePath != null) {
			File excelFile = new File(m_filePath);
			String excelFileName = excelFile.getName();
			String extension = excelFileName.substring(excelFileName.lastIndexOf('.') + 1);
			String csvFilePath = String.format("%s%s", destinationFolder, excelFileName.replace(extension, "csv"));
			Log.writeMessage(CLASS_NAME, m_filePath);
			Log.writeMessage(CLASS_NAME, csvFilePath);
			// m_sheet = m_workbook.getSheetAt(0);
			Log.writeMessage(CLASS_NAME, "Sheet Name: " + m_sheet.getSheetName());
			FileOutputStream fileOutputStream = null;

			try {
				// For storing data into CSV files
				StringBuffer csvData = new StringBuffer();
				fileOutputStream = new FileOutputStream(csvFilePath);

				for (Row row : m_sheet) {
					for (Cell cell : row) {
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_BOOLEAN:
							csvData.append(cell.getBooleanCellValue() + ",");
							break;
						case Cell.CELL_TYPE_NUMERIC:
							double dblValue = cell.getNumericCellValue();
							String dblString = CommonUtil.customDecimalFormat("#", dblValue);
							// This will fail when numeric value has decimals
							// expected on it.
							csvData.append('"' + dblString + '"' + ',');
							break;
						case Cell.CELL_TYPE_STRING:
							csvData.append('"' + cell.getStringCellValue() + '"' + ',');
							break;
						case Cell.CELL_TYPE_BLANK:
							csvData.append("" + ',');
							break;
						default:
							csvData.append('"' + cell.toString() + '"' + ",");
						}
					}
					csvData.deleteCharAt(csvData.length() - 1);
					csvData.append("\r\n");
				}

				fileOutputStream.write(csvData.toString().getBytes());
				return csvFilePath;
			} catch (Exception e) {
				Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.getMessage());
			} finally {
				if (fileOutputStream != null)
					fileOutputStream.close();
			}

		} else {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, "File doesn't exists.");
		}
		return null;
	}

	/**
	 * This method will return the cell value based on row number and cell
	 * number.
	 * 
	 * @param rowNum
	 * @param cellNum
	 * @return
	 */
	public String getCellValue(int rowNum, int cellNum) {
		try {
			Row row = m_sheet.getRow(rowNum);
			Cell cell = row.getCell(cellNum);
			return cell.toString();
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * This method will return the cell value based on row number and cell
	 * number for a particular sheet.
	 * 
	 * @param rowNum
	 * @param cellNum
	 * @param sheetName
	 * @return
	 */
	public String getCellValue(int rowNum, int cellNum, String sheetName) {
		getSheetDetails(sheetName);
		return getCellValue(rowNum, cellNum);
	}

	/**
	 * This method will return the cell value based on row text and column name
	 * for a particular sheet.
	 * 
	 * @param rowText
	 * @param columnName
	 * @param sheetName
	 * @return cell value
	 */
	public String getCellValueBasedOnRowColumnName(String rowText, String columnName, String sheetName) {
		getSheetDetails(sheetName);
		return getCellValueBasedOnRowColumnName(rowText, columnName);
	}

	/**
	 * This method will return the cell value based on the row text and column
	 * name from a sheet which is already loaded in memory.
	 * 
	 * @param rowText
	 * @param columnName
	 * @return
	 */
	public String getCellValueBasedOnRowColumnName(String rowText, String columnName) {
		int rowNum = getRowNumber(rowText);
		int cellNum = getColumnNumber(columnName);
		return getCellValue(rowNum, cellNum);
	}

	/**
	 * This method will return the column number based on the column name.
	 * 
	 * @param columnName
	 * @return
	 */
	public int getColumnNumber(String columnName) {
		return getCellOrRowNumberBasedOnText(columnName, 0);
	}

	/**
	 * This method will return the column number based on the column name for a
	 * particular sheet.
	 * 
	 * @param columnName
	 * @param sheetName
	 * @return
	 */
	public int getColumnNumber(String columnName, String sheetName) {
		getSheetDetails(sheetName);
		return getCellOrRowNumberBasedOnText(columnName, 0);
	}

	/**
	 * This method will return the number of columns available in the sheet. If
	 * sheet details are already loaded in memory means else returns -1.
	 * 
	 * @return
	 */
	public int getNumberOfColumns() {
		return getNumberOfColumns(0);
	}

	/**
	 * This method will return the number of columns based on the row number in
	 * the existing loaded in the memory.
	 * 
	 * @param rowNumber
	 * @return
	 */
	public int getNumberOfColumns(int rowNumber) {
		if (m_sheet != null)
			return m_sheet.getRow(rowNumber).getPhysicalNumberOfCells();
		return m_sheetColumns;
	}

	/**
	 * This method will returns the number of columns available for the given
	 * row number based on the sheet name passed by the user.
	 * 
	 * @param rowNumber
	 * @param sheetName
	 * @return
	 */
	public int getNumberOfColumns(int rowNumber, String sheetName) {
		getSheetDetails(sheetName);
		return getNumberOfColumns(rowNumber);
	}

	/**
	 * This Method will return the number of rows available in the sheet. If
	 * Sheet details are already loaded in memory means else returns -1.
	 * 
	 * @return
	 */
	public int getNumberOfRows() {
		return m_sheetRows;
	}

	/**
	 * 
	 * @param sheetName
	 * @return
	 */
	public int getNumberOfRows(String sheetName) {
		getSheetDetails(sheetName);
		return m_sheetRows;
	}

	/**
	 * This method will return the row number based on text.
	 * 
	 * @param rowText
	 * @return
	 */
	public int getRowNumber(String rowText) {
		return getCellOrRowNumberBasedOnText(rowText, 1);
	}

	/**
	 * This method will return the row number based on text for a particular
	 * sheet.
	 * 
	 * @param rowText
	 * @param sheetName
	 * @return
	 */
	public int getRowNumber(String rowText, String sheetName) {
		getSheetDetails(sheetName);
		return getRowNumber(rowText);
	}

	/**
	 * This method will return the Sheet number based on sheet name.
	 * 
	 * @return
	 */
	public int getSheetNumber() {
		return m_workbook.getSheetIndex(m_sheet);
	}

	/**
	 * This method will return the sheet number for a particular sheet name.
	 * 
	 * @param sheetName
	 * @return
	 */
	public int getSheetNumber(String sheetName) {
		getSheetDetails(sheetName);
		return getSheetNumber();
	}

	/**
	 * Method to create XLSX file.
	 * 
	 * @param fileName
	 *            -> FileName to create the XLSX file.
	 */
	public void createXlsxFile(String fileName) {
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook();
			FileOutputStream fs2 = new FileOutputStream(new File(fileName));
			workbook.write(fs2);
			fs2.close();
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.toString());

		} finally {
			try {
				if (workbook != null) {
					workbook.close();
				}
			} catch (IOException e) {
				Log.writeMessage(LogLevel.ERROR, e.toString());
			}
		}
	}

	/**
	 * Method to write data into excel sheet.
	 * 
	 * @param arr1
	 *            -> Variable IDs to write to excel sheet
	 * @param arr2
	 *            -> Variable Values to write to excel sheet
	 * @param fileName
	 *            -> File Name
	 */
	public void writeValuesToSheet(String[] arr1, String[] arr2, String fileName) {
		XSSFWorkbook workbook = null;
		try {
			FileInputStream fs1 = new FileInputStream(new File(fileName));
			workbook = new XSSFWorkbook(fs1);
			XSSFSheet sheet = workbook.createSheet("Sheet_" + workbook.getNumberOfSheets());

			XSSFRow row;
			XSSFCell cell;

			for (int i = 0; i < arr1.length; i++) {
				row = sheet.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue(arr1[i]);
				cell = row.createCell(1);
				cell.setCellValue(arr2[i]);

			}
			fs1.close();
			FileOutputStream fs2 = new FileOutputStream(new File(fileName));
			workbook.write(fs2);
			fs2.close();
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.toString());
		} finally {
			try {
				if (workbook != null) {
					workbook.close();
				}
			} catch (IOException e) {
				Log.writeMessage(LogLevel.ERROR, e.toString());
			}
		}
	}

	/**
	 * Close Open Work Book
	 * 
	 * @return -> True/False
	 */
	public boolean closeWorkBook() {
		try {
			if (m_workbook != null) {
				m_workbook.close();
			}
			return true;
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, e.toString());
		}
		return false;
	}

	// End of public methods

	// Private Methods

	/**
	 * Creates Workbook based on workbook type and filename.
	 * 
	 * @param fileName
	 * 
	 * @param workbookType
	 */
	private void createWorkbook(String fileName, WorkbookTypeConstants workbookType) {
		try {
			m_filePath = fileName;
			switch (workbookType) {
			case HSSFWORKBOOK:
				m_workbook = (fileName.isEmpty()) ? new HSSFWorkbook()
						: new HSSFWorkbook(new FileInputStream(new File(fileName)));
				Log.writeMessage(CLASS_NAME, "Created HSSFWORKBOOK instance.");
				break;
			case SXSSFWORKBOOK:
				m_workbook = (fileName.isEmpty()) ? new SXSSFWorkbook()
						: new SXSSFWorkbook(new XSSFWorkbook(new FileInputStream(new File(fileName))));
				Log.writeMessage(CLASS_NAME, "Created SXSSFWORKBOOK instance.");
				break;
			case XSSFWORKBOOK:
				m_workbook = (fileName.isEmpty()) ? new XSSFWorkbook()
						: new XSSFWorkbook(new FileInputStream(new File(fileName)));
				Log.writeMessage(CLASS_NAME, "Created XSSFWORKBOOK instance.");
				break;
			case FACTORY:
			default:
				if (!fileName.isEmpty())
					m_workbook = WorkbookFactory.create(new File(fileName));
				else
					m_workbook = new XSSFWorkbook();
				Log.writeMessage(CLASS_NAME, "File name is not provided. Created XSSFWorkbook instance.");
				break;
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.getMessage());
		}
	}

	/**
	 * This method will return the cell number of row number based on the value
	 * 
	 * @param value
	 * 
	 * @param cellOrRow
	 * 
	 * @return
	 */
	private int getCellOrRowNumberBasedOnText(String value, int cellOrRow) {
		try {
			if (m_sheet != null) {
				for (Row row : m_sheet) {
					for (Cell cell : row) {
						if (cell.toString().toLowerCase().equals(value.toLowerCase())) {
							if (cellOrRow == 0)
								return cell.getColumnIndex();
							else
								return row.getRowNum();
						}
					}
				}
			} else {
				Log.writeMessage(LogLevel.ERROR, CLASS_NAME, "Sheet details are not loaded, please pass sheet name");
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.getLocalizedMessage());
		}
		return -1;
	}

	/**
	 * Loads the sheet details in to memory based on the sheet name.
	 * 
	 * @param sheetName
	 */
	private void getSheetDetails(String sheetName) {
		try {
			if (!sheetName.isEmpty()) {
				m_sheet = m_workbook.getSheet(sheetName);
				m_sheetRows = m_sheet.getPhysicalNumberOfRows();
				m_sheetColumns = m_sheet.getRow(0).getPhysicalNumberOfCells();
			}
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.getMessage());
			m_sheetRows = m_sheetColumns = -1;
		}
	}

	/**
	 * Method to set single cell value into excel sheet.
	 * 
	 * @param rowIndex
	 *            -> Index of the row to update in excel sheet
	 * @param columnIndex
	 *            -> Index of the column to update in excel sheet
	 * @param value
	 *            -> Value to update in excel sheet
	 * @param fileName
	 *            -> Output filename
	 */
	public void setCellValue(int rowNum, int collNum, String value, String fileName) {
		try {
			Cell cell = m_sheet.getRow(rowNum).getCell(collNum);
			cell.setCellValue(value);
			FileOutputStream fs2 = new FileOutputStream(new File(fileName));
			m_workbook.write(fs2);
			fs2.close();
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.toString());
		}

	}

	/**
	 * Method to update multiple cell values in the excel sheet.
	 * 
	 * @param rowDetails
	 *            -> ArrayList of HashMap containing values for following keys
	 *            row, column, value
	 * @param fileName
	 *            -> Output filename
	 */
	public void setCellValue(ArrayList<HashMap<String, Object>> rowDetails, String fileName) {
		try {
			FileOutputStream fs2 = new FileOutputStream(new File(fileName));
			try {

				for (int i = 0; i < rowDetails.size(); i++) {
					HashMap<String, Object> rowDetail = rowDetails.get(i);
					int rowNum = (Integer) rowDetail.get("row");
					int colNum = (Integer) rowDetail.get("column");
					String value = (String) rowDetail.get("value");
					Cell cell = m_sheet.getRow(rowNum).getCell(colNum);
					cell.setCellValue(value);
				}
				m_workbook.write(fs2);
			} catch (Exception e) {
				Log.writeMessage(LogLevel.ERROR, CLASS_NAME, e.toString());
			} finally {
				fs2.close();
			}
		} catch (Exception ex) {
			Log.writeMessage(LogLevel.ERROR, CLASS_NAME, ex.toString());
		}
	}

	/*
	 * Constant Variables
	 */
	private final static String CLASS_NAME = ExcelOperations.class.getName();

	/*
	 * Holds test data folder path.
	 */
	private final static String TEST_DATA_PATH = System.getProperty("user.dir") + "\\TestData\\";

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		closeWorkBook();
	}
}
