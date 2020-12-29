package com.backflipsource.helper;

import static com.backflipsource.helper.StaticLangHelper.joinStrings;
import static com.backflipsource.helper.StaticNioHelper.acceptDirectoryEntries;
import static com.backflipsource.helper.StaticUtilHelper.iterator;
import static com.backflipsource.helper.StaticUtilHelper.safeStream;
import static com.backflipsource.helper.StaticUtilHelper.unsafeGet;
import static com.backflipsource.helper.StaticUtilHelper.unsafeRun;
import static java.nio.file.Files.readString;
import static java.nio.file.Files.writeString;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.stream.Stream;

public class SourceStaticHelpersWriter extends AbstractStaticHelpersWriter {

//	private static Logger logger = logger(SourceStaticClassesWriter.class, ALL);

	public static void main(String[] args) throws IOException {
		Path path = Paths.get(".").toAbsolutePath().normalize();

		StaticHelpersWriter writer = new SourceStaticHelpersWriter();

		Path source = path.resolve("source");
		String package1 = "com.backflipsource.helper";
		writer.writeStaticHelpers(source, package1);
	}

	@Override
	public void writeStaticHelpers(Path source, String package1) {
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
		String name = "Static" + interface1;

		String staticInstance = formatStaticInstance(interface1, "Default" + interface1);
		String methods = joinStrings(staticMethodContents(source), "\n");
		String content = staticInstance + "\n" + methods;

		return formatClass(package1, imports, name, content);
	}

	protected Stream<String> importContents(String source) {
		Matcher matcher = JavaPatterns.PATTERN_IMPORT.matcher(source);
		return safeStream(iterator(() -> matcher.find(), () -> formatImport(matcher.group(1))));
	}

	protected Stream<String> staticMethodContents(String source) {
		Matcher matcher = JavaPatterns.PATTERN_METHOD.matcher(source);
		return safeStream(iterator(() -> matcher.find(), () -> {
			String returnType = matcher.group(1);
			String name = matcher.group(2);
			String parameters = matcher.group(3);
			String parameters2 = joinStrings(parameterContents(parameters), ", ");
			return formatStaticMethod(returnType, name, parameters, parameters2);
		}));
	}

	protected Stream<String> parameterContents(String input) {
		Matcher matcher = JavaPatterns.PATTERN_PARAMETER.matcher(input);
		return safeStream(iterator(() -> matcher.find(), () -> matcher.group("name")));
	}
}
