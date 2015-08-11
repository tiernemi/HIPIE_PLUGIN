package org.xtext.hipie.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class DataPropertyPage extends PropertyPage implements
		IWorkbenchPropertyPage {

	public static String ID = "org.xtext.hipie.databombPage" ;
	
	private Button compileCheckBox ;
	private Text cmdArgsTextBox ;
	private IFile datFile ;
	private Preferences dataPrefs ;
	
	public DataPropertyPage() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createContents(Composite parent) {
		setTitle("Databomb Compilation");
		
		datFile = (IFile) getElement().getAdapter(IFile.class) ;
		final String fileName = datFile.getName() ;
		IScopeContext projectScope = new ProjectScope(datFile.getProject()) ;
		Preferences preferences = projectScope.getNode("org.xtext.hipie.ui");
		dataPrefs = preferences.node("data_prefs");
		String dataCompileState = dataPrefs.get("comp_state" + fileName, "false") ;
		String dataCmdArgs = dataPrefs.get("cmd_prefs" + fileName, "") ;
		
		compileCheckBox = new Button(parent, SWT.CHECK);
		compileCheckBox.setText("Compile To Databomb");
		if (dataCompileState.equals("true"))
			compileCheckBox.setSelection(true);
		else
			compileCheckBox.setSelection(false);	
		
		
		Composite labelComposite = new Composite(parent, SWT.NONE) ;
		labelComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
		GridLayout labelLayout = new GridLayout() ;
		labelLayout.numColumns = 2 ;
		labelComposite.setLayout(labelLayout);
		
		
        Label label = new Label(labelComposite, SWT.NONE);
        GridData textData = new GridData(SWT.FILL, SWT.CENTER, true, false) ;
		label.setText("Command Line Arguments : ");
        cmdArgsTextBox = new Text(labelComposite ,SWT.SINGLE | SWT.LEAD | SWT.BORDER) ;
        cmdArgsTextBox.setLayoutData(textData);
        cmdArgsTextBox.setText(dataCmdArgs);
        
		return parent;	
	}

	@Override
	protected void performApply() {
		if (compileCheckBox.getSelection())
			dataPrefs.put("comp_state" + datFile.getName(), "true") ;
		else
			dataPrefs.put("comp_state" + datFile.getName(), "false") ;
		dataPrefs.put("cmd_prefs" + datFile.getName(), cmdArgsTextBox.getText()) ;
		try {
			dataPrefs.parent().flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void performDefaults() {
		cmdArgsTextBox.setText("");
		compileCheckBox.setSelection(false);
	}
}
