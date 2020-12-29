package com.backflipsource.ui;

import static com.backflipsource.helper.Helper.emptyArray;
import static com.backflipsource.helper.Helper.emptyString;
import static com.backflipsource.helper.Helper.linkedHashMapCollector;
import static com.backflipsource.helper.Helper.listGet;
import static com.backflipsource.helper.Helper.logger;
import static com.backflipsource.helper.Helper.safeStream;
import static com.backflipsource.helper.Helper.unsafeGet;
import static java.util.Comparator.comparingInt;
import static java.util.Map.entry;
import static java.util.logging.Level.ALL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.backflipsource.RequestHandler;
import com.backflipsource.RequestMatcher;
import com.backflipsource.RequestMatcher.And;
import com.backflipsource.RequestMatcher.Parameter;
import com.backflipsource.RequestMatcher.Regex;
import com.backflipsource.ui.spec.EntityResource;
import com.backflipsource.ui.spec.EntityUI;

public class DefaultRequestHandlerProvider implements RequestHandler.Provider {

	private static Logger logger = logger(DefaultRequestHandlerProvider.class, ALL);

	protected Map<RequestMatcher, RequestHandler> handlers;

	public DefaultRequestHandlerProvider(EntityResource resource, EntityUI ui) {
		handlers = safeStream(ui.getControllers()).collect(linkedHashMapCollector(class1 -> {
			Entity.Controller controller = class1.getAnnotation(Entity.Controller.class);
			List<RequestMatcher> matchers = new ArrayList<>();
			if (!emptyString(controller.regex())) {
				matchers.add(new Regex(controller.regex().replace("_uri_", resource.getUri()), controller.score()));
			}
			if (!emptyArray(controller.parameter())) {
				matchers.add(new Parameter(controller.parameter()[0], controller.score()));
			}
			return (matchers.size() >= 2) ? new And(matchers) : listGet(matchers, 0);
		}, class1 -> {
			RequestHandler handler = unsafeGet(
					() -> (RequestHandler) class1.getConstructor().newInstance());
			((EntityRequestHandler) handler).setResource(resource);
			return handler;
		}));
	}

	@Override
	public RequestHandler provide(HttpServletRequest request) {
		RequestHandler handler = safeStream(handlers).map(entry -> {
			RequestMatcher matcher = entry.getKey();
			RequestHandler handler2 = entry.getValue();
			logger.fine(() -> "handler2 = " + handler2);

			int match = matcher.match(request);
			if (match < 0) {
				return null;
			}

			Entry<Integer, RequestHandler> entry2 = entry(match, handler2);
			return entry2;
		}).filter(Objects::nonNull).max(comparingInt(entry -> entry.getKey())).map(Entry::getValue).orElse(null);
		logger.fine(() -> "handler " + handler);
		return handler;
	}
}