package org.xtext.hipie.views;

import org.eclipse.core.runtime.Path;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

public class DesignModeView extends ViewPart {

	static public String ID = "org.xtext.hipie.design_mode" ;
	
	DesignModeComposite internal_composite;
	
	
	public DesignModeView() {
		
	}

	@Override
	public void createPartControl(Composite parent) {	
		internal_composite = new DesignModeComposite(parent, true);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	public void UpdateView(String dd, String db, String per)
	{
		internal_composite.setStrings(dd, db, per) ;
	}
}
