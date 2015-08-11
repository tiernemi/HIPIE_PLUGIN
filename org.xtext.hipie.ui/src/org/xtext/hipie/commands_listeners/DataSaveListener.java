package org.xtext.hipie.commands_listeners;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.osgi.service.prefs.Preferences;
import org.xtext.hipie.ui.DataPropertyPage;
import org.xtext.hipie.ui.HIPIENature;

public class DataSaveListener {
	
	static public IResourceChangeListener dataSaveListener = new IResourceChangeListener() {		
				
		public void promptDialog(final IFile changedFile) {
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
						MessageBox dialog = 
								new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
					dialog.setText("Databomb");
					dialog.setMessage("Do you want to enable databomb compilation for this file?");
					int answer = dialog.open() ;
					if (answer == SWT.OK)
						PreferencesUtil.createPropertyDialogOn(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),  changedFile, 
								DataPropertyPage.ID , null, null).open() ;
					}
				}) ;
				
			IScopeContext projectScope = new ProjectScope(changedFile.getProject()) ;
			Preferences preferences = projectScope.getNode("org.xtext.hipie.ui");
			Preferences dataPrefNode = preferences.node("data_prefs");
			String dataCompileState = dataPrefNode.get("comp_state" + changedFile.getName(), "false") ;
			if (dataCompileState.equals("true"))
				compileDatabomb(changedFile) ;
		}
		
		public void compileDatabomb(final IFile changedFile) {
			Preferences workPrefs = InstanceScope.INSTANCE.getNode("org.xtext.hipie.ui");
			Path compilerPath = new Path(workPrefs.get("Compiler Location" , ""))	;					
			String fileName = changedFile.getName() ;
			IScopeContext projectScope = new ProjectScope(changedFile.getProject()) ;
			Preferences preferences = projectScope.getNode("org.xtext.hipie.ui");
			Preferences dataPrefNode = preferences.node("data_prefs");
			String dataCmdArgs = dataPrefNode.get("cmd_prefs" + fileName , "") ;
			String dataCmdString = "-csv " + changedFile.getRawLocation().toOSString() + " " + dataCmdArgs ;
			IPath dataBombFilePath = changedFile.getRawLocation().removeFileExtension().addFileExtension("databomb") ;
			
			String databombCmd = "java -cp " + compilerPath.toOSString() + " org/hpcc/HIPIE/commandline/CommandLineService "
				+ dataCmdString + " -o " + dataBombFilePath.toOSString() ;
			System.out.println(databombCmd) ;
			Process procData = null;
			try {
				procData = Runtime.getRuntime().exec(databombCmd);
				InputStream in = procData.getInputStream() ; 
				InputStream er = procData.getErrorStream() ;
				Scanner scVerbose = new Scanner(in) ;
				Scanner scError = new Scanner(er) ;
				String streamString = new String() ;
				String streamStringErrors = new String() ;
				if (scVerbose.hasNext())
					streamString = scVerbose.useDelimiter("\\Z").next() ;
				if (scError.hasNext())
					streamStringErrors = scError.useDelimiter("\\Z").next() ;
				System.out.println(streamString) ;
				System.out.println(streamStringErrors) ;
				in.close() ;
				er.close() ;
				scVerbose.close() ;
				scError.close() ;
			} catch (IOException e) {
				e.printStackTrace();
			}
			Job job = new Job("Refresh") {
				protected IStatus run(IProgressMonitor monitor) {
					try {
						changedFile.getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
					} catch (CoreException e) {
						e.printStackTrace();
					}
					return Status.OK_STATUS;
		        }
		     };
		     job.setPriority(Job.SHORT);
		     job.schedule(); 
		} 

		public void resourceChanged(IResourceChangeEvent event) {	
						
			if (event.getDelta() != null) {
				final ArrayList<IResource> changed = new ArrayList<IResource>();
				IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() {
					public boolean visit(IResourceDelta delta) {
						//only interested in changed resources (not added or removed)
						if (delta.getKind() != IResourceDelta.CHANGED)
							return true;
						//only interested in content changes
						if ((delta.getFlags() & IResourceDelta.CONTENT) == 0)
							return true;
						IResource resource = delta.getResource();
						//only interested in files with the "txt" extension
			             if (resource.getType() == IResource.FILE && 
			       			!"databomb".equalsIgnoreCase(resource.getFileExtension()) && 
			       			!"dud".equalsIgnoreCase(resource.getFileExtension()) && 
					       	!"ddl".equalsIgnoreCase(resource.getFileExtension()) && 
					       	!"persist".equalsIgnoreCase(resource.getFileExtension()) && 
			       			!"html".equalsIgnoreCase(resource.getFileExtension()))
							changed.add(resource);
						return true;
					}
				};
	         
				try {
					event.getDelta().accept(visitor);
				} catch (CoreException e) {
					e.printStackTrace();
				}
	      	
				if (changed.size() > 0)
					if (changed.get(0) instanceof IFile) {
						IFile changedFile = (IFile) changed.get(0) ;
						final IProject contProject = changedFile.getProject() ;
						try {
							if (contProject.hasNature(HIPIENature.ID)) {
								String fileName = changedFile.getName() ;
								IScopeContext projectScope = new ProjectScope(contProject) ;
								Preferences preferences = projectScope.getNode("org.xtext.hipie.ui");
								Preferences dataPrefNode = preferences.node("data_prefs");
								String dataCompileState = dataPrefNode.get("comp_state" + fileName, "false") ;
								if (dataCompileState.equals("false"))
									promptDialog(changedFile) ;
								else {	
									compileDatabomb(changedFile) ;
								} 
							}
						} catch (CoreException e) {
							e.printStackTrace();
					}
				}
			}
		}
	};
	
	static public void init() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(dataSaveListener) ;
	}
	
	static public void remove() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(dataSaveListener);
	}
}
