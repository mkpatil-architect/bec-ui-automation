

package compare;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Utility to compare outcomes in excel files based on pre-defined tolerance
 * Usage: java -jar compareExcelFiles.jar dir1 dir2 <tolerance value for
 * optimization> Example --> java -jar compareExcelFiles.jar dir1 dir2 10
 */
public class compareResults {

	public static StringBuilder sb = new StringBuilder();

	/**
	 * Main Program
	 * 
	 * @param args
	 *            => Excel file 1, Excel file 2, Tolerance value.
	 */
	public static void main(String[] args) {
		try {
			compareResults f1 = new compareResults();

			if (args.length < 3) {
				f1.printUsage();
				return;
			}

			ArrayList<String> dir1 = new ArrayList<String>();
			ArrayList<String> dir2 = new ArrayList<String>();

			dir1 = f1.getFileNames(args[0]);
			dir2 = f1.getFileNames(args[1]);
			Double delta1 = 0.0001;
			Double delta2 = Double.parseDouble(args[2]);

			if (!(dir1.size() == dir2.size())) {
				System.out
						.println("\n*****File count is not the same in given folders "
								+ dir1.size() + " " + dir2.size() + "****");
				return;
			}

			System.out.println("\n*****Validation started****");

			supportMethods.createHTML();
			supportMethods.bw.append("<h2>Comparison Started:</h2>");

			for (int i = 0; i < dir1.size(); i++) {
				if (dir1.get(i).contains("Sim")) {
					f1.compareExcel(args[0] + "\\" + dir1.get(i), args[1]
							+ "\\" + dir2.get(i), "1", delta1);
				} else {
					f1.compareExcel(args[0] + "\\" + dir1.get(i), args[1]
							+ "\\" + dir2.get(i), "2", delta2);
				}
			}
			System.out.println("\n*****Validation completed**");
			supportMethods.bw.append("<h2>Comparison Competed.</h2>");
			supportMethods.closeHTML();

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Method to compare excel files.
	 * 
	 * @param fileName1
	 *            -> Excel file 1.
	 * @param fileName2
	 *            -> Excel file 2.
	 * @param calcType
	 *            -> Calculation Type.
	 * @param tolerance
	 *            -> Tolerance.
	 * @throws IOException
	 *             -> IO Exception
	 */
	public void compareExcel(String fileName1, String fileName2,
			String calcType, double tolerance) throws IOException {

		if (calcType.equals("1")) {
			System.out.println("\n*****Comparison started for " + fileName1
					+ "," + fileName2 + "****");
			compareSimulation(fileName1, fileName2, tolerance);
			System.out.println("\n*****Comparison completed for " + fileName1
					+ "," + fileName2 + "**");

		} else if (calcType.equals("2")) {
			System.out.println("\n*****Comparison started for " + fileName1
					+ "," + fileName2 + "****");
			compareOptimization(fileName1, fileName2, tolerance);
			System.out.println("\n*****Comparison completed for " + fileName1
					+ "," + fileName2 + "**");
		} else {
			printUsage();
			return;
		}

	}

	/**
	 * Method to compare optimization files.
	 * 
	 * @param fileName1
	 *            -> Excel file 1.
	 * @param fileName2
	 *            -> Excel file 2.
	 * @param delta
	 *            -> Delta value.
	 */
	public void compareOptimization(String fileName1, String fileName2,
			double delta) {
		sb.setLength(0);

		try {
			FileInputStream file1 = new FileInputStream(new File(fileName1));
			FileInputStream file2 = new FileInputStream(new File(fileName2));

			XSSFWorkbook workbook1 = new XSSFWorkbook(file1);
			XSSFWorkbook workbook2 = new XSSFWorkbook(file2);

			if ((workbook1.getNumberOfSheets()) != (workbook2
					.getNumberOfSheets())) {
				System.out.println("Number of sheets differ in input files.. "
						+ workbook1.getNumberOfSheets() + ","
						+ workbook1.getNumberOfSheets());
				return;
			}

			sb.append("<h4 align=\"center\"><i>" + fileName1 + " VS "
					+ fileName2 + "</i></h4>");
			sb.append("<table style=\"width:100%\">");
			sb.append("<tr>");
			sb.append("<th>File1-Value</th>");
			sb.append("<th>File2-Value</th>");
			sb.append("<th>SheetNumber</th>");
			sb.append("<th>VariableId</th>");
			sb.append("<th>DiffPercent</th>");
			sb.append("</tr>");
			supportMethods.bw.append(sb.toString());

			for (int i = 0; i < workbook1.getNumberOfSheets(); i++) {
				supportMethods.compareSheetValues(workbook1.getSheetAt(i),
						workbook2.getSheetAt(i), delta, i);
			}
			supportMethods.bw.append("</table>");
		} catch (IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
		}

	}

	/**
	 * Method to compare simulation files.
	 * 
	 * @param fileName1
	 *            -> Simulation file 1.
	 * @param fileName2
	 *            -> Simulation file 2.
	 * @param delta
	 *            -> Delta value.
	 */
	public void compareSimulation(String fileName1, String fileName2,
			double delta) {
		sb.setLength(0);
		try {
			FileInputStream file1 = new FileInputStream(new File(fileName1));
			FileInputStream file2 = new FileInputStream(new File(fileName2));

			XSSFWorkbook workbook1 = new XSSFWorkbook(file1);
			XSSFWorkbook workbook2 = new XSSFWorkbook(file2);

			if (workbook1.getNumberOfSheets() != workbook2.getNumberOfSheets()) {
				System.out.println("Number of sheets differ in input files.. "
						+ workbook1.getNumberOfSheets() + ","
						+ workbook1.getNumberOfSheets());
				return;
			}

			sb.append("<h4 align=\"center\"><i>" + fileName1 + " VS "
					+ fileName2 + "</i></h4>");
			sb.append("<table style=\"width:100%\">");
			sb.append("<tr>");
			sb.append("<th>File1-Value</th>");
			sb.append("<th>File2-Value</th>");
			sb.append("<th>SheetNumber</th>");
			sb.append("<th>VariableId</th>");
			sb.append("<th>DiffPercernt</th>");
			sb.append("</tr>");
			supportMethods.bw.append(sb.toString());

			for (int i = 0; i < workbook1.getNumberOfSheets(); i++) {
				supportMethods.compareSheetValues(workbook1.getSheetAt(i),
						workbook2.getSheetAt(i), delta, i);
			}
			supportMethods.bw.append("</table>");

		} catch (IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
		}
	}

	/**
	 * Method to get file Names.
	 * 
	 * @param dirname
	 *            -> Directory name
	 * @return -> ArrayList[String]
	 */
	public ArrayList<String> getFileNames(String dirname) {

		ArrayList<String> A1 = new ArrayList<String>();

		try {

			File folder = new File(dirname);
			File[] listOfFiles = folder.listFiles();

			for (int i = 0; i < listOfFiles.length; i++) {
				A1.add(listOfFiles[i].getName());
			}
		} catch (Exception e) {
			System.err.println("Caught IOException: " + e.getMessage());
		}
		return A1;
	}

	/**
	 * Method to print how use this tool.
	 */
	public void printUsage() {
		System.out.println("\n");
		System.out
				.println("*********************************************USAGE***************************************************");
		System.out
				.println("********java -jar compareExcelFiles.jar dir1 dir2 <tolerance value for optimization>*****************");
		System.out
				.println("************* Example --> java -jar compareExcelFiles.jar dir1 dir2 10  *****************************");
		System.out
				.println("*****************************************************************************************************");
	}
}
