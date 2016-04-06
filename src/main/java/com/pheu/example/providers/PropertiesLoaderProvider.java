package com.pheu.example.providers;


import javax.inject.Inject;
import javax.inject.Named;

import com.google.inject.Provider;
import com.pheu.common.PropertyLoader;

public class PropertiesLoaderProvider implements Provider<PropertyLoader> {

	private String filename;
	
	@Inject
	public PropertiesLoaderProvider(@Named("filename") String filename) {
		this.filename = filename;
	}
	
	@Override
	public PropertyLoader get() {
		return new PropertyLoader(filename);
	}

}
