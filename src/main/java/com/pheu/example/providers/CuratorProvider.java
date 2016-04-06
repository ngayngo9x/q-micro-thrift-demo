package com.pheu.example.providers;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.google.inject.Provider;

public class CuratorProvider implements Provider<CuratorFramework> {

	@Override
	public CuratorFramework get() {
		CuratorFramework framework = CuratorFrameworkFactory.builder().connectionTimeoutMs(1000)
				.connectString("localhost:2181").retryPolicy(new ExponentialBackoffRetry(1000, 5)).build();
		return framework;
	}

}
