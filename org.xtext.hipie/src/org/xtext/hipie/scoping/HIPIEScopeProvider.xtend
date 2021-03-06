/*
 * generated by Xtext
 */
package org.xtext.hipie.scoping

import org.eclipse.xtext.scoping.IScope
import org.eclipse.emf.ecore.EReference
import org.xtext.hipie.hIPIE.OutputSection
import org.eclipse.xtext.scoping.Scopes
import org.xtext.hipie.hIPIE.VisualSection
import org.xtext.hipie.hIPIE.InputSection
import org.eclipse.emf.ecore.util.EcoreUtil
import org.xtext.hipie.hIPIE.*
import org.xtext.hipie.hIPIE.VisBasisQualifiers
import org.eclipse.xtext.scoping.impl.MapBasedScope
import java.util.ArrayList

/**
 * This class contains custom scoping description.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#scoping
 * on how and when to use it.
 * 
 * 
 * NOTE TO SELF : class iterable is extremely buggy on xtend, use lists.
 */
class HIPIEScopeProvider extends org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider {

	def scope_OutputBase_base(OutputSection context, EReference ref) {
		val parent = EcoreUtil.getRootContainer(context)
		for (i : 0 ..< parent.eContents.length)
			if (parent.eContents.get(i) instanceof InputSection)
				return getScope(parent.eContents.get(i), ref)
	}

	def scope_VisBasis_basis(VisualSection context, EReference ref) {
		val parent = EcoreUtil.getRootContainer(context)
		for (i : 0 ..< parent.eContents.length)
			if (parent.eContents.get(i) instanceof OutputSection)
				return getScope(parent.eContents.get(i), ref)
	}

	// REVisit to enact nestbasis stuff
	def scope_Function_vars(VisBasisQualifiers context, EReference ref) {
		val parent = context.eContainer
		if (parent instanceof VisBasis) {
			val function_container = parent as VisBasis
			if (function_container.basis.out_base != null) {
				val origin = function_container.basis.out_base.base
				val list_1 = Scopes::scopedElementsFor(origin.eContents)
				var list_ecl_fields = (function_container.basis.eAllContents.filter(ECLFieldType).toIterable)
				val list_2 = Scopes::scopedElementsFor(list_ecl_fields)
				val list = list_1 + list_2
				return MapBasedScope::createScope(IScope::NULLSCOPE, list)
			} else {
				var list_ecl_fields = (function_container.basis.eAllContents.filter(ECLFieldType).toIterable)
				return Scopes::scopeFor(list_ecl_fields)
			}
		}
	}

	def scope_AggFunction_vars(VisBasisQualifiers context, EReference ref) {
		val parent = context.eContainer
		if (parent instanceof VisBasis) {
			val function_container = parent as VisBasis
			if (function_container.basis.out_base != null) {
				val origin = function_container.basis.out_base.base
				val list_1 = Scopes::scopedElementsFor(origin.eContents)
				var list_ecl_fields = (function_container.basis.eAllContents.filter(ECLFieldType).toIterable)
				val list_2 = Scopes::scopedElementsFor(list_ecl_fields)
				val list = list_1 + list_2
				return MapBasedScope::createScope(IScope::NULLSCOPE, list)
			} else {
				var list_ecl_fields = (function_container.basis.eAllContents.filter(ECLFieldType).toIterable)
				return Scopes::scopeFor(list_ecl_fields)
			}
		}
	}

	def scope_VisFilter_vals(VisBasis context, EReference ref) {
		val function_container = context
		if (function_container.basis.out_base != null) {
			val origin = function_container.basis.out_base.base
			val list_1 = Scopes::scopedElementsFor(origin.eContents)
			var list_ecl_fields = (function_container.basis.eAllContents.filter(ECLFieldType).toIterable)
			val list_2 = Scopes::scopedElementsFor(list_ecl_fields)
			val list = list_1 + list_2
			return MapBasedScope::createScope(IScope::NULLSCOPE, list)
		} else {
			var list_ecl_fields = (function_container.basis.eAllContents.filter(ECLFieldType).toIterable)
			return Scopes::scopeFor(list_ecl_fields)
		}
	}

	// Revisit for nest basis
	def scope_Function_vars(VisualOptions context, EReference ref) {
		val parent = context.eContainer
		if (parent instanceof Visualization) {
			val viz = parent as Visualization
			val function_container = viz.parens.input
			if (function_container.quals == null) {
				if (function_container.basis.out_base != null) {
					val origin = function_container.basis.out_base.base
					val list_1 = Scopes::scopedElementsFor(origin.eContents)
					var list_ecl_fields = (function_container.basis.eAllContents.filter(ECLFieldType).toIterable)
					val list_2 = Scopes::scopedElementsFor(list_ecl_fields)
					val list = list_1 + list_2
					return MapBasedScope::createScope(IScope::NULLSCOPE, list)
				} else {
					var list_ecl_fields = (function_container.basis.eAllContents.filter(ECLFieldType).toIterable)
					return Scopes::scopeFor(list_ecl_fields)
				}
			} else {
				var valid_vars = (function_container.quals.eAllContents.filter(Function).toList)
				var var_list = new ArrayList<PosVizData>
				for (i : 0 ..< valid_vars.size)
					if (valid_vars.get(i).vars != null)
						var_list += valid_vars.get(i).vars

				return Scopes::scopeFor(var_list)
			}
		}
	}

	def scope_AggFunction_vars(VisualOptions context, EReference ref) {
		val parent = context.eContainer
		if (parent instanceof Visualization) {
			val viz = parent as Visualization
			val function_container = viz.parens.input
			if (function_container.quals == null) {
				if (function_container.basis.out_base != null) {
					val origin = function_container.basis.out_base.base
					val list_1 = Scopes::scopedElementsFor(origin.eContents)
					var list_ecl_fields = (function_container.basis.eAllContents.filter(ECLFieldType).toIterable)
					val list_2 = Scopes::scopedElementsFor(list_ecl_fields)
					val list = list_1 + list_2
					return MapBasedScope::createScope(IScope::NULLSCOPE, list)
				} else {
					var list_ecl_fields = (function_container.basis.eAllContents.filter(ECLFieldType).toIterable)
					return Scopes::scopeFor(list_ecl_fields)
				}
			} else {
				var valid_vars = (function_container.quals.eAllContents.filter(Function).toList)
				var var_list = new ArrayList<PosVizData>
				for (i : 0 ..< valid_vars.size)
					if (valid_vars.get(i).vars != null)
						var_list += valid_vars.get(i).vars

				return Scopes::scopeFor(var_list)
			}
		}
	}

	def scope_SelectOptionMapping_src(VisualOptions context, EReference ref) {
		val parent = context.eContainer
		if (parent instanceof Visualization) {
			val viz = parent as Visualization
			val function_container = viz.parens.input
			if (function_container.quals == null) {
				if (function_container.basis.out_base != null) {
					val origin = function_container.basis.out_base.base
					val list_1 = Scopes::scopedElementsFor(origin.eContents)
					var list_ecl_fields = (function_container.basis.eAllContents.filter(ECLFieldType).toIterable)
					val list_2 = Scopes::scopedElementsFor(list_ecl_fields)
					val list = list_1 + list_2
					return MapBasedScope::createScope(IScope::NULLSCOPE, list)
				} else {
					var list_ecl_fields = (function_container.basis.eAllContents.filter(ECLFieldType).toIterable)
					return Scopes::scopeFor(list_ecl_fields)
				}
			} else {
				var valid_vars = (function_container.quals.eAllContents.filter(Function).toList)
				var var_list = new ArrayList<PosVizData>
				for (i : 0 ..< valid_vars.size)
					var_list += valid_vars.get(i).vars

				return Scopes::scopeFor(var_list)
			}
		}
	}

	def scope_SelectOptionMapping_viz(VisualSection context, EReference ref) {
		var valid_vars = (context.eContents)
		return Scopes::scopeFor(valid_vars)
	}

}
