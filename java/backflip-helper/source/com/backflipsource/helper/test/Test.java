package com.backflipsource.helper.test;

import static com.backflipsource.helper.JavaPatterns.PATTERN_METHOD;
import static com.backflipsource.helper.JavaPatterns.PATTERN_PARAMETER;

import java.util.regex.Matcher;

public class Test {

	public static void main(String[] args) {
		oneMethod();
		twoMethods();
		multilineMethod();
		oneParameter();
		twoParameters();
		genericParameter();
	}

	protected static void oneMethod() {
		String input = "interface Foo {\n" + "void bar();\n" + "}";
		Matcher matcher = PATTERN_METHOD.matcher(input);
		assert matcher.find();
		assert matcher.group().equals("void bar();");
	}

	protected static void twoMethods() {
		String input = "interface Foo {\n" + "void bar();\n" + "void baz();\n" + "}";
		Matcher matcher = PATTERN_METHOD.matcher(input);
		assert matcher.find();
		assert matcher.group().equals("void bar();");
		assert matcher.find();
		assert matcher.group().equals("void baz();");
	}

	protected static void multilineMethod() {
		String input = "interface Foo {\n" + "void bar(Baz baz,\n" + "Qux qux);\n" + "}";
		Matcher matcher = PATTERN_METHOD.matcher(input);
		assert matcher.find();
		assert matcher.group().equals("void bar(Baz baz,\n" + "Qux qux);");
	}

	protected static void oneParameter() {
		String input = "Foo foo";
		Matcher matcher = PATTERN_PARAMETER.matcher(input);
		assert matcher.find();
		assert matcher.group().equals("Foo foo");
	}

	protected static void twoParameters() {
		String input = "Foo foo, Bar bar";
		Matcher matcher = PATTERN_PARAMETER.matcher(input);
		assert matcher.find();
		assert matcher.group().equals("Foo foo");
		assert matcher.find();
		assert matcher.group().equals("Bar bar");
	}

	protected static void genericParameter() {
		String input = "Foo<? super T, ? extends U> foo";
		Matcher matcher = PATTERN_PARAMETER.matcher(input);
		assert matcher.find();
		assert matcher.group().equals("Foo<? super T, ? extends U> foo");
	}
}
