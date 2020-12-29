package com.backflipsource.test;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.backflipsource.DefaultLangHelper;
import com.backflipsource.LangHelper;
import com.backflipsource.test.foo.Foo;
import com.backflipsource.test.foo.bar.Bar;

public class Tests {

	protected static LangHelper langHelper = new DefaultLangHelper();

	public static void main(String[] args) {
		camelCaseWords1();
		camelCaseWords2();
		camelCaseWords3();
		camelCaseWords4();
		camelCaseWords5();

		packageClasses1();
		packageClasses2();
	}

	protected static void camelCaseWords1() {
		Stream<String> stream = langHelper.camelCaseWords(null);
		assert stream == null;
	}

	protected static void camelCaseWords2() {
		Stream<String> stream = langHelper.camelCaseWords("");
		assert stream != null;
		String[] words = stream.toArray(String[]::new);
		assert words.length == 0;
	}

	protected static void camelCaseWords3() {
		Stream<String> stream = langHelper.camelCaseWords("foo");
		assert stream != null;
		String[] words = stream.toArray(String[]::new);
		assert words.length == 1;
		assert Objects.equals(words[0], "foo");
	}

	protected static void camelCaseWords4() {
		Stream<String> stream = langHelper.camelCaseWords("fooBarBaz");
		assert stream != null;
		String[] words = stream.toArray(String[]::new);
		assert words.length == 3;
		assert Objects.equals(words[0], "foo");
		assert Objects.equals(words[1], "bar");
		assert Objects.equals(words[2], "baz");
	}

	protected static void camelCaseWords5() {
		Stream<String> stream = langHelper.camelCaseWords("FooBarBaz");
		assert stream != null;
		String[] words = stream.toArray(String[]::new);
		assert words.length == 3;
		assert Objects.equals(words[0], "Foo");
		assert Objects.equals(words[1], "bar");
		assert Objects.equals(words[2], "baz");
	}

	protected static void packageClasses1() {
		Stream<Class<?>> classes = langHelper.packageClasses("com.backflipsource.test.foo");
		assert classes != null;

		Class<?>[] classes2 = classes.toArray(Class[]::new);
		assert classes2.length == 1;
		assert classes2[0] == Foo.class;
	}

	protected static void packageClasses2() {
		Stream<Class<?>> classes = langHelper.packageClasses("com.backflipsource.test.foo", true);
		assert classes != null;

		List<Class<?>> classes2 = classes.collect(toList());
		assert classes2.size() == 2;
		assert classes2.contains(Foo.class);
		assert classes2.contains(Bar.class);
	}
}
