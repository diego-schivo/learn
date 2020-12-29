package com.backflipsource.ui;

import static com.backflipsource.helper.Helper.camelCaseWords;
import static com.backflipsource.helper.Helper.joinStrings;
import static com.backflipsource.helper.Helper.safeGet;

import java.util.Stack;

import com.backflipsource.Control;
import com.backflipsource.RequestHandler;
import com.backflipsource.helper.Helper;
import com.backflipsource.ui.spec.EntityResource;
import com.backflipsource.ui.spec.EntityUI;

public class DefaultEntityContext implements EntityUI.Context {

	protected EntityServlet servlet;

	protected RequestHandler handler;

	protected Stack<Control> controls = new Stack<>();

	@Override
	public EntityResource getResource() {
		return servlet.getResource();
	}

	@Override
	public RequestHandler getHandler() {
		return handler;
	}

	public Stack<Control> getControls() {
		return controls;
	}

	@Override
	public Control getControl() {
		if (controls.empty()) {
			return null;
		}
		return controls.peek();
	}

	public String text(AbstractEntityControl.Factory<?> factory) {
		return joinStrings(camelCaseWords(factory.getName()).map(Helper::capitalizeString), " ");
	}

	public String text(Control control) {
		return safeGet(() -> ((AbstractEntityControl) control).getResource().getEntity().getName());
	}
}