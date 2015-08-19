package org.xtext.hipie.launcher;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
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
		IFile dudFile = ResourcesPlugin.getWorkspace().getRoot()
				.getFileForLocation(containerPath.append(filePath));
		boolean externState = configuration.getAttribute("isRunningExtern",
				true);
		boolean designState = configuration.getAttribute("isRunningDesign",
				false);
		Injector injector = HIPIEActivator.getInstance().getInjector(
				"org.xtext.hipie.HIPIE");
		ResourceSet resourceSet = injector.getInstance(ResourceSet.class);
		IGenerator generator = injector.getInstance(IGenerator.class);
		InMemoryFileSystemAccess fsa = injector
				.getInstance(InMemoryFileSystemAccess.class);

		Resource r = resourceSet.getResource(
				URI.createPlatformResourceURI(filePath.toOSString(), false),
				true);
		try {
			r.load(null);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
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
					e.printStackTrace();
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
			if (designState) {
				IFile corDdlFile = htmlFile.getProject().getFile(
						htmlFile.getProjectRelativePath().removeFileExtension()
								.addFileExtension("ddl"));
				IFile corDatabombFile = htmlFile.getProject().getFile(
						htmlFile.getProjectRelativePath().removeFileExtension()
								.addFileExtension("databomb"));
				IFile corPersistFile = htmlFile.getProject().getFile(
						htmlFile.getProjectRelativePath().removeFileExtension()
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
							} catch (PartInitException e) {
								e.printStackTrace();
							}
							view.updateView(htmlFile);
						}
					});
				}
			}
		}
	}

}
