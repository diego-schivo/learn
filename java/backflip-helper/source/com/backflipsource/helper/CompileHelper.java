package com.backflipsource.helper;

import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.stream.Stream;

public interface CompileHelper {

	Iterable<String> javaCompilerOptions(Path currentDir, Path classDir, Path javacDir, Path sourceDir);

	Collection<Path> collectSourceFiles(Path source);

	void callJavaCompilerTask(Iterable<String> options, Collection<Path> sourceFiles);

	void copyResourceFiles(Path source, Path javac);

	void initClassDirectory(Path classDir, Stream<Entry<Path, URL>> stream);
}
