package com.backflipsource.servlet;

import java.lang.reflect.Field;

public class Anchor extends Control {

	@SuppressWarnings("rawtypes")
	public static class Factory extends Control.Factory<Anchor> {

		public Factory(Field field, StringConverter converter) {
			super(Anchor.class, field, converter);
		}
	}
}
