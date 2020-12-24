package com.backflipsource.servlet;

import static com.backflipsource.Helpers.getClasses;
import static com.backflipsource.Helpers.linkedHashMapCollector;
import static com.backflipsource.Helpers.nonNullInstance;
import static com.backflipsource.Helpers.safeStream;
import static java.util.ResourceBundle.getBundle;
import static java.util.logging.Level.ALL;
import static java.util.logging.Logger.getLogger;
import static java.util.stream.Collectors.toCollection;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.ResourceBundle;
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

	public static EntityContextListener getInstance(String className) {
		return instances.get(className);
	}

	protected static Map<String, EntityContextListener> instances = new LinkedHashMap<>();

	private static Logger logger = logger(EntityContextListener.class, ALL);

	protected String packageName;

	protected ResourceBundle resourceBundle;

	protected Map<Class<?>, EntityView> views;

	protected Map<Class<?>, Dynamic> servlets;

	public EntityContextListener() {
		this(null, null);
	}

	public EntityContextListener(String packageName, String baseName) {
		instances.put(getClass().getName(), this);
		this.packageName = nonNullInstance(packageName, () -> getClass().getPackageName());
		resourceBundle = getBundle(nonNullInstance(baseName, () -> getClass().getName()));
	}

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public Map<Class<?>, EntityView> getViews() {
		return views;
	}

	public Map<Class<?>, Dynamic> getServlets() {
		return servlets;
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		Set<Class<?>> classes = getClasses(packageName)
				.filter(class1 -> class1.getAnnotationsByType(View.class).length > 0)
				.collect(toCollection(LinkedHashSet::new));
		logger.fine(() -> "classes " + classes);

		views = safeStream(classes).map(class1 -> {
			EntityView view = new DefaultEntityView(class1);
			return new Object[] { class1, view };
		}).collect(linkedHashMapCollector(objects -> (Class<?>) objects[0], objects -> (EntityView) objects[1]));
		logger.fine(() -> "views " + views);

		servlets = safeStream(classes).map(class1 -> {
			String servletName = class1.getName();
			Dynamic servlet = event.getServletContext().addServlet(servletName, EntityServlet.class);
			servlet.setInitParameter(EntityContextListener.class.getName(), getClass().getName());

			String urlPattern1 = views.get(class1).getUri();
			String urlPattern2 = urlPattern1 + "/*";
			servlet.addMapping(urlPattern1, urlPattern2);

			return new Object[] { class1, servlet };
		}).collect(linkedHashMapCollector(objects -> (Class<?>) objects[0], objects -> (Dynamic) objects[1]));
		logger.fine(() -> "servlets " + servlets);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		logger.fine("EntityContextListener contextDestroyed");
	}
}