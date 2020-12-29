package com.backflipsource.servlet;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class Helper {

	public static void forwardServletRequest(String path, ServletRequest request, ServletResponse response) {
		StaticServletHelper.forwardServletRequest(path, request, response);
	}

}
