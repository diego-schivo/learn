package com.backflipsource.dynamic;

import java.util.stream.Stream;

public interface DynamicClass extends DynamicAnnotated {

	String getFullName();

	Stream<DynamicField> fields();

	<T> T newInstance();
}
