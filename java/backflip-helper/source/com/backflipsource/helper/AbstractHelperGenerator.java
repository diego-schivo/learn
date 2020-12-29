package com.backflipsource.helper;

import static com.backflipsource.helper.Helper.readResource;
import static com.backflipsource.helper.Helper.string;
import static java.text.MessageFormat.format;

public abstract class AbstractHelperGenerator implements HelperGenerator {

//	private static Logger logger = logger(AbstractStaticClassesWriter.class, ALL);

	private static String classFormat = readResource("class.txt", AbstractHelperGenerator.class);
	private static String importFormat = readResource("import.txt", AbstractHelperGenerator.class);
	private static String staticMethodFormat = readResource("static-method.txt", AbstractHelperGenerator.class);

	protected String formatClass(String package1, String imports, String name, String content) {
		return format(classFormat, package1, imports, name, content);
	}

	protected String formatImport(String import1) {
		return format(importFormat, import1);
	}

	protected String formatStaticMethod(String returnType, String name, String parameters, String class1,
			String parameters2) {
		return format(staticMethodFormat, returnType, name, parameters,
				(string(returnType).equals("void") || string(returnType).endsWith(" void")) ? 0 : 1, class1,
				parameters2);
	}
}
