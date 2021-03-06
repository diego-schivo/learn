package com.backflipsource.jsp;

import static com.backflipsource.helper.Helper.joinStrings;
import static com.backflipsource.helper.Helper.logger;
import static com.backflipsource.helper.Helper.substringAfterLast;
import static com.backflipsource.helper.Helper.unsafeGet;
import static com.backflipsource.helper.Helper.unsafeRun;
import static java.nio.file.Files.readString;
import static java.nio.file.Files.writeString;
import static java.text.MessageFormat.format;
import static java.util.logging.Level.ALL;

import java.nio.file.Path;
import java.util.logging.Logger;
import java.util.stream.Stream;

public abstract class AbstractTaglibWriter implements TaglibWriter {

	private static Logger logger = logger(AbstractTaglibWriter.class, ALL);

	private static String taglibFormat = readResource("taglib.txt");
	private static String tagFormat = readResource("tag.txt");
	private static String attributeFormat = readResource("attribute.txt");

	@Override
	public void writeTaglib(Path source, String package1) {
		Path file = source.resolve("META-INF/" + substringAfterLast(package1, ".") + ".tld");
		CharSequence content = taglibContent(source, package1);
		unsafeRun(() -> writeString(file, content));
	}

	protected abstract String taglibContent(Path src, String package1);

	protected String formatTaglib(Stream<String> tags) {
		return format(taglibFormat, joinStrings(tags, "\n"));
	}

	protected String formatTag(String name, String tagClass, Stream<String> attributes) {
		return format(tagFormat, name, tagClass, joinStrings(attributes, "\n"));
	}

	protected String formatAttribute(String name) {
		return format(attributeFormat, name);
	}

	private static String readResource(String resourceName) {
		return unsafeGet(() -> readString(Path.of(AbstractTaglibWriter.class.getResource(resourceName).toURI())));
	}
}
