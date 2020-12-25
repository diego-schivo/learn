package com.backflipsource.test;

import java.util.Objects;
import java.util.stream.Stream;

import com.backflipsource.DefaultLangHelper;

public class Tests {

	protected static DefaultLangHelper langHelper = new DefaultLangHelper();

	public static void main(String[] args) {
		camelCaseWords1();
		camelCaseWords2();
		camelCaseWords3();
		camelCaseWords4();
		camelCaseWords5();
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
}
