package org.xtext.hipie.ui;

import org.eclipse.ui.INewWizard;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

public class HIPIEWizard extends BasicNewProjectResourceWizard implements INewWizard {
	
	public HIPIEWizard() {
	    setWindowTitle("HIPIE Project");
	}
}
