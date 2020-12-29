package com.backflipsource;

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
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class StaticUtilHelper {

	private static UtilHelper instance;

	public static UtilHelper getInstance() {
		if (instance == null) {
			instance = new DefaultUtilHelper();
		}
		return instance;
	}

	public static <T extends Collection<U>, U> T collection(T collection) {
		return getInstance().collection(collection);
	}
	public static <T> boolean emptyArray(T[] array) {
		return getInstance().emptyArray(array);
	}
	public static boolean emptyCollection(Collection<?> collection) {
		return getInstance().emptyCollection(collection);
	}
	public static <T> T[] safeArray(Collection<T> collection, Class<T> class1) {
		return getInstance().safeArray(collection, class1);
	}
	public static <T> T[] unsafeArray(Collection<T> collection, Class<T> class1) {
		return getInstance().unsafeArray(collection, class1);
	}
	public static <T> List<T> safeList(T[] array) {
		return getInstance().safeList(array);
	}
	public static <T> List<T> unsafeList(T[] array) {
		return getInstance().unsafeList(array);
	}
	public static <T> Stream<T> safeStream(T[] array) {
		return getInstance().safeStream(array);
	}
	public static <T> Stream<T> unsafeStream(T[] array) {
		return getInstance().unsafeStream(array);
	}
	public static <T> Stream<T> safeStream(Collection<T> collection) {
		return getInstance().safeStream(collection);
	}
	public static <T> Stream<T> unsafeStream(Collection<T> collection) {
		return getInstance().unsafeStream(collection);
	}
	public static <T> Stream<T> safeStream(Enumeration<T> enumeration) {
		return getInstance().safeStream(enumeration);
	}
	public static <T> Stream<T> unsafeStream(Enumeration<T> enumeration) {
		return getInstance().unsafeStream(enumeration);
	}
	public static <T> Stream<T> safeStream(Iterable<T> iterable) {
		return getInstance().safeStream(iterable);
	}
	public static <T> Stream<T> unsafeStream(Iterable<T> iterable) {
		return getInstance().unsafeStream(iterable);
	}
	public static <T> Stream<T> safeStream(Iterator<T> iterator) {
		return getInstance().safeStream(iterator);
	}
	public static <T> Stream<T> unsafeStream(Iterator<T> iterator) {
		return getInstance().unsafeStream(iterator);
	}
	public static <K, V> Stream<Entry<K, V>> safeStream(Map<K, V> map) {
		return getInstance().safeStream(map);
	}
	public static <K, V> Stream<Entry<K, V>> unsafeStream(Map<K, V> map) {
		return getInstance().unsafeStream(map);
	}
	public static <T> Collector<T, ?, Set<T>> linkedHashSetCollector() {
		return getInstance().linkedHashSetCollector();
	}
	public static <K, V> Map<K, V> safeMap(Map<K, V> map) {
		return getInstance().safeMap(map);
	}
	public static void safeRun(RunnableThrowingException runnable) {
		getInstance().safeRun(runnable);
	}
	public static void unsafeRun(RunnableThrowingException runnable) {
		getInstance().unsafeRun(runnable);
	}
	public static <T> T safeGet(SupplierThrowingException<T> supplier) {
		return getInstance().safeGet(supplier);
	}
	public static <T> T unsafeGet(SupplierThrowingException<T> supplier) {
		return getInstance().unsafeGet(supplier);
	}
	public static <T, R> R safeApply(FunctionThrowingException<T, R> function, T t) {
		return getInstance().safeApply(function, t);
	}
	public static <T, R> R unsafeApply(FunctionThrowingException<T, R> function, T t) {
		return getInstance().unsafeApply(function, t);
	}
	public static <T, R> Function<T, R> safeFunction(FunctionThrowingException<T, R> function) {
		return getInstance().safeFunction(function);
	}
	public static <T, R> Function<T, R> unsafeFunction(FunctionThrowingException<T, R> function) {
		return getInstance().unsafeFunction(function);
	}
	public static Map<String, Function<Object, Object>> getGetterFunctions(Class<?> class1) {
		return getInstance().getGetterFunctions(class1);
	}
	public static Map<String, BiConsumer<Object, Object>> getSetterConsumers(Class<?> class1) {
		return getInstance().getSetterConsumers(class1);
	}
	public static Future<?>[] startExecutorService(ExecutorService executor, Callable<?>... tasks) {
		return getInstance().startExecutorService(executor, tasks);
	}
	public static void stopExecutorService(ExecutorService executor, Future<?>... futures) {
		getInstance().stopExecutorService(executor, futures);
	}
	public static void watchDirectories(Path start, Consumer<Path[]> consumer) {
		getInstance().watchDirectories(start, consumer);
	}
	public static <T> T arrayGet(T[] array, int index) {
		return getInstance().arrayGet(array, index);
	}
	public static <T> void arraySet(T[] array, int index, T value) {
		getInstance().arraySet(array, index, value);
	}
	public static <T> T listGet(List<T> list, int index) {
		return getInstance().listGet(list, index);
	}
	public static <T> T listSet(List<T> list, int index, T value) {
		return getInstance().listSet(list, index, value);
	}
	public static <T> void collectionFill(Collection<T> collection, Stream<T> stream) {
		getInstance().collectionFill(collection, stream);
	}
	public static <K, V> void mapFill(Map<K, V> map, Stream<Entry<K, V>> stream) {
		getInstance().mapFill(map, stream);
	}
	public static Logger logger(Class<?> class1, Level level) {
		return getInstance().logger(class1, level);
	}
	public static <T> Set<T> linkedHashSet(T... values) {
		return getInstance().linkedHashSet(values);
	}
	public static <T> Iterator<T> iterator(BooleanSupplier hasNext, Supplier<T> next) {
		return getInstance().iterator(hasNext, next);
	}
	public static <K, V> Map<K, V> linkedHashMap(Entry<K, V>... entries) {
		return getInstance().linkedHashMap(entries);
	}
}
