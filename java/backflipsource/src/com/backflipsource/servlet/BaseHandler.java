package com.backflipsource.servlet;

import static java.util.logging.Logger.getLogger;
import static java.util.regex.Pattern.compile;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseHandler extends AbstractHandler {

	private static Logger logger = getLogger(BaseHandler.class.getName());

	static {
		logger.setLevel(Level.ALL);

		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
	}

	protected Pattern pattern;

	public BaseHandler(Class<?> class1) {
		super(class1);
		pattern = compile(entityView.getUri());
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
		Control control;
		if (entityData != null) {
			control = new Table.Factory(entityView).control(entityData.list());
		} else if (item0 != null) {
			control = new DescriptionList.Factory(entityView).control(item0);
		} else {
			control = null;
		}

		Class<?> view = (item0 != null) ? View.Show.class : View.List.class;

		render(control, view, request, response);
	}

	@Override
	public int vote(HttpServletRequest request) {
		String path = request.getRequestURI().substring(request.getContextPath().length());
		Matcher matcher = pattern.matcher(path);
		return matcher.matches() ? 1 : -1;
	}
}
