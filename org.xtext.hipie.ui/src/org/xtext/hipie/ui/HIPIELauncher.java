package org.xtext.hipie.ui;
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
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.xtext.hipie.ui.internal.HIPIEActivator;

import com.google.inject.Injector;


public class HIPIELauncher implements ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		
		IPath containerPath = new Path(configuration.getAttribute("ContainerLocation", ResourcesPlugin.getWorkspace()
						.getRoot().getRawLocation().toOSString())) ;
		IPath filePath = new Path(configuration.getAttribute("filePath", "")) ;
		IFile dudFile = ResourcesPlugin.getWorkspace().getRoot().getFile(containerPath.append(filePath)) ;
		System.out.println(dudFile.getRawLocation()) ;
		boolean externState = configuration.getAttribute("isRunningExtern", true);
		Injector injector = HIPIEActivator.getInstance().getInjector("org.xtext.hipie.HIPIE");
		ResourceSet resourceSet = injector.getInstance(ResourceSet.class) ;
		IGenerator generator = injector.getInstance(IGenerator.class);
		InMemoryFileSystemAccess fsa = injector.getInstance(InMemoryFileSystemAccess.class);

		Resource r = resourceSet.getResource(URI.createPlatformResourceURI(filePath.toOSString(), false), true);
		try {
			r.load(null);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		generator.doGenerate(r, fsa);
		System.out.println(dudFile.exists());
		IFile htmlFile =  dudFile.getProject().getFile(dudFile.getProjectRelativePath().removeFileExtension().addFileExtension("html")) ; 
          
         if (htmlFile.exists() && externState) {
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
		System.out.println(ResourcesPlugin.getWorkspace().getRoot()) ;

	}

}
