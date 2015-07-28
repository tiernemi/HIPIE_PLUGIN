package org.xtext.hipie.ui;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

public class HIPIEWizard extends BasicNewProjectResourceWizard implements INewWizard {

	private WizardNewProjectCreationPage pageOne;
	
	public HIPIEWizard() {
	    setWindowTitle("HIPIE Project");
	}
}
