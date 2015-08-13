package org.xtext.hipie.views;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;


public class DesignModeComposite extends Composite {

	
	
	private Composite buttonComposite;
	
	private String htmlFilepath ;

	// Composite elements. //
	private Browser designModeBrowser ;
	private Button syncButton ;
	private Button designModeButton;
	private URL dermaHtmlUrl ; 

	public DesignModeComposite(final Composite parent, boolean showAddressBar) {
		super(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns=1;
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.marginTop=-65;
		parent.setLayout(gridLayout);

		// Gets the information for the open design mode view from the previous instance of eclipse. //
		final IEclipsePreferences workPrefs = InstanceScope.INSTANCE.getNode("org.xtext.hipie.ui");

		buttonComposite = new Composite(parent, SWT.NONE) ;
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginTop=0;
		rowLayout.spacing=2;
		buttonComposite.setLayout(rowLayout);


		
		designModeButton = new Button(buttonComposite, SWT.PUSH | SWT.LEFT);
        designModeButton.setText("Design Mode");
        designModeButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	designModeBrowser.execute("DesignMode();");
            }
        });
     
				
		syncButton = new Button(buttonComposite, SWT.PUSH | SWT.LEFT ) ;
		syncButton.setText("Sync") ;
		syncButton.addSelectionListener(new SelectionAdapter() {		
			
			// Regenerates the html file from the ddl,databomb and persist strings. //
			@Override
			public void widgetSelected(SelectionEvent e) {
				String getPersistCmd = "return getPersist();" ;
				String getDdlCmd = "return getDdl();" ;
				String getDatabombCmd = "return getDatabomb();" ;

				try {
					String ddl = (String) designModeBrowser.evaluate(getDdlCmd) ;
					String databomb = (String) designModeBrowser.evaluate(getDatabombCmd) ;
					String persist = (String) designModeBrowser.evaluate(getPersistCmd) ;

					IPath htmlPath = new Path(dermaHtmlUrl.getPath()) ;
					IFile htmlFile = ResourcesPlugin.getWorkspace().getRoot().getFile(htmlPath) ;
					
					IPath persistPath = htmlPath.removeFileExtension().addFileExtension("persist") ;
					FileWriter fw = new FileWriter(persistPath.toOSString());
					fw.write(persist);
					fw.close() ;
					
					URL url = new URL("platform:/plugin/org.xtext.hipie/vis_files/dermatology.html") ;	
					InputStream inStream = url.openConnection().getInputStream() ;
					String streamStringHtml = "" ;
					Scanner scIn = new Scanner(inStream) ;
					if (scIn.hasNext())
						streamStringHtml = scIn.useDelimiter("\\Z").next() ;
					streamStringHtml = streamStringHtml.replace("%_data_%" , databomb) ;
					streamStringHtml = streamStringHtml.replace("%_ddl_%" , ddl) ;
					streamStringHtml = streamStringHtml.replace("%_persist_%" , persist) ;
					inStream.close();
					FileWriter html_out = new FileWriter(htmlFile.getFullPath().toOSString()) ;
					html_out.write(streamStringHtml);
					html_out.close();
					scIn.close() ;
					
					workPrefs.put("html_filepath_design", dermaHtmlUrl.toString()); 
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
		
		
		String htmlFileUrl = workPrefs.get("html_filepath_design" , "") ;
		designModeBrowser = new Browser(parent, SWT.NONE);
		designModeBrowser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		if (!htmlFileUrl.equals(""))
			designModeBrowser.setUrl(htmlFileUrl) ;
		
		
	}

	public void setFilepath(IFile htmlfile) {
		if (htmlfile.exists()) {
			URL url;
			try {
				IEclipsePreferences workPrefs = InstanceScope.INSTANCE.getNode("org.xtext.hipie.ui");
				url = htmlfile.getRawLocationURI().toURL();
				dermaHtmlUrl = url ;
				designModeBrowser.setUrl(url.toString()) ;
				workPrefs.put("html_filepath_design", dermaHtmlUrl.toString()); 
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}
}
