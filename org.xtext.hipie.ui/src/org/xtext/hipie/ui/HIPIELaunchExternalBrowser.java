package org.xtext.hipie.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;





public class HIPIELaunchExternalBrowser implements ILaunchShortcut {

    
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
					URL url;
					try {
						url = htmlFile.getRawLocationURI().toURL();
						PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(url) ;
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
	        }

		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		// BUILD HERE
		//  EclipseResourceFileSystemAccess fsa = fileAccessProvider.get();
          IFile dudFile = (IFile) editor.getEditorInput().getAdapter(IFile.class) ;
          IFile htmlFile =  dudFile.getProject().getFile(dudFile.getProjectRelativePath().removeFileExtension().addFileExtension("html")) ;
          
          if (htmlFile.exists()) {
        	 URL url;
			try {
				url = htmlFile.getRawLocationURI().toURL();
	        	PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(url) ;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (PartInitException e) {
				e.printStackTrace();
			}
          }
	}

}
