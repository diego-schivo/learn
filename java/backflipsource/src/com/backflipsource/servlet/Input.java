package com.backflipsource.servlet;

import static com.backflipsource.servlet.EntityContextListener.getViews;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class Input extends AbstractControl {

	public static String TYPE_DATE = "date";
	public static String TYPE_HIDDEN = "hidden";
	public static String TYPE_TEXT = "text";

	public String getType() {
		return ((Factory) factory).type;
	}

	public static class Factory extends AbstractFactory<Input> {

		protected String type;

		public Factory(Field field, View.Field annotation) {
			super(Input.class, field, annotation);

			if (getViews().containsKey(field.getType().getName())) {
				type = TYPE_HIDDEN;
			} else if (LocalDate.class.isAssignableFrom(field.getType())) {
				type = TYPE_DATE;
			} else {
				type = TYPE_TEXT;
			}
		}
	}
}
