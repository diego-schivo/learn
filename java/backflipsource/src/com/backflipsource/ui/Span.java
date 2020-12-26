package com.backflipsource.ui;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import com.backflipsource.ui.spec.EntityUI.Mode;

public class Span extends AbstractEntityControl {

	public boolean isMultiple() {
		return ((Factory) factory).multiple;
	}

//	@Override
//	protected Class<? extends Mode> getMode() {
//		return EntityDetail.class;
//	}

	public static class Factory extends AbstractEntityControl.Factory<Span> {

		protected boolean multiple;

		public Factory() {
			setControl(Span.class);

//			if (annotated instanceof Field) {
//				multiple = Iterable.class.isAssignableFrom(((Field) annotated).getType());
//			}
		}
	}
}
