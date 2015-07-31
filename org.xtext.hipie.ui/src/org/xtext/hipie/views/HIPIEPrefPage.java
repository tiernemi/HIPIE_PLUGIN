package org.xtext.hipie.views ;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.xtext.ui.editor.preferences.LanguageRootPreferencePage;
import org.xtext.hipie.ui.internal.HIPIEActivator;

public class HIPIEPrefPage extends LanguageRootPreferencePage {
    
	private FileFieldEditor compilerLocation ;
	
	@Override
    protected void createFieldEditors() {
		
		compilerLocation = new FileFieldEditor("Compiler Location", "&Compiler Location:", getFieldEditorParent()) ;
		String[] exten = {"*.jar"} ;
		compilerLocation.setFileExtensions(exten);
        addField(compilerLocation);       
    }
	
    @Override
    public void init(IWorkbench workbench) {        
    	setPreferenceStore(HIPIEActivator.getInstance().getPreferenceStore());
    	
    }
}