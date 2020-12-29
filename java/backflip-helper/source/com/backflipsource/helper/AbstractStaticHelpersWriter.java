package com.backflipsource.helper;

import static com.backflipsource.helper.StaticLangHelper.readResource;
import static com.backflipsource.helper.StaticLangHelper.string;
import static java.text.MessageFormat.format;

public abstract class AbstractStaticHelpersWriter implements StaticHelpersWriter {

//	private static Logger logger = logger(AbstractStaticClassesWriter.class, ALL);

	private static String classFormat = readResource("class.txt", AbstractStaticHelpersWriter.class);
	private static String importFormat = readResource("import.txt", AbstractStaticHelpersWriter.class);
	private static String staticInstanceFormat = readResource("static-instance.txt", AbstractStaticHelpersWriter.class);
	private static String staticMethodFormat = readResource("static-method.txt", AbstractStaticHelpersWriter.class);

	protected String formatClass(String package1, String imports, String interface1, String methods) {
		return format(classFormat, package1, imports, interface1, methods);
	}

	protected String formatImport(String import1) {
		return format(importFormat, import1);
	}

	protected String formatStaticInstance(String interface1, String class1) {
		return format(staticInstanceFormat, interface1, class1);
	}

	protected String formatStaticMethod(String returnType, String name, String parameters, String parameters2) {
		return format(staticMethodFormat, returnType, name, parameters,
				(string(returnType).equals("void") || string(returnType).endsWith(" void")) ? 0 : 1, "getInstance()",
				parameters2);
	}
}
