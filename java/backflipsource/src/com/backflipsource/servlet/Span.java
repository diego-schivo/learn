package com.backflipsource.servlet;

import java.lang.reflect.Field;
import java.util.Collection;

public class Span extends Control {

	protected boolean multiple;

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	@SuppressWarnings("rawtypes")
	public static class Factory extends Control.Factory<Span> {

		public Factory(Field field, StringConverter converter) {
			super(Span.class, field, converter);
		}

		@Override
		public Span control(Object object) {
			Span control = super.control(object);
			control.setMultiple(Collection.class.isAssignableFrom(field.getType()));
			return control;
		}
	}
}
