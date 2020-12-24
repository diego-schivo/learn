package com.backflipsource.servlet;

import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.unsafeGet;
import static com.backflipsource.servlet.EntityContextListener.logger;
import static java.util.Comparator.comparingInt;
import static java.util.Map.entry;
import static java.util.logging.Level.ALL;
import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.backflipsource.Helpers;
import com.backflipsource.servlet.RequestMatcher.Regex;

public class DefaultRequestHandlerProvider implements RequestHandler.Provider {

	private static Logger logger = logger(DefaultRequestHandlerProvider.class, ALL);

	protected Map<RequestMatcher, RequestHandler> handlers;

	public DefaultRequestHandlerProvider(EntityView entityView) {
		String[] packageNames = { "com.backflipsource.servlet", "com.backflipsource.petclinic" };
		handlers = Arrays.stream(packageNames).flatMap(Helpers::getClasses)
				.filter(class2 -> class2.getAnnotation(Controller.class) != null).collect(toMap(class2 -> {
					Controller annotation = class2.getAnnotation(Controller.class);
					RequestMatcher matcher = new Regex(entityView.getEntity(), annotation.score(), annotation.regex());
					return matcher;
				}, class2 -> {
					RequestHandler handler = (RequestHandler) unsafeGet(
							() -> class2.getDeclaredConstructor(EntityView.class).newInstance(entityView));
					return handler;
				}));
	}

	@Override
	public RequestHandler provide(HttpServletRequest request) {
		RequestHandler handler = safeStream(handlers).map(entry -> {
			RequestMatcher matcher = entry.getKey();
			int match = matcher.match(request);
			if (match < 0) {
				return null;
			}
			Entry<Integer, RequestHandler> entry2 = entry(match, entry.getValue());
			return entry2;
		}).filter(Objects::nonNull).max(comparingInt(entry -> entry.getKey())).map(Entry::getValue).orElse(null);
		logger.fine(() -> "handler " + handler);
		return handler;
	}
}