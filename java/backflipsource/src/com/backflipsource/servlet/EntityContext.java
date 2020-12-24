package com.backflipsource.servlet;

import static com.backflipsource.Helpers.camelCaseWords;
import static com.backflipsource.Helpers.joinStrings;
import static com.backflipsource.Helpers.safeStream;

import java.util.Stack;

import com.backflipsource.Helpers;

public class EntityContext {

	protected EntityServlet servlet;

	protected RequestHandler handler;

	protected Stack<Control> controls = new Stack<>();

	public EntityServlet getServlet() {
		return servlet;
	}

	public RequestHandler getHandler() {
		return handler;
	}

	public Stack<Control> getControls() {
		return controls;
	}

	public Control getControl() {
		if (controls.empty()) {
			return null;
		}
		return controls.peek();
	}

	public String text(AbstractControl.Factory<?> factory) {
		return joinStrings(safeStream(camelCaseWords(factory.getName())).map(Helpers::capitalizeString), " ");
	}

	public String text(Control control) {
		return control.getEntityView().getEntity().getSimpleName();
	}
}