package org.xtext.hipie.ui.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.osgi.service.prefs.Preferences;
import org.xtext.hipie.ui.DataSourceDialog;


public class DataSources implements IHandler {

	public DataSources()
	{
		
			ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener()
			{		
				public void resourceChanged(IResourceChangeEvent event) {
					
					
					/*	
					System.out.println(cont_project.getName()) ;
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
					System.out.println(prev_sel_files.size()) ;
					if (prev_sel_files.size() == 0)
					{
						Display.getDefault().syncExec(new Runnable() {
						public void run() {
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
					});
				}*/ 
			} 
			
		}
		, IResourceChangeEvent.PRE_BUILD) ; 	 
	} 
	
	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
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
				if (ResourcesPlugin.getWorkspace().getRoot().getFile(filepath).exists())
					prev_sel_files.add((IResource) ResourcesPlugin.getWorkspace().getRoot().getFile(filepath)) ;
			}
		}
		DataSourceDialog dialog = 
		new DataSourceDialog(HandlerUtil.getActiveShell(event), ResourcesPlugin.getWorkspace().getRoot(), "Select Data Sources") ;
				dialog.setTitle("Data Source Selection");

		if (prev_sel_files.size() > 0)
			dialog.setInitialSelections(prev_sel_files.toArray());
		// user pressed cancel
		if (dialog.open() != Window.OK)
			return false;
		Object[] result = dialog.getResult();

		String select_list = "" ;
		for (int i = 0 ; i < result.length ; ++i)
			if (result[i] instanceof File)
			{
				File temp =  (File) result[i] ;
				select_list += temp.getFullPath().toOSString() + " " ;
			}
		selected_items.put("select_prefs", select_list) ;
		return null ;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}
