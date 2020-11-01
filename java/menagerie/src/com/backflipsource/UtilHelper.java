package com.backflipsource;

import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

public interface UtilHelper {

	<T> List<T> safeList(T[] array);

	<T> List<T> unsafeList(T[] array);

	<T> Stream<T> safeStream(T[] array);

	<T> Stream<T> unsafeStream(T[] array);

	<T> Stream<T> safeStream(Collection<T> collection);

	<T> Stream<T> unsafeStream(Collection<T> collection);

	<T> Stream<T> safeStream(Enumeration<T> enumeration);

	<T> Stream<T> unsafeStream(Enumeration<T> enumeration);

	<K, V> Stream<Entry<K, V>> safeStream(Map<K, V> map);

	<K, V> Stream<Entry<K, V>> unsafeStream(Map<K, V> map);

	<T, K, U> Collector<T, ?, Map<K, U>> linkedHashMapCollector(Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends U> valueMapper);

	void safeRun(RunnableThrowingException runnable);

	void unsafeRun(RunnableThrowingException runnable);

	<T> T safeGet(SupplierThrowingException<T> supplier);

	<T> T unsafeGet(SupplierThrowingException<T> supplier);

	Map<String, Function<Object, Object>> getGetterFunctions(Class<?> class1);

	Map<String, BiConsumer<Object, Object>> getSetterConsumers(Class<?> class1);

	@FunctionalInterface
	public interface RunnableThrowingException {

		void run() throws Exception;
	}

	@FunctionalInterface
	public interface SupplierThrowingException<T> {

		T get() throws Exception;
	}
}
