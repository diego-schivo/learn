package com.backflipsource.helper;

import static com.backflipsource.helper.StaticLangHelper.joinStrings;
import static com.backflipsource.helper.StaticLangHelper.substringAfterLast;
import static com.backflipsource.helper.StaticNioHelper.acceptDirectoryEntries;
import static java.lang.Integer.MAX_VALUE;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.find;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Arrays.asList;
import static javax.tools.ToolProvider.getSystemJavaCompiler;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

public class Compile {

	public static void main(String[] args) throws IOException {
		new Compile();
	}

	public Compile() throws IOException {
		Path path = Paths.get(".").toAbsolutePath().normalize();

		Path source = path.resolve("source");
		Path class1 = path.resolve("class");
		Path javac = path.resolve("javac");

		createDirectories(class1);

		Collection<Path> paths = new ArrayList<>();
		try (Stream<Path> find = find(source, MAX_VALUE, (path2, attributes) -> attributes.isRegularFile())) {
			find.forEach(path2 -> {
				String name = path2.getFileName().toString();
				if (substringAfterLast(name, ".").toLowerCase().equals("java")) {
					paths.add(path2);
				} else if (!name.startsWith(".")) {
					Path resolve = javac.resolve(source.relativize(path2).toString());
					try {
						createDirectories(resolve.getParent());
						copy(path2, resolve, REPLACE_EXISTING);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			});
		}

		JavaCompiler compiler = getSystemJavaCompiler();
		try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
			Collection<Path> paths2 = new ArrayList<>();
			acceptDirectoryEntries(class1, "*.jar", paths2::add);
			String classpath = joinStrings(paths2.stream().map(path2 -> path.relativize(path2).toString()), ":");
			Iterable<String> options = asList("-classpath", classpath, "-d", path.relativize(javac).toString(),
					"-sourcepath", path.relativize(source).toString());
			Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromPaths(paths);
			compiler.getTask(null, fileManager, null, options, null, compilationUnits).call();
		}

		StaticHelpersWriter staticClassesWriter = new SourceStaticHelpersWriter();
		staticClassesWriter.writeStaticHelpers(source, "com.backflipsource.helper");
	}
}
