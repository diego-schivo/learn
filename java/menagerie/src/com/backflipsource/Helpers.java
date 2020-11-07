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

	public static LangHelper langHelper;

	public static UtilHelper utilHelper;

	public static ServletHelper servletHelper;

	public static TomcatHelper tomcatHelper;

	public static String emptyString() {
		return langHelper.emptyString();
	}

	public static boolean emptyString(String string) {
		return langHelper.emptyString(string);
	}

	public static String safeString(Object object) {
		return langHelper.safeString(object);
	}

	public static String unsafeString(Object object) {
		return langHelper.unsafeString(object);
	}

	public static String nonEmptyString(String string1, String string2) {
		return langHelper.nonEmptyString(string1, string2);
	}

	public static String trimString(String string) {
		return langHelper.trimString(string);
	}

	public static String capitalizeString(String string) {
		return langHelper.capitalizeString(string);
	}

	public static String[] splitString(String string, int character) {
		return langHelper.splitString(string, character);
	}

	public static <T> T nonNullInstance(T t1, T t2) {
		return langHelper.nonNullInstance(t1, t2);
	}

	public static <T> T nonNullInstance(T t1, Supplier<T> t2) {
		return langHelper.nonNullInstance(t1, t2);
	}

	public static List<Field> getFields(Class<?> class1) {
		return langHelper.getFields(class1);
	}

	public static String getGetterName(String field) {
		return langHelper.getGetterName(field);
	}

	public static String getSetterName(String field) {
		return langHelper.getSetterName(field);
	}

	public static Method getGetter(Field field) {
		return langHelper.getGetter(field);
	}

	public static Method getSetter(Field field) {
		return langHelper.getSetter(field);
	}

	public static Map<String, Method> getGetters(Class<?> class1) {
		return langHelper.getGetters(class1);
	}

	public static Map<String, Method> getSetters(Class<?> class1) {
		return langHelper.getSetters(class1);
	}

	public static <T> T[] unsafeArray(Collection<T> collection, Class<T> class1) {
		return utilHelper.unsafeArray(collection, class1);
	}

	public static <T> T[] safeArray(Collection<T> collection, Class<T> class1) {
		return utilHelper.safeArray(collection, class1);
	}

	public static <T> List<T> safeList(T[] array) {
		return utilHelper.safeList(array);
	}

	public static <T> List<T> unsafeList(T[] array) {
		return utilHelper.unsafeList(array);
	}

	public static <T> Stream<T> safeStream(T[] array) {
		return utilHelper.safeStream(array);
	}

	public static <T> Stream<T> unsafeStream(T[] array) {
		return utilHelper.unsafeStream(array);
	}

	public static <T> Stream<T> safeStream(Collection<T> collection) {
		return utilHelper.safeStream(collection);
	}

	public static <T> Stream<T> unsafeStream(Collection<T> collection) {
		return utilHelper.unsafeStream(collection);
	}

	public static <T> Stream<T> safeStream(Enumeration<T> enumeration) {
		return utilHelper.safeStream(enumeration);
	}

	public static <T> Stream<T> unsafeStream(Enumeration<T> enumeration) {
		return utilHelper.unsafeStream(enumeration);
	}

	public static <K, V> Stream<Entry<K, V>> safeStream(Map<K, V> map) {
		return utilHelper.safeStream(map);
	}

	public static <K, V> Stream<Entry<K, V>> unsafeStream(Map<K, V> map) {
		return utilHelper.unsafeStream(map);
	}

	public static <T, K, U> Collector<T, ?, Map<K, U>> linkedHashMapCollector(
			Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper) {
		return utilHelper.linkedHashMapCollector(keyMapper, valueMapper);
	}

	public static void safeRun(RunnableThrowingException runnable) {
		utilHelper.safeRun(runnable);
	}

	public static void unsafeRun(RunnableThrowingException runnable) {
		utilHelper.unsafeRun(runnable);
	}

	public static <T> T safeGet(SupplierThrowingException<T> supplier) {
		return utilHelper.safeGet(supplier);
	}

	public static <T> T unsafeGet(SupplierThrowingException<T> supplier) {
		return utilHelper.unsafeGet(supplier);
	}

	public static Map<String, Function<Object, Object>> getGetterFunctions(Class<?> class1) {
		return utilHelper.getGetterFunctions(class1);
	}

	public static Map<String, BiConsumer<Object, Object>> getSetterConsumers(Class<?> class1) {
		return utilHelper.getSetterConsumers(class1);
	}

	public static Future<?>[] startExecutorService(ExecutorService executor, Callable<?>... tasks) {
		return utilHelper.startExecutorService(executor, tasks);
	}

	public static void stopExecutorService(ExecutorService executor, Future<?>... futures) {
		utilHelper.stopExecutorService(executor, futures);
	}

	public static void watchDirectories(Path start, Consumer<Path[]> consumer) {
		utilHelper.watchDirectories(start, consumer);
	}

	public static void forwardServletRequest(String path, ServletRequest request, ServletResponse response) {
		servletHelper.forwardServletRequest(path, request, response);
	}

	public static void initTomcat(Tomcat tomcat, String root, String classes) {
		tomcatHelper.initTomcat(tomcat, root, classes);
	}
}
