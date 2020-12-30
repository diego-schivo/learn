package com.backflipsource.tomcat;

import org.apache.catalina.startup.Tomcat;

public interface TomcatHelper {

	void initTomcat(Tomcat tomcat, int port, String root, String classes);
}
