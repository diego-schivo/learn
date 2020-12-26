package com.backflipsource.ui;

import static com.backflipsource.servlet.EntityServlet.getInitialRequest;

public class Anchor extends AbstractEntityControl {

	protected String href;

	protected String text;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public static class Factory extends AbstractEntityControl.Factory<Anchor> {

		public Factory() {
			setControl(Anchor.class);
		}

		@Override
		protected void init(Anchor control) {
			String value = control.getValue();
			control.setHref(getInitialRequest().getRequestURI() + "/" + value);
			control.setText(value);
		}
	}
}
