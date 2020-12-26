package com.backflipsource.dynamic;

public interface DynamicAnnotation {

	String getName();

	<T> T getValue(String name);
}
