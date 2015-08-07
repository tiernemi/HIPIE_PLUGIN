package org.xtext.hipie.views;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class DesignModeView extends ViewPart {

	static public String ID = "org.xtext.hipie.design_mode" ;
	DesignModeComposite internalComposite ;	
	
	public DesignModeView() {
		
	}

	@Override
	public void createPartControl(Composite parent) {	
		internalComposite = new DesignModeComposite(parent, true);
	}

	@Override
	public void setFocus() {		
	}
	
	public void updateView(IFile htmlFile)
	{
		internalComposite.setFilepath(htmlFile) ;
	}

}
