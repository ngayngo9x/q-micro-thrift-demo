package com.pheu.example.server.modules;


import org.apache.thrift.TProcessorFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.pheu.example.providers.ProcessorFactoryProvider;
import com.pheu.example.providers.ThreadSelectorServerProvider;
import com.pheu.thrift.server.QThriftServer;

public class ThreadSelectorServerModuleImpl extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(TProcessorFactory.class).toProvider(ProcessorFactoryProvider.class).in(Singleton.class);
		bind(QThriftServer.class).toProvider(ThreadSelectorServerProvider.class).in(Singleton.class);
	}

}
