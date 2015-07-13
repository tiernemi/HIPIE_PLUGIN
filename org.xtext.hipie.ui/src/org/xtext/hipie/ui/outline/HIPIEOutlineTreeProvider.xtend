/*
 * generated by Xtext
 */
package org.xtext.hipie.ui.outline

import org.eclipse.xtext.ui.editor.outline.IOutlineNode
import org.xtext.hipie.hIPIE.*
import org.xtext.hipie.hIPIE.Value
import org.xtext.hipie.hIPIE.OutputOptions

/**
 * Customization of the default outline structure.
 *
 * See https://www.eclipse.org/Xtext/documentation/304_ide_concepts.html#outline
 */
class HIPIEOutlineTreeProvider extends org.eclipse.xtext.ui.editor.outline.impl.DefaultOutlineTreeProvider {
	def protected void _createChildren(IOutlineNode parentNode,
           BaseProp prop) {
        for(Value out_val : prop.val_list.vals) {
            createEObjectNode(parentNode.parent.parent, out_val);
        }
    }
    def protected boolean _isLeaf(BaseProp prop) {
		return true 
    }
    def protected boolean _isLeaf(Visualization vis) {
		return true 
    }
    def protected boolean _isLeaf(OutTypeSimple out_type) {
		return true 
    }
    def protected void _createNode(IOutlineNode parentNode, OutputOptions out)
    {
    	
    }
    def protected void _createNode(IOutlineNode parentNode, VisName vis_name)
    {
    	
    }
    def protected void _createNode(IOutlineNode parentNode, VisualOptions vis_ops)
    {
    	
    }
    def protected void _createNode(IOutlineNode parentNode, VisBasisParens vis_parents)
    {
    	
    }
    def protected void _createNode(IOutlineNode parentNode, VisInputValue vis_in)
    {
    	
    }
    def protected void _createNode(IOutlineNode parentNode, VisualSectionOptions vis_sec_ops)
    {
    	
    }
    def protected void _createNode(IOutlineNode parentNode, InputOptions input_ops)
    {
    	
    }   
}