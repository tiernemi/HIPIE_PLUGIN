package org.xtext.hipie.commands_listeners;

import java.util.ArrayList;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.xtext.hipie.views.DataSourceDialog;


public class DataSources implements IHandler {

	public DataSources()
	{
		
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
		try {
			preferences.flush() ;
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
