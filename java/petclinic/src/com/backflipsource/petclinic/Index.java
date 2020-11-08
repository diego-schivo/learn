package com.backflipsource.petclinic;

import static com.backflipsource.Helpers.forwardServletRequest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/index.html")
@SuppressWarnings({ "serial" })
public class Index extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("servlets", App.getServlets());
		forwardServletRequest("/index.jsp", request, response);
	}
}
