package com.backflipsource;

@FunctionalInterface
public interface SupplierThrowingException<T> {

	T get() throws Exception;
}