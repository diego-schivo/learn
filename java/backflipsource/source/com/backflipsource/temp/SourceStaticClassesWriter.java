package com.backflipsource.temp;

import static com.backflipsource.Helpers.acceptDirectoryEntries;
import static com.backflipsource.Helpers.iterator;
import static com.backflipsource.Helpers.joinStrings;
import static com.backflipsource.Helpers.logger;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.Helpers.unsafeRun;
import static java.nio.file.Files.readString;
import static java.nio.file.Files.writeString;
import static java.util.logging.Level.ALL;
import static java.util.regex.Pattern.compile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class SourceStaticClassesWriter extends AbstractStaticClassesWriter {

	private static Logger logger = logger(SourceStaticClassesWriter.class, ALL);

	public static void main(String[] args) throws IOException {
		Path path = Paths.get(".").toAbsolutePath().normalize();

		StaticClassesWriter taglib = new SourceStaticClassesWriter();

		Path source = path.resolve("source");
		String package1 = "com.backflipsource";
		taglib.writeStaticClasses(source, package1);
	}

	protected static Pattern packagePattern = compile("package (\\S[^;]*);");
	protected static Pattern importPattern = compile("import (\\S[^;]*);");
	protected static Pattern interfacePattern = compile("public interface (\\S+) \\{");
	protected static Pattern methodPattern = compile("(\\S.*)\\s(\\S+)\\((.*)\\);");
	protected static Pattern parameterPattern = compile("(\\S[^,]*)\\s(\\S+)(,|$)");

	@Override
	public void writeStaticClasses(Path source, String package1) {
		acceptDirectoryEntries(source.resolve(package1.replace('.', '/')), "*Helper.java", path -> {
			String input = unsafeGet(() -> readString(path));

			Matcher matcher = interfacePattern.matcher(input);
			if (!matcher.find()) {
				return;
			}

			Path target = path.getParent().resolve("Static" + path.getFileName().toString());
			CharSequence output = staticClassContent(input);
			unsafeRun(() -> writeString(target, output));
		});
	}

	protected String staticClassContent(String source) {
		Matcher packageMatcher = packagePattern.matcher(source);
		String package1 = safeStream(iterator(() -> packageMatcher.find(), () -> packageMatcher.group(1))).findFirst()
				.orElse(null);

		String imports = joinStrings(importContents(source), "\n");

		Matcher interfaceMatcher = interfacePattern.matcher(source);
		String interface1 = safeStream(iterator(() -> interfaceMatcher.find(), () -> interfaceMatcher.group(1)))
				.findFirst().orElse(null);

		String methods = joinStrings(methodContents(source), "\n");

		return formatStaticClass(package1, imports, interface1, methods);
	}

	protected Stream<String> importContents(String source) {
		Matcher matcher = importPattern.matcher(source);
		return safeStream(iterator(() -> matcher.find(), () -> formatImport(matcher.group(1))));
	}

	protected Stream<String> methodContents(String source) {
		Matcher matcher = methodPattern.matcher(source);
		return safeStream(iterator(() -> matcher.find(), () -> {
			String returnType = matcher.group(1);
			String name = matcher.group(2);
			String parameters = matcher.group(3);
			String parameters2 = joinStrings(parameterContents(parameters), ", ");
			return formatMethod(returnType, name, parameters, parameters2);
		}));
	}

	protected Stream<String> parameterContents(String input) {
		Matcher matcher = parameterPattern.matcher(input);
		return safeStream(iterator(() -> matcher.find(), () -> matcher.group(2)));
	}
}
