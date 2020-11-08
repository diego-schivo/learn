package com.backflipsource.servlet;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class Span extends Control {

	protected static String PAGE = "span.jsp";

	protected boolean multiple;

	public Span(String name, List<String> values, boolean multiple) {
		super(name, values);
		this.multiple = multiple;
	}

	public boolean isMultiple() {
		return multiple;
	}

	@Override
	public String getPage() {
		return PAGE;
	}

	@SuppressWarnings("rawtypes")
	public static class Factory extends Control.Factory {

		public Factory(Field field, StringConverter converter) {
			super(field, converter);
		}

		@Override
		public Control control(Object object) {
			boolean multiple = Collection.class.isAssignableFrom(field.getType());

			return new Span(name(), values(object), multiple);
		}
	}
}
