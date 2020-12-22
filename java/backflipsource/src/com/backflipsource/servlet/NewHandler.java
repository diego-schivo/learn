package com.backflipsource.servlet;

import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.Helpers.unsafeRun;
import static java.util.logging.Logger.getLogger;
import static java.util.regex.Pattern.compile;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NewHandler extends AbstractHandler {

	private static Logger logger = getLogger(NewHandler.class.getName());

	static {
		logger.setLevel(Level.ALL);

		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
	}

	protected Pattern pattern;

	public NewHandler(Class<?> class1) {
		super(class1);
		pattern = compile(entityView.getUri() + "/new");
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		Object item = unsafeGet(() -> class1.getDeclaredConstructor().newInstance());

		if ("post".equalsIgnoreCase(request.getMethod())) {
			save(item, request);
			unsafeRun(() -> response.sendRedirect((String) request.getAttribute("requestURI")));
			return;
		}

		render(new Form.Factory(entityView).control(item), View.Edit.class, request, response);
	}

	@Override
	public int vote(HttpServletRequest request) {
		String path = request.getRequestURI().substring(request.getContextPath().length());
		Matcher matcher = pattern.matcher(path);
		return matcher.matches() ? 1 : -1;
	}
}
