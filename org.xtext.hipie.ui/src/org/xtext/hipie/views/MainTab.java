package org.xtext.hipie.views;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.internal.browser.DefaultWebBrowser; ;

public class MainTab extends AbstractLaunchConfigurationTab {

	Label programToRun ;
	Combo dudList ;
	Composite programComp ;
	
	Label workspaceLabel ;
	//workspaceTextBox 
	
	Button runInExternalBrowserB ;
	Label runInExternalBrowserL ;
//	Combo externalBrowsers ;
	Composite externComp ;
	
	Button runInDesignModeB ;
	Label runInDesignModeL ;
	Composite designModeComp ;
	
	@Override
	public void createControl(Composite parent) {		
		Font font = parent.getFont();
		
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);
		GridLayout topLayout = new GridLayout();
		topLayout.verticalSpacing = 0;
		topLayout.numColumns = 2;
		topLayout.horizontalSpacing = 2;
		comp.setLayout(topLayout);
		comp.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true , true));
		comp.setFont(font);
			
		createVerticalSpacer(comp, 2);

		workspaceLabel = new Label(comp, SWT.NONE) ;
		workspaceLabel.setText("Workspace data:");

		
		programToRun = new Label(comp, SWT.NONE) ;
		programToRun.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false)) ;
		programToRun.setText("Dud file to run:") ;
		dudList = new Combo(comp, SWT.DROP_DOWN | SWT.READ_ONLY) ;
		dudList.setSize(700, dudList.getItemHeight());
		
		
		ResourcesPlugin.getWorkspace() ;
		
		dudList.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false)) ;

	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		return "Main";
	}

}
