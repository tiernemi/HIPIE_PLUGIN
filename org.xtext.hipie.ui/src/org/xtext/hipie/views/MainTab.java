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
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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

	String selectedFile;
	String defaultContainer;
	String containerPath;
	boolean externState;
	boolean designState;

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
		workspaceGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
				false));
		workspaceLabel = new Label(workspaceGroup, SWT.NONE);
		workspaceLabel.setText("Location:");
		workspaceText = new Text(workspaceGroup, SWT.NONE);
		workspaceText.setText(ResourcesPlugin.getWorkspace().getRoot()
				.getRawLocation().toOSString());
		defaultContainer = workspaceText.getText();
		GridData gdWorkText = new GridData(SWT.FILL, SWT.CENTER, true, true);
		gdWorkText.widthHint = 500;
		workspaceText.setLayoutData(gdWorkText);
		workspaceText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				updateLaunchConfigurationDialog();
				IPath newContainerPath = new Path("");
				if (newContainerPath.isValidPath(workspaceText.getText())) {
					newContainerPath = new Path(workspaceText.getText());
					IContainer newContainer = ResourcesPlugin.getWorkspace()
							.getRoot()
							.getContainerForLocation(newContainerPath);
					if (newContainer != null && newContainer.exists()) {
						ArrayList<IFile> dudFiles = new ArrayList<IFile>();
						findAllDudFiles(newContainer, dudFiles);
						ArrayList<String> items = new ArrayList<String>();
						for (int i = 0; i < dudFiles.size(); ++i)
							items.add(dudFiles.get(i).getFullPath()
									.toOSString());

						if (items.size() > 0) {
							String[] dudFileList = items
									.toArray(new String[items.size()]);
							programCombo.setItems(dudFileList);
							programCombo.setText(dudFileList[0]);
						} else {
							programCombo.removeAll();
							programCombo.setText("");
							selectedFile = programCombo.getText();
							System.out.println("error2");

						}
					} else {
						programCombo.removeAll();
						programCombo.setText("");
						selectedFile = programCombo.getText();
						System.out.println("error2");
					}
				}
			}
		});

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
				IPath workspacePath = cont.getRawLocation();
				IPath oldContainerPath = new Path(containerPath);

				if (cont.exists()) {
					ContainerSelectionDialog dialog = new ContainerSelectionDialog(
							PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getShell(),
							cont, false, "");
					if (dialog.open() == dialog.OK) {
						IContainer newContainer;
						IPath newContainerPath = workspacePath
								.append(((IPath) dialog.getResult()[0]));
						newContainer = ResourcesPlugin.getWorkspace().getRoot()
								.getContainerForLocation(newContainerPath);
						if (!oldContainerPath.equals(newContainerPath)) {
							workspaceText.setText(newContainerPath.toOSString());
							ArrayList<IFile> dudFiles = new ArrayList<IFile>();
							findAllDudFiles(newContainer, dudFiles);
							ArrayList<String> items = new ArrayList<String>();
							for (int i = 0; i < dudFiles.size(); ++i)
								items.add(dudFiles.get(i).getFullPath()
										.toOSString());
							if (items.size() > 0) {
								String[] dudFileList = items
										.toArray(new String[items.size()]);
								programCombo.setItems(dudFileList);
								programCombo.setText(dudFileList[0]);
								selectedFile = programCombo.getText();
							} else {
								programCombo.removeAll();
								programCombo.setText("");
								selectedFile = programCombo.getText();
							}
							updateLaunchConfigurationDialog();
						}

					} else
						System.out.println("error4"); // replace
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		// Bugged
		/*
		 * fileSystemButton.addSelectionListener(new SelectionListener() {
		 * 
		 * @Override public void widgetSelected(SelectionEvent e) {
		 * DirectoryDialog dialog = new DirectoryDialog(PlatformUI
		 * .getWorkbench().getActiveWorkbenchWindow().getShell()); String path =
		 * dialog.open(); if (!path.equals("")) { workspaceText.setText(path);
		 * IContainer newContainer = ResourcesPlugin.getWorkspace()
		 * .getRoot().getContainerForLocation(new Path(path));
		 * ResourcesPlugin.getWorkspace()
		 * .getRoot().findFilesForLocationURI(URIUtil.(new Path(path)). ;
		 * 
		 * System.out.println(newContainer.getRawLocation());
		 * System.out.println(newContainer.exists()); ArrayList<IFile> dudFiles
		 * = new ArrayList<IFile>(); findAllDudFiles(newContainer, dudFiles);
		 * ArrayList<String> items = new ArrayList<String>(); for (int i = 0; i
		 * < dudFiles.size(); ++i) items.add(dudFiles.get(i).getName());
		 * String[] dudFileList = items.toArray(new String[items .size()]);
		 * programCombo.setItems(dudFileList); } }
		 * 
		 * @Override public void widgetDefaultSelected(SelectionEvent e) { //
		 * TODO Auto-generated method stub
		 * 
		 * } });
		 */
		programToRunGroup = new Group(comp, SWT.NONE);
		programToRunGroup.setText("DUD file to Run");
		programToRunGroup.setLayout(new GridLayout(1, false));
		programToRunGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
				true));

		Composite comboComposite = new Composite(programToRunGroup, SWT.NONE);
		comboComposite.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
				true, true));
		comboComposite.setLayout(new GridLayout(2, false));

		programLabel = new Label(comboComposite, SWT.NONE);
		programLabel.setText("Dud file to Run:");
		programCombo = new Combo(comboComposite, SWT.DROP_DOWN | SWT.READ_ONLY);

		ArrayList<IFile> dudFiles = new ArrayList<IFile>();
		findAllDudFiles(ResourcesPlugin.getWorkspace().getRoot(), dudFiles);
		ArrayList<String> items = new ArrayList<String>();
		for (int i = 0; i < dudFiles.size(); ++i)
			items.add(dudFiles.get(i).getFullPath().toOSString());
		String[] dudFileList = items.toArray(new String[items.size()]);
		programCombo.setItems(dudFileList);
		programCombo.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
				true, true, 1, 1));

		programCombo.setEnabled(true);
		if (programCombo.getItems().length >= 1) {
			programCombo
					.select(programCombo.indexOf(programCombo.getItems()[0]));
			selectedFile = programCombo.getText();

		} else
			programCombo.setEnabled(false);

		programCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!selectedFile.equals(programCombo.getText()))
					updateLaunchConfigurationDialog();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});

		Composite radioComposite = new Composite(programToRunGroup, SWT.NONE);
		radioComposite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				true));
		radioComposite.setLayout(new GridLayout(2, false));

		runInExternalBrowserB = new Button(radioComposite, SWT.RADIO);
		runInExternalBrowserB.setText("Run in external browser");
		runInExternalBrowserB.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				true, true));
		runInExternalBrowserB.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (externState != runInExternalBrowserB.getSelection()) {
					updateLaunchConfigurationDialog();
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		runInDesignModeB = new Button(radioComposite, SWT.RADIO);
		runInDesignModeB.setText("Run in design mode");

		runInDesignModeB.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (designState != runInDesignModeB.getSelection()) {
					updateLaunchConfigurationDialog();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		runInDesignModeB.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				true, true));
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
		try {
			workspaceText.setText(configuration.getAttribute(
					"ContainerLocation", ResourcesPlugin.getWorkspace()
							.getRoot().getRawLocation().toOSString()));
			containerPath = configuration.getAttribute("ContainerLocation",
					ResourcesPlugin.getWorkspace().getRoot().getRawLocation()
							.toOSString());
		} catch (CoreException e) {
			e.printStackTrace();
		}
		try {
			final IPath dudFilePath = new Path(configuration.getAttribute(
					"filePath", selectedFile));
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					ArrayList<IFile> dudFiles = new ArrayList<IFile>();
					IContainer cont = ResourcesPlugin.getWorkspace().getRoot()
							.getContainerForLocation(new Path(containerPath));
					findAllDudFiles(cont, dudFiles);
					ArrayList<String> items = new ArrayList<String>();
					for (int i = 0; i < dudFiles.size(); ++i)
						items.add(dudFiles.get(i).getFullPath().toOSString());
					String[] dudFileList = items.toArray(new String[items
							.size()]);
					programCombo.setItems(dudFileList);
					programCombo.setText(dudFilePath.toOSString());
				}

			});
		} catch (CoreException e) {
			e.printStackTrace();
		}

		try {
			runInDesignModeB.setSelection(configuration.getAttribute(
					"isRunningDesign", false));
			designState = configuration.getAttribute("isRunningDesign", false);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		try {
			runInExternalBrowserB.setSelection(configuration.getAttribute(
					"isRunningExtern", true));
			externState = configuration.getAttribute("isRunningExtern", true);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute("filePath", programCombo.getText());
		configuration.setAttribute("isRunningDesign",
				runInDesignModeB.getSelection());
		configuration.setAttribute("isRunningExtern",
				runInExternalBrowserB.getSelection());
		configuration
				.setAttribute("ContainerLocation", workspaceText.getText());
		ArrayList<String> temp = new ArrayList<String>();
		String[] itemList = programCombo.getItems();
		for (int i = 0; i < itemList.length; ++i)
			temp.add(itemList[i]);
		configuration.setAttribute("dudFileList", temp);
		selectedFile = programCombo.getText();
		externState = runInExternalBrowserB.getSelection();
		designState = runInDesignModeB.getSelection();
		containerPath = workspaceText.getText();
	}

	@Override
	public String getName() {
		return "Main";
	}

	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		// TODO Auto-generated method stub
		return super.isValid(launchConfig);
	}
	
}
