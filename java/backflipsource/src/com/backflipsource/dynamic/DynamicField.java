package com.backflipsource.dynamic;

import java.lang.reflect.Type;

public interface DynamicField extends DynamicAnnotated {

	Class<?> getType();

	Type getGenericType();

	<T> T getValue(Object instance);

	<T> void setValue(Object instance, T value);
}
