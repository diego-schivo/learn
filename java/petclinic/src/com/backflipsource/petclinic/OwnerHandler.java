package com.backflipsource.petclinic;

import static com.backflipsource.Helpers.emptyString;

import java.util.List;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;

import com.backflipsource.servlet.DefaultEntityHandler;
import com.backflipsource.servlet.EntityHandler;
import com.backflipsource.servlet.Table;
import com.backflipsource.servlet.View;

public class OwnerHandler extends DefaultEntityHandler {

	public OwnerHandler() {
		super(Owner.class);
	}

	@Override
	protected EntityHandler.Result handleMatch(Matcher matcher, HttpServletRequest request) {
		boolean find = "/find".equals(matcher.group(1));
		if (find) {
			return find(request);
		}

		return super.handleMatch(matcher, request);
	}

	protected EntityHandler.Result find(HttpServletRequest request) {
		Result result = new Result();
		result.setView(View.Edit.class);
		result.setControl(new OwnerFind.Factory(entityView).control(null));
		return result;
	}

	@Override
	protected EntityHandler.Result base(HttpServletRequest request) {
		String lastName = request.getParameter("lastName");
		if (emptyString(lastName)) {
			return super.base(request);
		}
		Result result = new Result();
		List<Owner> find = ((Owner.Data) entityData).find(lastName);
		if (find.size() == 1) {
			result.setRedirect(request.getAttribute("requestURI") + "/" + find.get(0).getId());
		} else {
			result.setView(View.List.class);
			result.setControl(new Table.Factory(entityView).control(find));
		}
		return result;
	}
}
