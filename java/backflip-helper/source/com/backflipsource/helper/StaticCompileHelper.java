package com.backflipsource.helper;

import java.nio.file.Path;
import java.util.Collection;

public class StaticCompileHelper {

	private static CompileHelper instance;

	public static CompileHelper getInstance() {
		if (instance == null)
			instance = new DefaultCompileHelper();
		return instance;
	}

	public static Iterable<String> javaCompilerOptions(Path currentDir, Path sourceDir, Path classDir, Path javacDir) {
		return getInstance().javaCompilerOptions(currentDir, sourceDir, classDir, javacDir);
	}

	public static Collection<Path> collectSourceFiles(Path source) {
		return getInstance().collectSourceFiles(source);
	}

	public static void callJavaCompilerTask(Iterable<String> options, Collection<Path> sourceFiles) {
		getInstance().callJavaCompilerTask(options, sourceFiles);
	}

	public static void copyResourceFiles(Path source, Path javac) {
		getInstance().copyResourceFiles(source, javac);
	}

}
