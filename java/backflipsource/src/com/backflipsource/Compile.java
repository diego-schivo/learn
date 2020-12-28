package com.backflipsource;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Compile {

	public static void main(String[] args) {
		Path path = Paths.get(".").toAbsolutePath().normalize();
		System.out.println(path);
	}
}
