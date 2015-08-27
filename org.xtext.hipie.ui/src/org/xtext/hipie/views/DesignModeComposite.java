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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;


public class DesignModeComposite extends Composite {

	private Composite buttonComposite;

	// Composite elements. //
	private Browser designModeBrowser ;
	private Button syncButton ;
	private Button designModeButton;
	private URL cleanHtmlUrl = null;

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
				
				IEclipsePreferences workPrefs = InstanceScope.INSTANCE.getNode("org.xtext.hipie.ui");

				try {
					String ddl = (String) designModeBrowser.evaluate(getDdlCmd) ;
					String databomb = (String) designModeBrowser.evaluate(getDatabombCmd) ;
					String persist = (String) designModeBrowser.evaluate(getPersistCmd) ;
					
					if (cleanHtmlUrl == null) {
						 cleanHtmlUrl = new URL(workPrefs.get("clean_html_url_design" , ""));
					}

					IPath htmlCleanPath = new Path(cleanHtmlUrl.getPath()) ;
					IFile htmlCleanFile = ResourcesPlugin.getWorkspace().getRoot().getFile(htmlCleanPath) ;
										
					IPath persistPath = htmlCleanPath.removeFileExtension().addFileExtension("persist") ;
					FileWriter fw = new FileWriter(persistPath.toOSString());
					fw.write(persist);
					fw.close() ;
									
					URL cleanUrl = new URL("platform:/plugin/org.xtext.hipie/vis_files/clean.html") ;	
					InputStream inStreamClean = cleanUrl.openConnection().getInputStream() ;
					String streamStringHtml = "" ;
					Scanner scIn = new Scanner(inStreamClean) ;
					if (scIn.hasNext())
						streamStringHtml = scIn.useDelimiter("\\Z").next() ;
					streamStringHtml = streamStringHtml.replace("%_data_%" , databomb) ;
					streamStringHtml = streamStringHtml.replace("%_ddl_%" , ddl) ;
					streamStringHtml = streamStringHtml.replace("%_persist_%" , persist) ;
					inStreamClean.close();
					FileWriter htmlOut = new FileWriter(htmlCleanFile.getFullPath().toOSString()) ;
					htmlOut.write(streamStringHtml);
					htmlOut.close();
					scIn.close() ;
					
					workPrefs.put("clean_html_url_design", cleanHtmlUrl.toString());
				}
				catch (IOException e1) {
					e1.printStackTrace();
				}
				
				try {
					ResourcesPlugin.getWorkspace().getRoot().refreshLocal
						(IResource.DEPTH_INFINITE, new NullProgressMonitor()) ;
				} catch (CoreException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		
		String cleanFilePath =  workPrefs.get("clean_html_filepath_design" , "") ;
		
		designModeBrowser = new Browser(parent, SWT.NONE) ;
		designModeBrowser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true)) ;
	    IFile htmlFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(cleanFilePath)) ;
		
		if (htmlFile!= null && htmlFile.exists()) {
			setFilepath(htmlFile) ;
		}
		
	}

	public void setFilepath(IFile htmlfile) {
		if (htmlfile.exists()) {
			URL cleanUrl ;
			URL dermUrl ;
			try {
				IEclipsePreferences workPrefs = InstanceScope.INSTANCE.getNode("org.xtext.hipie.ui") ;
				cleanUrl = htmlfile.getRawLocationURI().toURL() ;
				cleanHtmlUrl = cleanUrl ;
				
				
				IFile ddlFile = htmlfile.getProject().getFile(htmlfile.getProjectRelativePath().removeFileExtension().addFileExtension("ddl")) ; 
				IFile databombFile = htmlfile.getProject().getFile(htmlfile.getProjectRelativePath().removeFileExtension().addFileExtension("databomb")) ; 
				IFile persistFile = htmlfile.getProject().getFile(htmlfile.getProjectRelativePath().removeFileExtension().addFileExtension("persist")) ; 
			
				if (ddlFile.exists() && databombFile.exists() && persistFile.exists()) {
					InputStream inStreamDdl = ddlFile.getContents() ;
					String streamStringDdl = "" ;
					Scanner scInddl = new Scanner(inStreamDdl) ;
					if (scInddl.hasNext())
						streamStringDdl = scInddl.useDelimiter("\\Z").next() ;
					inStreamDdl.close();
					scInddl.close() ;			
					
					InputStream inStreamDatabomb = databombFile.getContents() ;
					String streamStringData = "" ;
					Scanner scIndatabomb = new Scanner(inStreamDatabomb) ;
					if (scIndatabomb.hasNext())
						streamStringData = scIndatabomb.useDelimiter("\\Z").next() ;
					streamStringData = streamStringData.replace("\n" , "") ;
					streamStringData = streamStringData.replace("\t" , "") ;
					streamStringData = streamStringData.replace("\r" , "") ;
					inStreamDatabomb.close() ;
					scIndatabomb.close() ;	
					
					InputStream inStreamPersist = persistFile.getContents() ;
					String streamStringPersist = "" ;
					Scanner scInper = new Scanner(inStreamPersist) ;
					if (scInper.hasNext())
						streamStringPersist = scInper.useDelimiter("\\Z").next() ;
					inStreamPersist.close();
					scInper.close() ;
					
					String ddl = streamStringDdl ;
					String databomb = streamStringData ;
					String persist = streamStringPersist ;
					
					dermUrl = new URL("platform:/plugin/org.xtext.hipie/vis_files/dermatology.html") ;	
					
					InputStream inStreamDerm = dermUrl.openConnection().getInputStream() ;
					String streamStringHtml = "" ;
					Scanner scIn = new Scanner(inStreamDerm) ;
					if (scIn.hasNext())
						streamStringHtml = scIn.useDelimiter("\\Z").next() ;
					streamStringHtml = streamStringHtml.replace("%_data_%" , databomb) ;
					streamStringHtml = streamStringHtml.replace("%_ddl_%" , ddl) ;
					streamStringHtml = streamStringHtml.replace("%_persist_%" , persist) ;
					inStreamDerm.close();
					scIn.close() ;
					
					String html = streamStringHtml ;

					designModeBrowser.setText(html) ;
					String htmlFilepath = htmlfile.getRawLocation().toOSString()  ;
					workPrefs.put("clean_html_url_design", cleanHtmlUrl.toString()); 
					workPrefs.put("clean_html_filepath_design", htmlFilepath); 
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
}
