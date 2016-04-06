package com.pheu.example.commands;

import com.facebook.nifty.core.NiftyBootstrap;
import com.pheu.app.commands.Command;

public class StopNiftyServerCommand implements Command {

	private NiftyBootstrap boostrap;
	
	public StopNiftyServerCommand(NiftyBootstrap boostrap) {
		this.boostrap = boostrap;
	}
	
	@Override
	public <E extends Exception> void execute() throws E {
		boostrap.stop();
	}

}
