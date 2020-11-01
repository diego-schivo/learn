package com.backflipsource.form;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class Input extends Control {

	protected static String PAGE = "input.jsp";
	protected static String TYPE_TEXT = "text";
	protected static String TYPE_DATE = "date";

	public Input(Field field) {
		super(field);
	}

	@Override
	public String getPage() {
		return PAGE;
	}

	public String getType() {
		if (LocalDate.class.isAssignableFrom(field.getType())) {
			return TYPE_DATE;
		}
		return TYPE_TEXT;
	}
}
