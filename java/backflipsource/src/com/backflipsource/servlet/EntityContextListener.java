package com.backflipsource.servlet;

import static com.backflipsource.Helpers.getClasses;
import static com.backflipsource.Helpers.linkedHashMapCollector;
import static com.backflipsource.Helpers.safeStream;
import static java.util.logging.Level.ALL;
import static java.util.logging.Logger.getLogger;
import static java.util.stream.Collectors.toCollection;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration.Dynamic;

public class EntityContextListener implements ServletContextListener {

	public static Logger logger(Class<?> class1, Level level) {
		Logger logger = getLogger(class1.getName());

		ConsoleHandler handler = new ConsoleHandler();
		logger.addHandler(handler);

		logger.setLevel(level);
		handler.setLevel(level);

		return logger;
	}

	private static Logger logger = logger(EntityContextListener.class, ALL);

	protected String packageName;

	protected static Map<String, EntityView> views;

	protected static Map<String, Dynamic> servlets;

	public EntityContextListener(String packageName) {
		this.packageName = packageName;
	}

	public static Map<String, EntityView> getViews() {
		return views;
	}

	public static Map<String, Dynamic> getServlets() {
		return servlets;
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		logger.fine("EntityContextListener contextInitialized");

		Set<Class<?>> classes = getClasses(packageName)
				.filter(class1 -> class1.getAnnotationsByType(View.class).length > 0)
				.collect(toCollection(LinkedHashSet::new));
		logger.fine(() -> "classes " + classes);

		views = safeStream(classes).map(class1 -> {
			EntityView view = new DefaultEntityView(class1);
			return new Object[] { class1.getName(), view };
		}).collect(linkedHashMapCollector(objects -> (String) objects[0], objects -> (EntityView) objects[1]));
		logger.fine(() -> "views " + views);

		servlets = safeStream(views).map(entry -> {
			String servletName = entry.getKey();
			Dynamic servlet = event.getServletContext().addServlet(servletName, EntityServlet.class);

			String urlPattern1 = entry.getValue().getUri();
			String urlPattern2 = urlPattern1 + "/*";
			servlet.addMapping(urlPattern1, urlPattern2);

			return new Object[] { servletName, servlet };
		}).collect(linkedHashMapCollector(objects -> (String) objects[0], objects -> (Dynamic) objects[1]));
		logger.fine(() -> "servlets " + servlets);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		logger.fine("EntityContextListener contextDestroyed");
	}
}