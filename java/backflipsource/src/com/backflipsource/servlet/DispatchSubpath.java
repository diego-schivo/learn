package com.backflipsource.servlet;

import static com.backflipsource.Helpers.forwardServletRequest;
import static com.backflipsource.servlet.EntityContextListener.logger;
import static java.util.logging.Level.ALL;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller(regex = "/_uri_/.+")
public class DispatchSubpath extends EntityRequestHandler {

	private static Logger logger = logger(DispatchSubpath.class, ALL);

	public DispatchSubpath(EntityView entityView) {
		super(entityView);
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		String path = request.getRequestURI().substring(request.getContextPath().length());
		String substring = path.substring(entityView.getUri().length());
		if (idField != null) {
			substring = substring.substring(substring.indexOf('/', 1));
		}

		String path2 = substring;
		logger.fine(() -> "path2 " + path2);

		forwardServletRequest(path2, request, response);
	}
}
