package com.backflipsource;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface LangHelper {

	String emptyString();

	boolean emptyString(String string);

	String safeString(Object object);

	String unsafeString(Object object);

	String trimString(String string);

	String capitalizeString(String string);

	<T> T nonNullInstance(T t1, T t2);

	<T> T nonNullInstance(T t1, Supplier<T> t2);

	List<String> getFieldNames(Class<?> class1);

	String getGetterName(String field);

	String getSetterName(String field);

	Method getGetter(Field field);

	Method getSetter(Field field);

	Map<String, Method> getGetters(Class<?> class1);

	Map<String, Method> getSetters(Class<?> class1);
}
