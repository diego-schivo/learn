package com.backflipsource.temp;

import static com.backflipsource.Helpers.logger;
import static com.backflipsource.Helpers.string;
import static com.backflipsource.Helpers.unsafeGet;
import static java.lang.String.format;
import static java.nio.file.Files.readString;
import static java.util.logging.Level.ALL;

import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.logging.Logger;

public abstract class AbstractStaticClassesWriter implements StaticClassesWriter {

	private static Logger logger = logger(AbstractStaticClassesWriter.class, ALL);

	private static String staticClassFormat = readResource("static-class.txt");
	private static String importFormat = readResource("import.txt");
	private static String methodFormat = readResource("method.txt");

	protected String formatStaticClass(String package1, String imports, String interface1, String methods) {
		return format(staticClassFormat, package1, imports, interface1, methods);
	}

	protected String formatImport(String import1) {
		return format(importFormat, import1);
	}

	protected String formatMethod(String returnType, String name, String parameters, String parameters2) {
		return MessageFormat.format(methodFormat, returnType, name, parameters,
				(string(returnType).equals("void") || string(returnType).endsWith(" void")) ? 0 : 1, parameters2);
	}

	private static String readResource(String resourceName) {
		return unsafeGet(
				() -> readString(Path.of(AbstractStaticClassesWriter.class.getResource(resourceName).toURI())));
	}
}
