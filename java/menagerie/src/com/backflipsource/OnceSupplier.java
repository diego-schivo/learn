package com.backflipsource;

import java.util.function.Supplier;

public class OnceSupplier<T> implements Supplier<T> {

	protected Supplier<T> supplier;

	protected boolean supplied;

	protected T result;

	public OnceSupplier(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	@Override
	public T get() {
		if (!supplied) {
			result = supplier.get();
			supplied = true;
		}
		return result;
	}
}
