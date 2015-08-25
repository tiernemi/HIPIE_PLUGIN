package org.xtext.hipie.launcher;

import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

public class MainTab extends AbstractLaunchConfigurationTab {

	Label containerLabel;
	Group containerGroup;
	Text dudFileContainerText;
	Button workspaceButton;
	Composite workspaceFileSystemComp;

	Group programToRunGroup;
	Label dudFileComboLabel;
	Combo dudFileListCombo;
	Composite comboComposite;

	Composite radioComposite;
	Button externalBrowserButton;
	Button designModeButton;

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

		containerGroup = new Group(comp, SWT.NONE);
		containerGroup.setText("Workspace Data");
		containerGroup.setLayout(new GridLayout(2, false));
		containerGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
				false));
		containerLabel = new Label(containerGroup, SWT.NONE);
		containerLabel.setText("Location:");
		dudFileContainerText = new Text(containerGroup, SWT.NONE);
		dudFileContainerText.setText(ResourcesPlugin.getWorkspace().getRoot()
				.getRawLocation().toOSString());
		defaultContainer = dudFileContainerText.getText();
		GridData gdWorkText = new GridData(SWT.FILL, SWT.CENTER, true, true);
		gdWorkText.widthHint = 500;
		dudFileContainerText.setLayoutData(gdWorkText);
		dudFileContainerText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				updateLaunchConfigurationDialog();
				IPath newContainerPath = new Path("");
				if (newContainerPath.isValidPath(dudFileContainerText.getText())) {
					newContainerPath = new Path(dudFileContainerText.getText());
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
							dudFileListCombo.setItems(dudFileList);
							dudFileListCombo.setText(dudFileList[0]);
						} else {
							dudFileListCombo.removeAll();
							dudFileListCombo.setText("");
							selectedFile = dudFileListCombo.getText();
						}
					} else {
						dudFileListCombo.removeAll();
						dudFileListCombo.setText("");
						selectedFile = dudFileListCombo.getText();
					}
				}
			}
		});

		workspaceFileSystemComp = new Composite(containerGroup, SWT.NONE);
		workspaceFileSystemComp.setLayoutData(new GridData(SWT.RIGHT,
				SWT.CENTER, true, true, 2, 1));
		workspaceFileSystemComp.setLayout(new GridLayout(1, false));
		workspaceButton = new Button(workspaceFileSystemComp, SWT.PUSH);
		workspaceButton.setText("WorkSpace...");

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
					if (dialog.open() == Window.OK) {
						IContainer newContainer;
						IPath newContainerPath = workspacePath
								.append(((IPath) dialog.getResult()[0]));
						newContainer = ResourcesPlugin.getWorkspace().getRoot()
								.getContainerForLocation(newContainerPath);
						if (!oldContainerPath.equals(newContainerPath)) {
							dudFileContainerText.setText(newContainerPath
									.toOSString());
							ArrayList<IFile> dudFiles = new ArrayList<IFile>();
							findAllDudFiles(newContainer, dudFiles);
							ArrayList<String> items = new ArrayList<String>();
							for (int i = 0; i < dudFiles.size(); ++i)
								items.add(dudFiles.get(i).getFullPath()
										.toOSString());
							if (items.size() > 0) {
								String[] dudFileList = items
										.toArray(new String[items.size()]);
								dudFileListCombo.setItems(dudFileList);
								dudFileListCombo.setText(dudFileList[0]);
								selectedFile = dudFileListCombo.getText();
							} else {
								dudFileListCombo.removeAll();
								dudFileListCombo.setText("");
								selectedFile = dudFileListCombo.getText();
							}
							updateLaunchConfigurationDialog();
						}
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		programToRunGroup = new Group(comp, SWT.NONE);
		programToRunGroup.setText("DUD file to Run");
		programToRunGroup.setLayout(new GridLayout(1, false));
		programToRunGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
				true));

		comboComposite = new Composite(programToRunGroup, SWT.NONE);
		comboComposite.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
				true, true));
		comboComposite.setLayout(new GridLayout(2, false));

		dudFileComboLabel = new Label(comboComposite, SWT.NONE);
		dudFileComboLabel.setText("Dud file to Run:");
		dudFileListCombo = new Combo(comboComposite, SWT.DROP_DOWN
				| SWT.READ_ONLY);

		ArrayList<IFile> dudFiles = new ArrayList<IFile>();
		findAllDudFiles(ResourcesPlugin.getWorkspace().getRoot(), dudFiles);
		ArrayList<String> items = new ArrayList<String>();
		for (int i = 0; i < dudFiles.size(); ++i)
			items.add(dudFiles.get(i).getFullPath().toOSString());
		String[] dudFileList = items.toArray(new String[items.size()]);
		dudFileListCombo.setItems(dudFileList);
		dudFileListCombo.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER,
				true, true, 1, 1));

		dudFileListCombo.setEnabled(true);
		if (dudFileListCombo.getItems().length >= 1) {
			dudFileListCombo.select(dudFileListCombo.indexOf(dudFileListCombo
					.getItems()[0]));
			selectedFile = dudFileListCombo.getText();

		} else
			dudFileListCombo.setEnabled(false);

		dudFileListCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!selectedFile.equals(dudFileListCombo.getText()))
					updateLaunchConfigurationDialog();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});

		radioComposite = new Composite(programToRunGroup, SWT.NONE);
		radioComposite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				true));
		radioComposite.setLayout(new GridLayout(2, false));

		externalBrowserButton = new Button(radioComposite, SWT.RADIO);
		externalBrowserButton.setText("Run in external browser");
		externalBrowserButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				true, true));
		externalBrowserButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (externState != externalBrowserButton.getSelection()) {
					updateLaunchConfigurationDialog();
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		designModeButton = new Button(radioComposite, SWT.RADIO);
		designModeButton.setText("Run in design mode");

		designModeButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (designState != designModeButton.getSelection()) {
					updateLaunchConfigurationDialog();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		designModeButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
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
			dudFileContainerText.setText(configuration.getAttribute(
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
					if (cont != null && cont.exists()) {
						findAllDudFiles(cont, dudFiles);
						ArrayList<String> items = new ArrayList<String>();
						for (int i = 0; i < dudFiles.size(); ++i)
							items.add(dudFiles.get(i).getFullPath()
									.toOSString());
						String[] dudFileList = items.toArray(new String[items
								.size()]);
						dudFileListCombo.setItems(dudFileList);
						dudFileListCombo.setText(dudFilePath.toOSString());
					}
				}

			});
		} catch (CoreException e) {
			e.printStackTrace();
		}

		try {
			designModeButton.setSelection(configuration.getAttribute(
					"isRunningDesign", false));
			designState = configuration.getAttribute("isRunningDesign", false);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		try {
			externalBrowserButton.setSelection(configuration.getAttribute(
					"isRunningExtern", true));
			externState = configuration.getAttribute("isRunningExtern", true);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute("filePath", dudFileListCombo.getText());
		configuration.setAttribute("isRunningDesign",
				designModeButton.getSelection());
		configuration.setAttribute("isRunningExtern",
				externalBrowserButton.getSelection());
		configuration.setAttribute("ContainerLocation",
				dudFileContainerText.getText());
		ArrayList<String> temp = new ArrayList<String>();
		String[] itemList = dudFileListCombo.getItems();
		for (int i = 0; i < itemList.length; ++i)
			temp.add(itemList[i]);
		configuration.setAttribute("dudFileList", temp);
		selectedFile = dudFileListCombo.getText();
		externState = externalBrowserButton.getSelection();
		designState = designModeButton.getSelection();
		containerPath = dudFileContainerText.getText();
	}

	@Override
	public String getName() {
		return "Main";
	}

	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		if (selectedFile.equals(""))
			return false;
		else
			return true;
	}

}
