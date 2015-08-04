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

	static public String ID = "org.xtext.hipie.visualiser_post_build" ;
	
	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {

	}

	@Override
	public void dispose() {

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorInput editorInput = HandlerUtil.getActiveEditorInput(event) ;
		IFile file = ((FileEditorInput)editorInput).getFile() ;
			
		IPath ddlFilepath = file.getFullPath().removeFileExtension().addFileExtension("ddl") ;
		IPath datFilepath = file.getFullPath().removeFileExtension().addFileExtension("databomb") ;
		IPath perFilepath = file.getFullPath().removeFileExtension().addFileExtension("persist") ;

		IFile ddlFile = ResourcesPlugin.getWorkspace().getRoot().getFile(ddlFilepath) ;
		IFile datFile = ResourcesPlugin.getWorkspace().getRoot().getFile(datFilepath) ;
		IFile perFile = ResourcesPlugin.getWorkspace().getRoot().getFile(perFilepath) ;

		
		if (ddlFile.exists() && datFile.exists())
		{
			InputStream inStream = null;
			try {
				inStream = ddlFile.getContents();
			} catch (CoreException e3) {
				e3.printStackTrace();
			}
			String streamStringDdl = "" ;
			Scanner scIn = new Scanner(inStream) ;
			if (scIn.hasNext())
				streamStringDdl = scIn.useDelimiter("\\Z").next() ;
		
			streamStringDdl = streamStringDdl.replace("\n" , "") ;
			streamStringDdl = streamStringDdl.replace(" " , "") ; 
			streamStringDdl = streamStringDdl.replace("\t" , "") ;
			streamStringDdl = streamStringDdl.replace("\r" , "") ;
			
			try {
				inStream.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			scIn.close() ;
			
			try {
				inStream = datFile.getContents() ;
			} catch (CoreException e1) {
				e1.printStackTrace();
			}
			String streamStringDatabomb = "" ;
			scIn = new Scanner(inStream) ;
			if (scIn.hasNext())
				streamStringDatabomb = scIn.useDelimiter("\\Z").next() ;
		
			streamStringDatabomb = streamStringDatabomb.replace("\n" , "") ;
			streamStringDatabomb = streamStringDatabomb.replace(" " , "") ;
			streamStringDatabomb = streamStringDatabomb.replace("\t" , "") ;
			streamStringDatabomb = streamStringDatabomb.replace("\r" , "") ;
			
			try {
				inStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			scIn.close() ;
				
			String streamStringPer = "" ;
			if(perFile.exists())
			{
				try {
					inStream = perFile.getContents() ;
				} catch (CoreException e1) {
					e1.printStackTrace();
				}
				scIn = new Scanner(inStream) ;
				if (scIn.hasNext())
					streamStringPer = scIn.useDelimiter("\\Z").next() ;
			
				streamStringPer = streamStringDatabomb.replace("\n" , "") ;
				streamStringPer = streamStringDatabomb.replace(" " , "") ;
				streamStringPer = streamStringDatabomb.replace("\t" , "") ;
				streamStringPer = streamStringDatabomb.replace("\r" , "") ;
				
				try {
					inStream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				scIn.close() ;
			}
			try {
				DesignModeView view = (DesignModeView) HandlerUtil.getActiveWorkbenchWindowChecked(event).getActivePage().showView(DesignModeView.ID) ;
				view.setDdl(streamStringDdl);
				view.setDatabomb(streamStringDatabomb);
				view.setPersist(streamStringPer);
				view.setPer_filepath(perFile.getRawLocation().toOSString());
				view.updateView();
			} 	catch (PartInitException e) {
				e.printStackTrace();
			}
		}
		else if(!datFile.exists())
		{
			String message = "The corresponding databomb file " + datFile.getName() + " does not exist." ;
			IStatus status = new HIPIEStatus(IStatus.ERROR, HIPIEStatus.DESIGN_MODE_DATBOMB__ERROR, HIPIEActivator.ORG_XTEXT_HIPIE_HIPIE, message, null) ;
		    StatusManager.getManager().handle(status, StatusManager.LOG|StatusManager.SHOW);
		}
		else if(!ddlFile.exists())
		{
			String message = "The corresponding ddl file " + datFile.getName() + " does not exist." ;
			IStatus status = new HIPIEStatus(IStatus.ERROR, HIPIEStatus.DESIGN_MODE_DATBOMB__ERROR, HIPIEActivator.ORG_XTEXT_HIPIE_HIPIE, message, null) ;
		    StatusManager.getManager().handle(status, StatusManager.LOG|StatusManager.SHOW);
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
