package org.xtext.hipie.commands_listeners;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.statushandlers.StatusManager;
import org.xtext.hipie.error.HIPIEStatus;
import org.xtext.hipie.ui.internal.HIPIEActivator;
import org.xtext.hipie.views.DesignModeView;

/**
 * Opens design mode view when DesignModeVisualiserListener is triggered.
 */

public class DesignModePostBuild implements IHandler {

	static public String ID = "org.xtext.hipie.visualiser_post_build";

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorInput editorInput = HandlerUtil.getActiveEditorInput(event);
		IFile file = ((FileEditorInput) editorInput).getFile();
		IPath htmlFilepath = file.getFullPath().removeFileExtension()
				.addFileExtension("html");
		IFile htmlFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFile(htmlFilepath);

		try {
			DesignModeView view = (DesignModeView) HandlerUtil
					.getActiveWorkbenchWindowChecked(event).getActivePage()
					.showView(DesignModeView.ID);
			view.updateView(htmlFile);
		} catch (PartInitException e) {
			e.printStackTrace();
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
