package com.backflipsource;

import static com.backflipsource.Helpers.getGetters;
import static com.backflipsource.Helpers.getSetters;
import static java.util.Collections.emptyList;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.StreamSupport.stream;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class DefaultUtilHelper implements UtilHelper {

	@Override
	public <T> List<T> safeList(T[] array) {
		List<T> list = unsafeList(array);
		if (list == null) {
			return emptyList();
		}
		return list;
	}

	@Override
	public <T> List<T> unsafeList(T[] array) {
		if (array == null) {
			return null;
		}
		return Arrays.asList(array);
	}

	@Override
	public <T> Stream<T> safeStream(T[] array) {
		Stream<T> stream = unsafeStream(array);
		if (stream == null) {
			return Stream.empty();
		}
		return stream;
	}

	@Override
	public <T> Stream<T> unsafeStream(T[] array) {
		if (array == null) {
			return null;
		}
		return Arrays.stream(array);
	}

	@Override
	public <T> Stream<T> safeStream(Collection<T> collection) {
		Stream<T> stream = unsafeStream(collection);
		if (stream == null) {
			return Stream.empty();
		}
		return stream;
	}

	@Override
	public <T> Stream<T> unsafeStream(Collection<T> collection) {
		if (collection == null) {
			return null;
		}
		return collection.stream();
	}

	@Override
	public <T> Stream<T> safeStream(Enumeration<T> enumeration) {
		Stream<T> stream = unsafeStream(enumeration);
		if (stream == null) {
			return Stream.empty();
		}
		return stream;
	}

	@Override
	public <T> Stream<T> unsafeStream(Enumeration<T> enumeration) {
		if (enumeration == null) {
			return null;
		}
		return stream(spliteratorUnknownSize(enumeration.asIterator(), Spliterator.ORDERED), false);
	}

	@Override
	public <K, V> Stream<Entry<K, V>> safeStream(Map<K, V> map) {
		Stream<Entry<K, V>> stream = unsafeStream(map);
		if (stream == null) {
			return Stream.empty();
		}
		return stream;
	}

	@Override
	public <K, V> Stream<Entry<K, V>> unsafeStream(Map<K, V> map) {
		if (map == null) {
			return null;
		}
		return safeStream(map.entrySet());
	}

	@Override
	public <T, K, U> Collector<T, ?, Map<K, U>> linkedHashMapCollector(Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends U> valueMapper) {
		return toMap(keyMapper, valueMapper, ((first, second) -> first), LinkedHashMap::new);
	}

	@Override
	public void safeRun(RunnableThrowingException runnable) {
		try {
			runnable.run();
		} catch (Exception e) {
		}
	}

	@Override
	public void unsafeRun(RunnableThrowingException runnable) {
		try {
			runnable.run();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T safeGet(SupplierThrowingException<T> supplier) {
		try {
			T result = supplier.get();
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public <T> T unsafeGet(SupplierThrowingException<T> supplier) {
		try {
			T result = supplier.get();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Map<String, Function<Object, Object>> getGetterFunctions(Class<?> class1) {
		Map<String, Method> getters = getGetters(class1);
		return safeStream(getters).collect(linkedHashMapCollector(Entry::getKey, entry -> {
			Method getter = entry.getValue();
			Function<Object, Object> function = (object) -> {
				Object value = safeGet(() -> getter.invoke(object));
				return value;
			};
			return function;
		}));
	}

	@Override
	public Map<String, BiConsumer<Object, Object>> getSetterConsumers(Class<?> class1) {
		Map<String, Method> setters = getSetters(class1);
		return safeStream(setters).collect(linkedHashMapCollector(Entry::getKey, entry -> {
			Method setter = entry.getValue();
			BiConsumer<Object, Object> consumer = (object, value) -> {
				safeRun(() -> setter.invoke(object, value));
			};
			return consumer;
		}));
	}
}
