package com.backflipsource;

import java.util.stream.Stream;

public interface DynamicClass {

	Stream<? extends DynamicField> fields();
}
