package com.backflipsource.ui.spec;

import java.util.Map;

import com.backflipsource.Control;
import com.backflipsource.dynamic.DynamicClass;
import com.backflipsource.servlet.StringConverter;
import com.backflipsource.ui.spec.EntityUI.Mode;

public interface EntityResource {

	String getUri();

	DynamicClass getEntity();

	DynamicClass entity(Class<? extends Mode> mode);

	Map<String, Control.Factory<?>> controlFactories(Class<? extends Mode> mode);

	Map<String, StringConverter<?>> converters(Class<? extends Mode> mode);
}
