package org.xtext.hipie.views;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

public class VizualiserPart extends ViewPart {

	Vizualiser browser;
	Vizualiser text;

	
	public VizualiserPart() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		 text = new Vizualiser(parent, true);

	}


	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	
	}
}
