package org.xtext.hipie.launcher;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
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
import org.xtext.hipie.views.DesignModeView;

import com.google.inject.Injector;

public class HIPIELauncher implements ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {

		IPath containerPath = new Path(configuration.getAttribute(
				"ContainerLocation", ResourcesPlugin.getWorkspace().getRoot()
						.getRawLocation().toOSString()));
		IPath filePath = new Path(configuration.getAttribute("filePath", ""));
		final IFile dudFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFileForLocation(containerPath.append(filePath));

		if (dudFile == null || !dudFile.exists()) {
			String message = "The resource " + dudFile.getName()
					+ " does not exist.";
			IStatus status = new HIPIEStatus(IStatus.ERROR,
					"org.xtext.hipie.ui", HIPIEStatus.RESOURCE_DOESNT_EXIST,
					message, null);
			StatusManager.getManager().handle(status,
					StatusManager.LOG | StatusManager.SHOW);
			return ;
		}
			
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				IEditorReference[] refList = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage()
						.getEditorReferences();
				for (int i = 0; i < refList.length; ++i) {
					IFile editorFile = null ;
					try {
						editorFile = (IFile) refList[i].getEditorInput().getAdapter(IFile.class);
					} catch (PartInitException e1) {
						e1.printStackTrace();
					}
					if (editorFile.getFullPath().equals(dudFile.getFullPath())) {
						if (refList[i].getEditor(false).isDirty()) {
							MessageDialog dialog = new MessageDialog(
									null,
									"Unsaved File",
									null,
									dudFile.getName()
											+ " has unsaved changes. Do you want to save these changes?",
									MessageDialog.QUESTION, new String[] {
											"Yes", "No" }, 0);
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
			}
		});

		boolean externState = configuration.getAttribute("isRunningExtern",
				true);
		boolean designState = configuration.getAttribute("isRunningDesign",
				false);
		Injector injector = HIPIEActivator.getInstance().getInjector(
				"org.xtext.hipie.HIPIE");
		ResourceSet resourceSet = injector.getInstance(ResourceSet.class);
		IGenerator generator = injector.getInstance(IGenerator.class);
		IResourceValidator validator = injector
				.getInstance(IResourceValidator.class);
		InMemoryFileSystemAccess fsa = injector
				.getInstance(InMemoryFileSystemAccess.class);

		Resource r = resourceSet.getResource(
				URI.createPlatformResourceURI(filePath.toOSString(), false),
				true);

		try {
			r.load(null);
		} catch (IOException e) {
			String message = "The resource " + dudFile.getName()
					+ " does not exist.";
			IStatus status = new HIPIEStatus(IStatus.ERROR,
					"org.xtext.hipie.ui", HIPIEStatus.RESOURCE_DOESNT_EXIST,
					message, e);
			StatusManager.getManager().handle(status,
					StatusManager.LOG | StatusManager.SHOW);
			e.printStackTrace();
		}

		List<Issue> errorList = validator.validate(r, CheckMode.ALL,
				CancelIndicator.NullImpl);

		if (errorList.isEmpty()) {
			generator.doGenerate(r, fsa);
			final IFile htmlFile = dudFile.getProject().getFile(
					dudFile.getProjectRelativePath().removeFileExtension()
							.addFileExtension("html"));

			if (htmlFile.exists()) {
				if (externState) {
					URL url;
					try {
						url = htmlFile.getRawLocationURI().toURL();
						PlatformUI.getWorkbench().getBrowserSupport()
								.getExternalBrowser().openURL(url);
					} catch (MalformedURLException e) {
						String message = "The resource " + dudFile.getName()
								+ " does not exist.";
						IStatus status = new HIPIEStatus(IStatus.ERROR,
								"org.xtext.hipie.ui",
								HIPIEStatus.MALFORMED_URL, message, e);
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
				if (designState) {
					IFile corDdlFile = htmlFile.getProject().getFile(
							htmlFile.getProjectRelativePath()
									.removeFileExtension()
									.addFileExtension("ddl"));
					IFile corDatabombFile = htmlFile.getProject().getFile(
							htmlFile.getProjectRelativePath()
									.removeFileExtension()
									.addFileExtension("databomb"));
					IFile corPersistFile = htmlFile.getProject().getFile(
							htmlFile.getProjectRelativePath()
									.removeFileExtension()
									.addFileExtension("persist"));

					if (corDdlFile.exists() && corDatabombFile.exists()
							&& corPersistFile.exists()) {
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								DesignModeView view = null;
								try {
									view = (DesignModeView) PlatformUI
											.getWorkbench()
											.getActiveWorkbenchWindow()
											.getActivePage()
											.showView(DesignModeView.ID);
									view.updateView(htmlFile);

								} catch (PartInitException e) {
									String message = "Design Mode failed to open.";
									IStatus status = new HIPIEStatus(
											IStatus.ERROR,
											"org.xtext.hipie.ui",
											HIPIEStatus.DESIGN_MODE_FAILED,
											message, e);
									StatusManager.getManager().handle(
											status,
											StatusManager.LOG
													| StatusManager.SHOW);
									e.printStackTrace();
								}
							}
						});
					}
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
