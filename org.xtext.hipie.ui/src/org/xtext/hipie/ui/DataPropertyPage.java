package org.xtext.hipie.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

public class DataPropertyPage extends PropertyPage implements
		IWorkbenchPropertyPage {

	public DataPropertyPage() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createContents(Composite parent) {
		 setTitle("hello");
		Button button = new Button(parent, SWT.CHECK);
		button.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
				false));
		button.setText("Compile To Databomb");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		return parent;
		
	}

}
