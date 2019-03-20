

package com.benchmark.managers;

import com.benchmark.core.constants.LogLevel;
import com.benchmark.core.constants.WorkbookTypeConstants;
import com.benchmark.core.logger.Log;
import com.benchmark.core.util.CommonUtil;
import com.benchmark.core.util.ExcelOperations;

public final class Common {

	/*
	 * Variable to hold instance of Excel Operation class object.
	 */
	private static volatile ExcelOperations m_excelOperations = null;

	/**
	 * Singleton instance will be created for ExcelOperations class Object.
	 * 
	 * @return Pages Instance.
	 */
	public static ExcelOperations ExcelOperations(String fileName,
			String sheetName) {
		try {
			m_excelOperations = new ExcelOperations(CommonUtil.getBatHomePath()
					+ "/DownloadedFile/" + fileName, sheetName,
					WorkbookTypeConstants.HSSFWORKBOOK);
		} catch (Exception e) {
			Log.writeMessage(LogLevel.ERROR, "", e.getMessage());
		}
		return m_excelOperations;
	}

}
