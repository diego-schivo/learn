package com.backflipsource.servlet;

import static com.backflipsource.helper.Helper.unsafeRun;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class DefaultServletHelper implements ServletHelper {

	@Override
	public void forwardServletRequest(String path, ServletRequest request, ServletResponse response) {
		unsafeRun(() -> request.getRequestDispatcher(path).forward(request, response));
	}
}
