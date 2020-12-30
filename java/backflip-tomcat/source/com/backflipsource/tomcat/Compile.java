package com.backflipsource.tomcat;

import static com.backflipsource.helper.Helper.callJavaCompilerTask;
import static com.backflipsource.helper.Helper.collectSourceFiles;
import static com.backflipsource.helper.Helper.copyResourceFiles;
import static com.backflipsource.helper.Helper.initClassDirectory;
import static com.backflipsource.helper.Helper.javaCompilerOptions;
import static com.backflipsource.helper.Helper.linkedHashMap;
import static com.backflipsource.helper.Helper.safeStream;
import static com.backflipsource.helper.Helper.url;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import com.backflipsource.helper.HelperGenerator;
import com.backflipsource.helper.SourceHelperGenerator;
import com.backflipsource.helper.SourceStaticHelpersGenerator;
import com.backflipsource.helper.StaticHelpersGenerator;

public class Compile {

	public static void main(String[] args) {
		new Compile();
	}

	public Compile() {
		Path currentDir = Paths.get(".").toAbsolutePath().normalize();

		Path sourceDir = currentDir.resolve("source");

		StaticHelpersGenerator staticGenerator = new SourceStaticHelpersGenerator();
		staticGenerator.generateStaticHelpers(sourceDir, "com.backflipsource.tomcat");

		HelperGenerator generator = new SourceHelperGenerator();
		generator.generateHelper(sourceDir, "com.backflipsource.tomcat");

		Path classDir = currentDir.resolve("class");
		Path javacDir = currentDir.resolve("javac");

		initClassDirectory(classDir, safeStream(linkedHashMap(Path.of("taglibs-standard-impl-1.2.5.jar"), url(
				"https://downloads.apache.org/tomcat/taglibs/taglibs-standard-1.2.5/taglibs-standard-impl-1.2.5.jar"),
				Path.of("taglibs-standard-spec-1.2.5.jar"),
				url("https://downloads.apache.org/tomcat/taglibs/taglibs-standard-1.2.5/taglibs-standard-spec-1.2.5.jar"),
				Path.of("tomcat-embed-core.jar"),
				url("https://downloads.apache.org/tomcat/tomcat-8/v8.5.61/bin/embed/apache-tomcat-8.5.61-embed.zip"))));

		Iterable<String> options = javaCompilerOptions(currentDir, classDir, javacDir, sourceDir);

		List<String> list = (List<String>) options;
		int index = list.indexOf("-classpath") + 1;
		list.set(index, list.get(index) + ":../backflip-helper/javac");

		Collection<Path> sourceFiles = collectSourceFiles(sourceDir);
		callJavaCompilerTask(options, sourceFiles);

		copyResourceFiles(sourceDir, javacDir);
	}
}
