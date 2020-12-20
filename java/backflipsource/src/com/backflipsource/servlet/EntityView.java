package com.backflipsource.servlet;

import java.util.Map;

public interface EntityView {

	String getUri();

	@SuppressWarnings("rawtypes")
	public Map<String, Control.Factory> controlFactories(Class<?> view);

	public Map<String, StringConverter<?>> converters(Class<?> view);
}
