package com.backflipsource.form;

import java.lang.reflect.Field;
import java.util.List;

public class Input extends Control {

	public static String TYPE_TEXT = "text";
	public static String TYPE_DATE = "date";

	protected static String PAGE = "input.jsp";

	protected String type;

	public Input(String name, List<String> values, String type) {
		super(name, values);
		this.type = type;
	}

	public String getType() {
		return type;
	}

	@Override
	public String getPage() {
		return PAGE;
	}

//	public String getType() {
//		if (LocalDate.class.isAssignableFrom(field.getType())) {
//			return TYPE_DATE;
//		}
//		return TYPE_TEXT;
//	}

	@SuppressWarnings("rawtypes")
	public static class Factory extends Control.Factory {

		public Factory(Field field, StringConverter converter) {
			super(field, converter);
		}

		@Override
		public Control control(Object object) {
			String type = TYPE_TEXT;
			return new Input(name(), values(object), type);
		}
	}
}
