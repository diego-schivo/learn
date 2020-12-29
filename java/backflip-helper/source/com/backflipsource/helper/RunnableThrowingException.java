package com.backflipsource.helper;

@FunctionalInterface
public interface RunnableThrowingException {

	void run() throws Exception;
}