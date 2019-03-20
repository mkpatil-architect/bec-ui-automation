

package compare;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * Helper class for compare results tool.
 */
public class supportMethods {

	public static File logFile = new File("ComparisonResult.html");
	public static FileWriter fw;
	public static BufferedWriter bw;
	public static StringBuilder sb1 = new StringBuilder();

	/**
	 * Method to compare sheet values.
	 * 
	 * @param sheet1
	 *            -> Excel Sheet 1.
	 * @param sheet2
	 *            -> Excel Sheet 2.
	 * @param delta
	 *            -> Delta value.
	 * @param number
	 *            -> number.
	 * @throws IOException
	 *             -> IO Exception.
	 */
	public static void compareSheetValues(XSSFSheet sheet1, XSSFSheet sheet2,
			double delta, int number) throws IOException {
		System.out.println("Comparing the values in sheet " + number);

		Integer rowNum = sheet1.getLastRowNum() + 1;
		Integer colNum = (int) sheet1.getRow(0).getLastCellNum();

		String value1 = null;
		String value2 = null;

		for (int i = 0; i < rowNum; i++) {

			XSSFRow row1 = sheet1.getRow(i);
			for (int j = 1; j < colNum; j++) {
				XSSFCell cell = row1.getCell(j);
				if (cell != null) {
					value1 = cell.toString();
				}
			}

			XSSFRow row2 = sheet2.getRow(i);
			for (int k = 1; k < colNum; k++) {
				XSSFCell cell = row2.getCell(k);
				if (cell != null) {
					value2 = cell.toString();
				}
			}

			/*
			 * if(!(value1.equals(value2))) {
			 * System.out.println("Values are not matching for variable :" +
			 * row1.getCell(0).toString()); }
			 */

			if ((compareWithDeltaPercentage(value1, value2, row1.getCell(0)
					.toString(), number, delta))) {
				System.out.println("Values are not matching for variable :"
						+ row1.getCell(0).toString() + " in sheet " + number);
			}
		}

	}

	/**
	 * Method to compare with delta values.
	 * 
	 * @param value1
	 *            -> Value1.
	 * @param value2
	 *            -> Value2.
	 * @param delta
	 *            -> Delta
	 * @return -> True/False.
	 */
	public static boolean compareWithDelta(String value1, String value2,
			double delta) {
		Boolean result = false;
		Double difference;

		if ((value1 == null || value2 == null)
				|| (value1 == "" || value2 == "")) {
			System.out.println("Data is not available");
			return result;
		}

		if (Double.parseDouble(value1) > Double.parseDouble(value2)) {
			difference = Math.abs(Double.parseDouble(value1)
					- Double.parseDouble(value2));
		} else {
			difference = Math.abs(Double.parseDouble(value2)
					- Double.parseDouble(value1));
		}

		if (difference > delta) {
			result = !result;
			return result;
		} else {
			return result;
		}
	}

	/**
	 * Method to compare with delta percentage.
	 * 
	 * @param value1
	 *            -> Value1.
	 * @param value2
	 *            -> Value2.
	 * @param varId
	 *            -> Variable Id
	 * @param sheet
	 *            -> Sheet
	 * @param delta
	 *            -> Delta
	 * @return -> True/False
	 * @throws IOException
	 *             -> IO Exception
	 */
	public static boolean compareWithDeltaPercentage(String value1,
			String value2, String varId, int sheet, double delta)
			throws IOException {
		sb1.setLength(0);
		Boolean result = false;
		Double diffPercent;

		if ((value1 == null || value2 == null)
				|| (value1 == "" || value2 == "")) {
			System.out.println("Data is not available");
			return result;
		}
		Double dValue1 = Double.parseDouble(value1);
		Double dValue2 = Double.parseDouble(value2);

		Double diff = Math.abs(dValue1 - dValue2);

		if (diff == 0) {
			return result;
		}

		diffPercent = diff / dValue1;

		if (diffPercent > delta) {
			System.out
					.println("The difference in percentage is " + diffPercent);
			sb1.append("<tr>");
			sb1.append("<td>" + dValue1 + "</td>");
			sb1.append("<td>" + dValue2 + "</td>");
			sb1.append("<td>" + sheet + "</td>");
			sb1.append("<td>" + varId + "</td>");
			sb1.append("<td>" + diffPercent + "</td>");
			sb1.append("</tr>");
			bw.append(sb1.toString());
			result = !result;
			return result;
		} else {
			return result;
		}
	}

	/**
	 * Create HTML page.
	 */
	public static void createHTML() {
		try {

			fw = new FileWriter(logFile);
			bw = new BufferedWriter(fw);
			bw.write("<!DOCTYPE html>");
			sb1.append("<html lang=\"en\">");
			sb1.append("<head>");
			sb1.append("<title>Results</title>");
			sb1.append("<style>");
			sb1.append("table, th, td {");
			sb1.append("border: 1px solid black;");
			sb1.append("}");
			sb1.append("body");
			sb1.append("{");
			sb1.append("color: purple");
			sb1.append("}");
			sb1.append("</style>");
			sb1.append("</head>");
			sb1.append("<body>");
			sb1.append("<h1 align=\"center\"><u>Replicated vs UnReplicated</u></h1>");
			bw.append(sb1.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Close HTML Page.
	 */
	public static void closeHTML() {
		try {
			bw.append("</body>");
			bw.append("</html>");
			bw.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}