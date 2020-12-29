package com.backflipsource;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class StaticServletHelper {

	private static ServletHelper instance;

	public static ServletHelper getInstance() {
		if (instance == null) {
			instance = new DefaultServletHelper();
		}
		return instance;
	}

	public static void forwardServletRequest(String path, ServletRequest request, ServletResponse response) {
		getInstance().forwardServletRequest(path, request, response);
	}
}
