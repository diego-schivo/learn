package com.backflipsource.servlet;

import static com.backflipsource.helper.Helper.callJavaCompilerTask;
import static com.backflipsource.helper.Helper.collectSourceFiles;
import static com.backflipsource.helper.Helper.copyResourceFiles;
import static com.backflipsource.helper.Helper.initClassDirectory;
import static com.backflipsource.helper.Helper.javaCompilerOptions;
import static com.backflipsource.helper.Helper.linkedHashMap;
import static com.backflipsource.helper.Helper.safeStream;
import static com.backflipsource.helper.Helper.url;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

public class Compile {

	public static void main(String[] args) {
		new Compile();
	}

	public Compile() {
		Path currentDir = Paths.get(".").toAbsolutePath().normalize();

		Path classDir = currentDir.resolve("class");
		initClassDirectory(classDir, safeStream(linkedHashMap(Path.of("javax.servlet-api-3.1.0.jar"), url(
				"https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/3.1.0/javax.servlet-api-3.1.0.jar"))));

		Path javacDir = currentDir.resolve("javac");
		Path sourceDir = currentDir.resolve("source");
		Iterable<String> options = javaCompilerOptions(currentDir, classDir, javacDir, sourceDir);

		List<String> list = (List<String>) options;
		int index = list.indexOf("-classpath") + 1;
		list.set(index, list.get(index) + ":../backflip-helper/javac");

		Collection<Path> sourceFiles = collectSourceFiles(sourceDir);
		callJavaCompilerTask(options, sourceFiles);

		copyResourceFiles(sourceDir, javacDir);
	}
}
