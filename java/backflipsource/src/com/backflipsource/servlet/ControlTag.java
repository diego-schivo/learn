package com.backflipsource.servlet;

import static com.backflipsource.servlet.EntityServlet.CONTEXT;

import java.util.Stack;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

@SuppressWarnings("serial")
public class ControlTag extends TagSupport {

	protected String var;

	protected Control control;

	protected Control control2;

	protected Object object;

	public void setVar(String var) {
		this.var = var;
	}

	public void setControl(Control control) {
		this.control = control;
	}

	@Override
	public int doStartTag() throws JspException {
		Control control = this.control;
		Stack<Control> stack = stack();
		if (control != null) {
			stack.push(control);
		}
		control2 = stack.peek();
		object = pageContext.getRequest().getAttribute(var);
		pageContext.getRequest().setAttribute(var, control2);
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		pageContext.getRequest().setAttribute(var, object);
		if (control != null) {
			stack().pop();
		}
		return EVAL_PAGE;
	}

	@Override
	public void release() {
		super.release();
		var = null;
		control = null;
		control2 = null;
		object = null;
	}

	protected Stack<Control> stack() {
		return ((EntityContext) pageContext.getRequest().getAttribute(CONTEXT)).getControls();
	}
}
