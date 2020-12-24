package com.backflipsource.test;

import java.util.Objects;

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
		String[] words = langHelper.camelCaseWords(null);
		assert words == null;
	}

	protected static void camelCaseWords2() {
		String[] words = langHelper.camelCaseWords("");
		assert words != null;
		assert words.length == 0;
	}

	protected static void camelCaseWords3() {
		String[] words = langHelper.camelCaseWords("foo");
		assert words != null;
		assert words.length == 1;
		assert Objects.equals(words[0], "foo");
	}

	protected static void camelCaseWords4() {
		String[] words = langHelper.camelCaseWords("fooBarBaz");
		assert words != null;
		assert words.length == 3;
		assert Objects.equals(words[0], "foo");
		assert Objects.equals(words[1], "bar");
		assert Objects.equals(words[2], "baz");
	}

	protected static void camelCaseWords5() {
		String[] words = langHelper.camelCaseWords("FooBarBaz");
		assert words != null;
		assert words.length == 3;
		assert Objects.equals(words[0], "Foo");
		assert Objects.equals(words[1], "bar");
		assert Objects.equals(words[2], "baz");
	}
}
