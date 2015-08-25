package org.xtext.hipie.error;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.statushandlers.AbstractStatusHandler;
import org.eclipse.ui.statushandlers.StatusAdapter;

public class HIPIELauncherErrorHandler extends AbstractStatusHandler {

	public HIPIELauncherErrorHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(StatusAdapter statusAdapter, int style) {
		IStatus oldStatus = statusAdapter.getStatus();
		if (oldStatus.getCode() == HIPIEStatus.ERRORS_IN_FILE)
			ErrorDialog.openError(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), "Launch Error",
					oldStatus.getMessage(), oldStatus);
		if (oldStatus.getCode() == HIPIEStatus.RESOURCE_DOESNT_EXIST)
			ErrorDialog.openError(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), "Launch Error",
					oldStatus.getMessage(), oldStatus);
		if (oldStatus.getCode() == HIPIEStatus.MALFORMED_URL)
			ErrorDialog.openError(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), "Launch Error",
					oldStatus.getMessage(), oldStatus);
		if (oldStatus.getCode() == HIPIEStatus.BROWSER_OPENING_FAILED)
			ErrorDialog.openError(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), "Launch Error",
					oldStatus.getMessage(), oldStatus);
		if (oldStatus.getCode() == HIPIEStatus.DUD_FILE_DOES_NOT_EXIST)
			ErrorDialog.openError(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), "Launch Error",
					oldStatus.getMessage(), oldStatus);
	}
}
