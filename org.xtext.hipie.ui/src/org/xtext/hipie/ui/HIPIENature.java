package org.xtext.hipie.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.xtext.hipie.commands_listeners.DataSaveListener;

public class HIPIENature implements IProjectNature {

	private IProject project ;
	public static String ID = "org.xtext.hipie.ui.HIPIENature" ;
	
	@Override
	public void configure() throws CoreException {
		DataSaveListener.init() ;
	}

	@Override
	public void deconfigure() throws CoreException {
		DataSaveListener.remove();
	}

	@Override
	public IProject getProject() {
		return project;
	}

	@Override
	public void setProject(IProject proj) {
		project = proj ;
	}

}
