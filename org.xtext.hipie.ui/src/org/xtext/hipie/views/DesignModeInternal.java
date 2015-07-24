package org.xtext.hipie.views;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.eclipse.core.internal.content.Activator;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;

public class DesignModeInternal extends Composite {

	public DesignModeInternal(final Composite parent, boolean showAddressBar, String ddl , String databomb, String persist) {
		super(parent, SWT.NONE);

		try {
			parent.setLayout(new GridLayout(2, false));
			final Browser browser = new Browser(parent, SWT.NONE);
			URL derma_html = new URL("platform:/plugin/org.xtext.hipie/vis_files/marsh.html") ;
			String url = "" ;
			try {
			url =	FileLocator.toFileURL(derma_html).getPath() ;
			url = "file://".concat(url) ;
			
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(url) ;
			browser.setUrl("file:///home/michael/Desktop/LexisNexis/runtime-New_configuration/proj_2/src/test.html");
		
			String ddl_func = "setDdl(" + ddl + ");" ;
			String databomb_func = "setDatabomb(" + databomb + ");" ;
			String persist_func = "setPersist(" + persist + ");" ;
			browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
			Button addMarker = new Button(parent, SWT.PUSH);
			addMarker.setText("GWGG");
        	addMarker.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                browser.evaluate("showProperties(true);");   
            }
        	});
        
		Button NoShowProperties = new Button(parent, SWT.PUSH);
        NoShowProperties.setText("trss");
        NoShowProperties.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	 browser.evaluate("showProperties(false);");
               
            }
        });
        
        
        
		Button Refresh = new Button(parent, SWT.PUSH);
        Refresh.setText("Refresh The Page ");
        Refresh.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                browser.refresh();
               
            }
        });
        
		}
		catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
