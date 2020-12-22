package com.backflipsource.servlet;

import static com.backflipsource.Helpers.safeStream;
import static java.util.Comparator.comparingInt;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CompositeHttpHandler implements HttpHandler {

	protected Set<VotingHttpHandler> handlers = new LinkedHashSet<>();

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		HttpHandler handler = safeStream(handlers).max(comparingInt(item -> item.vote(request))).orElse(null);
		handler.handle(request, response);
	}
}
