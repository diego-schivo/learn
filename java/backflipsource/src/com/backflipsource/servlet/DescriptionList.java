package com.backflipsource.servlet;

import java.lang.reflect.Field;
import java.util.Collection;

public class DescriptionList extends AbstractControl {

	@SuppressWarnings("rawtypes")
	private Collection<Control.Factory> factories;

	@SuppressWarnings("rawtypes")
	public Collection<Control.Factory> getFactories() {
		if (factories == null) {
			factories = getEntityView().controlFactories(View.Show.class).values();
			factories.forEach(factory -> factory.setParent(this));
		}
		return factories;
	}

	public static class Factory extends AbstractFactory<DescriptionList> {

		public Factory(Field field, View.Field annotation) {
			super(DescriptionList.class, field, annotation);
		}

		public Factory(EntityView entityView) {
			super(DescriptionList.class, entityView, null, null, null, "dl.jsp");
		}
	}
}
