package com.backflipsource.helper;

import static com.backflipsource.helper.StaticLangHelper.joinStrings;
import static com.backflipsource.helper.StaticNioHelper.acceptDirectoryEntries;
import static com.backflipsource.helper.StaticUtilHelper.iterator;
import static com.backflipsource.helper.StaticUtilHelper.logger;
import static com.backflipsource.helper.StaticUtilHelper.safeStream;
import static com.backflipsource.helper.StaticUtilHelper.unsafeGet;
import static com.backflipsource.helper.StaticUtilHelper.unsafeRun;
import static java.nio.file.Files.readString;
import static java.nio.file.Files.writeString;
import static java.util.logging.Level.ALL;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.stream.Stream;

public class SourceStaticClassesWriter extends AbstractStaticClassesWriter {

//	private static Logger logger = logger(SourceStaticClassesWriter.class, ALL);

	public static void main(String[] args) throws IOException {
		Path path = Paths.get(".").toAbsolutePath().normalize();

		StaticClassesWriter writer = new SourceStaticClassesWriter();

		Path source = path.resolve("source");
		String package1 = "com.backflipsource.helper";
		writer.writeStaticClasses(source, package1);
	}

	@Override
	public void writeStaticClasses(Path source, String package1) {
		acceptDirectoryEntries(source.resolve(package1.replace('.', '/')), "*Helper.java", path -> {
			String input = unsafeGet(() -> readString(path));

			Matcher matcher = JavaPatterns.PATTERN_INTERFACE.matcher(input);
			if (!matcher.find()) {
				return;
			}

			Path target = path.getParent().resolve("Static" + path.getFileName().toString());
			CharSequence output = staticClassContent(input);
			unsafeRun(() -> writeString(target, output));
		});
	}

	protected String staticClassContent(String source) {
		Matcher packageMatcher = JavaPatterns.PATTERN_PACKAGE.matcher(source);
		String package1 = safeStream(iterator(() -> packageMatcher.find(), () -> packageMatcher.group(1))).findFirst()
				.orElse(null);

		String imports = joinStrings(importContents(source), "\n");

		Matcher interfaceMatcher = JavaPatterns.PATTERN_INTERFACE.matcher(source);
		String interface1 = safeStream(iterator(() -> interfaceMatcher.find(), () -> interfaceMatcher.group(1)))
				.findFirst().orElse(null);

		String methods = joinStrings(methodContents(source), "\n");

		return formatStaticClass(package1, imports, interface1, methods);
	}

	protected Stream<String> importContents(String source) {
		Matcher matcher = JavaPatterns.PATTERN_IMPORT.matcher(source);
		return safeStream(iterator(() -> matcher.find(), () -> formatImport(matcher.group(1))));
	}

	protected Stream<String> methodContents(String source) {
		Matcher matcher = JavaPatterns.PATTERN_METHOD.matcher(source);
		return safeStream(iterator(() -> matcher.find(), () -> {
			String returnType = matcher.group(1);
			String name = matcher.group(2);
			String parameters = matcher.group(3);
			String parameters2 = joinStrings(parameterContents(parameters), ", ");
			return formatMethod(returnType, name, parameters, parameters2);
		}));
	}

	protected Stream<String> parameterContents(String input) {
		Matcher matcher = JavaPatterns.PATTERN_PARAMETER.matcher(input);
		return safeStream(iterator(() -> matcher.find(), () -> matcher.group("name")));
	}
}
