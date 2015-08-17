package org.xtext.hipie.views;

import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.CheckedTreeSelectionDialog;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.internal.browser.DefaultWebBrowser;

;

public class MainTab extends AbstractLaunchConfigurationTab {

	Label programToRun;
	Combo dudList;
	Composite programComp;

	Label workspaceLabel;
	Group workspaceGroup;
	Text workspaceText;

	Group programToRunGroup;
	Label programLabel;
	Combo programCombo;

	Button runInExternalBrowserB;
	Label runInExternalBrowserL;
	Combo externalBrowsers;

	Button runInDesignModeB;
	Label runInDesignModeL;

	@Override
	public void createControl(Composite parent) {
		Font font = parent.getFont();

		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);
		GridLayout topLayout = new GridLayout();
		topLayout.verticalSpacing = 0;
		topLayout.numColumns = 1;
		topLayout.horizontalSpacing = 2;
		GridData gd = new GridData(SWT.BEGINNING, SWT.CENTER, true, true);
		comp.setLayoutData(gd);
		comp.setFont(font);
		comp.setLayout(topLayout);

		workspaceGroup = new Group(comp, SWT.NONE);
		workspaceGroup.setText("Workspace Data");
		workspaceGroup.setLayout(new GridLayout(2, false));
		workspaceGroup
				.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		workspaceLabel = new Label(workspaceGroup, SWT.NONE);
		workspaceLabel.setText("Location:");
		workspaceText = new Text(workspaceGroup, SWT.NONE);
		workspaceText.setText(ResourcesPlugin.getWorkspace().getRoot()
				.getRawLocation().toOSString());
		GridData gdWorkText = new GridData(SWT.FILL, SWT.CENTER, true, true);
		gdWorkText.widthHint = 500;
		workspaceText.setLayoutData(gdWorkText);

		Composite workspaceFileSystemComp = new Composite(workspaceGroup,
				SWT.NONE);
		workspaceFileSystemComp.setLayoutData(new GridData(SWT.RIGHT,
				SWT.CENTER, true, true, 2, 1));
		workspaceFileSystemComp.setLayout(new GridLayout(2, false));
		Button workspaceButton = new Button(workspaceFileSystemComp, SWT.PUSH);
		workspaceButton.setText("WorkSpace...");
		Button fileSystemButton = new Button(workspaceFileSystemComp, SWT.PUSH);
		fileSystemButton.setText("File System...");

		workspaceButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IContainer cont = ResourcesPlugin.getWorkspace().getRoot();
				if (cont.exists()) {
					ContainerSelectionDialog dialog = new ContainerSelectionDialog(
							PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getShell(),
							cont, false, "");
					if (dialog.open() == dialog.OK)
						workspaceText.setText(cont
								.getFile(((IPath) dialog.getResult()[0]))
								.getRawLocation().toOSString());
				} else
					System.out.println("error"); // replace
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		fileSystemButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(PlatformUI
						.getWorkbench().getActiveWorkbenchWindow().getShell());
				String path = dialog.open();
				if (!path.equals(""))
					workspaceText.setText(path);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		programToRunGroup = new Group(comp, SWT.NONE);
		programToRunGroup.setText("DUD file to Run");
		programToRunGroup.setLayout(new GridLayout(3, false));
		programToRunGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
				true));

		programLabel = new Label(programToRunGroup, SWT.NONE);
		programLabel.setText("Dud file:");
		programCombo = new Combo(programToRunGroup, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		
		ArrayList<IFile> dudFiles = new ArrayList<IFile>() ;
		findAllDudFiles(ResourcesPlugin.getWorkspace().getRoot(), dudFiles) ;
		ArrayList<String> items = new ArrayList<String>() ;
		for (int i = 0 ; i < dudFiles.size() ; ++i)
			items.add(dudFiles.get(i).getName()) ;
		String[] dudFileList =  items.toArray(new String[items.size()]) ;
		programCombo.setItems(dudFileList) ;
		programCombo.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, true, 2, 1));
		
		if (programCombo.getItems().length >= 1)
			programCombo.setText(programCombo.getItems()[0]);
		else
			programCombo.setEnabled(false);
		
		Button runInExternalBrowserB = new Button(programToRunGroup, SWT.CHECK) ;
		runInExternalBrowserB.setText("Run in external browser :");
		Label runInExternalBrowserL;
		Combo externalBrowsers;


		// programToRun.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
		// false, false)) ;
		// programToRun.setText("Dud file to run:") ;
		// dudList = new Combo(comp, SWT.DROP_DOWN | SWT.READ_ONLY) ;
		// dudList.setSize(700, dudList.getItemHeight());

		// ResourcesPlugin.getWorkspace() ;

		// dudList.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true,
		// false)) ;

	}

	private void findAllDudFiles(IContainer cont, ArrayList<IFile> dudFiles) {
		IResource[] memberList;

		try {
			memberList = cont.members();
			for (int i = 0; i < memberList.length; ++i) {
				if (memberList[i] instanceof IFolder
						|| memberList[i] instanceof IProject)
					findAllDudFiles(((IContainer) memberList[i]), dudFiles);
				if (memberList[i] instanceof IFile)
					if (((IFile) memberList[i]).getFileExtension()
							.equals("dud"))
						dudFiles.add(((IFile) memberList[i]));
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return;
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {

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
