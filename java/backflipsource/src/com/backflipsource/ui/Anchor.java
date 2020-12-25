package com.backflipsource.ui;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import com.backflipsource.ui.spec.EntityUI.Mode;

public class Anchor extends AbstractEntityControl {

	@Override
	protected Class<? extends Mode> getMode() {
		return EntityDetail.class;
	}

	public static class Factory extends AbstractEntityControl.Factory<Anchor> {

		public Factory(AnnotatedElement annotated, Class<? extends Mode> mode) {
			super(Anchor.class, annotated, mode);
		}
	}
}
