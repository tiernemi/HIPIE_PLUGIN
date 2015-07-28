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
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
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

public class DesignModeComposite extends Composite {

	private String persist ;
	private String ddl ;
	private String databomb ;

	private Browser design_mode_browser ;
	private String derma_html_url = "file:///home/michael/Downloads/derm_page.html" ; 
	
	
	public DesignModeComposite(final Composite parent, boolean showAddressBar) {
		super(parent, SWT.NONE);

			parent.setLayout(new GridLayout(2, false));
			design_mode_browser = new Browser(parent, SWT.NONE);
			design_mode_browser.setUrl(derma_html_url);
			design_mode_browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			String dd_cmd = "setStrings(" + ddl +"," + databomb + "," + persist + ");" ;
			
			design_mode_browser.addProgressListener(new ProgressAdapter() {
				public void changed(ProgressEvent event)
				{
					if (ddl != "" && databomb != "")
					{
						String dd_cmd = "setStrings(" + ddl +"," + databomb + "," + persist + ");" ;
						design_mode_browser.execute(dd_cmd) ;
					}
				}
			});
	}
	
	public void setStrings(String dd, String db, String per)
	{
		ddl = dd ;
		databomb = db ;
		persist = per ;
		design_mode_browser.refresh(); 
	}
}
