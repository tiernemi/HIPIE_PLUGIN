package org.xtext.hipie.ui;

import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

public class HIPIEWizard extends BasicNewProjectResourceWizard implements INewWizard {
	
	public HIPIEWizard() {
	    setWindowTitle("HIPIE Project");
	}
	
	@Override
	public boolean performFinish() {
		boolean state = super.performFinish() ;
		addNature() ;
		return state ;
	}
	
	public void addNature() {
		try {
			IProjectDescription description = getNewProject().getDescription();
			String[] natures = description.getNatureIds();
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[natures.length] = HIPIENature.ID ;
			description.setNatureIds(newNatures);
			getNewProject().setDescription(description, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
}
