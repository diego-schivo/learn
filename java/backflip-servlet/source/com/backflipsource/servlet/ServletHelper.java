package com.backflipsource.servlet;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public interface ServletHelper {

	void forwardServletRequest(String path, ServletRequest request, ServletResponse response);
}
