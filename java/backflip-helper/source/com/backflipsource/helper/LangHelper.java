package com.backflipsource.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface LangHelper {

	String emptyString();

	boolean emptyString(String string);

	String string(Object object);

	String unsafeString(Object object);

	String nonEmptyString(String string1, String string2);

	String nonEmptyString(String string1, Supplier<String> string2);

	String trimString(String string);

	String capitalizeString(String string);

	String uncapitalizeString(String string);

	String[] splitString(String string, String delimiter);

	String joinStrings(Stream<String> strings, String delimiter);

	String stringWithoutPrefix(String string, String prefix);

	String stringWithoutSuffix(String string, String suffix);

	String substringBeforeFirst(String string, String substr);

	String substringAfterFirst(String string, String substr);

	String substringBeforeLast(String string, String substr);

	String substringAfterLast(String string, String substr);

	String camelCaseString(String[] words);

	Stream<String> camelCaseWords(String string);

	<T> T nonNullInstance(T t1, T t2);

	<T> T nonNullInstance(T t1, Supplier<T> t2);

	Stream<Field> classFields(Class<?> class1);

	String getGetterName(String field);

	String getSetterName(String field);

	Method getGetter(Field field);

	Method getSetter(Field field);

	Map<String, Method> getGetters(Class<?> class1);

	Map<String, Method> getSetters(Class<?> class1);

	Stream<Class<?>> packageClasses(String package1);

	Stream<Class<?>> packageClasses(String package1, boolean subpackages);

	List<Type> getArgumentTypes(Type type);

	Object getFieldValue(Object instance, String field, Class<?> class1);

	void setFieldValue(Object instance, String field, Object value, Class<?> class1);

	<A extends Annotation> Stream<Entry<String, Method>> annotationMethods(A annotation);

	<A extends Annotation> Stream<Entry<String, Object>> annotationEntries(A annotation);
	
	Stream<Annotation> repeatedAnnotations(Annotation annotation);

	<A extends Annotation, T> T annotationTypeInstance(A annotation, Function<A, Class<? extends T>> getClass,
			Class<? extends T> defaultClass);

	String classEnclosingName(Class<?> class1);
}
