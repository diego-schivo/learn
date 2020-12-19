package com.backflipsource.servlet;

import java.lang.reflect.Field;
import java.util.Collection;

public class Span extends Control {

	protected boolean multiple;

	public boolean isMultiple() {
		return multiple;
	}

	public static class Factory extends Control.Factory<Span> {

		public Factory(Field field, View.Field annotation) {
			super(Span.class, field, annotation);
		}

		@Override
		public Span control(Object object) {
			Span control = super.control(object);
			control.multiple = Collection.class.isAssignableFrom(field.getType());
			return control;
		}
	}
}
