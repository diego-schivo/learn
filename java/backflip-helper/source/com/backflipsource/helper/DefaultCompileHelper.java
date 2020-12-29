package com.backflipsource.helper;

import static com.backflipsource.helper.Helper.acceptDirectoryEntries;
import static com.backflipsource.helper.Helper.joinStrings;
import static com.backflipsource.helper.Helper.safeStream;
import static com.backflipsource.helper.Helper.string;
import static com.backflipsource.helper.Helper.substringAfterLast;
import static com.backflipsource.helper.Helper.unsafeGet;
import static com.backflipsource.helper.Helper.unsafeRun;
import static java.lang.Integer.MAX_VALUE;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.find;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Arrays.asList;
import static javax.tools.ToolProvider.getSystemJavaCompiler;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

public class DefaultCompileHelper implements CompileHelper {

	@Override
	public Iterable<String> javaCompilerOptions(Path currentDir, Path sourceDir, Path classDir, Path javacDir) {
		Collection<Path> classFiles = new ArrayList<>();
		acceptDirectoryEntries(classDir, "*.jar", classFiles::add);
		String classpath = joinStrings(safeStream(classFiles).map(path -> string(currentDir.relativize(path))), ":");
		Iterable<String> options = asList("-classpath", classpath, "-d", string(currentDir.relativize(javacDir)),
				"-sourcepath", string(currentDir.relativize(sourceDir)));
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
}
