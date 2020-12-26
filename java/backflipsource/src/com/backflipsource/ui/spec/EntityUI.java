package com.backflipsource.ui.spec;

import static com.backflipsource.Helpers.safeStream;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;

import javax.servlet.ServletRegistration;

import com.backflipsource.Control;
import com.backflipsource.RequestHandler;
import com.backflipsource.dynamic.DynamicClass;

public interface EntityUI {

	Map<String, DynamicClass> getEntities();

	Set<Class<? extends Mode>> getModes();

	Map<String, EntityResource> getResources();

	Map<String, ServletRegistration.Dynamic> getServlets();

	Set<Class<? extends RequestHandler>> getControllers();

	ResourceBundle getResourceBundle();

	static Set<Function<Class<?>, EntityUI>> GETTERS = new LinkedHashSet<>();

	static EntityUI entityUI(Class<?> uiClass) {
		EntityUI ui = safeStream(GETTERS).map(getter -> getter.apply(uiClass)).filter(Objects::nonNull).findFirst()
				.orElse(null);
		return ui;
	}

	interface Mode {
	}

	interface Context {

		EntityResource getResource();

		RequestHandler getHandler();

		Control getControl();
	}
}
