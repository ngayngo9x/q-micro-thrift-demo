package com.pheu.example.server;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.facebook.nifty.core.NiftyBootstrap;
import com.google.inject.Module;
import com.librato.disco.DiscoService;
import com.pheu.app.AbstractApplication;
import com.pheu.app.AppLauncher;
import com.pheu.app.commands.Command;
import com.pheu.app.core.ShutdownRegistry;
import com.pheu.app.core.ShutdownStage;
import com.pheu.example.commands.StopCuratorCommandImpl;
import com.pheu.example.commands.StopDiscoServiceCommandImpl;
import com.pheu.example.commands.StopNiftyServerCommand;
import com.pheu.example.server.modules.NiftyModuleImpl;
import com.pheu.example.server.modules.ServiceDiscoverModule;

public class Nifty3ServerApp extends AbstractApplication {

	private static final Logger log = LoggerFactory.getLogger(Nifty3ServerApp.class);
	private volatile boolean running = true;

	@Inject
	private CuratorFramework curatorClient;
	@Inject
	private DiscoService discoClient;

	@Inject
	@ShutdownStage
	private Command shutdownCommand;

	@Inject
	private NiftyBootstrap boostrap;
	
	@Override
	public void run() {
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		
		ShutdownRegistry shutdownRegistry = ((ShutdownRegistry) shutdownCommand);
		shutdownRegistry.addAction(new StopDiscoServiceCommandImpl(discoClient));
		shutdownRegistry.addAction(new StopCuratorCommandImpl(curatorClient));
		shutdownRegistry.addAction(new StopNiftyServerCommand(boostrap));

		int port = 9092;

		try {
			discoClient.start("localhost", port, true, "".getBytes());
		} catch (Exception e) {
			log.error("Register fail: " + e.getMessage());
			System.exit(0);
		}

		try {
			boostrap.start();
			while (running) {
				Thread.sleep(10000);
				log.warn("Running...");
				Thread.yield();
				Thread.yield();
				Thread.yield();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			System.exit(0);
		}
	}

	@Override
	public Iterable<? extends Module> getModules() {
		List<Module> modules = new ArrayList<>();

		modules.add(new ServiceDiscoverModule());
		modules.add(new NiftyModuleImpl());

		return modules;
	}

	public static void main(String[] args) {

		AppLauncher.launch(Nifty3ServerApp.class, args);

	}

	// public void startServer(int port) throws Exception {
	//
	// ThriftServerDef def = new ThriftServerDefBuilder().listen(port)
	// .limitQueuedResponsesPerConnection(50)
	// .withProcessor(new TestThriftService.Processor<>(new EchoHandler(port)))
	// .speaks(new TCompactProtocol.Factory()).build();
	//
	// QNetty4ThriftServer server = new
	// QNetty4ThriftServer.Builder(def).workerSize(20).bossSize(10).backLog(128)
	// .logLevel(LogLevel.WARN).keepAlive(true).build();
	//
	// try {
	// discoClient.start("localhost", port, true, "".getBytes());
	// } catch (Exception e) {
	// log.error("Register fail: " + e.getMessage());
	// System.exit(0);
	// }
	//
	// server.start();
	//
	// }

}
