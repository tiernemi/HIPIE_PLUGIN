package org.xtext.hipie.generator

import org.eclipse.core.resources.IContainer
import java.util.ArrayList
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IFolder
import org.eclipse.core.resources.IProject
import org.eclipse.emf.ecore.resource.Resource
import org.xtext.hipie.hIPIE.OutDataset
import java.util.List
import org.xtext.hipie.error.HIPIEStatus
import org.eclipse.core.runtime.IStatus
import org.eclipse.ui.statushandlers.StatusManager
import org.eclipse.swt.widgets.Display
import org.eclipse.ui.dialogs.ListDialog
import org.eclipse.ui.PlatformUI
import org.eclipse.jface.viewers.ArrayContentProvider

class DatabombLocator {
	
	static def void findAllDatabombFiles(IContainer cont, ArrayList<IFile> fileList) {	
		var memberList = cont.members.toArray
		for (i : 0..<memberList.size) {
			if (memberList.get(i) instanceof IFolder || memberList.get(i) instanceof IProject)
				findAllDatabombFiles((memberList.get(i) as IContainer), fileList)
			if (memberList.get(i) instanceof IFile)
				if ((memberList.get(i) as IFile).fileExtension == "databomb")
					fileList += (memberList.get(i) as IFile)
		}
		return ;
	}
	
	static def void filterDatabombs(Resource resource, ArrayList<IFile> fileList, ArrayList<IFile> filteredFileList) {
		var List<OutDataset> datasetList = resource.allContents.filter(OutDataset).toIterable.toList
		var ArrayList<OutDataset> databombList = new ArrayList()
		var ArrayList<String> nonDuplicateList = new ArrayList()
		for (i : 0..<datasetList.size)
			if (datasetList.get(i).ops != null)
				for (j : 0..<datasetList.get(i).ops.output_ops.size) 
					if (datasetList.get(i).ops.output_ops.get(j).type == "DATABOMB")
						databombList += datasetList.get(i)
		
		for (i : 0..<fileList.size)
			for (j : 0..<databombList.size)
				if (databombList.get(j).name == fileList.get(i).fullPath.removeFileExtension.lastSegment) {
					var nonDuplicate = true ;
					for (k : 0..<nonDuplicateList.size)
						if (nonDuplicateList.get(k) == fileList.get(i).fullPath.removeFileExtension.lastSegment) {
							nonDuplicate = false
							var message = "Warning. Duplicate files with the name : " + fileList.get(i).name + "have been detected."
							var status = new HIPIEStatus(IStatus.WARNING,
								"org.xtext.hipie", HIPIEStatus.DUPLICATE_DATABOMB_FILES,
								message, null);
							StatusManager.getManager().handle(status,
								(StatusManager.SHOW.bitwiseOr(StatusManager.LOG)));
						}
					if (nonDuplicate)
						filteredFileList += fileList.get(i)
						nonDuplicateList += fileList.get(i).fullPath.removeFileExtension.lastSegment
				}
		}
					
}