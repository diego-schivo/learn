package com.backflipsource.tomcat;

import org.apache.catalina.startup.Tomcat;

public class Helper {

	public static void initTomcat(Tomcat tomcat, int port, String root, String classes) {
		StaticTomcatHelper.initTomcat(tomcat, port, root, classes);
	}

}
