package com.backflipsource.servlet;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class Input extends Control {

	public static String TYPE_DATE = "date";
	public static String TYPE_TEXT = "text";

	protected String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@SuppressWarnings("rawtypes")
	public static class Factory extends Control.Factory<Input> {

		public Factory(Field field, StringConverter converter) {
			super(Input.class, field, converter);
		}

		@Override
		public Input control(Object object) {
			Input control = super.control(object);
			control.setType(LocalDate.class.isAssignableFrom(field.getType()) ? TYPE_DATE : TYPE_TEXT);
			return control;
		}
	}
}
