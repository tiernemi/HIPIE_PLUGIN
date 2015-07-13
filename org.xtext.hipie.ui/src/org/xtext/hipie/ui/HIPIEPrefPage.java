package org.xtext.hipie.ui ;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.xtext.ui.editor.preferences.LanguageRootPreferencePage;
import org.xtext.hipie.ui.internal.HIPIEActivator;

public class HIPIEPrefPage extends LanguageRootPreferencePage {
    @Override
    protected void createFieldEditors() {
        addField(new DirectoryFieldEditor("Compiler Location", "&Compiler Location:", getFieldEditorParent()));
    }
    @Override
    public void init(IWorkbench workbench) {        
    	setPreferenceStore(HIPIEActivator.getInstance().getPreferenceStore());
    }
}