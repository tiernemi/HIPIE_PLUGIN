package org.xtext.hipie.commands_listeners;

import java.util.ArrayList;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.FileEditorInput;

public class DesignModeVisualiserListener {

	DesignModeVisualiserListener()
	{
		
	}
	
	static public void init()
	{
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener()
		{		
			public void resourceChanged(IResourceChangeEvent event) {
					Display.getDefault().syncExec(new Runnable() {
					public void run() {
						IEditorInput editorFile = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput() ;
						IPath dud_filepath = ((FileEditorInput)editorFile).getFile().getFullPath() ;
						IFile html_file = ResourcesPlugin.getWorkspace().getRoot().getFile(dud_filepath.removeFileExtension().addFileExtension("html")) ;
						if (html_file.exists())
						{
							ICommandService command_serv = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class)  ;
							Command command = command_serv.getCommand(DesignModePostBuild.command_ID) ;
							try {
								command.executeWithChecks(new ExecutionEvent()) ;
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
				}); 
			} 
		}
	, IResourceChangeEvent.POST_BUILD) ; 	 
	} 
}