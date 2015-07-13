package org.xtext.hipie.ui;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;

public class HIPIECompilerPreferences {
	
	public static String compiler_directory = "comp_directory" ;
	public static int bob = 2 ;
	  
	public static String getGreetingText() {
	    return getPreferenceStore().get(compiler_directory, "");
	  }
	public static IEclipsePreferences getPreferenceStore() {
	    return InstanceScope.INSTANCE.getNode("org.xtext.hipie");
	   }
	
}
