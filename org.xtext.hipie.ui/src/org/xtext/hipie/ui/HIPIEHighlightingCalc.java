package org.xtext.hipie.ui;

import org.xtext.hipie.hIPIE.GenerateBodyInline;
//import org.xtext.hipie.hIPIE.generate_body_inline;
//import org.xtext.hipie.hIPIE.generate_section;
import org.eclipse.xtext.CrossReference;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;

public class HIPIEHighlightingCalc implements ISemanticHighlightingCalculator
{
	 public void provideHighlightingFor( XtextResource resource, IHighlightedPositionAcceptor acceptor ) 
	 {
		 if (resource == null || resource.getParseResult() == null)
			 return;
		 
		INode root = resource.getParseResult().getRootNode();
		for (INode node : root.getAsTreeIterable()) {
			if (node.getGrammarElement() instanceof CrossReference) {
				acceptor.addPosition(node.getOffset(), node.getLength(),
				HIPIEHighlightingConfig.CROSS_REF);
			}
			if (node.getSemanticElement() instanceof GenerateBodyInline)
			{
				acceptor.addPosition(node.getOffset(), node.getLength(),
				HIPIEHighlightingConfig.GENERATE);
			}
		}
	 }
}