package com.backflipsource.servlet;

import static com.backflipsource.Helpers.nonEmptyString;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import com.backflipsource.servlet.DescriptionList.Factory;
import com.backflipsource.ui.AbstractEntityControl;
import com.backflipsource.ui.EntityDetail;
import com.backflipsource.ui.spec.EntityResource;
import com.backflipsource.ui.spec.EntityUI.Mode;

public class DescriptionList extends AbstractEntityControl<Factory> {

	@Override
	protected Class<? extends Mode> getMode() {
		return EntityDetail.class;
	}

	public static class Factory extends AbstractEntityControl.Factory<DescriptionList> {

		public Factory(AnnotatedElement annotated, Class<? extends Mode> mode) {
			super(DescriptionList.class, annotated, mode);
		}

//		public Factory(EntityResource entityView, String page) {
//			super(DescriptionList.class, null, null, null, nonEmptyString(page, "description-list.jsp"), null,
//					entityView);
//		}

//		public Factory(EntityResource entityView) {
//			this(entityView, null);
//		}
	}
}
