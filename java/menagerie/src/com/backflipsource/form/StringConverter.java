package com.backflipsource.form;

import static com.backflipsource.Helpers.emptyString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public interface StringConverter<T> {

	String convertToString(T instance);

	T convertFromString(String string);

	public class Identity implements StringConverter<String> {

		@Override
		public String convertToString(String instance) {
			return (String) instance;
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
}
