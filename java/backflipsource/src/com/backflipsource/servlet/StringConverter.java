package com.backflipsource.servlet;

import static com.backflipsource.Helpers.emptyString;
import static java.lang.Integer.parseInt;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public interface StringConverter<T> {

	String convertToString(T instance);

	T convertFromString(String string);

//	static String encodeAndJoin(String[] strings) {
//		return safeStream(strings).map(item -> encode(item, UTF_8)).collect(joining(","));
//	}
//
//	static String[] splitAndDecode(String string) {
//		return safeStream(splitString(string, ',')).map(item -> decode(item, UTF_8)).toArray(String[]::new);
//	}

	public class ForString implements StringConverter<String> {

		@Override
		public String convertToString(String instance) {
			return instance;
		}

		@Override
		public String convertFromString(String string) {
			return string;
		}
	}

	public class ForInteger implements StringConverter<Integer> {

		@Override
		public String convertToString(Integer instance) {
			if (instance == null) {
				return null;
			}
			return instance.toString();
		}

		@Override
		public Integer convertFromString(String string) {
			if (emptyString(string)) {
				return null;
			}
			return parseInt(string);
		}
	}

	public class ForLocalDate implements StringConverter<LocalDate> {

		private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		@Override
		public String convertToString(LocalDate instance) {
			if (instance == null) {
				return null;
			}
			return FORMATTER.format(instance);
		}

		@Override
		public LocalDate convertFromString(String string) {
			if (emptyString(string)) {
				return null;
			}
			return LocalDate.parse(string, FORMATTER);
		}
	}

//	public class List2<T> implements StringConverter<List<T>> {
//
//		StringConverter<T> converter;
//
//		public List2(StringConverter<T> converter) {
//			this.converter = converter;
//		}
//
//		@Override
//		public String convertToString(List<T> instance) {
//			if (instance == null) {
//				return null;
//			}
//			return safeStream(instance).map(item -> converter.convertToString(item)).collect(joining(","));
//		}
//
//		@Override
//		public List<T> convertFromString(String string) {
//			if (string == null) {
//				return null;
//			}
//			return safeStream(splitString(string, ',')).map(item -> converter.convertFromString(item))
//					.collect(toList());
//		}
//	}
}
