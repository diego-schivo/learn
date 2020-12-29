package com.backflipsource;

@FunctionalInterface
public interface FunctionThrowingException<T, R> {
	R apply(T t) throws Exception;
}