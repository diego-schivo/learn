package com.backflipsource;

import java.util.Collection;
import java.util.List;

public interface Control {

	Control getParent();

	Object getTarget();

	String getName();

	List<String> getValues();

	String getPage();

	String getValue();

	String getHeading();

	String getTextKey();

	String text(String key);

	// Collection<Control.Factory<?>> childFactories();

	void init();

	public interface Factory<T extends Control> {

		String getHeading();

		T newControl(Object target, Control parent);
	}
}
