package com.backflipsource;

import static com.backflipsource.Helpers.joinStrings;
import static java.lang.Integer.MAX_VALUE;
import static java.nio.file.FileSystems.newFileSystem;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.find;
import static java.nio.file.Files.newDirectoryStream;
import static java.nio.file.Files.walkFileTree;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Arrays.asList;
import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;
import static javax.tools.ToolProvider.getSystemJavaCompiler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

public class Compile {

	public static void main(String[] args) throws IOException {
		new Compile();
	}

//	protected LangHelper langHelper = new DefaultLangHelper();
//
//	protected NetHelper netHelper = new DefaultNetHelper();
//
//	protected NioHelper nioHelper = new DefaultNioHelper();
//
//	protected UtilHelper utilHelper = new DefaultUtilHelper();

	@SuppressWarnings("unchecked")
	public Compile() throws IOException {
		Path path = Paths.get(".").toAbsolutePath().normalize();

		Path source = path.resolve("source");
		Path class1 = path.resolve("class");
		Path javac = path.resolve("javac");

		createDirectories(class1);

		Map<String, String> map = linkedHashMap(entry("taglibs-standard-impl-1.2.5.jar",
				"https://downloads.apache.org/tomcat/taglibs/taglibs-standard-1.2.5/taglibs-standard-impl-1.2.5.jar"),
				entry("taglibs-standard-spec-1.2.5.jar",
						"https://downloads.apache.org/tomcat/taglibs/taglibs-standard-1.2.5/taglibs-standard-spec-1.2.5.jar"),
				entry("tomcat-embed-core.jar",
						"https://downloads.apache.org/tomcat/tomcat-8/v8.5.61/bin/embed/apache-tomcat-8.5.61-embed.zip"));
		map.entrySet().forEach(entry -> {
			URL url = url(entry.getValue());
			Path target = class1.resolve(entry.getKey());
			download(url, target);
		});

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
	}

	private void download(URL url, Path target) {
		if (exists(target)) {
			return;
		}

		try (InputStream in = url.openStream()) {
			copy(in, target, REPLACE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (substringAfterLast(target.getFileName().toString(), ".").toLowerCase().equals("zip")) {
			extractArchive(target, target.getParent());
		}
	}

	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> linkedHashMap(Entry<K, V>... entries) {
		return Stream.of(entries).collect(linkedHashMapCollector(Entry::getKey, Entry::getValue));
	}

	public <T, K, U> Collector<T, ?, Map<K, U>> linkedHashMapCollector(Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends U> valueMapper) {
		return toMap(keyMapper, valueMapper, ((first, second) -> first), LinkedHashMap::new);
	}

	public URL url(String spec) {
		try {
			return new URL(spec);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public void acceptDirectoryEntries(Path directory, String glob, Consumer<Path> consumer) {
		try (DirectoryStream<Path> stream = newDirectoryStream(directory, glob)) {
			stream.forEach(consumer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String substringAfterLast(String string, String substr) {
		if (emptyString(string) || emptyString(substr)) {
			return string;
		}
		int index = string.lastIndexOf(substr);
		if (index == -1) {
			return null;
		}
		return string.substring(index + substr.length());
	}

	public boolean emptyString(String string) {
		if (string == null) {
			return true;
		}
		return string.length() == 0;
	}

	public void extractArchive(Path archive, Path directory) {
		try (FileSystem fileSystem = newFileSystem(archive)) {
			Path start = fileSystem.getPath("/");

			FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
					Path target = directory.resolve(start.relativize(file).toString());
					try {
						createDirectories(target.getParent());
						copy(file, target, REPLACE_EXISTING);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					return CONTINUE;
				}
			};

			walkFileTree(start, visitor);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
