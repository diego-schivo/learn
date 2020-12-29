package com.backflipsource.dynamic;

import java.util.stream.Stream;

public interface DynamicAnnotated {

	String getName();

	DynamicAnnotation annotation(String name);

	Stream<DynamicAnnotation> annotations(String name);
}
