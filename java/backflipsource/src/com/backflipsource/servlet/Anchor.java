package com.backflipsource.servlet;

import java.lang.reflect.Field;

public class Anchor extends AbstractControl {

	@Override
	protected Class<?> getView() {
		return View.Show.class;
	}

	public static class Factory extends AbstractControl.Factory<Anchor> {

		public Factory(Field field, Class<?> view) {
			super(Anchor.class, field, view);
		}
	}
}
