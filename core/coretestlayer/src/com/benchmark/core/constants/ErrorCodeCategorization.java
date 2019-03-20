package com.benchmark.core.constants;

import java.util.Arrays;
import java.util.List;

public final class ErrorCodeCategorization {
	 public final static List<String> INTERNAL_ERROR = Arrays.asList("INTERNAL SERVER ERROR", "INTERNAL_ERROR", "GRAILS RUNTIME EXCEPTION", "ERROR REPORT", "CUSTOMEXCEPTION - ERROR");
	 public final static List<String> AUTO_UNHANDLED_EXCEPTION = Arrays.asList("NULL POINTER EXCEPTION", "BINDEXCEPTION", "MALFORMEDJSONEXCEPTION", "ILLEGALSTATEEXCEPTION", "JSONNULL", "FAILED WHILE PROCESS METHOD",
	 "EXCEPTION MESSAGE: UNKNOWN ERROR", "STRING INDEX OUT OF RANGE", "NULL");
	 public final static List<String> RESOURCE_NOT_LOADED =	 Arrays.asList("TIME OUT", "PAGE NOT LOADED", "AFTER WAITING FOR","FAILED WHILE LOGIN TO THE APPLICATION");
	 public final static List<String> DB_CONNECTION_FAILURE = Arrays.asList("CONNECTION CLOSED", "CONNECTION TIMEOUT", "CLOSED CONNECTION","CONNECTION NOT ESTABLISHED");
	 public final static List<String> AUTO_DATA_MISMATCH = Arrays.asList("EXPECTED", "ACTUAL", "DATA MISMATCH");
	 public final static List<String> AUTO_SKIPPED_EXCEPTION = Arrays.asList("SKIPEXCEPTION", "SKIP");
	 public final static List<String> ELEMENT_NOT_FOUND =	 Arrays.asList("FAILED WHILE SELECTING", "UNABLE TO LOCATE ELEMENT","NO FILE FOUND","ERROR WHILE ATTEMPTING TO FIND ELEMENT","UNABLE TO LOCATE CHECKBOX");
	 public final static List<String> KNOWN_FAILURES = Arrays.asList("KNOWN ISSUE");
	 public final static List<String> BASIC_CHECK =	 Arrays.asList("BASIC_CHECK");
	 public final static List<String> DEPENDENT_FAILURE =	 Arrays.asList("DEPENDENT_FAILURE");	 

}
