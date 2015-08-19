package org.xtext.hipie.launcher;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class HIPIELaunchConfigurationTabGroup extends
		AbstractLaunchConfigurationTabGroup {

	public HIPIELaunchConfigurationTabGroup() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
				new MainTab()
		};
		setTabs(tabs);
	}
}
