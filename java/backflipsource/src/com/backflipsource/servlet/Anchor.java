package com.backflipsource.servlet;

import java.lang.reflect.Field;

public class Anchor extends Control {

	public static class Factory extends Control.Factory<Anchor> {

		public Factory(Field field, View.Field annotation) {
			super(Anchor.class, field, annotation);
		}
	}
}
