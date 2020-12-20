package com.backflipsource.servlet;

import static com.backflipsource.servlet.EntityServlet.CONTROL_STACK;

import java.util.Stack;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

@SuppressWarnings("serial")
public class ControlTag extends TagSupport {

	protected String var;

	protected Control control;

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
		if (control == null) {
			control = stack.peek();
		} else {
			stack.push(control);
		}
		object = pageContext.getRequest().getAttribute(var);
		pageContext.getRequest().setAttribute(var, control);
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		pageContext.getRequest().setAttribute(var, object);
		stack().pop();
		return EVAL_PAGE;
	}

	@Override
	public void release() {
		super.release();
		var = null;
		control = null;
		object = null;
	}

	@SuppressWarnings("unchecked")
	protected Stack<Control> stack() {
		return (Stack<Control>) pageContext.getRequest().getAttribute(CONTROL_STACK);
	}
}
