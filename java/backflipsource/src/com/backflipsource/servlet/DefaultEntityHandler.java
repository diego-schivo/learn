package com.backflipsource.servlet;

import static java.util.logging.Logger.getLogger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultEntityHandler extends CompositeHttpHandler {

	private static Logger logger = getLogger(DefaultEntityHandler.class.getName());

	static {
		logger.setLevel(Level.ALL);

		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
	}

	public DefaultEntityHandler(Class<?> class1) {
		handlers.add(new SubpathHandler(class1));
		handlers.add(new BaseHandler(class1));
		handlers.add(new NewHandler(class1));
		handlers.add(new EditHandler(class1));
		handlers.add(new ShowHandler(class1));
	}
}