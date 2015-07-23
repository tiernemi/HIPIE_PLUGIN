package org.xtext.hipie.commands_listeners;

import java.util.ArrayList;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.FileEditorInput;
import org.osgi.service.prefs.Preferences;
import org.eclipse.core.resources.ProjectScope;

public class DataSourceListener {

	DataSourceListener()
	{
		
	}
	
	static public void init()
	{
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener()
		{		
			public void resourceChanged(IResourceChangeEvent event) {
					Display.getDefault().syncExec(new Runnable() {
					public void run() {
						IEditorInput EditorFile = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput() ;
						IProject cont_project = ((FileEditorInput)EditorFile).getFile().getProject() ;
						IScopeContext projectScope = new ProjectScope(cont_project) ;
						Preferences preferences = projectScope.getNode("org.xtext.hipie.ui");
						Preferences selected_items = preferences.node("data_prefs");
						String select_string = selected_items.get("select_prefs", "") ;
						String[] filepaths = select_string.split(" ") ;
						ArrayList<Object> prev_sel_files = new ArrayList<Object>() ;

						for (int i = 0 ; i < filepaths.length ; ++i)
						{
							if(filepaths[i].length() > 0)
							{
								Path filepath = new Path(filepaths[i]) ;
								prev_sel_files.add((IResource) ResourcesPlugin.getWorkspace().getRoot().getFile(filepath)) ;
							}
						}
						if (prev_sel_files.size() == 0)
						{
							ICommandService command_serv = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class)  ;
							Command command = command_serv.getCommand("org.xtext.hipie.ui.datasources") ;
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
	, IResourceChangeEvent.PRE_BUILD) ; 	 
	} 
}
