package com.backflipsource.helper;

import static com.backflipsource.helper.StaticLangHelper.readResource;
import static com.backflipsource.helper.StaticLangHelper.string;
import static java.text.MessageFormat.format;

public abstract class AbstractStaticClassesWriter implements StaticClassesWriter {

//	private static Logger logger = logger(AbstractStaticClassesWriter.class, ALL);

	private static String classFormat = readResource("class.txt", AbstractStaticClassesWriter.class);
	private static String importFormat = readResource("import.txt", AbstractStaticClassesWriter.class);
	private static String staticInstanceFormat = readResource("static-instance.txt", AbstractStaticClassesWriter.class);
	private static String methodFormat = readResource("method.txt", AbstractStaticClassesWriter.class);

	protected String formatClass(String package1, String imports, String interface1, String methods) {
		return format(classFormat, package1, imports, interface1, methods);
	}

	protected String formatImport(String import1) {
		return format(importFormat, import1);
	}

	protected String formatStaticInstance(String interface1, String class1) {
		return format(staticInstanceFormat, interface1, class1);
	}

	protected String formatMethod(String returnType, String name, String parameters, String parameters2) {
		return format(methodFormat, returnType, name, parameters,
				(string(returnType).equals("void") || string(returnType).endsWith(" void")) ? 0 : 1, parameters2);
	}
}
