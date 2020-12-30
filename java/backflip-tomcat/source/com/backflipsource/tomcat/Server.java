package com.backflipsource.tomcat;

import static com.backflipsource.helper.Helper.safeStream;
import static com.backflipsource.helper.Helper.startExecutorService;
import static com.backflipsource.helper.Helper.stopExecutorService;
import static com.backflipsource.helper.Helper.watchDirectories;
import static com.backflipsource.tomcat.Helper.initTomcat;
import static java.lang.Runtime.getRuntime;
import static java.lang.System.getenv;
import static java.lang.Thread.sleep;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class Server {

	private static final String[] COMPILE_COMMAND = { "bash", "-c", "./compile" };
	private static final String COMPILE_DIR = "bin";
	private static final String COMPILE_PATH = "";
	private static final String ROOT_DIR = "";
	private static final String WATCH_EXTENSION = ".java";
	private static final String WATCH_PATH = "src";
	private static final int TOMCAT_PORT_DEFAULT = 8081;

	private Tomcat tomcat;

	public Server() {
		ExecutorService watchExecutor = newSingleThreadExecutor();
		Future<?> watchFuture = startExecutorService(watchExecutor, () -> {
			watchDirectories(Paths.get(WATCH_PATH), this::stopTomcat);
			return null;
		})[0];

		try {
			while (build() && serve()) {
				sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		stopExecutorService(watchExecutor, watchFuture);
	}

	protected void stopTomcat(Path... paths) {
		boolean java = safeStream(paths).anyMatch(path -> path.toString().endsWith(WATCH_EXTENSION));
		// out.println(safeList(paths) + " " + java);
		if (!java) {
			return;
		}
		try {
			tomcat.stop();
			tomcat.getServer().destroy();
		} catch (LifecycleException e) {
			e.printStackTrace();
		}
	}

	protected boolean build() {
		Process process;
		try {
			File dir = new File(Paths.get(COMPILE_PATH).toAbsolutePath().toString());
			process = getRuntime().exec(COMPILE_COMMAND, null, dir);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		try {
			process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	protected boolean serve() {
		tomcat = new Tomcat();

		int port;
		try {
			port = Integer.valueOf(getenv("PORT"));
		} catch (NumberFormatException e) {
			port = TOMCAT_PORT_DEFAULT;
		}

		initTomcat(tomcat, port, ROOT_DIR, COMPILE_DIR);
		try {
			tomcat.start();
		} catch (LifecycleException e) {
			e.printStackTrace();
			return false;
		}
		tomcat.getServer().await();
		return true;
	}
}
