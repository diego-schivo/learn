package com.backflipsource.ui;

import com.backflipsource.ui.spec.EntityUI.Mode;

public class Input extends AbstractEntityControl {

	public static String TYPE_DATE = "date";
	public static String TYPE_HIDDEN = "hidden";
	public static String TYPE_TEXT = "text";

	public String getType() {
		return ((Factory) factory).type;
	}

//	@Override
//	protected Class<? extends Mode> getMode() {
//		return EntityForm.class;
//	}

	public static class Factory extends AbstractEntityControl.Factory<Input> {

		protected String type;

//		public Factory(AnnotatedElement annotated, Class<? extends Mode> mode) {
//			super(Input.class, annotated, mode);
//
//			if (getContextListener().getViews().containsKey(field.getType())) {
//				type = TYPE_HIDDEN;
//			} else if (LocalDate.class.isAssignableFrom(field.getType())) {
//				type = TYPE_DATE;
//			} else {
//				type = TYPE_TEXT;
//			}
//		}

		public Factory() {
			setControl(Input.class);
			type = TYPE_TEXT;
		}
	}
}
