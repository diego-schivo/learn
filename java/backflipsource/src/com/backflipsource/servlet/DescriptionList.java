package com.backflipsource.servlet;

import java.lang.reflect.Field;

public class DescriptionList extends AbstractControl {

	@Override
	protected Class<?> getView() {
		return View.Show.class;
	}

	public static class Factory extends AbstractControl.Factory<DescriptionList> {

		public Factory(Field field, View.Field annotation) {
			super(DescriptionList.class, field, annotation);
		}

		public Factory(EntityView entityView) {
			super(DescriptionList.class, entityView, null, null, null, "description-list.jsp");
		}
	}
}
