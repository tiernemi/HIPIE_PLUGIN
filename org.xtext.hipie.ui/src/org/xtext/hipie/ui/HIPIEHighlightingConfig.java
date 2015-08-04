package org.xtext.hipie.ui;

import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.xtext.ui.editor.utils.TextStyle;
import static org.eclipse.swt.SWT.*;

public class HIPIEHighlightingConfig implements IHighlightingConfiguration
{
	 // Provide an id string for the highlighting calculator. //
	 public static final String KEYWORD = "keyword";
	 public static final String DATATYPE = "datatype";
	 public static final String COMMENT = "comment";
	 public static final String STRING = "string";
	 public static final String CROSS_REF= "CrossReference" ;
	 public static final String GENERATE = "Generate Inline" ;
	 
	 public static final String[] ALL_STRINGS = {KEYWORD , STRING} ;
	
	 public void configure(IHighlightingConfigurationAcceptor acceptor) {
		 addType( acceptor, STRING, 125, 38, 205, NORMAL );
		 addType( acceptor, COMMENT, 150, 200, 200, NORMAL );
		 addType( acceptor, KEYWORD, 39, 64, 138, BOLD );
		 addType( acceptor, GENERATE, 72, 118, 255, NORMAL );
		 addType( acceptor, CROSS_REF, 140, 140, 140, ITALIC );
		 addType( acceptor, DATATYPE, 140, 140, 140, ITALIC );
	 }
	 
	 public void addType( IHighlightingConfigurationAcceptor acceptor, String s,
			 int r, int g, int b, int style ){
		 TextStyle textStyle = new TextStyle();
		 textStyle.setBackgroundColor(new RGB(255, 255, 255));
		 textStyle.setColor(new RGB(r, g, b));
		 textStyle.setStyle(style);
		 acceptor.acceptDefaultHighlighting(s, s, textStyle);
	 }
} 
