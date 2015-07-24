package org.xtext.hipie.views;

import org.eclipse.core.runtime.Path;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

public class DesignModeView extends ViewPart {

	static public String ID = "org.xtext.hipie.design_mode" ;
	private String ddl ;
	private String databomb ;
	private String persist ;

	
	DesignModeInternal browser;
	DesignModeInternal text;
	
	public DesignModeView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		text = new DesignModeInternal(parent, true, ddl, databomb, persist);
	}


	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	
	}
	
	public void setDdl(String in)
	{
		ddl = in ;
	}
	
	public void setDatabomb(String in)
	{
		databomb = in ;
	}
	
	public void setPersist(String in)
	{
		persist = in ;
	}
	
	public String getDdl()
	{
		return ddl ;
	}
	
	public String getDatabomb()
	{
		return databomb ;
	}
	
	public String getPersist()
	{
		return persist ;
	}
}
