package org.xtext.hipie.views;

import org.eclipse.core.runtime.Path;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

public class DesignModeView extends ViewPart {

	static public String ID = "org.xtext.hipie.design_mode" ;
	private String persist ;
	private String ddl ;
	private String databomb ;
	private String per_filepath ;
	DesignModeComposite internal_composite ;	
	
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
	
	public void updateView()
	{
		internal_composite.setStrings(ddl, databomb, persist, per_filepath);
	}

	public String getPersist() {
		return persist;
	}

	public void setPersist(String persist) {
		this.persist = persist;
	}

	public String getDdl() {
		return ddl;
	}

	public void setDdl(String ddl) {
		this.ddl = ddl;
	}

	public String getDatabomb() {
		return databomb;
	}

	public void setDatabomb(String databomb) {
		this.databomb = databomb;
	}

	public String getPer_filepath() {
		return per_filepath;
	}

	public void setPer_filepath(String per_filepath) {
		this.per_filepath = per_filepath;
	}
	
	
	
}
