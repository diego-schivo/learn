package com.backflipsource.helper;

import java.net.URL;

public class StaticNetHelper {

	private static NetHelper instance;

	public static NetHelper getInstance() {
		if (instance == null)
			instance = new DefaultNetHelper();
		return instance;
	}

	public static URL url(String spec) {
		return getInstance().url(spec);
	}

}
