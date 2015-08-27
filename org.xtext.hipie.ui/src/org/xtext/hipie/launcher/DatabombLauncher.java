package org.xtext.hipie.launcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.statushandlers.StatusManager;
import org.osgi.service.prefs.Preferences;
import org.xtext.hipie.error.HIPIEStatus;


public class DatabombLauncher implements ILaunchShortcut {

	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof TreeSelection) {
			Object[] selectionArray = ((TreeSelection) selection).toArray();
			for (int i = 0; i < selectionArray.length; ++i)
				if (selectionArray[i] instanceof IFile) {
					IFile datFile = (IFile) selectionArray[i];
					compileDatabomb(datFile);
				}
		}

	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		IFile datFile = (IFile) editor.getEditorInput().getAdapter(IFile.class);
		compileDatabomb(datFile);
	}

	public static void compileDatabomb(final IFile runFile) {
		Preferences workPrefs = InstanceScope.INSTANCE
				.getNode("org.xtext.hipie.ui");
		Path compilerPath = new Path(workPrefs.get("Compiler Location", ""));
		String fileName = runFile.getName();
		IScopeContext projectScope = new ProjectScope(runFile.getProject());
		Preferences preferences = projectScope.getNode("org.xtext.hipie.ui");
		Preferences dataPrefNode = preferences.node("data_prefs");
		String dataCmdArgs = dataPrefNode.get("cmd_prefs" + fileName, "");
		String dataCmdString = "-csv " + runFile.getRawLocation().toOSString()
				+ " " + dataCmdArgs;
		IPath dataBombFilePath = runFile.getRawLocation().removeFileExtension()
				.addFileExtension("databomb");

		String databombCmd = "java -cp " + compilerPath.toOSString()
				+ " org/hpcc/HIPIE/commandline/CommandLineService "
				+ dataCmdString + " -o " + dataBombFilePath.toOSString();
		System.out.println("Compiling Databomb......");
		System.out.println(databombCmd);
		Process procData = null;
		try {
			procData = Runtime.getRuntime().exec(databombCmd);
			InputStream in = procData.getInputStream();
			InputStream er = procData.getErrorStream();
			Scanner scVerbose = new Scanner(in);
			Scanner scError = new Scanner(er);
			String streamString = new String();
			String streamStringErrors = new String();
			if (scVerbose.hasNext())
				streamString = scVerbose.useDelimiter("\\Z").next();
			if (scError.hasNext())
				streamStringErrors = scError.useDelimiter("\\Z").next();
			System.out.println(streamString);
			System.out.println(streamStringErrors);
			if (!streamStringErrors.isEmpty()) {
				String message = streamStringErrors;
				IStatus status = new HIPIEStatus(IStatus.ERROR,
						"org.xtext.hipie.ui",
						HIPIEStatus.FAILED_TO_COMPILE_DATABOMB, message, null);
				StatusManager.getManager().handle(status,
						StatusManager.LOG | StatusManager.SHOW);
			}

			in.close();
			er.close();
			scVerbose.close();
			scError.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Job job = new Job("Refresh") {
			protected IStatus run(IProgressMonitor monitor) {
				try {
					runFile.getProject().refreshLocal(IResource.DEPTH_INFINITE,
							monitor);
				} catch (CoreException e) {
					e.printStackTrace();
				}
				return Status.OK_STATUS;
			}
		};
		job.setPriority(Job.SHORT);
		job.schedule();
	}
}
