package com.backflipsource.servlet;

import static com.backflipsource.Helpers.nonEmptyString;

import java.lang.reflect.Field;

public class DescriptionList extends AbstractControl {

	@Override
	protected Class<?> getView() {
		return View.Show.class;
	}

	public static class Factory extends AbstractControl.Factory<DescriptionList> {

		public Factory(Field field, Class<?> view) {
			super(DescriptionList.class, field, view);
		}

		public Factory(EntityView entityView, String page) {
			super(DescriptionList.class, entityView, null, null, null, nonEmptyString(page, "description-list.jsp"));
		}

		public Factory(EntityView entityView) {
			this(entityView, null);
		}
	}
}
