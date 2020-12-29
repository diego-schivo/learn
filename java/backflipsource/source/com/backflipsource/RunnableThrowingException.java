package com.backflipsource;

@FunctionalInterface
public interface RunnableThrowingException {

	void run() throws Exception;
}