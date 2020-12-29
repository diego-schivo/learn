package com.backflipsource.helper;

@FunctionalInterface
public interface ConsumerThrowingException<T> {

	void accept(T t) throws Exception;
}