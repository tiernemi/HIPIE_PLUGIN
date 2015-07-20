package org.xtext.hipie.ui;

import org.eclipse.xtext.ui.editor.outline.IOutlineNode;
import org.eclipse.xtext.ui.editor.outline.actions.SortOutlineContribution.DefaultComparator;
import org.eclipse.xtext.ui.editor.outline.impl.EObjectNode;
import org.eclipse.xtext.ui.editor.outline.impl.EStructuralFeatureNode;

/**
 * @author Michael Tierney
 * 
 * This class overrides the default alphabetical ordering of the outline such that the
 * main HIPIE plugin outline always remains the same.
 */


public class HIPIEOutlineOrdering extends DefaultComparator {
 	
	@Override
    public int getCategory(IOutlineNode node) {
    if (node instanceof EStructuralFeatureNode)
      {
    	 if(node.getText().equals("Composition Header"))
    		 return -10 ;
    	 if(node.getText().equals("Plugin Properties"))
    		 return -9 ;
    	 if(node.getText().equals("Plugin Permissions"))
    		 return -8 ;
    	 if(node.getText().equals("Inputs"))
    		 return -7 ;
    	 if(node.getText().equals("Contract Instances"))
    		 return -6 ;
    	 if(node.getText().equals("Outputs"))
    		 return -5 ;
    	 if(node.getText().equals("Integrates"))
    		 return -4 ;
    	 if(node.getText().equals("Visualize"))
    		 return -3 ;
    	 if(node.getText().equals("Generates"))
    		 return -2 ;
    	 if(node.getText().equals("Custom"))
    		 return -1 ;
    	 if(node.getText().equals("Resources"))
    		 return 0 ;
      }
      return 0 ;
    }
  }