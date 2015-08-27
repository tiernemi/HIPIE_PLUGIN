package org.xtext.hipie.error;

import org.eclipse.core.runtime.Status;

public class HIPIEStatus extends Status {

	public static final int DESIGN_MODE_FAILED = -1;
	public static final int COMPILER_MISSING = -2;
	public static final int ERRORS_IN_FILE = -3 ;
	public static final int RESOURCE_DOESNT_EXIST = -4 ;
	public static final int BROWSER_OPENING_FAILED = - 5 ;
	public static final int MALFORMED_URL = -6 ;
	public static final int FAILED_TO_GENERATE_HTML = - 7 ;
	public static final int FAILED_TO_COMPILE_DATABOMB = -8 ;
	public static final int DUD_FILE_DOES_NOT_EXIST = -9 ;
	public static final int MISSING_DATABOMB = -10 ;
	public static final int MISSING_DDL = -11 ;
	public static final int MISSING_PERSIST = -12 ;
	public static final int DUPLICATE_DATABOMB_FILES = -13 ;
	public static final int DATABOMB_GENERATION_FAILED = -14 ;
	public static final int DDL_GENERATION_FAILED = -15 ;
	public static final int HTML_GENERATION_FAILED = -16 ;
	public static final int INSUFFICIENT_DATABOMB_FILES = -17 ;

	
	public HIPIEStatus(int severity, String id, int code, String message, Throwable t) {
		  super(severity, id, code, message, t);
	}

	public HIPIEStatus(int severity, String id, int code, String message,
			Object t) {
		  super(severity, id, code, message, null);
	}
}
