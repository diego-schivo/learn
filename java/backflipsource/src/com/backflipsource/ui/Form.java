package com.backflipsource.ui;

import com.backflipsource.ui.Form.Factory;

public class Form extends AbstractEntityControl<Factory> {

	protected String action;

	protected String method;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public static class Factory extends AbstractEntityControl.Factory<Form> {

		public Factory() {
			setControl(Form.class);
		}
	}
}
