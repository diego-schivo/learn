package com.backflipsource;

import org.apache.catalina.startup.Tomcat;

public class StaticTomcatHelper {

	private static TomcatHelper instance;

	public static TomcatHelper getInstance() {
		if (instance == null) {
			instance = new DefaultTomcatHelper();
		}
		return instance;
	}

	public static void initTomcat(Tomcat tomcat, int port, String root, String classes) {
		getInstance().initTomcat(tomcat, port, root, classes);
	}
}
