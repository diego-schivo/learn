package com.backflipsource.ui;

import static com.backflipsource.ui.EntityServlet.initialRequest;

import com.backflipsource.Control;

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
		protected void init(Control control) {
			Anchor anchor = (Anchor) control;

			String value = control.getValue();
			anchor.setHref(initialRequest().getRequestURI() + "/" + value);
			anchor.setText(value);
		}
	}
}
