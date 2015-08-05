/*
 * generated by Xtext
 */
package org.xtext.hipie.ui.labeling

import com.google.inject.Inject
import org.xtext.hipie.hIPIE.*
import org.eclipse.emf.edit.provider.StyledString
import org.eclipse.swt.graphics.Image

/**
 * Provides labels for EObjects.
 * 
 * See https://www.eclipse.org/Xtext/documentation/304_ide_concepts.html#label-provider
 */
class HIPIELabelProvider extends org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider {

	@Inject
	new(org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider delegate) {
		super(delegate);
	}

	// Labels and icons can be computed like this:
	
	def text(Value v) {
		if (v.name == null)
		{
			if (v.str_val != null)
				'STRING : ' + v.str_val
			else
				'INT : ' + v.int_val 
		}
	}
	
	def text(Bool obj)
	{
		var label_string = ""
		label_string = obj.name + " : " + obj.type
	}
	def text(FieldDecl obj)
	{
		var label_string = ""
		label_string = obj.name + " : " + obj.type
		return new StyledString("helllo")
	}
	def text(IntVar obj)
	{
		var label_string = ""
		label_string = obj.name + " : " + obj.type
	}
	def text(StringVar obj)
	{
		var label_string = ""
		label_string = obj.name + " : " + obj.type
	}
	def text(Group obj)
	{
		var label_string = ""
		label_string = obj.name + " : " + obj.type
	}
	def text(Real obj)
	{
		var label_string = ""
		label_string = obj.name + " : " + obj.type
	}
	def text(Record obj)
	{
		var label_string = ""
		label_string = obj.name + " : " + obj.type
	}
	def text(KelAttrDecl obj)
	{
		var label_string = ""
		label_string = obj.name + " : " + obj.type
	}
	def text(KelEntityDecl obj)
	{
		var label_string = ""
		label_string = obj.name + " : " + obj.type
	}
	def text(KelEntityDeclSimple obj)
	{
		var label_string = ""
		label_string = obj.name + " : " + obj.type
	}
	def text(KelBase obj)
	{
		var label_string = ""
		label_string = obj.name + " : " + obj.type
	}
	def text(Dataset obj)
	{
		var label_string = ""
		label_string = obj.name + " : " + obj.type
	}
	
	def text(BaseProp base)
	{
		var label_string = ""
		if (base.val_list != null)
		{
			for (i : 0..<base.val_list.vals.size)
			{
				if (base.val_list.vals.get(i).name != null)
				{
					if(label_string.length != 0)
						label_string += ", "
					label_string += base.val_list.vals.get(i).name
				}
				else if (base.val_list.vals.get(i).str_val != null)
				{
					if(label_string.length != 0)
						label_string += ", "
					label_string += base.val_list.vals.get(i).str_val
				}
				else 
				{
					if(label_string.length != 0)
						label_string += ", "
					label_string += base.val_list.vals.get(i).int_val
				}
			}
		}		
		if (base.cat_list != null)
		{
			for (i : 0..<base.cat_list.categories.size)
				{
					if(base.cat_list.categories.get(i) != null)
					{
						if(label_string.length != 0)
							label_string += ", "
						label_string += base.cat_list.categories.get(i).category_type
					}
				}
		}
		base.property + " : " + label_string
	}
	
	def text(OutDataset out_dataset)
	{
		var out_string = ""
		for (i : 0..<out_dataset.ops.output_ops.size)
		{
			if (out_dataset.ops.output_ops.get(i).type != null)
			{
				if(out_string.length != 0)
					out_string += ", "
				else
					out_string = " : "
				out_string += out_dataset.ops.output_ops.get(i).type
			}
		}
		out_dataset.name + out_string
	}
	
	def image(OutDataset element) {
		return "data_folder_out_small.png"
	}
	
	def image(Dataset element) {
		return "data_folder_in_small.png"
	}
	
	def image(ECLFieldType element) {
		return "variable.png"
	}
	
	def image(OutTypeSimple element) {		
		return "variable.png"
	}
	
	def image(InputSimpleTypes element) {		
		return "variable.png"
	}
	
	def image(Visualization element) {
		if (element.type == "PIE")
			return "pie.png"
		if (element.type == "BAR")
			return "bar.png"			
	}
	
	/* 
	def text(Visualization vis)
	{
		var vis_type = vis.type.name
		var out_string = ""
		var vis_basis  = "";
		if (vis.parens != null)
			vis_basis =  vis.parens.input.basis.name
		out_string = vis.name + " : " + vis_type + " - " + vis_basis
	}
	
	def text(VisualSection vis)
	{
		var sec_name = vis.section_name 
		var sec_id = vis.name
		
		sec_name + " : " + sec_id
	}

	def text(OutTypeSimple out)
	{
		var out_string = ""
		if (out.sitype != null)
		{
			if (out.vals.name != null)
			{
				out_string += out.vals.name + ' : ' + out.type
			}
		}
	}
	*/
	
}
