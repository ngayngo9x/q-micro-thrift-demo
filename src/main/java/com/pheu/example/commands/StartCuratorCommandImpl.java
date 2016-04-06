package com.pheu.example.commands;

import javax.inject.Inject;

import org.apache.curator.framework.CuratorFramework;

import com.pheu.app.commands.Command;

public class StartCuratorCommandImpl implements Command {

	private CuratorFramework curatorClient;
	
	@Inject
	public StartCuratorCommandImpl(CuratorFramework framework) {
		this.curatorClient = framework;
	}
	
	@Override
	public void execute() throws Exception {
		System.out.println("StartCuratorCommandImpl");
		curatorClient.start();
	}
	
	

}
