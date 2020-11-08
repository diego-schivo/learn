package com.backflipsource.servlet;

import java.lang.reflect.Field;
import java.util.List;

public class Anchor extends Control {

	protected static String PAGE = "anchor.jsp";

	public Anchor(String name, List<String> values) {
		super(name, values);
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
			return new Anchor(name(), values(object));
		}
	}
}
