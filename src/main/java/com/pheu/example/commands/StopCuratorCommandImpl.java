package com.pheu.example.commands;

import org.apache.curator.framework.CuratorFramework;

import com.pheu.app.commands.Command;

public class StopCuratorCommandImpl implements Command {

	private CuratorFramework framework;
	
	public StopCuratorCommandImpl(CuratorFramework framework) {
		this.framework = framework;
	}
	
	@Override
	public <E extends Exception> void execute() throws E {
		System.out.println("StopCuratorCommandImpl");
		framework.close();
	}

}
