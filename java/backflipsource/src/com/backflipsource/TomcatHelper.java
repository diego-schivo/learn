package com.backflipsource;

import org.apache.catalina.startup.Tomcat;

public interface TomcatHelper {

	void initTomcat(Tomcat tomcat, String root, String classes);
}
