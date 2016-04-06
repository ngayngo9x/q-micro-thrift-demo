package com.pheu.example.providers;

import javax.inject.Inject;

import org.apache.thrift.TProcessorFactory;

import com.google.inject.Provider;
import com.pheu.common.PropertyLoader;
import com.pheu.thrift.server.QThreadSelectorServer;
import com.pheu.thrift.server.QThriftServer;

public class ThreadSelectorServerProvider implements Provider<QThriftServer> {

	private TProcessorFactory processorFactory;
	private PropertyLoader propsLoader;
	
	@Inject
	public ThreadSelectorServerProvider(TProcessorFactory processorFactory, PropertyLoader propsLoader) {
		this.processorFactory = processorFactory;
		this.propsLoader = propsLoader;
	}
	
	@Override
	public QThriftServer get() {
		return new QThreadSelectorServer.Builder(processorFactory, propsLoader.getInt("port"))
				.selectorSize(2).acceptQueueSizePerThread(10000)
				.workerThreadSize(20)
				.build();
	}

}
