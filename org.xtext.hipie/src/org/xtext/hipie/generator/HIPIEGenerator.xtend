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
import java.nio.file.Files
import java.nio.file.FileSystems
import java.net.URL
import org.eclipse.core.runtime.preferences.IEclipsePreferences
import org.eclipse.core.runtime.preferences.InstanceScope
import org.eclipse.core.resources.IResource
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.core.runtime.Path

/**
 * Generates code from your model files on save.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#code-generation
 */
class HIPIEGenerator implements IGenerator {
	
	override void doGenerate(Resource resource, IFileSystemAccess fsa) 
	{
		val defaultCompilerPath = ResourcesPlugin.getWorkspace().getRoot().getRawLocation().toOSString() ;
		var prefs = InstanceScope.INSTANCE.getNode("org.xtext.hipie.ui");
		val compilerPath = new Path(prefs.get('Compiler Location' , defaultCompilerPath))
		val compilerPathString = compilerPath.addTrailingSeparator.toOSString + "HIPIE.jar"
		
		var ws_root = ResourcesPlugin.getWorkspace().getRoot();
		if (resource.URI.isPlatformResource()) {
			var platformString = resource.URI.toPlatformString(true);
			var resourceFile = ws_root.findMember(platformString);
			var project = resourceFile.project;
			var genFolder = project.getFolder("gen");
			if (!genFolder.exists){
				genFolder.create(true, true, null);				
			}
			var dataFolder = project.getFolder("data");
			
			var ddlFilePath = resourceFile.fullPath;
			var ddlFileName = ddlFilePath.lastSegment;
			ddlFilePath = genFolder.getFile(ddlFileName).projectRelativePath.removeFileExtension().addFileExtension("ddl");
			var ddlFile = project.getFile(ddlFilePath);
			
			//  Generate DDL  --- 			
			var proc = Runtime.getRuntime().exec("java -cp "  + compilerPathString + " org/hpcc/HIPIE/commandline/CommandLineService -databomb " + resourceFile.rawLocation.toOSString() + " -o " + ddlFile.rawLocation.toOSString() + " -verbose") as Process;
			var in = proc.inputStream
			var er = proc.errorStream
			var sc_verbose = new Scanner(in)
			var sc_er = new Scanner(er)
			var streamString = new String
			var streamString_er = new String
			if (sc_verbose.hasNext())
				streamString = sc_verbose.useDelimiter("\\Z").next() ;
			if (sc_er.hasNext())
				streamString_er = sc_er.useDelimiter("\\Z").next() ;
			System.out.println(streamString)
			System.out.println(streamString_er)
		
			//  Generate DataBomb  ---
			var databombFilePath = genFolder.getFile(ddlFileName).projectRelativePath.removeFileExtension().addFileExtension("databomb");
			var databombFile = project.getFile(databombFilePath);
			var data_file_paths = ""
			for (i : 0..<dataFolder.members.size) {
				data_file_paths += " " + dataFolder.members.get(i).rawLocation.toOSString() 
			}
			//data_file_paths = data_file_paths.replaceAll("\\\\", "\\\\\\\\")
			var databombFileString = databombFile.rawLocation.toOSString()//.replaceAll("\\\\", "\\\\\\\\")
			var cmd = 'java -cp ' + compilerPathString + ' org/hpcc/HIPIE/commandline/CommandLineService -csv' + data_file_paths + ' -separator \\t -escape / -quote \\\" -lineseparator \\n -o ' + databombFileString 
			System.out.println(cmd)
			val proc_data = Runtime.getRuntime().exec(cmd) as Process ;
			in.close()
			er.close() 	
			in = proc_data.inputStream
			er = proc_data.errorStream
			sc_verbose = new Scanner(in)
			sc_er = new Scanner(er)
			streamString = new String
			streamString_er = new String
			if (sc_verbose.hasNext())
				streamString = sc_verbose.useDelimiter("\\Z").next() ;
			if (sc_er.hasNext())
					streamString_er = sc_er.useDelimiter("\\Z").next() ;
			System.out.println(streamString)
			System.out.println(streamString_er)
			
			var in_stream = new FileInputStream(ddlFile.rawLocation.toOSString());
			var streamString_ddl = new String
			var sc_in = new Scanner(in_stream)
			if (sc_in.hasNext())
				streamString_ddl = sc_in.useDelimiter("\\Z").next()
		
			streamString_ddl = streamString_ddl.replace("\n" , "")
			streamString_ddl = streamString_ddl.replace(" " , "")
			streamString_ddl = streamString_ddl.replace("\t" , "")
			streamString_ddl = streamString_ddl.replace("\r" , "")
			
			in_stream = new FileInputStream(databombFile.rawLocation.toOSString())
			var streamString_databomb = new String
			sc_in = new Scanner(in_stream)
			if (sc_in.hasNext())
				streamString_databomb = sc_in.useDelimiter("\\Z").next()
		
			streamString_databomb = streamString_databomb.replace("\n" , "")
			streamString_databomb = streamString_databomb.replace(" " , "")
			streamString_databomb = streamString_databomb.replace("\t" , "")
			streamString_databomb = streamString_databomb.replace("\r" , "")

			var url = new URL("platform:/plugin/org.xtext.hipie/vis_files/marsh.html")		
			var n = url.openConnection().getInputStream()
			var streamString_html = new String
			sc_in = new Scanner(n)
			if (sc_in.hasNext())
				streamString_html = sc_in.useDelimiter("\\Z").next() 
				
			streamString_html = streamString_html.replace("%_data_%" , streamString_databomb)
			streamString_html = streamString_html.replace("%_ddl_%" , streamString_ddl)
			fsa.generateFile('marshaller.html', 'HTML', streamString_html)

			genFolder.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor())
		}
		
		
		
		/*
		var ws_root = ResourcesPlugin.getWorkspace().getRoot();
		if (resource.URI.isPlatformResource()) 
		{
			var platformString = resource.URI.toPlatformString(true);
			var resourceFile = ws_root.findMember(platformString);
			var project = resourceFile.project;
			var dataFolder = project.getFolder("data");
         
			val filepath = resourceFile.rawLocation.toOSString 
			val filename = resourceFile.rawLocation.removeFileExtension.lastSegment;		
			val filepath_output = resourceFile.rawLocation.removeFileExtension + ".json" 
								
			var proc = Runtime.getRuntime().exec("java -cp ./libs/HIPIE.jar org/hpcc/HIPIE/commandline/CommandLineService -databomb " + filepath + " -o " + filepath_output + " -verbose") as Process ;
			var in = proc.inputStream
			var er = proc.errorStream
			var sc_verbose = new Scanner(in)
			var sc_er = new Scanner(er)
			var streamString = new String
			var streamString_er = new String
			if (sc_verbose.hasNext())
				streamString = sc_verbose.useDelimiter("\\Z").next() ;
			if (sc_er.hasNext())
				streamString_er = sc_er.useDelimiter("\\Z").next() ;
			System.out.println(streamString)
			System.out.println(streamString_er)	
		
			var in_stream = new FileInputStream(filepath_output);
			var streamString_ddl = new String
			var sc_in = new Scanner(in_stream)
			if (sc_in.hasNext())
				streamString_ddl = sc_in.useDelimiter("\\Z").next() ;
		
			in.close()
			er.close()
			in_stream.close() 
			sc_in.close()
			sc_verbose.close()
			sc_er.close()
		
			Files.delete(FileSystems.getDefault().getPath(filepath_output))
			fsa.generateFile(filename + '.json' , streamString_ddl)
			
			val cont_files = dataFolder.members
			val data_folder_path = dataFolder.rawLocation.toOSString
			var data_file_paths = ""
			for (i : 0..<cont_files.size)
				data_file_paths += " " +  cont_files.get(i).rawLocation.toOSString

			val output_filepath = data_folder_path + 'databomb.json'		
			val proc_data = Runtime.getRuntime().exec('java -cp ./libs/HIPIE.jar org/hpcc/HIPIE/commandline/CommandLineService -csv ' + data_file_paths + ' -separator \\t -escape / -quote \" -lineseparator \\n -o ' + output_filepath) as Process ;
			in.close()
			er.close() 	
			in = proc_data.inputStream
			er = proc_data.errorStream
			sc_verbose = new Scanner(in)
			sc_er = new Scanner(er)
			streamString = new String
			streamString_er = new String
			if (sc_verbose.hasNext())
				streamString = sc_verbose.useDelimiter("\\Z").next() ;
			if (sc_er.hasNext())
				streamString_er = sc_er.useDelimiter("\\Z").next() ;
			System.out.println(streamString)
			System.out.println(streamString_er)	
	
			in_stream = new FileInputStream(output_filepath);
			var streamString_data = new String
			sc_in = new Scanner(in_stream)
			if (sc_in.hasNext())
				streamString_data = sc_in.useDelimiter("\\Z").next() ;
		
			streamString_data = streamString_data.replace("\n" , "") ;
			streamString_data = streamString_data.replace(" " , "") ;
			streamString_data = streamString_data.replace("\t" , "") ;
			streamString_data = streamString_data.replace("\r" , "") ;
			Files.delete(FileSystems.getDefault().getPath(output_filepath))
			fsa.generateFile('databomb.json', 'Databomb', streamString_data)
		
			var url = new URL("platform:/plugin/org.xtext.hipie/vis_files/marsh.html")		
			var n = url.openConnection().getInputStream()
			var streamString_html = new String
			sc_in = new Scanner(n)
			if (sc_in.hasNext())
				streamString_html = sc_in.useDelimiter("\\Z").next()
			
			streamString_html = streamString_html.replace("%_data_%" , streamString_data)
			streamString_html = streamString_html.replace("%_ddl_%" , streamString_ddl)
			fsa.generateFile('Visualization_stub.html', 'HTML', streamString_html)
		}	
	

	*/
	}
}