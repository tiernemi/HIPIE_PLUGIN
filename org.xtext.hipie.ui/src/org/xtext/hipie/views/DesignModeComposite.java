package org.xtext.hipie.views;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.eclipse.core.internal.content.Activator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
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
	private String per_filepath ;

	private Browser design_mode_browser ;
	private Button sync_button ;
	private String derma_html_url = "file:///home/michael/Downloads/derm_clean.html" ; 
	
	
	public DesignModeComposite(final Composite parent, boolean showAddressBar) {
		super(parent, SWT.NONE);

		// Gets the information for the open design mode view from the previous instance of eclipse 
		
		final IEclipsePreferences work_prefs = InstanceScope.INSTANCE.getNode("org.xtext.hipie.ui");
		ddl = work_prefs.get("ddl_design_str" , "") ;
		databomb = work_prefs.get("databomb_design_str" , "") ;
		persist = work_prefs.get("persist_design_str" , "") ;
		per_filepath = work_prefs.get("per_filepath_design_str" , "") ;
		
		parent.setLayout(new GridLayout(2, false));
		design_mode_browser = new Browser(parent, SWT.NONE);
		design_mode_browser.setUrl(derma_html_url);
		design_mode_browser.setJavascriptEnabled(true);
		design_mode_browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		design_mode_browser.addProgressListener(new ProgressAdapter() {
			public void changed(ProgressEvent event)
			{
					String ddl_cmd = "setDdl(" + ddl + ");" ;
					String databomb_cmd = "setDatabomb(" + databomb + ");" ;
					String persist_cmd = "setPersist(" + persist + ");" ;
					String update_cmd = "updateWidget();" ;
					design_mode_browser.execute(ddl_cmd) ;
					design_mode_browser.execute(databomb_cmd) ;
					design_mode_browser.execute(persist_cmd) ;
					design_mode_browser.execute(update_cmd) ;
					work_prefs.put("ddl_design_str", ddl);
					work_prefs.put("databomb_design_str", databomb);
					work_prefs.put("persist_design_str", persist);
					work_prefs.put("per_filepath_design_str", per_filepath);
				}
			});
			
		Button sync_button = new Button(parent, SWT.PUSH) ;
		sync_button.setText("Sync") ;
		sync_button.addSelectionListener(new SelectionAdapter() {
				
			@Override
			public void widgetSelected(SelectionEvent e) {
				String get_persist_cmd = "return getPersist();" ;
				persist = "ahahhaaha" ; //(String) design_mode_browser.evaluate(get_persist_cmd) ;
				System.out.println(per_filepath);
				try {
					FileWriter fw = new FileWriter(per_filepath );
					fw.write(persist);
					fw.close() ;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try {
					ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor()) ;
				} catch (CoreException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
				
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
					
			}
		});
	}
	
	public void setStrings(String dd, String db, String per, String per_fp)
	{
		ddl = dd ;
		databomb = db ;
		persist = per ;
		per_filepath = per_fp ;
		design_mode_browser.refresh(); 
	}
	
	public void setPersist(String per)
	{
		persist =  per ;
	}
}
