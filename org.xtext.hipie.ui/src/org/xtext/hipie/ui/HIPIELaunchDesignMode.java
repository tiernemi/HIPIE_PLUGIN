package org.xtext.hipie.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.xtext.hipie.views.DesignModeView;

public class HIPIELaunchDesignMode implements ILaunchShortcut {

	@Override
	public void launch(ISelection selection, String mode) {
		IFile dudFile = null ;
		
		// BUILD
		
		if (selection instanceof TreeSelection) {
			Object[] selectionArray = ((TreeSelection) selection).toArray() ;
			for (int i = 0 ; i < selectionArray.length ; ++i)
				if (selectionArray[i] instanceof IFile)
					if (((IFile) selectionArray[i]).getFileExtension().equals("dud"))
						 dudFile = (IFile) selectionArray[i] ;
	        
			if(dudFile != null) {
				IFile htmlFile =  dudFile.getProject().getFile(dudFile.getProjectRelativePath().removeFileExtension().addFileExtension("html")) ;
	        
				if (htmlFile.exists()) {
					DesignModeView view ;
					try {
						view = (DesignModeView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DesignModeView.ID);
						view.updateView(htmlFile) ;
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
	        }
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
        IFile dudFile = (IFile) editor.getEditorInput().getAdapter(IFile.class) ;
        IFile htmlFile =  dudFile.getProject().getFile(dudFile.getProjectRelativePath().removeFileExtension().addFileExtension("html")) ;
        
        if (htmlFile.exists()) {
			DesignModeView view;
			try {
				view = (DesignModeView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DesignModeView.ID);
				view.updateView(htmlFile) ;
			} catch (PartInitException e) {
				e.printStackTrace();
			}
        }
	}

}
