package com.backflipsource.ui;

public class Form extends AbstractEntityControl {

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
