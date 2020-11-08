package com.backflipsource;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
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

	public static String trimString(String string) {
		return getLangHelper().trimString(string);
	}

	public static String capitalizeString(String string) {
		return getLangHelper().capitalizeString(string);
	}

	public static String[] splitString(String string, int character) {
		return getLangHelper().splitString(string, character);
	}

	public static <T> T nonNullInstance(T t1, T t2) {
		return getLangHelper().nonNullInstance(t1, t2);
	}

	public static <T> T nonNullInstance(T t1, Supplier<T> t2) {
		return getLangHelper().nonNullInstance(t1, t2);
	}

	public static List<Field> getFields(Class<?> class1) {
		return getLangHelper().getFields(class1);
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

	public static Stream<Class<?>> getClasses(String packageName) {
		return getLangHelper().getClasses(packageName);
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

	public static <K, V> Stream<Entry<K, V>> safeStream(Map<K, V> map) {
		return getUtilHelper().safeStream(map);
	}

	public static <K, V> Stream<Entry<K, V>> unsafeStream(Map<K, V> map) {
		return getUtilHelper().unsafeStream(map);
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

	public static void forwardServletRequest(String path, ServletRequest request, ServletResponse response) {
		getServletHelper().forwardServletRequest(path, request, response);
	}

	public static void initTomcat(Tomcat tomcat, String root, String classes) {
		getTomcatHelper().initTomcat(tomcat, root, classes);
	}
}
