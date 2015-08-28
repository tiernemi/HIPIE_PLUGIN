package org.xtext.hipie.error;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.ui.statushandlers.AbstractStatusHandler;
import org.eclipse.ui.statushandlers.StatusAdapter;

public class HIPIEGeneratorErrorHandler extends AbstractStatusHandler {

	public HIPIEGeneratorErrorHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(StatusAdapter statusAdapter, int style) {
			IStatus oldStatus = statusAdapter.getStatus() ;
			if (oldStatus.getCode() == HIPIEStatus.HTML_GENERATION_FAILED)
				ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "HTML Generation Error", oldStatus.getMessage(), oldStatus) ;
			if (oldStatus.getCode() == HIPIEStatus.DATABOMB_GENERATION_FAILED)
				ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "HTML Generation Error", oldStatus.getMessage(), oldStatus) ;
			if (oldStatus.getCode() == HIPIEStatus.DDL_GENERATION_FAILED)
				ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "DDL Generation Error", oldStatus.getMessage(), oldStatus) ;
			if (oldStatus.getCode() == HIPIEStatus.INSUFFICIENT_DATABOMB_FILES)
				ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Insufficient Databombs", oldStatus.getMessage(), oldStatus) ;
	}
}
