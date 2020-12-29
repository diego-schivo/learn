package com.backflipsource.helper;

import static com.backflipsource.helper.JavaPatterns.PATTERN_CLASS;
import static com.backflipsource.helper.JavaPatterns.PATTERN_IMPORT;
import static com.backflipsource.helper.JavaPatterns.PATTERN_METHOD;
import static com.backflipsource.helper.JavaPatterns.PATTERN_PARAMETER;
import static com.backflipsource.helper.StaticLangHelper.joinStrings;
import static com.backflipsource.helper.StaticLangHelper.string;
import static com.backflipsource.helper.StaticLangHelper.substringBeforeLast;
import static com.backflipsource.helper.StaticNioHelper.acceptDirectoryEntries;
import static com.backflipsource.helper.StaticUtilHelper.iterator;
import static com.backflipsource.helper.StaticUtilHelper.safeStream;
import static com.backflipsource.helper.StaticUtilHelper.unsafeGet;
import static com.backflipsource.helper.StaticUtilHelper.unsafeRun;
import static java.nio.file.Files.readString;
import static java.nio.file.Files.writeString;
import static java.util.Collections.sort;
import static java.util.Comparator.comparing;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Stream;

public class SourceHelperWriter extends AbstractHelperWriter {

//	private static Logger logger = logger(SourceStaticClassesWriter.class, ALL);

	public static void main(String[] args) throws IOException {
		Path path = Paths.get(".").toAbsolutePath().normalize();

		HelperWriter writer = new SourceHelperWriter();

		Path source = path.resolve("source");
		String package1 = "com.backflipsource.helper";
		writer.writeHelper(source, package1);
	}

	@Override
	public void writeHelper(Path source, String package1) {
		Path directory = source.resolve(package1.replace('.', '/'));
		Path target = directory.resolve("Helper.java");
		CharSequence output = staticClassContent(directory, package1);
		unsafeRun(() -> writeString(target, output));
	}

	protected String staticClassContent(Path directory, String package1) {
		Set<String> imports = new LinkedHashSet<>();
		List<String> staticMethods = new ArrayList<>();

		List<Path> paths = new ArrayList<>();
		acceptDirectoryEntries(directory, "Static*Helper.java", paths::add);

		sort(paths, comparing(path -> substringBeforeLast(string(path.getFileName()), ".").toLowerCase()));

		paths.forEach(path -> {
			String input = unsafeGet(() -> readString(path));

			Matcher matcher = PATTERN_CLASS.matcher(input);
			String class1 = safeStream(iterator(() -> matcher.find(), () -> matcher.group(1))).findFirst().orElse(null);

			importContents(input).forEach(imports::add);
			staticMethodContents(input, class1).forEach(staticMethods::add);
		});

		return formatClass(package1, joinStrings(safeStream(imports), "\n"), "Helper",
				joinStrings(safeStream(staticMethods), "\n"));
	}

	protected Stream<String> importContents(String source) {
		Matcher matcher = PATTERN_IMPORT.matcher(source);
		return safeStream(iterator(() -> matcher.find(), () -> formatImport(matcher.group(1))));
	}

	protected Stream<String> staticMethodContents(String source, String class1) {
		Matcher matcher = PATTERN_METHOD.matcher(source);
		return safeStream(iterator(() -> matcher.find(), () -> {
			String name = matcher.group("name");
			if (string(name).equals("getInstance")) {
				return null;
			}

			String returnType = matcher.group("returnType");
			String parameters = matcher.group("parameters");
			String parameters2 = joinStrings(parameterContents(parameters), ", ");

			return formatStaticMethod(returnType, name, parameters, class1, parameters2);
		})).filter(Objects::nonNull);
	}

	protected Stream<String> parameterContents(String input) {
		Matcher matcher = PATTERN_PARAMETER.matcher(input);
		return safeStream(iterator(() -> matcher.find(), () -> matcher.group("name")));
	}
}
