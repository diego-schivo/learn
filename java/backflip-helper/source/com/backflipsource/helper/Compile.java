package com.backflipsource.helper;

import static com.backflipsource.helper.Helper.callJavaCompilerTask;
import static com.backflipsource.helper.Helper.collectSourceFiles;
import static com.backflipsource.helper.Helper.copyResourceFiles;
import static com.backflipsource.helper.Helper.javaCompilerOptions;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

public class Compile {

	public static void main(String[] args) {
		new Compile();
	}

	public Compile() {
		Path currentDir = Paths.get(".").toAbsolutePath().normalize();

		Path sourceDir = currentDir.resolve("source");
		Path classDir = currentDir.resolve("class");
		Path javacDir = currentDir.resolve("javac");

		StaticHelpersGenerator staticGenerator = new SourceStaticHelpersGenerator();
		staticGenerator.generateStaticHelpers(sourceDir, "com.backflipsource.helper");

		HelperGenerator generator = new SourceHelperGenerator();
		generator.generateHelper(sourceDir, "com.backflipsource.helper");

		Iterable<String> options = javaCompilerOptions(currentDir, sourceDir, classDir, javacDir);
		Collection<Path> sourceFiles = collectSourceFiles(sourceDir);
		callJavaCompilerTask(options, sourceFiles);

		copyResourceFiles(sourceDir, javacDir);
	}
}
