package com.pheu.example.providers;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.transport.TFramedTransport;

import com.google.inject.Provider;
import com.pheu.thrift.server.QThreadPoolServer;
import com.pheu.thrift.server.QThriftServer;

public class ThreadPoolServerProvider implements Provider<QThriftServer> {

	private TProcessorFactory processorFactory;
	private int port;
	
	@Inject
	public ThreadPoolServerProvider(TProcessorFactory processorFactory, @Named("port")int port) {
		this.processorFactory = processorFactory;
		this.port = port;
	}
	
	@Override
	public QThriftServer get() {
		return new QThreadPoolServer.Builder(processorFactory, port).transportFactory(new TFramedTransport.Factory()).build();
	}

}
