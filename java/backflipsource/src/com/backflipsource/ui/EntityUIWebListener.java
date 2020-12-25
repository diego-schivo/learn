package com.backflipsource.ui;

import static com.backflipsource.Helpers.linkedHashSetCollector;

import java.util.Set;
import java.util.stream.Stream;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.backflipsource.ui.spec.EntityUI;

public abstract class EntityUIWebListener implements ServletContextListener {

	protected EntityUI entityUI;

	protected ServletContext servletContext;

	public EntityUI getEntityUI() {
		if (entityUI == null) {
			entityUI = newEntityUI();
		}
		return entityUI;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		servletContext = event.getServletContext();
		getEntityUI().getServlets();
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		servletContext = null;
	}

	protected EntityUI newEntityUI() {
		Set<String> packages = Stream.of(DefaultEntityUI.class, getClass()).map(Class::getPackageName)
				.collect(linkedHashSetCollector());
		String baseName = getClass().getName();
		return new DefaultEntityUI(this::getServletContext, packages, baseName);
	}
}
