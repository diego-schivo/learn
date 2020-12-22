package com.backflipsource.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HttpHandler {

	void handle(HttpServletRequest request, HttpServletResponse response);
}
