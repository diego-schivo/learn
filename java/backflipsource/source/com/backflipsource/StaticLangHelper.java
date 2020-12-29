package com.backflipsource;

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

public class StaticLangHelper {

	private static LangHelper instance;

	public static LangHelper getInstance() {
		if (instance == null) {
			instance = new DefaultLangHelper();
		}
		return instance;
	}

	public static String emptyString() {
		return getInstance().emptyString();
	}
	public static boolean emptyString(String string) {
		return getInstance().emptyString(string);
	}
	public static String string(Object object) {
		return getInstance().string(object);
	}
	public static String unsafeString(Object object) {
		return getInstance().unsafeString(object);
	}
	public static String nonEmptyString(String string1, String string2) {
		return getInstance().nonEmptyString(string1, string2);
	}
	public static String nonEmptyString(String string1, Supplier<String> string2) {
		return getInstance().nonEmptyString(string1, string2);
	}
	public static String trimString(String string) {
		return getInstance().trimString(string);
	}
	public static String capitalizeString(String string) {
		return getInstance().capitalizeString(string);
	}
	public static String uncapitalizeString(String string) {
		return getInstance().uncapitalizeString(string);
	}
	public static String[] splitString(String string, String delimiter) {
		return getInstance().splitString(string, delimiter);
	}
	public static String joinStrings(Stream<String> strings, String delimiter) {
		return getInstance().joinStrings(strings, delimiter);
	}
	public static String stringWithoutPrefix(String string, String prefix) {
		return getInstance().stringWithoutPrefix(string, prefix);
	}
	public static String stringWithoutSuffix(String string, String suffix) {
		return getInstance().stringWithoutSuffix(string, suffix);
	}
	public static String substringBeforeFirst(String string, String substr) {
		return getInstance().substringBeforeFirst(string, substr);
	}
	public static String substringAfterFirst(String string, String substr) {
		return getInstance().substringAfterFirst(string, substr);
	}
	public static String substringBeforeLast(String string, String substr) {
		return getInstance().substringBeforeLast(string, substr);
	}
	public static String substringAfterLast(String string, String substr) {
		return getInstance().substringAfterLast(string, substr);
	}
	public static String camelCaseString(String[] words) {
		return getInstance().camelCaseString(words);
	}
	public static Stream<String> camelCaseWords(String string) {
		return getInstance().camelCaseWords(string);
	}
	public static <T> T nonNullInstance(T t1, T t2) {
		return getInstance().nonNullInstance(t1, t2);
	}
	public static <T> T nonNullInstance(T t1, Supplier<T> t2) {
		return getInstance().nonNullInstance(t1, t2);
	}
	public static Stream<Field> classFields(Class<?> class1) {
		return getInstance().classFields(class1);
	}
	public static String getGetterName(String field) {
		return getInstance().getGetterName(field);
	}
	public static String getSetterName(String field) {
		return getInstance().getSetterName(field);
	}
	public static Method getGetter(Field field) {
		return getInstance().getGetter(field);
	}
	public static Method getSetter(Field field) {
		return getInstance().getSetter(field);
	}
	public static Map<String, Method> getGetters(Class<?> class1) {
		return getInstance().getGetters(class1);
	}
	public static Map<String, Method> getSetters(Class<?> class1) {
		return getInstance().getSetters(class1);
	}
	public static Stream<Class<?>> packageClasses(String package1) {
		return getInstance().packageClasses(package1);
	}
	public static Stream<Class<?>> packageClasses(String package1, boolean subpackages) {
		return getInstance().packageClasses(package1, subpackages);
	}
	public static List<Type> getArgumentTypes(Type type) {
		return getInstance().getArgumentTypes(type);
	}
	public static Object getFieldValue(Object instance, String field, Class<?> class1) {
		return getInstance().getFieldValue(instance, field, class1);
	}
	public static void setFieldValue(Object instance, String field, Object value, Class<?> class1) {
		getInstance().setFieldValue(instance, field, value, class1);
	}
	public static <A extends Annotation> Stream<Entry<String, Method>> annotationMethods(A annotation) {
		return getInstance().annotationMethods(annotation);
	}
	public static <A extends Annotation> Stream<Entry<String, Object>> annotationEntries(A annotation) {
		return getInstance().annotationEntries(annotation);
	}
	public static Stream<Annotation> repeatedAnnotations(Annotation annotation) {
		return getInstance().repeatedAnnotations(annotation);
	}
	public static String classEnclosingName(Class<?> class1) {
		return getInstance().classEnclosingName(class1);
	}
}
