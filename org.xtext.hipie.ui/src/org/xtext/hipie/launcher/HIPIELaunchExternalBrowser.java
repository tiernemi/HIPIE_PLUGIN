package org.xtext.hipie.launcher;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;
import org.xtext.hipie.error.HIPIEStatus;
import org.xtext.hipie.ui.internal.HIPIEActivator;

import com.google.inject.Injector;

public class HIPIELaunchExternalBrowser implements ILaunchShortcut {

	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof TreeSelection) {
			Object[] selectionArray = ((TreeSelection) selection).toArray();
			for (int i = 0; i < selectionArray.length; ++i)
				if (selectionArray[i] instanceof IFile)
					if (((IFile) selectionArray[i]).getFileExtension().equals(
							"dud")) {
						IFile dudFile = (IFile) selectionArray[i];
						launchInExternal(dudFile);
					}
		}

	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		IFile dudFile = (IFile) editor.getEditorInput().getAdapter(IFile.class);
		launchInExternal(dudFile);
	}

	public void launchInExternal(final IFile dudFile) {

		if (dudFile == null || !dudFile.exists()) {
			String message = "The resource " + dudFile.getName()
					+ " does not exist.";
			IStatus status = new HIPIEStatus(IStatus.ERROR,
					"org.xtext.hipie.ui", HIPIEStatus.RESOURCE_DOESNT_EXIST,
					message, null);
			StatusManager.getManager().handle(status,
					StatusManager.LOG | StatusManager.SHOW);
			return;
		}

		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				IEditorReference[] refList = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage()
						.getEditorReferences();
				for (int i = 0; i < refList.length; ++i) {
					IFile editorFile = null;
					try {
						editorFile = (IFile) refList[i].getEditorInput()
								.getAdapter(IFile.class);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
					if (editorFile.getFullPath().equals(dudFile.getFullPath())) {
						MessageDialog dialog = new MessageDialog(
								null,
								"Unsaved File",
								null,
								dudFile.getName()
										+ " has unsaved changes. Do you want to save these changes?",
								MessageDialog.QUESTION, new String[] { "Yes",
										"No" }, 0);
						int result = dialog.open();
						if (result == 0)
							refList[i].getEditor(false).doSave(null);
						try {
							dudFile.getProject().refreshLocal(
									IResource.DEPTH_INFINITE, null);
						} catch (CoreException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});

		Injector injector = HIPIEActivator.getInstance().getInjector(
				"org.xtext.hipie.HIPIE");
		ResourceSet resourceSet = injector.getInstance(ResourceSet.class);
		IGenerator generator = injector.getInstance(IGenerator.class);
		IResourceValidator validator = injector
				.getInstance(IResourceValidator.class);

		InMemoryFileSystemAccess fsa = injector
				.getInstance(InMemoryFileSystemAccess.class);

		Resource r = resourceSet.getResource(URI.createPlatformResourceURI(
				dudFile.getFullPath().toOSString(), false), true);
		try {
			r.load(null);
		} catch (IOException e1) {
			String message = "The resource " + dudFile.getName()
					+ " does not exist.";
			IStatus status = new HIPIEStatus(IStatus.ERROR,
					"org.xtext.hipie.ui", HIPIEStatus.RESOURCE_DOESNT_EXIST,
					message, e1);
			StatusManager.getManager().handle(status,
					StatusManager.LOG | StatusManager.SHOW);
			e1.printStackTrace();
		}

		List<Issue> errorList = validator.validate(r, CheckMode.ALL,
				CancelIndicator.NullImpl);

		if (errorList.isEmpty()) {
			generator.doGenerate(r, fsa);

			IFile htmlFile = dudFile.getProject().getFile(
					dudFile.getProjectRelativePath().removeFileExtension()
							.addFileExtension("html"));

			if (htmlFile.exists()) {
				URL url;
				try {
					url = htmlFile.getRawLocationURI().toURL();
					PlatformUI.getWorkbench().getBrowserSupport()
							.getExternalBrowser().openURL(url);
				} catch (MalformedURLException e) {
					String message = "The resource " + dudFile.getName()
							+ " does not exist.";
					IStatus status = new HIPIEStatus(IStatus.ERROR,
							"org.xtext.hipie.ui", HIPIEStatus.MALFORMED_URL,
							message, e);
					StatusManager.getManager().handle(status,
							StatusManager.LOG | StatusManager.SHOW);
					e.printStackTrace();
				} catch (PartInitException e) {
					String message = "The resource " + dudFile.getName()
							+ " does not exist.";
					IStatus status = new HIPIEStatus(IStatus.ERROR,
							"org.xtext.hipie.ui",
							HIPIEStatus.BROWSER_OPENING_FAILED, message, e);
					StatusManager.getManager().handle(status,
							StatusManager.LOG | StatusManager.SHOW);
					e.printStackTrace();
				}
			}
		} else {
			String message = "Errors exist within " + dudFile.getName() + ".";
			IStatus status = new HIPIEStatus(IStatus.ERROR,
					"org.xtext.hipie.ui", HIPIEStatus.ERRORS_IN_FILE, message,
					null);
			StatusManager.getManager().handle(status,
					StatusManager.LOG | StatusManager.SHOW);
		}

	}

}
