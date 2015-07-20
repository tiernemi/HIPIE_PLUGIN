package org.xtext.hipie.custom;


import static org.eclipse.xtext.xbase.lib.CollectionLiterals.newHashSet;

import java.util.Set;

import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IOutputConfigurationProvider;
import org.eclipse.xtext.generator.OutputConfiguration;

public class OutputConfigs implements IOutputConfigurationProvider {

	  public static final String Databomb_OUTPUT = "Databomb";
	  public static final String HTML_OUTPUT = "HTML";
	@Override
	public Set<OutputConfiguration> getOutputConfigurations() {
		// TODO Auto-generated method stub
		return null;
	}

	  /**
	   * @return a set of {@link OutputConfiguration} available for the generator
	   */
	  
	  /*
	   public Set<OutputConfiguration> getOutputConfigurations() {
	    OutputConfiguration defaultOutput = new OutputConfiguration(IFileSystemAccess.DEFAULT_OUTPUT);
	    defaultOutput.setDescription("Output folder for generated src.");
	    defaultOutput.setOutputDirectory("./src-gen");
	    defaultOutput.setOverrideExistingResources(true);
	    defaultOutput.setCreateOutputDirectory(true);
	    defaultOutput.setCleanUpDerivedResources(true);
	    defaultOutput.setSetDerivedProperty(true);

	    OutputConfiguration Databomb = new OutputConfiguration(Databomb_OUTPUT);
	    Databomb.setDescription("Output folder for databombs.");
	    Databomb.setOutputDirectory("./data_gen");
	    Databomb.setOverrideExistingResources(true);
	    Databomb.setCreateOutputDirectory(true);
	    Databomb.setCleanUpDerivedResources(false);
	    Databomb.setSetDerivedProperty(false);
	    
	    OutputConfiguration htmlgen = new OutputConfiguration(HTML_OUTPUT);
	    htmlgen.setDescription("Output folder for HTML");
	    htmlgen.setOutputDirectory("./vis_gen");
	    htmlgen.setOverrideExistingResources(true);
	    htmlgen.setCreateOutputDirectory(true);
	    htmlgen.setCleanUpDerivedResources(false);
	    htmlgen.setSetDerivedProperty(false);    
	    
	    return newHashSet(defaultOutput, Databomb, htmlgen);   
	  }
	  */
	}