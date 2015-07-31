package org.xtext.hipie.commands_listeners;

import java.util.ArrayList;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IFile;
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

/**
 * Opens data sources dialog based of currently opened file in editor.
 */

public class DataSources implements IHandler {

	public DataSources()
	{
		
	}
	
	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IEditorInput EditorFile = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput() ;
		IProject contProject = ((FileEditorInput)EditorFile).getFile().getProject() ;
		String fileName = ((FileEditorInput)EditorFile).getFile().getName() ;
		
		IScopeContext projectScope = new ProjectScope(contProject) ;
		Preferences preferences = projectScope.getNode("org.xtext.hipie.ui");
		Preferences selectedItems = preferences.node("data_prefs");
		String select_string = selectedItems.get("select_prefs_" + fileName , "") ;
		String[] filepaths = select_string.split(" ") ;
		ArrayList<Object> prevSelFiles = new ArrayList<Object>() ;
		for (int i = 0 ; i < filepaths.length ; ++i)
		{
			if(filepaths[i].length() > 0)
			{
				Path filepath = new Path(filepaths[i]) ;
				if (ResourcesPlugin.getWorkspace().getRoot().getFile(filepath).exists())
					prevSelFiles.add((IResource) ResourcesPlugin.getWorkspace().getRoot().getFile(filepath)) ;
			}
		}
		DataSourceDialog dialog = 
		new DataSourceDialog(HandlerUtil.getActiveShell(event), ResourcesPlugin.getWorkspace().getRoot(), "Select Data Sources") ;
				dialog.setTitle("Data Source Selection");

		if (prevSelFiles.size() > 0)
			dialog.setInitialSelections(prevSelFiles.toArray());
		// User pressed cancel. //
		if (dialog.open() != Window.OK)
			return false;
		Object[] result = dialog.getResult();

		String selectList = "" ;
		for (int i = 0 ; i < result.length ; ++i)
			if (result[i] instanceof IFile)
			{
				IFile temp =  (IFile) result[i] ;
				selectList += temp.getFullPath().toOSString() + " " ;
			}
		selectedItems.put("select_prefs_" + fileName, selectList) ;
		try {
			preferences.flush() ;
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		return null ;
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
