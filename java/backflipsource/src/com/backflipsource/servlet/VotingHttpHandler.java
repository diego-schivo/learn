package com.backflipsource.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface VotingHttpHandler extends HttpHandler {

	int vote(HttpServletRequest request);
}
