package com.backflipsource;

import static java.lang.System.getenv;
import static java.lang.System.out;
import static java.nio.file.Files.createTempDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.WebResourceSet;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.EmptyResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import com.backflipsource.menagerie.Server;

public class DefaultTomcatHelper implements TomcatHelper {

	private static final String TOMCAT_BASE_DIR = "tomcat-base-dir";
	private static final String TOMCAT_DOC_BASE = "web";
	private static final String TOMCAT_DOC_BASE_DEFAULT = "tomcat-doc-base";
	private static final int TOMCAT_PORT_DEFAULT = 8080;
	private static final String WEB_APP_MOUNT = "/WEB-INF/classes";

	@Override
	public void initTomcat(Tomcat tomcat, String root, String classes) {
		Path tempPath;
		try {
			tempPath = createTempDirectory(TOMCAT_BASE_DIR);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		tomcat.setBaseDir(tempPath.toString());

		int port;
		try {
			port = Integer.valueOf(getenv("PORT"));
		} catch (NumberFormatException e) {
			port = TOMCAT_PORT_DEFAULT;
		}
		tomcat.setPort(port);

		File rootDir = new File(Paths.get(root).toAbsolutePath().toString());
		String rootPath = rootDir.getAbsolutePath();
		out.println("rootPath " + rootPath);

		File docBaseDir = new File(rootPath, TOMCAT_DOC_BASE);
		if (!docBaseDir.exists()) {
			try {
				docBaseDir = createTempDirectory(TOMCAT_DOC_BASE_DEFAULT).toFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		String docBasePath = docBaseDir.getAbsolutePath();
		out.println("docBasePath " + docBasePath);

		StandardContext context = (StandardContext) tomcat.addWebapp("", docBasePath);
		context.setParentClassLoader(Server.class.getClassLoader());

		WebResourceRoot resourceRoot = new StandardRoot(context);
		File classesDir = new File(rootPath, classes);
		String classesPath = classesDir.getAbsolutePath();
		out.println("classesPath " + classesPath);
		WebResourceSet resourceSet;
		if (classesDir.exists()) {
			resourceSet = new DirResourceSet(resourceRoot, WEB_APP_MOUNT, classesPath, "/");
		} else {
			resourceSet = new EmptyResourceSet(resourceRoot);
		}
		resourceRoot.addPreResources(resourceSet);
		context.setResources(resourceRoot);
	}
}
