package com.backflipsource.ui;

import java.lang.reflect.AnnotatedElement;

import com.backflipsource.ui.Form.Factory;
import com.backflipsource.ui.spec.EntityUI.Mode;

public class Form extends AbstractEntityControl<Factory> {

	@Override
	protected Class<? extends Mode> getMode() {
		return EntityForm.class;
	}

	public static class Factory extends AbstractEntityControl.Factory<Form> {

		public Factory(AnnotatedElement annotated, Class<? extends Mode> mode) {
			super(Form.class, annotated, mode);
		}

//		public Factory(EntityResource entityView) {
//			super(Form.class, entityView);
//		}
	}
}
