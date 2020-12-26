package com.backflipsource;

import static com.backflipsource.Helpers.annotationEntries;
import static com.backflipsource.Helpers.mapFill;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

import com.backflipsource.dynamic.DynamicAnnotation;

public class DefaultDynamicAnnotation implements DynamicAnnotation {

	protected Annotation target;

	protected String name;

	protected Map<String, Object> values = new LinkedHashMap<>();

	public DefaultDynamicAnnotation() {
	}

	public DefaultDynamicAnnotation(Annotation target) {
		setTarget(target);
	}

	public void setTarget(Annotation target) {
		this.target = target;
		name = target.annotationType().getSimpleName();
		mapFill(values, annotationEntries(target));
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getValue(String name) {
		return (T) values.get(name);
	}
}
