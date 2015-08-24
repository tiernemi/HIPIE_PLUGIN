package org.xtext.hipie.commands_listeners;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.xtext.hipie.launcher.DatabombLauncher;
import org.xtext.hipie.propertyTesters.DatabombPropTester;
import org.xtext.hipie.ui.DataPropertyPage;
import org.xtext.hipie.ui.HIPIENature;

public class DataSaveListener {

	static public IResourceChangeListener dataSaveListener = new IResourceChangeListener() {

		public void resourceChanged(IResourceChangeEvent event) {

			if (event.getDelta() != null) {
				final ArrayList<IResource> changed = new ArrayList<IResource>();
				IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() {
					public boolean visit(IResourceDelta delta) {
						// only interested in changed resources (not added or
						// removed)
						if (delta.getKind() != IResourceDelta.CHANGED)
							return true;
						// only interested in content changes
						if ((delta.getFlags() & IResourceDelta.CONTENT) == 0)
							return true;
						IResource resource = delta.getResource();
						if (resource.getType() == IResource.FILE
								&& DatabombPropTester
										.testFile(
												resource,
												DatabombPropTester.PROPERTY_IS_VALID_FILE))
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
						IFile changedFile = (IFile) changed.get(0);
						final IProject contProject = changedFile.getProject();
						try {
							if (contProject.hasNature(HIPIENature.ID)) {
								if (DatabombPropTester
										.testFile(
												changedFile,
												DatabombPropTester.PROPERTY_IS_COMPILATION_ENABLED))
									DatabombLauncher
											.compileDatabomb(changedFile);
							}
						} catch (CoreException e) {
							e.printStackTrace();
						}
					}
			}
		}
	};

	static public void init() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				dataSaveListener);
	}

	static public void remove() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(
				dataSaveListener);
	}
}
