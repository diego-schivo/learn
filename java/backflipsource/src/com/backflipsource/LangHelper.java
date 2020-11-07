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

	String nonEmptyString(String string1, String string2);

	String trimString(String string);

	String capitalizeString(String string);

	String[] splitString(String string, int character);

	<T> T nonNullInstance(T t1, T t2);

	<T> T nonNullInstance(T t1, Supplier<T> t2);

	List<Field> getFields(Class<?> class1);

	String getGetterName(String field);

	String getSetterName(String field);

	Method getGetter(Field field);

	Method getSetter(Field field);

	Map<String, Method> getGetters(Class<?> class1);

	Map<String, Method> getSetters(Class<?> class1);
}