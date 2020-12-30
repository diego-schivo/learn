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
		StaticHelpersGenerator staticGenerator = new SourceStaticHelpersGenerator();
		staticGenerator.generateStaticHelpers(sourceDir, "com.backflipsource.helper");

		HelperGenerator generator = new SourceHelperGenerator();
		generator.generateHelper(sourceDir, "com.backflipsource.helper");

		Path javacDir = currentDir.resolve("javac");

		Iterable<String> options = javaCompilerOptions(currentDir, null, javacDir, sourceDir);
		Collection<Path> sourceFiles = collectSourceFiles(sourceDir);
		callJavaCompilerTask(options, sourceFiles);

		copyResourceFiles(sourceDir, javacDir);
	}
}
