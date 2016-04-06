package com.pheu.example.server.modules;

import com.facebook.nifty.guice.NiftyModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.pheu.example.TestThriftService;
import com.pheu.thrift.example.EchoHandler;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TCompactProtocol;

import com.facebook.nifty.core.NettyServerConfig;
import com.facebook.nifty.core.NettyServerConfigBuilder;
import com.facebook.nifty.core.NiftyBootstrap;
import com.facebook.nifty.core.ThriftServerDefBuilder;

public class NiftyModuleImpl extends NiftyModule {

	@Override
	protected void configureNifty() {
		//TMultiplexedProcessor mul = new TMultiplexedProcessor();
		//mul.registerProcessor("b", );
		bind().toInstance(new ThriftServerDefBuilder().listen(9092).limitQueuedResponsesPerConnection(2000).protocol(new TCompactProtocol.Factory()).withProcessor(new TestThriftService.Processor<>(new EchoHandler(9092))).build());
		withNettyServerConfig(NettyConfigProvider.class);
		bind(NiftyBootstrap.class).in(Singleton.class);
	}

	public static class NettyConfigProvider implements Provider<NettyServerConfig> {
		@Override
		public NettyServerConfig get() {
			NettyServerConfigBuilder nettyConfigBuilder = new NettyServerConfigBuilder();
			nettyConfigBuilder.getSocketChannelConfig().setTcpNoDelay(true);
			nettyConfigBuilder.getSocketChannelConfig().setConnectTimeoutMillis(5000);
			nettyConfigBuilder.getSocketChannelConfig().setKeepAlive(true);
			nettyConfigBuilder.setBossThreadCount(10);
			nettyConfigBuilder.setWorkerThreadCount(20);
			return nettyConfigBuilder.build();
		}
	}

}
