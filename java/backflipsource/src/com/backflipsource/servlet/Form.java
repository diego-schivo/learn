package com.backflipsource.servlet;

import java.lang.reflect.Field;

public class Form extends AbstractControl {

	@Override
	protected Class<?> getView() {
		return View.Edit.class;
	}

	public static class Factory extends AbstractControl.Factory<Form> {

		public Factory(Field field, View.Field annotation) {
			super(Form.class, field, annotation);
		}

		public Factory(EntityView entityView) {
			super(Form.class, entityView);
		}
	}
}
