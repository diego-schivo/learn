package com.backflipsource.menagerie;

import javax.servlet.annotation.WebListener;

import com.backflipsource.Server;
import com.backflipsource.servlet.EntityContextListener;

@WebListener
public class App extends EntityContextListener {

	public App() {
		super("com.backflipsource.menagerie");
	}

	public static void main(String[] args) {
		new Server();
	}
}
