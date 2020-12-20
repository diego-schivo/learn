package com.backflipsource.servlet;

import java.lang.reflect.Field;

public class Anchor extends AbstractControl {

	public static class Factory extends AbstractFactory<Anchor> {

		public Factory(Field field, View.Field annotation) {
			super(Anchor.class, field, annotation);
		}
	}
}
