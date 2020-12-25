package com.backflipsource;

import java.util.Collection;
import java.util.List;

public interface Control {

	Object getTarget();

	String getName();

	List<String> getValues();

	String getPage();

	String getValue();

	Control getParent();

	String getHeading();

	String getTextKey();

	String text(String key);

	Collection<Control.Factory<?>> getFactories();

	public interface Factory<T extends Control> {

		String getHeading();

		void setParent(Control parent);

		T create(Object target);
	}
}
