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
import org.xtext.hipie.error.DuplicatesPrompt

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
		var ArrayList<IFile> nonDuplicateList = new ArrayList()
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
						if (nonDuplicateList.get(k).fullPath.removeFileExtension.lastSegment == fileList.get(i).fullPath.removeFileExtension.lastSegment) {
							nonDuplicate = false
							DuplicatesPrompt.prompt(nonDuplicateList.get(k))
						}
					if (nonDuplicate) {
						filteredFileList += fileList.get(i)
						nonDuplicateList += fileList.get(i)
					}
				}
		
		
		if (nonDuplicateList.size != databombList.size) {
			var ArrayList<OutDataset> missingDatabombs = new ArrayList() ;
			for (i : 0..<databombList.size) {
				var exists = false
				for (j : 0..<nonDuplicateList.size)
					if (nonDuplicateList.get(j).fullPath.removeFileExtension.lastSegment == databombList.get(i).name)
						exists = true ;
				if (!exists)
					missingDatabombs += databombList.get(i)
			}
			var message = "Databombs for the following files are missing."
			for (i : 0..<missingDatabombs.size)
				message += "\n" + missingDatabombs.get(i).name + ".databomb"
			var status = new HIPIEStatus(IStatus.ERROR,
					"org.xtext.hipie", HIPIEStatus.INSUFFICIENT_DATABOMB_FILES,
					message, null);
				StatusManager.getManager().handle(status,
					(StatusManager.SHOW.bitwiseOr(StatusManager.LOG)));
				return ;		
		}
	}	
}