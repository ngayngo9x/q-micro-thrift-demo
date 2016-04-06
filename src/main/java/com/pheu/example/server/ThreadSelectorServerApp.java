package com.pheu.example.server;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameter;
import com.google.inject.Module;
import com.librato.disco.DiscoService;
import com.pheu.app.AbstractApplication;
import com.pheu.app.AppLauncher;
import com.pheu.app.commands.Command;
import com.pheu.app.core.ShutdownRegistry;
import com.pheu.app.core.ShutdownStage;
import com.pheu.common.PropertyLoader;
import com.pheu.example.commands.StopCuratorCommandImpl;
import com.pheu.example.commands.StopDiscoServiceCommandImpl;
import com.pheu.example.commands.StopThriftServerCommand;
import com.pheu.example.server.modules.AppModule;
import com.pheu.example.server.modules.ServiceDiscoverModule;
import com.pheu.example.server.modules.ThreadSelectorServerModuleImpl;
import com.pheu.thrift.server.QThriftServer;

public class ThreadSelectorServerApp extends AbstractApplication {

	@Parameter(names="--fileConfig", description="Config the config file app")
	private String FILE_CONFIG = "config1Names.properties";
	
	private static final Logger log = LoggerFactory.getLogger(ThreadSelectorServerApp.class);

	@Inject
	private CuratorFramework curatorClient;
	@Inject
	private DiscoService discoClient;

	@Inject
	@ShutdownStage
	private Command shutdownCommand;

	@Inject
	private QThriftServer server;
	
	@Inject
	private PropertyLoader config;
	
	@Override
	public void run() {
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		
		ShutdownRegistry shutdownRegistry = ((ShutdownRegistry) shutdownCommand);
		shutdownRegistry.addAction(new StopDiscoServiceCommandImpl(discoClient));
		shutdownRegistry.addAction(new StopCuratorCommandImpl(curatorClient));
		shutdownRegistry.addAction(new StopThriftServerCommand(server));

		try {
			discoClient.start("localhost", config.getInt("port"), true, "".getBytes());
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

		modules.add(new AppModule(FILE_CONFIG));
		modules.add(new ServiceDiscoverModule());
		modules.add(new ThreadSelectorServerModuleImpl());

		return modules;
	}

	public static void main(String[] args) {
		AppLauncher.launch(ThreadSelectorServerApp.class, args);

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

	public void startServer(int port) throws Exception {

		try {
			discoClient.start("localhost", port, true, "".getBytes());
		} catch (Exception e) {
			log.error("Register fail: " + e.getMessage());
			System.exit(0);
		}

		QNetty3ThriftServer.start(port);

	}

}
