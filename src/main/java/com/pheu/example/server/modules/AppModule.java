package com.pheu.example.server.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.pheu.common.PropertyLoader;
import com.pheu.example.providers.PropertiesLoaderProvider;

public class AppModule extends AbstractModule {

	private String filename;
	
	public AppModule(String filename) {
		this.filename = filename;
	}
	
	@Override
	protected void configure() {
		bind(String.class).annotatedWith(Names.named("filename")).toInstance(filename);
		bind(PropertyLoader.class).toProvider(PropertiesLoaderProvider.class).in(Singleton.class);
	}
}
