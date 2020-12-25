package com.backflipsource.servlet;

import static com.backflipsource.servlet.EntityServlet.CONTEXT;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.backflipsource.ui.spec.EntityUI;

@SuppressWarnings("serial")
public class ContextTag extends TagSupport {

	protected String var;

	protected Object object;

	public void setVar(String var) {
		this.var = var;
	}

	@Override
	public int doStartTag() throws JspException {
		object = pageContext.getRequest().getAttribute(var);
		pageContext.getRequest().setAttribute(var,
				((EntityUI.Context) pageContext.getRequest().getAttribute(CONTEXT)));
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		pageContext.getRequest().setAttribute(var, object);
		return EVAL_PAGE;
	}

	@Override
	public void release() {
		super.release();
		var = null;
		object = null;
	}
}
