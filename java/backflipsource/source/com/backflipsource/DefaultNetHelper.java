package com.backflipsource;

import java.net.MalformedURLException;
import java.net.URL;

public class DefaultNetHelper implements NetHelper {

	@Override
	public URL url(String spec) {
		try {
			return new URL(spec);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
}
