package com.backflipsource.helper;

import static com.backflipsource.helper.Helper.acceptDirectoryEntries;
import static com.backflipsource.helper.Helper.downloadFile;
import static com.backflipsource.helper.Helper.joinStrings;
import static com.backflipsource.helper.Helper.safeStream;
import static com.backflipsource.helper.Helper.string;
import static com.backflipsource.helper.Helper.substringAfterLast;
import static com.backflipsource.helper.Helper.unsafeGet;
import static com.backflipsource.helper.Helper.unsafeRun;
import static java.lang.Integer.MAX_VALUE;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.find;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static javax.tools.ToolProvider.getSystemJavaCompiler;

import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Stream;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

public class DefaultCompileHelper implements CompileHelper {

	@Override
	public Iterable<String> javaCompilerOptions(Path currentDir, Path classDir, Path javacDir, Path sourceDir) {
		List<String> options = new ArrayList<>();

		if (classDir != null) {
			Collection<Path> classFiles = new ArrayList<>();
			acceptDirectoryEntries(classDir, "*.jar", classFiles::add);
			String classpath = joinStrings(safeStream(classFiles).map(path -> string(currentDir.relativize(path))),
					":");
			options.add("-classpath");
			options.add(classpath);
		}

		if (javacDir != null) {
			options.add("-d");
			options.add(string(currentDir.relativize(javacDir)));
		}

		if (sourceDir != null) {
			options.add("-sourcepath");
			options.add(string(currentDir.relativize(sourceDir)));
		}

		return options;
	}

	@Override
	public Collection<Path> collectSourceFiles(Path source) {
		Collection<Path> paths = new ArrayList<>();
		try (Stream<Path> find = unsafeGet(
				() -> find(source, MAX_VALUE, (path2, attributes) -> attributes.isRegularFile()))) {
			find.forEach(path2 -> {
				String name = path2.getFileName().toString();
				if (!substringAfterLast(name, ".").toLowerCase().equals("java")) {
					return;
				}
				paths.add(path2);
			});
		}
		return paths;
	}

	@Override
	public void callJavaCompilerTask(Iterable<String> options, Collection<Path> sourceFiles) {
		JavaCompiler compiler = getSystemJavaCompiler();
		unsafeRun(() -> {
			try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
				Iterable<? extends JavaFileObject> compilationUnits = fileManager
						.getJavaFileObjectsFromPaths(sourceFiles);
				compiler.getTask(null, fileManager, null, options, null, compilationUnits).call();
			}
		});
	}

	@Override
	public void copyResourceFiles(Path source, Path javac) {
		try (Stream<Path> find = unsafeGet(
				() -> find(source, MAX_VALUE, (path2, attributes) -> attributes.isRegularFile()))) {
			find.forEach(path2 -> {
				String name = path2.getFileName().toString();
				if (substringAfterLast(name, ".").toLowerCase().equals("java") || name.startsWith(".")) {
					return;
				}
				Path resolve = javac.resolve(source.relativize(path2).toString());
				unsafeRun(() -> {
					createDirectories(resolve.getParent());
					copy(path2, resolve, REPLACE_EXISTING);
				});
			});
		}
	}

	@Override
	public void initClassDirectory(Path classDir, Stream<Entry<Path, URL>> stream) {
		unsafeRun(() -> createDirectories(classDir));

		stream.forEach(entry -> {
			if (exists(classDir.resolve(entry.getKey()))) {
				return;
			}
			downloadFile(entry.getValue(), classDir);
		});
	}
}
