package com.pheu.example.providers;

import javax.inject.Inject;

import org.apache.thrift.TProcessorFactory;

import com.google.inject.Provider;
import com.pheu.common.PropertyLoader;
import com.pheu.example.TestThriftService;
import com.pheu.thrift.example.EchoHandler;

public class ProcessorFactoryProvider implements Provider<TProcessorFactory> {

	private PropertyLoader propsLoader;
	
	@Inject
	public ProcessorFactoryProvider(PropertyLoader propsLoader) {
		this.propsLoader = propsLoader;
	}
	
	@Override
	public TProcessorFactory get() {
		return new TProcessorFactory(
				new TestThriftService.Processor<>(
					new EchoHandler(propsLoader.getInt("port"))
				));
	}

}
