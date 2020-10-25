import static java.lang.Runtime.getRuntime;
import static java.lang.System.getenv;
import static java.lang.System.out;
import static java.lang.System.setProperty;
import static java.lang.Thread.sleep;
import static java.nio.file.Files.createTempDirectory;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.WebResourceSet;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.EmptyResourceSet;
import org.apache.catalina.webresources.StandardRoot;

public class App {

	public static void main(String[] args) {
		new App();
	}

	private static final String[] COMPILE_COMMAND = { "bash", "-c", "./compile" };
	private static final String COMPILE_DIR = "bin";
	private static final String COMPILE_PATH = "";
	private static final String ROOT_DIR = "";
	private static final String TOMCAT_BASE_DIR = "tomcat-base-dir";
	private static final String TOMCAT_DOC_BASE = "web";
	private static final String TOMCAT_DOC_BASE_DEFAULT = "tomcat-doc-base";
	private static final int TOMCAT_PORT_DEFAULT = 8080;
	private static final String WATCH_EXTENSION = ".java";
	private static final String WATCH_PATH = "src";
	private static final String WEB_APP_MOUNT = "/WEB-INF/classes";

	private Tomcat tomcat;

	public App() {
		ExecutorService watchExecutor = newSingleThreadExecutor();
		Future<Object> watchFuture = watchExecutor.submit(() -> {
			watch(Paths.get(WATCH_PATH), paths -> {
				boolean java = Arrays.stream(paths).anyMatch(path -> path.toString().endsWith(WATCH_EXTENSION));
				out.println(Arrays.asList(paths) + " " + java);
				if (java) {
					try {
						tomcat.stop();
						tomcat.getServer().destroy();
					} catch (LifecycleException e) {
						e.printStackTrace();
					}
				}
			});
			return null;
		});
		watchExecutor.shutdown();

		for (;;) {
			File dir = new File(Paths.get(COMPILE_PATH).toAbsolutePath().toString());
			try {
				Process compile = getRuntime().exec(COMPILE_COMMAND, null, dir);
				compile.waitFor();
				init();
				tomcat.start();
				tomcat.getServer().await();
				sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}

		stopExecutor(watchExecutor, watchFuture);
	}

	private void init() throws Exception {
		setProperty("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE", "true");

		tomcat = new Tomcat();
		Path tempPath = createTempDirectory(TOMCAT_BASE_DIR);
		tomcat.setBaseDir(tempPath.toString());

		int port;
		try {
			port = Integer.valueOf(getenv("PORT"));
		} catch (NumberFormatException e) {
			port = TOMCAT_PORT_DEFAULT;
		}
		tomcat.setPort(port);

		File rootDir = new File(Paths.get(ROOT_DIR).toAbsolutePath().toString());
		String rootPath = rootDir.getAbsolutePath();
		out.println("rootPath " + rootPath);

		File docBaseDir = new File(rootPath, TOMCAT_DOC_BASE);
		if (!docBaseDir.exists()) {
			docBaseDir = createTempDirectory(TOMCAT_DOC_BASE_DEFAULT).toFile();
		}
		String docBasePath = docBaseDir.getAbsolutePath();
		out.println("docBasePath " + docBasePath);

		StandardContext context = (StandardContext) tomcat.addWebapp("", docBasePath);
		context.setParentClassLoader(App.class.getClassLoader());

		WebResourceRoot resourceRoot = new StandardRoot(context);
		File classesDir = new File(rootPath, COMPILE_DIR);
		String classesPath = classesDir.getAbsolutePath();
		out.println("classesPath " + classesPath);
		WebResourceSet resourceSet;
		if (classesDir.exists()) {
			resourceSet = new DirResourceSet(resourceRoot, WEB_APP_MOUNT, classesPath, "/");
		} else {
			resourceSet = new EmptyResourceSet(resourceRoot);
		}
		resourceRoot.addPreResources(resourceSet);
		context.setResources(resourceRoot);
	}

	@SuppressWarnings("unchecked")
	private void watch(Path path, Consumer<Path[]> consumer) throws IOException {
		FileSystem fileSystem = FileSystems.getDefault();
		WatchService watchService = fileSystem.newWatchService();
		try {
			System.out.println("path " + path.toAbsolutePath());
			path.register(watchService, ENTRY_MODIFY);

			for (;;) {
				WatchKey watchKey;
				try {
					watchKey = watchService.take();
				} catch (InterruptedException e) {
					break;
				}

				Path[] filenames = watchKey.pollEvents().stream().filter(event -> event.kind() != OVERFLOW)
						.map(event -> ((WatchEvent<Path>) event).context()).toArray(Path[]::new);
				consumer.accept(filenames);

				boolean valid = watchKey.reset();
				if (!valid) {
					break;
				}
			}
		} finally {
			watchService.close();
		}
		// out.println(currentThread().getName() + " end");
	}

	private void stopExecutor(ExecutorService executor, Future<?>... futures) {
		if (executor == null) {
			return;
		}
		if (futures != null) {
			for (Future<?> future : futures) {
				future.cancel(true);
			}
		}
		try {
			executor.awaitTermination(1, SECONDS);
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
		executor.shutdownNow();
	}
}
