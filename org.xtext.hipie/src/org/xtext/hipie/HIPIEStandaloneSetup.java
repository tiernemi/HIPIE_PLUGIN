/*
 * generated by Xtext
 */
package org.xtext.hipie;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class HIPIEStandaloneSetup extends HIPIEStandaloneSetupGenerated{

	public static void doSetup() {
		new HIPIEStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

