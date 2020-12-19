package com.backflipsource.servlet;

import static com.backflipsource.servlet.EntityContextListener.getViews;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class Input extends Control {

	public static String TYPE_DATE = "date";
	public static String TYPE_HIDDEN = "hidden";
	public static String TYPE_TEXT = "text";

	protected String type;

	public String getType() {
		return type;
	}

	public static class Factory extends Control.Factory<Input> {

		public Factory(Field field, View.Field annotation) {
			super(Input.class, field, annotation);
		}

		@Override
		public Input control(Object object) {
			Input control = super.control(object);

			Class<?> type = field.getType();
			if (getViews().containsKey(type.getName())) {
				control.type = TYPE_HIDDEN;
			} else if (LocalDate.class.isAssignableFrom(type)) {
				control.type = TYPE_DATE;
			} else {
				control.type = TYPE_TEXT;
			}

			return control;
		}
	}
}
