package com.backflipsource.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestHandler {

	void handle(HttpServletRequest request, HttpServletResponse response);

	interface Provider {

		RequestHandler provide(HttpServletRequest request);
	}
}
