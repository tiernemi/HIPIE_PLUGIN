package org.xtext.hipie.views ;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.xtext.ui.editor.preferences.LanguageRootPreferencePage;
import org.xtext.hipie.ui.internal.HIPIEActivator;

public class HIPIEPrefPage extends LanguageRootPreferencePage {
    
	private FileFieldEditor compiler_location ;
	private PathEditor data_file_paths ;
	
	@Override
    protected void createFieldEditors() {
		
		compiler_location = new FileFieldEditor("Compiler Location", "&Compiler Location:", getFieldEditorParent()) ;
		String[] exten = {"*.jar"} ;
		compiler_location.setFileExtensions(exten);
        addField(compiler_location);
        
    //    data_file_paths = new PathEditor("Data Files", "&Data Filepaths:", "hahha",  getFieldEditorParent()) ;
    //    data_file_paths.fillIntoGrid(getFieldEditorParent(), 3);
     //   addField(data_file_paths) ;
       
    }
    @Override
    public void init(IWorkbench workbench) {        
    	setPreferenceStore(HIPIEActivator.getInstance().getPreferenceStore());
    	
    }
}