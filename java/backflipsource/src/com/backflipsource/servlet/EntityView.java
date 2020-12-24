package com.backflipsource.servlet;

import java.util.Map;

public interface EntityView {

	Class<?> getEntity();

	String getUri();

	Class<?> entity(Class<?> view);

	Map<String, Control.Factory<?>> controlFactories(Class<?> view);

	Map<String, StringConverter<?>> converters(Class<?> view);
}
