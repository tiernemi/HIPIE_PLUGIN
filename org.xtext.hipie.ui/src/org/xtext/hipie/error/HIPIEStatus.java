package org.xtext.hipie.error;

import org.eclipse.core.runtime.Status;

public class HIPIEStatus extends Status {

	public static final int DESIGN_MODE_DATBOMB__ERROR = -10;
	public static final int DESIGN_MODE_DDL__ERROR = -11;

	public HIPIEStatus(int severity, int code, String id, String message, Throwable t) {
		  super(severity, id, code, message, t);
	}
}
