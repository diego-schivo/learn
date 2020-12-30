package com.backflipsource.helper;

import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.stream.Stream;

public class StaticCompileHelper {

	private static CompileHelper instance;

	public static CompileHelper getInstance() {
		if (instance == null)
			instance = new DefaultCompileHelper();
		return instance;
	}

	public static Iterable<String> javaCompilerOptions(Path currentDir, Path classDir, Path javacDir, Path sourceDir) {
		return getInstance().javaCompilerOptions(currentDir, classDir, javacDir, sourceDir);
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

	public static void initClassDirectory(Path classDir, Stream<Entry<Path, URL>> stream) {
		getInstance().initClassDirectory(classDir, stream);
	}

}
