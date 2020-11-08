package com.backflipsource.servlet;

import static com.backflipsource.Helpers.getClasses;
import static com.backflipsource.Helpers.safeStream;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration.Dynamic;

public class EntityContextListener implements ServletContextListener {

	protected static Map<String, Dynamic> servlets;

	public static Map<String, Dynamic> getServlets() {
		return servlets;
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		String packageName = "com.backflipsource";
		Set<Class<?>> classes = getClasses(packageName)
				.filter(class1 -> class1.getAnnotationsByType(View.class).length > 0).collect(toSet());
		servlets = safeStream(classes).map(class1 -> {
			String servletName = class1.getName();
			Dynamic servlet = event.getServletContext().addServlet(servletName, EntityServlet.class);

			String urlPattern1 = "/" + class1.getSimpleName().toLowerCase();
			String urlPattern2 = urlPattern1 + "/*";
			servlet.addMapping(urlPattern1, urlPattern2);

			return new Object[] { servletName, servlet };
		}).collect(toMap(objects -> (String) objects[0], objects -> (Dynamic) objects[1]));
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}
}