package org.xtext.hipie.commands_listeners;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.statushandlers.StatusManager;
import org.xtext.hipie.error.HIPIEStatus;
import org.xtext.hipie.views.DesignModeView;

/**
 * This class opens the design mode view based off the selection in the package
 * explorer.
 */

public class DesignModeSelect implements IHandler {

	static public String ID = "org.xtext.hipie.visualiser_select";

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		TreeSelection selectTree = (TreeSelection) HandlerUtil
				.getCurrentSelection(event);
		Object[] selectionList = selectTree.toArray();

		for (int j = 0; j < selectionList.length; ++j) {
			IFile file = (IFile) Platform.getAdapterManager().getAdapter(
					selectionList[j], IFile.class);

			IPath htmlFilepath = file.getFullPath();

			IFile htmlFile = ResourcesPlugin.getWorkspace().getRoot()
					.getFile(htmlFilepath);
			IFile corDdlFile = htmlFile.getProject().getFile(
					htmlFile.getProjectRelativePath().removeFileExtension()
							.addFileExtension("ddl"));
			IFile corDatabombFile = htmlFile.getProject().getFile(
					htmlFile.getProjectRelativePath().removeFileExtension()
							.addFileExtension("databomb"));
			IFile corPersistFile = htmlFile.getProject().getFile(
					htmlFile.getProjectRelativePath().removeFileExtension()
							.addFileExtension("persist"));

			DesignModeView view;

			if (corDdlFile.exists() && corDatabombFile.exists()
					&& corPersistFile.exists()) {
				try {
					view = (DesignModeView) HandlerUtil
							.getActiveWorkbenchWindowChecked(event)
							.getActivePage().showView(DesignModeView.ID);
					view.updateView(htmlFile);
					return null;
				} catch (PartInitException e) {
					String message = "Design Mode failed to open.";
					IStatus status = new HIPIEStatus(IStatus.ERROR,
							"org.xtext.hipie.ui",
							HIPIEStatus.DESIGN_MODE_FAILED, message, e);
					StatusManager.getManager().handle(status,
							StatusManager.LOG | StatusManager.SHOW);
					e.printStackTrace();
				}
			}
			else {
				if (!corDdlFile.exists()) {
					String message = "Corresponding DDL file does not exist.";
					IStatus status = new HIPIEStatus(IStatus.ERROR,
							"org.xtext.hipie.ui",
							HIPIEStatus.MISSING_DDL, message, null);
					StatusManager.getManager().handle(status,
							StatusManager.LOG | StatusManager.SHOW);
				}
				if (!corDatabombFile.exists()) {
					String message = "Corresponding databomb file does not exist.";
					IStatus status = new HIPIEStatus(IStatus.ERROR,
							"org.xtext.hipie.ui",
							HIPIEStatus.MISSING_DATABOMB, message, null);
					StatusManager.getManager().handle(status,
							StatusManager.LOG | StatusManager.SHOW);
				}
				if (!corPersistFile.exists()) {
					String message = "Corresponding persist file does not exist.";
					IStatus status = new HIPIEStatus(IStatus.ERROR,
							"org.xtext.hipie.ui",
							HIPIEStatus.MISSING_PERSIST, message, null);
					StatusManager.getManager().handle(status,
							StatusManager.LOG | StatusManager.SHOW);					
				}
			}

		}
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {

	}

}
