

package com.benchmark.commonfactory;

import com.benchmark.core.constants.WorkbookTypeConstants;
import com.benchmark.core.logger.Log;
import com.benchmark.core.util.ExcelOperations;


/**
 * 
 *
 */
public class CommonManager {

	// Create singleton instance for ExcelOperations class.
	private static volatile ExcelOperations m_excelOperations = null;

	/**
	 * Gets ExcelOperations class instance.
	 * 
	 * @param fileName
	 * @param sheetName
	 * @return
	 */
	public static ExcelOperations getExcelOperationsObject(String fileName,
			String sheetName) {
		if (m_excelOperations == null) {
			synchronized (ExcelOperations.class) {
				if (m_excelOperations == null) {
					try {
						m_excelOperations = new ExcelOperations(fileName,
								sheetName, WorkbookTypeConstants.FACTORY);
					} catch (Exception e) {
						Log.writeMessage(
								CommonManager.class.getName(),
								String.format(
										"Exception occured while creating ExcelOpertionds object in factory class. Error: %s",
										e.getLocalizedMessage()));
					}
				}
			}
		}
		return m_excelOperations;
	}

	

	

}
