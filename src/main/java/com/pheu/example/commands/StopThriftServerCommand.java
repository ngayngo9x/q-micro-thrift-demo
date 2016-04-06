package com.pheu.example.commands;

import com.pheu.app.commands.Command;
import com.pheu.thrift.server.QThriftServer;

public class StopThriftServerCommand implements Command {

	private QThriftServer server;
	
	public StopThriftServerCommand(QThriftServer server) {
		this.server = server;
	}
	
	@Override
	public void execute() throws Exception {
		this.server.close();
	}

}
