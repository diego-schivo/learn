package com.backflipsource.servlet;

import java.lang.reflect.Field;

public class Span extends AbstractControl {

	public boolean isMultiple() {
		return ((Factory) factory).multiple;
	}

	@Override
	protected Class<?> getView() {
		return View.Show.class;
	}

	public static class Factory extends AbstractControl.Factory<Span> {

		protected boolean multiple;

		public Factory(Field field, Class<?> view) {
			super(Span.class, field, view);

			multiple = Iterable.class.isAssignableFrom(field.getType());
		}
	}
}
