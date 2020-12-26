package com.backflipsource.ui;

public class Span extends AbstractEntityControl {

	public boolean isMultiple() {
		return ((Factory) factory).multiple;
	}

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
