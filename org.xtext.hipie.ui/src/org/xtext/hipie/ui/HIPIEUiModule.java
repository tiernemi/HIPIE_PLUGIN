/*
 * generated by Xtext
 */
package org.xtext.hipie.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.ui.editor.outline.impl.OutlineFilterAndSorter.IComparator;
import org.eclipse.xtext.ui.editor.preferences.LanguageRootPreferencePage;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;
import org.xtext.hipie.commands_listeners.DataSaveListener;
import org.xtext.hipie.commands_listeners.DataSourceListener;
import org.xtext.hipie.commands_listeners.DesignModeVisualiserListener;
import org.xtext.hipie.views.HIPIEPrefPage;

/**
 * Use this class to register components to be used within the IDE.
 */

public class HIPIEUiModule extends org.xtext.hipie.ui.AbstractHIPIEUiModule {
	public HIPIEUiModule(AbstractUIPlugin plugin) {
		super(plugin);
		DataSourceListener.init() ;
		DesignModeVisualiserListener.init() ;
		DataSaveListener.init();
	}
	
	 public Class<? extends IHighlightingConfiguration> bindIHighlightingConfiguration () {
		 return HIPIEHighlightingConfig.class;
		 }
		 
	 public Class<? extends ISemanticHighlightingCalculator> bindISemanticHighlightingCalculator(){
		 return HIPIEHighlightingCalc.class;
		 }
		 
	 
	 public Class<? extends LanguageRootPreferencePage> bindLanguageRootPreferencePage() {
		 return HIPIEPrefPage.class;
		 }
	 @Override
	 public Class<? extends IComparator>  bindOutlineFilterAndSorter$IComparator() {
		 return HIPIEOutlineOrdering.class;
		 }
}

