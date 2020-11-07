package com.backflipsource.form;

import static com.backflipsource.Helpers.emptyString;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.splitString;
import static java.net.URLDecoder.decode;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

	public class Identity implements StringConverter<String> {

		@Override
		public String convertToString(String instance) {
			return instance;
		}

		@Override
		public String convertFromString(String string) {
			return string;
		}
	}

	public class Date implements StringConverter<LocalDate> {

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
