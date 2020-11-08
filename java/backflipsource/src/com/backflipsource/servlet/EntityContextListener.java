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
		Set<Class<?>> classes = getClasses(packageName)
				.filter(class1 -> class1.getAnnotationsByType(View.class).length > 0).collect(toSet());

		views = safeStream(classes).map(class1 -> {
			EntityView view = new EntityView(class1);
			return new Object[] { class1.getName(), view };
		}).collect(toMap(objects -> (String) objects[0], objects -> (EntityView) objects[1]));

		servlets = safeStream(views).map(entry -> {
			String servletName = entry.getKey();
			Dynamic servlet = event.getServletContext().addServlet(servletName, EntityServlet.class);

			String urlPattern1 = entry.getValue().getUri();
			String urlPattern2 = urlPattern1 + "/*";
			servlet.addMapping(urlPattern1, urlPattern2);

			return new Object[] { servletName, servlet };
		}).collect(toMap(objects -> (String) objects[0], objects -> (Dynamic) objects[1]));
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}
}