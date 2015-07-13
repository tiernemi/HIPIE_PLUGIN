package org.xtext.hipie.ui;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.xtext.hipie.ui.internal.HIPIEActivator;

public class CompilerPreferenceInitialiser extends
		AbstractPreferenceInitializer {

	public CompilerPreferenceInitialiser() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initializeDefaultPreferences() {
    	String default_path = ResourcesPlugin.getWorkspace().getRoot().getRawLocation().toOSString() ;
		IPreferenceStore store = HIPIEActivator.getInstance().getPreferenceStore();
		    store.setDefault("Compiler Location", default_path);
	}

}
