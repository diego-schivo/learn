package com.backflipsource.ui;

import static com.backflipsource.ui.EntityServlet.CONTEXT;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ContextTag extends SimpleTagSupport {

	protected String var;

	public void setVar(String var) {
		this.var = var;
	}

	@Override
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		ServletRequest request = pageContext.getRequest();
		DefaultEntityContext context = (DefaultEntityContext) request.getAttribute(CONTEXT);

		JspWriter out = pageContext.getOut();
		Object object = request.getAttribute(var);
		try {
			request.setAttribute(var, context);
			getJspBody().invoke(out);
		} finally {
			request.setAttribute(var, object);
		}
	}
}
