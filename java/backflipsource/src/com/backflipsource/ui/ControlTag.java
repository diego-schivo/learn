package com.backflipsource.ui;

import static com.backflipsource.ui.EntityServlet.CONTEXT;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.backflipsource.Control;

public class ControlTag extends SimpleTagSupport {

	protected String var;

	protected Control control;

	public void setVar(String var) {
		this.var = var;
	}

	public void setControl(Control control) {
		this.control = control;
	}

	@Override
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		ServletRequest request = pageContext.getRequest();
		DefaultEntityContext context = (DefaultEntityContext) request.getAttribute(CONTEXT);

		Control control = this.control;
		if (control != null) {
			control.init();
			context.getControls().push(control);
		}

		Object object = request.getAttribute(var);
		JspWriter out = pageContext.getOut();
		try {
			request.setAttribute(var, context.getControls().peek());
			getJspBody().invoke(out);
		} finally {
			request.setAttribute(var, object);
			if (control != null) {
				context.getControls().pop();
			}
		}
	}
}
