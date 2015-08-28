/*
 * generated by Xtext
 */
package org.xtext.hipie.ui.contentassist

import org.xtext.hipie.ui.contentassist.AbstractHIPIEProposalProvider
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor
import javax.inject.Inject
import org.eclipse.xtext.ui.label.StylerFactory
import org.eclipse.xtext.ui.editor.utils.TextStyle
import org.eclipse.swt.graphics.RGB
import org.eclipse.swt.graphics.FontData
import org.eclipse.swt.SWT
import org.eclipse.jface.viewers.StyledString
import org.eclipse.xtext.Assignment
import org.eclipse.xtext.Keyword
import org.xtext.hipie.hIPIE.OutDataset
import org.eclipse.xtext.naming.QualifiedName
import org.xtext.hipie.hIPIE.PosVizData
import org.xtext.hipie.hIPIE.NestedDatasetDecl
import org.xtext.hipie.hIPIE.Dataset
import org.xtext.hipie.hIPIE.Visualization
import org.xtext.hipie.hIPIE.VisualSection
import org.xtext.hipie.hIPIE.ECLFieldType
import org.xtext.hipie.hIPIE.FieldDecl
import org.xtext.hipie.hIPIE.ECLString
import org.xtext.hipie.hIPIE.ECLInteger
import org.xtext.hipie.hIPIE.ECLQstring
import org.xtext.hipie.hIPIE.ECLReal
import org.xtext.hipie.hIPIE.ECLUnicode
import org.xtext.hipie.hIPIE.ECLData
import org.xtext.hipie.hIPIE.ECLVarstring
import org.xtext.hipie.hIPIE.ECLVarunicode
import org.xtext.hipie.hIPIE.ECLUnsigned
import org.xtext.hipie.hIPIE.ECLBoolean
import org.xtext.hipie.hIPIE.ECLNumType
import org.xtext.hipie.hIPIE.ECLDecType
import org.eclipse.xtext.scoping.IScopeProvider
import org.eclipse.emf.ecore.EReference
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.jface.text.contentassist.ICompletionProposal
import com.google.common.base.Predicate
import org.eclipse.xtext.scoping.IScope
import com.google.common.base.Function
import java.util.ArrayList
import org.eclipse.xtext.naming.IQualifiedNameConverter
import org.eclipse.xtext.conversion.IValueConverter
import org.eclipse.xtext.conversion.IValueConverterService
import org.eclipse.xtext.ui.editor.contentassist.ContentProposalLabelProvider
import org.eclipse.jface.viewers.ILabelProvider
import org.eclipse.xtext.ui.editor.contentassist.IProposalConflictHelper
import org.eclipse.xtext.ui.editor.contentassist.IContentProposalPriorities
import org.eclipse.emf.common.util.Logger
import org.eclipse.xtext.ui.editor.contentassist.AbstractJavaBasedContentProposalProvider
import org.eclipse.xtext.conversion.ValueConverterException
import org.eclipse.xtext.CrossReference
import org.eclipse.xtext.RuleCall
import org.xtext.hipie.hIPIE.SelectOptionMapping

/**
 * See https://www.eclipse.org/Xtext/documentation/304_ide_concepts.html#content-assist
 * on how to customize the content assistant.
 */
class HIPIEProposalProvider extends AbstractHIPIEProposalProvider {

	@Inject
	private StylerFactory stylerFactory;

	def protected TextStyle getTypeTextStyle() {
		var textStyle = new TextStyle();
		textStyle.setColor(new RGB(125, 38, 205));
		textStyle.setFontData(new FontData("typefont", 9, SWT.NORMAL))
		textStyle.setStyle(SWT.NORMAL);
		return textStyle;
	}

	def protected TextStyle getCrossRefTextStyle() {
		var textStyle = new TextStyle()
		textStyle.setColor(new RGB(140, 140, 140));
		textStyle.setFontData(new FontData("basefont", 9, SWT.ITALIC))
		textStyle.setStyle(SWT.ITALIC);
		return textStyle;
	}

	def protected TextStyle getKeywordTextStyle() {
		var textStyle = new TextStyle()
		textStyle.setColor(new RGB(39, 64, 138));
		textStyle.setFontData(new FontData("keyfont", 10, SWT.BOLD))
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	def protected TextStyle getBoldTextStyle() {
		var textStyle = new TextStyle()
		textStyle.setColor(new RGB(0, 0, 0));
		textStyle.setFontData(new FontData("keyfont", 10, SWT.BOLD))
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}
	
	
	@Inject
	private IScopeProvider scopeProvider;

	@Inject
	private HIPIEProposalCreator customHIPIEProposalCreator;
	

	override completeKeyword(Keyword keyword, ContentAssistContext contentAssistContext,
		ICompletionProposalAcceptor acceptor) {
		var StyledString proposalString
		if (keyword.getValue().equals("-") || keyword.getValue().equals(",") || keyword.getValue().equals("(") ||
			keyword.getValue().equals(")") || keyword.getValue().equals("{") || keyword.getValue().equals("}")) {
			if (contentAssistContext.prefix == keyword.value)
				return;
			if (contentAssistContext.offset < contentAssistContext.lastCompleteNode.endOffset)
				return;
		}

		if (keyword.value == ";" || keyword.value == ":")
			proposalString = new StyledString(keyword.value,
				stylerFactory.createXtextStyleAdapterStyler(getBoldTextStyle()))
		else
			proposalString = new StyledString(keyword.value,
				stylerFactory.createXtextStyleAdapterStyler(getKeywordTextStyle()))
		var proposal = createCompletionProposal(keyword.getValue(), proposalString, getImage(keyword),
			contentAssistContext)
		getPriorityHelper().adjustKeywordPriority(proposal, contentAssistContext.getPrefix())
		acceptor.accept(proposal)
	}

	override completeVisBasis_Basis(EObject model, Assignment assignment, ContentAssistContext context,
		ICompletionProposalAcceptor acceptor) {
		super.completeVisBasis_Basis(model, assignment, context, acceptor)
	}

	override completeVisualOption_Vis_cust(EObject model, Assignment assignment, ContentAssistContext context,
		ICompletionProposalAcceptor acceptor) {
		super.completeVisualOption_Vis_cust(model, assignment, context, acceptor)
		var validCustValues = CustomOptionFetcher.fetchValidCustomOptions();
		for (i : 0 ..< validCustValues.length) {
			var proposal = validCustValues.get(i);
			var proposalStyleString = new StyledString(validCustValues.get(i),
				stylerFactory.createXtextStyleAdapterStyler(getKeywordTextStyle()));
			acceptor.accept(createCompletionProposal(proposal, proposalStyleString, getImage(model), context));
		}
	}
	
	override completeSelectOptionMapping_Viz(EObject model, Assignment assignment, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		super.completeSelectOptionMapping_Viz(model, assignment, context, acceptor)
	}
		
	override protected StyledString getStyledDisplayString(EObject element, String qualifiedName, String shortName) {
		var qualName = getQualifiedName(element, qualifiedName, shortName)
		var name = new StyledString(qualName.lastSegment)
		var qualNameString = qualName.toString
		var type = new StyledString()
		var typestring = ""

		if (element instanceof PosVizData) {
			var obj = element as PosVizData
			if (element instanceof ECLFieldType) {
				var cont = obj.eContainer.eContainer
				switch element {
					case element instanceof ECLString: typestring = 'STRING'
					case element instanceof ECLInteger: typestring = 'INTEGER'
					case element instanceof ECLQstring: typestring = 'QSTRING'
					case element instanceof ECLReal: typestring = 'REAL'
					case element instanceof ECLUnicode: typestring = 'UNICODE'
					case element instanceof ECLData: typestring = 'DATA'
					case element instanceof ECLVarstring: typestring = 'VARSTRING'
					case element instanceof ECLVarunicode: typestring = 'VARUNICODE'
					case element instanceof ECLUnsigned: typestring = 'UNSIGNED'
					case element instanceof ECLBoolean: typestring = 'BOOL'
					case element instanceof ECLNumType: typestring = (element as ECLNumType).type
					case element instanceof ECLDecType: typestring = (element as ECLDecType).type
				}
				while (cont instanceof OutDataset || cont instanceof NestedDatasetDecl || cont instanceof Dataset) {
					if (cont instanceof OutDataset) {
						var dataset = cont as OutDataset
						qualNameString = "OUTPUTS." + dataset.name + "." + qualNameString
					}
					if (cont instanceof NestedDatasetDecl) {
						var dataset = cont as NestedDatasetDecl
						qualNameString = dataset.name + "." + qualNameString
					}
					cont = cont.eContainer.eContainer
				}
			}
			if (element instanceof FieldDecl) {
				var cont = obj.eContainer
				typestring = "FIELD"
				if (cont instanceof Dataset) {
					var dataset = cont as Dataset
					qualNameString = "INPUTS." + dataset.name + "." + qualNameString
				}
			}
		}

		if (element instanceof Visualization) {
			var viz = element.eContainer as VisualSection
			typestring = (element as Visualization).type
			qualNameString = viz.name + "." + qualName.toString
		}

		if (element instanceof OutDataset) {
			var data = element as OutDataset
			typestring = data.type
		}

		if (element instanceof Dataset) {
			var data = element as Dataset
			typestring = data.type
		}

		var qualSec = new StyledString(" - " + qualNameString,
			stylerFactory.createXtextStyleAdapterStyler(getCrossRefTextStyle()))
		if (typestring != "")
			type = new StyledString(" : " + typestring, stylerFactory.createXtextStyleAdapterStyler(getTypeTextStyle()))
		return name.append(type).append(qualSec)
	}

	protected def QualifiedName getQualifiedName(EObject element, String qualifiedName, String shortName) {
		var qualifiedNameAsString = qualifiedName
		if (qualifiedNameAsString == null)
			qualifiedNameAsString = shortName
		if (qualifiedNameAsString == null) {
			if (element != null)
				qualifiedNameAsString = labelProvider.getText(element)
			else
				return null
		}
		return qualifiedNameConverter.toQualifiedName(qualifiedNameAsString)
	}
	
	// Manipulation of the objects passed from a scope can be done here
	
	public static class HIPIEProposalCreator {

		@Inject
		private IScopeProvider scopeProvider;

		public final def lookupCrossReference(EObject model, EReference reference, ICompletionProposalAcceptor acceptor,
				Predicate<IEObjectDescription> filter,
				Function<IEObjectDescription, ICompletionProposal> proposalFactory , ContentAssistContext contentAssistContext) {
			if (model != null) {
				var scope = getScopeProvider().getScope(model, reference);
				lookupCrossReference(scope, model, reference, acceptor, filter, proposalFactory, contentAssistContext);
			}
		}
		
		protected def Function<IEObjectDescription, ICompletionProposal> getWrappedFactory(
				EObject model, EReference reference,
				Function<IEObjectDescription, ICompletionProposal> proposalFactory) {
			return proposalFactory;
		}

		// Need to overwrite validProposal functionality with wrappedDactory.apply in order to remove duplicates
	
		public def lookupCrossReference(IScope scope, EObject model, EReference reference,
				ICompletionProposalAcceptor acceptor, Predicate<IEObjectDescription> filter,
				Function<IEObjectDescription, ICompletionProposal> proposalFactory, ContentAssistContext contentAssistContext) {
			var Function<IEObjectDescription, ICompletionProposal> wrappedFactory = getWrappedFactory(model, reference, proposalFactory);
			var Iterable<IEObjectDescription> candidates = queryScope(scope, model, reference, filter);
			var ArrayList<IEObjectDescription> filteredCandidates = filter(candidates, model, contentAssistContext)
			for (IEObjectDescription candidate : filteredCandidates) {
				if (!acceptor.canAcceptMoreProposals())
					return;
				if (filter.apply(candidate)) {
					acceptor.accept(wrappedFactory.apply(candidate));
				}
			}
		}
		
		def ArrayList<IEObjectDescription> filter(Iterable<IEObjectDescription> candidates, EObject model, ContentAssistContext contentAssistContext) {
			var results = new ArrayList<IEObjectDescription>()
			if (model instanceof SelectOptionMapping) {
				println(contentAssistContext.prefix)
				if (contentAssistContext.prefix.empty) {
				for (i : 0..<candidates.size)
					for(j : 0..<candidates.size) {
						var qualName1 = candidates.get(i).qualifiedName
						var qualName2 = candidates.get(j).qualifiedName
						if (qualName1.lastSegment == qualName2.lastSegment) {
								if(qualName1.lastSegment == qualName1.firstSegment)
									results += candidates.get(i)
						}
					}					
				} else {
					results.addAll(candidates)
				}
			}
			else {
				results.addAll(candidates)
			}
				return results
		}

		def void setScopeProvider(IScopeProvider scopeProvider) {
			this.scopeProvider = scopeProvider;
		}

		def IScopeProvider getScopeProvider() {
			return scopeProvider;
		}
		
		def Iterable<IEObjectDescription> queryScope(IScope scope, EObject model, EReference reference, Predicate<IEObjectDescription> filter) {
			return scope.getAllElements();
		}
	}	
	
	override void lookupCrossReference(CrossReference crossReference, EReference reference,
			ContentAssistContext contentAssistContext, ICompletionProposalAcceptor acceptor,
			Predicate<IEObjectDescription> filter) {
		var String ruleName = null;
		if (crossReference.getTerminal() instanceof RuleCall) {
			ruleName = (crossReference.getTerminal() as RuleCall).getRule().getName();
		}
		lookupCrossReference(contentAssistContext.getCurrentModel(), reference, acceptor, filter,
				getProposalFactory(ruleName, contentAssistContext), contentAssistContext);
	}
	
	def protected lookupCrossReference(EObject model, EReference reference, ICompletionProposalAcceptor acceptor,
			Predicate<IEObjectDescription> filter, Function<IEObjectDescription, ICompletionProposal> proposalFactory, ContentAssistContext contentAssistContext) {
		customHIPIEProposalCreator.lookupCrossReference(model, reference, acceptor, filter, proposalFactory, contentAssistContext);
	}
	
	
	// Manipulation of proposal creations can be done here.
	
}
