package com.backflipsource.servlet;

import static com.backflipsource.Helpers.safeStream;
import static java.util.Comparator.comparingInt;
import static java.util.logging.Logger.getLogger;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

public class DefaultEntityHandler implements HttpHandler.Resolver {

	private static Logger logger = getLogger(DefaultEntityHandler.class.getName());

	static {
		logger.setLevel(Level.ALL);

		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
	}

	protected Set<VotingHttpHandler> handlers = new LinkedHashSet<>();

	public DefaultEntityHandler(Class<?> class1) {
		handlers.add(new SubpathHandler(class1));
		handlers.add(new BaseHandler(class1));
		handlers.add(new NewHandler(class1));
		handlers.add(new EditHandler(class1));
		handlers.add(new ShowHandler(class1));
	}

	@Override
	public HttpHandler handler(HttpServletRequest request) {
		HttpHandler handler = safeStream(handlers).max(comparingInt(item -> item.vote(request))).orElse(null);
		return handler;
	}
}