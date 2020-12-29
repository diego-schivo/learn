package com.backflipsource.jsp;

import static com.backflipsource.Helpers.logger;
import static com.backflipsource.Helpers.packageClasses;
import static com.backflipsource.Helpers.stringWithoutSuffix;
import static com.backflipsource.Helpers.uncapitalizeString;
import static java.lang.reflect.Modifier.isProtected;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Collections.sort;
import static java.util.Comparator.comparing;
import static java.util.logging.Level.ALL;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.servlet.jsp.tagext.SimpleTag;

public class ReflectionTaglibWriter extends AbstractTaglibWriter {

	private static Logger logger = logger(ReflectionTaglibWriter.class, ALL);

	public static void main(String[] args) {
		Path path = Paths.get(".").toAbsolutePath().normalize();

		TaglibWriter taglib = new ReflectionTaglibWriter();

		Path source = path.resolve("source");
		String package1 = "com.backflipsource.ui";
		taglib.writeTaglib(source, package1);
	}

	@Override
	protected String taglibContent(Path src, String package1) {
		Stream<Class<?>> classes = packageClasses(package1);
		Stream<String> tags = tagContents(classes);
		return formatTaglib(tags);
	}

	@SuppressWarnings("unchecked")
	public Stream<String> tagContents(Stream<Class<?>> classes) {
		List<Class<? extends SimpleTag>> list = classes.filter(SimpleTag.class::isAssignableFrom)
				.map(class1 -> (Class<? extends SimpleTag>) class1).collect(toList());
		sort(list, comparing(class1 -> stringWithoutSuffix(class1.getSimpleName(), "Tag")));
		return list.stream().map(this::tagContent);
	}

	protected String tagContent(Class<? extends SimpleTag> class1) {
		String name = uncapitalizeString(stringWithoutSuffix(class1.getSimpleName(), "Tag"));
		String tagClass = class1.getName();
		Stream<String> attributes = attributeContents(Stream.of(class1.getDeclaredFields()));
		return formatTag(name, tagClass, attributes);
	}

	protected Stream<String> attributeContents(Stream<Field> fields) {
		return fields.filter(field -> !isStatic(field.getModifiers()) && isProtected(field.getModifiers()))
				.map(Field::getName).map(this::formatAttribute);
	}
}
