package com.backflipsource.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface EntityHandler {

	void handle(HttpServletRequest request, HttpServletResponse response);

	interface Result {

		Class<?> getView();

		Control getControl();

		String getForward();

		String getRedirect();
	}
}
