package org.xtext.hipie.error;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.statushandlers.AbstractStatusHandler;
import org.eclipse.ui.statushandlers.StatusAdapter;

public class HIPIEDatabombErrorHandler extends AbstractStatusHandler {

	public HIPIEDatabombErrorHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(StatusAdapter statusAdapter, int style) {
			IStatus oldStatus = statusAdapter.getStatus() ;
			if (oldStatus.getCode() == HIPIEStatus.FAILED_TO_COMPILE_DATABOMB)
				ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Databomb Creation Error", oldStatus.getMessage(), oldStatus) ;
	}

}
