/*
 * generated by Xtext
 */
package org.xtext.hipie.generator

import org.eclipse.emf.ecore.resource.Resource
import java.lang.Process
import java.util.Scanner
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.core.resources.ResourcesPlugin
import java.io.FileInputStream
import java.net.URL
import org.eclipse.core.runtime.preferences.InstanceScope
import org.eclipse.core.resources.IResource
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.core.runtime.Path
import java.io.FileOutputStream
import java.util.ArrayList
import java.io.FileWriter
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IFolder
import org.eclipse.core.resources.IContainer
import org.xtext.hipie.hIPIE.OutDataset
import java.util.List


/**
 * Generates code from your model files on save.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#code-generation
 */
 
class HIPIEGenerator implements IGenerator {
	
	override void doGenerate(Resource resource, IFileSystemAccess fsa) {
		var ws_root = ResourcesPlugin.getWorkspace().getRoot() 
		if (resource.URI.isPlatformResource()) {
			var platformString = resource.URI.toPlatformString(true)
			var resourceFile = ws_root.findMember(platformString)
			var project = resourceFile.project
						
			// Get Compiler Location //
			val defaultCompilerPath = ResourcesPlugin.getWorkspace().getRoot().getRawLocation().toOSString() ;
			var workPrefs = InstanceScope.INSTANCE.getNode("org.xtext.hipie.ui");
			val compilerPath = new Path(workPrefs.get('Compiler Location' , defaultCompilerPath))

			var ddlFilePath = resourceFile.projectRelativePath.removeFileExtension().addFileExtension("ddl")
			var ddlFile = project.getFile(ddlFilePath)
			println(ddlFile.rawLocation.toOSString)
			
			//  Generate DDL  //	
			var ddl_cmd = "java -cp "  + compilerPath.toOSString + " org/hpcc/HIPIE/commandline/CommandLineService -databomb " + resourceFile.rawLocation.toOSString() + " -o " + ddlFile.rawLocation.toOSString() + " -verbose"
			System.out.println(ddl_cmd)
			var proc = Runtime.getRuntime().exec(ddl_cmd) as Process
			var in = proc.inputStream
			var er = proc.errorStream
			var scVerbose = new Scanner(in)
			var scError = new Scanner(er)
			var streamString = new String
			var streamStringErrors = new String
			if (scVerbose.hasNext())
				streamString = scVerbose.useDelimiter("\\Z").next() ;
			if (scError.hasNext())
				streamStringErrors = scError.useDelimiter("\\Z").next() ;
			System.out.println(streamString)
			System.out.println(streamStringErrors)
			in.close()
			er.close() 	
		
			//  Generate DataBomb  //
			var databombFilePath =  resourceFile.projectRelativePath.removeFileExtension().addFileExtension("databomb");
			var databombFile = project.getFile(databombFilePath);
			if (databombFile.exists())
				databombFile.delete(true,null)
				
			var ArrayList<IFile> fileList = new ArrayList<IFile>() ;
			var ArrayList<IFile> filteredFileList = new ArrayList<IFile>() ;
			findAllDatabombFiles(project, fileList)
			filterDatabombs(resource, fileList, filteredFileList)
					
			for (i : 0..<filteredFileList.size) {
				var tempDatFile = filteredFileList.get(i)
				var inStream = new FileInputStream(tempDatFile.rawLocation.toOSString)
				var fw = new FileWriter(databombFile.rawLocation.toOSString,true)
				var streamStringTemp = new String
				var scIn = new Scanner(inStream)
				if (scIn.hasNext())
					streamStringTemp = scIn.useDelimiter("\\Z").next()
				if(i != filteredFileList.size-1 && filteredFileList.size > 1) {
					streamStringTemp = streamStringTemp.substring(0, streamStringTemp.lastIndexOf("}"))
					streamStringTemp += ","
				}
				if(i != 0 && fileList.size > 1) {
					streamStringTemp = streamStringTemp.substring(streamStringTemp.indexOf("{")+1) 
				}
				fw.write(streamStringTemp)
				fw.close
				inStream.close
			}
			
			// Creates an empty persist if there is none already //
			var perFilepath = resourceFile.projectRelativePath.removeFileExtension().addFileExtension("persist")
			var perFile =  project.getFile(perFilepath)
			if(!perFile.exists) {
				var fwPer = new FileWriter(perFile.rawLocation.toOSString)
				fwPer.close()
			}

			// Converts ddl file to string //
			var inStream = new FileInputStream(ddlFile.rawLocation.toOSString());			
			var streamStringDdl = new String
			var scIn = new Scanner(inStream)
			if (scIn.hasNext())
				streamStringDdl = scIn.useDelimiter("\\Z").next()
		
			streamStringDdl = streamStringDdl.replace("\n" , "")
			streamStringDdl = streamStringDdl.replace(" " , "")
			streamStringDdl = streamStringDdl.replace("\t" , "")
			streamStringDdl = streamStringDdl.replace("\r" , "")
			inStream.close() 
			scIn.close()
			
			// Converts databomb to cleaned string 
			inStream = new FileInputStream(databombFile.rawLocation.toOSString())
			var streamStringDatabomb = new String
			scIn = new Scanner(inStream)
			if (scIn.hasNext())
				streamStringDatabomb = scIn.useDelimiter("\\Z").next()
		
			streamStringDatabomb = streamStringDatabomb.replace("\n" , "")
			streamStringDatabomb = streamStringDatabomb.replace(" " , "")
			streamStringDatabomb = streamStringDatabomb.replace("\t" , "")
			streamStringDatabomb = streamStringDatabomb.replace("\r" , "")
			inStream.close()
			scIn.close()
			
			inStream = new FileInputStream(perFile.rawLocation.toOSString());
			var streamString_per = new String
			scIn = new Scanner(inStream)
			if (scIn.hasNext())
				streamString_per = scIn.useDelimiter("\\Z").next()
			streamString_per = streamString_per.replace("\n" , "")
			streamString_per = streamString_per.replace(" " , "")
			streamString_per = streamString_per.replace("\t" , "")
			streamString_per = streamString_per.replace("\r" , "")
			inStream.close()
			scIn.close()
			
			// Generate HTML //
			var cleanUrl = new URL("platform:/plugin/org.xtext.hipie/vis_files/clean.html")		
			var inStreamCleanConnection = cleanUrl.openConnection().getInputStream()
			
			var htmlCleanFilePath = resourceFile.projectRelativePath.removeFileExtension().addFileExtension("html")
			
			var streamStringHtml = new String
			scIn = new Scanner(inStreamCleanConnection)
			if (scIn.hasNext())
				streamStringHtml = scIn.useDelimiter("\\Z").next() 
			
			streamStringHtml = streamStringHtml.replace("%_data_%" , streamStringDatabomb)
			streamStringHtml = streamStringHtml.replace("%_ddl_%" , streamStringDdl)
			streamStringHtml = streamStringHtml.replace("%_persist_%" , streamString_per)
			
			var htmlCleanFile = project.getFile(htmlCleanFilePath)
			var htmlOut = new FileOutputStream(htmlCleanFile.rawLocation.toOSString)
			htmlOut.write(streamStringHtml.getBytes())			
			project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor())
			in.close()
			er.close()
			htmlOut.close()
		}
	}
	
	def void findAllDatabombFiles(IContainer cont, ArrayList<IFile> fileList) {	
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
	
	def void filterDatabombs(Resource resource, ArrayList<IFile> fileList, ArrayList<IFile> filteredFileList) {
		var List<OutDataset> datasetList = resource.allContents.filter(OutDataset).toIterable.toList
		var ArrayList<OutDataset> databombList = new ArrayList() ;
		for (i : 0..<datasetList.size)
			if (datasetList.get(i).ops != null)
				for (j : 0..<datasetList.get(i).ops.output_ops.size) 
					if (datasetList.get(i).ops.output_ops.get(j).type == "DATABOMB")
						databombList += datasetList.get(i)
		
		for (i : 0..<fileList.size)
			for (j : 0..<databombList.size)
				if (databombList.get(j).name == fileList.get(i).fullPath.removeFileExtension.lastSegment) {
					filteredFileList += fileList.get(i)
					
				}
					
	}
}
