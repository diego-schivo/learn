package com.backflipsource.servlet;

import static com.backflipsource.Helpers.forwardServletRequest;
import static java.util.logging.Logger.getLogger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SubpathHandler extends AbstractHandler {

	private static Logger logger = getLogger(SubpathHandler.class.getName());

	static {
		logger.setLevel(Level.ALL);

		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
	}

	public SubpathHandler(Class<?> class1) {
		super(class1);
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

	@Override
	public int vote(HttpServletRequest request) {
		return 0;
	}
}
