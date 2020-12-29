package com.backflipsource.helper;

import java.nio.file.Path;
import java.util.Collection;

public interface CompileHelper {

	Iterable<String> javaCompilerOptions(Path currentDir, Path sourceDir, Path classDir, Path javacDir);

	Collection<Path> collectSourceFiles(Path source);

	void callJavaCompilerTask(Iterable<String> options, Collection<Path> sourceFiles);

	void copyResourceFiles(Path source, Path javac);
}
