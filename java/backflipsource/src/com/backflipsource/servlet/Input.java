package com.backflipsource.servlet;

import static com.backflipsource.servlet.EntityServlet.getContextListener;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class Input extends AbstractControl {

	public static String TYPE_DATE = "date";
	public static String TYPE_HIDDEN = "hidden";
	public static String TYPE_TEXT = "text";

	public String getType() {
		return ((Factory) factory).type;
	}

	@Override
	protected Class<?> getView() {
		return View.Edit.class;
	}

	public static class Factory extends AbstractControl.Factory<Input> {

		protected String type;

		public Factory(Field field, Class<?> view) {
			super(Input.class, field, view);

//			if (getContextListener().getViews().containsKey(field.getType())) {
//				type = TYPE_HIDDEN;
//			} else if (LocalDate.class.isAssignableFrom(field.getType())) {
//				type = TYPE_DATE;
//			} else {
//				type = TYPE_TEXT;
//			}
		}
	}
}
