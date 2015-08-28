package org.xtext.hipie.error;


import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class DuplicatesPrompt {
	public static void prompt(final IFile duplicateFile) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				String message = "Duplicate files detected with the name "
						+ duplicateFile.getName()
						+ ". Only " + duplicateFile.getFullPath().toOSString() + " will be included.";
				MessageDialog dialog = new MessageDialog(PlatformUI
						.getWorkbench().getActiveWorkbenchWindow().getShell(),
						"Duplicates Detected", null, message,
						MessageDialog.WARNING, new String[] { "Ok" }, 0);
				dialog.open();
			}
		});
	}
}
