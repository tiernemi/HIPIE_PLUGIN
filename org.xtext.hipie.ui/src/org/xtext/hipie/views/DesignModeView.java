package org.xtext.hipie.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class DesignModeView extends ViewPart {

	static public String ID = "org.xtext.hipie.design_mode" ;
	private String persist ;
	private String ddl ;
	private String databomb ;
	private String perFilepath ;
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
	
	public void updateView()
	{
		internalComposite.setStrings(ddl, databomb, persist, perFilepath);
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
		return perFilepath;
	}

	public void setPer_filepath(String per_filepath) {
		this.perFilepath = per_filepath;
	}	
}
