package com.backflipsource.helper;

@FunctionalInterface
public interface SupplierThrowingException<T> {

	T get() throws Exception;
}