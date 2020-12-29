package com.backflipsource.ui;

import static com.backflipsource.ui.EntityServlet.CONTEXT;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.backflipsource.Control;

public class ControlFactoriesTag extends SimpleTagSupport {

	protected String var;

	public void setVar(String var) {
		this.var = var;
	}

	@Override
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		ServletRequest request = pageContext.getRequest();
		DefaultEntityContext context = (DefaultEntityContext) request.getAttribute(CONTEXT);

		AbstractEntityControl control = (AbstractEntityControl) context.getControl();
		Collection<Control.Factory<?>> factories = control.getResource().controlFactories(control.getMode()).values();

		JspWriter out = pageContext.getOut();
		Object object = request.getAttribute(var);
		try {
			for (Control.Factory<?> factory : factories) {
				request.setAttribute(var, factory);
				getJspBody().invoke(out);
			}
		} finally {
			request.setAttribute(var, object);
		}
	}
}
