package com.backflipsource.temp;

import static com.backflipsource.Helpers.iterator;
import static com.backflipsource.Helpers.logger;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.stringWithoutSuffix;
import static com.backflipsource.Helpers.substringBeforeLast;
import static com.backflipsource.Helpers.uncapitalizeString;
import static com.backflipsource.Helpers.unsafeFunction;
import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.Helpers.unsafeRun;
import static java.nio.file.Files.newDirectoryStream;
import static java.util.Collections.sort;
import static java.util.Comparator.comparing;
import static java.util.logging.Level.ALL;
import static java.util.regex.Pattern.compile;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class SourceTaglibWriter extends AbstractTaglibWriter {

	private static Logger logger = logger(SourceTaglibWriter.class, ALL);

	public static void main(String[] args) {
		TaglibWriter taglib = new SourceTaglibWriter();

		Path src = Path.of("/home/diego/git/learn/java/backflipsource/src");
		String package1 = "com.backflipsource.ui";
		taglib.writeTaglib(src, package1);

	}

	private static Pattern packagePattern = compile("package ([\\w\\.]+)");
	private static Pattern tagClassPattern = compile("public class (\\w+) extends SimpleTagSupport");
	private static Pattern attributeFieldPattern = compile("protected \\w+ (\\w+)");

	@Override
	protected String taglibContent(Path src, String package1) {
		List<Path> list = new ArrayList<>();
		unsafeRun(() -> {
			try (DirectoryStream<Path> stream = unsafeGet(
					() -> newDirectoryStream(src.resolve(package1.replace('.', '/')), "*.java"))) {
				stream.forEach(list::add);
			}
		});
		sort(list,
				comparing(path -> stringWithoutSuffix(substringBeforeLast(path.getFileName().toString(), "."), "Tag")));
		Stream<String> tags = tagContents(list.stream());
		return formatTaglib(tags);
	}

	public Stream<String> tagContents(Stream<Path> files) {
		return files.map(unsafeFunction(Files::readString)).filter(input -> tagClassPattern.matcher(input).find())
				.map(this::tagContent);
	}

	protected String tagContent(String source) {
		Matcher packageMatcher = packagePattern.matcher(source);
		String package1 = safeStream(iterator(() -> packageMatcher.find(), () -> packageMatcher.group(1))).findFirst()
				.orElse(null);

		Matcher tagClassMatcher = tagClassPattern.matcher(source);
		String class1 = safeStream(iterator(() -> tagClassMatcher.find(), () -> tagClassMatcher.group(1))).findFirst()
				.orElse(null);

		String name = uncapitalizeString(stringWithoutSuffix(class1, "Tag"));
		String tagClass = package1 + "." + class1;
		Stream<String> attributes = attributeContents(source);
		return formatTag(name, tagClass, attributes);
	}

	public Stream<String> attributeContents(String source) {
		Matcher attributeFieldMatcher = attributeFieldPattern.matcher(source);
		return safeStream(iterator(() -> attributeFieldMatcher.find(), () -> attributeFieldMatcher.group(1)))
				.map(this::formatAttribute);
	}
}
