package org.xtext.hipie.launcher;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.xtext.hipie.ui.internal.HIPIEActivator;
import org.xtext.hipie.views.DesignModeView;

import com.google.inject.Injector;

public class HIPIELaunchDesignMode implements ILaunchShortcut {

	@Override
	public void launch(ISelection selection, String mode) {
		Injector injector = HIPIEActivator.getInstance().getInjector(
				"org.xtext.hipie.HIPIE");
		ResourceSet resourceSet = injector.getInstance(ResourceSet.class);
		IGenerator generator = injector.getInstance(IGenerator.class);
		InMemoryFileSystemAccess fsa = injector
				.getInstance(InMemoryFileSystemAccess.class);
		IFile dudFile = null;

		if (selection instanceof TreeSelection) {
			Object[] selectionArray = ((TreeSelection) selection).toArray();
			for (int i = 0; i < selectionArray.length; ++i)
				if (selectionArray[i] instanceof IFile)
					if (((IFile) selectionArray[i]).getFileExtension().equals(
							"dud"))
						dudFile = (IFile) selectionArray[i];

			if (dudFile != null) {
				Resource r = resourceSet.getResource(URI.createPlatformResourceURI(dudFile.getFullPath().toOSString(), false), true);
				try {
					r.load(null);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				generator.doGenerate(r, fsa);

				IFile htmlFile = dudFile.getProject().getFile(
						dudFile.getProjectRelativePath().removeFileExtension()
								.addFileExtension("html"));

				if (htmlFile.exists()) {
					DesignModeView view;
					try {
						view = (DesignModeView) PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow().getActivePage()
								.showView(DesignModeView.ID);
						view.updateView(htmlFile);
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		Injector injector = HIPIEActivator.getInstance().getInjector("org.xtext.hipie.HIPIE");
		ResourceSet resourceSet = injector.getInstance(ResourceSet.class);
		IGenerator generator = injector.getInstance(IGenerator.class);
		InMemoryFileSystemAccess fsa = injector.getInstance(InMemoryFileSystemAccess.class);
		IFile dudFile = (IFile) editor.getEditorInput().getAdapter(IFile.class);

		Resource r = resourceSet.getResource(URI.createPlatformResourceURI(dudFile.getFullPath().toOSString(), false), true);
		try {
			r.load(null);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		generator.doGenerate(r, fsa);

		IFile htmlFile = dudFile.getProject().getFile(
				dudFile.getProjectRelativePath().removeFileExtension()
						.addFileExtension("html"));

		if (htmlFile.exists()) {
			DesignModeView view;
			try {
				view = (DesignModeView) PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage()
						.showView(DesignModeView.ID);
				view.updateView(htmlFile);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
	}

}
