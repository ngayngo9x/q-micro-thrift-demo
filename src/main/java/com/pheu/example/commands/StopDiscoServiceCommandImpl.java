package com.pheu.example.commands;

import com.librato.disco.DiscoService;
import com.pheu.app.commands.Command;

public class StopDiscoServiceCommandImpl implements Command {

	private DiscoService discoClient;
	
	public StopDiscoServiceCommandImpl(DiscoService discoClient) {
		this.discoClient = discoClient;
	}
	
	@Override
	public void execute() throws Exception {
		System.out.println("StopDiscoServiceCommandImpl");
		discoClient.stop();
	}

}
