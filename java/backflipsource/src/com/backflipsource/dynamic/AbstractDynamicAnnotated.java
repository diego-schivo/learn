package com.backflipsource.dynamic;

import static com.backflipsource.Helpers.classEnclosingName;
import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.safeString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractDynamicAnnotated implements DynamicAnnotated {

	protected String name;

	protected List<DynamicAnnotation> annotations = new ArrayList<>();

	@Override
	public String getName() {
		return name;
	}

	@Override
	public DynamicAnnotation annotation(String name) {
		return annotations(name).findFirst().orElse(null);
	}

	@Override
	public Stream<DynamicAnnotation> annotations(String name) {
		return safeStream(annotations).filter(annotation -> safeString(annotation.getName()).equals(name));
	}

	@Override
	public String toString() {
		return classEnclosingName(getClass()) + "(name=" + name + ")";
	}
}
