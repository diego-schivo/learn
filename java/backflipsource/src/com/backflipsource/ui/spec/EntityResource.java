package com.backflipsource.ui.spec;

import java.util.Map;

import com.backflipsource.Control;
import com.backflipsource.servlet.StringConverter;
import com.backflipsource.ui.spec.EntityUI.Mode;

public interface EntityResource {

	String getUri();

	Class<?> getEntity();

	Class<?> entity(Class<? extends Mode> mode);

	Map<String, Control.Factory<?>> controlFactories(Class<? extends Mode> mode);

	Map<String, StringConverter<?>> converters(Class<? extends Mode> mode);
}
