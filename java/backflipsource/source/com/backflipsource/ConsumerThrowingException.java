package com.backflipsource;

@FunctionalInterface
public interface ConsumerThrowingException<T> {

	void accept(T t) throws Exception;
}