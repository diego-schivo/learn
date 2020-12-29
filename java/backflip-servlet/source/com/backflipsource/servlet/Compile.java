package com.backflipsource.servlet;

import static com.backflipsource.helper.Helper.callJavaCompilerTask;
import static com.backflipsource.helper.Helper.collectSourceFiles;
import static com.backflipsource.helper.Helper.copyResourceFiles;
import static com.backflipsource.helper.Helper.extractArchive;
import static com.backflipsource.helper.Helper.javaCompilerOptions;
import static com.backflipsource.helper.Helper.linkedHashMap;
import static com.backflipsource.helper.Helper.string;
import static com.backflipsource.helper.Helper.substringAfterLast;
import static com.backflipsource.helper.Helper.unsafeRun;
import static com.backflipsource.helper.Helper.url;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.exists;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Map.entry;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.backflipsource.helper.HelperGenerator;
import com.backflipsource.helper.SourceHelperGenerator;
import com.backflipsource.helper.SourceStaticHelpersGenerator;
import com.backflipsource.helper.StaticHelpersGenerator;

public class Compile {

	public static void main(String[] args) {
		new Compile();
	}

	public Compile() {
		Path currentDir = Paths.get(".").toAbsolutePath().normalize();

		Path sourceDir = currentDir.resolve("source");
		Path classDir = currentDir.resolve("class");
		Path javacDir = currentDir.resolve("javac");

		unsafeRun(() -> Files.createDirectories(classDir));
		Map<String, String> map = linkedHashMap(entry("javax.servlet-api-3.1.0.jar",
				"https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/3.1.0/javax.servlet-api-3.1.0.jar"));
		map.entrySet().forEach(entry -> {
			URL url = url(entry.getValue());
			Path target = classDir.resolve(entry.getKey());
			download(url, target);
		});

		StaticHelpersGenerator staticGenerator = new SourceStaticHelpersGenerator();
		staticGenerator.generateStaticHelpers(sourceDir, "com.backflipsource.servlet");

		HelperGenerator generator = new SourceHelperGenerator();
		generator.generateHelper(sourceDir, "com.backflipsource.servlet");

		Iterable<String> options = javaCompilerOptions(currentDir, sourceDir, classDir, javacDir);

		List<String> list = (List<String>) options;
		int index = list.indexOf("-classpath") + 1;
		list.set(index, list.get(index) + ":../backflip-helper/javac");

		Collection<Path> sourceFiles = collectSourceFiles(sourceDir);
		callJavaCompilerTask(options, sourceFiles);

		copyResourceFiles(sourceDir, javacDir);
	}

	private void download(URL url, Path target) {
		if (exists(target)) {
			return;
		}

		unsafeRun(() -> {
			try (InputStream in = url.openStream()) {
				copy(in, target, REPLACE_EXISTING);
			}
		});

		if (substringAfterLast(string(target.getFileName()), ".").toLowerCase().equals("zip")) {
			extractArchive(target, target.getParent());
		}
	}
}
