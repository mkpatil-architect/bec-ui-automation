

package com.benchmark.core.util;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.logger.Log;

public class CSVFileOperations {

	/**
	 * This method will return the lines that are available in the CSV file.
	 * 
	 * @param fileName
	 * @param delimiter
	 * @return
	 */
	public static int getLinesCount(String fileName, char delimiter) {
		return getCSVReader(fileName, delimiter).size();
	}

	/**
	 * This method will return the line number based on the text.
	 * 
	 * @param fileName
	 * @param text
	 *            (matching text line number will return)
	 * @return line number
	 */
	public static int getLineNumber(String fileName, String text) {
		List<String[]> lines = getCSVReader(fileName, DEFAULT_DELIMITER);
		int i = 1;
		for (String[] elements : lines) {
			// System.out.println(i);
			for (String element : elements) {
				// System.out.print(element);
				// System.out.print(",");
				if (element.equals(text)) {
					Log.writeMessage(CSVFileOperations.class.getName(),
							String.format("Text '%s' is found in the file %s at line number %d", text, fileName, i));
					return i;
				}
			}
			// System.out.println("\n");
			i++;
		}
		Log.writeMessage(LogLevel.ERROR, CSVFileOperations.class.getName(),
				String.format("Text '%s' is not found in the file %s.", text, fileName));
		return -1;
	}

	/**
	 * This method will write data into CSV file
	 * 
	 * @param fileName
	 *            -> creates CSV file to write data
	 * @param data
	 *            -> data to write into CSV file
	 */
	public static void createCSVFile(String fileName, List<String[]> data) {
		try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
			writer.writeAll(data);
			writer.close();
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, "CSVFileOperations", e.toString());
		}
	}

	/**
	 * This method will write ResultSet data to CSV file
	 * 
	 * @param resultSet
	 *            -> java.sql.ResultSet
	 * @param includeColumnNames
	 *            -> True - Includes Column Names in CSV File False - Column
	 *            Names will not be included in CSV file
	 * @param fileName
	 *            -> CSV file Name
	 */
	public static void createCSVFile(ResultSet resultSet, boolean includeColumnNames, String fileName) {
		try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
			writer.writeAll(resultSet, includeColumnNames);
			writer.close();
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, "CSVFileOperations", e.toString());
		}
	}

	/**
	 * This method will read the CSV file and returns Iterator<Object[]>
	 * 
	 * @param fileName
	 * @param delimiter
	 * @return
	 */
	public static Iterator<Object[]> readData(String fileName, char delimiter) {

		List<String[]> lines = getCSVReader(fileName, delimiter);
		List<Object[]> data = new ArrayList<Object[]>();

		for (String[] line : lines) {
			data.add(new Object[] { line });
		}
		Iterator<Object[]> finalValues = data.iterator();
		return finalValues;
	}

	/**
	 * This method will read the CSV file and return lines that are available in
	 * the CSV file.
	 * 
	 * @param fileName
	 * 
	 * @param delimiter
	 * 
	 * @return
	 */
	public static List<String[]> getCSVReader(String fileName, char delimiter) {
		CSVReader csvReader = null;
		try {
			csvReader = new CSVReader(new FileReader(fileName), delimiter);
			return csvReader.readAll();
		} catch (Exception e) {
			Log.writeMessage(CSVFileOperations.class.getName(), e.getMessage());
		} finally {
			try {
				csvReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private final static char DEFAULT_DELIMITER = ',';
}
