package com.pheu.example.server;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Module;
import com.librato.disco.DiscoService;
import com.pheu.app.AbstractApplication;
import com.pheu.app.AppLauncher;
import com.pheu.app.commands.Command;
import com.pheu.app.core.ShutdownRegistry;
import com.pheu.app.core.ShutdownStage;
import com.pheu.example.commands.StopCuratorCommandImpl;
import com.pheu.example.commands.StopDiscoServiceCommandImpl;
import com.pheu.example.commands.StopThriftServerCommand;
import com.pheu.example.server.modules.ServiceDiscoverModule;
import com.pheu.example.server.modules.ThreadPoolServerModuleImpl;
import com.pheu.thrift.server.QThriftServer;

public class ThreadPoolServerApp extends AbstractApplication {

	private static final Logger log = LoggerFactory.getLogger(ThreadPoolServerApp.class);

	@Inject
	private CuratorFramework curatorClient;
	@Inject
	private DiscoService discoClient;

	@Inject
	@ShutdownStage
	private Command shutdownCommand;

	@Inject
	private QThriftServer server;
	
	@Override
	public void run() {
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		
		ShutdownRegistry shutdownRegistry = ((ShutdownRegistry) shutdownCommand);
		shutdownRegistry.addAction(new StopDiscoServiceCommandImpl(discoClient));
		shutdownRegistry.addAction(new StopCuratorCommandImpl(curatorClient));
		shutdownRegistry.addAction(new StopThriftServerCommand(server));

		int port = 9092;

		try {
			discoClient.start("localhost", port, true, "".getBytes());
		} catch (Exception e) {
			log.error("Register fail: " + e.getMessage());
			System.exit(0);
		}

		try {
			server.start();
		} catch (Exception e) {
			log.error(e.getMessage());
			System.exit(0);
		}
	}

	@Override
	public Iterable<? extends Module> getModules() {
		List<Module> modules = new ArrayList<>();

		modules.add(new ServiceDiscoverModule());
		modules.add(new ThreadPoolServerModuleImpl());

		return modules;
	}

	public static void main(String[] args) {

		AppLauncher.launch(ThreadPoolServerApp.class, args);

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
