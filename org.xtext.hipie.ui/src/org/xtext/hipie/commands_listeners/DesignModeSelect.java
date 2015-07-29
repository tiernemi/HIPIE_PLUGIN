package org.xtext.hipie.commands_listeners;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.statushandlers.StatusManager;
import org.xtext.hipie.error.HIPIEStatus;
import org.xtext.hipie.ui.internal.HIPIEActivator;
import org.xtext.hipie.views.DesignModeView;

public class DesignModeSelect implements IHandler {

	static public String command_ID = "org.xtext.hipie.visualiser_select" ;
	
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
		
		TreeSelection select = (TreeSelection) HandlerUtil.getCurrentSelection(event) ;
		Object[] selection_list = select.toArray() ;
	
		for (int j=0 ; j<selection_list.length ; ++j) 
		{
			IFile html_file = (IFile) Platform.getAdapterManager().getAdapter(selection_list[j], IFile.class) ;				
			IPath ddl_filepath = html_file.getFullPath().removeFileExtension().addFileExtension("ddl") ;
			IPath dat_filepath = html_file.getFullPath().removeFileExtension().addFileExtension("databomb") ;
			// IPath persist_filepath = file.getFullPath().removeFileExtension().addFileExtension("databomb") ;	
			IFile ddl_file = ResourcesPlugin.getWorkspace().getRoot().getFile(ddl_filepath) ;
			IFile dat_file = ResourcesPlugin.getWorkspace().getRoot().getFile(dat_filepath) ;
			
			if (ddl_file.exists() && dat_file.exists())
			{
				InputStream in_stream = null;
				try {
					in_stream = ddl_file.getContents();
				} catch (CoreException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				String streamString_ddl = "" ;
				Scanner sc_in = new Scanner(in_stream) ;
				if (sc_in.hasNext())
					streamString_ddl = sc_in.useDelimiter("\\Z").next() ;
		
				streamString_ddl = streamString_ddl.replace("\n" , "") ;
				streamString_ddl = streamString_ddl.replace(" " , "") ; 
				streamString_ddl = streamString_ddl.replace("\t" , "") ;
				streamString_ddl = streamString_ddl.replace("\r" , "") ;
				try {
					in_stream.close();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
				e2.printStackTrace();
				}
				sc_in.close() ;
			
				try {
					in_stream = dat_file.getContents() ;
				} catch (CoreException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String streamString_databomb = "" ;
				sc_in = new Scanner(in_stream) ;
				if (sc_in.hasNext())
					streamString_databomb = sc_in.useDelimiter("\\Z").next() ;
		
				streamString_databomb = streamString_databomb.replace("\n" , "") ;
				streamString_databomb = streamString_databomb.replace(" " , "") ;
				streamString_databomb = streamString_databomb.replace("\t" , "") ;
				streamString_databomb = streamString_databomb.replace("\r" , "") ;
			
				try {
					in_stream.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				sc_in.close() ;
				try {
					DesignModeView view = (DesignModeView) HandlerUtil.getActiveWorkbenchWindowChecked(event).getActivePage().showView(DesignModeView.ID) ;
					System.out.println(streamString_ddl) ;
					view.UpdateView(streamString_ddl, streamString_databomb);
					return null ;
				} 	catch (PartInitException e) {
					e.printStackTrace();
				}
		
			}
			else if(!dat_file.exists())
			{
				String message = "The corresponding databomb file " + dat_file.getName() + " does not exist." ;
				IStatus status = new HIPIEStatus(IStatus.ERROR, HIPIEStatus.DESIGN_MODE_DATBOMB__ERROR, HIPIEActivator.ORG_XTEXT_HIPIE_HIPIE, message, null) ;
			    StatusManager.getManager().handle(status, StatusManager.LOG|StatusManager.SHOW);
			}
			else if(!ddl_file.exists())
			{
				String message = "The corresponding ddl file " + dat_file.getName() + " does not exist." ;
				IStatus status = new HIPIEStatus(IStatus.ERROR, HIPIEStatus.DESIGN_MODE_DATBOMB__ERROR, HIPIEActivator.ORG_XTEXT_HIPIE_HIPIE, message, null) ;
			    StatusManager.getManager().handle(status, StatusManager.LOG|StatusManager.SHOW);
			}
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
