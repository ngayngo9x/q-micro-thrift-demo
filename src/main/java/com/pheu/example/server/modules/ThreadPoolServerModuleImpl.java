package com.pheu.example.server.modules;

import org.apache.thrift.TProcessorFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.pheu.example.providers.ProcessorFactoryProvider;
import com.pheu.example.providers.ThreadPoolServerProvider;
import com.pheu.thrift.server.QThriftServer;

public class ThreadPoolServerModuleImpl extends AbstractModule {

	@Override
	protected void configure() {
		bind(Integer.class).annotatedWith(Names.named("port")).toInstance(9092);
		bind(TProcessorFactory.class).toProvider(ProcessorFactoryProvider.class).in(Singleton.class);
		bind(QThriftServer.class).toProvider(ThreadPoolServerProvider.class).in(Singleton.class);
	}

}
