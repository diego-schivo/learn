package com.backflipsource.ui;

import static com.backflipsource.Helpers.linkedHashMapCollector;
import static com.backflipsource.Helpers.linkedHashSetCollector;
import static com.backflipsource.Helpers.logger;
import static com.backflipsource.Helpers.nonNullInstance;
import static com.backflipsource.Helpers.safeStream;
import static java.util.ResourceBundle.getBundle;
import static java.util.function.Function.identity;
import static java.util.logging.Level.ALL;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration.Dynamic;

import com.backflipsource.Helpers;
import com.backflipsource.RequestHandler;
import com.backflipsource.servlet.EntityServlet;
import com.backflipsource.ui.Entity.Controller;
import com.backflipsource.ui.spec.EntityResource;
import com.backflipsource.ui.spec.EntityUI;

public class DefaultEntityUI implements EntityUI {

	public static Map<Class<? extends EntityUI>, EntityUI> instances = new LinkedHashMap<>();

	private static Logger logger = logger(DefaultEntityUI.class, ALL);

	static {
		GETTERS.add(instances::get);
	}

	protected Supplier<ServletContext> servletContext;

	protected Set<String> packages;

	protected Set<Class<?>> entities;

	protected Set<Class<? extends Mode>> modes;

	protected Map<Class<?>, EntityResource> resources;

	protected Map<Class<?>, Dynamic> servlets;

	protected Set<Class<? extends RequestHandler>> controllers;

	protected ResourceBundle resourceBundle;

	protected DefaultEntityUI(Supplier<ServletContext> servletContext) {
		this(servletContext, null, null);
	}

	public DefaultEntityUI(Supplier<ServletContext> servletContext, Set<String> packages, String baseName) {
		this.servletContext = servletContext;

		this.packages = nonNullInstance(packages, () -> Stream.of(DefaultEntityUI.class, getClass())
				.map(Class::getPackageName).collect(linkedHashSetCollector()));

		resourceBundle = getBundle(nonNullInstance(baseName, () -> getClass().getName()));

		instances.put(getClass(), this);
	}

	@Override
	public Set<Class<?>> getEntities() {
		if (entities == null) {
			entities = safeStream(packages).flatMap(Helpers::getClasses)
					.filter(class1 -> class1.getAnnotation(Entity.class) != null).collect(linkedHashSetCollector());
			logger.fine(() -> "entities " + entities);
		}
		return entities;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<Class<? extends Mode>> getModes() {
		if (modes == null) {
			modes = safeStream(packages).flatMap(Helpers::getClasses)
					.filter(class1 -> Mode.class.isAssignableFrom(class1)).map(class1 -> (Class<? extends Mode>) class1)
					.collect(linkedHashSetCollector());
			logger.fine(() -> "modes " + modes);
		}
		return modes;
	}

	@Override
	public Map<Class<?>, EntityResource> getResources() {
		if (resources == null) {
			resources = safeStream(getEntities())
					.collect(linkedHashMapCollector(identity(), class1 -> new DefaultEntityResource(class1, this)));
			logger.fine(() -> "resources " + resources);
		}
		return resources;
	}

	@Override
	public Map<Class<?>, Dynamic> getServlets() {
		if (servlets == null) {
			ServletContext servletContext2 = servletContext.get();
			servlets = safeStream(getEntities()).collect(linkedHashMapCollector(identity(), entity -> {
				String servletName = entity.getName();

				Dynamic servlet = servletContext2.addServlet(servletName, EntityServlet.class);
				servlet.setInitParameter(EntityUI.class.getName(), getClass().getName());

				String urlPattern1 = getResources().get(entity).getUri();
				String urlPattern2 = urlPattern1 + "/*";
				servlet.addMapping(urlPattern1, urlPattern2);

				return servlet;
			}));
			logger.fine(() -> "servlets " + servlets);
		}
		return servlets;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<Class<? extends RequestHandler>> getControllers() {
		if (controllers == null) {
			controllers = safeStream(packages).flatMap(Helpers::getClasses)
					.filter(class1 -> class1.getAnnotation(Controller.class) != null)
					.map(class1 -> (Class<? extends RequestHandler>) class1).collect(linkedHashSetCollector());
			logger.fine(() -> "controllers " + controllers);
		}
		return controllers;
	}

	@Override
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
}
