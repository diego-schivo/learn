package com.backflipsource.servlet;

import java.lang.reflect.Field;

public class Span extends AbstractControl {

	public boolean isMultiple() {
		return ((Factory) factory).multiple;
	}

	public static class Factory extends AbstractFactory<Span> {

		protected boolean multiple;

		public Factory(Field field, View.Field annotation) {
			super(Span.class, field, annotation);

			multiple = Iterable.class.isAssignableFrom(field.getType());
		}
	}
}
