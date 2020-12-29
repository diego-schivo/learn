package com.backflipsource.helper.test;

import static com.backflipsource.helper.JavaPatterns.PATTERN_METHOD;
import static com.backflipsource.helper.JavaPatterns.PATTERN_PARAMETER;

import java.util.regex.Matcher;

public class JavaPatternsTest {

	public static void main(String[] args) {
		interfaceMethod();
		classMethod();
		twoMethods();
		publicStaticMethod();
		annotatedMethod1();
		annotatedMethod2();
		multilineMethod();
		oneParameter();
		twoParameters();
		genericParameter();
	}

	protected static void interfaceMethod() {
		String input = "interface Foo {\n" + "void bar();\n" + "}";
		Matcher matcher = PATTERN_METHOD.matcher(input);
		assert matcher.find();
		assert matcher.group().equals("void bar();");
	}

	protected static void classMethod() {
		String input = "class Foo {\n" + "void bar() {\n" + "baz = qux;\n" + "}\n" + "}";
		Matcher matcher = PATTERN_METHOD.matcher(input);
		assert matcher.find();
		assert matcher.group().equals("void bar() {\n" + "baz = qux;\n" + "}");
	}

	protected static void twoMethods() {
		String input = "interface Foo {\n" + "void bar();\n" + "void baz();\n" + "}";
		Matcher matcher = PATTERN_METHOD.matcher(input);
		assert matcher.find();
		assert matcher.group().equals("void bar();");
		assert matcher.find();
		assert matcher.group().equals("void baz();");
	}

	protected static void publicStaticMethod() {
		String input = "class Foo {\n" + "public static void bar() {\n" + "baz = qux;\n" + "}\n" + "}";
		Matcher matcher = PATTERN_METHOD.matcher(input);
		assert matcher.find();
		assert matcher.group().equals("public static void bar() {\n" + "baz = qux;\n" + "}");
	}

	protected static void annotatedMethod1() {
		String input = "class Foo {\n" + "@Bar\n" + "Object baz() {\n" + "return qux;\n" + "}\n" + "}";
		Matcher matcher = PATTERN_METHOD.matcher(input);
		assert matcher.find();
		assert matcher.group().equals("@Bar\n" + "Object baz() {\n" + "return qux;\n" + "}");
		assert matcher.group("returnType").equals("Object");
		assert matcher.group("name").equals("baz");
	}

	protected static void annotatedMethod2() {
		String input = "class Foo {\n" + "@Bar(\"baz\")\n" + "Qux qux() {\n" + "return null;\n" + "}\n" + "}";
		Matcher matcher = PATTERN_METHOD.matcher(input);
		assert matcher.find();
		assert matcher.group().equals("@Bar(\"baz\")\n" + "Qux qux() {\n" + "return null;\n" + "}");
		assert matcher.group("returnType").equals("Qux");
		assert matcher.group("name").equals("qux");
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
