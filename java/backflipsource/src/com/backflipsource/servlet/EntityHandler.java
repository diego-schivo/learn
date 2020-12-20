package com.backflipsource.servlet;

import javax.servlet.http.HttpServletRequest;

public interface EntityHandler {

	Result handle(HttpServletRequest request);

	interface Result {

		String getPath();

		Class<?> getView();

		Control getControl();
	}
}
