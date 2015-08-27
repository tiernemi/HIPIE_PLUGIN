package org.xtext.hipie.ui.contentassist;

// This is for adding the custom options to autocomplete 

public class CustomOptionFetcher {

	public CustomOptionFetcher() {
	}

	public static String[] fetchValidCustomOptions() {
		String[] results = { "test1", "test2" };
		return results;
	}
}
