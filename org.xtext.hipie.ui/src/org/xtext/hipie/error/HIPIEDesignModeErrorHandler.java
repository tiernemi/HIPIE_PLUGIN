package org.xtext.hipie.error;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.statushandlers.AbstractStatusHandler;
import org.eclipse.ui.statushandlers.StatusAdapter;

public class HIPIEDesignModeErrorHandler extends AbstractStatusHandler {

	public HIPIEDesignModeErrorHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(StatusAdapter statusAdapter, int style) {
			IStatus oldStatus = statusAdapter.getStatus() ;
			if (oldStatus.getCode() == HIPIEStatus.DESIGN_MODE_DATBOMB__ERROR)
				ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Design Mode Error", oldStatus.getMessage(), oldStatus) ;
			if (oldStatus.getCode() == HIPIEStatus.DESIGN_MODE_DDL__ERROR)
				ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Design Mode Error", oldStatus.getMessage(), oldStatus) ;
	}

}
