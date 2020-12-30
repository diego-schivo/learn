package com.backflipsource.helper;

import java.net.URL;
import java.nio.file.Path;
import java.util.function.Consumer;

public class StaticNioHelper {

	private static NioHelper instance;

	public static NioHelper getInstance() {
		if (instance == null)
			instance = new DefaultNioHelper();
		return instance;
	}

	public static void acceptDirectoryEntries(Path directory, String glob, Consumer<Path> consumer) {
		getInstance().acceptDirectoryEntries(directory, glob, consumer);
	}

	public static void extractArchive(Path archive, Path directory) {
		getInstance().extractArchive(archive, directory);
	}

	public static void downloadFile(URL url, Path directory) {
		getInstance().downloadFile(url, directory);
	}

}
