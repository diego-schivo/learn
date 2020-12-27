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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Stream;

public interface UtilHelper {

	<T extends Collection<U>, U> T collection(T collection);

	<T> boolean emptyArray(T[] array);

	boolean emptyCollection(Collection<?> collection);

	<T> T[] safeArray(Collection<T> collection, Class<T> class1);

	<T> T[] unsafeArray(Collection<T> collection, Class<T> class1);

	<T> List<T> safeList(T[] array);

	<T> List<T> unsafeList(T[] array);

	<T> Stream<T> safeStream(T[] array);

	<T> Stream<T> unsafeStream(T[] array);

	<T> Stream<T> safeStream(Collection<T> collection);

	<T> Stream<T> unsafeStream(Collection<T> collection);

	<T> Stream<T> safeStream(Enumeration<T> enumeration);

	<T> Stream<T> unsafeStream(Enumeration<T> enumeration);

	<T> Stream<T> safeStream(Iterable<T> iterable);

	<T> Stream<T> unsafeStream(Iterable<T> iterable);

	<T> Stream<T> safeStream(Iterator<T> iterator);

	<T> Stream<T> unsafeStream(Iterator<T> iterator);

	<K, V> Stream<Entry<K, V>> safeStream(Map<K, V> map);

	<K, V> Stream<Entry<K, V>> unsafeStream(Map<K, V> map);

	<T> Collector<T, ?, Set<T>> linkedHashSetCollector();

	<K, V> Map<K, V> safeMap(Map<K, V> map);

	<T, K, U> Collector<T, ?, Map<K, U>> linkedHashMapCollector(Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends U> valueMapper);

	void safeRun(RunnableThrowingException runnable);

	void unsafeRun(RunnableThrowingException runnable);

	<T> T safeGet(SupplierThrowingException<T> supplier);

	<T> T unsafeGet(SupplierThrowingException<T> supplier);

	Map<String, Function<Object, Object>> getGetterFunctions(Class<?> class1);

	Map<String, BiConsumer<Object, Object>> getSetterConsumers(Class<?> class1);

	Future<?>[] startExecutorService(ExecutorService executor, Callable<?>... tasks);

	void stopExecutorService(ExecutorService executor, Future<?>... futures);

	void watchDirectories(Path start, Consumer<Path[]> consumer);

	<T> T arrayGet(T[] array, int index);

	<T> void arraySet(T[] array, int index, T value);

	<T> T listGet(List<T> list, int index);

	<T> T listSet(List<T> list, int index, T value);

	<T> void collectionFill(Collection<T> collection, Stream<T> stream);

	<K, V> void mapFill(Map<K, V> map, Stream<Entry<K, V>> stream);

	Logger logger(Class<?> class1, Level level);

	<T> Set<T> linkedHashSet(T... values);

	@FunctionalInterface
	public interface RunnableThrowingException {

		void run() throws Exception;
	}

	@FunctionalInterface
	public interface ConsumerThrowingException<T> {

		void accept(T t) throws Exception;
	}

	@FunctionalInterface
	public interface SupplierThrowingException<T> {

		T get() throws Exception;
	}
}
