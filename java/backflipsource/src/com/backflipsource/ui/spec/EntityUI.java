package com.backflipsource.ui.spec;

import static com.backflipsource.Helpers.safeStream;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;

import javax.servlet.ServletRegistration.Dynamic;

import com.backflipsource.Control;
import com.backflipsource.RequestHandler;

public interface EntityUI {

	Set<Class<?>> getEntities();

	Set<Class<? extends Mode>> getModes();

	Map<Class<?>, EntityResource> getResources();

	Map<Class<?>, Dynamic> getServlets();

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
