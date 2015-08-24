package org.xtext.hipie.propertyTesters;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.ui.part.FileEditorInput;
import org.osgi.service.prefs.Preferences;

public class DatabombPropTester extends PropertyTester {

	public static final String[] VALID_DATABOMB_FILES = { "txt", "csv", "tsv",
			"dat"};
	public static final String PROPERTY_NAMESPACE = "org.xtext.hipie.ui";
	public static final String PROPERTY_IS_COMPILATION_ENABLED = "isCompilationEnabled";
	public static final String PROPERTY_IS_VALID_FILE = "isValidFile";
	public static final String PROPERTY_HAS_BEEN_PROMPTED = "hasBeenPrompted";

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		return testFile(receiver, property);
	}

	// Programmatically testing without needing to declare an instance of the
	// property tester //

	public static boolean testFile(Object receiver, String property) {
		IFile testFile = null;
		if (receiver instanceof IFile)
			testFile = (IFile) receiver;
		if (receiver instanceof FileEditorInput)
			testFile = (IFile) ((FileEditorInput) receiver).getAdapter(IFile.class) ;
		if (testFile != null && testFile.exists()) {
			if (property.equals(PROPERTY_IS_COMPILATION_ENABLED)) {
				IScopeContext projectScope = new ProjectScope(
						testFile.getProject());
				Preferences preferences = projectScope
						.getNode("org.xtext.hipie.ui");
				Preferences dataPrefNode = preferences.node("data_prefs");
				String dataCompileState = dataPrefNode.get("comp_state"
						+ testFile.getName(), "false");
				if (dataCompileState.equals("true"))
					return true;
				else
					return false;
			} else if (property.equals(PROPERTY_IS_VALID_FILE)) {
				for (int i = 0; i < VALID_DATABOMB_FILES.length; ++i)
					if (testFile.getFileExtension().equals(
							VALID_DATABOMB_FILES[i]))
						return true;
				return false;
			} else if (property.equals(PROPERTY_HAS_BEEN_PROMPTED)) {
				IScopeContext projectScope = new ProjectScope(
						testFile.getProject());
				Preferences preferences = projectScope
						.getNode("org.xtext.hipie.ui");
				Preferences dataPrefNode = preferences.node("data_prefs");
				String dataPromptedState = dataPrefNode.get("cmd_visited"
						+ testFile.getName(), "false");
				if (dataPromptedState.equals("true"))
					return true;
				else
					return false;
			}
		}
		return false;
	}
}
