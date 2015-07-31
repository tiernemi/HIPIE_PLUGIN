package org.xtext.hipie.views;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;


public class DesignModeComposite extends Composite {

	// Fields passed to the design mode browser in order to initialize design mode browser. //
	private String persist ;
	private String ddl ;
	private String databomb ;
	private String perFilepath ;

	// Composite elements. //
	private Browser designModeBrowser ;
	private Button syncButton ;
	private String dermaHtmlUrl = "file:///home/michael/Downloads/derm.html" ; 
	
	
	public DesignModeComposite(final Composite parent, boolean showAddressBar) {
		super(parent, SWT.NONE);

		// Gets the information for the open design mode view from the previous instance of eclipse. //
		final IEclipsePreferences workPrefs = InstanceScope.INSTANCE.getNode("org.xtext.hipie.ui");
		ddl = workPrefs.get("ddl_design_str" , "") ;
		databomb = workPrefs.get("databomb_design_str" , "") ;
		persist = workPrefs.get("persist_design_str" , "") ;
		perFilepath = workPrefs.get("per_filepath_design_str" , "") ;
		
		parent.setLayout(new GridLayout(2, false));
		designModeBrowser = new Browser(parent, SWT.NONE);
		designModeBrowser.setUrl(dermaHtmlUrl);
		designModeBrowser.setJavascriptEnabled(true);
		designModeBrowser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));		
		
		// Initializes the design mode browser based on ddl,databomb and persist strings. //
		designModeBrowser.addProgressListener(new ProgressAdapter() {
			public void completed(ProgressEvent event) {
					String ddlCmd = "setDdl(" + ddl + ");" ;
					String databombCmd = "setDatabomb(" + databomb + ");" ;
					String persistCmd = "setPersist(" + persist + ");" ;
					String updateCmd = "updateWidget();" ;
					System.out.println(designModeBrowser.execute(ddlCmd)) ;
					System.out.println(designModeBrowser.execute(databombCmd)) ;
					System.out.println(designModeBrowser.execute(persistCmd)) ;
					System.out.println(designModeBrowser.execute(updateCmd)) ;
					System.out.println(designModeBrowser.evaluate("return " + databombCmd)) ;
					System.out.println(designModeBrowser.evaluate("return " + ddlCmd)) ;
					workPrefs.put("ddl_design_str", ddl);
					workPrefs.put("databomb_design_str", databomb);
					workPrefs.put("persist_design_str", persist);
					workPrefs.put("per_filepath_design_str", perFilepath);
				}
			});
		
		syncButton = new Button(parent, SWT.PUSH) ;
		syncButton.setText("Sync") ;
		syncButton.addSelectionListener(new SelectionAdapter() {		
			
			// Regenerates the html file from the ddl,databomb and persist strings. //
			@Override
			public void widgetSelected(SelectionEvent e) {
				String get_persist_cmd = "return getPersist();" ;
				persist = "test"; //(String) designModeBrowser.evaluate(get_persist_cmd) ;
				System.out.println(perFilepath);
				
				try {
					FileWriter fw = new FileWriter(perFilepath );
					fw.write(persist);
					fw.close() ;
					IPath persist_path = new Path(perFilepath) ;
					IPath html_path = persist_path.removeFileExtension().addFileExtension("html") ;
					IFile html_file = ResourcesPlugin.getWorkspace().getRoot().getFile(html_path) ;
					URL url = new URL("platform:/plugin/org.xtext.hipie/vis_files/marsh.html") ;	
					InputStream in_stream = url.openConnection().getInputStream() ;
					String streamString_html = "" ;
					Scanner sc_in = new Scanner(in_stream) ;
					if (sc_in.hasNext())
						streamString_html = sc_in.useDelimiter("\\Z").next() ;
					streamString_html = streamString_html.replace("%_data_%" , databomb) ;
					streamString_html = streamString_html.replace("%_ddl_%" , ddl) ;
					streamString_html = streamString_html.replace("%_persist_%" , persist) ;
					in_stream.close();
					FileWriter html_out = new FileWriter(html_file.getFullPath().toOSString()) ;
					html_out.write(streamString_html);
					html_out.close();
					sc_in.close() ;
				}
				catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try {
					ResourcesPlugin.getWorkspace().getRoot().refreshLocal
						(IResource.DEPTH_INFINITE, new NullProgressMonitor()) ;
				} catch (CoreException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
	
	public void setStrings(String dd, String db, String per, String per_fp)
	{
		ddl = dd ;
		databomb = db ;
		persist = per ;
		perFilepath = per_fp ;
		designModeBrowser.refresh(); 
	}
	
	public void setPersist(String per)
	{
		persist =  per ;
	}
}
