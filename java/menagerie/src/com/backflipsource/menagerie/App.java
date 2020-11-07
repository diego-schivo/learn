package com.backflipsource.menagerie;

import com.backflipsource.DefaultLangHelper;
import com.backflipsource.DefaultServletHelper;
import com.backflipsource.DefaultTomcatHelper;
import com.backflipsource.DefaultUtilHelper;
import com.backflipsource.Helpers;

public class App {

	public static void main(String[] args) {
		Helpers.langHelper = new DefaultLangHelper();
		Helpers.utilHelper = new DefaultUtilHelper();
		Helpers.servletHelper = new DefaultServletHelper();
		Helpers.tomcatHelper = new DefaultTomcatHelper();

		new Server();
	}
}
