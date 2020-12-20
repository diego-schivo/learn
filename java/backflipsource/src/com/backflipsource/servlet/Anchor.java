package com.backflipsource.servlet;

import java.lang.reflect.Field;

public class Anchor extends AbstractControl {

	@Override
	protected Class<?> getView() {
		return View.Show.class;
	}

	public static class Factory extends AbstractControl.Factory<Anchor> {

		public Factory(Field field, View.Field annotation) {
			super(Anchor.class, field, annotation);
		}
	}
}
