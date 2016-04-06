package com.pheu.example.providers;

import javax.inject.Inject;

import org.apache.curator.framework.CuratorFramework;

import com.google.inject.Provider;
import com.librato.disco.DiscoService;

public class DiscoProvider implements Provider<DiscoService> {

	private CuratorFramework curatorClient;
	
	@Inject
	public DiscoProvider(CuratorFramework curatorClient) {
		this.curatorClient = curatorClient;
	}
	
	@Override
	public DiscoService get() {
		return new DiscoService(curatorClient, "qthriftserverice");
	}

}
