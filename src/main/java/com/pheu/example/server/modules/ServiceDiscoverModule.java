package com.pheu.example.server.modules;

import static com.pheu.app.modules.LifecycleModule.bindStartupAction;

import org.apache.curator.framework.CuratorFramework;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.librato.disco.DiscoService;
import com.pheu.example.commands.StartCuratorCommandImpl;
import com.pheu.example.providers.CuratorProvider;
import com.pheu.example.providers.DiscoProvider;

public class ServiceDiscoverModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CuratorFramework.class).toProvider(CuratorProvider.class).in(Singleton.class);
		bind(DiscoService.class).toProvider(DiscoProvider.class).in(Singleton.class);
	
		bindStartupAction(binder(), StartCuratorCommandImpl.class);
	}

}
