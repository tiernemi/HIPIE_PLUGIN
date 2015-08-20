package org.xtext.hipie.commands_listeners;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Opens design mode when the build is completed.
 */

public class DesignModeVisualiserListener {

	DesignModeVisualiserListener() {

	}

	static public void init() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				new IResourceChangeListener() {
					public void resourceChanged(IResourceChangeEvent event) {
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								openDesignMode();
							}
						});
					}
				}, IResourceChangeEvent.POST_BUILD);
	}

	public static void openDesignMode() {

		IWorkbenchWindow activeWindow = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (activeWindow != null) {
			IEditorPart activeEditor = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();

			if (activeEditor != null) {
				IEditorInput editorFile = activeEditor.getEditorInput();
				IPath dudFilepath = ((FileEditorInput) editorFile).getFile()
						.getFullPath();
				IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace()
						.getRoot();
				IFile htmlFile = workspaceRoot.getFile(dudFilepath
						.removeFileExtension().addFileExtension("html"));
				IFile ddlFile = workspaceRoot.getFile(dudFilepath
						.removeFileExtension().addFileExtension("ddl"));
				IFile databombFile = workspaceRoot.getFile(dudFilepath
						.removeFileExtension().addFileExtension("databomb"));

				if (htmlFile.exists() && ddlFile.exists()
						&& databombFile.exists()) {
					ICommandService commandServ = (ICommandService) PlatformUI
							.getWorkbench().getService(ICommandService.class);

					Command command = commandServ
							.getCommand(DesignModePostBuild.ID);
					try {
						command.executeWithChecks(new ExecutionEvent());
					} catch (ExecutionException e) {
						e.printStackTrace();
					} catch (NotDefinedException e) {
						e.printStackTrace();
					} catch (NotEnabledException e) {
						e.printStackTrace();
					} catch (NotHandledException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}