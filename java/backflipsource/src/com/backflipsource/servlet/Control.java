package com.backflipsource.servlet;

import java.util.List;

public interface Control {

	EntityView getEntityView();

	Object getItem();

	String getName();

	List<String> getValues();

	String getPage();

	String getValue();

	Control getParent();

	String getHeading();

	public interface Factory<T extends Control> {

		void setParent(Control parent);

		T create(Object target);
	}
}
