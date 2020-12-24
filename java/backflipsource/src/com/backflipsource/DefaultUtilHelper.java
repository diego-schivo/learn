package com.backflipsource;

import static com.backflipsource.Helpers.getGetters;
import static com.backflipsource.Helpers.getSetters;
import static com.backflipsource.servlet.EntityContextListener.logger;
import static java.nio.file.Files.walkFileTree;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import static java.util.Collections.emptyList;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.FINER;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.StreamSupport.stream;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Spliterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class DefaultUtilHelper implements UtilHelper {

	private static Logger logger = logger(DefaultUtilHelper.class, FINE);

	@Override
	public <T> boolean emptyArray(T[] array) {
		if (array == null) {
			return true;
		}
		return array.length == 0;
	}

	@Override
	public boolean emptyCollection(Collection<?> collection) {
		if (collection == null) {
			return true;
		}
		return collection.size() == 0;
	}

	@Override
	public <T> T[] unsafeArray(Collection<T> collection, Class<T> class1) {
		if (collection == null) {
			return null;
		}
		return collection.stream().toArray(length -> (T[]) Array.newInstance(class1, length));
	}

	@Override
	public <T> T[] safeArray(Collection<T> collection, Class<T> class1) {
		T[] array = unsafeArray(collection, class1);
		if (array == null) {
			return (T[]) Array.newInstance(class1, 0);
		}
		return array;
	}

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
			logger.log(FINER, e.getMessage(), e);
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
			logger.log(FINER, e.getMessage(), e);
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

	@Override
	public Future<?>[] startExecutorService(ExecutorService executor, Callable<?>... tasks) {
		Future<?>[] futures = safeStream(tasks).map(task -> executor.submit(task)).toArray(Future<?>[]::new);
		executor.shutdown();
		return futures;
	}

	@Override
	public void stopExecutorService(ExecutorService executor, Future<?>... futures) {
		if (executor == null) {
			return;
		}
		safeStream(futures).forEach(item -> item.cancel(true));
		try {
			executor.awaitTermination(1, SECONDS);
		} catch (InterruptedException e) {
		}
		executor.shutdownNow();
	}

	@Override
	public void watchDirectories(Path start, Consumer<Path[]> consumer) {
		FileSystem fileSystem = FileSystems.getDefault();
		try (WatchService watchService = fileSystem.newWatchService()) {
			// out.println("path " + path.toAbsolutePath());

			SimpleFileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					dir.register(watchService, ENTRY_MODIFY);
					return FileVisitResult.CONTINUE;
				}
			};

			walkFileTree(start, visitor);

			for (;;) {
				WatchKey watchKey;
				try {
					watchKey = watchService.take();
				} catch (InterruptedException e) {
					break;
				}

				Path[] filenames = safeStream(watchKey.pollEvents()).filter(event -> event.kind() != OVERFLOW)
						.map(event -> ((WatchEvent<Path>) event).context()).toArray(Path[]::new);
				consumer.accept(filenames);

				boolean valid = watchKey.reset();
				if (!valid) {
					break;
				}
			}
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	@Override
	public <T> T arrayGet(T[] array, int index) {
		int length = (array != null) ? array.length : 0;
		if (index < 0 || index >= length) {
			return null;
		}
		return array[index];
	}

	@Override
	public <T> void arraySet(T[] array, int index, T value) {
		int length = (array != null) ? array.length : 0;
		if (index < 0 || index >= length) {
			return;
		}
		array[index] = value;
	}

	@Override
	public <T> T listGet(List<T> list, int index) {
		int size = (list != null) ? list.size() : 0;
		if (index < 0 || index >= size) {
			return null;
		}
		return list.get(index);
	}

	@Override
	public <T> T listSet(List<T> list, int index, T value) {
		int size = (list != null) ? list.size() : 0;
		if (index < 0 || index >= size) {
			return null;
		}
		return list.set(index, value);
	}
}
