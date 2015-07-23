package org.xtext.hipie.views.design_mode_browser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.eclipse.core.internal.content.Activator;
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

public class Vizualiser extends Composite {

	public Vizualiser(final Composite parent, boolean showAddressBar) {
		super(parent, SWT.NONE);

		
		final Text text;
		final Text textwo;
		final Browser browser;
		parent.setLayout(new GridLayout(2, false));

		final String myString="Hello World!";
		
	//	String id = "myFunction()";
/*
		browser = new Browser(parent, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	//	browser.setUrl("file:///Users/Evan/Desktop/project_2/javascript_test.html");
		browser.setUrl("www.google.com");
		if (browser.execute(id) != true) {
			text = new Text(parent, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
			text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			text.setText("text here ");
		}*/
		
	
		
		
		
		
		
		
		
		
		
		browser = new Browser(parent, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		browser.setUrl("file:///Users/Evan/Google%20Drive/ICHEC/hpcc_viz_2/demos/derm2.html");
	
		
		
		
		Button addMarker = new Button(parent, SWT.PUSH);
        addMarker.setText("Show Properites");
        addMarker.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                browser.evaluate("showProperties(true);");
               
            }
        });
        
		Button NoShowProperties = new Button(parent, SWT.PUSH);
        NoShowProperties.setText("Do not show properties");
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
        
        

        
        
        
		
		
		
		
		
		
		
		
		
		
		
		/* Working browser and calling a script from within the browser 
		
		
		browser = new Browser(parent, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		browser.setUrl("file:///Users/Evan/Desktop/project_2/javascript_test.html");
	
		
        Button addMarker = new Button(parent, SWT.PUSH);
        addMarker.setText("Add Marker");
        addMarker.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                browser.evaluate("myFunction();");
               
            }
        });
        
        */
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*
		
		browser.addControlListener(new ControlListener() {
            public void controlResized(ControlEvent e) {
		
        if(browser.execute("document.getElementById('demo').innerHTML= "
                        + myString + ";")){
        	
        	
        	Text textwo = new Text(parent, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
			textwo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			textwo.setText("true ");
        	
        	
        }
        
        else{
        	Text textwo = new Text(parent, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
			textwo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			textwo.setText("false ");
        	
        	
        }
        
        
            }
            

			@Override
			public void controlMoved(ControlEvent e) {
				// TODO Auto-generated method stub
				
			}


		});
        
       */ 

		  
		/*  
		
		  browser = new Browser(parent, SWT.NONE); browser.setLayoutData(new
		  GridData(SWT.FILL, SWT.FILL, true, true)); // browser.setUrl( "file:///Users/Evan/Desktop/runtime-Test_Rig/pop/vis_gen/marshaller.html" ); 
		  browser.setUrl(
		  "file:///Users/Evan/Google%20Drive/ICHEC/hpcc_viz_2/demos/derm2.html"
		  );
		  
		  Button button = new Button(parent, SWT.PUSH); button.setText(
		  "Design Mode"); button.addSelectionListener(new SelectionAdapter() {
		  
		  @Override public void widgetSelected(SelectionEvent e) {
		  
		  browser.setUrl(
		  "file:///Users/Evan/Desktop/runtime-Test_Rig/pop/vis_gen/marshaller.html"
		  );
		  
		  } });
		  
		  Button button2 = new Button(parent, SWT.PUSH);
		  button2.setText("Dermatology"); button2.addSelectionListener(new
		  SelectionAdapter() {
		  
		  @Override public void widgetSelected(SelectionEvent e) {
		  
		  browser.setUrl(
		  "file:///Users/Evan/Google%20Drive/ICHEC/hpcc_viz_2/demos/derm2.html"
		  );
		  
		  } }); 
		  */
		 
	}

}
