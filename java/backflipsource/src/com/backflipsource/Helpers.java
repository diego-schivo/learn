package com.backflipsource;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Stream;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.catalina.startup.Tomcat;

import com.backflipsource.UtilHelper.RunnableThrowingException;
import com.backflipsource.UtilHelper.SupplierThrowingException;

public class Helpers {

	private static LangHelper langHelper;

	private static UtilHelper utilHelper;

	private static ServletHelper servletHelper;

	private static TomcatHelper tomcatHelper;

	public static LangHelper getLangHelper() {
		if (langHelper == null) {
			langHelper = new DefaultLangHelper();
		}
		return langHelper;
	}

	public static UtilHelper getUtilHelper() {
		if (utilHelper == null) {
			utilHelper = new DefaultUtilHelper();
		}
		return utilHelper;
	}

	public static ServletHelper getServletHelper() {
		if (servletHelper == null) {
			servletHelper = new DefaultServletHelper();
		}
		return servletHelper;
	}

	public static TomcatHelper getTomcatHelper() {
		if (tomcatHelper == null) {
			tomcatHelper = new DefaultTomcatHelper();
		}
		return tomcatHelper;
	}

	public static String emptyString() {
		return getLangHelper().emptyString();
	}

	public static boolean emptyString(String string) {
		return getLangHelper().emptyString(string);
	}

	public static String safeString(Object object) {
		return getLangHelper().safeString(object);
	}

	public static String unsafeString(Object object) {
		return getLangHelper().unsafeString(object);
	}

	public static String nonEmptyString(String string1, String string2) {
		return getLangHelper().nonEmptyString(string1, string2);
	}

	public static String nonEmptyString(String string1, Supplier<String> string2) {
		return getLangHelper().nonEmptyString(string1, string2);
	}

	public static String trimString(String string) {
		return getLangHelper().trimString(string);
	}

	public static String capitalizeString(String string) {
		return getLangHelper().capitalizeString(string);
	}

	public static String uncapitalizeString(String string) {
		return getLangHelper().uncapitalizeString(string);
	}

	public static String[] splitString(String string, String delimiter) {
		return getLangHelper().splitString(string, delimiter);
	}

	public static String joinStrings(Stream<String> strings, String delimiter) {
		return getLangHelper().joinStrings(strings, delimiter);
	}

	public static String stringWithoutPrefix(String string, String prefix) {
		return getLangHelper().stringWithoutPrefix(string, prefix);
	}

	public static String stringWithoutSuffix(String string, String suffix) {
		return getLangHelper().stringWithoutSuffix(string, suffix);
	}

	public static String substringBeforeFirst(String string, String substr) {
		return getLangHelper().substringBeforeFirst(string, substr);
	}

	public static String substringAfterFirst(String string, String substr) {
		return getLangHelper().substringAfterFirst(string, substr);
	}

	public static String substringBeforeLast(String string, String substr) {
		return getLangHelper().substringBeforeLast(string, substr);
	}

	public static String substringAfterLast(String string, String substr) {
		return getLangHelper().substringAfterLast(string, substr);
	}

	public static String camelCaseString(String[] words) {
		return getLangHelper().camelCaseString(words);
	}

	public static Stream<String> camelCaseWords(String string) {
		return getLangHelper().camelCaseWords(string);
	}

	public static <T> T nonNullInstance(T t1, T t2) {
		return getLangHelper().nonNullInstance(t1, t2);
	}

	public static <T> T nonNullInstance(T t1, Supplier<T> t2) {
		return getLangHelper().nonNullInstance(t1, t2);
	}

	public static Stream<Field> classFields(Class<?> class1) {
		return getLangHelper().classFields(class1);
	}

	public static String getGetterName(String field) {
		return getLangHelper().getGetterName(field);
	}

	public static String getSetterName(String field) {
		return getLangHelper().getSetterName(field);
	}

	public static Method getGetter(Field field) {
		return getLangHelper().getGetter(field);
	}

	public static Method getSetter(Field field) {
		return getLangHelper().getSetter(field);
	}

	public static Map<String, Method> getGetters(Class<?> class1) {
		return getLangHelper().getGetters(class1);
	}

	public static Map<String, Method> getSetters(Class<?> class1) {
		return getLangHelper().getSetters(class1);
	}

	public static Stream<Class<?>> packageClasses(String package1) {
		return getLangHelper().packageClasses(package1);
	}

	public static Stream<Class<?>> packageClasses(String package1, boolean subpackages) {
		return getLangHelper().packageClasses(package1, subpackages);
	}

	public static List<Type> getArgumentTypes(Type type) {
		return getLangHelper().getArgumentTypes(type);
	}

	public static Object getFieldValue(Object instance, String field, Class<?> class1) {
		return getLangHelper().getFieldValue(instance, field, class1);
	}

	public static void setFieldValue(Object instance, String field, Object value, Class<?> class1) {
		getLangHelper().setFieldValue(instance, field, value, class1);
	}

	public static <A extends Annotation> Stream<Entry<String, Method>> annotationMethods(A annotation) {
		return getLangHelper().annotationMethods(annotation);
	}

	public static <A extends Annotation> Stream<Entry<String, Object>> annotationEntries(A annotation) {
		return getLangHelper().annotationEntries(annotation);
	}

	public static Stream<Annotation> repeatedAnnotations(Annotation annotation) {
		return getLangHelper().repeatedAnnotations(annotation);
	}

	public static <A extends Annotation, T> T annotationTypeInstance(A annotation,
			Function<A, Class<? extends T>> getClass, Class<? extends T> defaultClass) {
		return getLangHelper().annotationTypeInstance(annotation, getClass, defaultClass);
	}

	public static String classEnclosingName(Class<?> class1) {
		return getLangHelper().classEnclosingName(class1);
	}

	public static <T> boolean emptyArray(T[] array) {
		return getUtilHelper().emptyArray(array);
	}

	public static boolean emptyCollection(Collection<?> collection) {
		return getUtilHelper().emptyCollection(collection);
	}

	public static <T> T[] unsafeArray(Collection<T> collection, Class<T> class1) {
		return getUtilHelper().unsafeArray(collection, class1);
	}

	public static <T> T[] safeArray(Collection<T> collection, Class<T> class1) {
		return getUtilHelper().safeArray(collection, class1);
	}

	public static <T> List<T> safeList(T[] array) {
		return getUtilHelper().safeList(array);
	}

	public static <T> List<T> unsafeList(T[] array) {
		return getUtilHelper().unsafeList(array);
	}

	public static <T> Stream<T> safeStream(T[] array) {
		return getUtilHelper().safeStream(array);
	}

	public static <T> Stream<T> unsafeStream(T[] array) {
		return getUtilHelper().unsafeStream(array);
	}

	public static <T> Stream<T> safeStream(Collection<T> collection) {
		return getUtilHelper().safeStream(collection);
	}

	public static <T> Stream<T> unsafeStream(Collection<T> collection) {
		return getUtilHelper().unsafeStream(collection);
	}

	public static <T> Stream<T> safeStream(Enumeration<T> enumeration) {
		return getUtilHelper().safeStream(enumeration);
	}

	public static <T> Stream<T> unsafeStream(Enumeration<T> enumeration) {
		return getUtilHelper().unsafeStream(enumeration);
	}

	public static <T> Stream<T> safeStream(Iterable<T> iterable) {
		return getUtilHelper().safeStream(iterable);
	}

	public <T> Stream<T> unsafeStream(Iterable<T> iterable) {
		return getUtilHelper().unsafeStream(iterable);
	}

	public static <T> Stream<T> safeStream(Iterator<T> iterator) {
		return getUtilHelper().safeStream(iterator);
	}

	public static <T> Stream<T> unsafeStream(Iterator<T> iterator) {
		return getUtilHelper().unsafeStream(iterator);
	}

	public static <K, V> Stream<Entry<K, V>> safeStream(Map<K, V> map) {
		return getUtilHelper().safeStream(map);
	}

	public static <K, V> Stream<Entry<K, V>> unsafeStream(Map<K, V> map) {
		return getUtilHelper().unsafeStream(map);
	}

	public static <T> Collector<T, ?, Set<T>> linkedHashSetCollector() {
		return getUtilHelper().linkedHashSetCollector();
	}

	public static <K, V> Map<K, V> safeMap(Map<K, V> map) {
		return getUtilHelper().safeMap(map);
	}

	public static <T, K, U> Collector<T, ?, Map<K, U>> linkedHashMapCollector(
			Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper) {
		return getUtilHelper().linkedHashMapCollector(keyMapper, valueMapper);
	}

	public static void safeRun(RunnableThrowingException runnable) {
		getUtilHelper().safeRun(runnable);
	}

	public static void unsafeRun(RunnableThrowingException runnable) {
		getUtilHelper().unsafeRun(runnable);
	}

	public static <T> T safeGet(SupplierThrowingException<T> supplier) {
		return getUtilHelper().safeGet(supplier);
	}

	public static <T> T unsafeGet(SupplierThrowingException<T> supplier) {
		return getUtilHelper().unsafeGet(supplier);
	}

	public static Map<String, Function<Object, Object>> getGetterFunctions(Class<?> class1) {
		return getUtilHelper().getGetterFunctions(class1);
	}

	public static Map<String, BiConsumer<Object, Object>> getSetterConsumers(Class<?> class1) {
		return getUtilHelper().getSetterConsumers(class1);
	}

	public static Future<?>[] startExecutorService(ExecutorService executor, Callable<?>... tasks) {
		return getUtilHelper().startExecutorService(executor, tasks);
	}

	public static void stopExecutorService(ExecutorService executor, Future<?>... futures) {
		getUtilHelper().stopExecutorService(executor, futures);
	}

	public static void watchDirectories(Path start, Consumer<Path[]> consumer) {
		getUtilHelper().watchDirectories(start, consumer);
	}

	public static <T> T arrayGet(T[] array, int index) {
		return getUtilHelper().arrayGet(array, index);
	}

	public static <T> void arraySet(T[] array, int index, T value) {
		getUtilHelper().arraySet(array, index, value);
	}

	public static <T> T listGet(List<T> list, int index) {
		return getUtilHelper().listGet(list, index);
	}

	public <T> T listSet(List<T> list, int index, T value) {
		return getUtilHelper().listSet(list, index, value);
	}

	public static <T> void collectionFill(Collection<T> collection, Stream<T> stream) {
		getUtilHelper().collectionFill(collection, stream);
	}

	public static <K, V> void mapFill(Map<K, V> map, Stream<Entry<K, V>> stream) {
		getUtilHelper().mapFill(map, stream);
	}

	public static Logger logger(Class<?> class1, Level level) {
		return getUtilHelper().logger(class1, level);
	}

	public static void forwardServletRequest(String path, ServletRequest request, ServletResponse response) {
		getServletHelper().forwardServletRequest(path, request, response);
	}

	public static void initTomcat(Tomcat tomcat, int port, String root, String classes) {
		getTomcatHelper().initTomcat(tomcat, port, root, classes);
	}
}
